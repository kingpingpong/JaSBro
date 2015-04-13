package jasbro.game.events.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.business.SpawnData.CustomerData;
import jasbro.game.housing.House;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BusinessCalculations {
    
    public int calculateCustomerAmount(List<Charakter> relevantCharacters, Fame fame) {
    	int amountCharacters = relevantCharacters.size();
        int step = (int) Math.sqrt(amountCharacters);
        if (step == 0) {
            step = 1;
        }
    	
        int baseAmount = 3;
        
        long fameValue = fame.getFame();
        int fameModifier = 0;
        float famePerCustomer = 2;
        for (int i = 0; fameValue > 0; i++) {
        	fameModifier++;
        	fameValue -= famePerCustomer;
        	
        	if (i != 0) {
        		famePerCustomer = famePerCustomer * 1.75f;
        	}
        }
        
        int finalAmount = baseAmount + fameModifier;
        int rndFactor = Util.getInt(-1 - finalAmount / 10, 2 + finalAmount / 10);
        finalAmount = finalAmount + rndFactor;
        
        return finalAmount;
    }
    
    public Fame calculateFame(House house, List<Charakter> relevantCharacters) {
    	int fameAmount = 0;
    	for (Charakter character : relevantCharacters) {
    		fameAmount += character.getFame().getFame();
        }
        
    	fameAmount += house.getFame().getFame() + fameAmount;
        
        Fame fame = new Fame();
        fame.modifyFame(fameAmount);
        return fame;
    }
    
    public List<CustomerData> getBaseChances(Fame fame) {
        List<CustomerData> customerDataList = new ArrayList<CustomerData>();
        customerDataList.add(new CustomerData(CustomerType.CELEBRITY, 0 + getModifier(fame.getFame(), 2, 50000)));
        customerDataList.add(new CustomerData(CustomerType.LORD, 0 + getModifier(fame.getFame(), 10, 5000)));
        customerDataList.add(new CustomerData(CustomerType.MINORNOBLE, 0 + getModifier(fame.getFame(), 15, 2000)));
        customerDataList.add(new CustomerData(CustomerType.BUSINESSMAN, 1 + getModifier(fame.getFame(), 25, 800)));
        customerDataList.add(new CustomerData(CustomerType.GROUP, 5 + getModifier(fame.getFame(), 15, 1000)));
        customerDataList.add(new CustomerData(CustomerType.MERCHANT, 2 + getModifier(fame.getFame(), 40, 400)));
        customerDataList.add(new CustomerData(CustomerType.SOLDIER, 5 + getModifier(fame.getFame(), 50, 200)));
        customerDataList.add(new CustomerData(CustomerType.PEASANT, 15 + getModifier(fame.getFame(), 90, 50)));
        customerDataList.add(new CustomerData(CustomerType.BUM, 100));        
        return customerDataList;
    }
    
    public int getModifier(long fame, int max, int fameToIncrease) {
    	long modifier = fame / fameToIncrease;
    	if (modifier > max) {
    		modifier = max;
    		fame = fame - max * fameToIncrease;
    		fameToIncrease = (int) (fameToIncrease * 5);
    		while (fame >= fameToIncrease) {
    			modifier++;
    			fameToIncrease = (int) (fameToIncrease * Math.sqrt(fameToIncrease));
    			fame -= fameToIncrease;
    		}
    	}
    	return (int)modifier;
    }
    
    public void randomizeCharacters(List<Charakter> characters) {
        Collections.shuffle(characters);
    }
    
    public void assignCustomers(List<BusinessMainActivity> mainActivities, List<Customer> customers, List<Customer> remainingCustomers) {
    	int bestValue;
    	do {
	    	BusinessMainActivity activity = null;
	    	Customer customer = null;
    		bestValue = 0;
	    	for (int i = 0; i < mainActivities.size(); i++) {
		    	for (int j = 0; j < customers.size(); j++) {
		    		BusinessMainActivity curActivity = mainActivities.get(i);
			    	Customer curCustomer = customers.get(j);
			    	if (curCustomer.getMoney() <= 0) {
			    		j--;
			    		customers.remove(curCustomer);
			    		continue;
			    	}
			    	
			    	int curValue = curActivity.rateCustomer(curCustomer);
			    	//assign modifications by traits
			    	if(activity!=null){
			    	for (Charakter character : activity.getCharacters()) {
			    	    for (Trait trait : character.getTraits()) {
			    	        curValue = trait.modifyCustomerRating(curValue, curCustomer, activity);
			    	    }
			    	}
			    	}
			    	if (curValue > bestValue) {
			    		bestValue = curValue;
			    		activity = curActivity;
			    		customer = curCustomer;
			    	}
		    	}
	    	}
	    	if (bestValue > 0 && activity != null && customer != null) {
	    		activity.addMainCustomer(customer); //TODO maybe check if activity is full in the future?
	    		customers.remove(customer);
	    		mainActivities.remove(activity);
	    		if (remainingCustomers != null) {
	    			remainingCustomers.remove(customer);
	    		}
	    	}
    	} while (customers.size() > 0 && mainActivities.size() > 0 && bestValue != 0);
    }

	public void assignCustomersToSecondaryActivities(List<BusinessSecondaryActivity> secondaryActivities, List<Customer> initialCustomers) {
		//Init customers for secondary activities (separate groups)
		List<Customer> customers = new ArrayList<Customer>();
		for (Customer customer : initialCustomers) {
			if (customer instanceof CustomerGroup) {
				CustomerGroup customerGroup = (CustomerGroup) customer;
				customers.addAll(customerGroup.getCustomers());
			}
			else {
				customers.add(customer);
			}
		}
		
		//Sort secondary activities by appeal
		Collections.sort(secondaryActivities, Collections.reverseOrder(new Comparator<BusinessSecondaryActivity>() {
			@Override
			public int compare(BusinessSecondaryActivity o1, BusinessSecondaryActivity o2) {
				return ((Integer)o1.getAppeal()).compareTo(o2.getAppeal());
			}
		}));
		
		//Sort customers by money
		Collections.sort(customers, Collections.reverseOrder(new Comparator<Customer>() {
			@Override
			public int compare(Customer o1, Customer o2) {
				return ((Integer)o1.getMoney()).compareTo(o2.getMoney());
			}
		}));	
		
		List<Class<? extends BusinessSecondaryActivity>> unwantedActivities = new ArrayList<Class<? extends BusinessSecondaryActivity>>();
		for (Customer customer : customers) {
			if (customer.getMaxSecondaryActivities() == 0) {
				continue;
			}
			int amountActivities = 0;
			unwantedActivities.clear();
			
			for (BusinessSecondaryActivity curActivity : secondaryActivities) {
				if (!unwantedActivities.contains(curActivity.getClass())) { //Skip activities the customers does not want to attend
					int baseChance=10;
					if(customer.getPreferedSecondaryActivity1().getActivity() == curActivity
							|| customer.getPreferedSecondaryActivity2().getActivity() == curActivity){
						baseChance+=30;
					}
					if (Util.getInt(0, 100) > baseChance) { // 66% chance that customer wants to participate in an activity type
						if (curActivity.getCustomers().size() < curActivity.getMaxAttendees()) {
							amountActivities++;
							curActivity.addAttendingCustomer(customer);
							if(customer.getPreferedSecondaryActivity1().getActivity() == curActivity
									|| customer.getPreferedSecondaryActivity2().getActivity() == curActivity){
								customer.addToSatisfaction(25, this);
							}
							else{
								customer.addToSatisfaction(-25, this);
							}
							unwantedActivities.add(curActivity.getClass());
							if (amountActivities >= customer.getMaxSecondaryActivities()) {
								break;
							}
						}
					}
					else {
						unwantedActivities.add(curActivity.getClass());
					}
				}
			}
		}
	}
    
}
