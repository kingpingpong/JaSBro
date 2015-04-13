package jasbro.game.events.business;

import jasbro.Util;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnData implements MyEventListener {
	private Map<CustomerType, Integer> spawnChanceModificatorMap;
	private Map<CustomerType, Integer> spawnAmountModificatorMap;	
	private Map<Gender, Integer> genderSpawnMap;
	private int bonusCustomers = 0;
	private float modCustomerAmount = 0;
	private transient List<CustomerData> chances;
	
	private Gender lastModified = Gender.MALE;
	private Gender lastModifiedPassive = Gender.FEMALE;
	
	public SpawnData() {
		spawnChanceModificatorMap = new HashMap<CustomerType, Integer>();
		spawnAmountModificatorMap = new HashMap<CustomerType, Integer>();
		genderSpawnMap = new HashMap<Gender, Integer>();
		genderSpawnMap.put(Gender.MALE, 90);
		genderSpawnMap.put(Gender.FEMALE, 10);
		genderSpawnMap.put(Gender.FUTA, 0);
	}
	
	public SpawnData(SpawnData spawnData) {
		spawnChanceModificatorMap = new HashMap<CustomerType, Integer>(spawnData.spawnChanceModificatorMap);
		spawnAmountModificatorMap = new HashMap<CustomerType, Integer>(spawnData.spawnAmountModificatorMap);
		genderSpawnMap = new HashMap<Gender, Integer>(spawnData.genderSpawnMap);
	}
	
	public void addFixedAmountCustomers(CustomerType customerType, int amount) {
		if (!spawnAmountModificatorMap.containsKey(customerType)) {
			spawnAmountModificatorMap.put(customerType, 0);
		}
		spawnAmountModificatorMap.put(customerType, spawnAmountModificatorMap.get(customerType) + amount);
	}
	
	public void addToSpawnChance(CustomerType customerType, int amount) {
		if (!spawnChanceModificatorMap.containsKey(customerType)) {
			spawnChanceModificatorMap.put(customerType, 0);
		}
		spawnChanceModificatorMap.put(customerType, spawnChanceModificatorMap.get(customerType) + amount);
	}

	public Map<CustomerType, Integer> getSpawnChanceModificatorMap() {
		return spawnChanceModificatorMap;
	}

	public void setSpawnChanceModificatorMap(Map<CustomerType, Integer> spawnChanceModificator) {
		this.spawnChanceModificatorMap = spawnChanceModificator;
	}

	public Map<CustomerType, Integer> getSpawnAmountModificatorMap() {
		return spawnAmountModificatorMap;
	}

	public void setSpawnAmountModificatorMap(Map<CustomerType, Integer> spawnAmountModificator) {
		this.spawnAmountModificatorMap = spawnAmountModificator;
	}
	
	public int getPercentFemale() {
		return genderSpawnMap.get(Gender.FEMALE);
	}

	public int getPercentFuta() {
		return genderSpawnMap.get(Gender.FUTA);
	}

	public int getPercentMale() {
		return genderSpawnMap.get(Gender.MALE);
	}

	public int getChance(Gender gender) {
		if (!genderSpawnMap.containsKey(gender)) {
			genderSpawnMap.put(gender, 0);			
		}
		return genderSpawnMap.get(gender);
	}
	
	public void increaseChance(Gender gender) {
		if (modify(gender, true)) {
			if (lastModified != gender) {
				Gender genderTmp = Gender.getRemaining(gender, lastModified);
				if (modify(genderTmp, false)) {
					lastModifiedPassive = genderTmp;
				}
				else {
					modify(lastModified, false);
					lastModifiedPassive = lastModified;
				}
			}
			else {
				Gender genderTmp = Gender.getRemaining(gender, lastModifiedPassive);
				if (modify(genderTmp, false)) {
					lastModifiedPassive = genderTmp;
				}
				else {
					modify(lastModifiedPassive, false);
				}
			}			
			lastModified = gender;
		}
	}
	
	public void decreaseChance(Gender gender) {
		if (modify(gender, false)) {
			if (lastModified != gender) {
				Gender genderTmp = Gender.getRemaining(gender, lastModified);
				if (modify(genderTmp, true)) {
					lastModifiedPassive = genderTmp;
				}
				else {
					modify(lastModified, true);
					lastModifiedPassive = lastModified;
				}
			}
			else {
				Gender genderTmp = Gender.getRemaining(gender, lastModifiedPassive);
				if (modify(genderTmp, true)) {
					lastModifiedPassive = genderTmp;
				}
				else {
					modify(lastModifiedPassive, true);
				}
			}			
			lastModified = gender;
		}
	}
	
	public boolean modify(Gender gender, boolean positive) {
		int value = genderSpawnMap.get(gender);
		if (positive) {
			if (value >= 100) {
				return false;
			}
			else {
				genderSpawnMap.put(gender, value + 10);
				return true;
			}
		}
		else {
			if (value <= 0) {
				return false;
			}
			else {
				genderSpawnMap.put(gender, value - 10);
				return true;
			}
		}
	}
	
	public Gender generateGender() {
		int chance = Util.getInt(0, 100);
		int sumChance = 0;
		
		Gender genders[] = Gender.values();
		for (int i = 0; i < genders.length - 1; i++) {
			if (chance < getChance(genders[i]) + sumChance) {
				return genders[i];
			}
			else {
				sumChance += getChance(genders[i]);
			}
		}
		return genders[genders.length-1];
	}

	public List<Customer> spawn(int baseAmountCustomers, Fame fame) {
		BusinessCalculations calc = new BusinessCalculations();
		List<Customer> customers = new ArrayList<Customer>();
		chances = calc.getBaseChances(fame);
		int finalAmountCustomers = (int) (baseAmountCustomers * (1 + modCustomerAmount) + bonusCustomers);
		
		//Add bonus chances
		for (CustomerData chance : chances) {
			if (spawnChanceModificatorMap.containsKey(chance.getCustomerType())) {
				chance.setValue(chance.getValue() + spawnChanceModificatorMap.get(chance.getCustomerType()));
			}
		}
		
		//generate customers
		for (int i = 0; i < finalAmountCustomers; i++) {
			customers.add(spawnCustomer(0));
		}
		
		//Add bonus customers
		for (CustomerType customerType : CustomerType.values()) {
			if (spawnAmountModificatorMap.containsKey(customerType)) {
				for (int i = 0; i < spawnAmountModificatorMap.get(customerType); i++) {
					customers.add(createCustomer(customerType));					
				}
			}
		}
		
		Collections.shuffle(customers);
		return customers;
	}
	
	private Customer spawnCustomer(int modifier) {
		for (CustomerData customerData : chances) {
			if (Util.getInt(0, 100) - modifier < customerData.getValue()) {
				return createCustomer(customerData.getCustomerType());
			}
		}
		return createCustomer(CustomerType.BUM);
	}

	public Customer createCustomer(CustomerType customerType) {
		Customer customer = CustomerType.generateCustomer(customerType);
		if (customer instanceof CustomerGroup) {
			initGroup((CustomerGroup) customer);
		}
		
		customer.setGender(generateGender());		
		customer.setPreferredSextype(Sextype.getPreferredSextype(customer));
		customer.setPreferedSecondaryActivity1(ActivityType.getRandomSecondaryActivity(customer));
		customer.setPreferedSecondaryActivity2(ActivityType.getRandomSecondaryActivity(customer));
		return customer;
	}
	
	private void initGroup(CustomerGroup customerGroup) {
		int amount;
		int chance = Util.getInt(0, 100);
		if (chance < 40) {
			amount = 2;
		}
		else if (chance < 60) {
			amount = 3;
		}
		else if (chance < 70) {
			amount = 4;
		}
		else if (chance < 80) {
			amount = 5;
		}
		else if (chance < 85) {
			amount = 6;
		}
		else if (chance < 90) {
			amount = 7;
		}
		else if (chance < 95) {
			amount = 8;
		}
 		else if (chance < 98) {
			amount = 9;
		}
 		else {
 			amount = 10;
 		}
		amount++;

		
		Customer subCustomer;
		do {
			subCustomer = spawnCustomer(-10);
		}
		while (subCustomer instanceof CustomerGroup);
		customerGroup.getCustomers().add(subCustomer);
		
		for (int i = 0; i < amount - 1; i++) {
			Customer customer = createCustomer(subCustomer.getType());
			customer.setGender(subCustomer.getGender());
			customerGroup.getCustomers().add(customer);
		}
	}

	public void reset() {
		spawnChanceModificatorMap.clear();
		spawnAmountModificatorMap.clear();	
		bonusCustomers = 0;
		modCustomerAmount = 0;
	}
	
	@Override
	public void handleEvent(MyEvent e) {
		if (e.getType() == EventType.NEXTSHIFT) {
			reset();
		}
	}

	public void addBonusCustomers(int amount) {
		bonusCustomers += amount;
	}


	public static class CustomerData {
		private CustomerType customerType;
		private Integer value;
					
		public CustomerData() {
			super();
		}

		public CustomerData(CustomerType customerType, Integer value) {
			super();
			this.customerType = customerType;
			this.value = value;
		}



		public CustomerType getCustomerType() {
			return customerType;
		}

		public void setCustomerType(CustomerType customerType) {
			this.customerType = customerType;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}	
	}


	public Map<Gender, Integer> getGenderSpawnMap() {
		return genderSpawnMap;
	}
	
	public void addToModCustomerAmount(float modAmount) {
	    modCustomerAmount += modAmount;
	}
}
