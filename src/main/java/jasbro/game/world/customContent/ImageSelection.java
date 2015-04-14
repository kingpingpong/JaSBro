package jasbro.game.world.customContent;

import jasbro.game.character.Charakter;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.game.items.Item;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TPath;
import bsh.EvalError;

public class ImageSelection {

    //option1: image by filename
    private String image;
    private ImageLocation imageLocation = ImageLocation.LOCAL;
    
    //option2 image by character imagetag / background
    private List<ImageTag> imageTags;
    private boolean background = false;
    private String target = "character";
    
    public ImageData getImageData(WorldEvent worldEvent) throws EvalError {
        if (background) {
            return ((Charakter)worldEvent.getAttribute(target)).getBackground();
        }
        else if (image != null) {
            String relativePath;
            if (imageLocation == ImageLocation.LOCAL) {
                TFile folder = worldEvent.getFile().getParentFile();
                relativePath = getRelativePathFor(folder);
            }
            else {
                relativePath = "images";
            }
            return new ImageData(relativePath + "/" + image);
        }
        else if (imageTags.size() > 0) {
            HasImagesInterface character = ((HasImagesInterface)worldEvent.getAttribute(target));
            return ImageUtil.getInstance().getImageDataByTag(imageTags.get(0), character);
        }

        
        return null;
    }
    
    private String getRelativePathFor(final TFile folder) {
        String path=AccessController.doPrivileged(new PrivilegedAction<String>() {

			@Override
			public String run() {
				return new TPath(new TFile("")).relativize(new TPath(folder)).toString();
			}
        });
        return path;
	}

	public ImageData getImageData(Item item) {        
        if (image != null) {
            String relativePath;
            if (imageLocation == ImageLocation.LOCAL) {
                TFile folder = item.getFile().getParentFile();
                relativePath = getRelativePathFor(folder);
            }
            else {
                relativePath = "images";
            }
            return new ImageData(relativePath + "/" + image);
        }
        
        return null;
    }
    
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public boolean isBackground() {
        return background;
    }
    public void setBackground(boolean background) {
        this.background = background;
    }
    public ImageLocation getImageLocation() {
        return imageLocation;
    }
    public void setImageLocation(ImageLocation imageLocation) {
        this.imageLocation = imageLocation;
    }
    public List<ImageTag> getImageTags() {
        if (imageTags == null) {
            imageTags = new ArrayList<ImageTag>();
            imageTags.add(ImageTag.STANDARD);
        }
        return imageTags;
    }
    public void setImageTags(List<ImageTag> imageTags) {
        this.imageTags = imageTags;
    }



    public static enum ImageLocation {
        LOCAL, GLOBAL
    }
    
    
    
}
