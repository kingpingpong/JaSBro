package jasbro.game.character.traits.perktrees;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.business.Attend;
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.game.world.Time;
import jasbro.texts.TextUtil;

public class BartenderPerks {
	
	public static class CatMaid extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend || activity instanceof Attend) {
					int amountBum=0;
					int payBum=0;
					for (Customer customer : activity.getCustomers()) {
						if(customer.getType()==CustomerType.BUM && Util.getInt(0, 100)<80 && customer.getMoney()!=0){
							amountBum++;
							payBum+=customer.getMoney();
							customer.payFixed(customer.getMoney());
							customer.setStatus(CustomerStatus.VERYDRUNK);
						}
					}
					activity.setIncome(payBum);
					Object arguments[]={amountBum, payBum};
					if(amountBum>0)
						activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("barevent.milkbums",character,arguments));
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
	//Increase max customers for bartending based on Int
	public static class Multitasking extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYCREATED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend) {
					Bartend bartend = (Bartend)activity;
					bartend.setBonus(bartend.getBonus()+character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)/4);
				}
			}
		}
	} 
	
	public static class NightShift extends TraitEffect{
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITYCREATED) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Bartend && Jasbro.getInstance().getData().getTime()==Time.NIGHT) {
					Bartend bartend = (Bartend)activity;
					bartend.setBonus(bartend.getBonus()+15);
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
					activity.getAttributeModifications().add(new AttributeModification(0.1f,EssentialAttributes.MOTIVATION, character));
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
					activity.getAttributeModifications().add(new AttributeModification(0.05f,EssentialAttributes.MOTIVATION, character));
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
}
