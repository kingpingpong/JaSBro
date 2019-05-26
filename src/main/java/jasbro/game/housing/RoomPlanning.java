package jasbro.game.housing;

import jasbro.Jasbro;
import jasbro.game.events.rooms.Crypt;
import jasbro.game.events.rooms.Garden;

import java.util.ArrayList;
import java.util.List;

public class RoomPlanning {
	private House house;	
	private List<RoomInfo> newRooms;
	
	public RoomPlanning(House house) {
		this.house = house;
		newRooms = getRoomInfoList(house);
	}
	
	public int getCosts() {
		int costs = 0;
		if (newRooms.size() < house.getRoomAmount()) {
			return 0; //TODO error
		}
		List<RoomInfo> roomTypeList = getRoomInfoList(house);
		
		for (int i = 0; i < newRooms.size(); i++) {
			RoomInfo newRoomType = newRooms.get(i);
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
			RoomInfo newType = newRooms.get(i);
			
			Room newRoom = null;
			if("GARDEN".equals(newType.getId()) || "BIGGARDEN".equals(newType.getId())) {
				newRoom = new Garden(newType);
			} else if ("CRYPT".equals(newType.getId())){
				newRoom = new Crypt(newType);
			}
			else {
				newRoom = new ConfigurableRoom(newType);
			}
			
			
			if (i >= house.getRooms().size()) {
				house.getRooms().add(newRoom);
			}
			else {
				if (!newType.getId().equals((house.getRooms().get(i).getRoomInfo().getId()))) {
				    RoomSlot roomSlot = house.getRoomSlots().get(i);
				    roomSlot.getRoom().empty();
					newRoom.setHouse(house);
					roomSlot.setRoom(newRoom);
					roomSlot.setDownTime(roomSlot.getSlotType().getDownTime());
				}
			}
		}
	}
	
	public List<RoomInfo> getNewRooms() {
		return newRooms;
	}
	
	public void reset() {
		newRooms = getRoomInfoList(house);
	}
	
	public List<RoomInfo> getRoomInfoList(final House house) {
		List<RoomInfo> roomInfoList = new ArrayList<>();
		for (final Room room : house.getRooms()) {
			roomInfoList.add(room.getRoomInfo());
		}
		return roomInfoList;
	}
}