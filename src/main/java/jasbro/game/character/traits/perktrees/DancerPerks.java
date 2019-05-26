package jasbro.game.character.traits.perktrees;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Sunbathe;
import jasbro.game.character.activities.sub.business.Attend;
import jasbro.game.character.activities.sub.business.Strip;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.SunEffect;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.CustomersArriveEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public class DancerPerks {

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
							customer.addToSatisfaction(character.getFinalValue(SpecializationAttribute.STRIP)/10, activity);
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
								int tips = (int)(richest.getMoney() / (200.0 / character.getFinalValue(SpecializationAttribute.STRIP)) + Util.getInt(10, 20));
								activity.modifyIncome(tips);
								Object arguments[] = {richest.getName(), tips};
								richest.addToSatisfaction(character.getFinalValue(SpecializationAttribute.STRIP)/5, activity);
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
							customer.addToSatisfaction(activity.getCustomers().size()/3, activity);
							activity.getAttributeModifications().add(new AttributeModification(0.005f,EssentialAttributes.MOTIVATION, character));
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
		public static class NiceHips extends TraitEffect {

			@Override
			public float getAttributeModifier(Attribute attribute) {
				if (attribute.getAttributeType() == BaseAttributeTypes.CHARISMA) {
					int sexBonus=0;
					sexBonus+=attribute.getCharacter().getFinalValue(Sextype.VAGINAL);
					sexBonus+=attribute.getCharacter().getFinalValue(Sextype.ANAL);
					sexBonus/=12;
					return sexBonus;
				}
				return 0;
			}
		}
		public static class Trendy extends TraitEffect {

			private static final String[] KEYS = { "STRIP.bunny", "STRIP.cowgirl", "STRIP.bellydancer", "STRIP.schoolgirl",
				"STRIP.microbikini", "STRIP.maid", "STRIP.chainmailbikini", "STRIP.catsuit" };
			private static final int[] REQUIRED_SKILL = { 40, 25, 50, 50, 40, 40, 40, 40 };
			private static final int[] SKILL_SCALE = { 5, 5, 5, 5, 5, 5, 5, 5 };
			private static final int[] RAND_MAX = { 5, 5, 5, 5, 5, 5, 5, 5 };
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
							customer.addToSatisfaction(1 + character.getFinalValue(SpecializationAttribute.STRIP) / 10, activity);	
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
								customer.addToSatisfaction(1 + character.getFinalValue(SpecializationAttribute.STRIP) / 8, activity);
							}
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
							activity.getAttributeModifications().add(new AttributeModification(0.5f,EssentialAttributes.MOTIVATION, character));
							int rand=Util.getInt(1, 4);
							if(rand==1) activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STRIP.horny.one",character));
							else if(rand==2) activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STRIP.horny.two",character));
							else activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("STRIP.horny.three",character));           		
							character.addCondition(new Buff.HornyBuff(character));
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
}
