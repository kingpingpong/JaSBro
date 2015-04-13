package jasbro.game.character;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.java.truevfs.access.TFile;

public class CharacterBase implements Serializable, HasImagesInterface {
    private String id;
    private String name;
    private List<ImageData> images = new ArrayList<ImageData>();
    private CharacterType type = null;
    private Map<BaseAttributeTypes, Integer> attributes = new HashMap<BaseAttributeTypes, Integer>();
    private Gender gender = Gender.FEMALE;
    private List<Trait> traits = new ArrayList<Trait>();
    private TFile folder;
    private boolean changed = false;
    private String description;
    private String youngerBase;
    private String olderBase;
    private SpecializationType initialSpecialization;
    private AgeProgressionData ageProgressionData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ImageData> getImages() {
        return images;
    }

    public void setImages(List<ImageData> images) {
        this.images = images;
    }

    public CharacterType getType() {
        return type;
    }

    public void setType(CharacterType type) {
        this.type = type;
    }    

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAttribute(BaseAttributeTypes key) {
        if (key == null) {
            return 1;
        }
        else if (!attributes.containsKey(key)) {
            return 6;
        }
        else {
            return attributes.get(key);
        }
    }

    public Integer setAttribute(BaseAttributeTypes key, Integer value) {
        if (key == null) {
            return null;
        }
        else {
            if (attributes.containsKey(key)) {
                attributes.remove(key);
            }
            return attributes.put(key, value);
        }
    }
    
    public Set<Entry<BaseAttributeTypes, Integer>> getAttributes() {
        return attributes.entrySet();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    

    
    public void addTrait(Trait trait) {
    	if (!traits.contains(trait)) {
    		traits.add(trait);
    	}
    }
    
    public void removeTrait(Trait trait) {
    	traits.remove(trait);
    }

    public List<Trait> getTraits() {
		return traits;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CharacterBase other = (CharacterBase) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

	public TFile getFolder() {
		return folder;
	}

	public void setFolder(TFile folder) {
		this.folder = folder;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public String getYoungerBase() {
        return youngerBase;
    }

    public void setYoungerBase(String previousBase) {
        this.youngerBase = previousBase;
    }

    public String getOlderBase() {
        return olderBase;
    }

    public void setOlderBase(String nextBase) {
        this.olderBase = nextBase;
    }

    public SpecializationType getInitialSpecialization() {
        return initialSpecialization;
    }

    public void setInitialSpecialization(SpecializationType initialSpecialization) {
        this.initialSpecialization = initialSpecialization;
    }

    public AgeProgressionData getAgeProgressionData() {
        if (ageProgressionData == null) {
            ageProgressionData = new AgeProgressionData();
        }
        return ageProgressionData;
    }
    
    public String getFullDescription() {
        String description = "";
        
        if (this.description != null && !this.description.equals("")) {
            description += TextUtil.t("ui.description")+ "\n" + getDescription();
        }
        description += "\n\n";
        description += TextUtil.t("ui.baseId", new Object[]{getId()}) + "\n";
        description += TextUtil.t("ui.imageAmount", new Object[]{getImages().size()}) + "\n\n";
        description += TextUtil.t("ui.startAttributes", new Object[]{
                getAttribute(BaseAttributeTypes.CHARISMA),
                getAttribute(BaseAttributeTypes.OBEDIENCE),
                getAttribute(BaseAttributeTypes.STAMINA),
                getAttribute(BaseAttributeTypes.INTELLIGENCE),
                getAttribute(BaseAttributeTypes.STRENGTH)}) + "\n\n";
        if (getInitialSpecialization() != null) {
            description += TextUtil.t("ui.startSpecialization", new Object[]{getInitialSpecialization().getText()}) + "\n\n";
        }
        return description;
    }

    @Override
    public List<ImageTag> getBaseTags() {
        return new ArrayList<ImageTag>();
    }
}
