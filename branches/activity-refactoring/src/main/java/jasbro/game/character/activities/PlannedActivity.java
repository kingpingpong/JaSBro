package jasbro.game.character.activities;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.gui.pages.SelectionData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class PlannedActivity implements Serializable {
    private final static Logger log = Logger.getLogger(PlannedActivity.class);
    private ActivityType type;
    private List<Charakter> characters = new ArrayList<Charakter>();
    private CharacterLocation source;
    private SelectionData<?> selectedOption = null;
    private String eventId;
    private CustomQuest quest;
    
    public PlannedActivity() {
    }
    

    public PlannedActivity(CharacterLocation location, ActivityType activity) {
        this.source = location;
        this.type = activity;
    }
    
    public PlannedActivity(PlannedActivity plannedActivity) {
        this.source = plannedActivity.source;
        this.type = plannedActivity.type;
        characters.addAll(plannedActivity.getCharacters());
        selectedOption = plannedActivity.selectedOption;
    }
    
    public PlannedActivity(ActivityType newType, PlannedActivity plannedActivity, Charakter...charakters) {
        this.source = plannedActivity.source;
        this.type = newType;
        selectedOption = plannedActivity.selectedOption;
        for (Charakter character : charakters) {
            this.characters.add(character);
        }
    }
    
    public PlannedActivity(PlannedActivity plannedActivity, Charakter...charakters) {
        this.source = plannedActivity.source;
        this.type = plannedActivity.type;
        selectedOption = plannedActivity.selectedOption;
        for (Charakter character : charakters) {
            this.characters.add(character);
        }
    }

    public ActivityType getType() {
        if (type == null) {
            checkPossibleActivities();
        }
        return type;
    }
    
    public void setType(ActivityType type) {
        if (type != this.type && type != ActivityType.CUSTOMEVENT) {
            this.type = type;
            this.eventId = null;
            this.quest = null;
            
            if (source != null) {
                MyEvent e = new MyEvent(EventType.ACTIVITYCHANGE, this);
                for (Charakter character : getCharacters()) {
                    character.handleEvent(e);
                }
                source.fireEvent(e);
            }
        }
    }
    
    public List<Charakter> getCharacters() {
        return characters;
    }
    public void setCharacters(List<Charakter> characters) {
        this.characters = characters;
    }
    public CharacterLocation getSource() {
        return source;
    }
    public void setSource(CharacterLocation location) {
        this.source = location;
    }    

    public void removeCharacter(Charakter charakter) {
        characters.remove(charakter);
        charakter.removeActivity(this);
        
        checkPossibleActivities();
        MyEvent e = new MyEvent(EventType.ACTIVITYCHANGE, this);
        charakter.handleEvent(e);
        source.fireEvent(e);
    }
    
    public void removeAllCharacters() {
        while (getCharacters().size() > 0) {
            removeCharacter(getCharacters().get(0));
        }
    }
    
    public boolean add(Charakter character) {
        if (getCharacters().size() < source.getMaxPeople()) {
            characters.add(character);
            character.setActivity(this);
            
            checkPossibleActivities();
            return true;
        }
        else {
            return false;
        }
    }
    
    public ActivityExecutor getActivityExecutor(Shift shift) {
    	Class<? extends ActivityExecutor> executorClass=type.getExecutorClass();
    	ActivityExecutor executor=null;
    	
		try {
			executor = executorClass.newInstance();
			executor.setPlannedActivity(this);
			executor.setShift(shift);
		} catch (InstantiationException e) {
			log.error("Error on instantiating Executor", e);
		} catch (IllegalAccessException e) {
			log.error("Error on instantiating Executor", e);
		}
		return executor;
    	
    }
    
    public void checkPossibleActivities() {
    	if (source != null && source.getCurrentUsage() == this && 
    	        !Jasbro.getInstance().getData().getEventManager().isShiftInProgress()) {
            List<ActivityDetails> possibleActivities = source.getPossibleActivities();
            if (!possibleActivities.contains(getActivityDetails()) && type != ActivityType.EVENT) {
                setActivityDetails(possibleActivities.get(0));
            }
    	}
    }
    
    public void setActivityDetails(ActivityDetails activityDetails) {
        if (activityDetails == null) {
            this.eventId = null;
            this.quest = null;
            type = null;
        }
        else {
            if (!activityDetails.equals(getActivityDetails())) {
                this.eventId = activityDetails.getEventId();
                this.quest = activityDetails.getQuest();
                this.type = activityDetails.getActivityType();
                
                if (source != null) {
                    MyEvent e = new MyEvent(EventType.ACTIVITYCHANGE, this);
                    for (Charakter character : getCharacters()) {
                        character.handleEvent(e);
                    }
                    source.fireEvent(e);
                }
            }
        }
    }
    
    public ActivityDetails getActivityDetails() {
        return new ActivityDetails(type, eventId);
    }
    
    public SelectionData<?> getSelectedOption() {
        List<SelectionData<?>> selectionDataList = getType().getSelectionOptions(this);
        if (selectedOption != null && selectionDataList != null && selectionDataList.contains(selectedOption)) {
            return selectedOption;
        }
        else {
            selectedOption = null;
            return null;
        }
    }
    
    public void setSelectedOption(SelectionData<?> selectedOption) {
        this.selectedOption = selectedOption;
    }


    public String getEventId() {
        return eventId;
    }


    public void setEventId(String eventId) {
        this.eventId = eventId;
    }


    public CustomQuest getQuest() {
        return quest;
    }


    public void setQuest(CustomQuest quest) {
        this.quest = quest;
    }
}
