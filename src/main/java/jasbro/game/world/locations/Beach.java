package jasbro.game.world.locations;

import jasbro.Jasbro;
import jasbro.Util.TypeAmounts;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Beach extends OtherLocation {
	private transient HashMap<Time, ImageData> images;
	
	public Beach() {
		Map<Time, PlannedActivity> roomUsageMap = getUsageMap();
		for (Time time : Time.values()) {
			roomUsageMap.put(time, new PlannedActivity(this, ActivityType.SUNBATHE));
		}
	}
	
	@Override
	public ImageData getImage() {
		return getImages().get(Jasbro.getInstance().getData().getTime());
	}
	
	@Override
	public List<ActivityDetails> getPossibleActivities(Time time, TypeAmounts typeAmounts) {
		List<ActivityDetails> possibleActivities = new ArrayList<ActivityDetails>();
		possibleActivities.add(new ActivityDetails(ActivityType.SWIM));
		possibleActivities.add(new ActivityDetails(ActivityType.WALK));
		if (time != Time.NIGHT) {
			possibleActivities.add(new ActivityDetails(ActivityType.SUNBATHE));
			possibleActivities.add(new ActivityDetails(ActivityType.ADVERTISE));
		}
		else {
			possibleActivities.add(new ActivityDetails(ActivityType.CAMP));
		}
		return possibleActivities;
	}
	
	@Override
	public List<ActivityDetails> getPossibleActivitiesChildCare(Time time, TypeAmounts typeAmounts) {
		List<ActivityDetails> activities = new ArrayList<ActivityDetails>();
		if (time != Time.NIGHT && typeAmounts.getInfantAmount() == 0 && 
				(typeAmounts.getChildAmount() == 0 || typeAmounts.isAdultPresent())) {
			activities.add(new ActivityDetails(ActivityType.PLAY));
		}
		return activities;
	}
	
	@Override
	public String getName() {
		return getType().getText();
	}
	
	@Override
	public String getDescription() {
		return getType().getDescription();
	}
	
	private HashMap<Time, ImageData> getImages() {
		if (images == null) {
			images = new HashMap<Time, ImageData>();
			
			images.put(Time.MORNING, new ImageData("images/backgrounds/beach_morning.jpg"));
			images.put(Time.AFTERNOON, new ImageData("images/backgrounds/beach_afternoon.jpg"));
			images.put(Time.NIGHT, new ImageData("images/backgrounds/beach_night.jpg"));
		}
		return images;
	}
	
	@Override
	public LocationTypeInterface getLocationType() {
		return getType();
	}
	
	public LocationType getType() {
		return LocationType.BEACH;
	}
	
	
}