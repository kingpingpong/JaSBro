package jasbro.game.housing;

import jasbro.Jasbro;
import jasbro.game.events.rooms.Crypt;
import jasbro.game.events.rooms.Garden;

import java.util.ArrayList;
import java.util.List;

public class RoomPlanning {
	private House house;	
	private List<RoomInfo> newRooms;
	private List<RoomSlotType> newRoomSlots;
	
	public RoomPlanning(House house) {
		this.house = house;
		newRooms = getRoomInfoList(house);
		newRoomSlots = getRoomTypeList(house);
	}
	
	public int getCosts() {
		int costs = 0;
		if (newRooms.size() < house.getRoomAmount()) {
			return 0; //TODO error
		}
		List<RoomInfo> roomTypeList = getRoomInfoList(house);
		List<RoomSlotType> roomSlotList = getRoomTypeList(house);
		
		for (int i = 0; i < newRooms.size(); i++) {
			RoomInfo newRoomType = newRooms.get(i);
			if (i >= roomTypeList.size()) { // TODO Figure out why this is here C-L
				costs += 500;
				costs += newRoomType.getCost();
			}
			else {
				if (newRoomType != roomTypeList.get(i)) {
					costs += newRoomType.getCost();
				}
			}
		}

		for (int i = 0; i < newRoomSlots.size(); i++) {
			RoomSlotType newSlotType = newRoomSlots.get(i);
			if (newSlotType != roomSlotList.get(i)) {
				if (newRooms.get(i) != roomTypeList.get(i) &&
					!newRooms.get(i).fitsInSlot(newSlotType)) {
					costs -= newRooms.get(i).getCost(); // Basically refunding for the new room since it won't get built
														// The GUI will need to display a warning for this
				}
				costs += newSlotType.getConCost();
				costs += roomSlotList.get(i).getConCost() / 2; // This is a demolishing cost.
			}
		}
		return costs;
	}
	
	public void adoptRoomLayout() {
		if (newRooms.size() < house.getRoomAmount()) {
			return; //TODO error
		}
		
		int cost = getCosts();
		Jasbro.getInstance().getData().spendMoney(cost, "Remodeling");

		List<RoomSlotType> roomSlotTypes = getRoomTypeList(house);

		for (int i = 0; i < newRooms.size(); i++) {
			RoomInfo newType = newRooms.get(i);
			RoomSlotType newSlotType = newRoomSlots.get(i);
			boolean diffSlotType = newSlotType != roomSlotTypes.get(i);

			Room newRoom = null;
			if (diffSlotType && !newType.fitsInSlot(newSlotType)) { // Just in case a room is cheated in; we don't want to replace it.
				if ((RoomInfoUtil.getRoomInfo("EMPTYROOM").fitsInSlot(newSlotType))) {
					newRoom = new ConfigurableRoom(RoomInfoUtil.getRoomInfo("EMPTYROOM"));
				} else {
					newRoom = new ConfigurableRoom(RoomInfoUtil.getRoomInfo("EMPTYSPACE"));
				}
			} else if("GARDEN".equals(newType.getId()) || "BIGGARDEN".equals(newType.getId())) {
				newRoom = new Garden(newType);
			} else if ("CRYPT".equals(newType.getId())){
				newRoom = new Crypt(newType);
			}
			else {
				newRoom = new ConfigurableRoom(newType);
			}
			
			
			if (i >= house.getRooms().size()) { // TODO Figure out why this is here C-L
				house.getRooms().add(newRoom);
			}
			else {
				RoomSlot roomSlot = house.getRoomSlots().get(i);
				if (diffSlotType) {
					RoomSlotType oldType = roomSlot.getSlotType();
					roomSlot.setSlotType(newSlotType);
					roomSlot.getRoom().empty();
					roomSlot.setDownTime(roomSlot.getSlotType().getDownTime() + oldType.getDownTime() / 2); // TODO Consider rebalancing (The oldType's down type is for demo reasons.)
				}
				if (!newRoom.getRoomInfo().getId().equals((house.getRooms().get(i).getRoomInfo().getId()))) {
				    roomSlot.getRoom().empty();
					newRoom.setHouse(house);
					roomSlot.setRoom(newRoom);

					if (roomSlot.getDownTime() == 0) // If the room is already being rebuilt, consider this a bonus to time.
						roomSlot.setDownTime(roomSlot.getSlotType().getDownTime());
				}
			}
		}
	}
	
	public List<RoomInfo> getNewRooms() {
		return newRooms;
	}

	public List<RoomSlotType> getNewRoomSlots() {
		return newRoomSlots;
	}

	public void reset() {
		newRooms = getRoomInfoList(house);
		newRoomSlots = getRoomTypeList(house);
	}

	public List<RoomSlotType> getRoomTypeList(final House house) {
		List<RoomSlotType> roomSlotTypeList = new ArrayList<>();
		for (final RoomSlot slot : house.getRoomSlots()) {
			roomSlotTypeList.add(slot.getSlotType());
		}
		return roomSlotTypeList;
	}

	public List<RoomInfo> getRoomInfoList(final House house) {
		List<RoomInfo> roomInfoList = new ArrayList<>();
		for (final Room room : house.getRooms()) {
			roomInfoList.add(room.getRoomInfo());
		}
		return roomInfoList;
	}
}