package jasbro.game.character.traits.perktrees;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Clean;
import jasbro.game.character.activities.sub.Sleep;
import jasbro.game.character.activities.sub.Swim;
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.activities.sub.business.BathAttendant;
import jasbro.game.character.activities.sub.business.SellFood;
import jasbro.game.character.activities.sub.business.Strip;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.conditions.BattleCondition;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.Illness;
import jasbro.game.character.conditions.OvipositionPregnancy;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Perks.PerkUtil;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.Person;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.game.items.AccessoryType;
import jasbro.game.items.Equipment;
import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class FurryPerks {

	/**
	 * 	Static buffs that are created for the sake of "Beast in Heat" to make checking for them easier
	 * 	They don't do anything other than show an icon.
	 */
	static Buff h1 = new Buff.Heat1(),
			h2 = new Buff.Heat2(),
			h3 = new Buff.Heat3();

	/**
	 * Gives the character the Anthro-Tag making him somewhat less human.
	 * Customers might or might not like it.
	 * Other than that: gives a chance to increase customer satisfaction
	 * 
	 * TODO: Flavor text
	 */
	public static class BestialFeatures extends TraitEffect {

		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (!character.getTraits().contains(Trait.FURRY)) {
				character.addTrait(Trait.BESTIAL);
			}
			if (character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue() < 10) {
				character.getAttribute(SpecializationAttribute.TRANSFORMATION).setInternValue(10);
			}
		}
	}

	/* *********
	 * Aquatic *
	 ***********/

	public static class Aquatic extends TraitEffect {
		int stage = 0,
				stagelimit = 5, // max number of stages
				stagestep = 10; // every 50 points in Transformation gets you another stage

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue(); 

			stage = (int) (value / stagestep);
			if (stage > stagelimit) stage = stagelimit;
			PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("AQUATICSTAGE"+stage);

			switch(stage) {
			case 5:
			case 4: PerkUtil.addMaybe(character, Trait.RELENTLESSBEAST);
			case 3: PerkUtil.addMaybe(character, Trait.AQUATICNURSE);
			case 2: PerkUtil.addMaybe(character, Trait.AQUATICSWIM);
			case 1:
			default: PerkUtil.addMaybe(character, Trait.AQUATICDOWNSIDE);
			}
		}

		@Override
		public boolean removeTrait(Charakter character) {
			PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);

			PerkUtil.removeMaybe(character, Trait.AQUATICSWIM);
			PerkUtil.removeMaybe(character, Trait.AQUATICDOWNSIDE);
			PerkUtil.removeMaybe(character, Trait.AQUATICNURSE);
			PerkUtil.removeMaybe(character, Trait.RELENTLESSBEAST);
			return super.removeTrait(character);
		}

		@Override
		public float getAttributeModifier(Attribute attribute) {
			float value = attribute.getInternValue();
			float multiplier = stage * 0.05f;
			if (attribute.getAttributeType() == SpecializationAttribute.MEDICALKNOWLEDGE) {
				return value * multiplier;
			}
			if (attribute.getAttributeType() == SpecializationAttribute.MAGIC) {
				return value * multiplier;
			}
			return 0;
		}
	}

	public static class AquaticSwim extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Swim) {
					for (AttributeModification a : activity.getAttributeModifications()) {
						if (a.getAttributeType() == EssentialAttributes.ENERGY) {
							a.addModificator(Math.abs(a.getBaseAmount()) + 10);
						}
						if (a.getAttributeType() == EssentialAttributes.HEALTH) {
							a.addModificator(5.0f);
						}
					}

					int chance = Util.getInt(0, 100);
					if (chance > 50) {
						List<Item> loot = Jasbro.getInstance().getAvailableItemsByType(ItemType.LOOT);
						int item = Util.getInt(0, loot.size());
						Item foundItem = loot.get(item);
						Jasbro.getInstance().getData().getInventory().addItem(foundItem);
						activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("AQUATIC.found", character, foundItem.getName()));
					}
				}
			}
		}
	}

	public static class AquaticDownside extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			boolean dosomething = false;
			boolean dry = true;


			if (e.getType() == EventType.NEXTDAY) {
				dosomething = true;
				dry = true;
			}
			else if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.SWIM ||
						activity.getType() == ActivityType.SOAK ||
						activity.getType() == ActivityType.BATHATTENDANT ||
						activity.getType() == ActivityType.BATHE
						) {
					dosomething = true;
					dry = false;
				}

			}

			if (dosomething) {
				boolean found = false;
				for (Condition con : character.getConditions()) {
					if (con instanceof Buff.AquaticTrait) {
						int stage = ((Buff.AquaticTrait) con).getStage();
						if (dry) {
							if (stage > 1) stage--;
						}
						else {
							if (stage < 5) stage++;
						}
						character.removeCondition(con);
						character.addCondition(new Buff.AquaticTrait(character, stage));
						found = true;
						break;
					}
				}
				if (!found) {
					character.addCondition(new Buff.AquaticTrait(character, 3));
				}
			}
		}
	}

	public static class AquaticNurse extends TraitEffect {
		/*
		 * Actual effect in Nurse.java, line 72++
		 */
	}

	public static class RelentlessBeast extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if(e.getType() == EventType.ENERGYZERO && !e.isCancelled()){
				e.setCancelled(true);
				AttributeChangedEvent attributeChangedEvent = (AttributeChangedEvent) e;
				RunningActivity activity = attributeChangedEvent.getActivity();
				if (activity != null) {
					activity.getAttributeModifications().
					add(new AttributeModification(-10f, EssentialAttributes.HEALTH, character));
					//TODO maybe add fitting text to activity?
				}
				else { //No activity... shouldn't happen too often
					character.getAttribute(EssentialAttributes.HEALTH).addToValue(-10f);
				}

			}
			if (e.getType() == EventType.STATUSCHANGE) {
				for (Condition con : character.getConditions()) {
					if (con instanceof Illness) {
						character.removeCondition(con);
					}
				}
			}
		}
	}

	/* **********
	 * Arachnid *
	 ************/

	public static class Arachnid extends TraitEffect {
		//TODO: Stage 5 is still missing (Pregnancy is bugged. I'm unable to add a trait to a child)
		//      Downside is missing too.
		int stage = 0,
				stagelimit = 5, // max number of stages
				stagestep = 10; // every 50 points in Transformation gets you another stage

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue(); 

			stage = (int) (value / stagestep);
			if (stage > stagelimit) stage = stagelimit;
			PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("ARACHNIDSTAGE"+stage);

			switch(stage) {
			case 5: PerkUtil.addMaybe(character, Trait.AUTONOMOUSPERK);
			case 4: PerkUtil.addMaybe(character, Trait.HEARTOFTHESWARM);
			case 3: PerkUtil.addMaybe(character, Trait.OVIPOSITION);
			case 2: PerkUtil.addMaybe(character, Trait.INHUMANPREGNANCY);
			case 1: 
			default: PerkUtil.addMaybe(character, Trait.ARACHNIDSLEEP);
			}
		}

		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
				return currentValue + 5 + 5*stage;
			}
			return currentValue;
		}

		@Override
		public boolean removeTrait(Charakter character) {
			PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);

			PerkUtil.removeMaybe(character, Trait.HEARTOFTHESWARM);
			PerkUtil.removeMaybe(character, Trait.OVIPOSITION);
			PerkUtil.removeMaybe(character, Trait.INHUMANPREGNANCY);
			PerkUtil.removeMaybe(character, Trait.AUTONOMOUSPERK);
			PerkUtil.removeMaybe(character, Trait.ARACHNIDSLEEP);

			return super.removeTrait(character);
		}
	}

	/**
	 * Increases pregnancy chance by a fixed 30%
	 * Also increases chance of Additional Offsprings by 20%
	 * 
	 * Also reduces Pregnancy Time by ~66% (but that's not shown here)
	 */
	public static class HeartOfTheSwarm extends TraitEffect {
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
				return currentValue + 30;
			}
			else if (calculatedAttribute == CalculatedAttribute.CHANCEADDITIONALCHILD) {
				return currentValue + 20;
			} else {
				return currentValue;
			}
		}
	}

	/**
	 * Slave has a ~10% Chance each day to get pregnant with an egg (if not already pregnant).
	 * This egg has a ~20% chance to birth a human and a ~80% chance to be a monster egg.
	 * Pregnancy lasts 30 days if nothing else shortens it below that.
	 * 
	 * TODO: Flavor Text?
	 */
	public static class Oviposition extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				boolean notPregnant = true;
				for (Condition condition : character.getConditions()) {
					if (condition instanceof PregnancyInterface) {
						notPregnant = false;
						break;
					}
				}

				if (notPregnant && Util.getInt(0, 100) < 6) {
					character.addCondition(new OvipositionPregnancy());

					MessageData messageData = new MessageData(TextUtil.t("oviposition.message"),
							ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character),
							character.getBackground(), true);
					messageData.createMessageScreen();
				}
			}
		}
	}

	public static class InhumanPregnancy extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
				return currentValue + 10;
			}
			else {
				return currentValue;
			}
		}
	}

	public static class ArachnidSleep extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.SLEEP) {
					List<Charakter> chars = activity.getCharacters();
					for (Charakter c : chars) {
						if (!c.getTraits().contains(Trait.ARACHNID)) {
							c.addCondition(new Buff.BadDreams(c));
						}
					}
				}
			}
		}
	}

	/* *******
	 * Avian *
	 *********/

	public static class Avian extends TraitEffect {
		int stage = 0,
				stagelimit = 5, // max number of stages
				stagestep = 10; // every 50 points in Transformation gets you another stage

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue(); 

			stage = (int) (value / stagestep);
			if (stage > stagelimit) stage = stagelimit;
			PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("AVIANSTAGE"+stage);

			switch(stage) {
			case 5:
			case 4:
			case 3: PerkUtil.addMaybe(character, Trait.AVIANDRAG);
			case 2: PerkUtil.addMaybe(character, Trait.AVIANPICKUP);
			case 1: PerkUtil.addMaybe(character, Trait.AVIANFLIGHT);
			default: PerkUtil.addMaybe(character, Trait.AVIANBIRDBRAIN);
			}
		}

		@Override
		public boolean removeTrait(Charakter character) {
			PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);

			PerkUtil.removeMaybe(character, Trait.AVIANDRAG);
			PerkUtil.removeMaybe(character, Trait.AVIANFLIGHT);
			PerkUtil.removeMaybe(character, Trait.AVIANPICKUP);
			PerkUtil.removeMaybe(character, Trait.AVIANBIRDBRAIN);
			return super.removeTrait(character);
		}

		@Override
		public float getAttributeModifier(Attribute attribute) {
			float value = attribute.getInternValue();
			if (attribute.getAttributeType() == SpecializationAttribute.ADVERTISING) {
				// 5% -> 25% Bonus to Advertising
				float multiplier = stage * 0.5f;
				return value * multiplier;
			}
			return 0;
		}
	}

	public static class AvianPickup extends TraitEffect {

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.ADVERTISE) {
					int chance = Util.getInt(0, 100);
					if (chance > 50) {
						List<Item> loot = Jasbro.getInstance().getAvailableItemsByType(ItemType.LOOT);
						int item = Util.getInt(0, loot.size());
						Item foundItem = loot.get(item);
						Jasbro.getInstance().getData().getInventory().addItem(foundItem);
						activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("AVIAN.found", character, foundItem.getName()));
					}
				}
			}
		}
	}

	public static class AvianBirdbrain extends TraitEffect {

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYPERFORMED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				int chance = Util.getInt(0, 100);
				if (activity.getIncome() > 0 && chance > 70) {
					activity.setIncome(0);
					activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("AVIAN.birdbrain", character));
				}
			}
		}
	}

	/* ********
	 * Bovine *
	 **********/

	public static class Bovine extends TraitEffect {
		int stage = 0,
				stagelimit = 5, // max number of stages
				stagestep = 10; // every 50 points in Transformation gets you another stage

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue(); 

			stage = (int) (value / stagestep);
			if (stage > stagelimit) stage = stagelimit;
			PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("BOVINESTAGE"+stage);

			switch(stage) {
			case 5:
			case 4:
			case 3: 
			case 2: 
			case 1: 
			default: 
			}
		}

		@Override
		public boolean removeTrait(Charakter character) {
			PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);

			return super.removeTrait(character);
		}

		@Override
		public float getAttributeModifier(Attribute attribute) {
			float value = attribute.getInternValue();
			if (attribute.getAttributeType() == BaseAttributeTypes.STAMINA) {
				// 5% -> 25% Bonus to Stamina
				float multiplier = stage * 0.5f;
				return value * multiplier;
			}
			return 0;
		}
	}

	/* *********
	 * Canine  *
	 ***********/

	public static class Canine extends TraitEffect {
		int stage = 0,
				stagelimit = 5, // max number of stages
				stagestep = 10; // every 50 points in Transformation gets you another stage

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();

			stage = (int) (value / stagestep);
			if (stage > stagelimit) stage = stagelimit;
			PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("CANINESTAGE"+stage);

			switch(stage) {
			case 5: // TODO: Guarding increases energy instead of reducing it. Implement working Security for that.
			case 4: PerkUtil.addMaybe(character, Trait.CANINECOMMAND);
			case 3: // TODO: More Security, the more guards (up to +20%): Implement working Security for that
			case 2: // TODO: Reduce Upkeep
			case 1: // TODO: +10% Efficiency for people in the same room
			default: PerkUtil.addMaybe(character, Trait.CANINESLEEP);
			}

			// TODO: Make it work. It does not right now and I'm too mentally exhausted to figure out, why
		}


		@Override
		public boolean removeTrait(Charakter character) {
			PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
			return super.removeTrait(character);
		}
	}

	public static class CanineSleep extends TraitEffect {		
		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.SLEEP) {
					if (activity.getRoom().getAmountPeople() < 2) {
						float modification = 0;
						for (AttributeModification mod : activity.getAttributeModifications()) {
							if (mod.getAttributeType() == EssentialAttributes.ENERGY) {
								modification = mod.getBaseAmount();
							}
						}
						modification *= -1.3;
						activity.getAttributeModifications().add(new AttributeModification(modification, EssentialAttributes.ENERGY, character));
						activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("CANINE.sleep",character));
					}
				}
			}
		}
	}

	public static class CanineCommand extends TraitEffect {
		@Override
		public float getAttributeModifier(Attribute attribute) {
			if (attribute.getAttributeType() == BaseAttributeTypes.COMMAND) {
				return attribute.getInternValue() *0.25f;
			}
			return 0;
		}

		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.CONTROL) {
				if (currentValue < 0) {
					double temp = Math.abs(currentValue) * 0.75;
					return currentValue + temp;
				}
			}
			return currentValue;
		}
	}

	/* *********
	 * Feline  *
	 ***********/

	public static class Feline extends TraitEffect {
		int stage = 0,
				stagelimit = 5, // max number of stages
				stagestep = 10; // every 50 points in Transformation gets you another stage

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();

			stage = (int) (value / stagestep);
			if (stage > stagelimit) stage = stagelimit;
			PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("FELINESTAGE"+stage);

			switch(stage) {
			case 5: PerkUtil.addMaybe(character, Trait.FELINEHEAT); 
			case 4: PerkUtil.addMaybe(character, Trait.FELINESTRIP); 	
			case 3: PerkUtil.addMaybe(character, Trait.NOCTURNAL);
			case 2: PerkUtil.addMaybe(character, Trait.CATNAP);	// TODO: Implement Rest Everywhere for Catnap
			case 1: // No Perk added	
			}

			if (stage >= 4) {
				if (e.getType() == EventType.ACTIVITYCREATED) {
					RunningActivity activity = (RunningActivity) e.getSource();
					if (activity instanceof Strip) {
						Strip strip = (Strip) activity;
						strip.setBonus(strip.getBonus() + 5);
					}
				} 
			}
		}

		@Override
		public float getAttributeModifier(Attribute attribute) {
			float value = attribute.getInternValue();
			float multiplier1 = stage * 0.05f;
			float multiplier2 = -(stage * 0.1f);
			if (attribute.getAttributeType() == SpecializationAttribute.STRIP) {
				// 5% -> 25% Bonus to Bartending
				return value * multiplier1;
			}
			else if (attribute.getAttributeType() == BaseAttributeTypes.OBEDIENCE) {
				boolean fed = false;
				List<Condition> temp = attribute.getCharacter().getConditions();
				for (Condition con : temp) {
					if (con instanceof Buff.Satiated)
						fed = true;
				}
				if (!fed) {
					return value * multiplier2;
				}
			}
			return 0;
		}

		@Override
		public boolean removeTrait(Charakter character) {
			PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
			PerkUtil.addMaybe(character, Trait.FELINESTRIP); 
			PerkUtil.removeMaybe(character, Trait.NOCTURNAL);
			PerkUtil.removeMaybe(character, Trait.CATNAP);
			PerkUtil.removeMaybe(character, Trait.FELINEHEAT);
			return super.removeTrait(character);
		}
	}


	
	public static class FelineStrip extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.STRIP) {
					Strip strip = (Strip) activity;
					strip.setBonus(strip.getBonus()+5);
				}
			}
		}
	}

	public static class Nocturnal extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY && Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof BusinessMainActivity) {
					if (activity instanceof Whore) {

						activity.getMainCustomers().get(0).addToSatisfaction(95, trait);

					}
				}
				if (activity instanceof Strip) {
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(20, activity);	
					}
				}
				if (activity instanceof Bartend) {
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(10, activity);	
					}
				}
				if (activity instanceof BathAttendant) {
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(15, activity);	
					}
				}
				if (activity instanceof SellFood) {
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(5, activity);	
					}
				}
			}
		}
	}

	public static class CatNap extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Sleep) {
					activity.getAttributeModifications().add(new AttributeModification(15f, EssentialAttributes.ENERGY, character));
					activity.getAttributeModifications().add(new AttributeModification(10f, EssentialAttributes.HEALTH, character));
				}
			}
		}
	}
	/* ********
	 * Insect *
	 **********/

	public static class Insect extends TraitEffect {
		int stage = 0,
				stagelimit = 5,
				stagestep = 10;

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue(); 

			stage = (int) (value / stagestep);
			if (stage > stagelimit) stage = stagelimit;
			PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("INSECTSTAGE"+stage);

			switch(stage) {
			case 5: PerkUtil.addMaybe(character, Trait.INSECTSEVERYWHERE);
			case 4: PerkUtil.addMaybe(character, Trait.INSECTEFFICIENCY);
			case 3: PerkUtil.addMaybe(character, Trait.INSECTGATHER);
			case 2: PerkUtil.addMaybe(character, Trait.INSECTRESILIENCE);
			case 1:
			default: 
			}
		}

		@Override
		public boolean removeTrait(Charakter character) {
			PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
			PerkUtil.removeMaybe(character, Trait.INSECTGATHER);
			PerkUtil.removeMaybe(character, Trait.INSECTRESILIENCE);
			PerkUtil.removeMaybe(character, Trait.INSECTEFFICIENCY);
			PerkUtil.removeMaybe(character, Trait.INSECTSEVERYWHERE);
			return super.removeTrait(character);
		}

		@Override
		public float getAttributeModifier(Attribute attribute) {
			float value = attribute.getInternValue();
			float multiplier = stage * 0.05f;
			if (attribute.getAttributeType() == SpecializationAttribute.CLEANING) {
				return value * multiplier;
			}
			if (attribute.getAttributeType() == SpecializationAttribute.COOKING) {
				return value * multiplier;
			}
			return 0;
		}
	}

	public static class InsectResilience extends TraitEffect {

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() != ActivityType.FIGHT) {
					for (AttributeModification mod : activity.getAttributeModifications()) {
						if (mod.getAttributeType() == EssentialAttributes.HEALTH) {
							if (mod.getBaseAmount() < 100)
								mod.setBaseAmount(0);
							// activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("INSECT.tough",character));
						}
					}
				}
			}
		}
	}

	public static class InsectGather extends TraitEffect {

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				int chance = Util.getInt(0, 100);
				if (chance > 90) {
					List<Item> loot = Jasbro.getInstance().getAvailableItemsByType(ItemType.INGREDIENT);
					if (loot.size() > 0) {
						int item = Util.getInt(0, loot.size());
						Item foundItem = loot.get(item);
						Jasbro.getInstance().getData().getInventory().addItem(foundItem);
						activity.getMessages().get(0).addToMessage("\n" + TextUtil.t("INSECT.found", character, foundItem.getName()));
					} else {
						System.err.println("Error: No Ingredient could be found to be gathered.");
					}
				}
			}
		}
	}

	public static class InsectEfficiency extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.CLEAN) {
					int insects = 0;
					for (Room r : activity.getHouse().getRooms()) {
						for (Charakter c : r.getCurrentUsage().getCharacters()) {
							if (c.getTraits().contains(Trait.INSECT))
								insects++;
						}
					}
					Clean clean = (Clean) activity;
					float dirt = clean.getDirtModification();
					dirt += dirt * 0.1f * insects;
					clean.setDirtModification(dirt);
				}
			}
		}
	}

	public static class InsectsEverywhere extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.CLEAN) {
					Clean c = (Clean) activity;
					float dirt = c.getDirtModification();
					c.setDirtModification(0);					
					List<House> houses = Jasbro.getInstance().getData().getHouses();
					int nrHouses = houses.size();
					int dph = (int) (dirt / nrHouses);
					for (House h : houses) {
						h.modDirt(dph);
					}
				}
			}
		}
	}

	/* ***********
	 * Lagomorph *
	 *************/

	public static class Lagomorph extends TraitEffect {
		//TODO: Stage 2 (Eiri needs to write some flavor + effects), Stage 5 (Eiri needs to write some flavor + effects) and Downside

		int stage = 0,
				stagelimit = 5,
				stagestep = 10;

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue(); 

			stage = (int) (value / stagestep);
			if (stage > stagelimit) stage = stagelimit;
			PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("LAGOMORPHSTAGE"+stage);

			switch(stage) {
			case 5: PerkUtil.addMaybe(character, Trait.LAGOMORPHORGY);
			case 4: PerkUtil.addMaybe(character, Trait.LAGOMORPHHORNY);
			case 3: PerkUtil.addMaybe(character, Trait.LAGOMORPHENDURANCE);
			case 2: PerkUtil.addMaybe(character, Trait.LAGOMORPHQUICKY);
			case 1: 
			}



			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				List<AttributeModification> mods = activity.getAttributeModifications();
				boolean lago = false;
				float modification = 0;
				for (AttributeModification mod : mods) {
					if (mod.getAttributeType() == EssentialAttributes.MOTIVATION) {
						modification = mod.getBaseAmount();
						Room r = activity.getRoom();
						if(r!=null){
							if (r.getAmountPeople() > 1) {
								lago=true;
							}
						}
					}
				}
				if (!lago) {
					if(modification < 0) 
						activity.getAttributeModifications().add(new AttributeModification(modification, EssentialAttributes.MOTIVATION, character));
					activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("LAGOMORPH.lonely", character));
				}
			}
		}

		@Override
		public float getAttributeModifier(Attribute attribute) {
			float value = attribute.getInternValue();
			float multiplier = stage * 0.05f;
			if (attribute.getAttributeType() == SpecializationAttribute.BARTENDING) {
				return value * multiplier;
			}
			return 0;
		}

		@Override
		public boolean removeTrait(Charakter character) {
			PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);

			PerkUtil.removeMaybe(character, Trait.LAGOMORPHQUICKY);
			PerkUtil.removeMaybe(character, Trait.LAGOMORPHHORNY);
			PerkUtil.removeMaybe(character, Trait.LAGOMORPHENDURANCE);
			PerkUtil.removeMaybe(character, Trait.LAGOMORPHORGY);
			return super.removeTrait(character);
		}
	}

	public static class LagomorphEndurance extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification)*0.3f;
							attributeModification.addModificator(change);
						}
					}
					Bartend bartend = (Bartend) activity;
					bartend.setBonus(bartend.getBonus()+10);
				}
			}
		}
	}

	public static class LagomorphHorny extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.BARTEND) {
					int rnd = Util.getRnd().nextInt(101);
					if (rnd < 51) {
						for (AttributeModification a : activity.getAttributeModifications()) {
							if (a.getAttributeType() == EssentialAttributes.MOTIVATION) {
								a.addModificator(2.0f);
							}
						}
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("LAGOMORPH.motivation", character));
					}
					//character.addCondition(new Buff.HornyBuff(character));	
				}
			}
		}	
	}

	public static class LagomorphOrgy extends TraitEffect {

		/*@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.BARTEND) {
					Bartend bartend = (Bartend) activity;
					int rnd = Util.getRnd().nextInt(101);
					if (rnd < 26) {
						ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
						bartend.getMessages().get(0).setImage(image);
						bartend.getMessages().get(0).addToMessage("\n" + TextUtil.t("LAGOMORPH.orgy",character));
						for (Customer c : bartend.getCustomers()) {
							c.addToSatisfaction((int) (c.getSatisfactionAmount()*0.1), null);
						}						
						bartend.getAttributeModifications().add(new AttributeModification(2f, Sextype.GROUP, character));
					}
				}
			}
		}*/
	}

	/* *********
	 * Reptile *
	 ***********/

	public static class Reptilian extends TraitEffect {
		int stage = 0,
				stagelimit = 5, // max number of stages
				stagestep = 10; // every 50 points in Transformation gets you another stage

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();

			stage = (int) (value / stagestep);
			if (stage > stagelimit) stage = stagelimit;
			PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("REPTILIANSTAGE"+stage);

			Attribute HP = character.getAttribute(EssentialAttributes.HEALTH);
			switch(stage) {
			case 3:
				if (HP.getMaxValue() < 160) {
					HP.setMaxValue(HP.getMaxValue() + 60);
				}
				break;
			case 4:
				if (HP.getMaxValue() < 180) {
					HP.setMaxValue(HP.getMaxValue() + 20);
				}
				break;
			case 5:
				if (HP.getMaxValue() < 200) {
					HP.setMaxValue(HP.getMaxValue() + 20);
				}
				break;
			}

			/*
			 * Fall Through structure:
			 * No break on higher cases, so they go through the lower ones still
			 * If someone somehow skips a stage he/she/it will still get all traits
			 */
			switch(stage) {
			case 5: PerkUtil.addMaybe(character, Trait.FLAMEBREATH);
			case 4: PerkUtil.addMaybe(character, Trait.REPTILIANMOTIVATION);
			case 3: PerkUtil.addMaybe(character, Trait.REPTILIANARMOR);
			case 2: PerkUtil.addMaybe(character, Trait.EXTRACTABLECLAWS);
			case 1: PerkUtil.addMaybe(character, Trait.REPTILIANDOWNSIDE);
			}
		}

		@Override
		public float getAttributeModifier(Attribute attribute) {
			float value = attribute.getInternValue();
			float multiplier = stage * 0.05f;
			if (attribute.getAttributeType() == BaseAttributeTypes.STRENGTH) {
				return value * multiplier;
			}
			if (attribute.getAttributeType() == EssentialAttributes.HEALTH) {
				//if (stage >= 3) {
				//					multiplier = stage * 0.2f;
				//				return value + multiplier;
				//		}
			}
			return 0;
		}

		@Override
		public boolean removeTrait(Charakter character) {
			PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);

			PerkUtil.removeMaybe(character, Trait.FLAMEBREATH);
			PerkUtil.removeMaybe(character, Trait.REPTILIANDOWNSIDE);
			PerkUtil.removeMaybe(character, Trait.EXTRACTABLECLAWS);
			PerkUtil.removeMaybe(character, Trait.REPTILIANMOTIVATION);
			PerkUtil.removeMaybe(character, Trait.REPTILIANARMOR);

			if (stage >= 3)
				character.getAttribute(EssentialAttributes.HEALTH).setMaxValue(character.getAttribute(EssentialAttributes.HEALTH).getMaxValue() - 40 - stage*20);
			return super.removeTrait(character);
		}
	}

	public final static class FlameBreath extends TraitEffect {
		@Override
		public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
			attacks.add(new Attack.FlameBreath(character));
		}
	}

	public final static class ExtractableClaws extends TraitEffect {
		@Override
		public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
			for (Equipment item : character.getCharacterInventory().listEquipment()) {
				if (item.getAccessoryType() == AccessoryType.ONEHANDED ||
						item.getAccessoryType() == AccessoryType.TWOHANDED) {
					return;
				}
			}

			if (attacks.get(0) instanceof Attack.StandardAttack) {
				attacks.remove(0);
			}

			attacks.add(new Attack.ClawAttack(character));
		}
	}

	public static class ReptilianArmor extends TraitEffect {
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.ARMORVALUE) {
				return currentValue *1.2;
			} else {
				return currentValue;
			}
		}
	}

	public static class ReptilianDownside extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.isAbort()) {
					return;
				}
				if (activity instanceof Whore && activity.getType() != ActivityType.TEASE) {
					if (activity instanceof BusinessMainActivity) {
						BusinessMainActivity businessMainActivity = (BusinessMainActivity) activity;
						if (businessMainActivity.getMainCustomers().size() > 0) {
							int rnd = Util.getInt(0, 100);
							int chance = 5;

							if (rnd < chance) { // does the girl hit the customer?
								MessageData message = activity.getMessages().get(0);
								message.addToMessage(TextUtil.t("REPTILIAN.hit", character, businessMainActivity.getMainCustomers().get(0)));
								businessMainActivity.getMainCustomers().get(0).addToSatisfaction((int) (-character.getDamage() * 10 * 2), trait);
								activity.getAttributeModifications().add(new AttributeModification(-0.05f, BaseAttributeTypes.OBEDIENCE, character));

								int chanceKo = (int) (character.getDamage());
								rnd = Util.getInt(0, 100);

								if (rnd < chanceKo) {
									activity.setAbort(true);
									List<AttributeModification> attributeModifications = new ArrayList<AttributeModification>();
									attributeModifications.add(new AttributeModification(-20f, EssentialAttributes.ENERGY, character));
									attributeModifications.add(new AttributeModification(-0.05f, BaseAttributeTypes.OBEDIENCE, character));
									message.addToMessage(TextUtil.t("REPTILIAN.hitKO", character, businessMainActivity.getMainCustomers().get(0)));
									message.setBackground(character.getBackground());
									message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character));
									message.setAttributeModifications(attributeModifications);
									message.createMessageScreen();
								} else {
									rnd = Util.getInt(0, 100);
									int chanceAbort = (-businessMainActivity.getMainCustomers().get(0).getSatisfactionAmount()) - (int) (character.getDamage() * 4) - character.getCharisma() / 2;

									if (rnd < chanceAbort) {
										activity.setAbort(true);
										List<AttributeModification> attributeModifications = new ArrayList<AttributeModification>();
										attributeModifications.add(new AttributeModification(-20f, EssentialAttributes.ENERGY, character));
										attributeModifications.add(new AttributeModification(-10f, EssentialAttributes.HEALTH, character));
										attributeModifications.add(new AttributeModification(-0.05f, BaseAttributeTypes.OBEDIENCE, character));
										message.addToMessage(TextUtil.t("REPTILIAN.hitBack", character, businessMainActivity.getMainCustomers().get(0)));
										message.setBackground(character.getBackground());
										message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character));
										message.setAttributeModifications(attributeModifications);
										message.createMessageScreen();
									} else {
										message.addToMessage(TextUtil.t("REPTILIAN.continue", character, businessMainActivity.getMainCustomers().get(0)));
									}
								}
							}
						}
					} else if (activity instanceof BusinessSecondaryActivity) {

					}
				}
			}
		}
	}

	public static class ReptilianMotivation extends TraitEffect {
		/*
		 * Necessary Motivation stuff exists in "Fight.java" "getStatModification()" as well as in MonsterFight (same Method)
		 */		
	}

	/* *********
	 * Vulpine *
	 ***********/

	public static class Vulpine extends TraitEffect {
		//TODO: Stage 2, Stage 3, Stage 4, Stage 5 and Downside	
		int stage = 0,
				stagelimit = 5, // max number of stages
				stagestep = 10; // every 50 points in Transformation gets you another stage

		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			float value = character.getAttribute(SpecializationAttribute.TRANSFORMATION).getInternValue();

			stage = (int) (value / stagestep);
			if (stage > stagelimit) stage = stagelimit;
			PerkUtil.addAndReturn(character, Trait.TRANSFORMATION).setText("VULPINESTAGE"+stage);

			switch(stage) {
			case 5: PerkUtil.addMaybe(character, Trait.BEASTINHEAT);
			case 4:
			case 3: 
			case 2: 
			case 1: 
			}
		}

		@Override
		public float getAttributeModifier(Attribute attribute) {
			float value = attribute.getInternValue();
			float multiplier = stage * 0.05f;
			if (attribute.getAttributeType() == SpecializationAttribute.SEDUCTION) {
				return value * multiplier;
			}
			return 0;
		}	

		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.CONTROL) {
				return currentValue * 2;
			}
			return currentValue;
		}

		@Override
		public boolean removeTrait(Charakter character) {
			PerkUtil.removeMaybe(character, Trait.TRANSFORMATION);
			PerkUtil.removeMaybe(character, Trait.BEASTINHEAT);
			return super.removeTrait(character);
		}
	}	

	/**
	 * Every 30 days the slave goes into heat.
	 * within a 3 day period the slave gets conditions with increasing bonuses.
	 * Day 1: +10% Pregnancy Chance, +50% Customer Satisfaction
	 * Day 2: +20% Pregnancy Chance, +75% Customer Satisfaction
	 * Day 3: +50% Pregnancy Chance, +150% Customer Satisfaction, +10 Customers per Shift
	 */
	public static class BeastInHeat extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, final Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {

				boolean heat = false;
				boolean pregnant = false;
				if (Jasbro.getInstance().getData().getDay()%30 == 0) {
					heat = true;
					for (Condition con : character.getConditions())
						if (con instanceof PregnancyInterface) {
							pregnant = true;
							break;
						}

				}
				/*
				 * Buffs applied last for 4 days, so that they can be checked upon day turn
				 * After the check the buffs are removed and the next stage is applied
				 * The buffs itself do nothing, so the conditions contains all the information
				 * 
				 * I've picked BattleCondition as that is actually an empty condition which is easy to work with
				 */
				List<Condition> list = character.getConditions();
				if (heat && !pregnant) {

					character.addCondition(h1);

					character.addCondition(new BattleCondition(character) {

						@Override
						public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
							if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
								return currentValue + 10;
							}
							else {
								return currentValue;
							}
						}

						@Override
						public void handleEvent(MyEvent e) {
							if (e.getType() == EventType.ACTIVITY) {
								RunningActivity activity = (RunningActivity) e.getSource();
								activity.getMainCustomer().addToSatisfaction(
										(int) (activity.getMainCustomer().getSatisfactionAmount()/2),
										this
										);
							} else if (e.getType() == EventType.NEXTDAY) {

								getCharacter().removeCondition(this);

							}
							super.handleEvent(e);
						}
					});

				} else if(list.contains(h1)) {

					character.addCondition(h2);
					character.removeCondition(h1);

					character.addCondition(new BattleCondition(character) {

						@Override
						public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
							if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
								return currentValue + 20;
							}
							else {
								return currentValue;
							}
						}

						@Override
						public void handleEvent(MyEvent e) {
							if (e.getType() == EventType.ACTIVITY) {
								RunningActivity activity = (RunningActivity) e.getSource();
								activity.getMainCustomer().addToSatisfaction(
										(int) (activity.getMainCustomer().getSatisfactionAmount()*0.75),
										this
										);
							} else if (e.getType() == EventType.NEXTDAY) {
								getCharacter().removeCondition(this);

							}
							super.handleEvent(e);
						}

					});

				} else if (list.contains(h2)) {

					character.addCondition(h3);
					character.removeCondition(h2);

					character.addCondition(new BattleCondition(character) {

						@Override
						public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
							if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
								return currentValue + 50;
							} else if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT)
								return currentValue +10;
							else {
								return currentValue;
							}
						}

						@Override
						public void handleEvent(MyEvent e) {
							if (e.getType() == EventType.NEXTDAY) {
								getCharacter().removeCondition(this);
							}
							else if (e.getType() == EventType.ACTIVITY) {
								RunningActivity activity = (RunningActivity) e.getSource();
								activity.getMainCustomer().addToSatisfaction(
										(int) (activity.getMainCustomer().getSatisfactionAmount()*1.5),
										this
										);
							}
						}

					});
				}

			}

		}	
	}
}
