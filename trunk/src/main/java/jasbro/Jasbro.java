package jasbro;

import jasbro.game.GameData;
import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterFileLoader;
import jasbro.game.character.CharacterManipulationManager;
import jasbro.game.character.CharacterSpawner;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Ownership;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.conditions.StartingAtTheBottom;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.housing.House;
import jasbro.game.items.Equipment;
import jasbro.game.items.Item;
import jasbro.game.items.ItemFileLoader;
import jasbro.game.items.ItemLocation;
import jasbro.game.items.ItemSpawnData;
import jasbro.game.items.ItemType;
import jasbro.game.items.UnlockItem;
import jasbro.game.world.customContent.CustomQuestTemplate;
import jasbro.game.world.customContent.EventAndQuestFileLoader;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.game.world.customContent.npc.NpcFileLoader;
import jasbro.gui.CharacterFilterListModel;
import jasbro.gui.RPGView;
import jasbro.gui.pages.ManagementScreen;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.security.JasbroPolicy;
import jasbro.security.ThreadPoolAccessPermission;
import jasbro.stats.StatCollector;
import jasbro.texts.TextUtil;
import jasbro.util.Comparators;
import jasbro.util.ConfigHandler;
import jasbro.gui.pictures.ImageUtil;

import java.io.File;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.UIManager;

import org.apache.log4j.Logger;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * The main class of the application.
 * 
 * @author Azrael
 */
public class Jasbro {
	private final static Logger log = Logger.getLogger(Jasbro.class);
	private static Jasbro instance;
	
    private final static ExecutorService threadPool = Executors.newCachedThreadPool();
	
	private RPGView gui;
	private GameData data;
	private List<CharacterBase> bases;
	
	private Map<String, Item> items;
	
	private Map<String, CustomQuestTemplate> customQuestTemplates;
	private Map<String, WorldEvent> worldEvents;
	private Map<String, ComplexEnemyTemplate> enemyTemplates;
	
	private Interpreter interpreter;

	public Jasbro() {
		instance = this;
	}

