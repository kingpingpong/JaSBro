package jasbro.game.character.traits;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
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
import jasbro.game.character.conditions.OvipositionPregnancy;
import jasbro.game.character.conditions.SunEffect;
//import jasbro.game.character.conditions.TraitEffect;
import jasbro.game.character.specialization.SpecializationAttribute;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Perks {

	static Buff h1 = new Buff.Heat1(),
			 h2 = new Buff.Heat2(),
			 h3 = new Buff.Heat3();
	
	//Kinky Tree
	public static class Pervert extends TraitEffect {//Adds 5000 Fame
		@Override
		public boolean addTrait(Charakter character) {
			character.getFame().modifyFame(5000);
			return true;
		}

		@Override
		public boolean removeTrait(Charakter character) {
			character.getFame().modifyFame(-5000);
			return true;
		}
	}
	public static class Recognized extends TraitEffect {//Goes through all incoming customers and increases their wealth by 20%
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					customer.setInitialMoney((int) (customer.getInitialMoney()*1.20));

				}	

			}
		}
	}
	//The following increases satisfaction for a certain act.
	public static class CozyCunt extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					if(((Whore) activity).getSexType()==Sextype.VAGINAL){
						activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/4, trait);
					}
				}
			}
		}
	}
	public static class RowdyRump extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					if(((Whore) activity).getSexType()==Sextype.ANAL){
						activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/4, trait);
					}
				}
			}
		}
	}
	public static class SlurpySlurp extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					if(((Whore) activity).getSexType()==Sextype.ORAL){
						activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/4, trait);
					}
				}
			}
		}
	}
	public static class TouchyFeely extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					if(((Whore) activity).getSexType()==Sextype.FOREPLAY){
						activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/4, trait);
					}
				}
			}
		}
	}
	public static class PuffPuff extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					if(((Whore) activity).getSexType()==Sextype.TITFUCK){
						activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/4, trait);
					}
				}
			}
		}
	}
	public static class BedroomPrincess extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/4, trait);
				}
			}
		}
	}
	//The following go change the customer rating based on their prefered sex type
	//Customers with higher ratings are served first.
	public static class WetForYou extends TraitEffect {
		@Override
		public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {

			if(customer.getPreferredSextype()==Sextype.VAGINAL){
				rating *= 4;
			}

			else {
				rating *= 0.3f;
			}

			return rating;
		}
	}
	public static class BackdoorOpen extends TraitEffect {
		@Override
		public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {

			if(customer.getPreferredSextype()==Sextype.ANAL){
				rating *= 4;
			}

			else {
				rating *= 0.3f;
			}

			return rating;
		}
	}
	public static class Thirsty extends TraitEffect {
		@Override
		public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {

			if(customer.getPreferredSextype()==Sextype.ORAL){
				rating *= 4;
			}

			else {
				rating *= 0.3f;
			}

			return rating;
		}
	}
	public static class FeelMeUp extends TraitEffect {
		@Override
		public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {

			if(customer.getPreferredSextype()==Sextype.FOREPLAY){
				rating *= 4;
			}

			else {
				rating *= 0.3f;
			}

			return rating;
		}
	}
	public static class ComeToMommy extends TraitEffect {
		@Override
		public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {

			if(customer.getPreferredSextype()==Sextype.TITFUCK){
				rating *= 4;
			}

			else {
				rating *= 0.3f;
			}

			return rating;
		}
	}
	//Adds satisfaction for all sex acts
	public static class BedGoddess extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/2, trait);
				}
			}
		}
	}
	//Same as above. Kinda redundant, but hey...
	public static class Kinky extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					activity.getMainCustomers().get(0).addToSatisfaction(10+activity.getMainCustomers().get(0).getSatisfactionAmount()/5, trait);
				}
			}
		}
	}
	//Increases fame gained when performing kinky sex
	public static class SexManiac extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Whore whoreActivity = (Whore) activity;
					if ((whoreActivity.getSexType() == Sextype.BONDAGE
							|| whoreActivity.getSexType() == Sextype.GROUP
							|| whoreActivity.getSexType() == Sextype.MONSTER)) {
						activity.setFameModifier(activity.getFameModifier( ) + 0.5f);
					}
				}
			}
		}
	}
	//May trigger some flavor text, which greatly boosts satisfaction.
	public static class Steamy extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore && Util.getInt(0, 100)<10) {
					Whore whoreActivity = (Whore) activity;
					whoreActivity.getMainCustomers().get(0).addToSatisfaction(150+whoreActivity.getMainCustomers().get(0).getSatisfactionAmount(), trait);
					int random=2;
					random=Util.getInt(1, 20);
					if(whoreActivity.getSexType() == Sextype.VAGINAL)
					{ 
						if(random>=10){
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.vaginal1",character, whoreActivity.getMainCustomer()));
						}
						else{
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.vaginal2",character, whoreActivity.getMainCustomer()));
						}
					}
					if(whoreActivity.getSexType() == Sextype.ORAL)
					{ 
						if(random>=10){
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.oral1",character, whoreActivity.getMainCustomer()));
						}
						else{
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.oral2",character, whoreActivity.getMainCustomer()));
						}
					}
					if(whoreActivity.getSexType() == Sextype.ANAL)
					{ 
						if(random>=10){
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.anal1",character, whoreActivity.getMainCustomer()));
						}
						else{
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.anal2",character, whoreActivity.getMainCustomer()));
						}
					}
					if(whoreActivity.getSexType() == Sextype.FOREPLAY)
					{ 
						if(random>=10){
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.foreplay1",character, whoreActivity.getMainCustomer()));
						}
						else{
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.foreplay2",character, whoreActivity.getMainCustomer()));
						}
					}
					if(whoreActivity.getSexType() == Sextype.TITFUCK)
					{ 
						if(random>=10){
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.titfuck1",character, whoreActivity.getMainCustomer()));
						}
						else{
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.titfuck2",character, whoreActivity.getMainCustomer()));
						}
					}
					if(whoreActivity.getSexType() == Sextype.GROUP)
					{ 
						if(random>=10){
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.group1",character, whoreActivity.getMainCustomer()));
						}
						else{
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STEAMY.group2",character, whoreActivity.getMainCustomer()));
						}
					}

				}

			}
		}
	}
	//Character will always obey
	//Customers may get rough, increasing their satisfaction and making the act more exhausting
	public static class Submissive extends TraitEffect {
		//Set minimum obedience to 0
		@Override
		public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
			return 0;
		}
		//Make customers rougher
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {

					Whore whoreActivity = (Whore) activity;
					if(Util.getInt(1, 5)==1 || (Util.getInt(1, 2)==1 && (whoreActivity.getMainCustomer().getStatus()==CustomerStatus.PISSED || whoreActivity.getMainCustomer().getStatus()==CustomerStatus.DRUNK))){
						//Increase Health and Energy costs
						for (AttributeModification attributeModification : activity.getAttributeModifications()) {
							if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
								float modification = attributeModification.getBaseAmount();
								float change = -Math.abs(modification)*0.1f;
								attributeModification.addModificator(change-1.0f);
							}
							if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
								float modification = attributeModification.getBaseAmount();
								float change = -Math.abs(modification)*0.4f;
								attributeModification.addModificator(change);
							}
						}
						//Add satisfaction and flavor text.
						whoreActivity.getMainCustomers().get(0).addToSatisfaction(whoreActivity.getMainCustomers().get(0).getSatisfactionAmount()/5, trait);
						if(whoreActivity.getSexType() == Sextype.ORAL)
						{ 
							if(Util.getInt(0, 10)<5){activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SUBMISSIVE.rough.oral.one",character));}
							else{activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SUBMISSIVE.rough.oral.two",character));}
						}
						if(whoreActivity.getSexType() == Sextype.VAGINAL || whoreActivity.getSexType() == Sextype.ANAL)
						{ 
							if(Util.getInt(0, 10)<5){activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SUBMISSIVE.rough.one",character));}
							else{activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SUBMISSIVE.rough.two",character));}
						}
						if(whoreActivity.getSexType() == Sextype.GROUP)
						{ 
							if(Util.getInt(0, 10)<5){activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SUBMISSIVE.rough.group.one",character));}
							else{activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SUBMISSIVE.rough.group.two",character));}
							character.addCondition(new Buff.RoughenedUp());
						}

					}

				}
				//Same for monsters
				if (activity instanceof SubmitToMonster && Util.getInt(0, 100)>40) {
					character.addCondition(new Buff.RoughenedUp());
					if(Util.getInt(0, 10)<5){activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SUBMISSIVE.monster.one",character));}
					else{activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SUBMISSIVE.monster.two",character));}
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
							float modification = attributeModification.getBaseAmount();
							float change = -Math.abs(modification)*0.3f;
							attributeModification.addModificator(change-1.0f);
						}
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
							float modification = attributeModification.getBaseAmount();
							float change = -Math.abs(modification)*0.6f;
							attributeModification.addModificator(change);
						}
					}
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(1 + character.getFinalValue(Sextype.MONSTER)*2, activity);
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
						}
						int bonusSatisfaction=(100-character.getFinalValue(EssentialAttributes.ENERGY))*2;
						whoreActivity.getMainCustomers().get(0).addToSatisfaction(bonusSatisfaction, trait);
					}
				}
			}
		}
	}
	//Reduces energy loss based on lost energy.
	public static class MeatToilet extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore || activity instanceof SubmitToMonster) {
					int reduction=(100-character.getFinalValue(EssentialAttributes.ENERGY))*2;
					if(reduction>90){reduction=90;}
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY && Util.getInt(0, 100)>60) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification)*reduction/100;
							attributeModification.addModificator(change);
						}
					}
				}
			}
		}
	}
	//Monster sex costs no health and no energy
	public static class MonsterSow extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof BusinessMainActivity) {
					if (activity instanceof SubmitToMonster) {
						for (AttributeModification attributeModification : activity.getAttributeModifications()) {
							if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification)*1.0f;
								attributeModification.addModificator(change);
							}
							if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification)*0.5f;
								attributeModification.addModificator(change);
							}
						}
					}
				}
			}
		}
	}
	//Increase max customers by 50%
	//Adds a 20% to greatly reduce energy cost
	public static class SexAddict extends TraitEffect{
		@Override
		public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {

			if(customer.getStatus()==CustomerStatus.STRONGSTATUS){
				rating *= 1.1f;
			}

			else {
				rating *= 0.9f;
			}

			return rating;
		}
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT) {
				return currentValue *1.5;
			}
			else {
				return currentValue;
			}
		}
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY && Util.getInt(0, 100)>80) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification);
							attributeModification.addModificator(change);
							if(Util.getInt(0, 10)<5){activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SEXADDICT.another.one",character));}
							else{activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SEXADDICT.another.two",character));}
						}
					}
				}
			}
		}
	}
	//Oral and group sex may give buffs.
	public final static class CumSlut extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Whore whoreActivity = (Whore) activity;
					if ((whoreActivity.getSexType() == Sextype.GROUP && whoreActivity.getMainCustomers().size() > 0) || whoreActivity.getSexType() == Sextype.ORAL) {
						int magnitude=whoreActivity.getMainCustomers().size()*2;
						character.getAttribute(EssentialAttributes.ENERGY).addToValue(magnitude*4.0f, activity);
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("CUMSLUT.enjoy", character));
						if(Util.getInt(0, 100)>90){
							//Add buff
							character.addCondition(new Buff.Satiated(magnitude,character));
							character.getAttribute(EssentialAttributes.HEALTH).addToValue(magnitude*1.0f, activity);
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("CUMSLUT.drink", character));
						}
						if(Util.getInt(0, 100)>90){
							//Add buff
							character.addCondition(new Buff.SmoothSkin(magnitude,character));
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("CUMSLUT.smear", character));
						}
					}
				}
				if (activity instanceof SubmitToMonster) {
					character.getAttribute(EssentialAttributes.ENERGY).addToValue(4.0f, activity);
					activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("CUMSLUT.enjoy.monster", character));
					if (Util.getInt(0, 100) > 50) {
						character.addCondition(new Buff.Satiated(15,character));
						character.getAttribute(EssentialAttributes.HEALTH).addToValue(4.0f, activity);
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("CUMSLUT.drink.monster", character));
					}
					if (Util.getInt(0, 100) > 50) {
						character.addCondition(new Buff.SmoothSkin(20, character ));
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("CUMSLUT.smear.monster", character));
					}

				}
			}
		}
	}
	//Strip may give a buff.
	public final static class Horny extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Strip || activity instanceof Attend) {
					if(Util.getInt(0, 100)>40)
					{
						int rand=Util.getInt(1, 3);
						if(rand==1){activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STRIP.horny.one",character));}
						if(rand==2){activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STRIP.horny.two",character));}
						if(rand==3){activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STRIP.horny.three",character));}            		
						character.addCondition(new Buff.HornyBuff(character));
					}
				}
			}
		}
	}
	//Increases  womb stuff
	public static class Breeder extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
				return currentValue + 10;
			}
			else if (calculatedAttribute == CalculatedAttribute.MINCHILDREN) {
				return currentValue + 1;
			}
			else if (calculatedAttribute == CalculatedAttribute.MAXCHILDREN) {
				return currentValue + 2;
			}
			else if (calculatedAttribute == CalculatedAttribute.CHANCEADDITIONALCHILD) {
				return currentValue + 25;
			}
			else {
				return currentValue;
			}
		}
	}
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
							//Cha,ge energy loss to an energy gain.
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
	//Increase advertising base effectiveness by 10
	public static class Exhibitionist extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					Advertise advertiseActivity = (Advertise) activity;   
					advertiseActivity.setStartingEffectiveness(advertiseActivity.getStartingEffectiveness()+10);				}
			}
		}
	}
	//Same
	public static class Salesperson extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					Advertise advertiseActivity = (Advertise) activity;   
					advertiseActivity.setStartingEffectiveness(advertiseActivity.getStartingEffectiveness()+10);				}
			}
		}
	}
	//The following go through all incoming customers and may change their prefered sex type.
	public static class OverwriteBondage extends TraitEffect{
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					if(Util.getInt(1, 20)==5 && customer.getType()!=CustomerType.GROUP){
						customer.setPreferredSextype(Sextype.BONDAGE);
					}					
				}				
			}
		}
	}
	public static class OverwriteMonster extends TraitEffect{
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					if(Util.getInt(1, 20)==5 && customer.getType()!=CustomerType.GROUP){
						customer.setPreferredSextype(Sextype.MONSTER);
					}					
				}				
			}
		}
	}
	public static class OverwriteVaginal extends TraitEffect{
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					if(Util.getInt(1, 20)==5 && customer.getType()!=CustomerType.GROUP){
						customer.setPreferredSextype(Sextype.VAGINAL);
					}					
				}				
			}
		}
	}
	public static class OverwriteAnal extends TraitEffect{
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					if(Util.getInt(1, 20)==5 && customer.getType()!=CustomerType.GROUP){
						customer.setPreferredSextype(Sextype.ANAL);
					}					
				}				
			}
		}
	}
	public static class OverwriteForeplay extends TraitEffect{
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					if(Util.getInt(1, 20)==5 && customer.getType()!=CustomerType.GROUP){
						customer.setPreferredSextype(Sextype.FOREPLAY);
					}					
				}				
			}
		}
	}
	public static class OverwriteTitfuck extends TraitEffect{
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					if(Util.getInt(1, 20)==5 && customer.getType()!=CustomerType.GROUP){
						customer.setPreferredSextype(Sextype.TITFUCK);
					}					
				}				
			}
		}
	}
	public static class OverwriteOral extends TraitEffect{
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					if(Util.getInt(1, 20)==5 && customer.getType()!=CustomerType.GROUP){
						customer.setPreferredSextype(Sextype.ORAL);
					}					
				}				
			}
		}
	}

	//Increase advertising effectiveness based on Energy
	public static class Spirited extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					if(character.getEnergy()>80){
						Advertise advertiseActivity = (Advertise) activity;   
						advertiseActivity.setStartingEffectiveness(advertiseActivity.getStartingEffectiveness()+10);
					}
				}
			}
		}
	}
	//The following generate bonus customers of a certain type
	public static class TargetBum extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					//Create a new list
					List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
					List<House> listHouses = new ArrayList<House>();
					int skill=character.getFinalValue(SpecializationAttribute.ADVERTISING)/20;
					skill+=character.getFinalValue(BaseAttributeTypes.CHARISMA)/10;
					//Add customers to the list
					bonusCustomers.add(new CustomerData(CustomerType.BUM, skill));
					//Spawn them
					for (House house : listHouses) {
						SpawnData spawnData = house.getSpawnData();
						for (CustomerData bonusCustData : bonusCustomers) {
							spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
						}
					}
				}
			}
		}
	}
	public static class TargetPeasants extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
					List<House> listHouses = new ArrayList<House>();
					int skill=character.getFinalValue(SpecializationAttribute.ADVERTISING)/25;
					skill+=character.getFinalValue(BaseAttributeTypes.CHARISMA)/10;
					bonusCustomers.add(new CustomerData(CustomerType.PEASANT, skill));	
					for (House house : listHouses) {
						SpawnData spawnData = house.getSpawnData();
						for (CustomerData bonusCustData : bonusCustomers) {
							spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
						}
					}
				}
			}
		}
	}
	public static class TargetSoldiers extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
					List<House> listHouses = new ArrayList<House>();
					int skill=character.getFinalValue(SpecializationAttribute.ADVERTISING)/30;
					skill+=character.getFinalValue(BaseAttributeTypes.CHARISMA)/10;
					bonusCustomers.add(new CustomerData(CustomerType.SOLDIER, skill));	
					for (House house : listHouses) {
						SpawnData spawnData = house.getSpawnData();
						for (CustomerData bonusCustData : bonusCustomers) {
							spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
						}
					}
				}
			}
		}
	}
	public static class TargetBusinessmen extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
					List<House> listHouses = new ArrayList<House>();
					int skill=character.getFinalValue(SpecializationAttribute.ADVERTISING)/35;
					skill+=character.getFinalValue(BaseAttributeTypes.CHARISMA)/10;
					bonusCustomers.add(new CustomerData(CustomerType.BUSINESSMAN, skill));	
					for (House house : listHouses) {
						SpawnData spawnData = house.getSpawnData();
						for (CustomerData bonusCustData : bonusCustomers) {
							spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
						}
					}
				}
			}
		}
	}
	public static class TargetNobles extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
					List<House> listHouses = new ArrayList<House>();
					int skill=character.getFinalValue(SpecializationAttribute.ADVERTISING)/40;
					skill+=character.getFinalValue(BaseAttributeTypes.CHARISMA)/10;
					bonusCustomers.add(new CustomerData(CustomerType.MINORNOBLE, skill));	
					for (House house : listHouses) {
						SpawnData spawnData = house.getSpawnData();
						for (CustomerData bonusCustData : bonusCustomers) {
							spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
						}
					}
				}
			}
		}
	}
	public static class TargetLords extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
					List<House> listHouses = new ArrayList<House>();
					int skill=character.getFinalValue(SpecializationAttribute.ADVERTISING)/45;
					skill+=character.getFinalValue(BaseAttributeTypes.CHARISMA)/10;
					bonusCustomers.add(new CustomerData(CustomerType.LORD, skill));	
					for (House house : listHouses) {
						SpawnData spawnData = house.getSpawnData();
						for (CustomerData bonusCustData : bonusCustomers) {
							spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
						}
					}
				}
			}
		}
	}
	public static class TargetCelebrities extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
					List<House> listHouses = new ArrayList<House>();
					int skill=character.getFinalValue(SpecializationAttribute.ADVERTISING)/50;
					skill+=character.getFinalValue(BaseAttributeTypes.CHARISMA)/10;
					bonusCustomers.add(new CustomerData(CustomerType.CELEBRITY, skill));	
					for (House house : listHouses) {
						SpawnData spawnData = house.getSpawnData();
						for (CustomerData bonusCustData : bonusCustomers) {
							spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
						}
					}
				}
			}
		}
	}
	public static class TargetGroups extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Advertise) {
					List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
					List<House> listHouses = new ArrayList<House>();
					int skill=character.getFinalValue(SpecializationAttribute.ADVERTISING)/50;
					bonusCustomers.add(new CustomerData(CustomerType.GROUP, skill));	
					for (House house : listHouses) {
						SpawnData spawnData = house.getSpawnData();
						for (CustomerData bonusCustData : bonusCustomers) {
							spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
						}
					}
				}
			}
		}
	}

	//Customers pay 30% more
	//Bartender
	public static class LiquorMaster extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend || activity instanceof Attend) {
					for (Customer customer : activity.getCustomers()) {
						customer.changePayModifier(0.3f);
						if(Util.getInt(1, 3)==1 && customer.getStatus()==CustomerStatus.DRUNK){customer.setStatus(CustomerStatus.VERYDRUNK);}
						if(Util.getInt(1, 3)==1 && customer.getStatus()!=CustomerStatus.DRUNK){customer.setStatus(CustomerStatus.DRUNK);}
						
					}
				}
			}
		}
	} 
	public static class Aphrodisiacs extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend || activity instanceof Attend) {
					int occurences =0;
					for (Customer customer : activity.getCustomers()) {
						customer.changePayModifier(0.3f);
						
						if(Util.getInt(1, 3)==1 && customer.getStatus()==CustomerStatus.HORNYSTATUS){customer.setStatus(CustomerStatus.VERYHORNY); occurences++;}
						if(Util.getInt(1, 3)==1 && (customer.getStatus()==CustomerStatus.DRUNK
								|| customer.getStatus()==CustomerStatus.SAD
								|| customer.getStatus()==CustomerStatus.PISSED)){customer.setStatus(CustomerStatus.DRUNK); occurences++;}
						
					}
					if(occurences!=0){
						Object arguments[] = {occurences};
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("ALCHEMIST.aphrodisiacs",character,arguments));
					}
				}
			}
		}
	} 
	//Increase max customers for bartending based on Int
	public static class Multitasking extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYCREATED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend) {
					Bartend bartend = (Bartend)activity;
					bartend.setBonus(bartend.getBonus()+character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)/2);
				}
			}
		}
	} 
	//Adds Catgirl skill to customer satisfaction.
	public static class CatMaid extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend || activity instanceof Attend) {
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(character.getFinalValue(SpecializationAttribute.CATGIRL)/3, activity);
						if(Util.getInt(1, 3)==1 && customer.getStatus()==CustomerStatus.SAD){customer.setStatus(CustomerStatus.HAPPY);}
					}
				}
			}
		}
	} 
	//Bartending gives more fame
	public static class TheConfident extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend || activity instanceof Attend) {
					activity.setFameModifier(activity.getFameModifier( ) + 0.3f);
					for (Customer customer : activity.getCustomers()) {
					if(Util.getInt(1, 3)==1 && customer.getStatus()==CustomerStatus.SAD){customer.setStatus(CustomerStatus.HAPPY);}
					if(Util.getInt(1, 3)==1 && customer.getStatus()==CustomerStatus.PISSED){customer.setStatus(CustomerStatus.HAPPY);}
					}
				}

			}
		}
	}

	//Bartending may give some bonuses
	public static class Outgoing extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend || activity instanceof Attend) {
					if(Util.getInt(1, 100)<70){
						activity.getAttributeModifications().add(new AttributeModification(0.06f,BaseAttributeTypes.INTELLIGENCE, character));
					}
					if(Util.getInt(1,100)<70){
						activity.getAttributeModifications().add(new AttributeModification(0.06f, BaseAttributeTypes.CHARISMA, character));
					}
				}
			}
		}
	} 
	//Decrease Max customers, increase satisfaction (strip)
	public static class OrientalCharms extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYCREATED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Strip) {
					Strip strip = (Strip)activity;
					strip.setBonus(strip.getBonus()-10);
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(character.getFinalValue(SpecializationAttribute.STRIP)*2, activity);
						if(Util.getInt(1, 3)==1 && customer.getStatus()==CustomerStatus.HYPED){customer.setStatus(CustomerStatus.HORNYSTATUS);}
					}
				}
			}
		}
	} 
	//Search for the richest customer, milk him dry.
	public static class TargetAudience extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if ((activity instanceof Strip || activity instanceof Attend) && Util.getInt(1, 5)==4) {
					Customer richest = null;
					for (Customer customer : activity.getCustomers()) {
						if(richest==null){richest=customer;}
						if(customer.getMoney()>richest.getMoney()){richest=customer;}
					}
					if(richest!=null){
						if(richest.getType()!=CustomerType.BUM && richest.getType()!=CustomerType.PEASANT)
						{
							int tips = (int)(richest.getMoney() / (1000.0 / character.getFinalValue(SpecializationAttribute.STRIP)) + Util.getInt(10, 20));
							activity.modifyIncome(tips);
							Object arguments[] = {richest.getName(), tips};
							richest.addToSatisfaction(character.getFinalValue(SpecializationAttribute.STRIP)*2, activity);
							if(richest.getStatus()==CustomerStatus.HORNYSTATUS){richest.setStatus(CustomerStatus.VERYHORNY);}
							else{richest.setStatus(CustomerStatus.HORNYSTATUS);}
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STRIP.target",character,arguments));
						}
					}
				}
			}
		}
	} 

	//May increase the amount of gold customers start with.
	public static class TheUntouchable extends TraitEffect{
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					if(Util.getInt(1, 20)==5){customer.setInitialMoney((int) (customer.getInitialMoney()*2));}
					if(Util.getInt(1, 200)==5){customer.setInitialMoney((int) (customer.getInitialMoney()*5));}
				}
			}
		}
	}
	//Increase Max customers.
	public static class CrowdLover extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYCREATED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Strip) {
					Strip strip = (Strip)activity;
					strip.setBonus(strip.getBonus()+20);
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(activity.getCustomers().size()*2, activity);
					}
				}
			}
		}
	}  
	//Increase Fame gain
	public static class Teaser extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Strip || activity instanceof Attend) {
					activity.setFameModifier(activity.getFameModifier( ) + 0.3f);
				}
			}
		}
	}
	//Decrease Energy loss
	public static class Lascivious extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Strip || activity instanceof Attend) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification)*0.3f;
							attributeModification.addModificator(change);
						}
					}
				}
			}
		}
	}
	public static class Trendy extends TraitEffect {

		private static final String[] KEYS = { "STRIP.bunny", "STRIP.cowgirl", "STRIP.bellydancer", "STRIP.schoolgirl",
			"STRIP.microbikini", "STRIP.maid", "STRIP.chainmailbikini", "STRIP.catsuit" };
		private static final int[] REQUIRED_SKILL = { 200, 50, 50, 50, 200, 200, 200, 200 };
		private static final int[] SKILL_SCALE = { 10, 2, 2, 2, 10, 10, 10, 10 };
		private static final int[] RAND_MAX = { 50, 15, 15, 15, 50, 50, 50, 50 };
		private static final AttributeType[] REQUIRED_ATTRIBUTES = { SpecializationAttribute.STRIP,
			BaseAttributeTypes.STAMINA, BaseAttributeTypes.CHARISMA, BaseAttributeTypes.INTELLIGENCE,
			SpecializationAttribute.SEDUCTION, SpecializationAttribute.CLEANING, SpecializationAttribute.VETERAN ,
			SpecializationAttribute.CATGIRL };

		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Strip || activity instanceof Attend) {
					MessageData message = activity.getMessages().get(0);
					int random = Util.getInt(0, KEYS.length);

					message.addToMessage("\n" + TextUtil.t(KEYS[random], character));
					int skill = character.getFinalValue(REQUIRED_ATTRIBUTES[random])
							+ Util.getInt(0, RAND_MAX[random]);
					if (skill > REQUIRED_SKILL[random]) {
						skill *= 2;
						message.addToMessage(" " + TextUtil.t(KEYS[random] + ".win", character));
					}
					else {
						skill /= 2;
						message.addToMessage(TextUtil.t("STRIP.fail"));
					}
					for (Customer c : activity.getCustomers()) {
						c.addToSatisfaction(skill / SKILL_SCALE[random], activity);
					}
				}
			}
		}
	}
	//Increase energy costs and satisfaction
	public static class Acrobatics extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Strip || activity instanceof Attend) {
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(1 + character.getFinalValue(SpecializationAttribute.STRIP) / 9, activity);	
					}
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
							float modification = attributeModification.getBaseAmount();
							float change = -Math.abs(modification)*0.2f;
							attributeModification.addModificator(change);
						}
					}
				}
			}
		}
	}
	//Bonus satisfaction for high class customers
	public static class Refined extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Strip || activity instanceof Attend) {
					for (Customer customer : activity.getCustomers()) {
						if ((customer.getType() == CustomerType.CELEBRITY
								|| customer.getType() == CustomerType.LORD
								|| customer.getType() == CustomerType.MINORNOBLE 
								|| customer.getType() == CustomerType.BUSINESSMAN)) {
							customer.addToSatisfaction(1 + character.getFinalValue(SpecializationAttribute.STRIP) / 10, activity);
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
					house.modDirt(-5-character.getFinalValue(SpecializationAttribute.CLEANING)/48);
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
						int skill = 10 + character.getFinalValue(SpecializationAttribute.COOKING) / 24;
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
	
	public static class RelaxingIncences extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Clean) {
					House house = activity.getHouse();
					if (house != null) {
						int skill = 10 + character.getFinalValue(SpecializationAttribute.CLEANING) / 24;
						if (skill > 50) {
							skill = 50;
						}
						for (Room room : house.getRooms()) {
							for (Charakter target : room.getCurrentUsage().getCharacters()) {
								if(Util.getInt(0, 3)==2){
									target.addCondition(new Buff.Stoned(skill,target, Util.getInt(-50, 50)));
									activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("ALCHEMIST.incence",character, target));
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
						int skill = 10+character.getFinalValue(SpecializationAttribute.CLEANING)/24;
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
					int skill=character.getFinalValue(SpecializationAttribute.CLEANING)+character.getFinalValue(BaseAttributeTypes.CHARISMA)*5;
					bonusCustomers.add(new CustomerData(CustomerType.CELEBRITY, skill/300));
					bonusCustomers.add(new CustomerData(CustomerType.LORD, skill/250));
					bonusCustomers.add(new CustomerData(CustomerType.MINORNOBLE, skill/220));
					bonusCustomers.add(new CustomerData(CustomerType.GROUP, skill/180));
					bonusCustomers.add(new CustomerData(CustomerType.BUSINESSMAN, skill/200));
					bonusCustomers.add(new CustomerData(CustomerType.MERCHANT, skill/160));
					bonusCustomers.add(new CustomerData(CustomerType.SOLDIER, skill/140));
					bonusCustomers.add(new CustomerData(CustomerType.PEASANT, skill/120));
					bonusCustomers.add(new CustomerData(CustomerType.BUM, skill/100));
					if (activity.getHouse() != null) {
						SpawnData spawnData = activity.getHouse().getSpawnData();
						for (CustomerData bonusCustData : bonusCustomers) {
							spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
						}
					}
				}
				if (activity instanceof Cook) {
					List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
					int skill=character.getFinalValue(SpecializationAttribute.COOKING)+character.getFinalValue(BaseAttributeTypes.CHARISMA)*5;
					bonusCustomers.add(new CustomerData(CustomerType.CELEBRITY, skill/300));
					bonusCustomers.add(new CustomerData(CustomerType.LORD, skill/250));
					bonusCustomers.add(new CustomerData(CustomerType.MINORNOBLE, skill/220));
					bonusCustomers.add(new CustomerData(CustomerType.GROUP, skill/180));
					bonusCustomers.add(new CustomerData(CustomerType.BUSINESSMAN, skill/200));
					bonusCustomers.add(new CustomerData(CustomerType.MERCHANT, skill/160));
					bonusCustomers.add(new CustomerData(CustomerType.SOLDIER, skill/140));
					bonusCustomers.add(new CustomerData(CustomerType.PEASANT, skill/120));
					bonusCustomers.add(new CustomerData(CustomerType.BUM, skill/100));
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
				}
				if (activity instanceof Cook && Util.getInt(0, 100)>10) {
					Attribute attribute = character.getAttribute(SpecializationAttribute.COOKING);
					activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("MAID.improvecooking",character));
					attribute.setMaxValue(attribute.getMaxValue() + 1);
				}
			}
		}
	}
	//Max customers may randomly increase
	public static class KeepEmComing extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT) {
				if(currentValue>=10){
					currentValue+=character.getFinalValue(SpecializationAttribute.SEDUCTION)/50;
				}
			}
			return currentValue;

		}
	}
	//Max customers halved, satisfaction increased
	public static class OurTime extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT) {
				return currentValue/2;
			}
			else {
				return currentValue;
			}
		}
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					activity.getMainCustomers().get(0).addToSatisfaction(50+activity.getMainCustomers().get(0).getSatisfactionAmount()/2, trait);
				}
			}
		}
	}
	//One customer per shift, satisfaction bonus, Energy loss and stats gains increased
	public static class OneNight extends TraitEffect{
		@Override
		public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {

			if(customer.getStatus()==CustomerStatus.HORNYSTATUS || customer.getStatus()==CustomerStatus.VERYHORNY){
				rating *= 1.6;
			}

			else {
				rating *= 0.9f;
			}

			return rating;
		}
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT) {
				return 1;
			}
			else {
				return currentValue;
			}
		}
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					activity.getMainCustomers().get(0).changePayModifier(10f);
					activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()*3, trait);
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {

						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
							float modification = attributeModification.getBaseAmount();
							float change = -Math.abs(modification);
							attributeModification.addModificator(change);
						}
						if (attributeModification.getAttributeType() == Sextype.ORAL || 
								attributeModification.getAttributeType() == Sextype.VAGINAL || 
								attributeModification.getAttributeType() == Sextype.ANAL || 
								attributeModification.getAttributeType() == Sextype.TITFUCK || 
								attributeModification.getAttributeType() == Sextype.FOREPLAY || 
								attributeModification.getAttributeType() == Sextype.BONDAGE || 
								attributeModification.getAttributeType() == Sextype.GROUP) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification)*2.0f;
							attributeModification.addModificator(change);
						}
					}
				}
			}
		}
	}
	//First customer gets bonus satisfaction
	public static class MyFirst extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Long servedToday=character.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString());
					if(servedToday==0){
						activity.getMainCustomers().get(0).addToSatisfaction(65+activity.getMainCustomers().get(0).getSatisfactionAmount(), trait);
					}
				}
			}
		}
	}
	//Increase Base gold for celevrities
	public static class Renowed extends TraitEffect{
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					if(customer.getType()==CustomerType.CELEBRITY){
						customer.setInitialMoney((int) (customer.getInitialMoney()+5000));
					}
				}

			}
		}
	}
	//Bonus satisfaction if alone in a room.
	public static class YouAndMe extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					if(activity.getRoom() != null && activity.getRoom().getAmountPeople() == 1){
						activity.getMainCustomers().get(0).addToSatisfaction(100, trait);
					}
				}
			}
		}
	}
	//Sleeping increases Char
	public static class BeautySleep extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Sleep) {
					activity.getAttributeModifications().add(new AttributeModification(0.05f, BaseAttributeTypes.CHARISMA, character));
				}
			}
		}
	}
	//Bonus satisfaction based on numbr of whores working at the same time.
	public static class Competitive extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				int bonus=0;
				if (activity instanceof Whore) {
					House house = activity.getHouse();
					if (house != null) {
						for (Room room : house.getRooms()) {
							if (room.getAmountPeople() > 0 && room.getSelectedActivity().isCustomerDependent()) {
								bonus += room.getAmountPeople() * 7;
								break;
							}
						}
						activity.getMainCustomers().get(0).addToSatisfaction(bonus, trait);
					}
				}
			}
		}
	}
	//Energy loss -30%
	public static class Endurance extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification)*0.3f;
							attributeModification.addModificator(change);
						}
					}
				}
			}
		}
	}
	//After 7 customers, energy loss reduced. Maybe satisfaction bonus/malus
	public static class SitBack extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore  ) {
					Long servedToday=character.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString());
					if(servedToday>7){
						if (Util.getInt(0, 100)<servedToday*2){
							if (Util.getInt(0, 100)<50){
								activity.getMainCustomers().get(0).addToSatisfaction(-activity.getMainCustomers().get(0).getSatisfactionAmount()/2, trait);				
								activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SITBACK.unhappy",character));}
							else
							{
								activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/2, trait);				
								activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SITBACK.happy",character));}
						}
						for (AttributeModification attributeModification : activity.getAttributeModifications()) {		
							if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
								float modification = attributeModification.getBaseAmount();
								float change = Math.abs(modification)*0.5f;
								attributeModification.addModificator(change);
							}
						}
					}
				}
			}
		}
	}
	//Satisfaction+=Intell
	public static class Chatty extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof BusinessMainActivity) {
					if (activity instanceof Whore) {
						if ((activity.getMainCustomer().getType() != CustomerType.BUM)) {
							activity.getMainCustomers().get(0).addToSatisfaction(10+character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)*2, trait);
							if(activity.getMainCustomers().get(0).getStatus()==CustomerStatus.SAD){
								activity.getMainCustomers().get(0).setStatus(CustomerStatus.HAPPY);
								activity.getMainCustomers().get(0).addToSatisfaction(10+character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)*2, trait);
								activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("CHATTY.sadcustomer",character));
							}
						} 
					}
				}
			}
		}
	}
	//Increase customer rating based on wealth
	public static class HighClass extends TraitEffect {

		@Override
		public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {
			if(customer.getType()==CustomerType.CELEBRITY){
				rating *= 5.0f;
			}
			else if(customer.getType()==CustomerType.LORD){
				rating *= 2.0f;
			}
			else if(customer.getType()==CustomerType.MINORNOBLE){
				rating *= 1.6f;
			}
			else if(customer.getType()==CustomerType.BUSINESSMAN){
				rating *= 1.2f;
			}
			else if(customer.getType()==CustomerType.MERCHANT){
				rating *= 0.8f;
			}
			else if(customer.getType()==CustomerType.SOLDIER){
				rating *= 0.5f;
			}
			else if(customer.getType()==CustomerType.PEASANT){
				rating *= 0.0f;
			}
			else if(customer.getType()==CustomerType.BUM){
				rating *= 0.0f;
			}

			return rating;
		}
	}
	//Increase customer rating of groups
	public static class GangbangQueen extends TraitEffect {
		@Override
		public int modifyCustomerRating(int rating, Customer customer, BusinessMainActivity businessMainActivity) {

			if(customer.getType()==CustomerType.GROUP){
				CustomerGroup group=(CustomerGroup)customer;
				rating *= group.getCustomers().size()*2;
			}

			else {
				rating *= 0.3f;
			}

			return rating;
		}
	}
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
	//Customers who prefer kinky sex bring more gold
	public static class FleshToy extends TraitEffect {
		@Override
		public void handleEvent(MyEvent event, Charakter character, Trait trait) {
			if (event.getType() == EventType.CUSTOMERSARRIVE) {
				CustomersArriveEvent customerEvent = (CustomersArriveEvent) event;
				for (Customer customer : customerEvent.getCustomers()) {
					if(customer.getType()==CustomerType.GROUP || customer.getPreferredSextype()==Sextype.BONDAGE || customer.getPreferredSextype()==Sextype.MONSTER){
						customer.setInitialMoney((int) (customer.getInitialMoney()*1.6));
					}						
				}				
			}
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
	//Every 30 days, Max customers+32 and no energy loss
	public static class Sloppy extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT 
					&& Jasbro.getInstance().getData().getDay()%30 == 0) {
				return currentValue +32;
			}
			else {
				return currentValue;
			}
		}
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore && Jasbro.getInstance().getData().getDay()%30 == 0) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification);
							attributeModification.addModificator(change);
						}
					}
				}
			}
		}
	}

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
	//The folllowing increases some combat thievery stats 
	public static class MindOfTheFighter extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.CRITCHANCE) {
				return currentValue + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE);
			}
			else {
				return currentValue;
			}
		}
	}
	public static class ElementalStudy extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.DAMAGE) {
				return currentValue + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)/10;
			}
			else {
				return currentValue;
			}
		}
	}
	public static class Distraction extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.HIT || calculatedAttribute == CalculatedAttribute.CRITCHANCE) {
				return currentValue + character.getFinalValue(SpecializationAttribute.SEDUCTION)/30;
			}
			else {
				return currentValue;
			}
		}
	}
	public static class IronBody extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.BLOCKCHANCE) {
				return currentValue + character.getFinalValue(BaseAttributeTypes.STRENGTH)*3/2;
			}
			else {
				return currentValue;
			}
		}
	}
	public static class EtherShield extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.BLOCKCHANCE) {
				return currentValue + character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)*3/2;
			}
			else {
				return currentValue;
			}
		}
	}
	public static class CastTime extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.DAMAGE) {
				return currentValue + 10;
			}
			else if (calculatedAttribute == CalculatedAttribute.SPEED){
				currentValue-=10;

				if(currentValue<=0){currentValue=1;}
				return currentValue;
			}
			else {
				return currentValue;
			}
		}
	}
	public static class JackTheRipper extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.CRITCHANCE) {
				return currentValue + 4;
			}
			else if (calculatedAttribute == CalculatedAttribute.CRITDAMAGEAMOUNT){
				return currentValue + 1;
			}
			else {
				return currentValue;
			}
		}
	}
	public static class Plunderer extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.ITEMLOOTCHANCEMODIFIER) {
				return 100;
			}
			else {
				return currentValue;
			}
		}
	}
	public static class DoubleVie extends TraitEffect {
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue,
				Charakter character) {
			if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
				if (calculatedAttribute == CalculatedAttribute.STEALAMOUNTMODIFIER) {
					return currentValue + 30;
				}
				else if (calculatedAttribute == CalculatedAttribute.STEALITEMCHANCE) {
					return currentValue + 30;
				}
				else if (calculatedAttribute == CalculatedAttribute.STEALCHANCE) {
					return currentValue + 30;
				}
			}
			return currentValue;
		}
	}
	public static class NightShade extends TraitEffect {
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue,
				Charakter character) {
			if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {

				if (calculatedAttribute == CalculatedAttribute.STEALITEMCHANCE) {
					return currentValue + 10;
				}
				else if (calculatedAttribute == CalculatedAttribute.STEALAMOUNTMODIFIER) {
					return currentValue + 10;
				}
				else if (calculatedAttribute == CalculatedAttribute.STEALCHANCE) {
					return currentValue + 30;
				}
			}
			return currentValue;

		}
	}
	public static class ShadowBody extends TraitEffect {
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue,
				Charakter character) {
			if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
				if (calculatedAttribute == CalculatedAttribute.DAMAGE) {
					return currentValue + 3;
				}
				else if (calculatedAttribute == CalculatedAttribute.DODGE) {
					return currentValue + 3;
				}
				else if (calculatedAttribute == CalculatedAttribute.HIT) {
					return currentValue + 3;
				}
				else if (calculatedAttribute == CalculatedAttribute.CRITCHANCE) {
					return currentValue + 3;
				}
				else if (calculatedAttribute == CalculatedAttribute.CRITDAMAGEAMOUNT) {
					return currentValue + 1;
				}

			}
			return currentValue;

		}
	}
	public static class Agility extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.HIT || calculatedAttribute == CalculatedAttribute.DODGE) {
				return currentValue + character.getFinalValue(SpecializationAttribute.CATGIRL)*0.01f;
			}
			else {
				return currentValue;
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
						customer.addToSatisfaction(100, activity);	
					}
				}
				if (activity instanceof Bartend) {
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(80, activity);	
					}
				}
				if (activity instanceof BathAttendant) {
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(75, activity);	
					}
				}
				if (activity instanceof SellFood) {
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(75, activity);	
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
	public static class CatPun extends TraitEffect {
		@Override
		public boolean addTrait(Charakter character) {
			character.getFame().modifyFame(20000);
			return true;
		}

		@Override
		public boolean removeTrait(Charakter character) {
			character.getFame().modifyFame(-20000);
			return true;
		}
	}
	public static class CatInHeat extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT 
					&& Jasbro.getInstance().getData().getDay()%30 == 0) {
				return currentValue +10;
			}
			else {
				return currentValue;
			}
		}
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore && Jasbro.getInstance().getData().getDay()%30 == 0) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification)*0.9f;
							attributeModification.addModificator(change);
							MessageData message = activity.getMessages().get(0);
							message.addToMessage("\n"+TextUtil.t("SEXADDICT.another"));
						}
					}
				}
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

	//Slave / trainer trees
	public static class GoodSlave extends TraitEffect {

		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYPERFORMED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Train) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getTargetCharacter() == character && 
								!(attributeModification.getAttributeType() instanceof EssentialAttributes)) {
							attributeModification.addModificator(attributeModification.getBaseAmount() * 0.3f);
						}
					}
				}
			}
		}
	}

	public static class EffectiveTrainer extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYPERFORMED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Train) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (!(attributeModification.getAttributeType() instanceof EssentialAttributes)) {
							attributeModification.addModificator(attributeModification.getBaseAmount() * 0.3f);
						}
					}
				}
			}
		}
	}

	public static class Motivator extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYFINISHED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Talk) {
					for (Charakter curCharacter : activity.getCharacters()) {
						for (Condition condition : curCharacter.getConditions()) {
							if (condition instanceof Buff) {
								Buff buff = (Buff) condition;
								for(Map.Entry<AttributeType, Integer> entry : buff.getAttributeModifiers().entrySet()) {
									if(entry.getKey() instanceof BaseAttributeTypes) {
										entry.setValue(entry.getValue() + 1);
									} else {
										entry.setValue(entry.getValue() + 10);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public static class TanLines extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYFINISHED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Sunbathe) {
					for (Charakter curCharacter : activity.getCharacters()) {
						for (Condition condition : curCharacter.getConditions()) {
							if (condition instanceof SunEffect) {
								SunEffect buff = (SunEffect) condition;
								if(!buff.isSunburn())
								{
									buff.setRemainingTime(buff.getRemainingTime() + 20);
								}
							}
						}
					}
				}
			}
		}
	}
	public static class SkinCare extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYFINISHED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Sunbathe) {
					for (Charakter curCharacter : activity.getCharacters()) {
						for (Condition condition : curCharacter.getConditions()) {
							if (condition instanceof SunEffect) {
								SunEffect buff = (SunEffect) condition;
								if(!buff.isSunburn()) {
									buff.setIntensity(20);
									buff.updateEffect();

								}
							}
						}
					}
				}
			}
		}
	}

	// Nurse
	public static class Indulgence extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Massage) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification)*0.3f;
							attributeModification.addModificator(change);
						}
					}
				}
			}
		}
	}

	public static class Tantric extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Massage) {
					for (Customer customer : activity.getCustomers()) {
						customer.addToSatisfaction(1 + character.getFinalValue(SpecializationAttribute.WELLNESS) / 5, activity);	
					}
				}
			}
		}
	}

	public final static class Oily extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Massage) {
					Massage massageActivity = (Massage) activity;
					int rnd = Util.getInt(0, 100);

					if (rnd > 75) {
						MessageData message = activity.getMessages().get(0);
						message.addToMessage("\n"+TextUtil.t("OILY.like"));
						massageActivity.getMainCustomers().get(0).addToSatisfaction(30, trait);
					}
				}
			}
		}
	}

	public final static class Soapy extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Massage) {
					Massage massageActivity = (Massage) activity;
					int rnd = Util.getInt(0, 100);

					if (rnd > 90) {
						MessageData message = activity.getMessages().get(0);
						message.addToMessage("\n"+TextUtil.t("SOAPY.like", character, activity.getMainCustomer()));
						massageActivity.getMainCustomers().get(0).addToSatisfaction(70, trait);
					}
				}
			}
		}
	}

	public final static class Naiad extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof BathAttendant) {
					BathAttendant bathAttendantActivity = (BathAttendant) activity;

					if (bathAttendantActivity.getsWet(character)) {
						MessageData message = activity.getMessages().get(0);
						message.addToMessage(TextUtil.t("attendBath.result.naiad", character));
						for (Customer customer: bathAttendantActivity.getCustomers()) {
							customer.addToSatisfaction(50, trait);
						}
					}
				} else if (activity instanceof Swim) {
					MessageData message = activity.getMessages().get(0);
					activity.getStatModifications().add(activity.new ModificationData(TargetType.SINGLE, character, 0.15f, BaseAttributeTypes.STAMINA));
					message.addToMessage(TextUtil.t("swim.naiad", character));
				}
			}
		}
	}

	public final static class Dermatologist extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Pamper || activity instanceof BodyWrap) {
					for (Charakter other: activity.getCharacters()) {
						if (!other.equals(character)) {
							Condition sunburn=null;
							for (Condition condition: other.getConditions()) {
								if (condition instanceof SunEffect && ((SunEffect)condition).isSunburn()) {
									sunburn=condition;
								}
							}
							if (sunburn!=null) {
								MessageData message = activity.getMessages().get(0);
								message.addToMessage(TextUtil.t("pamper.sunburn", character, other));
								other.removeCondition(sunburn);
							}
						}
					}

				}
			}
		}
	}

	public final static class Medic extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYPERFORMED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof RapeEvent || activity instanceof Fight) {
					for (AttributeModification modification: activity.getAttributeModifications()) {
						if (modification.getAttributeType()==EssentialAttributes.HEALTH && modification.getBaseAmount()<0) {
							float reduceDamage=Math.min((float)character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)/800f, 0.5f);
							modification.setBaseAmount(modification.getBaseAmount()*(1-reduceDamage));  
							// TODO: message for medical treatment ? 
						}
					}
				}
			}
		}
	}

	public static class SeXpert extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYPERFORMED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Orgy) {

					for (AttributeModification modification: activity.getAttributeModifications()) {
						if (modification.getAttributeType() instanceof Sextype) {
							modification.setBaseAmount(modification.getBaseAmount()*1.25f);
						}
					}
				}

			}
		}
	}	
	public final static class ConsumeAndAdapt extends TraitEffect { //done
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof MonsterFight) {
					if(Util.getInt(0, 100)>40)
					{
						character.addCondition(new Buff.Satiated(3,character));
						character.getAttribute(EssentialAttributes.HEALTH).addToValue(3*1.0f, activity);
						character.getAttribute(BaseAttributeTypes.STRENGTH).addToValue(1.0f, activity);						
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("ALWAYSABIGGERFISH.eat", character));
					}
					
					for (Customer custom : activity.getCustomers()) {
						custom.addToSatisfaction((int) (custom.getSatisfactionAmount()*0.25),trait);
					}
				}
			}
		}
	}	
	public final static class TheBiggerTheyAre extends TraitEffect { //done
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof MonsterFight) {
					character.addCondition(new BattleCondition(character) {
						@Override
						public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
							if (calculatedAttribute == CalculatedAttribute.CRITDAMAGEAMOUNT) {
								return currentValue + 25;
							}
							else {
								return currentValue;
							}
						}
					});
				}
			}
		}
	}	
	public static class IveSeenWorse extends TraitEffect {
		
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT) {
				return currentValue + 2;
			}
			else {
				return currentValue;
			}
		}
		
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					for (AttributeModification attributeModification : activity.getAttributeModifications()) {
						if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
							float modification = attributeModification.getBaseAmount();
							float change = Math.abs(modification)*0.3f;
							attributeModification.addModificator(change);
						}
					}
					if (Util.getInt(0,10) < 2) { //apathetic
						activity.getMainCustomers().get(0).addToSatisfaction(-100, trait);
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("IVESEENWORSE.apathetic", character)); //I have no idea where messages are kept, so I just typed something there.
					}
				}
			}
		}
	}
	
	public final static class MonsterHunter extends TraitEffect { //done
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof MonsterFight) {
					character.addCondition(new BattleCondition(character) {
						@Override
						public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
							if (calculatedAttribute == CalculatedAttribute.HIT) {
								return currentValue * 1.14;
							}	
							if (calculatedAttribute == CalculatedAttribute.DAMAGE) {
								return currentValue + 2;
							}
							if (calculatedAttribute == CalculatedAttribute.DODGE) {
								return currentValue * 1.14;
							}						                
							else {
								return currentValue;
							}
						}

						@Override
						public void handleEvent(MyEvent e) {
							if (e.getType() == EventType.NEXTSHIFT) {
								getCharacter().removeCondition(this);
							}
							super.handleEvent(e);
						}
					});

				}
			}
		}
	}
	public final static class Monsterpedia extends TraitEffect { //done
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof MonsterFight) {
					character.addCondition(new BattleCondition(character) {
						@Override
						public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
							if (calculatedAttribute == CalculatedAttribute.CRITCHANCE) {
								return currentValue + 10;
							}	
							if (calculatedAttribute == CalculatedAttribute.CRITDAMAGEAMOUNT) {
								return currentValue + 25;
							}					                
							else {
								return currentValue;
							}
						}

						@Override
						public void handleEvent(MyEvent e) {
							if (e.getType() == EventType.NEXTSHIFT) {
								getCharacter().removeCondition(this);
							}
							super.handleEvent(e);
						}
					});

				}
			}
		}
	}
	public final static class NumberFortySeven extends TraitEffect { //done
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof MonsterFight) {
					character.addCondition(new BattleCondition(character) {
						@Override
						public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
							if (calculatedAttribute == CalculatedAttribute.SPEED) {
								return currentValue + 25;
							}	
							if (calculatedAttribute == CalculatedAttribute.DAMAGE) {
								return currentValue + 4;
							}
							if (calculatedAttribute == CalculatedAttribute.DODGE) {
								return currentValue + 10;
							}						                
							else {
								return currentValue;
							}
						}

						@Override
						public void handleEvent(MyEvent e) {
							if (e.getType() == EventType.NEXTSHIFT) {
								getCharacter().removeCondition(this);
							}
							super.handleEvent(e);
						}
					});

				}
			}
		}
	}
	public static class RulesOfNature extends TraitEffect{ //done
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.DAMAGE) {
				return currentValue + character.getFinalValue(BaseAttributeTypes.STRENGTH)/10;
			}
			else {
				return currentValue;
			}
		}
	}

	public final static class PrimalInstincts extends TraitEffect { // done
		
		@Override
		public void handleEvent(MyEvent e, final Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof MonsterFight) {
					character.addCondition(new BattleCondition(character) {
						@Override
						public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
							if (character.getCharacterInventory().getItem(EquipmentSlot.DRESS) == null) {
								if (calculatedAttribute == CalculatedAttribute.HIT) {
									return currentValue + 15;
								}
								if (calculatedAttribute == CalculatedAttribute.DAMAGE) {
									return currentValue + 4;
								}
								if (calculatedAttribute == CalculatedAttribute.DODGE) {
									return currentValue + 15;
								}
								if (calculatedAttribute == CalculatedAttribute.SPEED) {
									return currentValue + 15;
								} else {
									return currentValue;
								}
							} else {
								return currentValue;
							}
						}

						@Override
						public void handleEvent(MyEvent e) {
							if (e.getType() == EventType.NEXTSHIFT) {
								getCharacter().removeCondition(this);
							}
							super.handleEvent(e);
						}
					});

				}
			}
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
	public static class Hibernation extends TraitEffect{
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
	
	public static class LetsDoItLikeRabbits extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Sex) {
					activity.getAttributeModifications().add(new AttributeModification(-10f, EssentialAttributes.ENERGY, character));

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
							if (e.getType() == EventType.NEXTSHIFT) {
								getCharacter().removeCondition(this);
							}
							super.handleEvent(e);
						}
					});

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
	public static class CumCombs extends TraitEffect{
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
				return currentValue + 20;
			}
			else {
				return currentValue;
			}
		}
	}
	/**
	 * 
	 * Every 30 days the slave goes into heat.
	 * within a 3 day period the slave gets conditions with increasing bonuses.
	 * Day 1: +10% Pregnancy Chance, +50% Customer Satisfaction
	 * Day 2: +20% Pregnancy Chance, +75% Customer Satisfaction
	 * Day 3: +50% Pregnancy Chance, +150% Customer Satisfaction, +10 Customers per Shift
	 * 
	 * @author Scythless
	 *
	 */
	public static class BeastInHeat extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				
				boolean heat = false;
				if (Jasbro.getInstance().getData().getDay()%30 == 0) {
					heat = true;
				}
				
				List<Condition> list = character.getConditions();
				if (heat) {
					
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
										trait
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
										trait
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
										trait
										);
							}
						}
						
					});
				}
									
			}

		}	
	}
	public static class FluctuatingHormones extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				int random = Util.getInt(1, 10);
				switch(random) {
					case 1: 
						character.addCondition(new Buff.HornyBuff(character));
						break;
					case 2:
						character.addCondition(new Buff.Angry(character));
						break;
					case 3:
						character.addCondition(new Buff.MotivatedTwo(character));
						break;
					case 4:
						character.addCondition(new Buff.Happy(character));
						break;
					case 5:
						character.addCondition(new Buff.Hyperactive(character));
						break;
					case 6:
					case 7:
					case 8:
					case 9:
					default:
						character.addCondition(new Buff.Depressed(character));
						break;
				}
			}
		}	
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
		}
	}

	/*
	 * Slave has a ~10% Chance each day to get pregnant with an egg (if not already pregnant).
	 * This egg has a ~20% chance to birth a human and a ~80% chance to be a monster egg.
	 * Pregnancy lasts 30 days.
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

				if (notPregnant && Util.getInt(0, 100) < 10) {
					character.addCondition(new OvipositionPregnancy());

					MessageData messageData = new MessageData("oviposition.message", //TODO
							ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character),
							character.getBackground(), true);
					messageData.createMessageScreen();
				}
			}
		}
	}
	//Alchemist
	public static class GreenThumb extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Gardening) {
					Gardening gardenActivity = (Gardening) activity;   
					gardenActivity.setEfficiency(gardenActivity.getEfficiency()+5);
				}

			}
		}
	}
	public static class SelfTaught extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Sleep) {
					activity.getAttributeModifications().add(new AttributeModification(0.65f, SpecializationAttribute.ALCHEMY, character));
					activity.getAttributeModifications().add(new AttributeModification(0.65f, SpecializationAttribute.PLANTKNOWLEDGE, character));
				}
			}
		}
	}
	
	
	/**
	 * @author Scythless
	 * 
	 * Increases one stat at random every 7 days on day turnover
	 * Increase by 1
	 *
	 */
	
	public static class FastMetabolism extends TraitEffect{
		
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				int day = Jasbro.getInstance().getData().getDay();
				if (day%7 == 0) {
					int selection = Util.getInt(0, 4);
					switch(selection) {
					case 0: 
						character.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(1.0f);
						break;
					case 1:
						character.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(1.0f);
						break;
					case 2:
						character.getAttribute(BaseAttributeTypes.STRENGTH).addToValue(1.0f);
						break;
					case 3: 
						character.getAttribute(BaseAttributeTypes.STAMINA).addToValue(1.0f);
						break;
					default:
						break;
					}
				}
			}				
		}
	}
	
	/**
	 * @author Scythless
	 * 
	 * Increases pregnancy chance by a fixed 30%
	 * Also increases chance of Additional Offsprings by 50%
	 * 
	 * Also reduces Pregnancy Time by ~66% (but that's not shown here)
	 *
	 */
	public static class HeartOfTheSwarm extends TraitEffect {
		@Override
		public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
			if (calculatedAttribute == CalculatedAttribute.PREGNANCYCHANCE) {
				return currentValue + 30;
			}
			else if (calculatedAttribute == CalculatedAttribute.CHANCEADDITIONALCHILD) {
				return currentValue + 50;
			}
			else {
				return currentValue;
			}
		}
	}
	
	/**
	 * @author Scythless
	 * 
	 * Increases Obedience for every slave in the house by 0.2 at the end of every night
	 * Excludes the slave itself.
	 *
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
	
	/**
	 * @author Scythless
	 * 
	 * Increases Satisfaction during sex while pregnant by 50%
	 */
	public static class HoneyLactation extends TraitEffect {
		
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				
				for (Condition condition : character.getConditions()) {
                    if (condition instanceof PregnancyInterface) {
                    	if (activity.getType() == ActivityType.WHORE)
                    	activity.getMainCustomer().addToSatisfaction(
                    				activity.getMainCustomer().getSatisfactionAmount()/2, 
                    				trait
                    			);
                    }
				}
			}
		}
	}
	
	/*
	 * TODO: Check if it works the way it is now
	 */
	public final static class Nutbuster extends TraitEffect {
		@Override
		public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
			attacks.add(new Attack.Nutbuster(character));
		}
	}
	
	
	
	/**
	 * @author Scythless
	 *	
	 *	Intended to increase pregnancy chance for upper class customers.
	 *	Couldn't get it to work.
	 *	handleEvent doesn't access calculated Attributes and getAttributeModified can't get Access to customers
	 *	Increasing pregnancy chance for the character itself didn't yield a result, as the character is only updated every shift or day, not every customer.
	 */
	public static class BestialFeatures extends TraitEffect {
		
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (!character.getTraits().contains(Trait.ANTHRO)) {
				character.addTrait(Trait.ANTHRO);
			}
			
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					if (Util.getInt(0, 100) < 25) {
						activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/2, trait);
					}
				}
			}
		}
		
	}
	
	/**
	 * @author Scythless
	 * 
	 * +25% Satisfaction bonus for Titfuck and Foreplay while pregnant
	 * 
	 */
	public static class MotherlyWarmth extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					for (Condition con : character.getConditions()) {
						if (con instanceof PregnancyInterface) {
							if(((Whore) activity).getSexType()==Sextype.TITFUCK){
								activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/4, trait);
							}
							else if(((Whore) activity).getSexType()==Sextype.FOREPLAY){
								activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/4, trait);
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 
	 * Increases customer satisfaction for monster fight
	 * SHOULD only increase when the fight is won, but I don't know how to realise that, so it's globally at the moment.
	 * 
	 * @author Scythless
	 *
	 */
	
	public static class WhenTheHunterBecomesPrey extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity.getType() == ActivityType.MONSTERFIGHT) {
					for (Customer custom : activity.getCustomers()) {
						custom.addToSatisfaction((int) (custom.getSatisfactionAmount()*0.25),trait);
					}
				}
			}
		}
	}
	
	
}
