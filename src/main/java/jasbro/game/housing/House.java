/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jasbro.game.housing;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameObject;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.BuildingAdvertising;
import jasbro.game.events.business.Fame;
import jasbro.game.events.business.SpawnData;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Azrael
 */
public class House extends GameObject implements AreaInterface {
    private HouseType houseType;
    private List<RoomSlot> roomSlots = new ArrayList<RoomSlot>();
    private int value;
    private String name;
    private Fame fame = new Fame();
    private int dirt = 0;
    private SpawnData spawnData;
    private transient HashMap<Time, ImageData> images;
    private BuildingAdvertising advertising;

    public House() {
    }

    public House(HouseType houseType, int value) {
        this();
        this.houseType = houseType;
        this.value = value;
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

    public HashMap<Time, ImageData> getImages() {
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

    public int getSellPrice() {
        return (int) Util.getPercent(getValue(), 75);
    }

    public CleanState getCleanState() {
        return CleanState.calcState(this);
    }

    @Override
    public int getLocationAmount() {
        return roomSlots.size();
    }

    @Override
    public List<? extends CharacterLocation> getLocations() {
        return getRoomSlots();
    }

    public void setHouseType(HouseType houseType) {
        this.houseType = houseType;
    }

    public HouseType getHouseType() {
        if (houseType == null) {
            for (HouseType type : HouseType.values()) {
                if (type.isOfType(this)) {
                    houseType = type;
                    break;
                }
            }
        }
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
                        pay += room.getRoomType().getCost() / 4;
                    }
                }
            }
            Jasbro.getInstance().getData().spendMoney(pay, this); // general
                                                                  // expenses
        }

        super.handleEvent(e);
        getSpawnData().handleEvent(e);
    }

    /**
     * HOUSETYPES:
     * 
     * 
     * 
     * 
     */
    public static class Hut extends House {
        private final static int value = 400;

        public Hut() {
            super(HouseType.HUT, value);
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.BEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.OUTDOOR, RoomType.POND, this));
        }

        @Override
        public void initImages() {
            getImages().put(Time.MORNING, new ImageData("images/backgrounds/hut_morning.jpg"));
            getImages().put(Time.AFTERNOON, new ImageData("images/backgrounds/hut_afternoon.jpg"));
            getImages().put(Time.NIGHT, new ImageData("images/backgrounds/hut_night.jpg"));
        }
    }

    public static class SmallHouse extends House {
        private final static int value = 4500;

        public SmallHouse() {
            super(HouseType.HOUSE, value);

            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.BEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.KITCHEN, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.OUTDOOR, RoomType.GARDEN, this));
        }

        @Override
        public void initImages() {
            getImages().put(Time.MORNING, new ImageData("images/backgrounds/house2_morning.png"));
            getImages().put(Time.AFTERNOON, new ImageData("images/backgrounds/house2_afternoon.png"));
            getImages().put(Time.NIGHT, new ImageData("images/backgrounds/house2_night.png"));
        }
    }

    public static class School extends House {
        private final static int value = 15000;

        public School() {
            super(HouseType.SCHOOL, value);

            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.CLASSROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.CLASSROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.GYM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.EMPTYROOM, this));
        }

        @Override
        public void initImages() {
            getImages().put(Time.MORNING, new ImageData("images/backgrounds/school_morning.jpg"));
            getImages().put(Time.AFTERNOON, new ImageData("images/backgrounds/school_afternoon.jpg"));
            getImages().put(Time.NIGHT, new ImageData("images/backgrounds/school_night.jpg"));
        }
    }

    public static class Shrine extends House {
        private final static int value = 35000;

        public Shrine() {
            super(HouseType.SHRINE, value);

            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.UNDERGROUND, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.OUTDOOR, RoomType.ALTAR, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.OUTDOOR, RoomType.GARDEN, this));
        }

        @Override
        public void initImages() {
            getImages().put(Time.MORNING, new ImageData("images/backgrounds/shrine_morning.jpg"));
            getImages().put(Time.AFTERNOON, new ImageData("images/backgrounds/shrine_afternoon.jpg"));
            getImages().put(Time.NIGHT, new ImageData("images/backgrounds/shrine_night.jpg"));
        }
    }

    public static class Mansion extends House {
        private final static int value = 85000;

        public Mansion() {
            super(HouseType.MANSION, value);

            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.MASTERBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.KITCHEN, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.BEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.BEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.OUTDOOR, RoomType.BIGGARDEN, this));
        }

        @Override
        public void initImages() {
            getImages().put(Time.MORNING, new ImageData("images/backgrounds/house_morning.jpg"));
            getImages().put(Time.AFTERNOON, new ImageData("images/backgrounds/house_afternoon.jpg"));
            getImages().put(Time.NIGHT, new ImageData("images/backgrounds/house_night.jpg"));
        }
    }

    public static class Garrison extends House {
        private final static int value = 250000;

        public Garrison() {
            super(HouseType.GARRISON, value);

            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.LARGEBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.KITCHEN, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.UNDERGROUND, RoomType.ARENA, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.UNDERGROUND, RoomType.DUNGEON, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.OUTDOOR, RoomType.TRAININGGROUNDS, this));
        }

        @Override
        public void initImages() {
            getImages().put(Time.MORNING, new ImageData("images/backgrounds/garrison_morning.jpg"));
            getImages().put(Time.AFTERNOON, new ImageData("images/backgrounds/garrison_afternoon.jpg"));
            getImages().put(Time.NIGHT, new ImageData("images/backgrounds/garrison_night.jpg"));
        }
    }

    public static class Monastry extends House {
        private final static int value = 500000;

        public Monastry() {
            super(HouseType.MONASTRY, value);

            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.LARGEBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.LARGEBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.LARGEBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.MASTERBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.MASTERBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.SMALLLIBRARY, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.KITCHEN, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.SMALLROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.UNDERGROUND, RoomType.CRYPT, this));
        }

        @Override
        public void initImages() {
            getImages().put(Time.MORNING, new ImageData("images/backgrounds/monastry_morning.jpg"));
            getImages().put(Time.AFTERNOON, new ImageData("images/backgrounds/monastry_afternoon.jpg"));
            getImages().put(Time.NIGHT, new ImageData("images/backgrounds/monastry_night.jpg"));
        }
    }

    public static class Palace extends House {
        private final static int value = 750000;

        public Palace() {
            super(HouseType.PALACE, value);

            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.SMALLBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.BEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.KITCHEN, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.KITCHEN, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.MASTERBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.MASTERBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.MASTERBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.MASTERBEDROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.UNDERGROUND, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.OUTDOOR, RoomType.POND, this));
        }

        @Override
        public void initImages() {
            getImages().put(Time.MORNING, new ImageData("images/backgrounds/palace_morning.jpg"));
            getImages().put(Time.AFTERNOON, new ImageData("images/backgrounds/palace_afternoon.jpg"));
            getImages().put(Time.NIGHT, new ImageData("images/backgrounds/palace_night.jpg"));
        }
    }

    public static class GiantCastle extends House {
        private final static int value = 1250000;

        public GiantCastle() {
            super(HouseType.GIANTCASTLE, value);

            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.LARGEROOM, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.UNDERGROUND, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.UNDERGROUND, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.UNDERGROUND, RoomType.EMPTYROOM, this));
            getRoomSlots().add(new RoomSlot(RoomSlotType.OUTDOOR, RoomType.BIGGARDEN, this));
        }

        @Override
        public void initImages() {
            getImages().put(Time.MORNING, new ImageData("images/backgrounds/giant_castle_morning.jpg"));
            getImages().put(Time.AFTERNOON, new ImageData("images/backgrounds/giant_castle_afternoon.jpg"));
            getImages().put(Time.NIGHT, new ImageData("images/backgrounds/giant_castle_night.jpg"));
        }

    }
}
