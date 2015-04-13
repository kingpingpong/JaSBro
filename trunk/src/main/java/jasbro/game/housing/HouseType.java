package jasbro.game.housing;

import jasbro.game.housing.House.GiantCastle;
import jasbro.game.world.Time;
import jasbro.game.housing.House.Garrison;
import jasbro.game.housing.House.Hut;
import jasbro.game.housing.House.Mansion;
import jasbro.game.housing.House.Monastry;
import jasbro.game.housing.House.Palace;
import jasbro.game.housing.House.School;
import jasbro.game.housing.House.Shrine;
import jasbro.game.housing.House.SmallHouse;
import jasbro.texts.TextUtil;

import org.apache.log4j.Logger;

import jasbro.game.interfaces.UnlockObject;
import jasbro.gui.pictures.ImageData;


public enum HouseType implements UnlockObject {
	HUT(Hut.class), 
	HOUSE(SmallHouse.class), 
	SCHOOL(School.class), 
	SHRINE(Shrine.class),
	MANSION(Mansion.class), 
	GARRISON(Garrison.class),
	MONASTRY(Monastry.class), 
	PALACE(Palace.class),
	GIANTCASTLE(GiantCastle.class);
	
	private final static Logger log = Logger.getLogger(HouseType.class);
	private Class<? extends House> houseClass;
	private boolean locked = false;
	
	private HouseType(Class<? extends House> houseClass) {
		this.houseClass = houseClass;
	}
	
    public String getText() {
    	return TextUtil.t(this.toString());
    }
	
	public House getHouse() {
		try {
			return houseClass.newInstance();
		} catch (InstantiationException e) {
			log.error("Error instantiating " + this.toString(), e);
		} catch (IllegalAccessException e) {
			log.error("Error instantiating " + this.toString(), e);
		}
		return null;
	}
	
	public boolean isOfType(House house) {
		return houseClass.isInstance(house);
	}

    public boolean isLocked() {
        return locked;
    }

    @Override
    public String getDescription() {
        return ""; //TODO add description to houses
    }

    @Override
    public ImageData getImage() {
        return getHouse().getImages().get(Time.MORNING);
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    
    
}
