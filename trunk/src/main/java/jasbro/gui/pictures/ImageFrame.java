package jasbro.gui.pictures;

import java.awt.image.BufferedImage;

public class ImageFrame {
    private final int delay;
    private final BufferedImage image;
    private final String disposal;

    public ImageFrame(BufferedImage image, int delay, String disposal) {
        this.image = image;
        this.delay = delay;
        this.disposal = disposal;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getDelay() {
        return delay;
    }

    public String getDisposal() {
        return disposal;
    }
}