package jasbro.game.world.customContent;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.Unit;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.items.Inventory.ItemData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class RapeEvent extends RunningActivity {
	private int amountActions = 1;

	@Override
	public void perform() {
		Charakter character = getCharacter();
		Customer customer = getMainCustomer();

		if ((!character.getSpecializations().contains(SpecializationType.FIGHTER)) || character.getHealth() < 40) {
			getAttributeModifications().add(new AttributeModification(-15, EssentialAttributes.HEALTH, character));
			getAttributeModifications().add(new AttributeModification(-20, EssentialAttributes.ENERGY, character));
			amountActions = 2;

			String message = TextUtil.t("whore.rape", character, customer);
			List<ImageTag> tags = character.getBaseTags();
			tags.add(0, ImageTag.FORCED);
			tags.addAll(ImageTag.getAssociatedImageTags(character, customer));
			ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, character.getImages());
			MessageData messageData = new MessageData(message, image, getPlannedActivity().getSource().getImage());
			getMessages().remove(0);
			getMessages().add(0, messageData);
		} else {
			Unit fighter1 = character;
			Unit fighter2 = customer;
			int i = 0;
			int startHitPoints1 = fighter1.getHitpoints();
			int startHitPoints2 = fighter2.getHitpoints();
			Battle battle = new Battle(fighter1, fighter2);
			do {
			    battle.doRound();
				i++;
			} while (i < 1000 && fighter1.getHitpoints() > startHitPoints1 - 50 && fighter2.getHitpoints() > 10);
	        getMessages().get(0).addToMessage("\n\n" + battle.getCombatText());
            float diff1 = fighter1.getHitpoints() - startHitPoints1;
            float diff2 = fighter2.getHitpoints() - startHitPoints2;
			
			List<AttributeModification> modifications = new ArrayList<AttributeModification>();
			AttributeModification attributeModification = new AttributeModification(0, EssentialAttributes.HEALTH, character);
			attributeModification.setRealModification(diff1);
			modifications.add(attributeModification);
			modifications.add(new AttributeModification(-10, EssentialAttributes.ENERGY, character));
			modifications.add(new AttributeModification(0.1f, BaseAttributeTypes.STRENGTH, character));
			modifications.add(new AttributeModification(0.05f, BaseAttributeTypes.STAMINA, character));
			modifications.add(new AttributeModification(0.4f, SpecializationAttribute.VETERAN, character));
			MessageData messageData = new MessageData();
			ImageTag imageTag;

			if (fighter1.getHitpoints() == 0) {
				return;
			} else if (fighter2.getHitpoints() > 0 && diff1 < diff2) {
				modifications.add(new AttributeModification(-5, EssentialAttributes.ENERGY, character));
				if (Util.getRnd().nextBoolean() && ImageUtil.getInstance().tagExists(ImageTag.HURT, character.getImages())) {
					imageTag = ImageTag.HURT;
				} else {
					imageTag = ImageTag.FORCED;
				}
				messageData.addToMessage(TextUtil.t("whore.rape.defeat", character, customer));
				amountActions++;
			} else if (fighter2.getHitpoints() < 50) {
				modifications.add(new AttributeModification(0.4f, SpecializationAttribute.VETERAN, character));
				Jasbro.getInstance().getData().earnMoney(customer.getMoney(), TextUtil.t("whore.rape.moneySource"));
				imageTag = ImageTag.VICTORIOUS;
				
				if (character.getItemLootChanceModifier() > Util.getInt(0, 100)) {
				    List<ItemData> loot = customer.spawnItems();
				    if (loot.size() > 0) {
				        Jasbro.getInstance().getData().getInventory().addItems(loot);
		                Object arguments[] = { TextUtil.listItems(loot), customer.getMoney() };
		                messageData.addToMessage(TextUtil.t("whore.rape.victory.loot", character, customer, arguments));
				    }
				    else {
		                Object arguments[] = { customer.getMoney() };
                        messageData.addToMessage(TextUtil.t("whore.rape.victory", character, customer, arguments));
				    }
				}
                else {
                    Object arguments[] = { customer.getMoney() };
                    messageData.addToMessage(TextUtil.t("whore.rape.victory", character, customer, arguments));
                }
			} else {
				modifications.add(new AttributeModification(0.2f, SpecializationAttribute.VETERAN, character));
				messageData.addToMessage(TextUtil.t("whore.rape.foughtOff", character, customer));
				imageTag = ImageTag.VICTORIOUS;
			}

			getAttributeModifications().addAll(modifications);

			List<ImageTag> tags = character.getBaseTags();
			tags.add(0, imageTag);
			tags.addAll(ImageTag.getAssociatedImageTags(character, customer));
			messageData.setImage(ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()));
			messageData.setBackground(character.getBackground());
			getAttributeModifications().addAll(modifications);
			getMessages().add(messageData);
		}
	}

	@Override
	public MessageData getBaseMessage() {
		Charakter character = getCharacter();
		Customer customer = getMainCustomer();

		List<ImageTag> tags = character.getBaseTags();
		tags.add(0, ImageTag.FIGHT);
		tags.addAll(ImageTag.getAssociatedImageTags(character, customer));
		ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, character.getImages());
		return new MessageData(TextUtil.t("whore.rape.attemptedrape", character, customer), image, character.getBackground());
	}

	public int getAmountActions() {
		return amountActions;
	}
}
