package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Nurse extends RunningActivity {
	private Charakter nurse;
	
	@Override
	public void init() {
    	for (Charakter character : getCharacters()) {
    		if (character.getSpecializations().contains(SpecializationType.NURSE)) {
    			if (nurse == null || character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) > 
    				nurse.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)) {
    				nurse = character;
    			}
    		}
    	}
	}
	
    @Override
    public MessageData getBaseMessage() {
    	String message = TextUtil.t("nurse.basic", nurse);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.NURSE, nurse);
        MessageData messageData = new MessageData(message, image, getCharacter().getBackground());
        
        for (Charakter character : getCharacters()) {
            if (character != nurse) {
                List<ImageTag> tags = character.getBaseTags();
                tags.add(0, ImageTag.SLEEP);
                image = ImageUtil.getInstance().getImageDataByTags(tags, character.getImages());
                messageData.addImage(image);
            }
        }
        return messageData;
    }
    
    @Override
    public List<ModificationData> getStatModifications() {
    	modifyIncome(-10 * getCharacters().size());
    	
    	List<ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new ModificationData(TargetType.SINGLE, nurse, 1.5f, SpecializationAttribute.MEDICALKNOWLEDGE));
        modifications.add(new ModificationData(TargetType.SINGLE, nurse, 0.1f, BaseAttributeTypes.INTELLIGENCE));        
        modifications.add(new ModificationData(TargetType.SINGLE, nurse, -25, EssentialAttributes.ENERGY));
        
        for (Charakter character : getCharacters()) {
        	if (character != nurse) {
                modifications.add(new ModificationData(TargetType.SINGLE, character, 40, EssentialAttributes.ENERGY));
                modifications.add(new ModificationData(TargetType.SINGLE, character, 10 + nurse.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 8, 
                		EssentialAttributes.HEALTH));
        	}
        }        
       
    	return modifications;
    }

    public Charakter getNurse() {
        return nurse;
    }
}
