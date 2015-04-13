package jasbro.gui.pictures;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

/**
 * 
 * @author Azrael
 */
public class ImageData implements Serializable {
    private final static Logger log = Logger.getLogger(ImageData.class);
    
    private Set<ImageTag> tags;
    private String key;
    private String filename;
    private String customText;
    private transient Future<Boolean> future;
    private transient int curAccuracy;

    public ImageData() {
        tags = new HashSet<ImageTag>();
    }

    public ImageData(String key) {
        this();
        this.key = key;
        this.filename = key;
    }

    public ImageData(Future<Boolean> future) {
        this();
        this.future = future;
    }

    public void init(ImageData imageData) {
        this.tags = imageData.tags;
        this.key = imageData.key;
        this.filename = imageData.filename;
        this.customText = imageData.customText;
        this.curAccuracy = imageData.curAccuracy;
    }

    public Set<ImageTag> getTags() {
        try {
            if (future != null && future.get()) {            
            }
        }
        catch (Exception e) {
            log.error("Error during task", e);
        }
        return tags;
    }

    public boolean hasTag(ImageTag tag) {
        return getTags().contains(tag);
    }

    public String getKey() {
        try {
            if (future != null && future.get()) {            
            }
        }
        catch (Exception e) {
            log.error("Error during task", e);
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void addTag(ImageTag tag) {
        if (!getTags().contains(tag)) {
            getTags().add(tag);
        }
    }

    public void removeTag(ImageTag tag) {
        getTags().remove(tag);
    }

    @Override
    public String toString() {
        return getKey();
    }

    public String getTagString() {
        if (getTags().size() == 0) {
            return null;
        } else {
            String sTag = "";
            for (ImageTag tag : tags) {
                sTag += tag.toString() + ", ";
            }
            return sTag;
        }
    }

    public String getFilename() {
        try {
            if (future != null && future.get()) {            
            }
        }
        catch (Exception e) {
            log.error("Error during task", e);
        }
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getKey() == null) ? 0 : getKey().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImageData other = (ImageData) obj;
        if (getKey() == null) {
            if (other.getKey() != null)
                return false;
        } else if (!getKey().equals(other.getKey()))
            return false;
        return true;
    }

    public String getCustomText() {
        return customText;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    public int getCurAccuracy() {
        return curAccuracy;
    }

    public void setCurAccuracy(int curAccuracy) {
        this.curAccuracy = curAccuracy;
    }

    public void setFuture(Future<Boolean> future) {
        this.future = future;
    }
    
    

}
