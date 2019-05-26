/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jasbro.game.housing;

import jasbro.Jasbro;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;

import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Azrael
 */
public abstract class Room extends CharacterLocation {
	private Map<Time, PlannedActivity> usageMap = new EnumMap<>(Time.class);
	private RoomInfo roomInfo;
	private House house;
	private String name;
	
	private Room() {
		Map<Time, PlannedActivity> roomUsageMap = getUsageMap();
		for (Time time : Time.values()) {
			roomUsageMap.put(time, new PlannedActivity(this, ActivityType.SLEEP));
		}
	}
	
	public Room(final RoomInfo roomInfo) {
		this();
		this.roomInfo = roomInfo;
	}
	
	@Override
	public int getMaxPeople() {
		for (RoomSlot roomSlot : house.getRoomSlots()) {
			if (roomSlot.getRoom() == this) {
				if (!roomSlot.isAvailable()) {
					return 0;
				}
				break;
			}
		}
		return roomInfo.getMaxOccupancy();
	}
	
	public House getHouse() {
		return house;
	}
	
	public void setHouse(House house) {
		this.house = house;
	}
	
	@Override
	public String getName() {
		if (name == null || name.trim().equals("")) {
			return roomInfo.getText();
		}
		return name.trim() + " (" + roomInfo.getText() + ")";
	}
	
	@Override
	final public String getDescription() {
		return roomInfo.getDescription();
	}
	
	public RoomInfo getRoomInfo() {
		return this.roomInfo;
	}
	
	@Override
	public String toString() {
		return roomInfo.getText();
	}
	
	public String getInternName() {
		return name;
	}
	
	public void setInternName(String name) {
		this.name = name;
	}
	
	@Override
	public LocationTypeInterface getLocationType() {
		return new RoomLocationType(roomInfo.getId());
	}
	
	public PlannedActivity getCurrentUsage() {
		return getUsageMap().get(Jasbro.getInstance().getData().getTime());
	}
	
	public Map<Time, PlannedActivity> getUsageMap() {
		return usageMap;
	}

	public RoomType getRoomType() {
		for (RoomType rt: RoomType.values()) {
			if (rt.isOfType(this)) return rt;
		}
		return null;
	}
}