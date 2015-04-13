package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Strip extends RunningActivity implements BusinessSecondaryActivity {
	
	private MessageData messageData;
	private int bonus;
	
	@Override
	public void perform() {
		Charakter character = getCharacter();
		int skill = character.getCharisma() + character.getFinalValue(SpecializationAttribute.STRIP) / 4 + 1;
		
        int amountEarned = 0;
        int amountHappy = 0;
        int overalltips = 0;
		for (Customer customer : getCustomers()) {
			if (Util.getInt(0, 50) + skill + customer.getSatisfactionAmount() > 50) {
				amountHappy++;
				customer.addToSatisfaction(skill, this);
				int tips = (int)(customer.getMoney() / (1500.0 / skill) + Util.getInt(10, 20));
				tips = customer.pay(tips, getCharacter().getMoneyModifier());
				overalltips += tips;
				amountEarned +=  tips;
			}
			else {
				customer.addToSatisfaction(skill / 4, this);
			}
		}
		modifyIncome(amountEarned);
		
    	Object arguments[] = {getCustomers().size(), amountHappy, getIncome(), overalltips};
    	if (amountEarned > 0) {
    	    messageData.addToMessage("\n\n" + TextUtil.t("strip.result.owned", getCharacter(), arguments));
    	}
    	else {
    	    messageData.addToMessage("\n\n" + TextUtil.t("strip.result.basic", getCharacter(), arguments));    		
    	}
	}

	@Override
	public MessageData getBaseMessage() {
    	String messageText = TextUtil.t("strip.basic", getCharacter());
        this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, getCharacter()), 
        		getBackground());
        return this.messageData;
	}
	
    @Override
    public List<ModificationData> getStatModifications() {
    	List<ModificationData> modifications = new ArrayList<ModificationData>();
        modifications.add(new ModificationData(TargetType.ALL, -30, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.ALL, 1.1f, SpecializationAttribute.STRIP));
        modifications.add(new ModificationData(TargetType.ALL, 0.02f, BaseAttributeTypes.STRENGTH));
        modifications.add(new ModificationData(TargetType.ALL, 0.02f, BaseAttributeTypes.STAMINA));
        modifications.add(new ModificationData(TargetType.ALL, 0.05f, BaseAttributeTypes.CHARISMA));
        modifications.add(new ModificationData(TargetType.SLAVE, 0.02f, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new ModificationData(TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
    	return modifications;
    }

	@Override
	public int getAppeal() {
		return (getCharacter().getCharisma() + getCharacter().getFinalValue(SpecializationAttribute.STRIP) / 4) / 4;
	}

	@Override
	public int getMaxAttendees() {
		return 20+bonus;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

}
