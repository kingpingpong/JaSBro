package jasbro.game.character.traits.perktrees;

import java.util.List;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.activities.sub.whore.Dominate;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.CustomersArriveEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.texts.TextUtil;

public class DominatrixPerks {
	
	public final static Condition aggressiveAdvertiser = new Condition() {};

	//Increase customer rating for bondage
	public static class LeatherQueen extends TraitEffect {
		@Override
		public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {

			if(customer.getPreferredSextype()==Sextype.BONDAGE){
				rating *= 3;
			}

			else {
				rating *= 0.5f;
			}

			return rating;
		}

	}
	
	//On kinky sex, 5% chance to quadruple health loss and quintuple energy loss. Max health is then increased by 1.
	public static class KillYou extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore && Util.getInt(0, 100)<5) {
					Whore whoreActivity = (Whore) activity;
					if(whoreActivity.getSexType() == Sextype.BONDAGE || whoreActivity.getSexType() == Sextype.GROUP){
						for (AttributeModification attributeModification : activity.getAttributeModifications()) {
							if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
								float modification = attributeModification.getBaseAmount();
								float change = -Math.abs(modification)*3.0f;
								attributeModification.addModificator(change-1.0f);
							}
							if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
								float modification = attributeModification.getBaseAmount();
								float change = -Math.abs(modification)*4.5f;
								attributeModification.addModificator(change);
							}
						}
						whoreActivity.getMainCustomers().get(0).addToSatisfaction(200, trait);
						Attribute attribute = character.getAttribute(EssentialAttributes.HEALTH);
						attribute.setMaxValue(attribute.getMaxValue()+1);
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("KILLYOU.rough",character));
						character.addCondition(new Buff.RoughenedUp());
					}
				}
			}
		}
	}
	
	//Bondage more effective at low energy and costs less health/energy
	public static class Masochist extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Whore whoreActivity = (Whore) activity;
					if (whoreActivity.getSexType() == Sextype.BONDAGE) {
						for (AttributeModification attributeModification : activity.getAttributeModifications()) {
							if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification) * 0.5f;
								attributeModification.addModificator(change);
							}
							else if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification) * 0.5f;
								attributeModification.addModificator(change);
							}
							else if (attributeModification.getAttributeType() == EssentialAttributes.MOTIVATION) {
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification) * 0.8f;
								attributeModification.addModificator(change);
							}
						}
						int bonusSatisfaction=(100-character.getFinalValue(EssentialAttributes.ENERGY))*2;
						whoreActivity.getMainCustomers().get(0).addToSatisfaction(bonusSatisfaction, trait);
					}
				}
			}
		}
	}
	
	/**
	 * Unlocks Struggle-Activity in the dungeon (not the adventure dungeons)
	 * TODO: Struggle-Activity
	 */
	public static class MasterAndSlave extends TraitEffect {
		/*
		 * Nothing to see here
		 * Again, this is only here if someone wants to do something with it
		 */
	}
	
	/**
	 * Bondage counts as cleaning (every 100 Cleaning it cleans 1 dirt)
	 * May give the slave the flu (~1% Chance)
	 * Somehow does not accept check if Bondage... so it works generally for Whore and Submit 
	 * TODO: Add Flavor text
	 */
	public static class LickItUp extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.WHORE || activity.getType() == ActivityType.SUBMIT) {
					Whore whore = (Whore) activity;
					if (whore.getSexType() == Sextype.BONDAGE) {
						if (character.getSpecializations().contains(SpecializationType.MAID)) {
							House house = activity.getHouse();
							int clean = 0;
							for (SpecializationType spec : character.getSpecializations()) {
								if (spec == SpecializationType.MAID) {
									double cleaning = character.getAnyAttributeValue(SpecializationAttribute.CLEANING) / 100;
									clean = (int) (clean - cleaning);
								}
							}
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("LICKITUP.clean", character));
							character.getAttribute(SpecializationAttribute.CLEANING).addToValue(1);
							house.modDirt(clean);

							int random = Util.getInt(0, 100);
							if (random < 1) {
								character.addCondition(new jasbro.game.character.conditions.Illness.Flu());
								activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("LICKITUP.flu", character));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Nurse reduces the HP-Cost of Bondage (% based, not reducing to actually 0)
	 * Every 10 points of Medical Knowledge decrease the health reduction by 1%
	 * Reduction can't be more than 90%
	 */
	public static class SquealAndHeal extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();

				float nurse = 0.0f;
				if (character.getSpecializations().contains(SpecializationType.NURSE)) {
					nurse = (float) character.getAnyAttributeValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 1000;
					if (nurse > 0.9)
						nurse = 0.9f;
				}

				if (activity.getType() == ActivityType.SUBMIT) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification)*nurse;
							attributeModification.addModificator(change);
						}
					}
				} else if (activity.getType() == ActivityType.WHORE) {
					Whore whore = (Whore) activity;
					if (whore.getSexType() == Sextype.BONDAGE) {
						for (AttributeModification attributeModification : activity.getAttributeModifications()) {
							if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification)*nurse;
								attributeModification.addModificator(change);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Dominate gives +30 Bar Customers
	 */
	public static class EveryoneBehave extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYCREATED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend) {
					Bartend bartend = (Bartend) activity;
					bartend.setBonus(bartend.getBonus()+30);
				}
			}
		}
	} 
	
	/**
	 * Bonus to Advertise
	 * Customers may get Horny or Very Horny more easily (SpawnData.java line 247)
	 * Sets the preference of 15% customers to Bondage
	 */
	public static class AggressiveAdvertisement extends TraitEffect {

		@Override
		public float getAttributeModifier(Attribute attribute) {

			if (attribute.getAttributeType() == SpecializationAttribute.ADVERTISING) {	
				return attribute.getCharacter().getAttribute(SpecializationAttribute.DOMINATE).getInternValue()/20;				
			} else {
				return 0;
			}
		}

		@Override
		public boolean removeTrait(Charakter character) {
			character.removeCondition(aggressiveAdvertiser);
			return true;
		}

		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				if (!character.getConditions().contains(aggressiveAdvertiser)) {
					character.addCondition(aggressiveAdvertiser);
				}
			}
			else if (e.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) e;
				for (Customer customer : customerEvent.getCustomers()) {
					if(Util.getInt(1, 20)<=2 && customer.getType()!=CustomerType.GROUP){
						customer.setPreferredSextype(Sextype.BONDAGE);
					}
				}
			}
		}
	}
	
	/**
	 * Bondage and Dominate are always equal
	 * if both are already equal, they get a ~20% bonus.
	 */
	public static class GoodMastersAreTheBestSubs extends TraitEffect {

		@Override
		public float getAttributeModifier(Attribute attribute) {
			float dominate = attribute.getCharacter().getAttribute(SpecializationAttribute.DOMINATE).getInternValue();
			float bondage = attribute.getCharacter().getAttribute(Sextype.BONDAGE).getInternValue();
			float value = dominate - bondage;
			float percentBonus = 0.2f;

			if (value > 0) { // if Dominate > Bondage
				if (attribute.getAttributeType() == Sextype.BONDAGE) {
					return value;
				}
			} else if (value < 0) { // if Bondage > Dominate
				if (attribute.getAttributeType() == SpecializationAttribute.DOMINATE) {
					return -value;
				}
			} else if (value == 0) {
				if (attribute.getAttributeType() == SpecializationAttribute.DOMINATE)
					return dominate * percentBonus;
				if (attribute.getAttributeType() == Sextype.BONDAGE)
					return bondage * percentBonus;
			}
			return 0;
		}
	}
	
	/**
	 * Bonus to Strength and Stamina
	 * Increases gain of those by 20% each
	 */
	public static class GiveAndTake extends TraitEffect {

		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ATTRIBUTECHANGE) {
				AttributeModification attributeModification = (AttributeModification) e.getSource();
				if (attributeModification.getAttributeType() == BaseAttributeTypes.STRENGTH ||
						attributeModification.getAttributeType() == BaseAttributeTypes.STAMINA) {
					float modification = attributeModification.getBaseAmount();
					float change = Math.abs(modification) * 0.2f;
					attributeModification.addModificator(change);
				}
			}
		}
	}
	/**
	 * Stamina gives bonus to Bondage
	 * 
	 * Bondage and Submit reduce health loss if it's lower than 30
	 * Doubles Health loss above 30 Health
	 */
	public static class HitMeHarder extends TraitEffect {
		@Override
		public float getAttributeModifier(Attribute attribute) {
			if (attribute.getAttributeType() == Sextype.BONDAGE) {	
				return attribute.getCharacter().getAttribute(BaseAttributeTypes.STAMINA).getInternValue()/4;				
			} else {
				return 0;
			}
		}


		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.SUBMIT) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
							if (character.getHealth() > 30) {
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification)*2.0f;
								attributeModification.addModificator(-change);
							} else if (character.getHealth() <= 30) {
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification)*1.0f;
								attributeModification.addModificator(change);
							} 
						}
					}
				} else if (activity.getType() == ActivityType.WHORE) {
					Whore whore = (Whore) activity;
					if (whore.getSexType() == Sextype.BONDAGE) {
						for (AttributeModification attributeModification : activity.getAttributeModifications()) {
							if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
								if (character.getHealth() > 30) {
									float modification = attributeModification.getBaseAmount();
									float change = Math.abs(modification)*1.0f;
									attributeModification.addModificator(-change);
								} else if (character.getHealth() <= 30) {
									float modification = attributeModification.getBaseAmount();
									float change = Math.abs(modification)*1.0f;
									attributeModification.addModificator(change);
								} 
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Strength gives Bonus to Dominate 
	 * Dominate costs less actions (1 instead of 2)
	 */
	public static class CruelMaster extends TraitEffect {
		@Override
		public float getAttributeModifier(Attribute attribute) {
			if (attribute.getAttributeType() == SpecializationAttribute.DOMINATE) {	
				return attribute.getCharacter().getAttribute(BaseAttributeTypes.STRENGTH).getInternValue()/4;				
			} else {
				return 0;
			}
		}

		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.DOMINATE) {
					Dominate dom = (Dominate) activity;
					if (dom.getAmountActions() > 1)
						dom.setActionCost(1.0f);
				}
			}
		}
	}
	/**
	 * Bonus to learning of Bondage and Dominate (30%)
	 */
	public static class PleasureAndPain extends TraitEffect {

		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ATTRIBUTECHANGE) {
				AttributeModification attributeModification = (AttributeModification) e.getSource();
				if (attributeModification.getAttributeType() == SpecializationAttribute.DOMINATE ||
						attributeModification.getAttributeType() == Sextype.BONDAGE) {
					float modification = attributeModification.getBaseAmount();
					float change = Math.abs(modification) * 0.3f;
					attributeModification.addModificator(change);
				}
			}
		}
	}
	/**
	 * Dominate gives a buff
	 */
	public static class PleasureThroughPain extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.DOMINATE)
					character.addCondition(new Buff.SadisticDelight(character));
			}
		}	
	}
	/**
	 * Bondage/Submit gives a buff
	 */
	public static class PleasureInPain extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.SUBMIT) {
					character.addCondition(new Buff.MasochisticDelight(character));	
				}
				else if (activity.getType() == ActivityType.WHORE) {
					Whore whore = (Whore) activity;
					if (whore.getSexType() == Sextype.BONDAGE) {
						character.addCondition(new Buff.MasochisticDelight(character));	
					}
				}
			}
		}	
	}
	/**
	 * Opposite of Masochist.
	 * Dominate should be better the more energy the slave has
	 * 
	 * Adds 50% of Energy - 30 to Satisfaction => at 60 Energy Satisfaction gets lowered otherwise it's higher
	 */
	public static class Sadist extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.DOMINATE) {
					int bonusSatisfaction = character.getFinalValue(EssentialAttributes.ENERGY)/2 - 30;
					activity.getMainCustomers().get(0).addToSatisfaction(bonusSatisfaction, trait);
				}
			}
		}
	}
	/**
	 * Dominate in every room
	 */
	public static class MyWhipIsAllINeed extends TraitEffect {
		/*
		 * Nothing to see here
		 * Everything is done in RoomInfoUtil.java
		 * This class is here if someone ever wants to do something different with this perk.
		 */
	}
	
	/**
	 * Increases Obedience for every slave in the house by 0.2 at the end of every night
	 * Excludes the slave itself.
	 */
	public static class LeatherMistress extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {	// At the end of the day
				// Get all houses the player has and iterate through them
				List<House> listHouses = Jasbro.getInstance().getData().getHouses();
				for (House thisHouse : listHouses) {
					// get all Rooms and iterate through them
					List<Room> listRooms = thisHouse.getRooms();
					for (Room firstWalkThroughRooms : listRooms) {
						// if the character with the trait is in a room
						if (firstWalkThroughRooms.getCurrentUsage().getCharacters().contains(character)) {
							for (Room iterateAllRooms : listRooms) {
								// get all other characters in the house and modify their obedience
								List<Charakter> listCharacter = iterateAllRooms.getCurrentUsage().getCharacters();
								for (Charakter thisCharacter : listCharacter) {
									if (thisCharacter != character) { // No obedience gain for the character itself
										if (thisCharacter.getType() == CharacterType.SLAVE) { // character hast do be a slave
											AttributeModification attributeModification = 
													new AttributeModification(
															0.2f,
															BaseAttributeTypes.OBEDIENCE, 
															thisCharacter
															);
											attributeModification.applyModification();
										}
									}
								}
							}
							break;
						}

					}
				}

			}
		}
	}
}
