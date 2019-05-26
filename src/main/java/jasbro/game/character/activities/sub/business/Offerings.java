package jasbro.game.character.activities.sub.business;

import java.util.ArrayList;
import java.util.List;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.items.Inventory.ItemData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Offerings extends RunningActivity implements BusinessSecondaryActivity {

	private MessageData messageData;
	private int bonus;

	@Override
	public void perform() {
		Charakter character = getCharacter();
		int skill = Util.getInt(0, 5)*character.getCharisma()/8;
		if(skill>20)
			skill=20;

		int amountEarned = 0;
		int amountHappy = 0;
		int overalltips = 0;
		for (Customer customer : getCustomers()) {
			List<ItemData> loot = new ArrayList<ItemData>();
			if (Util.getInt(0, 100) < skill + customer.getInitialSatisfaction() / 5) {
				ItemData itemStolen = customer.getItem();
				if (itemStolen != null) {
					loot.add(itemStolen);
				}
			}
			if (loot.size() > 0) {
				Jasbro.getInstance().getData().getInventory().addItems(loot);

				Object arguments[] = { TextUtil.listItems(loot) };
				this.getMessages().get(0).addToMessage("\n"+TextUtil.t("offerings.loot", character, arguments));

			}

			if (Util.getInt(0, 50) + skill + customer.getSatisfactionAmount() > 50) {
				amountHappy++;
				customer.addToSatisfaction(skill, this);
				int tips = 0;
				switch(customer.getType()){
				case PEASANT:
					tips=10;
					break;
				case SOLDIER:
					tips=20;
					break;
				case MERCHANT:
					tips=40;
					break;
				case BUSINESSMAN:
					tips=80;
					break;
				case MINORNOBLE:
					tips=160;
					break;
				case LORD:
					tips=320;
					break;
				case CELEBRITY:
					tips=640;
					break;
				default:
					tips=5;
					break;
				}
				tips = customer.pay(tips, getCharacter().getMoneyModifier());
				overalltips += tips;
				amountEarned +=  tips;
			}
			else {
				customer.addToSatisfaction(skill / 4, this);
			}
		}
		modifyIncome(amountEarned);

		if (amountEarned > 0) {
			messageData.addToMessage("\n\n" + TextUtil.t("offerings.result", getCharacter(), getCustomers().size(), amountHappy, getIncome(), overalltips));
		}
	}

	@Override
	public MessageData getBaseMessage() {
		String messageText = TextUtil.t("offerings.basic", getCharacter());
		this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter()), 
				getBackground());
		return this.messageData;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, -15, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 0.05f, BaseAttributeTypes.CHARISMA));
		modifications.add(new ModificationData(TargetType.SLAVE, 0.02f, BaseAttributeTypes.OBEDIENCE));
		modifications.add(new ModificationData(TargetType.TRAINER, 0.02f, BaseAttributeTypes.COMMAND));
		modifications.add(new ModificationData(TargetType.ALL, 0.8f, EssentialAttributes.MOTIVATION));
		return modifications;
	}

	@Override
	public int getAppeal() {
		return Util.getInt(1, 30);
	}

	@Override
	public int getMaxAttendees() {
		return 15;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

}