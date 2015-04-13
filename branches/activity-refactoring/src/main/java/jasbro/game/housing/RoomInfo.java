package jasbro.game.housing;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.events.rooms.RoomEventHandler;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Information for each room type, for a flywheel-like pattern. Each room will
 * have a reference to a RoomInfo, much like the RoomTypes.
 * 
 * The only mutable field is activities.
 * 
 * The goal is to have this be the information that's consistent between rooms,
 * while Room handles the individual instances.
 * 
 * @author somextra
 */
public class RoomInfo {
	private final int maxOccupancy;
	private final int cost;
	private final String id;
	private final ImageData image;

	private RoomEventHandler eventHandler;
	private Map<ActivityType, ActivityRequirement> activityRequirements;
	private List<ActivityType> activities;
	private Map<ActivityType, ActivityRequirement> childCareActivityRequirements;

	public RoomInfo(final int maxOccupancy, final int cost, final String id, final String imageLocation) {
		this.maxOccupancy = maxOccupancy;
		this.cost = cost;
		this.id = id;
		this.image = new ImageData(imageLocation);
		this.activityRequirements = new HashMap<ActivityType, ActivityRequirement>();
		this.activities = new ArrayList<ActivityType>();
		this.childCareActivityRequirements = new HashMap<ActivityType, ActivityRequirement>();
	}

	/**
	 * Adds an activity to the room. This will overwrite an existing activity if
	 * there is one.
	 * 
	 * @param activity
	 *            The ActivityType being added.
	 * @param requirement
	 *            The requirement on the activity.
	 */
	public void addActivity(final ActivityType activity, final ActivityRequirement requirement) {
		activityRequirements.put(activity, requirement);
		if (!activities.contains(activity)) {
			activities.add(activity);
		}
	}

	/**
	 * Adds an child care activity to the room. This will overwrite an existing
	 * activity if there is one.
	 * 
	 * @param activity
	 *            The ActivityType being added.
	 * @param requirement
	 *            The requirement on the activity.
	 */
	public void addChildCareActivity(final ActivityType activity, final ActivityRequirement requirement) {
		childCareActivityRequirements.put(activity, requirement);
	}

	public void setEventHandler(final RoomEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public int getCost() {
		return cost;
	}

	public String getId() {
		return id;
	}

	public ImageData getImage() {
		return image;
	}

	public List<ActivityType> getActivities() {
		return activities;
	}

	public Collection<ActivityType> getChildCareActivities() {
		return childCareActivityRequirements.keySet();
	}

	public boolean isActivityValid(final ActivityType activity, final List<Charakter> characters,
			TypeAmounts typeAmounts) {
		return activityRequirements.get(activity).isValid(activity, characters, typeAmounts);
	}

	public boolean isChildCareActivityValid(final ActivityType activity, final List<Charakter> characters,
			TypeAmounts typeAmounts) {
		return childCareActivityRequirements.get(activity).isValid(activity, characters, typeAmounts);
	}

	public boolean hasEventHandler() {
		return eventHandler != null;
	}

	public RoomEventHandler getEventHandler() {
		return eventHandler;
	}
}
