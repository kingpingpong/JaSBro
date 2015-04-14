package jasbro.game.world;

import jasbro.Jasbro;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.RoomType;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.items.UnlockItem;
import jasbro.game.world.locations.LocationType;

import java.util.ArrayList;
import java.util.List;

public class Unlocks {
    
    private List<UnlockObject> unlockedObjects;
    private transient List<FameUnlock> fameUnlockObjects;
    
    public List<HouseType> getAvailableHouseTypes() {
        if (fameUnlockObjects == null) {
            init();
        }
        
        List<HouseType> houseTypes = new ArrayList<HouseType>();
        for (HouseType houseType : HouseType.values()) {
            if (!houseType.isLocked()) {
                houseTypes.add(houseType);
            }
            else if (getUnlockedObjects().contains(houseType)) {
                houseTypes.add(houseType);
            }
        }
        return houseTypes;
    }
    
    public List<RoomType> getAvailableRoomTypes() {
        if (fameUnlockObjects == null) {
            init();
        }
        
        List<RoomType> roomTypes = new ArrayList<RoomType>();        
        for (RoomType roomType : RoomType.values()) {
            if (!roomType.isLocked()) {
                roomTypes.add(roomType);
            }
            else if (getUnlockedObjects().contains(roomType)) {
                roomTypes.add(roomType);
            }
        }        
        return roomTypes;
    }
    
    public List<LocationType> getAvailableLocations() {
        if (fameUnlockObjects == null) {
            init();
        }
        
        List<LocationType> locationTypes = new ArrayList<LocationType>();        
        for (LocationType locationType : LocationType.values()) {
            if (!locationType.isLocked()) {
                locationTypes.add(locationType);
            }
            else if (getUnlockedObjects().contains(locationType)) {
                locationTypes.add(locationType);
            }
        }        
        return locationTypes;
    }
    
    public List<SpecializationType> getAvailableSpecializations() {
        if (fameUnlockObjects == null) {
            init();
        }
        
        List<SpecializationType> specializationTypes = new ArrayList<SpecializationType>();        
        for (SpecializationType specializationType : SpecializationType.values()) {
            if (!specializationType.isLocked()) {
                specializationTypes.add(specializationType);
            }
            else if (getUnlockedObjects().contains(specializationType)) {
                specializationTypes.add(specializationType);
            }
        }        
        return specializationTypes;
    }
    
    public void init() {
        for (HouseType houseType : HouseType.values()) {
            houseType.setLocked(false);
        }
        for (RoomType roomType : RoomType.values()) {
            roomType.setLocked(false);
        }
        for (SpecializationType specializationType : SpecializationType.values()) {
            specializationType.setLocked(false);
        }
        
        for (Item item : Jasbro.getInstance().getItems().values()) {
            if (item.getType() == ItemType.UNLOCK) {
                UnlockObject unlockObject = ((UnlockItem) item).getUnlockObject();
                if (unlockObject != null) {
                    unlockObject.setLocked(true);
                }
            }
        }
        
        fameUnlockObjects = new ArrayList<FameUnlock>();
        new FameUnlockLoader().loadUnlocks(this);
    }
    
    public List<FameUnlock> getFameUnlockObjects() {
        if (fameUnlockObjects == null) {
            init();
        }
        return fameUnlockObjects;
    }
    
    public List<UnlockObject> getUnlockedObjects() {
        ArrayList<UnlockObject> unlocks = new ArrayList<UnlockObject>(getUnlockedObjectsInternal());
        for (FameUnlock fameUnlock : getFameUnlockObjects()) {
            if (fameUnlock.isUnlocked()) {
                unlocks.add(fameUnlock.getUnlockObject());
            }
        }
        return unlocks;
    }

    public List<UnlockObject> getUnlockedObjectsInternal() {
        if (unlockedObjects == null) {
            unlockedObjects = new ArrayList<UnlockObject>();
        }
        return unlockedObjects;
    }

    private long getFame() {
        return Jasbro.getInstance().getData().getProtagonist().getFame().getFame();
    }
    
    public void addUnlock(UnlockObject unlockObject) {
        if (!getUnlockedObjectsInternal().contains(unlockObject)) {
            getUnlockedObjectsInternal().add(unlockObject);
        }
    }
    
    public class FameUnlock {
        private long requiredFame;
        private UnlockObject unlockObject;
        
        public FameUnlock() {
        }
        
        public FameUnlock(UnlockObject unlockObject, long requiredFame) {
            super();
            this.unlockObject = unlockObject;
            this.requiredFame = requiredFame;
        }
        
        public long getRequiredFame() {
            return requiredFame;
        }

        public void setRequiredFame(long requiredFame) {
            this.requiredFame = requiredFame;
        }

        public boolean isUnlocked() {
            return getFame() >= requiredFame;
        }
        public UnlockObject getUnlockObject() {
            return unlockObject;
        }
        public void setUnlockObject(UnlockObject unlockObject) {
            this.unlockObject = unlockObject;
        }
    }

    protected void addFameUnlock(UnlockObject unlockObject, long fame) {
        getFameUnlockObjects().add(new FameUnlock(unlockObject, fame));
        unlockObject.setLocked(true);
    }
    
    public boolean isUnlocked(UnlockObject unlockObject) {
        return !unlockObject.isLocked() || getUnlockedObjects().contains(unlockObject);
    }
    
    public boolean isLocked(UnlockObject unlockObject) {
        return unlockObject.isLocked() && !getUnlockedObjects().contains(unlockObject);
    }
}
