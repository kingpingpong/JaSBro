package jasbro.game.world.customContent.npc;

import jasbro.game.character.battle.SimpleEnemy;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;

import java.util.ArrayList;
import java.util.List;

public class ComplexEnemy extends SimpleEnemy implements HasImagesInterface {
    private List<ImageData> images = new ArrayList<ImageData>();
    private String description;
    private String characterBaseId;
    
    //Texts
    private List<String> encounterTexts;
    private List<String> rapeTexts;
    
    public List<ImageData> getImages() {
        return images;
    }

    public void setImages(List<ImageData> images) {
        this.images = images;
    }
    
    public List<ImageTag> getBaseTags() {
        return new ArrayList<ImageTag>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCharacterBaseId() {
        return characterBaseId;
    }

    public void setCharacterBaseId(String characterBaseId) {
        this.characterBaseId = characterBaseId;
    }

    public List<String> getEncounterTexts() {
        if (encounterTexts == null) {
            encounterTexts = new ArrayList<String>();
        }
        return encounterTexts;
    }

    public void setEncounterTexts(List<String> encounterTexts) {
        this.encounterTexts = encounterTexts;
    }

    public List<String> getRapeTexts() {
        if (rapeTexts == null) {
            rapeTexts = new ArrayList<String>();
        }
        return rapeTexts;
    }

    public void setRapeTexts(List<String> rapeTexts) {
        this.rapeTexts = rapeTexts;
    }

    
    
}
