package jasbro.game.character.traits.perktrees;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.RunningActivity.ModificationData;
import jasbro.game.character.activities.RunningActivity.TargetType;
import jasbro.game.character.activities.sub.BodyWrap;
import jasbro.game.character.activities.sub.Orgy;
import jasbro.game.character.activities.sub.Pamper;
import jasbro.game.character.activities.sub.Swim;
import jasbro.game.character.activities.sub.business.Fight;
import jasbro.game.character.activities.sub.business.Massage;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.SunEffect;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.Item;
import jasbro.game.world.customContent.RapeEvent;
import jasbro.texts.TextUtil;

public class NursePerks {


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

	public static class Brewer extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				int chance =character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE);
				Item item = Jasbro.getInstance().getItems().get("XX_JSbro_Potion Energy 1");
				chance +=character.getFinalValue(SpecializationAttribute.MAGIC);
				chance +=character.getFinalValue(BaseAttributeTypes.INTELLIGENCE);
				chance/=6;
				if(Util.getInt(0, 90)<chance){
					chance=Util.getInt(0, 100);
					if(chance<10 && character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)>50)
						item = Jasbro.getInstance().getItems().get("XX_JSbro_Potion Energy 3");
					else if(chance<20 && character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)>50)
						item = Jasbro.getInstance().getItems().get("XX_JSbro_Potion Health 3");
					else if(chance<35 && character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)>25)
						item = Jasbro.getInstance().getItems().get("XX_JSbro_Potion Health 2");
					else if(chance<50 && character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)>25)
						item = Jasbro.getInstance().getItems().get("XX_JSbro_Potion Energy 2");
					else if(chance<70)
						item = Jasbro.getInstance().getItems().get("XX_JSbro_Potion Health 1");
					else
						item = Jasbro.getInstance().getItems().get("XX_JSbro_Potion Energy 1");

					Jasbro.getInstance().getData().getInventory().addItems(item, 1);
				}
			}
		}
	}

	public static class LoveAndCare extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				PlannedActivity activity = character.getActivity();

				if(activity.getSource() instanceof Room)
				{
					Room room=(Room) activity.getSource();
					House house =room.getHouse();
					for(Room room2 : house.getRooms()){
						for(Charakter chara : room2.getCurrentUsage().getCharacters()){
							if(!chara.equals(character)){
								
								for (Condition condition: chara.getConditions()) {
									if (condition instanceof SunEffect && ((SunEffect)condition).isSunburn() && Util.getInt(0, 100)<50) {
										chara.removeCondition(condition);
									}
									if (condition instanceof Buff.RoughenedUp && Util.getInt(0, 100)<50) {
										chara.removeCondition(condition);
									}
									if (condition instanceof Buff.Exhausted && Util.getInt(0, 100)<50) {
										chara.removeCondition(condition);
									}
								}
							}
						}
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

	public static class BlessedAura extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				PlannedActivity activity = character.getActivity();

				if(activity.getSource() instanceof Room && Util.getInt(0, 100)<15)
				{
					Room room=(Room) activity.getSource();
					House house =room.getHouse();
					for(Room room2 : house.getRooms()){
						for(Charakter chara : room2.getCurrentUsage().getCharacters()){
							if(!chara.equals(character)){
								
								chara.getAttribute(EssentialAttributes.MOTIVATION).addToValue(10);
							}
						}
					}
				}
			}
		}
	}
	

	public static class SeXpert extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.NEXTDAY) {
				if(Util.getInt(0, 100)<15){
					int s=Util.getInt(0, 6);
					if(s==0)
						character.getAttribute(Sextype.ANAL).addToValue(1.0f, true);
					if(s==1)
						character.getAttribute(Sextype.VAGINAL).addToValue(1.0f, true);
					if(s==2)
						character.getAttribute(Sextype.ORAL).addToValue(1.0f, true);
					if(s==3)
						character.getAttribute(Sextype.FOREPLAY).addToValue(1.0f, true);
					if(s==4)
						character.getAttribute(Sextype.TITFUCK).addToValue(1.0f, true);
					if(s==5)
						character.getAttribute(Sextype.GROUP).addToValue(1.0f, true);
					
				}
			}
		}
	}	
}
