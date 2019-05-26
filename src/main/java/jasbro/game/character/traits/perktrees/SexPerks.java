package jasbro.game.character.traits.perktrees;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerStatus;
import jasbro.texts.TextUtil;

public class SexPerks {

	//The following increases satisfaction for a certain act.
	public static class CozyCunt extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Whore whoreActivity=(Whore) activity;
					if(whoreActivity.getSexType()==Sextype.VAGINAL || (whoreActivity.getSexType()==Sextype.GROUP)){		
						int size=0;

						whoreActivity.getMainCustomers().get(0).addToSatisfaction(5, trait);	
						int amountOrgasms=Util.getInt(1, 3);
						if(whoreActivity.getSexType()==Sextype.GROUP){
							CustomerGroup group=(CustomerGroup)whoreActivity.getMainCustomer();
							size=group.getCustomers().size();
							amountOrgasms*=Util.getInt(1, size);

						}
						int chance=3;
						chance*=(100+whoreActivity.getExecutionTime())/100;
						amountOrgasms+=whoreActivity.getExecutionTime()/10;
						if(Util.getInt(0, 10)>chance && amountOrgasms>0){
							Object arguments[]={amountOrgasms};
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("sexevent.vaginal",character, arguments));
							activity.getAttributeModifications().add(new AttributeModification(0.05f*amountOrgasms,EssentialAttributes.MOTIVATION, character));
						}
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
					if(((Whore) activity).getSexType()==Sextype.ANAL || ((Whore) activity).getSexType()==Sextype.GROUP){
						activity.getMainCustomers().get(0).addToSatisfaction(5, trait);

						if((activity.getMainCustomers().get(0).getStatus()==CustomerStatus.HORNYSTATUS ||
								activity.getMainCustomers().get(0).getStatus()==CustomerStatus.STRONGSTATUS ||
								activity.getMainCustomers().get(0).getStatus()==CustomerStatus.VERYHORNY ||
								activity.getMainCustomers().get(0).getStatus()==CustomerStatus.LIVELY) && Util.getInt(0, 5)==1){


							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("sexevent.anal",character));
							activity.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
						}
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
					if(((Whore) activity).getSexType()==Sextype.ORAL || ((Whore) activity).getSexType()==Sextype.GROUP){
						if(Util.getInt(0, 5)==1){
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("sexevent.oral",character));
							activity.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
						}
						activity.getMainCustomers().get(0).addToSatisfaction(5, trait);
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
					if(((Whore) activity).getSexType()==Sextype.FOREPLAY || ((Whore) activity).getSexType()==Sextype.GROUP){
						activity.getMainCustomers().get(0).addToSatisfaction(5, trait);
						if(Util.getInt(0, 5)==1){
							activity.getMessages().get(0).addToMessage("\n"+TextUtil.t("sexevent.foreplay",character));
							activity.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
						}
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
						activity.getMainCustomers().get(0).addToSatisfaction(5, trait);
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
					((Whore) activity).setExecutionModifier(((Whore) activity).getExecutionModifier()-0.2f);
				}
			}
		}
	}
	//The following go change the customer rating based on their prefered sex type
	//Customers with higher ratings are served first.
	public static class WetForYou extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Whore whoreActivity = (Whore) activity;
					if(whoreActivity.getSexType()==Sextype.VAGINAL){
						((Whore) activity).setExecutionModifier(((Whore) activity).getExecutionModifier()-0.2f);
						activity.getAttributeModifications().add(new AttributeModification(0.4f, EssentialAttributes.MOTIVATION, character));
					}
				}
			}
		}
	}
	public static class BackdoorOpen extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Whore whoreActivity = (Whore) activity;
					if(whoreActivity.getSexType()==Sextype.ANAL){
						((Whore) activity).setExecutionModifier(((Whore) activity).getExecutionModifier()-0.2f);
						activity.getAttributeModifications().add(new AttributeModification(0.4f, EssentialAttributes.MOTIVATION, character));
					}
				}
			}
		}
	}
	public static class Thirsty extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Whore whoreActivity = (Whore) activity;
					if(whoreActivity.getSexType()==Sextype.ORAL){
						((Whore) activity).setExecutionModifier(((Whore) activity).getExecutionModifier()-0.2f);
						activity.getAttributeModifications().add(new AttributeModification(0.4f, EssentialAttributes.MOTIVATION, character));
					}
				}
			}
		}
	}
	public static class FeelMeUp extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Whore whoreActivity = (Whore) activity;
					if(whoreActivity.getSexType()==Sextype.FOREPLAY){
						((Whore) activity).setExecutionModifier(((Whore) activity).getExecutionModifier()-0.2f);
						activity.getAttributeModifications().add(new AttributeModification(0.4f, EssentialAttributes.MOTIVATION, character));
					}
				}
			}
		}
	}
	public static class ComeToMommy extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore) {
					Whore whoreActivity = (Whore) activity;
					if(whoreActivity.getSexType()==Sextype.TITFUCK){
						((Whore) activity).setExecutionModifier(((Whore) activity).getExecutionModifier()-0.2f);
						activity.getAttributeModifications().add(new AttributeModification(0.4f, EssentialAttributes.MOTIVATION, character));
					}
				}
			}
		}
	}
	//Adds satisfaction for all sex acts
	public static class BedGoddess extends TraitEffect {
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

	//May trigger some flavor text, which greatly boosts satisfaction.
	public static class Steamy extends TraitEffect {
		@Override
		public void handleEvent(MyEvent e, Charakter character, Trait trait) {
			if (e.getType() == EventType.ACTIVITY) {
				RunningActivity activity = (RunningActivity) e.getSource();
				if (activity instanceof Whore && Util.getInt(0, 100)<6) {
					Whore whoreActivity = (Whore) activity;
					whoreActivity.getMainCustomers().get(0).addToSatisfaction(15, trait);
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
}
