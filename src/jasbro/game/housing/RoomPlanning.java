package jasbro.game.housing;

import jasbro.Jasbro;

import java.util.ArrayList;
import java.util.List;

public class RoomPlanning {
	private House house;	
	private List<RoomType> newRooms;
	
	public RoomPlanning(House house) {
		this.house = house;
		newRooms = getRoomTypeList(house);
	}
	
	public int getCosts() {
		int costs = 0;
		if (newRooms.size() < house.getRoomAmount()) {
			return 0; //TODO error
		}
		List<RoomType> roomTypeList = getRoomTypeList(house);

		for (int i = 0; i < newRooms.size(); i++) {
			RoomType newRoomType = newRooms.get(i);
			if (i >= roomTypeList.size()) {
				costs += 500;
				costs += newRoomType.getCost();
			}
			else {
				if (newRoomType != roomTypeList.get(i)) {
					costs += newRoomType.getCost();
				}
			}
		}
		return costs;
	}
	
	public void adoptRoomLayout() {
		if (newRooms.size() < house.getRoomAmount()) {
			return; //TODO error
		}
		
		int cost = getCosts();
		Jasbro.getInstance().getData().spendMoney(cost, "Rooms");
		
		for (int i = 0; i < newRooms.size(); i++) {
			RoomType newType = newRooms.get(i);
			if (i >= house.getRooms().size()) {
				house.getRooms().add(newType.getRoom());
			}
			else {
				if (!newType.isOfType(house.getRooms().get(i))) {
				    RoomSlot roomSlot = house.getRoomSlots().get(i);
				    roomSlot.getRoom().empty();
					Room room = newType.getRoom();
					room.setHouse(house);
					roomSlot.setRoom(room);
					roomSlot.setDownTime(roomSlot.getSlotType().getDownTime());
				}
			}
		}
	}
	
	public List<RoomType> getNewRooms() {
		return newRooms;
	}

	public void reset() {
		newRooms = getRoomTypeList(house);
	}
	
	public List<RoomType> getRoomTypeList(House house) {
		List<RoomType> roomTypeList = new ArrayList<RoomType>();
		for (Room room : house.getRooms()) {
			for (RoomType type : RoomType.values()) {
				if (type.isOfType(room)) {
					roomTypeList.add(type);
					break;
				}
			}
		}
		return roomTypeList;
	}
}
