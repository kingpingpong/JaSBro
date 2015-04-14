package jasbro.game.world.locations;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Restaurant extends OtherLocation {
    
	public Restaurant() {
    	Map<Time, PlannedActivity> roomUsageMap = getUsageMap();
        for (Time time : Time.values()) {
            roomUsageMap.put(time, new PlannedActivity(this, ActivityType.EAT));
        }
	}
	
	@Override
	public ImageData getImage() {
		return getType().getImage();
	}

	@Override
	public List<ActivityDetails> getPossibleActivities(Time time, TypeAmounts typeAmounts) {
		List<ActivityDetails> possibleActivities = new ArrayList<ActivityDetails>();
		possibleActivities.add(new ActivityDetails(ActivityType.EAT));
		return possibleActivities;
	}
	
	@Override
	public List<ActivityDetails> getPossibleActivitiesChildCare(Time time, TypeAmounts typeAmounts) {
	    List<ActivityDetails> possibleActivities = new ArrayList<ActivityDetails>();
        possibleActivities.add(new ActivityDetails(ActivityType.EAT));
        return possibleActivities;
	}
	

    @Override
    public String getName() {
        return getType().getText();
    }

    @Override
    public String getDescription() {
        return getType().getDescription();
    }
	
    @Override
    public LocationTypeInterface getLocationType() {
        return getType();
    }
    
    public LocationType getType() {
        return LocationType.RESTAURANT;
    }
}
