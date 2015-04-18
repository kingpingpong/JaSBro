package jasbro.game.interfaces;

import jasbro.game.world.CharacterLocation;
import jasbro.gui.pictures.ImageData;

import java.util.List;

public interface AreaInterface {
	public String getName();
	public List<? extends CharacterLocation> getLocations();
	public ImageData getImage();
	public int getLocationAmount();
}
