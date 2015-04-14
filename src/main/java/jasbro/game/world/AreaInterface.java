package jasbro.game.world;

import jasbro.gui.pictures.ImageData;

import java.util.List;

public interface AreaInterface {
	public String getName();
	public List<CharacterLocation> getLocations();
	public ImageData getImage();
	public int getLocationAmount();
}
