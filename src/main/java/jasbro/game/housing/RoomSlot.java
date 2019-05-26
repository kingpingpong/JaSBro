package jasbro.game.housing;

import jasbro.Jasbro;
import jasbro.Util.TypeAmounts;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.interfaces.MyEventListener;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomSlot extends CharacterLocation {
	private RoomSlotType slotType;
	private Room room;
	private int downTime = 0;
	
	public RoomSlot(RoomSlotType slotType, House house) {
		this(slotType, "EMPTYROOM", house);
	}
	public RoomSlot(RoomSlotType slotType, String roomInfoId, House house) {
		this.slotType = slotType;
		this.room = RoomInfoUtil.newRoom(roomInfoId);
		this.room.setHouse(house);
	}
	
	public RoomSlotType getSlotType() {
		return slotType;
	}
	public void setSlotType(RoomSlotType slotType) {
		this.slotType = slotType;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public boolean isAvailable() {
		return downTime <= 0;
	}
	public int getDownTime() {
		return downTime;
	}
	public void setDownTime(int downTime) {
		this.downTime = downTime;
	}
	
	@Override
	public void handleEvent(MyEvent e) {
		if (e.getType() == EventType.NEXTDAY && downTime > 0) {
			downTime--;
			if(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.BENEFACTORCARPENTERS) && downTime>0)
				downTime--;
		}
		room.handleEvent(e);
	}
	
	@Override
	public List<ActivityDetails> getPossibleActivities(Time time, TypeAmounts typeAmounts) {
		if (isAvailable()) {
			return room.getPossibleActivities(time, typeAmounts);
		}
		else {
			List<ActivityDetails> activityDetails = new ArrayList<ActivityDetails>();
			activityDetails.add(new ActivityDetails(ActivityType.IDLE));
			return activityDetails;
		}
	}
	
	@Override
	public List<ActivityDetails> getPossibleActivitiesChildCare(Time time, TypeAmounts typeAmounts) {
		if (isAvailable()) {
			return room.getPossibleActivitiesChildCare(time, typeAmounts);
		}
		else {
			List<ActivityDetails> activityDetails = new ArrayList<ActivityDetails>();
			activityDetails.add(new ActivityDetails(ActivityType.IDLE));
			return activityDetails;
		}
	}
	
	@Override
	public String getName() {
		return slotType.getText() + ": " + room.getName();
	}
	@Override
	public String getDescription() {
		return room.getDescription();
	}
	@Override
	public LocationTypeInterface getLocationType() {
		return room.getLocationType();
	}
	@Override
	public ImageData getImage() {
		if (!isAvailable()) {
			return new ImageData("images/backgrounds/under-construction.png");
		}
		return room.getImage();
	}
	@Override
	public PlannedActivity getCurrentUsage() {
		return room.getCurrentUsage();
	}
	@Override
	public Map<Time, PlannedActivity> getUsageMap() {
		return room.getUsageMap();
	}
	
	@Override
	public int getMaxPeople() {
		if (isAvailable()) {
			return room.getMaxPeople();
		}
		else {
			return 0;
		}
	}
	
	@Override
	public void empty() {
		room.empty();
	}
	
	@Override
	public void addListener(MyEventListener listener) {
		room.addListener(listener);
	}
}