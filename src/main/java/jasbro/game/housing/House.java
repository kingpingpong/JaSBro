/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jasbro.game.housing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameObject;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.BuildingAdvertising;
import jasbro.game.events.business.BuildingMercSecurity;
import jasbro.game.events.business.Fame;
import jasbro.game.events.business.SpawnData;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;

/**
 *
 * @author Azrael
 */
@SuppressWarnings("serial")
public class House extends GameObject implements AreaInterface {
	private HouseType houseType;
	private List<RoomSlot> roomSlots = new ArrayList<RoomSlot>();
	private int value;
	private String name;
	private Fame fame = new Fame();
	private int dirt = 0;
	private int security = 100;
	private boolean mercs = false;
	private SpawnData spawnData;
	private Map<Time, ImageData> images;
	private BuildingAdvertising advertising;
	private BuildingMercSecurity mercenaries;
	
	public House(HouseType houseType, int value) {
		this.houseType = houseType;
		this.value = value;
	}
	
	public House(final HouseType type, final int value, final String morningImage, final String afternoonImage,
			final String nightImage, final RoomSlotType[] slots, final String[] roomInfoIds) {
		this(type, value);
		images = new HashMap<>();
		images.put(Time.MORNING, new ImageData(morningImage));
		images.put(Time.AFTERNOON, new ImageData(afternoonImage));
		images.put(Time.NIGHT, new ImageData(nightImage));
		for(int i = 0; i < slots.length; ++i) {
			roomSlots.add(new RoomSlot(slots[i], roomInfoIds[i], this));
		}
	}
	
	public int getRoomAmount() {
		return roomSlots.size();
	}
	
	public void initImages() {
	}
	
	public int getAmountPeople() {
		int amount = 0;
		for (Room room : getRooms()) {
			if (room.getCurrentUsage() != null) {
				amount += room.getCurrentUsage().getCharacters().size();
			}
		}
		return amount;
	}
	
	public List<RoomSlot> getRoomSlots() {
		return roomSlots;
	}
	
	public List<Room> getRooms() {
		List<Room> rooms = new ArrayList<Room>();
		for (RoomSlot roomSlot : getRoomSlots()) {
			rooms.add(roomSlot.getRoom());
		}
		return rooms;
	}
	
	public String getInternName() {
		return name;
	}
	
	public String getName() {
		if (name == null || name.trim().equals("")) {
			return houseType.getText();
		}
		return name.trim() + " (" + houseType.getText() + ")";
	}
	
	public int getValue() {
		return value;
	}
	
	public ImageData getImage() {
		return getImages().get(Jasbro.getInstance().getData().getTime());
	}
	
	public void setInternName(String name) {
		this.name = name;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public Map<Time, ImageData> getImages() {
		if (images == null) {
			images = new HashMap<Time, ImageData>();
			initImages();
		}
		return images;
	}
	
	public void setImages(HashMap<Time, ImageData> images) {
		this.images = images;
	}
	
	public void empty() {
		for (Room room : getRooms()) {
			room.empty();
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public Fame getFame() {
		if (this.fame == null) {
			this.fame = new Fame();
		}
		return fame;
	}
	
	public void setFame(Fame fame) {
		this.fame = fame;
	}
	
	public int getDirt() {
		return dirt;
	}
	
	public void modDirt(int mod) {
		dirt += mod;
	}
	
	public void applyDirtLimit() {
		if (dirt < 0) {
			dirt = 0;
		}
	}	
	
	public void applySecurityLimit() {
		if (security < 0) {
			security = 0;
		}
		if (security > 100) {
			security = 100;
		}
	}
	
	public int getSellPrice() {
		return (int) Util.getPercent(getValue(), 75);
	}
	
	public CleanState getCleanState() {
		return CleanState.calcState(this);
	}
	public int getSecurity() {
		return security;
	}
	public void setSecurity(int security) {
		this.security = security;
	}
	public void modSecurity(int security) {
		this.security += security;
	}
	public boolean hasMercenaries() {
		return this.mercs;
	}
	public void setMercenaries(boolean mercenaries) {
		this.mercs = mercenaries;
	}
	

	public SecurityState getSecurityState() {
		return SecurityState.calcState(this);
	}
	
	@Override
	public int getLocationAmount() {
		return roomSlots.size();
	}
	
	@Override
	public List<? extends CharacterLocation> getLocations() {
		return getRoomSlots();
	}
	
	public HouseType getHouseType() {
		return houseType;
	}
	
	public SpawnData getSpawnData() {
		if (spawnData == null) {
			for (House house : Jasbro.getInstance().getData().getHouses()) {
				if (house != this && house.spawnData != null) {
					spawnData = new SpawnData(house.spawnData);
				}
			}
		}
		if (spawnData == null) {
			spawnData = new SpawnData();
		}
		return spawnData;
	}
	
	public BuildingAdvertising getAdvertising() {
		if (advertising == null) {
			advertising = new BuildingAdvertising();
		}
		return advertising;
	}
	public BuildingMercSecurity getMercSecurity() {
		if (mercenaries == null) {
			mercenaries = new BuildingMercSecurity();
		}
		return mercenaries;
	}
	
	@Override
	public void handleEvent(MyEvent e) {
		if (e.getType() == EventType.NEXTDAY || e.getType() == EventType.NEXTSHIFT) {
			for (RoomSlot roomSlot : getRoomSlots()) {
				roomSlot.handleEvent(e);
			}
		}
		
		if (e.getType() == EventType.NEXTDAY) {
			int pay = 0;
			pay += getValue() / 100;
			
			if (Jasbro.getInstance().getData().getDay() % 30 == 0) {
				for (Room room : getRooms()) {
					if (room != null) {
						pay += room.getRoomInfo().getCost() / 10;
					}
				}
			}
			Jasbro.getInstance().getData().spendMoney(pay, this); // general expenses
		}
		
		super.handleEvent(e);
		getSpawnData().handleEvent(e);
	}
}