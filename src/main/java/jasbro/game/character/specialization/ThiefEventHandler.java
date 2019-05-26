package jasbro.game.character.specialization;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.MyCharacterEventListener;
import jasbro.game.items.Inventory.ItemData;
import jasbro.game.items.ItemType;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class ThiefEventHandler implements MyCharacterEventListener {
	
	public void handleEvent(MyEvent e, Charakter character) {
		if (e.getType() == EventType.ACTIVITYPERFORMED) {
			RunningActivity activity = (RunningActivity) e.getSource();
			
			if (activity.getType().isCustomerDependent() && activity.getType() != ActivityType.SUBMITTOMONSTER
					&& activity.getType() != ActivityType.TEASE && activity.getType() != ActivityType.PUBLICUSE) {
				int stealChance = character.getStealChance();
				int stealAmount = character.getStealAmountModifier();
				int stealItemChance = character.getStealItemChance();
				
				if (activity.getMainCustomer() != null) { // Character attempts to steal money from main customer
					List<Customer> customers = activity.getMainCustomers();
					
					int amountStolen = 0;
					activity.getMessages().get(0).addToMessage("\n");
					for (Customer customer : customers) {
						ItemData stolenItem = null;
						if (Util.getInt(0, 100) < stealItemChance) {
							stolenItem = customer.getItem();
							if (stolenItem != null) {
								if (character.getTraits().contains(Trait.RESELLER) && stolenItem.getItem().getType() != ItemType.UNLOCK) {
									Jasbro.getInstance().getData().earnMoney(stolenItem.getItem().getValue() / 2, stolenItem);
								} else {
									Jasbro.getInstance().getData().getInventory().addItems(stolenItem.getItem(), stolenItem.getAmount());
									
								}
							}
						}
						
						int actualStealChance = stealChance + customer.getInitialSatisfaction() / 5; // TODO improve / change?
						if (Util.getInt(0, 100) < actualStealChance) {
							amountStolen += customer.payFixed((int) (stealAmount * 10 + customer.getMoney() * stealAmount / 100f));
							if (amountStolen > 0) {
								Jasbro.getInstance().getData().earnMoney(amountStolen, TextUtil.t("pickpocket.statSource", character));
								if (stolenItem == null) {
									Object arguments[] = { amountStolen };
									if (customer.getMoney() <= 0) {
										activity.getMessages().get(0)
										.addToMessage(TextUtil.t("pickpocket.mainCustomer.all", character, customer, arguments));
									} else {
										activity.getMessages().get(0)
										.addToMessage(TextUtil.t("pickpocket.mainCustomer", character, customer, arguments));
									}
								} else {
									Object arguments[] = { stolenItem, amountStolen };
									if (customer.getMoney() <= 0) {
										activity.getMessages()
										.get(0)
										.addToMessage(
												TextUtil.t("pickpocket.mainCustomer.item.all", character, customer, arguments));
									} else {
										activity.getMessages().get(0)
										.addToMessage(TextUtil.t("pickpocket.mainCustomer.item", character, customer, arguments));
									}
								}
								
							} else {
								if (stolenItem == null) {
									activity.getMessages().get(0)
									.addToMessage(TextUtil.t("pickpocket.mainCustomer.noMoney", character, customer));
								} else {
									Object arguments[] = { stolenItem };
									activity.getMessages().get(0).addToMessage(
											TextUtil.t("pickpocket.mainCustomer.item.noMoney", character, customer, arguments));
								}
							}
							if (activity instanceof Whore) {
								activity.getAttributeModifications().add(
										new AttributeModification(0.2f, SpecializationAttribute.PICKPOCKETING, character));
							} else {
								activity.getAttributeModifications().add(
										new AttributeModification(0.8f, SpecializationAttribute.PICKPOCKETING, character));
							}
							
						} else { // money steal unsuccessful
							if (stolenItem != null) {
								Object arguments[] = { stolenItem };
								activity.getMessages().get(0)
								.addToMessage(TextUtil.t("pickpocket.mainCustomer.itemOnly", character, customer, arguments));
							}
							
							if (activity instanceof Whore) {
								activity.getAttributeModifications().add(
										new AttributeModification(0.05f, SpecializationAttribute.PICKPOCKETING, character));
							} else {
								activity.getAttributeModifications().add(
										new AttributeModification(0.2f, SpecializationAttribute.PICKPOCKETING, character));
							}
						}
					}
					
				} else if (activity.getCustomers().size() != 0 && activity.getType() != ActivityType.SUBMITTOMONSTER && activity.getType() != ActivityType.PUBLICUSE) {// secondary activities
					activity.getMessages().get(0).addToMessage("\n");
					stealItemChance = stealItemChance / 5; // TODO steal items
					List<ItemData> loot = new ArrayList<ItemData>();
					int amountStolen = 0;
					int amountPeople = 0;
					for (Customer customer : activity.getCustomers()) {
						if (Util.getInt(0, 100) < stealChance) {
							amountStolen += customer.pay((int) (Util.getInt(1, 6) + customer.getImportance() * stealAmount / 100f), 1f);
							amountPeople++;
						}
						if (Util.getInt(0, 100) < stealItemChance + customer.getInitialSatisfaction() / 5) {
							ItemData itemStolen = customer.getItem();
							if (itemStolen != null) {
								loot.add(itemStolen);

							}
						}
					}
					if (loot.size() > 0) {
						if (character.getTraits().contains(Trait.RESELLER)) {
							for (int i = 0; i < loot.size(); i++) {
								Jasbro.getInstance().getData().earnMoney(loot.get(i).getItem().getValue() / 2, loot.get(i));
							}
						} else {
							Jasbro.getInstance().getData().getInventory().addItems(loot);
							
						}
						Object arguments[] = { TextUtil.listItems(loot), amountStolen };
						Jasbro.getInstance().getData().earnMoney(amountStolen, TextUtil.t("pickpocket.statSource", character));
						activity.getMessages().get(0)
						.addToMessage(TextUtil.t("pickpocket.secondaryCustomersPlusLoot", character, arguments));
						activity.getAttributeModifications().add(
								new AttributeModification(1f, SpecializationAttribute.PICKPOCKETING, character));
					} else if (amountStolen > 0) {
						Object arguments[] = { amountPeople, amountStolen };
						Jasbro.getInstance().getData().earnMoney(amountStolen, TextUtil.t("pickpocket.statSource", character));
						activity.getMessages().get(0).addToMessage(TextUtil.t("pickpocket.secondaryCustomers", character, arguments));
						activity.getAttributeModifications().add(
								new AttributeModification(0.8f, SpecializationAttribute.PICKPOCKETING, character));
					} else {
						activity.getAttributeModifications().add(
								new AttributeModification(0.05f, SpecializationAttribute.PICKPOCKETING, character));
					}
				}
			}
		}
	}
	
}