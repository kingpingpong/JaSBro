package jasbro.game;

import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.ControlData;
import jasbro.game.character.Ownership;
import jasbro.game.events.EventManager;
import jasbro.game.events.EventType;
import jasbro.game.events.MoneyChangedEvent;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.House;
import jasbro.game.items.Inventory;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.Unlocks;
import jasbro.game.world.locations.LocationType;
import jasbro.game.world.market.AuctionHouse;
import jasbro.game.world.market.QuestManager;
import jasbro.game.world.market.Shop;
import jasbro.stats.StatCollector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Azrael
 */
@XmlRootElement(name = "gamedata")
public class GameData implements Serializable {
	//private final transient static Logger log = Logger.getLogger(GameData.class);
    private int day = 1;
    private Time time = Time.MORNING;
    private long money = 500;
    private List<House> houses;
    private AuctionHouse auctionHouse;
    private Shop shop;
    private Inventory inventory;

    private EventManager eventManager;
    private QuestManager questManager;
    private transient StatCollector statCollector;

    private List<Charakter> characters;
    
    private Charakter protagonist;
    
    private Map<LocationType, CharacterLocation> otherLocationMap;    
    private DefaultPreferences defaultPreferences;
    private Unlocks unlocks;
    
    public void init() {
        houses = new ArrayList<House>();
        characters = new ArrayList<Charakter>();
        eventManager = new EventManager();
        otherLocationMap = new EnumMap<>(LocationType.class);
        questManager = new QuestManager();
        inventory = new Inventory();
        shop = new Shop();
    }

    public List<House> getHouses() {
      return houses;
    }

    public void setHouses(List<House> houses) {
      this.houses = houses;
    }

    public long getMoney() {
        return money;
    }

    public List<Charakter> getSlaves() {
        List<Charakter> slaveList = new ArrayList<Charakter>();
        for (Charakter character : getCharacters()) {
            if (character.getType() == CharacterType.SLAVE) {
                slaveList.add(character);
            }
        }        
        return slaveList;
    }

    public List<Charakter> getTrainers() {
        List<Charakter> trainerList = new ArrayList<Charakter>();
        for (Charakter character : getCharacters()) {
            if (character.getType() == CharacterType.TRAINER) {
                trainerList.add(character);
            }
        }        
        return trainerList;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Time getTime() {
        return time;
    }
    public void setTime(Time time) {
        this.time = time;
    }
    

    
    public boolean canAfford(long price) {
    	return price <= money;
    }
    
    public void earnMoney(long amount, Object source) {
        getEventManager().handleEvent(new MoneyChangedEvent(EventType.MONEYEARNED, source, amount));
        this.money += amount;
        Jasbro.getInstance().getGui().updateStatus();
    }
    
    public void spendMoney(long amount, Object source) {
    	getEventManager().handleEvent(new MoneyChangedEvent(EventType.MONEYSPENT, source, amount));
    	this.money -= amount;
    	Jasbro.getInstance().getGui().updateStatus();
    	if (this.money < 0) {
    		eventManager.handleEvent(new MyEvent(EventType.BROKE, null));
    	}
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public List<Charakter> getCharacters() {
        if (characters == null) {
            characters = new ArrayList<Charakter>();
        }
        return characters;
    }
    
    public List<Charakter> getSlavesForSale() {
        List<Charakter> availableCharacters = new ArrayList<Charakter>();
        availableCharacters.addAll(getSlaves());
        for (int i = 0; i < availableCharacters.size(); i++) {
        	if (!availableCharacters.get(i).canSell()) {
        		availableCharacters.remove(i);
        		i--;
        	}
        }
        return availableCharacters;
    }

	public AuctionHouse getAuctionHouse() {
		if (auctionHouse == null) {
			auctionHouse = new AuctionHouse();
		}
		return auctionHouse;
	}

	public void setAuctionHouse(AuctionHouse auctionHouse) {
		this.auctionHouse = auctionHouse;
	}

	public Map<LocationType, CharacterLocation> getOtherLocationMap() {
		return otherLocationMap;
	}

	public void setOtherLocationMap(Map<LocationType, CharacterLocation> otherLocationMap) {
		this.otherLocationMap = otherLocationMap;
	}
	
	public Collection<CharacterLocation> getOtherLocations() {
		return otherLocationMap.values();
	}

	public QuestManager getQuestManager() {
		if (questManager == null) {
			questManager = new QuestManager();
		}
		return questManager;
	}

	public StatCollector getStatCollector() {
		if (statCollector == null) {
			statCollector = new StatCollector();
			getEventManager().addListener(statCollector);
		}
		return statCollector;
	}

	public void setStatCollector(StatCollector statCollector) {
		this.statCollector = statCollector;
	}

	public void setQuestManager(QuestManager questManager) {
		this.questManager = questManager;
	}
	
    public DefaultPreferences getDefaultPreferences() {
    	if (defaultPreferences == null) {
    		defaultPreferences = new DefaultPreferences();
    	}
		return defaultPreferences;
	}

	public Shop getShop() {
		if (shop == null) {
			shop = new Shop();
		}
		return shop;
	}

	public Inventory getInventory() {
		if (inventory == null) {
			inventory = new Inventory();
		}
		return inventory;
	}
	
    public Unlocks getUnlocks() {
        if (unlocks == null) {
            unlocks = new Unlocks();
        }
        return unlocks;
    }
	
	public Charakter getProtagonist() {
	    if (protagonist == null) {
	        for (Charakter character : getTrainers()) {
	            character.setOwnership(Ownership.OWNED);
	            protagonist = character;
	            break;
	        }
	        if (protagonist == null) {
	            protagonist = getSlaves().get(0);
	        }
	    }
	    return protagonist;
	}

    public void setProtagonist(Charakter protagonist) {
        this.protagonist = protagonist;
    }
    
    public ControlData calculateControl() {
        ControlData controlData = new ControlData();
        for (Charakter character : getCharacters()) {
            controlData.add(character.getControl());
        }
        return controlData;
    }
}
