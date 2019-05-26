package jasbro.game.interfaces;

import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;

import java.util.List;

public interface HasImagesInterface {
	public List<ImageData> getImages();
	public List<ImageTag> getBaseTags();
}