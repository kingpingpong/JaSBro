package jasbro.game.world.locations;

import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import org.apache.log4j.Logger;

public enum LocationType implements LocationTypeInterface, UnlockObject {
	
	BEACH(Beach.class), RESTAURANT(Restaurant.class), LIBRARY(Library.class), STREETS(Streets.class),
	DUNGEON1(Dungeon.class), DUNGEON2(Dungeon.class), DUNGEON3(Dungeon.class), DUNGEON4(Dungeon.class);
	
	private Logger log = Logger.getLogger(LocationType.class);
	private Class<? extends CharacterLocation> locationClass;
	private boolean locked = false;
	
	private LocationType(Class<? extends CharacterLocation> locationClass) {
		this.locationClass = locationClass;
	}
	
	public CharacterLocation getLocation() {
		try {
			CharacterLocation location = (CharacterLocation)locationClass.newInstance();
			if (location instanceof Dungeon) {
			    ((Dungeon) location).setType(this);
			}
			return location;
		} catch (InstantiationException e) {
			log.error("Error instantiating Object", e);
		} catch (IllegalAccessException e) {
			log.error("Error instantiating Object", e);
		}
		return null;
	}
	
	public boolean isInstance(CharacterLocation characterLocation) {
		return locationClass.isInstance(characterLocation);
	}
	
    public String getText() {
    	return TextUtil.t(this.toString());
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public ImageData getImage() {
        switch (this) {
        case RESTAURANT:
            return new ImageData("images/backgrounds/restaurant.jpg");
        case BEACH:
            return new ImageData("images/backgrounds/beach_morning.jpg");
        case LIBRARY:
            return new ImageData("images/backgrounds/library.jpg");
        case STREETS:
            return new ImageData("images/backgrounds/streets_morning.jpg");
        case DUNGEON1:
            return new ImageData("images/backgrounds/dungeon/dungeon1.png");
        case DUNGEON2:
            return new ImageData("images/backgrounds/dungeon/dungeon1.png");
        case DUNGEON3:
            return new ImageData("images/backgrounds/dungeon/dungeon1.png");
        case DUNGEON4:
            return new ImageData("images/backgrounds/dungeon/dungeon1.png");
        }
        return null;
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    
}
