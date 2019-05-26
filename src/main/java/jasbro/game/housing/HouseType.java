package jasbro.game.housing;

import jasbro.game.interfaces.UnlockObject;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;


public enum HouseType implements UnlockObject {
	HUT,
	HOUSE,
	SCHOOL,
	SHRINE,
	MANSION,
	GARRISON,
	MONASTRY,
	PALACE,
	GIANTCASTLE;
	
	private boolean locked = false;
	
	public String getText() {
		return TextUtil.t(this.toString());
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
		switch(this) {
		case HUT:
			return new ImageData("images/backgrounds/hut_morning.jpg");
		case GARRISON:
			return  new ImageData("images/backgrounds/garrison_morning.jpg");
		case GIANTCASTLE:
			return new ImageData("images/backgrounds/giant_castle_morning.jpg");
		case HOUSE:
			return new ImageData("images/backgrounds/house2_morning.jpg");
		case MANSION:
			return new ImageData("images/backgrounds/house_morning.jpg");
		case MONASTRY:
			return new ImageData("images/backgrounds/monastry_morning.jpg");
		case PALACE:
			return new ImageData("images/backgrounds/palace_morning.jpg");
		case SCHOOL:
			return new ImageData("images/backgrounds/school_morning.jpg");
		case SHRINE:
			return new ImageData("images/backgrounds/shrine_morning.jpg");
		}
		return null;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	
}