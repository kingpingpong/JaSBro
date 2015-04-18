package jasbro.game.events;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.WeakList;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Illness.Flu;
import jasbro.game.character.conditions.Illness.Smallpox;
import jasbro.game.character.conditions.MonsterPregnancy;
import jasbro.game.character.conditions.Pregnancy;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.business.BusinessCalculations;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.Fame;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.MyEventListener;
import jasbro.game.interfaces.Person;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEvent.WorldEventVariables;
import jasbro.game.world.market.QuestManager;
import jasbro.gui.GuiUtil;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class EventManager implements MyEventListener, Serializable {
    private final static Logger log = Logger.getLogger(EventManager.class);
    private transient boolean shiftInProgress = false;

    private List<CentralEventlistener> listeners = new WeakList<CentralEventlistener>();

    public EventManager() {
    }

    public boolean performShift(GameData gameData) {
        shiftInProgress = true;
        try {
            // Just make sure these are properly initialized
            addListener(gameData.getStatCollector());
            addListener(gameData.getQuestManager());
    
            List<RunningActivity> activities = new ArrayList<RunningActivity>();
    
            notifyAll(new MyEvent(EventType.SHIFTSTART, null));
    
            // First handle idle characters
            for (Charakter character : gameData.getCharacters()) {
                if (character.getActivity() == null) {
                    PlannedActivity plannedActivity = new PlannedActivity();
                    plannedActivity.setType(ActivityType.IDLE);
                    plannedActivity.getCharacters().add(character);
                    activities.add(plannedActivity.getRunningActivity());
                }
            }
            for (int i = activities.size() - 1; i >= 0; i--) {
                activities.get(i).performActivity();
            }
            activities.clear();
    
            // Second: handle characters who are not inside a house
            for (CharacterLocation location : gameData.getOtherLocations()) {
                ActivityType activity = location.getSelectedActivity();
                if (activity == ActivityType.CUSTOMEVENT) {
                    triggerCustomEvent(location);
                }
                else if (activity != null && activity.isGroupActivity()) {
                    if (location.getCurrentUsage().getCharacters().size() > 0) {
                        activities.add(location.getCurrentUsage().getRunningActivity());
                    }
                } else {
                    for (Charakter character : location.getCurrentUsage().getCharacters()) {
                        PlannedActivity plannedActivity = new PlannedActivity(location.getCurrentUsage(), character);
                        activities.add(plannedActivity.getRunningActivity());
                    }
                }
                for (RunningActivity curActivity : activities) {
                    curActivity.performActivity();
                }
                activities.clear();
            }
    
            // Third handle the houses
            for (House house : gameData.getHouses()) {
                List<Charakter> whoresAndSupports = new ArrayList<Charakter>(); // Whoring is done separately
    
                for (Room room : house.getRooms()) {
                    ActivityType activity = room.getSelectedActivity();
                    if (!activity.isCustomerDependent()) {
                        if (activity == ActivityType.CUSTOMEVENT) {
                            triggerCustomEvent(room);
                        }
                        else if (activity.isGroupActivity()) {
                            if (room.getCurrentUsage().getCharacters().size() > 0) {
                                activities.add(room.getCurrentUsage().getRunningActivity());
                            }
                        } else {
                            for (Charakter character : room.getCurrentUsage().getCharacters()) {
                                PlannedActivity plannedActivity = new PlannedActivity(room.getCurrentUsage(), character);
                                activities.add(plannedActivity.getRunningActivity());
                            }
                        }
                    } else {
                        whoresAndSupports.addAll(room.getCurrentUsage().getCharacters()); // collect all business-related activities
                    }
                }
    
                for (RunningActivity activity : activities) {
                    activity.performActivity();
                }
                activities.clear();
    
                if (whoresAndSupports.size() > 0) {
                    doBusiness(whoresAndSupports, house);// perform all business-related activities
                }
            }
    
            // add an amount of dirt to each of the houses
            for (House house : gameData.getHouses()) {
                house.modDirt(house.getAmountPeople() * 3);
                house.applyDirtLimit(); // if dirt level is below zero, bring it back up to 0
            }
    
            // Advance Time
            return advanceTime(gameData);
        }
        finally {
            shiftInProgress = false;
        }
    }



    private void doBusiness(List<Charakter> whoresAndSupport, House house) {
        // Do advertising
        house.getAdvertising().performAdvertising(house);

        BusinessCalculations businessUtil = new BusinessCalculations();
        Fame fame = businessUtil.calculateFame(house, whoresAndSupport);
        int amountCustomers = businessUtil.calculateCustomerAmount(whoresAndSupport, fame);

        List<Customer> remainingCustomers = new ArrayList<Customer>(); // same as allCustomers, but customers get removed from list once they are assigned to a main activity
        List<Charakter> whores = new ArrayList<Charakter>();
        List<RunningActivity> mainActivities = new ArrayList<RunningActivity>();
        List<RunningActivity> secondaryActivities = new ArrayList<RunningActivity>();

        HashMap<Charakter, Float> remainingPossibleCustomers = new HashMap<Charakter, Float>();

        // init lists and maps for upcoming stuff
        for (int i = 0; i < whoresAndSupport.size(); i++) {
            Charakter character = whoresAndSupport.get(i);
            RunningActivity activity;
            if (Whore.class.isAssignableFrom(character.getActivity().getType().getActivityClass())) {
                whores.add(character); // standard whores
            } else {
                ActivityType activityType = character.getActivity().getType();
                if (activityType.isGroupActivity()) {
                    activity = character.getActivity().getRunningActivity();
                    for (Charakter curCharacter : character.getActivity().getCharacters()) { // if this is a group activity, remove everyone from the character list
                        if (whoresAndSupport.indexOf(curCharacter) <= i) {
                            i--;
                        }
                        whoresAndSupport.remove(curCharacter);
                    }
                } else {
                    PlannedActivity plannedActivity = new PlannedActivity(character.getActivity(), character);
                    activity = plannedActivity.getRunningActivity();
                }

                RunningActivity RunningActivity = activity;
                if (RunningActivity instanceof BusinessMainActivity) {
                    mainActivities.add(activity);
                }
                if (RunningActivity instanceof BusinessSecondaryActivity) {
                    secondaryActivities.add(activity);
                }
            }
        }
        for (Charakter whore : whores) {
            remainingPossibleCustomers.put(whore, whore.getPossibleAmountCustomers());
        }

        List<Customer> allCustomers = house.getSpawnData().spawn(amountCustomers, fame);
        amountCustomers = allCustomers.size();
        remainingCustomers.addAll(allCustomers);
        
        //Notify game objects about arriving customers
        MyEvent customersArriveEvent = new CustomersArriveEvent(house, allCustomers);
        handleEvent(customersArriveEvent);
        for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
            character.handleEvent(customersArriveEvent);
        }
            
        // inform player about amount customers
        if (amountCustomers != 0) {
            notifyCustomersArrival(house, allCustomers);
        }

        // Now go through all the main activities and assign main customers appropriately
        {
            List<BusinessMainActivity> curMainActivities = new ArrayList<BusinessMainActivity>();
            for (RunningActivity activity : mainActivities) {
                BusinessMainActivity RunningActivity = (BusinessMainActivity) activity;
                curMainActivities.add(RunningActivity);
            }
            businessUtil.assignCustomers(curMainActivities, remainingCustomers, null);
        }

        // Then assign customers to secondary activities
        {
            List<BusinessSecondaryActivity> curSecondaryActivities = new ArrayList<BusinessSecondaryActivity>();
            for (RunningActivity activity : secondaryActivities) {
                BusinessSecondaryActivity RunningActivity = (BusinessSecondaryActivity) activity;
                curSecondaryActivities.add(RunningActivity);
            }
            businessUtil.assignCustomersToSecondaryActivities(curSecondaryActivities, allCustomers);
        }

        // Then perform activities which are only secondary
        for (RunningActivity activity : secondaryActivities) {
            if (!(activity instanceof BusinessMainActivity)) {
                activity.performActivity();
            }
        }

        // Then perform all the main activities
        for (RunningActivity activity : mainActivities) {
            activity.performActivity();
        }
        
        //Remove customers which have basically no money left
        for (int i = 0; i < remainingCustomers.size(); i++) {
            Customer customer = remainingCustomers.get(i);
            if (customer.getMoney() < 5) {
                remainingCustomers.remove(customer);
            }
        }

        // Finally perform basic whoring
        int i = 0;
        do {
            mainActivities = new ArrayList<RunningActivity>();
            for (Charakter whore : whores) {
                PlannedActivity plannedActivity = new PlannedActivity(whore.getActivity(), whore);
                mainActivities.add(plannedActivity.getRunningActivity());
            }
            List<BusinessMainActivity> curMainActivities = new ArrayList<BusinessMainActivity>();
            for (RunningActivity activity : mainActivities) {
                curMainActivities.add((BusinessMainActivity) activity);
            }

            // Split customers up to add a bit more random to the mix
            List<Customer> curCustomers = new ArrayList<Customer>();
            int selectionSize = curMainActivities.size() * 2;
            int alternative = remainingCustomers.size() / Math.max(1, (8 - i)); 
            if (selectionSize < alternative) {
                selectionSize = alternative;
            }
            if (selectionSize >= remainingCustomers.size()) {
                curCustomers.addAll(remainingCustomers);
            }
            else {
                List<Customer> remainingCustomersCopy = new ArrayList<Customer>(remainingCustomers);
                for (int j = 0; j < selectionSize && remainingCustomersCopy.size() > 0; j++) {
                    do {
                        Customer customer = remainingCustomersCopy.get(Util.getInt(0, remainingCustomersCopy.size()));
                        if (!curCustomers.contains(customer)) {
                            curCustomers.add(customer);
                            remainingCustomersCopy.remove(customer);
                            break;
                        }
                    } while (true);
                }
            }

            businessUtil.assignCustomers(curMainActivities, curCustomers, remainingCustomers);
            Collections.shuffle(mainActivities);

            boolean assigned = false;
            for (RunningActivity activity : mainActivities) {
                BusinessMainActivity businessMainActivity = (BusinessMainActivity) activity;
                if (businessMainActivity.hasMainCustomer()) {
                    assigned = true;

                    activity.performActivity();

                    Charakter whore = activity.getCharacter();
                    Whore whoreActivity = (Whore) businessMainActivity;
                    remainingPossibleCustomers.put(whore, remainingPossibleCustomers.get(whore) - whoreActivity.getAmountActions());
                    if (remainingPossibleCustomers.get(whore) - whoreActivity.getAmountActions() < 0 || whore.getEnergy() < 1) {
                        whores.remove(whore);
                    }
                }
            }

            if (!assigned) {
                break; // no customer was assigned, Stop!
            }
        } while (remainingCustomers.size() > 0 && whores.size() > 0);
    }

    private void notifyCustomersArrival(House house, List<Customer> allCustomers) {
        List<Customer> normalCustomers = new ArrayList<Customer>();
        List<Customer> groups = new ArrayList<Customer>();
        for (Customer customer : allCustomers) {
            if (customer instanceof CustomerGroup) {
                groups.add(customer);
            } else {
                normalCustomers.add(customer);
            }
        }
        Object arguments[] = { normalCustomers.size() };
        String customerString = TextUtil.t("business.msgNormalCustomers", arguments);

        if (groups.size() > 0) {
            List<String> customerStringList = new ArrayList<String>();
            customerStringList.add(customerString);
            for (Customer customer : groups) {
                arguments[0] = customer.getName();
                customerStringList.add(TextUtil.t("grouplistitem", arguments));
            }
            customerString = TextUtil.listStrings(customerStringList);
        }

        Object arguments2[] = { customerString, house.getName() };
        MessageData messageData;
        if (house.getInternName() == null || house.getInternName().trim().equals("")) {
            messageData = new MessageData(TextUtil.t("business.msgCustomers1", arguments2), house.getImage(), null);
        } else {
            messageData = new MessageData(TextUtil.t("business.msgCustomers2", arguments2), house.getImage(), null);
        }
        messageData.setMessageGroupObject(house);
        messageData.createMessageScreen();
    }

    public boolean advanceTime(GameData gameData) {
        notifyAll(new MyEvent(EventType.NEXTSHIFT, null));

        if (gameData.getTime() == Time.NIGHT) {
            notifyAll(new MyEvent(EventType.NEXTDAY, null));
            gameData.setTime(gameData.getTime().getNextTimeOfDay());
            gameData.setDay(gameData.getDay() + 1);
            
            shiftInProgress = false;
            notifyAll(new MyEvent(EventType.NEXTSHIFTSTARTED, null));
            return true;
        } else {
            gameData.setTime(gameData.getTime().getNextTimeOfDay());
            
            shiftInProgress = false;
            notifyAll(new MyEvent(EventType.NEXTSHIFTSTARTED, null));
            return false;
        }
    }

    public List<CentralEventlistener> getListeners() {
        return listeners;
    }

    public void setListeners(List<CentralEventlistener> listeners) {
        this.listeners = listeners;
    }

    public void addListener(CentralEventlistener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void notifyAll(final MyEvent event) {
        for (final House house : Jasbro.getInstance().getData().getHouses()) {
            house.handleEvent(event);
        }
        for (final Charakter character : Jasbro.getInstance().getData().getCharacters()) {
            character.handleEvent(event);
        }
        handleEvent(event);
    }

    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.NEXTDAY && !(listeners.get(listeners.size()-1) instanceof QuestManager)) { //Make sure quest manager is last. Ensures it can mess with shop items and hirable character lists
            QuestManager questManager = Jasbro.getInstance().getData().getQuestManager();
            removeListener(questManager);
            addListener(questManager);
        }
        for (CentralEventlistener eventListener : new ArrayList<CentralEventlistener>(listeners)) {
            try {
                eventListener.handleCentralEvent(e);
            } catch (NullPointerException ex) {
            }
        }

        if (e.getType() == EventType.ACTIVITYPERFORMED) {
            RunningActivity activity = (RunningActivity) e.getSource();
            if (activity.getType() == ActivityType.SEX) {
                checkPregnancyPossible(activity.getSextype(), activity.getCharacters(), e);
            }
            else if (activity.getType() == ActivityType.WHORE) {
                List<Person> people = new ArrayList<Person>();
                people.add(activity.getCharacter());
                people.add(activity.getMainCustomer());
                checkPregnancyPossible(((Whore) activity).getSexType(), people, e);
            }
            else if (activity.getType() == ActivityType.ORGY || activity.getType() == ActivityType.THREESOME) {
                checkPregnancyPossible(Sextype.GROUP, activity.getCharacters(), e);
            }
            else if (activity.getSextype() == Sextype.MONSTER) {
                checkMonsterPregnancyPossible(activity.getCharacters(), e);
            }
        }
        else if (e.getType() == EventType.ENERGYZERO && !e.isCancelled()) {
            Charakter character = ((AttributeChangedEvent) e).getAttribute().getCharacter();
            if (Jasbro.getInstance().getData().getDay() > 150 && Util.getInt(0, 100) < 2) {
                character.addCondition(new Smallpox());
            } else {
                character.addCondition(new Flu());
            }
        } else if (e.getType() == EventType.HEALTHZERO && !e.isCancelled()) {
            Charakter character = ((AttributeChangedEvent) e).getAttribute().getCharacter();
            Jasbro.getInstance().removeCharacter(character);
            Jasbro.getInstance().getData().getProtagonist().getFame().modifyFame(-character.getFame().getFame()-500);
            
            
            if (!character.getType().isChildType()) {
                handleEvent(new MyEvent(EventType.CHARACTERDEATH, character));
                MessageData message = new MessageData(character.getName() + " died.", new ImageData("images/backgrounds/coffin.png"), null, true);
                GuiUtil.addMessageToEvent(message, e);
            }
            else {
                MessageData message = new MessageData(TextUtil.t("childcare.ill", character), ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character), character.getBackground(), true);
                GuiUtil.addMessageToEvent(message, e);
            }
        }
        else if (e.getType() == EventType.STATUSCHANGE && !shiftInProgress) {
            Jasbro.getInstance().getGui().updateStatus();
        }
    }

    public void removeListener(CentralEventlistener centralEventlistener) {
        listeners.remove(centralEventlistener);
    }
    
    public void checkPregnancyPossible(Sextype sexType, List<? extends Person> people, MyEvent event) {
        for (Person person : people) {
            if (person instanceof Charakter) {
                Charakter character = (Charakter) person;
                if (!character.isUsesContraceptives() && 
                        (character.getGender() != Gender.MALE || character.getTraits().contains(Trait.INHUMANPREGNANCY))) {
                    List<Person> otherPeople = new ArrayList<Person>(people);
                    otherPeople.remove(character);
                    for (Person otherPerson : otherPeople) {
                        if (Sextype.isPregnancyPossible(sexType, character, otherPerson)) {
                            int chance = character.getPregnancyChance();
                            if (otherPerson instanceof Charakter) {
                                chance += ((Charakter) otherPerson).getPregnancyChance();
                            }
                            chance = chance / 2;
                            if (Util.getInt(0, 100) < chance) {
                                character.addCondition(new Pregnancy(character, otherPerson, event));
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void checkMonsterPregnancyPossible(List<Charakter> characters, MyEvent event) {
        for (Charakter character : characters) {
            if (character.getGender() != Gender.MALE || character.getTraits().contains(Trait.INHUMANPREGNANCY)) {
                for (Condition condition : character.getConditions()) {
                    if (condition instanceof PregnancyInterface) {
                        continue; // Pregnant characters can't get pregnant
                    }
                }
                
                int chance = character.getPregnancyChance() / 2;
                if (character.isUsesContraceptives()) {
                    chance -= 30;
                }
                if (character.getTraits().contains(Trait.BEASTBREEDER)) {
                    chance += 50;
                }
                
                if (Util.getInt(0, 100) < chance) {
                    character.addCondition(new MonsterPregnancy(character, event));
                }
            }

        }
    }
    
    private void triggerCustomEvent(CharacterLocation location) {
        if (location.getCurrentUsage().getCharacters().size() > 0) {
            if (Jasbro.getInstance().getWorldEvents().containsKey(location.getCurrentUsage().getEventId())) {
                WorldEvent event = Jasbro.getInstance().getWorldEvents().get(location.getCurrentUsage().getEventId());
                try {
                    event.putAttribute(WorldEventVariables.questInstance, location.getCurrentUsage().getQuest());
                    event.putAttribute(WorldEventVariables.characters, location.getCurrentUsage().getCharacters());
                    event.putAttribute(WorldEventVariables.character, location.getCurrentUsage().getCharacters().get(0));
                    event.putAttribute(WorldEventVariables.location, location.getLocationType());
                    event.putAttribute(WorldEventVariables.people, location.getCurrentUsage().getCharacters());
                    event.execute();
                }
                finally {
                    event.reset();
                }
            }
            else {
                log.error("Event id not found: " + location.getCurrentUsage().getEventId());
            }
        }
    }

    public boolean isShiftInProgress() {
        return shiftInProgress;
    }
}