	/**
	 * At startup create and show the main frame of the application.
	 */
	protected void startup() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				log.error("Uncaught Exception", e);
			}
		});
		
		gui = new RPGView();
		gui.setVisible(true);
		gui.showMainMenu();
	}

	/**
	 * A convenient static getter for the application instance.
	 * 
	 * @return the instance of RPGApp
	 */
	public static Jasbro getInstance() {
		if (instance == null) {
			instance = new Jasbro();
		}
		return instance;
	}

	/**
	 * Main method launching the application.
	 */
	public static void main(String[] args) {
		setupSecurity();
		
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ConfigHandler.loadConfig();
                    if (ConfigHandler.isUseSystemLookAndFeel()) {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    }
                    new Jasbro().startup();
                } catch (Exception e) {
                    log.error("Error on startup", e);
                }
            }
        });
	}

	public static void setupSecurity() {
		log.debug("Installing security policy...");
		// first install the policy...
		Policy.setPolicy(new JasbroPolicy());
		// ... then activate the security manage to enforce the policy
		System.setSecurityManager(new SecurityManager());
	}

	public synchronized void save(File file) {
		(new SaveAndLoadPerformer()).save(file, getData());
	}

	public void continueLastGame() {
		File files[] = new File(".").listFiles();
		File lastSave = null;

		for (File file : files) {
			if (file.getName().matches("save\\-?\\d+\\.xml")) {
				if (lastSave == null || lastSave.lastModified() < file.lastModified()) {
					lastSave = file;
				}
			}
		}
		if (lastSave != null) {
			load(lastSave);
		}
	}

	public synchronized void load(File selectedFile) {
		this.data = (new SaveAndLoadPerformer()).load(selectedFile);
		List<Charakter> characters = new ArrayList<Charakter>();
		characters.addAll(data.getCharacters());
		characters.addAll(data.getAuctionHouse().getSlaves());
		List<CharacterBase> bases = getCharacterBases();
		for (Charakter character : characters) {
			for (CharacterBase base : bases) {
				if (character.getBaseId().equals(base.getId())) {
					character.setBase(base);
					break;
				}
			}
		}

		for (Charakter character : characters) {
			if (character.getBase() == null) {
			    if (!log.isDebugEnabled()) {
		             log.error("Character base not found, character removed " + character.getBaseId());
		             removeCharacter(character);
			    }
			    else {
			        do { //In debug use random character bases to replace missing ones
			            character.setBase(bases.get(Util.getInt(0, bases.size())));
			        }
			        while (character.getBase().getType() != null &&
			                character.getBase().getType().isChildType() != character.getType().isChildType());
			    }
			}
		}
		getData().getUnlocks().init(); //perform unlock init
		
		gui.setFilteredModel(new CharacterFilterListModel());
		gui.removeAllLayers();
		gui.addLayer(new ManagementScreen());
		gui.repaint();
	}

	public GameData getData() {
		return data;
	}

	public RPGView getGui() {
		return gui;
	}

	public void setGui(RPGView view) {
		this.gui = view;
	}


	public void advanceShift() {
		gui.removeAllLayers();
		System.gc();
        if (log.isDebugEnabled()) {
        	 log.error("Before shift: Current memory(mb): " + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + 
                    " Max memory: " + (Runtime.getRuntime().maxMemory() / 1024 / 1024));
        }
		try {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			GameData gameData = getData();
			StatCollector statCollector = gameData.getStatCollector();
	    	gameData.getEventManager().performShift(getData());
			Thread.sleep(200);
			statCollector.setMoneyAfterShift(gameData.getMoney());
			statCollector.showStatScreen(gameData.getTime().getPreviousTimeOfDay(), true);
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		} catch (Exception e) {
			log.error("Error on performing shift", e);
		}
	    gui.setFilteredModel(new CharacterFilterListModel());
		gui.addLayerBottom(new ManagementScreen());
		this.gui.validate();
		this.gui.repaint();
		
		System.gc();
        if (log.isDebugEnabled()) {
        	log.error("After shift: Current memory(mb): " + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + 
                    " Max memory: " + (Runtime.getRuntime().maxMemory() / 1024 / 1024));
        }
	}
	
	public void advanceDay() {
		gui.removeAllLayers();
		System.gc();
		try {
			GameData gameData = getData();
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			do {
		    	gameData.getEventManager().performShift(getData());
				Thread.sleep(200);
				gameData.getStatCollector().setMoneyAfterShift(gameData.getMoney());
				if (gameData.getTime().isNewDay()) {
					gameData.getStatCollector().showStatScreen(null, true);
				}
				else {
					gameData.getStatCollector().showStatScreen(gameData.getTime().getPreviousTimeOfDay(), false);
				}
				
			}
			while (!gameData.getTime().isNewDay());
			Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		} catch (Exception e) {
			log.error("Error on performing shift", e);
		}
	    gui.setFilteredModel(new CharacterFilterListModel());
		gui.addLayerBottom(new ManagementScreen());
		this.gui.validate();
		this.gui.repaint();
        System.gc();
        if (log.isDebugEnabled()) {
            System.out.println("After day: Current memory(mb): " + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + 
                    " Max memory: " + (Runtime.getRuntime().maxMemory() / 1024 / 1024));
        }
	}

	public void startNewGame(CharacterBase trainerBase, CharacterBase firstSlave) {
		data = new GameData();
		data.init();

		Charakter trainer = CharacterSpawner.create(trainerBase, CharacterType.TRAINER);
		data.getCharacters().add(trainer);
		trainer.setOwnership(Ownership.OWNED);
		trainer.addTrait(Trait.CERTIFIEDTRAINER);
		trainer.addCondition(new StartingAtTheBottom());
		
		Charakter slave = CharacterSpawner.create(firstSlave, CharacterType.SLAVE);
		data.getCharacters().add(slave);
		slave.setOwnership(Ownership.OWNED);

		House house = new House.Hut();
		getData().getHouses().add(house);
		
		getData().getUnlocks().init(); //perform unlock init

		gui.showHouseManagementScreen();
		
		getData().getEventManager().notifyAll(new MyEvent(EventType.GAMESTART, null));
	}
	
	public synchronized List<CharacterBase> getCharacterBases() {
		if (bases == null) {
			loadBases();
			Collections.sort(bases, new Comparators.CharacterBaseNameComparator());
			Collections.sort(bases, new Comparators.CharacterTypeComparator());
		}
		return bases;
	}

	public List<CharacterBase> getSlaveBases() {
		List<CharacterBase> slaveBases = new ArrayList<CharacterBase>();
		for (CharacterBase characterBase : getCharacterBases()) {
			if (characterBase.getType() == CharacterType.SLAVE || characterBase.getType() == null) {
				slaveBases.add(characterBase);
			}
		}
		return slaveBases;
	}

	public List<CharacterBase> getTrainerBases() {
		List<CharacterBase> trainerBases = new ArrayList<CharacterBase>();
		for (CharacterBase characterBase : getCharacterBases()) {
			if (characterBase.getType() == CharacterType.TRAINER || characterBase.getType() == null) {
				trainerBases.add(characterBase);
			}
		}
		return trainerBases;
	}

   public List<CharacterBase> getUnusedBases() {
        List<CharacterBase> unusedBases = new ArrayList<CharacterBase>();
        unusedBases.addAll(getCharacterBases());
        for (Charakter character : getData().getCharacters()) {
            unusedBases.remove(character.getBase());
        }
        for (Charakter character : getData().getAuctionHouse().getSlaves()) {
            unusedBases.remove(character.getBase());
        }
        if (unusedBases.size() == 0) {
            unusedBases.addAll(getSlaveBases());
        }
        return unusedBases;
    }
	
	public List<CharacterBase> getUnusedSlaveBases() {
		List<CharacterBase> slaveBases = new ArrayList<CharacterBase>();
		slaveBases.addAll(getSlaveBases());
		for (Charakter character : getData().getCharacters()) {
			slaveBases.remove(character.getBase());
		}
		for (Charakter character : getData().getAuctionHouse().getSlaves()) {
			slaveBases.remove(character.getBase());
		}
		if (slaveBases.size() == 0) {
			slaveBases.addAll(getSlaveBases());
		}
		return slaveBases;
	}

	public List<CharacterBase> getUnusedTrainerBases() {
		List<CharacterBase> bases = new ArrayList<CharacterBase>();
		bases.addAll(getTrainerBases());
		for (Charakter character : getData().getCharacters()) {
			bases.remove(character.getBase());
		}
		if (bases.size() == 0) {
			bases.addAll(getTrainerBases());
		}
		return bases;
	}

	public Charakter generateBasicSlave() {
		List<CharacterBase> bases = Jasbro.getInstance().getUnusedSlaveBases();
		if (bases.size() == 0) {
			bases = getSlaveBases();
		}
		CharacterBase base = bases.get(Util.getRnd().nextInt(bases.size()));
		Charakter character = CharacterSpawner.create(base, CharacterType.SLAVE);
		character.setOwnership(Ownership.NOTOWNED);
		return character;
	}

	public Charakter generateBasicTrainer() {
		List<CharacterBase> bases = Jasbro.getInstance().getUnusedTrainerBases();
		if (bases.size() == 0) {
			bases = getTrainerBases();
		}
		CharacterBase base = bases.get(Util.getRnd().nextInt(bases.size()));
		Charakter character = CharacterSpawner.create(base, CharacterType.TRAINER);
		character.setOwnership(Ownership.NOTOWNED);
		return character;
	}

	private synchronized void loadBases() {
		bases = CharacterFileLoader.getInstance().loadAllCharacters(true);
		
		List<ImageTag> additionalTags = new ArrayList<ImageTag>();
		for (CharacterBase characterBase : bases) {
			for (ImageData image : characterBase.getImages()) {
				for (ImageTag tag : image.getTags()) {
					if (tag.getIncludedTag() != null) {
						additionalTags.add(tag.getIncludedTag());
					}
				}
				image.getTags().addAll(additionalTags);
				additionalTags.clear();
			}
		}
	}
	
	private synchronized void loadItems() {
		items = new HashMap<String, Item>();
		List<Item> items = ItemFileLoader.getInstance().loadAllItems();
		for (Item item : items) {
			this.items.put(item.getId(), item);
		}
	}

	public void removeCharacter(Charakter character) {
		data.getCharacters().remove(character);
		for (PlannedActivity activity : character.getActivities().values()) {
			if (activity != null) {
				activity.removeCharacter(character);
			}
		}
		
		for (Equipment equipment : character.getCharacterInventory().listEquipment()) {
		    if (!equipment.getId().equals("RegularClothes")) {
		        getData().getInventory().addItem(equipment);
		    }
		}
		MyEvent event = new MyEvent(EventType.CHARACTERLOST, character);
		data.getEventManager().handleEvent(event);
		
		if (character == data.getProtagonist()) { // Main character died
		    List<Charakter> candidates = new ArrayList<Charakter>();
		    for (Charakter characterTmp : data.getCharacters()) {
		        if (characterTmp.getOwnership() == Ownership.OWNED || 
		                characterTmp.getOwnership() == Ownership.CONTRACT) {
		            candidates.add(characterTmp);
		        }
		    }
		    if (candidates.size() == 0) {
		        gui.showGameOverScreen();
		    }
		    else {
		        List<SelectionData<Charakter>> options = new ArrayList<SelectionData<Charakter>>();
		        for (Charakter curCharacter : candidates) {
	                SelectionData<Charakter> option = new SelectionData<Charakter>();
	                option.setSelectionObject(curCharacter);
	                option.setButtonText(curCharacter.getName());
	                options.add(option);
		        }
		        SelectionData<Charakter> selectedOption = new SelectionScreen<Charakter>().select(options, 
	                    new ImageData("images/backgrounds/coffin.png"), null, 
	                    new ImageData("images/backgrounds/sky.jpg"), 
	                    TextUtil.t("events.protagonistDead", character));
		        Charakter newMainChar = selectedOption.getSelectionObject();
                newMainChar.setOwnership(Ownership.OWNED);
		        if (newMainChar.getType() == CharacterType.TRAINER) {
		            new MessageScreen(TextUtil.t("events.newProtagonist.trainer", newMainChar), 
		                    ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, newMainChar), newMainChar.getBackground());
		        }
		        else {
		            CharacterManipulationManager.changeType(newMainChar, CharacterType.TRAINER);
		            new MessageScreen(TextUtil.t("events.newProtagonist.slave", newMainChar, character), 
                            ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, newMainChar), newMainChar.getBackground());
		        }
                data.setProtagonist(newMainChar);
		    }
		    
		}
	}

	public void addCentralListener(CentralEventlistener listener) {
		getData().getEventManager().addListener(listener);

	}

	public static ExecutorService getThreadpool() {
	    SecurityManager secManager = System.getSecurityManager();
	    if (secManager != null) {
	        secManager.checkPermission(new ThreadPoolAccessPermission());
	    }
		return threadPool;
	}

	public Map<String, Item> getItems() {
		if (items == null) {
			loadItems();
		}
		return items;
	}

	public synchronized void setItems(Map<String, Item> items) {
		this.items = items;
	}

    public Map<String, CustomQuestTemplate> getCustomQuestTemplates() {
        if (customQuestTemplates == null) {
            customQuestTemplates = new HashMap<String, CustomQuestTemplate>();
            for (CustomQuestTemplate questTemplate : EventAndQuestFileLoader.getInstance().loadAllCustomQuests()) {
                customQuestTemplates.put(questTemplate.getId(), questTemplate);
            }
        }
        return customQuestTemplates;
    }

    public Map<String, WorldEvent> getWorldEvents() {
        if (worldEvents == null) {
            worldEvents = new HashMap<String, WorldEvent>();
            for (WorldEvent worldEvent : EventAndQuestFileLoader.getInstance().loadAllCustomEvents()) {
                worldEvents.put(worldEvent.getId(), worldEvent);
            }
        }
        return worldEvents;
    }
    
    public Map<String, ComplexEnemyTemplate> getEnemyTemplates() {
        if (enemyTemplates == null) {
            enemyTemplates = new HashMap<String, ComplexEnemyTemplate>();
            for (ComplexEnemyTemplate complexEnemyTemplate : NpcFileLoader.getInstance().loadAllEnemies()) {
                enemyTemplates.put(complexEnemyTemplate.getId(), complexEnemyTemplate);
            }
        }
        return enemyTemplates;
    }
	
	public void performEvent(String eventId) {
        if (getWorldEvents().containsKey(eventId)) {
            try {
                WorldEvent event = getWorldEvents().get(eventId);
                event.execute();
            } catch (Exception e) {
                log.error("Error while performing event " + eventId, e);
            }
        }
    }
	
	public List<Item> getAvailableItemsByLocation(ItemLocation itemLocation) {
	    List<Item> items = new ArrayList<Item>();
        for (Item item : Jasbro.getInstance().getItems().values()) {
            for (ItemSpawnData itemSpawnData : item.getSpawnData()) {
                if (itemSpawnData.getItemLocation() == itemLocation) {
                    if (item.getType() == ItemType.UNLOCK) {
                        if (!getData().getUnlocks().getUnlockedObjects().contains(((UnlockItem) item).getUnlockObject())) {
                            items.add(item);
                        }
                    }
                    else {
                        items.add(item);
                    }
                }
            }
        }
	    return items;
	}

    public Interpreter getInterpreter() {
        if (interpreter == null) {
            interpreter = new Interpreter();
            try {
                interpreter.eval("import jasbro.*;" +                
                                 "import jasbro.game.*;" +
                                 "import jasbro.game.character.*;" +
                                 "import jasbro.game.character.activities.*;" +
                                 "import jasbro.game.character.activities.requirements.*;" +
                                 "import jasbro.game.character.activities.sub.*;" +
                                 "import jasbro.game.character.activities.sub.business.*;" +
                                 "import jasbro.game.character.activities.sub.childcare.*;" +
                                 "import jasbro.game.character.activities.sub.whore.*;" +
                                 "import jasbro.game.character.attributes.*;" +
                                 "import jasbro.game.character.battle.*;" +
                                 "import jasbro.game.character.conditions.*;" +
                                 "import jasbro.game.character.specialization.*;" +
                                 "import jasbro.game.character.traits.*;" +
                                 "import jasbro.game.character.warnings.*;" +
                                 "import jasbro.game.events.*;" +
                                 "import jasbro.game.events.business.*;" +
                                 "import jasbro.game.events.rooms.*;" +
                                 "import jasbro.game.housing.*;" +
                                 "import jasbro.game.interfaces.*;" +
                                 "import jasbro.game.items.*;" +
                                 "import jasbro.game.quests.*;" +
                                 "import jasbro.game.world.*;" +
                                 "import jasbro.game.world.customContent.*;" +
                                 "import jasbro.game.world.customContent.effects.*;" +
                                 "import jasbro.game.world.customContent.npc.*;" +
                                 "import jasbro.game.world.locations.*;" +
                                 "import jasbro.game.world.market.*;" +
                                 "import jasbro.gui.*;" +
                                 "import jasbro.gui.pages.*;" +
                                 "import jasbro.gui.pictures.*;" +
                                 "import jasbro.stats.*;" +
                                 "import jasbro.texts.*;" +
                                 "import jasbro.util.*;" +
                                 "import java.util.*;");
            } catch (EvalError e) {
               log.error("Error when initializing interpreter", e);
            }
        }        
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void setCustomQuestTemplates(Map<String, CustomQuestTemplate> customQuestTemplates) {
        this.customQuestTemplates = customQuestTemplates;
    }

    public void setWorldEvents(Map<String, WorldEvent> worldEvents) {
        this.worldEvents = worldEvents;
    }
	
    //Try to prevent memory leaks
    public void cleanupInterpreter() {
        log.debug("Reset Interpreter!");
        try {
            for (String variable : (String[])interpreter.eval("return this.variables;")) {
                interpreter.unset(variable);
            }
        } catch (EvalError e) {
            log.error("Error when resetting interpreter", e);
        }
    }

    public void setCharacterBases(List<CharacterBase> characters) {
        this.bases = characters;
    }

    public void setEnemyTemplates(Map<String, ComplexEnemyTemplate> enemyTemplates) {
        this.enemyTemplates = enemyTemplates;
    }
    
    
}
