package jasbro.game.character.traits.perktrees;

import java.util.ArrayList;
import java.util.List;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Sleep;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.CustomersArriveEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.texts.TextUtil;

public class WhorePerks {

	//Max customers may randomly increase
	public static class KeepEmComing extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Long servedToday=character.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString());
					if(servedToday>12){
						Whore whoreActivity = (Whore) activity;
						whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier()-0.3f);
						whoreActivity.setExecutionModifier(whoreActivity.getExecutionModifier()-0.3f);
					}
				}
			}
		}
	}
	//Max customers halved, satisfaction increased
	public static class OurTime extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					activity.getMainCustomers().get(0).addToSatisfaction(15, trait);
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
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					((Whore) activity).setExecutionTime(100000);
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
						activity.getMainCustomers().get(0).addToSatisfaction(activity.getMainCustomers().get(0).getSatisfactionAmount()/4, trait);
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
						activity.getMainCustomers().get(0).addToSatisfaction(10, trait);
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
					Whore whoreActivity = (Whore) activity;
					if(whoreActivity.getSexType()==Sextype.ANAL || whoreActivity.getSexType()==Sextype.VAGINAL ||whoreActivity.getSexType()==Sextype.FOREPLAY || whoreActivity.getSexType()==Sextype.GROUP){
						House house = activity.getHouse();

						List<Charakter> li  = new ArrayList<Charakter>();
						List<Charakter> li2  = new ArrayList<Charakter>();
						if (house != null) {
							for(Room room : house.getRooms()){

								for(Charakter cha : room.getCurrentUsage().getCharacters()){
									if(cha.getName()!=character.getName() && Util.getInt(0, 100)<5 && activity.getCharacter().getFinalValue(SpecializationAttribute.SEDUCTION)>10){
										cha.addCondition(new Buff.HornyBuff(cha));
										li.add(cha);
										if(Util.getInt(0, 100)<cha.getFinalValue(SpecializationAttribute.SEDUCTION)){
											li2.add(cha);
											
											activity.getAttributeModifications().add(new AttributeModification(0.4f, SpecializationAttribute.SEDUCTION, cha));
										}
									}
								}
							}

							if(li.size()!=0)
								activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("sexevent.loud", li)+" "+TextUtil.t("sexevent.loudtwo", character));
							if(li2.size()!=0)
							activity.getMessages().get(0).addToMessage(" "+TextUtil.t("sexevent.loud.pickup", li2));
						}
						else if(Util.getInt(0, 100)<10){
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("sexevent.loudstreet", character));
							character.getFame().modifyFame(character.getFinalValue(SpecializationAttribute.SEDUCTION));
						}
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
							float change = Math.abs(modification)*0.2f;
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


					if(servedToday>5){
						Whore whoreActivity = (Whore) activity;
						whoreActivity.setCooldownModifier(whoreActivity.getCooldownModifier()-0.2f);
						if (Util.getInt(0, 100)<servedToday*3){
							if (Util.getInt(0, 100)<50){
								activity.getMainCustomers().get(0).addToSatisfaction(-10, trait);				
								activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("SITBACK.unhappy",character));}
							else
							{
								activity.getMainCustomers().get(0).addToSatisfaction(5, trait);				
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
							activity.getMainCustomers().get(0).addToSatisfaction(1+character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)/2, trait);
							if(activity.getMainCustomers().get(0).getStatus()==CustomerStatus.HAPPY){
								activity.getAttributeModifications().add(new AttributeModification(0.4f,EssentialAttributes.MOTIVATION, character));
								activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("CHATTY.happycustomer",character));
							}
							if(activity.getMainCustomers().get(0).getStatus()==CustomerStatus.SAD){
								activity.getMainCustomers().get(0).setStatus(CustomerStatus.HAPPY);
								activity.getMainCustomers().get(0).addToSatisfaction(10+character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)/4, trait);
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
					Whore whoreActivity = (Whore) activity;
					whoreActivity.setCooldownTime(0);
					whoreActivity.setExecutionModifier(whoreActivity.getExecutionModifier()-0.7f);
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

}
