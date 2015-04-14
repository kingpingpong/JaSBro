package jasbro.gui;

import jasbro.game.interfaces.HasImagesInterface;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;


public class ImageDataFilterListModel extends AbstractListModel<ImageData> {
    private Filter filter = new Filter();
    private final ArrayList<Integer> displayedElements = new ArrayList<Integer>();
    private HasImagesInterface imageContainerObject;   
	
    public List<ImageData> getSourceList() {
        return imageContainerObject.getImages();
    }
    
    public void filter() {
        displayedElements.clear();
        
        for (int i = 0; i < getSourceList().size(); i++) {
            if (filter.accept(getSourceList().get(i))) {
                displayedElements.add(i);
            }
        }
        fireContentsChanged (this, 0, getSize() - 1);
    }

    public int getSize () {
        return displayedElements.size();
    }

    public ImageData getElementAt (int index) {
        return getSourceList().get(displayedElements.get(index));
    }
    
	public void setFilter(Filter filter) {
        this.filter = filter;
        filter();
    }

    public Filter getFilter() {
        return filter;
    }

    public void reset() {
        filter = new Filter();
        filter();
    }
    
    public void setBase(HasImagesInterface characterBase) {
        this.imageContainerObject = characterBase;
        reset();
    }


    public static class Filter {
		private String searchString = "";
		private ImageTag imageTag;
		private boolean noTagsOnly;
		
		public Filter () {
		}
		
        public Filter(String searchString) {
            super();
            this.searchString = searchString;
        }

        public boolean accept (ImageData imageData) {
        	if (searchString != null && !imageData.getFilename().toLowerCase().contains (searchString.toLowerCase())) {
        		return false;
        	}
        	if (noTagsOnly && imageData.getTags().size() > 0) {
        	    return false;
        	}
        	if (imageTag != null) {
        	    if (!imageData.getTags().contains(imageTag)) {
        	        return false;
        	    }
        	}
        	return true;
        }
        
        public String getSearchString () {
        	return searchString;
        }

        public void setSearchString(String searchString) {
            this.searchString = searchString;
        }

        public ImageTag getImageTag() {
            return imageTag;
        }

        public void setImageTag(ImageTag imageTag) {
            this.imageTag = imageTag;
        }

        public boolean isNoTagsOnly() {
            return noTagsOnly;
        }

        public void setNoTagsOnly(boolean noTagsOnly) {
            this.noTagsOnly = noTagsOnly;
        }
        
        
    }
}