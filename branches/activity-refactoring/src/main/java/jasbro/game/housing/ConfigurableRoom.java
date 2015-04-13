package jasbro.game.housing;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.List;

/**
 * Room with configurable activities.
 * 
 * @author somextra
 *
 */
public class ConfigurableRoom extends Room {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new room with the given RoomType.
	 * 
	 * @param roomType
	 */
	public ConfigurableRoom(final RoomType roomType) {
		super(roomType);
	}

	public ConfigurableRoom(final RoomType roomType, final House house) {
		this(roomType);
		setHouse(house);
		
	}

	@Override
	public List<ActivityDetails> getPossibleActivities(Time time, TypeAmounts typeAmounts) {
		List<ActivityDetails> valid = new ArrayList<ActivityDetails>();
		for (ActivityType activity : getRoomInfo().getActivities()) {
			if (getRoomInfo().isActivityValid(activity, getUsage(time).getCharacters(), typeAmounts)) {
				valid.add(new ActivityDetails(activity));
			}
		}
		return valid;
	}

	@Override
	public List<ActivityDetails> getPossibleActivitiesChildCare(Time time, TypeAmounts typeAmounts) {
		List<ActivityDetails> valid = new ArrayList<ActivityDetails>();
		for (ActivityType activity : getRoomInfo().getChildCareActivities()) {
			if (getRoomInfo().isChildCareActivityValid(activity, getCurrentUsage().getCharacters(), typeAmounts)) {
				valid.add(new ActivityDetails(activity));
			}
		}
		return valid;
	}

	@Override
	public ImageData getImage() {
		return getRoomInfo().getImage();
	}

	@Override
	public void handleEvent(MyEvent e) {
		if (getRoomInfo().hasEventHandler()) {
		    getRoomInfo().getEventHandler().handleEvent(e);
		}
		super.handleEvent(e);
	}

	// TODO This was taken out of SickRoom, figure out a way to put it back
	// while fitting in with the new stuff
	/*
	 * @Override public float getMoneyModifier(float currentModifier, Charakter
	 * character) { return currentModifier + 0.2f; }
	 */
	
	private RoomInfo getRoomInfo() {
	    return getRoomType().getRoomInfo();
	}
}
