package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class SellFood extends RunningActivity implements BusinessSecondaryActivity {
	private final static int COSTMEAL = 20;
	private final static int PROFITMEAL = 12;
	
	private MessageData messageData;
	
	@Override
	public void perform() {
		
		int amountEarned = 0;
		for (Customer customer : getCustomers()) {
			customer.addToSatisfaction(getCharacter().getFinalValue(SpecializationAttribute.COOKING) / 5, this);
			amountEarned += PROFITMEAL;
			int tips = (customer.getMoney() / 200) + Util.getInt(1, 4) + getCharacter().getFinalValue(SpecializationAttribute.COOKING) / 10;
			
			customer.payFixed(COSTMEAL);
			tips = customer.pay(tips, getCharacter().getMoneyModifier());
			
			amountEarned +=  tips;
		}
		modifyIncome(amountEarned);
		
		messageData.addToMessage("\n\n" + TextUtil.t("sellfood.result", getCharacter(), getCustomers().size(), getIncome()));
	}
	
	@Override
	public MessageData getBaseMessage() {
		String messageText = TextUtil.t("sellfood.basic", getCharacter());
		this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, getCharacter()), 
				getBackground());
		return this.messageData;
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 0.5f, SpecializationAttribute.COOKING));	
		if(getCharacter().getTraits().contains(Trait.RESTAURATEUR))
			modifications.add(new ModificationData(TargetType.ALL, 0.3f, EssentialAttributes.MOTIVATION));
		else
			modifications.add(new ModificationData(TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
		return modifications;
	}
	
	@Override
	public int getAppeal() {
		return 1 + getCharacter().getFinalValue(SpecializationAttribute.COOKING);
	}
	
	@Override
	public int getMaxAttendees() {
		return 5 + getCharacter().getFinalValue(SpecializationAttribute.COOKING) / 2;
	}
}