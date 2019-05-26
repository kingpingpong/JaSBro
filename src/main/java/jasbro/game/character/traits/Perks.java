package jasbro.game.character.traits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.RunningActivity.TargetType;
import jasbro.game.character.activities.sub.Advertise;
import jasbro.game.character.activities.sub.BodyWrap;
import jasbro.game.character.activities.sub.Clean;
import jasbro.game.character.activities.sub.Cook;
import jasbro.game.character.activities.sub.Gardening;
import jasbro.game.character.activities.sub.Orgy;
import jasbro.game.character.activities.sub.Pamper;
import jasbro.game.character.activities.sub.Sex;
import jasbro.game.character.activities.sub.Sleep;
import jasbro.game.character.activities.sub.Sunbathe;
import jasbro.game.character.activities.sub.Swim;
import jasbro.game.character.activities.sub.Talk;
import jasbro.game.character.activities.sub.Train;
import jasbro.game.character.activities.sub.business.Attend;
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.activities.sub.business.BathAttendant;
import jasbro.game.character.activities.sub.business.Fight;
import jasbro.game.character.activities.sub.business.Massage;
import jasbro.game.character.activities.sub.business.MonsterFight;
import jasbro.game.character.activities.sub.business.SellFood;
import jasbro.game.character.activities.sub.business.Strip;
import jasbro.game.character.activities.sub.business.SubmitToMonster;
import jasbro.game.character.activities.sub.whore.Dominate;
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
import jasbro.game.character.conditions.SunEffect;
//import jasbro.game.character.conditions.TraitEffect;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.CustomersArriveEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.events.business.SpawnData.CustomerData;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.game.items.AccessoryType;
import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.RapeEvent;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Perks {	

	public static class SimpleTraitEffect extends TraitEffect {
		private List<CalculatedAttribute> calculatableAttributes;
		private List<AttributeType> otherAttributes;
		private Double addValue;
		private Double multValue;

		public SimpleTraitEffect(final Double addValue, final Double multValue, final AttributeType... attributes) {
			calculatableAttributes = new ArrayList<CalculatedAttribute>();
			otherAttributes = new ArrayList<AttributeType>();
			for (AttributeType attributeType : attributes) {
				if (attributeType instanceof CalculatedAttribute) {
					calculatableAttributes.add((CalculatedAttribute) attributeType);
				}
				else {
					otherAttributes.add(attributeType);
				}
			}
			this.addValue = addValue;
			this.multValue = multValue;
		}

		@Override 
		public double getAttributeModified(final CalculatedAttribute calculatedAttribute, final double currentValue, final Charakter character) {
			if(calculatableAttributes.contains(calculatedAttribute)) {
				double temp = currentValue;
				if(multValue != null) {
					temp *= multValue;
				}
				if(addValue != null) {
					temp += addValue;
				}
				return temp;
			}

			return currentValue;
		}

		@Override
		public float getAttributeModifier(Attribute attribute) {
			if (otherAttributes.contains(attribute.getAttributeType())) {
				double temp = 0;
				if(multValue != null) {
					temp = (multValue - 1) * attribute.getValue();
				}
				if(addValue != null) {
					temp += addValue;
				}
				return (float) temp;
			}
			else {
				return super.getAttributeModifier(attribute);
			}
		}
	}

	//Add Attribute value to max value
	public static class AttributeMaxInfluence extends TraitEffect {
		private AttributeType targetAttributeType;
		private AttributeType sourceAttributeType;
		private float modifier;


		public AttributeMaxInfluence(AttributeType targetAttributeType, AttributeType sourceAttributeType, float modifier) {
			this.targetAttributeType = targetAttributeType;
			this.sourceAttributeType = sourceAttributeType;
			this.modifier = modifier;
		}

		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ATTRIBUTECHANGED) {
				AttributeChangedEvent attributeChangedEvent = (AttributeChangedEvent) e;
				Attribute attribute = attributeChangedEvent.getAttribute();
				if (attribute.getAttributeType() == sourceAttributeType) {
					Attribute targetAttribute = character.getAttribute(targetAttributeType);
					int newModifier = (int) (attribute.getInternValue()  * modifier);
					int oldModifier = (int) ((attribute.getInternValue() - attributeChangedEvent.getAmount()) * modifier);
					targetAttribute.setMaxValue(targetAttribute.getMaxValue() + (newModifier - oldModifier));
				}
			}
		}


		@Override
		public boolean addTrait(Charakter character) {
			Attribute sourceAttribute = character.getAttribute(sourceAttributeType);
			Attribute targetAttribute = character.getAttribute(targetAttributeType);
			targetAttribute.setMaxValue(targetAttribute.getMaxValue() + 
					((int) (sourceAttribute.getInternValue() * modifier)));
			return true;
		}

		public boolean removeTrait(Charakter character) {
			Attribute sourceAttribute = character.getAttribute(sourceAttributeType);
			Attribute targetAttribute = character.getAttribute(targetAttributeType);
			targetAttribute.setMaxValue(targetAttribute.getMaxValue() - 
					((int) (sourceAttribute.getInternValue() * modifier)));
			return true;
		}
	}

	//Add attribute value to another attribute value
	public static class AttributeInfluence extends TraitEffect {
		private AttributeType targetAttributeType;
		private AttributeType sourceAttributeType;
		private float modifier;


		public AttributeInfluence(AttributeType targetAttributeType, AttributeType sourceAttributeType, float modifier) {
			this.targetAttributeType = targetAttributeType;
			this.sourceAttributeType = sourceAttributeType;
			this.modifier = modifier;
		}

		@Override
		public float getAttributeModifier(Attribute attribute) {
			if (attribute.getAttributeType() == targetAttributeType) {
				return attribute.getCharacter().getAttribute(sourceAttributeType).getInternValue() * modifier;
			}
			else {
				return 0;
			}
		}
	}


	/**
	 * Small class to help easily add and remove traits to characters if not already there
	 * 
	 * @author Scythless
	 */
	public static class PerkUtil {
		/**
		 * Add to the character the trait if it's not already there.
		 * Needs the List of trait of the character to check if it contains the given trait.
		 * 
		 * @param character 
		 * 		The Character to get the trait
		 * @param trait 
		 * 		The trait to be checked for and added
		 * @return
		 * 		Returns true if the trait was added, false if it did not
		 */
		public static boolean addMaybe(Charakter character, Trait trait) {
			if(!character.getTraits().contains(trait)) {
				character.addTrait(trait);
				return true;
			}
			return false;
		}

		/**
		 * Remove from the character the trait if it's there.
		 * Needs the List of trait of the character to check if it contains the given trait.
		 * 
		 * @param character 
		 * 		The Character to remove the trait from
		 * @param trait 
		 * 		The trait to be checked for and removed
		 * @return
		 * 		Returns true if the trait was removed, false if it was not
		 */
		public static boolean removeMaybe(Charakter character, Trait trait) {
			if(character.getTraits().contains(trait)) {
				character.removeTrait(trait);
				return true;
			}
			return false;
		}

		/**
		 * Get the character owned instance of a given trait
		 * 
		 * @param character The character to get the trait from
		 * @param trait The trait which should be returned
		 * @return Returns either the character owned instance of the trait OR if the character does not have said trait it return NULL
		 */
		public static Trait getTraitFrom(Charakter character, Trait trait) {
			for (Trait t : character.getTraits() ) {
				if (t == trait) {
					return t;
				}
			}
			return null;
		}

		/**
		 * Adds a trait to the character if that one is not already there
		 * After that returns the character owned instance of the trait given.
		 * 
		 * @param character The character the trait should be given to and whose trait should be returned
		 * @param trait The trait that should be given to the character and which is then returned.
		 * @return Returns the given trait the character has
		 */
		public static Trait addAndReturn(Charakter character, Trait trait) {
			addMaybe(character, trait);
			return getTraitFrom(character, trait);
		}
	}

}