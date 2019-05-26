package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.RunningActivity.ModificationData;
import jasbro.game.character.activities.RunningActivity.TargetType;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

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
		modifications.add(new ModificationData(TargetType.SINGLE, nurse, 0.5f, SpecializationAttribute.MEDICALKNOWLEDGE));
		modifications.add(new ModificationData(TargetType.SINGLE, nurse, 0.05f, BaseAttributeTypes.INTELLIGENCE));        
		modifications.add(new ModificationData(TargetType.SINGLE, nurse, -25, EssentialAttributes.ENERGY));
		if(nurse.getTraits().contains(Trait.MAGICALHEALING))
			modifications.add(new ModificationData(TargetType.SINGLE, nurse, 0.25f, SpecializationAttribute.MAGIC));
		if(nurse.getTraits().contains(Trait.ALTRUISTIC))
			modifications.add(new ModificationData(TargetType.SINGLE, nurse, 0.4f, EssentialAttributes.MOTIVATION));
		else
			modifications.add(new ModificationData(TargetType.SINGLE, nurse, -0.5f, EssentialAttributes.MOTIVATION));
		if(nurse.getTraits().contains(Trait.BENEVOLENT))
			modifications.add(new ModificationData(TargetType.ALL, 0.2f, EssentialAttributes.MOTIVATION));
		for (Charakter character : getCharacters()) {
			if (character != nurse) {
				modifications.add(new ModificationData(TargetType.SINGLE, character, 40, EssentialAttributes.ENERGY));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 15 + nurse.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)/2, 
						EssentialAttributes.HEALTH));
				if ("SPAAREA".equals(getRoom().getRoomInfo().getId())) {
					if (nurse.getTraits().contains(Trait.AQUATICNURSE))
						modifications.add(new ModificationData(TargetType.SINGLE, character, 5 + nurse.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) / 10, 
								EssentialAttributes.HEALTH));
				}
			}
		}
		
		return modifications;
	}
	
	public Charakter getNurse() {
		return nurse;
	}
}