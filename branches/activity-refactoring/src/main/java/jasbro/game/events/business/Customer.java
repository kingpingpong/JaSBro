package jasbro.game.events.business;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Enemy;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Inventory.ItemData;
import jasbro.game.items.Item;
import jasbro.game.items.ItemLocation;
import jasbro.game.items.ItemSpawnData;

import java.util.ArrayList;
import java.util.List;


public class Customer extends Enemy implements Person {
    private CustomerType type;
    private int initialMoney;
    private int money;
    private int satisfactionAmount;
    private ActivityType preferedSecondaryActivity1;
    private ActivityType preferedSecondaryActivity2;
    private Sextype preferredSextype;
    private Gender gender = Gender.MALE;
    private float importance;
    private int initialSatisfaction;
    private List<SatisfactionModifier> satisfactionModifiers = new ArrayList<SatisfactionModifier>();
    private float payModifier = 1;
    
    public Customer() {
        super();
    }    
    public Customer(CustomerType type, int money, int startingSatisfaction, float importance) {
        super();
        this.type = type;
        this.initialMoney = money;
        this.money = money;
        this.satisfactionAmount = startingSatisfaction;
        this.initialSatisfaction = startingSatisfaction;
        this.importance = importance;
    }
    
    
    //Readies customer object for combat
    public void initCombat() {
    	int customerJob=Util.getInt(1, 7);
    	double bonusDamage=0;		double bonusCritChance=0;
    	double bonusHit=0;			int bonusHealth=0;
    	double bonusDodge=0;		double bonusCritDmg=0;
    	double bonusBlockChance=0;	double bonusBlockAmount=0;
    	
    	switch(customerJob){
    	case 1://Fighter
    		bonusHealth=15; bonusDamage=1; bonusBlockAmount=15;
    		break;
    	case 2://Mage
    		bonusHealth=-20; bonusDamage=1; bonusDodge=-3; 
    		break;
    	case 3://Berzerker
    		bonusHealth=40; bonusDamage=2; bonusHit=-10;
    		break;
    	case 4://Bandit
    		bonusDodge=10; bonusCritChance=10; bonusCritDmg=10; bonusHealth=-10;
    		break;
    	case 5://Boxer
    		bonusHit=15; bonusDamage=1; bonusBlockChance=10; bonusBlockAmount=10;
    		break;
    	case 6://Assassin
    		bonusHealth=-15; bonusCritChance=10; bonusCritDmg=30;
    		break;
    	case 7://Sorcerer
    		bonusHealth=-50; bonusDamage=5; bonusDodge=-10;
    		break;
    	}
    	
    			//Init customer combat stuff
        if (type == CustomerType.BUM) {
            setHitpoints(70+bonusHealth);
        }
        else{
        	setHitpoints(100+bonusHealth);
        }
        
        if (type == CustomerType.SOLDIER) {
            setAttribute(CalculatedAttribute.DAMAGE, 3d+bonusDamage);
        }
        else if (type == CustomerType.CELEBRITY) {
            setAttribute(CalculatedAttribute.DAMAGE, 1d+bonusDamage);
        }
        else {
            setAttribute(CalculatedAttribute.DAMAGE, 0.4d + bonusDamage + ((int)(getImportance() / 5f)));
        }
        
        int amount = 10 + ((int)(getImportance() * 60));
        if (type == CustomerType.SOLDIER) {
            amount = 500;
        }
        else if (type == CustomerType.CELEBRITY) {
            amount = 200;
        }
        setAttribute(CalculatedAttribute.BLOCKAMOUNT, 3d+bonusBlockAmount);
        
        double value = 0.3d;
        double mitigation = 4;
        do {
            mitigation += value;
            amount--;
            value = value / 1.005f;
        }
        while (amount > 0);
        setAttribute(CalculatedAttribute.ARMORPERCENT, mitigation);
        
        if (getType() == CustomerType.SOLDIER) {
            setAttribute(CalculatedAttribute.CRITCHANCE, 10d+bonusCritChance);
            setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 13d+bonusCritDmg);
            setAttribute(CalculatedAttribute.DODGE, 10d+bonusDodge);
            setAttribute(CalculatedAttribute.HIT, 5d+bonusHit);
            setAttribute(CalculatedAttribute.BLOCKCHANCE, 30d+bonusBlockChance);
        }
        else if (getType() == CustomerType.CELEBRITY) {
            setAttribute(CalculatedAttribute.CRITCHANCE, 1d+bonusCritChance);
            setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 10d+bonusCritDmg);
            setAttribute(CalculatedAttribute.DODGE, 5d+bonusDodge);
        }
        else {
            setAttribute(CalculatedAttribute.CRITCHANCE, (double)getImportance()+bonusCritChance);
            setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 10d+bonusCritDmg);
            setAttribute(CalculatedAttribute.DODGE, (double)getImportance()+bonusDodge);
        }
        setMaxHitpoints(getHitpoints());
    }


    public String getName() {
        return type.getText(this);
    }
    
    public int getMoney() {
        return money;
    }
    public int getSatisfactionAmount() {
        return satisfactionAmount;
    }

    public Sextype getPreferredSextype() {
        return preferredSextype;
    }

    public void setPreferredSextype(Sextype preferredSextype) {
        this.preferredSextype = preferredSextype;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
	public CustomerType getType() {
		return type;
	}
	public void setType(CustomerType type) {
		this.type = type;
	}
	public int getMaxSecondaryActivities() {
		return type.getMaxSecondaryActivities();
	}
	public float getImportance() {
		return importance;
	}
	public void setImportance(float importance) {
		this.importance = importance;
	}
	
	public Satisfaction getSatisfaction() {
		return Satisfaction.getSatisfaction(getSatisfactionAmount());
	}
	
    public int getInitialSatisfaction() {
		return initialSatisfaction;
	}
    
	public void addToSatisfaction(int mod, Object satisfactionModifier) {
        this.satisfactionAmount += mod;
        satisfactionModifiers.add(new SatisfactionModifier(mod, satisfactionModifier));
    }
    
    public int payFixed(int amount) {        
        if (amount > getMoney()) {
        	amount = getMoney();
        }
        if (amount < 0) {
            amount = 0;
        }
        money = money - amount;
        return amount;
    }
	
    public int pay(int amount, float modifier) {
        amount = (int) (amount * modifier * payModifier);
        
        if (amount > getMoney()) {
        	amount = getMoney();
        }
        if (amount < 0) {
            amount = 0;
        }
        money = money - amount;
        return amount;
    }
	
    public int pay(float modifier) {
        int payment = 0;        
        payment = getInitialMoney() / 10 + 50;        
        payment = (int) Util.getPercent(payment, getSatisfaction().getMoneyModifierPercent());        
        return pay(payment, modifier);
    }    
   
    public List<SatisfactionModifier> getSatisfactionModifiers() {
		return satisfactionModifiers;
	}

    public void changePayModifier(float modifier) {
    	payModifier += modifier;
    }



    public void setInitialMoney(int initialMoney) {
		this.initialMoney = initialMoney;
		this.money = initialMoney;
	}

	public int getInitialMoney() {
		return initialMoney;
	}

	public class SatisfactionModifier {
    	private int modifiedBy;
    	private Object source; 
    	
		public SatisfactionModifier() {
			super();
		}
		public SatisfactionModifier(int modidifyBy, Object source) {
			super();
			this.modifiedBy = modidifyBy;
			this.source = source;
		}
		
		public int getModifiedBy() {
			return modifiedBy;
		}
		public void setModifiedBy(int modidifyBy) {
			this.modifiedBy = modidifyBy;
		}
		public Object getSource() {
			return source;
		}
		public void setSource(Object source) {
			this.source = source;
		}
    }
    
    public List<ItemData> spawnItems() {
        List<ItemData> items = new ArrayList<ItemData>();
        items.add(getItem());
        return Util.getItemListNormalized(items);
    }
    
    public ItemData getItem() {
        List<ItemData> items = new ArrayList<ItemData>();
        List<ItemSpawnData> spawnChances = new ArrayList<ItemSpawnData>();
        int sum = 0;
        
        for (Item item : Jasbro.getInstance().getAvailableItemsByLocation(ItemLocation.valueOf(getType().toString()))) {
            for (ItemSpawnData itemSpawnData : item.getSpawnData()) {
                if (itemSpawnData.getItemLocation() == ItemLocation.valueOf(getType().toString())) {
                    ItemData itemData = new ItemData(item, Util.getInt(itemSpawnData.getMinAmount(), itemSpawnData.getMaxAmount() + 1));
                    if (itemData.getAmount() > 0) {
                        items.add(itemData);
                        spawnChances.add(itemSpawnData);
                        sum += itemSpawnData.getChance();
                    }
                }
            }
        }
        
        if (items.size() > 0) {            
            int selected = Util.getInt(0, sum);
            int curValue = 0;
            for (int i = 0; i < items.size(); i++) {
                curValue += spawnChances.get(i).getChance();
                if (curValue >= selected) {
                    return items.get(i);
                }
            }
            return null;
        }
        else {
            return null;
        }
    }
	public ActivityType getPreferedSecondaryActivity1() {
		return preferedSecondaryActivity1;
	}
	public void setPreferedSecondaryActivity1(ActivityType preferedSecondaryActivity1) {
		this.preferedSecondaryActivity1 = preferedSecondaryActivity1;
	}
	public ActivityType getPreferedSecondaryActivity2() {
		return preferedSecondaryActivity2;
	}
	public void setPreferedSecondaryActivity2(ActivityType preferedSecondaryActivity2) {
		this.preferedSecondaryActivity2 = preferedSecondaryActivity2;
	}


    
    
}
