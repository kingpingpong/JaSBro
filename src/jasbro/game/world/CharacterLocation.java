package jasbro.game.world;

import jasbro.Jasbro;
import jasbro.Util.TypeAmounts;
import jasbro.game.GameObject;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CharacterLocation extends GameObject {   
    public abstract List<ActivityDetails> getPossibleActivities(Time time, TypeAmounts typeAmounts);
    public abstract String getName();
    public abstract String getDescription();
    public abstract LocationTypeInterface getLocationType();
    public abstract PlannedActivity getCurrentUsage();
    public abstract Map<Time, PlannedActivity> getUsageMap();
    
    public abstract ImageData getImage();
    
    public final List<ActivityDetails> getPossibleActivities() {
        return getPossibleActivities(Jasbro.getInstance().getData().getTime());
    }
    
    public final List<ActivityDetails> getPossibleActivities(Time time) {
        TypeAmounts typeAmounts = jasbro.Util.getTypeAmounts(getCurrentUsage().getCharacters());
        
        List<ActivityDetails> activities;
        if (!typeAmounts.isChildPresent()) {
            activities = getPossibleActivities(time, typeAmounts);
        }
        else {
            activities = getPossibleActivitiesChildCare(time, typeAmounts);
        }
        Jasbro.getInstance().getData().getQuestManager().modifyActivities(activities, time, 
                getCurrentUsage().getCharacters(), typeAmounts, this);
        if (activities.size() == 0) {
            activities.add(new ActivityDetails(ActivityType.IDLE));
        }
        return activities;
    }
    
    public List<ActivityDetails> getPossibleActivitiesChildCare(Time time, TypeAmounts typeAmounts) {
        return new ArrayList<ActivityDetails>();
    }
    
    public PlannedActivity getUsage(Time time) {
        return getUsageMap().get(time);
    }
    
    public boolean isFull() {
        return getCurrentUsage().getCharacters().size() >= getMaxPeople();
    }

	public int getMaxPeople() {
		return 6;
	}
    
    public void empty() {
        for (Time time : Time.values()) {
        	PlannedActivity activity = getUsageMap().get(time);
        	activity.removeAllCharacters();
        }
    }
    
    public int getAmountPeople() {
        return getCurrentUsage().getCharacters().size();
    }    
    
    public ActivityType getSelectedActivity() {
        return getCurrentUsage().getType();
    }
    
    public ActivityDetails getSelectedActivityDetails() {
        return getCurrentUsage().getActivityDetails();
    }
    
    public void setSelectedActivityDetails(ActivityDetails selectedActivity) {
        getCurrentUsage().setActivityDetails(selectedActivity);
    }
    
    @Override
    public void handleEvent(MyEvent e) {
    	if (e.getType() == EventType.ACTIVITYCHANGE) {
            fireEvent(e);
    	}
    }
    
    public float getMoneyModifier(float currentModifier, Charakter character) {
    	return currentModifier;
    }
    
    @Override
    public String toString() {
    	return getName();
    }
}
