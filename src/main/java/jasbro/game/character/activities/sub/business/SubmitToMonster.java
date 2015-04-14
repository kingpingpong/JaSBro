package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Idle;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class SubmitToMonster extends RunningActivity implements BusinessMainActivity, BusinessSecondaryActivity {
	private ImageData monsterImage;
	private MessageData messageData;
	
	@Override
    public MessageData getBaseMessage() {
		if (getMainCustomer() != null) {
			List<ImageTag> tags = getCharacter().getBaseTags();
			tags.add(0, ImageTag.MONSTER);
			tags.add(ImageTag.FORCED);

			monsterImage = ImageUtil.getInstance().getImageDataByTags(tags, getCharacter().getImages());
			String monsterString = "";
			int rand=Util.getInt(1, 7);
			if(rand==1){monsterString = TextUtil.t("submitToMonster.bigmonster");}
			if(rand==2){monsterString = TextUtil.t("submitToMonster.strongmonster");}
			if(rand==3){monsterString = TextUtil.t("submitToMonster.weakmonster");}
			if(rand==4){monsterString = TextUtil.t("submitToMonster.giantmonster");}
			if(rand==5){monsterString = TextUtil.t("submitToMonster.slimymonster");}
			if(rand==6){monsterString = TextUtil.t("submitToMonster.smallmonster");}
			if(rand==7){monsterString = TextUtil.t("submitToMonster.hornymonster");}
			String message;
			Object arguments[] = {getMainCustomer().getName(), monsterString, getCustomers().size()};
			if(getCharacter().getFinalValue(Sextype.MONSTER)+getCharacter().getFinalValue(BaseAttributeTypes.OBEDIENCE)*6<300){
				 message = TextUtil.t("submitToMonster.basic", getCharacter(), arguments);
			} else{
				 message = TextUtil.t("submitToMonster.experienced", getCharacter(), arguments);
			}
			messageData = new MessageData(message, monsterImage, getBackground());
		}
		else {
			messageData = new MessageData(TextUtil.t("submitToMonster.nocustomer", getCharacter()), monsterImage, 
					getBackground());
		}
        return messageData;
    }
    
    @Override
    public List<ModificationData> getStatModifications() {
    	List<ModificationData> modifications = new ArrayList<ModificationData>();
    	if (getMainCustomer() != null) {
            modifications.add(new ModificationData(TargetType.ALL, -40, EssentialAttributes.ENERGY));
            modifications.add(new ModificationData(TargetType.ALL, -35, EssentialAttributes.HEALTH));
            modifications.add(new ModificationData(TargetType.SLAVE, 0.01f, BaseAttributeTypes.OBEDIENCE));
            modifications.add(new ModificationData(TargetType.TRAINER, -0.5f, BaseAttributeTypes.COMMAND));
            modifications.add(new ModificationData(TargetType.ALL, 0.025f, BaseAttributeTypes.STAMINA));
            modifications.add(new ModificationData(TargetType.ALL, 4f, Sextype.MONSTER));
    	}
    	else {
    		return new Idle().getStatModifications();
    	}
    	return modifications;
    }
    
    @Override
    public void perform() {
    	if (getMainCustomer() != null) {
    	    setSextype(Sextype.MONSTER);
	    	for (Customer customer : getCustomers()) {
	    		customer.addToSatisfaction(5, this);
	    	}
	    	
	    	getMainCustomer().addToSatisfaction(200 + getCharacter().getFinalValue(Sextype.MONSTER) * 4, this);
	    	int pay=0;
	    	if(getMainCustomer().getType()==CustomerType.GROUP){
				CustomerGroup group=(CustomerGroup)getMainCustomer();
	        	
				 pay = getMainCustomer().pay(getCharacter().getMoneyModifier()*group.getCustomers().size()/1.8f);
			}
			else{
				 pay = getMainCustomer().pay(getCharacter().getMoneyModifier());
			}
	    	
	    	modifyIncome(pay);
	    	messageData.addToMessage("\n\n" + TextUtil.t("submitToMonster.result", getCharacter(), pay));   
    	}
    }
	
	@Override
	public int rateCustomer(Customer customer) {
		if (customer.getMoney() > 500) {
			return customer.getMoney();
		}
		else {
			return 0;
		}
	}
	
	@Override
	public int getAppeal() {
		return Util.getInt(1, 7);
	}

	@Override
	public int getMaxAttendees() {
		return 40;
	}
	
}
