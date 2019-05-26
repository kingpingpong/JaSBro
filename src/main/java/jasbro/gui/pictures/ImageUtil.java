package jasbro.gui.pictures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.texts.TextUtil;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;

public class ImageUtil implements ImageObserver {
    private final static Logger log = LogManager.getLogger(ImageUtil.class);
    private static ImageUtil instance;    
    private Cache<String, Image> images;
    private Cache<String, Image> resizedImages;

    public ImageUtil() {
        long memoryMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long cacheSize = memoryMB / 15;
        log.info("Max-memory: {} Cache size: {}", memoryMB, cacheSize);
    	images = CacheBuilder.newBuilder()
        	       .maximumSize(cacheSize)
        	       .expireAfterAccess(1, TimeUnit.MINUTES)
        	       .build();
    	resizedImages = CacheBuilder.newBuilder()
                .maximumSize(cacheSize / 4)
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .build();
    }

    public static ImageUtil getInstance() {
        if (instance == null) {
            instance = new ImageUtil();
        }
        return instance;
    }

	public Image getImage(final ImageData image) {
		Image img = images.getIfPresent(image.getKey());
		if (img == null) {
			img = AccessController.doPrivileged(new PrivilegedAction<Image>() {

				@Override
				public Image run() {
					try {
						return loadImage(image);
					} catch (IOException e) {
						return null;
					}
				}
			});
			
			if(img == null) {
				log.error("Failed to load image");
			}
		}
		
		return img;
    }

