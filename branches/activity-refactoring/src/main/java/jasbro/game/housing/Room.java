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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Azrael
 */
public abstract class Room extends CharacterLocation {
    private Map<Time, PlannedActivity> usageMap = new HashMap<Time, PlannedActivity>();
    private RoomType roomType;
    private House house;
    private String name;
    
    private Room() {
    	Map<Time, PlannedActivity> roomUsageMap = getUsageMap();
        for (Time time : Time.values()) {
            roomUsageMap.put(time, new PlannedActivity(this, ActivityType.SLEEP));
        }
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
    	return roomType.getMaxPeople();
    }
    
    public Room(RoomType roomType) {
    	this();
    	this.roomType = roomType;    	
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
    		return roomType.getText();
    	}
        return name.trim() + " (" + roomType.getText() + ")";
    }
    
    @Override
    final public String getDescription() {
    	return roomType.getDescription();
    }
    
    public RoomType getRoomType() {
    	if (roomType == null) {
			for (RoomType type : RoomType.values()) {
				if (type.isOfType(this)) {
					roomType = type;
					break;
				}
			}
		}
    	return roomType;
    }
    
    @Override
    public String toString() {
    	return getRoomType().getText();
    }
    
    public String getInternName() {
    	return name;
    }

	public void setInternName(String name) {
		this.name = name;
	}
    
    @Override
    public LocationTypeInterface getLocationType() {
        return getRoomType();
    }
    
    public PlannedActivity getCurrentUsage() {
        return getUsageMap().get(Jasbro.getInstance().getData().getTime());
    }
    
    public Map<Time, PlannedActivity> getUsageMap() {
        return usageMap;
    }
}
