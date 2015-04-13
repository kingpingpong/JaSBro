package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Pamper extends RunningActivity {
    private Charakter nurse;

    @Override
    public void init() {
        for (Charakter character : getCharacters()) {
            if (character.getSpecializations().contains(SpecializationType.NURSE)) {
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
        String message = TextUtil.t("pamper.basic", nurse);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.NURSE, nurse);
        return new MessageData(message, image, getCharacter().getBackground());
    }

    @Override
    public List<ModificationData> getStatModifications() {
        modifyIncome(-10 * getCharacters().size());

        List<ModificationData> modifications = new ArrayList<ModificationData>();
        modifications.add(new ModificationData(TargetType.SINGLE, nurse, 1.5f, SpecializationAttribute.WELLNESS));
        modifications.add(new ModificationData(TargetType.SINGLE, nurse, -15, EssentialAttributes.ENERGY));

        for (Charakter character : getCharacters()) {
            if (character != nurse) {
                modifications.add(new ModificationData(TargetType.SINGLE, character, 10, EssentialAttributes.ENERGY));
                modifications.add(new ModificationData(TargetType.SINGLE, character, 5, EssentialAttributes.HEALTH));
                character.addCondition(new Buff.Pretty(nurse,character));
            }
        }
        return modifications;
    }
}
