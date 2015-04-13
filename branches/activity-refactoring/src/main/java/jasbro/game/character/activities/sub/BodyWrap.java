package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
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

import java.util.ArrayList;
import java.util.List;

public class BodyWrap extends RunningActivity {
    private Charakter nurse;

    @Override
    public void init() {
        for (Charakter character : getCharacters()) {
            if (character.getSpecializations().contains(SpecializationType.NURSE) && character.getTraits().contains(Trait.BEAUTICIAN)) {
                if (nurse == null
                        || character.getFinalValue(SpecializationAttribute.WELLNESS) > nurse
                                .getFinalValue(SpecializationAttribute.WELLNESS)) {
                    nurse = character;
                }
            }
        }
    }

    @Override
    public MessageData getBaseMessage() {
        String message = TextUtil.t("bodywrap.basic", nurse);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.NURSE, nurse);
        return new MessageData(message, image, getCharacter().getBackground());
    }

    @Override
    public List<ModificationData> getStatModifications() {
    	int skill=nurse.getFinalValue(SpecializationAttribute.WELLNESS);
        modifyIncome(-30 * getCharacters().size());
        
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        modifications.add(new ModificationData(TargetType.SINGLE, nurse, 1.5f, SpecializationAttribute.WELLNESS));
        modifications.add(new ModificationData(TargetType.SINGLE, nurse, -25, EssentialAttributes.ENERGY));

        for (Charakter character : getCharacters()) {
            if (character != nurse) {
                modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f+(skill/100), BaseAttributeTypes.CHARISMA));
                modifications.add(new ModificationData(TargetType.SINGLE, character, -10, EssentialAttributes.ENERGY));
            }
        }
        return modifications;
    }
}
