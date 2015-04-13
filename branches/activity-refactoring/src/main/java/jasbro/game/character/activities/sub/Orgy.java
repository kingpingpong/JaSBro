package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orgy extends RunningActivity {
	private Map<Gender, Integer> genderAmounts = new HashMap<Gender, Integer>();
	private boolean threesome = false;
	
	@Override
	public void init() {
		for (Gender gender : Gender.values()) {
			genderAmounts.put(gender, 0);
		}
		
		for (Charakter character : getCharacters()) {
			Gender curGender = character.getGender();
			genderAmounts.put(curGender, genderAmounts.get(curGender) + 1);
		}
		
		if (getCharacters().size() == 3) {
			threesome = true;
		}
	}
	
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
		if (threesome) {
			modifications.add(new ModificationData(TargetType.ALL, 1.2f, Sextype.VAGINAL));
			modifications.add(new ModificationData(TargetType.ALL, 1.2f, Sextype.ANAL));
			modifications.add(new ModificationData(TargetType.ALL, 1.2f, Sextype.ORAL));
			modifications.add(new ModificationData(TargetType.ALL, 1.2f, Sextype.TITFUCK));
			modifications.add(new ModificationData(TargetType.ALL, 1.2f, Sextype.FOREPLAY));		
			modifications.add(new ModificationData(TargetType.ALL, 1.2f, Sextype.GROUP));
			
			modifications.add(new ModificationData(TargetType.ALL, 0.04f, BaseAttributeTypes.STAMINA));
			modifications.add(new ModificationData(TargetType.ALL, -25, EssentialAttributes.ENERGY));
		}
		else {
            modifications.add(new ModificationData(TargetType.ALL, 2.5f, Sextype.VAGINAL));
            modifications.add(new ModificationData(TargetType.ALL, 2.5f, Sextype.ANAL));
            modifications.add(new ModificationData(TargetType.ALL, 2.5f, Sextype.ORAL));
            modifications.add(new ModificationData(TargetType.ALL, 2.5f, Sextype.TITFUCK));
            modifications.add(new ModificationData(TargetType.ALL, 2.5f, Sextype.FOREPLAY));            
			modifications.add(new ModificationData(TargetType.ALL, 2.5f, Sextype.GROUP));
			
			modifications.add(new ModificationData(TargetType.ALL, 0.01f, BaseAttributeTypes.STRENGTH));
			modifications.add(new ModificationData(TargetType.ALL, 0.01f, BaseAttributeTypes.STAMINA));
			modifications.add(new ModificationData(TargetType.ALL, -45, EssentialAttributes.ENERGY));
		}
		modifications.add(new ModificationData(TargetType.ALL, 0.01f, BaseAttributeTypes.OBEDIENCE));
		return modifications;
	}
	
	@Override
	public MessageData getBaseMessage() {
		List<Charakter> characters = getCharacters();
		List<ImageData> images = new ArrayList<ImageData>();
        for (Charakter character : characters) {
        	images.addAll(character.getImages());
        }
        List<ImageTag> tags = new ArrayList<ImageTag>();
        tags.addAll(ImageTag.getAssociatedImageTags(characters.toArray(new Person[characters.size()])));
        
        ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, images);
        String message;
        if (threesome) {
        	message = TextUtil.t("threesome.basic", getCharacters());
        }
        else {
        	message = TextUtil.t("orgy.basic", getCharacters());
        }
		return new MessageData(message, image, characters.get(0).getBackground());
	}
	
	@Override
	public Sextype getSextype() {
	    return Sextype.GROUP;
	}

	public Map<Gender, Integer> getGenderAmounts() {
		return genderAmounts;
	}


	public void setGenderAmounts(Map<Gender, Integer> genderAmounts) {
		this.genderAmounts = genderAmounts;
	}
}
