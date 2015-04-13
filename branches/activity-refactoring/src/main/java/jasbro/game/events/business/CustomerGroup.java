package jasbro.game.events.business;

import jasbro.Util;
import jasbro.Util.GenderAmounts;
import jasbro.game.character.Gender;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Inventory.ItemData;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class CustomerGroup extends Customer {
	private Logger log = Logger.getLogger(CustomerGroup.class);
	private List<Customer> customers = new ArrayList<Customer>();
	
	@Override
	public String getName() {
	    Object attributesTmp[] = {};
		Object attributes[] = {customers.size(), TextUtil.t("groupgender", customers.get(0), attributesTmp), customers.get(0).getName()};
    	String text;
    	String key;
    	if (getGender() == Gender.MALE) {
    		key = "nocheck.groupname."+customers.get(0).getType().toString()+".male";
    	}
    	else {
    		key = "nocheck.groupname."+customers.get(0).getType().toString()+".female";
    	}
		text = TextUtil.t(key, attributes);
    	if (! (key).equals(text)) {
    		return text;
    	}
    	else {
    		return TextUtil.t("groupname", attributes);
    	}
	}
	
	@Override
	public CustomerType getType() {
		return CustomerType.GROUP;
	}
	
	public Gender getGender() {
		GenderAmounts genderAmounts = Util.getGenderAmounts(new ArrayList<Person>(customers));
		if (genderAmounts.getGenderAmount(Gender.MALE) == 0 && genderAmounts.getGenderAmount(Gender.FUTA) == 0) {
			return Gender.FEMALE;
		}
		else if (genderAmounts.getGenderAmount(Gender.FEMALE) == 0 && genderAmounts.getGenderAmount(Gender.FUTA) == 0) {
			return Gender.MALE;
		}
		if (genderAmounts.getGenderAmount(Gender.MALE) == 0 && genderAmounts.getGenderAmount(Gender.FEMALE) == 0) {
			return Gender.FUTA;
		}
		else {
			log.error("No valid group gender found.");
			return Gender.MALE;
		}
	}

	public List<Customer> getCustomers() {
		return customers;
	}
	
	@Override
	public int getInitialSatisfaction() {
		int sum = 0;
		for (Customer customer : customers) {
			sum += customer.getInitialSatisfaction();
		}
		return sum;
	}

	@Override
	public int getMoney() {
		int sum = 0;
		for (Customer customer : customers) {
			sum += customer.getMoney();
		}
		return sum;
	}

	@Override
	public int getSatisfactionAmount() {
		int sum = 0;
		for (Customer customer : customers) {
			sum += customer.getSatisfactionAmount();
		}
		return sum / customers.size();
	}

	@Override
	public int getMaxSecondaryActivities() {
		return 0;
	}

	@Override
	public float getImportance() {
		float sum = 0;
		for (Customer customer : customers) {
			sum += customer.getImportance();
		}
		return sum / customers.size();
	}
	

	@Override
	public void addToSatisfaction(int mod, Object satisfactionModifier) {
		for (Customer customer : customers) {
			customer.addToSatisfaction(mod, satisfactionModifier);
		}
	}

	@Override
	public int payFixed(int amount) {
		int sum = 0;
		for (Customer customer : customers) {
			sum += customer.payFixed(amount / customers.size());
		}
		return sum;
	}

	@Override
	public int pay(int amount, float modifier) {
		int sum = 0;
		for (Customer customer : customers) {
			sum += customer.pay(amount / customers.size(), modifier);
		}
		return sum;
	}

	@Override
	public int pay(float modifier) {
		int sum = 0;
		for (Customer customer : customers) {
			sum += customer.pay(modifier / customers.size());
		}
		return sum;
	}

	@Override
	public List<SatisfactionModifier> getSatisfactionModifiers() {
		List<SatisfactionModifier> satisfactionModifiers = new ArrayList<SatisfactionModifier>();
		for (Customer customer : customers) {
			satisfactionModifiers.addAll(customer.getSatisfactionModifiers());
		}
		return satisfactionModifiers;
	}

	@Override
	public void changePayModifier(float modifier) {
		for (Customer customer : customers) {
			customer.changePayModifier(modifier);
		}
	}

	@Override
	public int getHitpoints() {
		int sum = 0;
		for (Customer customer : customers) {
			sum += customer.getHitpoints();
		}
		return sum;
	}

	@Override
	public int getMaxHitpoints() {
		int sum = 0;
		for (Customer customer : customers) {
			sum += customer.getMaxHitpoints();
		}
		return sum;
	}

	@Override
	public float modifyHitpoints(float modifier) {
		for (Customer customer : customers) {
			if (customer.getHitpoints() > 0 && customer.getMaxHitpoints() > customer.getHitpoints()) {
				return customer.modifyHitpoints(modifier);
			}
		}
		return customers.get(0).modifyHitpoints(modifier);
	}

	@Override
	public float getDamage() {
		float sum = 0;
		for (Customer customer : customers) {
			if (customer.getHitpoints() > 0) {
				sum += customer.getDamage();
			}
		}
		return sum;
	}

	@Override
	public int getArmor() {
		for (Customer customer : customers) {
			if (customer.getHitpoints() > 0) {
				return customer.getArmor();
			}
		}
		return 0;
	}

	@Override
	public float takeDamage(float power) {
		for (Customer customer : customers) {
			if (customer.getHitpoints() > 0) {
				return customer.takeDamage(power);
			}
		}
		return 0;
	}

	@Override
	public int getInitialMoney() {
		int sum = 0;
		for (Customer customer : customers) {
			sum += customer.getInitialMoney();
		}
		return sum;
	}
	
    public List<ItemData> spawnItems() {
        List<ItemData> items = new ArrayList<ItemData>();
        for (Customer customer : getCustomers()) {
            items.addAll(customer.spawnItems());
        }
        return Util.getItemListNormalized(items);
    }
    
    @Override
    public ItemData getItem() {
        if (getCustomers().size() > 0) {
            return getCustomers().get(Util.getInt(0, getCustomers().size())).getItem();
        }
        return null;
    }
	
}
