package jasbro.gui;

import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;


public class CharacterFilterListModel extends AbstractListModel<Charakter> {
    private Filter filter = new Filter();
    private final ArrayList<Integer> displayedElements = new ArrayList<Integer>();
    private List<Charakter> sourceList;
    
	
    public List<Charakter> getSourceList() {
        if (sourceList == null) {
            sourceList = Jasbro.getInstance().getData().getCharacters();
        }
        return sourceList;
    }
    
    public void filter() {
        displayedElements.clear();
        sourceList = null;
        
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

    public Charakter getElementAt (int index) {
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


    public static class Filter {
		private String searchString;
		private House house;
		private Gender gender;
		private CharacterType type;
		
		public Filter () {
		}
		
        public Filter(String searchString) {
            super();
            this.searchString = searchString;
        }

        public boolean accept (Charakter character) {
        	if (searchString != null && !character.getName().toLowerCase().contains (searchString.toLowerCase())) {
        		return false;
        	}
        	if (house != null) {
        	    if (character.getActivity() == null || !(character.getActivity().getSource() instanceof Room)) {
        	        return false;
        	    }
        	    else if (((Room)character.getActivity().getSource()).getHouse() != house) {
        	        return false;
        	    }
        	}
        	if (gender != null) {
        	    if (character.getGender() != gender) {
        	        return false;
        	    }
        	}
        	if (type != null) {
        	    if (character.getType() != type) {
        	        return false;
        	    }
        	}
        	return true;
        }
        
        public String getSearchString () {
        	return searchString;
        }

        public House getHouse() {
            return house;
        }

        public void setHouse(House house) {
            this.house = house;
        }

        public void setSearchString(String searchString) {
            this.searchString = searchString;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public CharacterType getType() {
            return type;
        }

        public void setType(CharacterType type) {
            this.type = type;
        }
    }
}