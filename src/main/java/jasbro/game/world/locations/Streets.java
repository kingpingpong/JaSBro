package jasbro.game.world.locations;

import jasbro.Jasbro;
import jasbro.Util;
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

public class Streets extends OtherLocation {
	public Streets() {
		Map<Time, PlannedActivity> roomUsageMap = getUsageMap();
		for (Time time : Time.values()) {
			roomUsageMap.put(time, new PlannedActivity(this, ActivityType.WHORESTREETS));
		}
	}
	
	@Override
	public ImageData getImage() {
		if (Jasbro.getInstance().getData().getTime() == Time.MORNING) {
			return new ImageData("images/backgrounds/streets_morning.jpg");
		}
		else if (Jasbro.getInstance().getData().getTime() == Time.AFTERNOON) {
			return new ImageData("images/backgrounds/streets_afternoon.jpg");
		}
		else {
			return new ImageData("images/backgrounds/streets_night.jpg");
		}
	}
	
	@Override
	public List<ActivityDetails> getPossibleActivities(Time time, TypeAmounts typeAmounts) {
		List<ActivityDetails> possibleActivities = new ArrayList<ActivityDetails>();
		if (Util.getTrainers(getCurrentUsage().getCharacters()).size() == 0) {
			possibleActivities.add(new ActivityDetails(ActivityType.WHORESTREETS));
		}
		possibleActivities.add(new ActivityDetails(ActivityType.ADVERTISE));
		possibleActivities.add(new ActivityDetails(ActivityType.WALK));
		possibleActivities.add(new ActivityDetails(ActivityType.ROB));
		
		if (possibleActivities.size() == 0) {
			possibleActivities.add(new ActivityDetails(ActivityType.IDLE));
		}
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
		return LocationType.STREETS;
	}
}