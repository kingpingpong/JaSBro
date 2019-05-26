package jasbro.game.housing;

import jasbro.game.events.rooms.Garden;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.interfaces.UnlockObject;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum RoomType implements UnlockObject, LocationTypeInterface {
	EMPTYROOM(6, 10, RoomSlotType.SMALLROOM, RoomSlotType.LARGEROOM, RoomSlotType.UNDERGROUND), 
	SMALLBEDROOM(2, 100), 
	BEDROOM(4, 500), 
	LARGEBEDROOM(6, 2000, RoomSlotType.LARGEROOM), 
	MASTERBEDROOM(2, 5000),
	SICKROOM(4, 4000), 
	ORGYROOM(10, 12000, RoomSlotType.LARGEROOM),
	KITCHEN(1,650), 
	ARENA(2, 6000, RoomSlotType.UNDERGROUND), 
	BAR(2, 1800), 
	STAGE(1, 2500, RoomSlotType.LARGEROOM, RoomSlotType.UNDERGROUND),
	DUNGEON(2, 4500, RoomSlotType.UNDERGROUND), 
	GLASSROOM(6, 4000, RoomSlotType.LARGEROOM),
	GLORYHOLE(4, 6500),
	CLASSROOM(6, 2500, RoomSlotType.LARGEROOM), 
	CABARET(6, 6000, RoomSlotType.LARGEROOM),
	SPAAREA(6, 4600, RoomSlotType.OUTDOOR, RoomSlotType.UNDERGROUND),
	SMALLLIBRARY(6, 3500, RoomSlotType.LARGEROOM),
	POND(6,5000, RoomSlotType.OUTDOOR),
	GARDEN(6,5000, RoomSlotType.OUTDOOR),
	GYM(6,5000, RoomSlotType.LARGEROOM),
	ALTAR(4,5000, RoomSlotType.OUTDOOR),
	BIGGARDEN(10,5000, RoomSlotType.OUTDOOR),
	TRAININGGROUNDS(8,5000, RoomSlotType.OUTDOOR),
	THRONEROOM(1,100000, RoomSlotType.LARGEROOM),
	CRYPT(6,0, RoomSlotType.UNDERGROUND),
	LOBBY(4, 8400, RoomSlotType.LARGEROOM);
	
    @SuppressWarnings("unused")
    private final static Logger log = LogManager.getLogger(RoomType.class);
    private int cost = 100;
    private int maxPeople;
    private boolean locked = false;
    private Set<RoomSlotType> slotTypes;

    private RoomType(int maxPeople, int cost) {
        this.cost = cost;
        this.maxPeople = maxPeople;
        slotTypes = new HashSet<RoomSlotType>();
        slotTypes.add(RoomSlotType.SMALLROOM);
        slotTypes.add(RoomSlotType.LARGEROOM);
    }
    
    private RoomType(int maxPeople, int cost, RoomSlotType... slotTypesTmp) {
        this.cost = cost;
        this.maxPeople = maxPeople;
        slotTypes = new HashSet<RoomSlotType>();
        for (RoomSlotType slotType : slotTypesTmp) {
            slotTypes.add(slotType);
            if (slotType == RoomSlotType.SMALLROOM) {
                slotTypes.add(RoomSlotType.LARGEROOM);
            }
        }
    }

    public String getText() {
        return TextUtil.t(this.toString());
    }

    public String getDescription() {
        String text = TextUtil.tNoCheck(this.toString() + ".description");
        if (!(this.toString() + ".description").equals(text)) {
            return text;
        } else {
            return null;
        }
    }

    public int getCost() {
        return cost;
    }

    public Room getRoom() {
        if (this == GARDEN || this == BIGGARDEN) {
            return new Garden(this.getRoomInfo());
        }
        return new ConfigurableRoom(this.getRoomInfo());
    }

    public boolean isOfType(Room room) {
        return room.getRoomInfo().getId().equals(this.toString());
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public boolean isLocked() {
        return locked;
    }
    
    public RoomInfo getRoomInfo() {
        return RoomInfoUtil.getRoomInfo(this.toString());
    }

    @Override
    public ImageData getImage() {
        return getRoomInfo().getImage();
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Set<RoomSlotType> getSlotTypes() {
        return slotTypes;
    }
    
    public boolean fitsInSlot(RoomSlotType roomSlotType) {
        return getSlotTypes().contains(roomSlotType);
    }

    @Override
    public String toString() {
        return String.valueOf(this);
    }

    @Override
    public boolean isValidLocation(LocationTypeInterface location) {
        return location == this;
    }
}