	private Image loadImage(ImageData imageData) throws IOException {
        InputStream input = null;
        try {
        	Image image = null;
        	image = images.getIfPresent(imageData.getKey());
        	if (image != null) {
        		return image;
        	}
        	else {
                TFile imageFile = new TFile(imageData.getKey());
                if (imageFile.exists()) {
                    input = new TFileInputStream(imageFile);
                    
                    if (imageData.getKey().endsWith(".gif")) {
                    	ImageInputStream imageInputStream = ImageIO.createImageInputStream( input );
                    	ImageReader imageReader = ImageIO.getImageReaders(imageInputStream).next();
                    	imageReader.setInput(imageInputStream);
                    	image = new MyGifImageObject(readGIF(imageReader));
                    	clearCache();
                    }
                    else {
                        image = ImageIO.read(input);
                    }
                    
                    images.put(imageData.getKey(), image);  
                    return image;
                }
                else {
                	log.error("Image not found: " + imageData.getFilename());
                    return null;
                }
        	}
        } catch (IOException e) {
        	log.error("Failed to load image for file '{}'", imageData.getFilename());
        	throw log.throwing(e);
        }
        finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                log.error("Error on closing input", e);
            }
        }
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return (infoflags & (ALLBITS | ABORT)) == 0;
    }
    
    public ImageData getImageDataByTag(ImageTag tag, HasImagesInterface character) {
    	List<ImageTag> tags = character.getBaseTags();
    	if (tags.size() == 0) {
    		return getImageDataByTag(tag, character.getImages());
    	}
    	else {
    	    if (tag == ImageTag.STANDARD) {
    	        //don't add, let character tags handle this
    	    }
    	    else {
    	        tags.add(0, tag);
    	    }
    		return getImageDataByTags(tags, character.getImages());
    	}
    }
    
    public ImageData getImageDataByTag(ImageTag tag, List<ImageData> imageList) {
    	//ImageTag orginalTag = tag;
    	List<ImageData> imagePool;
    	List<ImageData> imagesTag;
    	imagePool = removeStrongTags(tag, imageList);
    	do {
    		imagesTag = getImageDataByTagOnly(tag, imagePool);
	        tag = tag.getReplacementTag();
        	if (imagesTag.size() > 0 && imagesTag.size() < 6) {
        		if (Util.getInt(0, 100) < 30 - imagesTag.size() * 5) {
        			if (tag != null) {
            			continue;
        			}
        		}
        	}
    	}
    	while (imagesTag.size() == 0 && tag != null);
        if (imagesTag.size() == 0) {
        	if (imagePool.size() == 0) {
        		imagePool = imageList;
        	}
            ImageData image = imagePool.get(Util.getRnd().nextInt(imagePool.size()));
            //log.info("No image found, going random " + orginalTag + " " + image.getKey());
            return image;
        }
        ImageData image = imagesTag.get(Util.getRnd().nextInt(imagesTag.size()));
        return image;
    }

    public Image getImageResized(ImageData imageData, int width, int height, Scalr.Mode mode) {
        Image image = resizedImages.getIfPresent(imageData.getKey());
        if (!fitsWell(image, width, height, mode)) {                       
            image = getImage(imageData);
            if (image == null) {
                return null;
            }
            if (image.getWidth(this) > width + 100 && image.getHeight(this) > height + 100) {
                image = Scalr.resize((BufferedImage) image, Scalr.Method.AUTOMATIC, mode, width, height, Scalr.OP_ANTIALIAS);
            }
            else {
                image = Scalr.resize((BufferedImage) image, Scalr.Method.AUTOMATIC, mode, width, height);
            }

            resizedImages.put(imageData.getKey(), image);
        }
        return image;
    }
    
    public boolean fitsWell(Image image, int width, int height, Scalr.Mode mode) {
        if (image == null || 
                (mode == Scalr.Mode.FIT_TO_WIDTH && width != image.getWidth(this)) ||
                (mode == Scalr.Mode.FIT_TO_HEIGHT && height != image.getWidth(this)) ||
                (mode == Scalr.Mode.FIT_EXACT && (height != image.getWidth(this) || width != image.getHeight(this))) ||
                (mode == Scalr.Mode.AUTOMATIC && (height != image.getWidth(this) && width != image.getHeight(this)))
                ) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public Image getImageResizedSpeed(ImageData imageData, final int width, final int height, final Scalr.Mode mode) {                      
        Image image = getImage(imageData);
        if (image == null) {
            return null;
        }
        image = Scalr.resize((BufferedImage) image, Scalr.Method.SPEED, mode, width, height);
        return image;
    }
    
    public ImageData getImageDataByTags(final List<ImageTag> tags, final List<ImageData> imageList) {

    	if (tags.size() <= 1) {
            return getImageDataByTag(tags.get(0), imageList);
        }
    	
        final ImageData imageDataEmpty = new ImageData();
    	
    	final Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                List<ImageData> imageListTmp = removeStrongTags(tags, imageList);
                int highestValue = -10000;
                ImageData bestMatch = null;
                Map<ImageData, Integer> imageValueMap=new HashMap<>();
                for (ImageData imageData : imageListTmp) {
                    int sumValue = 0;
                    int tagValue = 0;
                    for (int i = 0; i < tags.size(); i++) {
                        ImageTag tag = tags.get(i);
                        tagValue = tag.getImageTagGroup().getGroupValue();
                        if (i > 2) {
                            tagValue -= i;
                        } else if (i == 0) {
                            tagValue += 120;
                        } else if (i == 1) {
                            tagValue += 20;
                        } else {
                            tagValue += 10;
                        }
                        
                        if (i > 0) {
                            if (//don't care about clothing tags when inappropriate  
                                tag.getImageTagGroup() == ImageTagGroup.CLOTHING &&
                                (tags.get(0).getImageTagGroup() == ImageTagGroup.SEX 
                                    || tags.get(0) == ImageTag.SWIM
                                    || tags.get(0) == ImageTag.BATHE)) {
                                tagValue /= 10;
                            }
                            else if (tags.get(0) == ImageTag.CLOTHED //but highly value clothes if it is main tag
                                    && tag.getImageTagGroup() == ImageTagGroup.CLOTHING) {
                                tagValue += 35;
                            }
                        }

                        if (tagValue > 0 && tag != null) {
                            if (imageData.getTags().contains(tag)) {
                                sumValue += tagValue;
                            }
                            else if (tag == ImageTag.CLOTHED) {
                                if (imageData.getTags().contains(ImageTag.NAKED)) {
                                    sumValue -= tagValue / 2;
                                }
                            }
                            else if (tag == ImageTag.NAKED) {
                                if (imageData.getTags().contains(ImageTag.CLOTHED)) {
                                    sumValue -= tagValue / 2;
                                }
                            }
                            else {
                                if (tag.getImageTagGroup() != ImageTagGroup.CLOTHING) { //Look for replacement tags (as long as it isn't clothing)
                                    while (tag.getReplacementTag() != null && tagValue > 15) {
                                        tagValue = tagValue / 2;
                                        tagValue -= 2;
                                        tag = tag.getReplacementTag();
                                        if (imageData.getTags().contains(tag)) {
                                            sumValue += tagValue;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (sumValue <= 0) {
                        continue;
                    }
                    
                    //if image is in cache, reduce significance 
                    if (images.getIfPresent(imageData.getKey()) != null) {
                        sumValue -= 25;
                    }
                    
                    //add random factor
                    int randomFactor = Util.getInt(0, 100);
                    sumValue += randomFactor;

                    if (sumValue > highestValue) {
                        bestMatch = imageData;
                        highestValue = sumValue;
                    }
                    
                    if (log.isDebugEnabled()) {
                    	imageValueMap.put(imageData, sumValue);
                    }
                }

                if (log.isDebugEnabled()) {
                	log.debug("Image values for tags {}: {}", tags, imageValueMap);
                }

                if (bestMatch == null) {
                    log.debug("No fitting image found by searching for tags, use getByTag method {}", tags.get(0));
                    bestMatch = getImageDataByTag(tags.get(0), imageList);
                }
                imageDataEmpty.setCurAccuracy(highestValue);
                imageDataEmpty.init(bestMatch);
                return true;
            }
        };
        AccessController.doPrivileged(new PrivilegedAction<Void>() {

			@Override
			public Void run() {
		        imageDataEmpty.setFuture(Jasbro.getThreadpool().submit(callable));
				return null;
			}
        });
        return imageDataEmpty;
    }
    
    private List<ImageData> removeStrongTags(ImageTag allowedTag, List<ImageData> imageList) {
        List<ImageTag> allowedTags = new ArrayList<ImageTag>();
        allowedTags.add(allowedTag);
        return removeStrongTags(allowedTags, imageList);
    }
    
    private List<ImageData> removeStrongTags(List<ImageTag> allowedTags, List<ImageData> imageList) {
        List<ImageData> images = new ArrayList<ImageData>();

        for (ImageData imageData : imageList) {
        	boolean accepted = true;
        	for (ImageTag imageTag : imageData.getTags()) {
        		try {
        			if (imageTag.isExcludeTag() && !allowedTags.contains(imageTag)) {
                        accepted = false;
                        break;
        			}
        		}
        		catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        	if (accepted) {
        		images.add(imageData);
        	}
        }
        return images;
    }

    private List<ImageData> getImageDataByTagOnly(ImageTag tag, List<ImageData> imageList) {
        List<ImageData> images = new ArrayList<ImageData>();
        for (ImageData data : imageList) {
            if (data.hasTag(tag)) {
                images.add(data);
            }
        }
        return images;
    }
    
    public boolean isImage(TFile file) {
        MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
        typeMap.addMimeTypes("image png tif jpg jpeg bmp gif");
        String mimeType = typeMap.getContentType(file);
        return mimeType.substring(0, 5).equalsIgnoreCase("image");
    }
    
    public boolean isImage(Path path) {
        String contentType = "asdfdsfafsda";
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            log.error("Error on checking content type", e);
        }
        if (contentType == null) {
            return false;
        }
        else {
            return contentType.substring(0, 5).equalsIgnoreCase("image");
        }
    }
    
	private List<ImageFrame> readGIF(ImageReader reader) throws IOException {
	    ArrayList<ImageFrame> frames = new ArrayList<ImageFrame>(2);

	    int width = -1;
	    int height = -1;

	    IIOMetadata metadata = reader.getStreamMetadata();
	    if (metadata != null) {
	        IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());

	        NodeList globalScreenDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");

	        if (globalScreenDescriptor != null && globalScreenDescriptor.getLength() > 0) {
	            IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreenDescriptor.item(0);

	            if (screenDescriptor != null) {
	                width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
	                height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
	            }
	        }
	    }

	    BufferedImage master = null;
	    Graphics2D masterGraphics = null;

	    for (int frameIndex = 0;; frameIndex++) {
	        BufferedImage image;
	        try {
	            image = reader.read(frameIndex);
	        } catch (IndexOutOfBoundsException io) {
	            break;
	        }

	        if (width == -1 || height == -1) {
	            width = image.getWidth();
	            height = image.getHeight();
	        }

	        IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
	        IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
	        int delay = Integer.valueOf(gce.getAttribute("delayTime"));
	        String disposal = gce.getAttribute("disposalMethod");

	        int x = 0;
	        int y = 0;

	        if (master == null) {
	            master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            masterGraphics = master.createGraphics();
	            masterGraphics.setBackground(new Color(0, 0, 0, 0));
	        } else {
	            NodeList children = root.getChildNodes();
	            for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++) {
	                Node nodeItem = children.item(nodeIndex);
	                if (nodeItem.getNodeName().equals("ImageDescriptor")) {
	                    NamedNodeMap map = nodeItem.getAttributes();
	                    x = Integer.valueOf(map.getNamedItem("imageLeftPosition").getNodeValue());
	                    y = Integer.valueOf(map.getNamedItem("imageTopPosition").getNodeValue());
	                }
	            }
	        }
	        masterGraphics.drawImage(image, x, y, null);

	        BufferedImage copy = new BufferedImage(master.getColorModel(), master.copyData(null), master.isAlphaPremultiplied(), null);
	        frames.add(new ImageFrame(copy, delay, disposal));

	        if (disposal.equals("restoreToPrevious")) {
	            BufferedImage from = null;
	            for (int i = frameIndex - 1; i >= 0; i--) {
	                if (!frames.get(i).getDisposal().equals("restoreToPrevious") || frameIndex == 0) {
	                    from = frames.get(i).getImage();
	                    break;
	                }
	            }

	            master = new BufferedImage(from.getColorModel(), from.copyData(null), from.isAlphaPremultiplied(), null);
	            masterGraphics = master.createGraphics();
	            masterGraphics.setBackground(new Color(0, 0, 0, 0));
	        } else if (disposal.equals("restoreToBackgroundColor")) {
	            masterGraphics.clearRect(x, y, image.getWidth(), image.getHeight());
	        }
	    }
	    reader.dispose();	    
	    return frames;
	}

	public boolean exists(final ImageData image) {
		if (image != null) {
		    boolean result = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
                @Override
                public Boolean run() {
                    return new TFile(image.getKey()).exists();
                }		        
		    });
		    return result;
		}
		return false;
	}
	
	public void clearCache() {
		images.invalidateAll();
		resizedImages.invalidateAll();
	}

	public boolean tagExists(ImageTag imageTag, List<ImageData> images) {
		for (ImageData image : images) {
			if (image.getTags().contains(imageTag)) {
				return true;
			}
		}
		return false;
	}
	
	public BufferedImage convertToGrayScale(BufferedImage original) {
	    return Scalr.apply((BufferedImage)original, Scalr.OP_GRAYSCALE);
	}
	
	public static class BestImageSelection implements Callable<MessageData> {	    
	    private Charakter characters[];
	    private List<ImageTag> tags;
	    private Sextype sextype;
	    
	    public BestImageSelection(List<ImageTag> tags, Charakter... characters) {
	        this.tags = tags;
	        this.characters = characters;
	    }
	    
        public BestImageSelection(Sextype sextype, Charakter... characters) {
            this.sextype = sextype;
            this.tags = new ArrayList<ImageTag>();
            tags.add(0, sextype.getAssociatedImageTag());
            this.characters = characters;
        }
        
        public BestImageSelection(Sextype sextype, List<ImageTag> tags, Charakter... characters) {
            this.tags = tags;
            this.characters = characters;
            this.sextype = sextype;
            tags.add(sextype.getAssociatedImageTag());
        }
	    
        @Override
        public MessageData call() throws Exception {
            MessageData messageData = new MessageData();
            List<ImageData> possibleImages = new ArrayList<ImageData>();
            
            for (Charakter character : characters) {
                List<ImageTag> tags = new ArrayList<ImageTag>(this.tags);
                List<Charakter> characters = new ArrayList<Charakter>(Arrays.asList(this.characters));
                characters.remove(character);
                characters.add(0, character);
                tags.addAll(ImageTag.getAssociatedImageTags(characters.toArray(new Charakter[characters.size()])));
                tags.addAll(character.getBaseTags());
                if (tags.contains(ImageTag.FUTA)) {
                    tags.remove(ImageTag.FUTA);
                    tags.add(0, ImageTag.FUTA);
                }
                if (tags.contains(ImageTag.LESBIAN)) {
                    tags.remove(ImageTag.LESBIAN);
                    tags.add(0, ImageTag.LESBIAN);
                }
                possibleImages.add(ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()));
            }
            ImageData bestMatch = possibleImages.get(0);
            possibleImages.remove(bestMatch);
            for (ImageData imageData : possibleImages) {
                if (imageData.getCurAccuracy() > bestMatch.getCurAccuracy()) {
                    bestMatch = imageData;
                }
            }
            messageData.setImage(bestMatch);
            messageData.setBackground(characters[0].getBackground());
            
            
            //Add sextype specific text
            if (sextype != null && characters.length == 2) {
                Charakter character1 = null;
                Charakter character2 = null;
                for (Charakter character : characters) {
                    if (character.getImages().contains(bestMatch)) {
                        character1 = character;
                        break;
                    }
                }
                for (Charakter character : characters) {
                    if (character1 != character) {
                        character2 = character;
                    }
                }
                if (character1.getGender() != Gender.MALE && character2.getGender() == Gender.MALE) {
                    Charakter charTmp = character1;
                    character1 = character2;
                    character2 = charTmp;
                }
                messageData.addToMessage(TextUtil.t("sex.basic." + sextype.toString(), character1, character2));
                
                ImageTag specificTag = ImageTag.getSpecificTag(sextype.getAssociatedImageTag(), bestMatch.getTags());
                if (specificTag != sextype.getAssociatedImageTag()) {
                    String message = TextUtil.t("sex.specific." + specificTag, character1, character2);
                    if (!message.equals("sex.specific." + specificTag)) {
                        messageData.addToMessage(message);
                    }
                }
            }
            return messageData;
        }
	}
}
