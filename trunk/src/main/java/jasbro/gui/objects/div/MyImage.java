/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jasbro.gui.objects.div;

import jasbro.Jasbro;
import jasbro.gui.GuiUtil;
import jasbro.gui.MyPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageUtil;
import jasbro.gui.pictures.MyGifImageObject;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.imgscalr.Scalr;

/**
 *
 * @author Azrael
 */
public class MyImage extends MyPanel {
    private static final long serialVersionUID = -7099838193972833604L;
    private final static Logger log = Logger.getLogger(MyImage.class);
    
    private ImageData image = null;
    private boolean resize = false;
    private boolean centered = false;
    private ImageData backgroundImage = null;
    private int insetX = 0;
    private int insetY = 0;
    private boolean grayscale = false;
    
    private Future<Image> backGroundImageObject;
    private Future<Image> imageObject;

    /**
     * Creates new form MyImage
     */
    public MyImage() {
    	addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
        setOpaque(false);
        setFocusTraversalKeysEnabled(false);
        
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                backGroundImageObject = null;
                imageObject = null;
            };
            
            @Override
            public void componentHidden(ComponentEvent e) {
                backGroundImageObject = null;
                imageObject = null;
            }
        });
    }

    public MyImage(ImageData image) {
        this();
        setImage(image);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (this.backgroundImage != null) {
            if (backGroundImageObject == null || !backGroundImageObject.isDone()) {
                Image image = ImageUtil.getInstance().getImage(this.backgroundImage);
                if (backGroundImageObject == null && !(image instanceof MyGifImageObject)) {
                    queryResizeBackground();
                }

                if (image != null) {
                    if (grayscale && !(image instanceof MyGifImageObject)) {
                        image = ImageUtil.getInstance().convertToGrayScale((BufferedImage)image);
                    }
                    g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null, this);
                }
            }
            else {
                try {
                    g.drawImage(backGroundImageObject.get(), 0, 0, this.getWidth(), this.getHeight(), null, this);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error when accessing background image object", e);
                }
            }
        }
        if (this.image != null) {
            Image image = null;
            if (imageObject == null || !imageObject.isDone()) {                
            	image = ImageUtil.getInstance().getImage(this.image);
                if (image != null) {                
                    if (grayscale && !(image instanceof MyGifImageObject)) {
                        image = ImageUtil.getInstance().convertToGrayScale((BufferedImage)image);
                    }
                }
            }
            else {
                try {
                    image = imageObject.get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error when accessing background image object", e);
                }
            }
            	
            if (image != null) {
                if (getModifiedWidth() > 0 && getModifiedHeight() > 0) {
                    int imageWidth = image.getWidth(this);
                    int imageHeight = image.getHeight(this);

            		float percw = (float) getModifiedWidth() / (float) imageWidth;
                    float perch = (float) getModifiedHeight() / (float) imageHeight;
                    if (!resize) {
                        if (percw > perch) {
                            percw = perch;
                        } else {
                            perch = percw;
                        }
                    }
                    
                    int targetWidth = (int)(percw * imageWidth);
                    int targetHeight = (int)(perch *  imageHeight);
            		
                    int offsetX = (getWidth() - targetWidth) / 2; //horizontally center image
                    int offsetY;
                    if (centered) {
                        offsetY = (getHeight() - targetHeight) / 2; //vertically center image
                    }
                    else {
                        offsetY = getHeight() - targetHeight; //image on bottom
                    }
                    
                    if (imageObject == null && !(image instanceof MyGifImageObject)) {
                        queryResizeImage(targetWidth, targetHeight);
                    }

                    g = (Graphics2D) g.create();
                    g.drawImage(image, offsetX, offsetY, targetWidth, targetHeight, this);
                    g.dispose();
            	}
            }

        }
    }
    
    public int getModifiedWidth() {
    	return getWidth() - insetX * 2;
    }
    
    public int getModifiedHeight() {
    	return getHeight() - insetY * 2;
    }

    public ImageData getImage() {
        return image;
    }

    public void setImage(ImageData image) {
    	this.image = image;
    	this.imageObject = null;
    }
    
    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isCentered() {
        return centered;
    }

    public void setCentered(boolean centred) {
        this.centered = centred;
    }

	public ImageData getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(ImageData backgroundImage) {
		this.backgroundImage = backgroundImage;
		this.backGroundImageObject = null;
	}

	public int getInsetX() {
		return insetX;
	}

	public void setInsetX(int insetX) {
		this.insetX = insetX;
	}

	public int getInsetY() {
		return insetY;
	}

	public void setInsetY(int insetY) {
		this.insetY = insetY;
	}

    public boolean isGrayscale() {
        return grayscale;
    }

    public void setGrayscale(boolean grayscale) {
        this.grayscale = grayscale;
    }
	
	
    public void queryResizeBackground() {
        final ImageData backgroundImage = this.backgroundImage;
        if (backgroundImage != null) {
            try {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    @Override
                    public Void run() {
                        Callable<Image> callable = new Callable<Image>() {
                            @Override
                            public Image call() throws Exception {
                                Image resizedImageObject = ImageUtil.getInstance().getImageResized(backgroundImage, MyImage.this.getWidth(), 
                                        MyImage.this.getHeight(), Scalr.Mode.FIT_EXACT);
                                if (grayscale) {
                                    resizedImageObject = ImageUtil.getInstance().convertToGrayScale((BufferedImage)resizedImageObject);
                                }
                                repaint(20);
                                return resizedImageObject;
                            }
                        };
                        
                        backGroundImageObject = Jasbro.getThreadpool().submit(callable);
                        return null;
                    }
                });
            }
            catch (Exception e) {
                log.error("Error when submitting background resize request", e);
            }
        }
    }
    
    public void queryResizeImage(final int width, final int height) {
        final ImageData image = this.image;
        if (image != null) {
            try {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    @Override
                    public Void run() {
                        Callable<Image> callable = new Callable<Image>() {
                            @Override
                            public Image call() throws Exception {
                                Image resizedImageObject = ImageUtil.getInstance().getImageResized(image, width, 
                                        height, Scalr.Mode.FIT_TO_WIDTH);
                                if (grayscale) {
                                    resizedImageObject = ImageUtil.getInstance().convertToGrayScale((BufferedImage)resizedImageObject);
                                }
                                repaint(20);
                                return resizedImageObject;
                            }
                        };
                        
                        imageObject = Jasbro.getThreadpool().submit(callable);
                        return null;
                    }
                });
            }
            catch (Exception e) {
                log.error("Error when submitting background resize request", e);
            }
        }
    }
}
