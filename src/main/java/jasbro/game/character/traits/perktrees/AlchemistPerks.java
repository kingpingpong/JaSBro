package jasbro.game.character.traits.perktrees;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Clean;
import jasbro.game.character.activities.sub.Gardening;
import jasbro.game.character.activities.sub.Sleep;
import jasbro.game.character.activities.sub.business.Attend;
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.texts.TextUtil;

public class AlchemistPerks {

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
					activity.getAttributeModifications().add(new AttributeModification(0.65f, SpecializationAttribute.MAGIC, character));
					activity.getAttributeModifications().add(new AttributeModification(0.65f, SpecializationAttribute.PLANTKNOWLEDGE, character));
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
}
