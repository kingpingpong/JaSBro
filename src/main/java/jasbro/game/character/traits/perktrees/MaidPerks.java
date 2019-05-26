package jasbro.game.character.traits.perktrees;

import java.util.ArrayList;
import java.util.List;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Clean;
import jasbro.game.character.activities.sub.Cook;
import jasbro.game.character.activities.sub.business.SellFood;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.events.business.SpawnData.CustomerData;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.texts.TextUtil;

public class MaidPerks {

	//Cooking and cleaning counts as sleeping.
		public static class TimeManipulation extends TraitEffect {
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof Clean) {
						//Reset the NoSleep counter
						character.getCounter().reset(CounterNames.NOSLEEP.toString());
						character.getCounter().add(CounterNames.NOSLEEP.toString(), -1l);
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("MAID.nap",character));
						for (AttributeModification attributeModification : activity.getAttributeModifications()) {
							if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
								//Change energy loss to an energy gain.
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification)*2;
								attributeModification.addModificator(change);
							}
						}
					}
					if (activity instanceof Cook) {
						character.getCounter().reset(CounterNames.NOSLEEP.toString());
						character.getCounter().add(CounterNames.NOSLEEP.toString(), -1l);
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("MAID.nap",character));
						for (AttributeModification attributeModification : activity.getAttributeModifications()) {
							if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification)*2;
								attributeModification.addModificator(change);
							}
						}
					}
				}
			}
		}
		
		//Cleans no matter what activity is being performed.
		public static class CompulsiveCleaner extends TraitEffect {
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					House house = activity.getHouse();
					if (house != null) {
						house.modDirt(-5-character.getFinalValue(SpecializationAttribute.CLEANING)/10);
					}
				}
			}
		}
		//Cooking increases everyone's Char and Sta
		public static class DietExpert extends TraitEffect {
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof Cook) {
						House house = activity.getHouse();
						if (house != null) {
							for (Room room : house.getRooms()) {
								for(Charakter target : room.getCurrentUsage().getCharacters()){
									activity.getAttributeModifications().add(new AttributeModification(0.02f, BaseAttributeTypes.CHARISMA, target));
									activity.getAttributeModifications().add(new AttributeModification(0.02f, BaseAttributeTypes.STAMINA, target));
								} 
							}
						}				
					}
				}
			}
		}
		//Cooking may give the "Well Fed" buff
		public static class CordonBleu extends TraitEffect {
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof Cook) {
						House house = activity.getHouse();
						if (house != null) {
							int skill = 10 + character.getFinalValue(SpecializationAttribute.COOKING) / 4;
							if (skill > 50) {
								skill = 50;
							}
							for (Room room : house.getRooms()) {
								for (Charakter target : room.getCurrentUsage().getCharacters()) {
									if(Util.getInt(0, 3)==2){
										target.addCondition(new Buff.Satiated(skill,target));
										activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("MAID.cordonbleu",character, target));
									}
								}
							}
						}
					}
				}
			}
		}
		
		//Cleaning may give the "All dolled up" buff
		public static class WashingAndIroning extends TraitEffect {
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof Clean) {
						House house = activity.getHouse();
						if (house != null) {
							int skill = 10+character.getFinalValue(SpecializationAttribute.CLEANING)/4;
							if (skill > 50) {
								skill = 50;
							}
							for (Room room : house.getRooms()) {
								for (Charakter target : room.getCurrentUsage().getCharacters()) {
									if(Util.getInt(0, 3)==2){
										target.addCondition(new Buff.AllDolledUp(skill, target));
										activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("MAID.dolledup",character, target));
									}
								}
							}
						}
					}
				}
			}
		}
		//Maid is more effective
		public static class Professional extends TraitEffect{
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof Clean) {
						Clean cleanActivity = (Clean) activity;
						cleanActivity.setDirtModification(cleanActivity.getDirtModification() -10);
					}
					if (activity instanceof Cook) {
						House house = activity.getHouse();
						if (house != null) {
							for (Room room : house.getRooms()) {
								for(Charakter target : room.getCurrentUsage().getCharacters()){
									activity.getAttributeModifications().add(new AttributeModification(10.0f, EssentialAttributes.ENERGY, target));
									activity.getAttributeModifications().add(new AttributeModification(2.0f, EssentialAttributes.HEALTH, target));
								} 
							}
						}
					}
				}
			}
		}
		//Cooking and cleaning may increase Int
		public static class HouseFairy extends TraitEffect{
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof Clean || activity instanceof Cook) {
						activity.getAttributeModifications().add(new AttributeModification(0.07f, 
								BaseAttributeTypes.INTELLIGENCE, character));
					}
				}
			}
		}
		//Cooking and cleaning generates bonus customers
		public static class Elegant extends TraitEffect{
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof Clean) {
						List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
						int skill=character.getFinalValue(SpecializationAttribute.CLEANING)+character.getFinalValue(BaseAttributeTypes.CHARISMA);
						skill/=3;
						bonusCustomers.add(new CustomerData(CustomerType.CELEBRITY, skill/30));
						bonusCustomers.add(new CustomerData(CustomerType.LORD, skill/25));
						bonusCustomers.add(new CustomerData(CustomerType.MINORNOBLE, skill/22));
						bonusCustomers.add(new CustomerData(CustomerType.GROUP, skill/18));
						bonusCustomers.add(new CustomerData(CustomerType.BUSINESSMAN, skill/20));
						bonusCustomers.add(new CustomerData(CustomerType.MERCHANT, skill/16));
						bonusCustomers.add(new CustomerData(CustomerType.SOLDIER, skill/14));
						bonusCustomers.add(new CustomerData(CustomerType.PEASANT, skill/12));
						bonusCustomers.add(new CustomerData(CustomerType.BUM, skill/10));
						if (activity.getHouse() != null) {
							SpawnData spawnData = activity.getHouse().getSpawnData();
							for (CustomerData bonusCustData : bonusCustomers) {
								spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
							}
						}
					}
					if (activity instanceof Cook) {
						List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
						int skill=character.getFinalValue(SpecializationAttribute.COOKING)+character.getFinalValue(BaseAttributeTypes.CHARISMA);
						skill/=3;
						bonusCustomers.add(new CustomerData(CustomerType.CELEBRITY, skill/30));
						bonusCustomers.add(new CustomerData(CustomerType.LORD, skill/25));
						bonusCustomers.add(new CustomerData(CustomerType.MINORNOBLE, skill/22));
						bonusCustomers.add(new CustomerData(CustomerType.GROUP, skill/18));
						bonusCustomers.add(new CustomerData(CustomerType.BUSINESSMAN, skill/20));
						bonusCustomers.add(new CustomerData(CustomerType.MERCHANT, skill/16));
						bonusCustomers.add(new CustomerData(CustomerType.SOLDIER, skill/14));
						bonusCustomers.add(new CustomerData(CustomerType.PEASANT, skill/12));
						bonusCustomers.add(new CustomerData(CustomerType.BUM, skill/10));
						if (activity.getHouse() != null) {
							SpawnData spawnData = activity.getHouse().getSpawnData();
							for (CustomerData bonusCustData : bonusCustomers) {
								spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
							}
						}
					}
				}
			}
		}
		//Cooking is more effective
		public static class MotherlyCare extends TraitEffect{
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof Cook) {
						House house = activity.getHouse();
						if (house != null) {
							for (Room room : house.getRooms()) {
								for(Charakter target : room.getCurrentUsage().getCharacters()){
									activity.getAttributeModifications().add(new AttributeModification(7.0f, EssentialAttributes.ENERGY, target));
									activity.getAttributeModifications().add(new AttributeModification(12.0f, EssentialAttributes.HEALTH, target));
								}
							}
						}
					}
				}
			}
		}
		
		public static class CulinaryDelights extends TraitEffect{
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof Cook) {
						House house = activity.getHouse();
						if (house != null) {
							for (Room room : house.getRooms()) {
								for(Charakter target : room.getCurrentUsage().getCharacters()){
									float modification = 0.1f + character.getAttribute(SpecializationAttribute.COOKING).getValue() / 150.0f;
									if(modification >= 1){
										modification = 1;
									}
									activity.getAttributeModifications().add(new AttributeModification(modification, EssentialAttributes.MOTIVATION, target));
								}
							}
						}
					}
				}
			}
		}

		//Cooking earns more fame
		public static class Chef extends TraitEffect {
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof SellFood) {
						activity.setFameModifier(activity.getFameModifier( ) + 0.5f);
						for(Customer customer : activity.getCustomers()){
							if(customer.getStatus()==CustomerStatus.TIRED){
								customer.setStatus(CustomerStatus.LIVELY);
							}
						}
					}

				}
			}
		}
		//Max Cooking may increase by itself
		public static class AlwaysImprove extends TraitEffect {
			@Override
			public void handleEvent(MyEvent e, Charakter character, Trait trait) {
				if (e.getType() == EventType.ACTIVITY) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof SellFood && Util.getInt(0, 100)>10) {
						Attribute attribute = character.getAttribute(SpecializationAttribute.COOKING);
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("MAID.improvecooking",character));
						attribute.setMaxValue(attribute.getMaxValue() + 1);
						activity.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
					}
					if (activity instanceof Cook && Util.getInt(0, 100)>10) {
						Attribute attribute = character.getAttribute(SpecializationAttribute.COOKING);
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("MAID.improvecooking",character));
						activity.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
						attribute.setMaxValue(attribute.getMaxValue() + 1);
					}
				}
			}
		}
}
