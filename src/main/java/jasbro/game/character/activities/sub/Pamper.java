package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.RunningActivity.ModificationData;
import jasbro.game.character.activities.RunningActivity.TargetType;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Pamper extends RunningActivity {
	private Charakter nurse;
	
	@Override
	public void init() {
		for (Charakter character : getCharacters()) {
			if (character.getSpecializations().contains(SpecializationType.NURSE)) {
				if (nurse == null
						|| character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE) > nurse
						.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)) {
					nurse = character;
				}
			}
		}
	}
	
	@Override
	public MessageData getBaseMessage() {
		String message = TextUtil.t("pamper.basic", nurse);
		ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.NURSE, nurse);
		return new MessageData(message, image, getCharacter().getBackground());
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		modifyIncome(-10 * getCharacters().size());
		
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		if(nurse.getTraits().contains(Trait.BENEVOLENT))
			modifications.add(new ModificationData(TargetType.ALL, 0.2f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.SINGLE, nurse, 1.5f, SpecializationAttribute.MEDICALKNOWLEDGE));
		if(nurse.getTraits().contains(Trait.MAGICALHEALING))
			modifications.add(new ModificationData(TargetType.SINGLE, nurse, 0.75f, SpecializationAttribute.MAGIC));
		modifications.add(new ModificationData(TargetType.SINGLE, nurse, -15, EssentialAttributes.ENERGY));
		if(nurse.getTraits().contains(Trait.ALTRUISTIC))
			modifications.add(new ModificationData(TargetType.SINGLE, nurse, 0.6f, EssentialAttributes.MOTIVATION));
		else
			modifications.add(new ModificationData(TargetType.SINGLE, nurse, -0.6f, EssentialAttributes.MOTIVATION));
		
		for (Charakter character : getCharacters()) {
			if (character != nurse) {
				modifications.add(new ModificationData(TargetType.SINGLE, character, 10, EssentialAttributes.ENERGY));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 5, EssentialAttributes.HEALTH));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.0f, EssentialAttributes.MOTIVATION));
				character.addCondition(new Buff.Pretty(nurse,character));
			}
		}
		return modifications;
	}
}