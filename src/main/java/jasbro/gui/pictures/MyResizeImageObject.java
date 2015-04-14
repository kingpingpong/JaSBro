package jasbro.gui.pictures;

import java.awt.image.BufferedImage;

public class MyResizeImageObject {
    private BufferedImage image;
    
    public MyResizeImageObject(BufferedImage image) {        
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    
}
