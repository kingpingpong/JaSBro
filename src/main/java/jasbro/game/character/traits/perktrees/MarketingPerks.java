package jasbro.game.character.traits.perktrees;

import java.util.ArrayList;
import java.util.List;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Advertise;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.events.CustomersArriveEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.events.business.SpawnData.CustomerData;
import jasbro.game.housing.House;

public class MarketingPerks {
	
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
							activity.getAttributeModifications().add(new AttributeModification(0.1f,EssentialAttributes.MOTIVATION, character));
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
}
