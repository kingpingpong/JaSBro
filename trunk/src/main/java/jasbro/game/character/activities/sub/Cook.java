package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Cook extends RunningActivity {
    
    private final static float BASEMODIFICATION = 1f;
    private final static float OBEDIENCEMODIFICATION = 0.01f;
    
    
    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        float modification = BASEMODIFICATION;

        modifications.add(new ModificationData(TargetType.ALL, modification, SpecializationAttribute.COOKING));
        modifications.add(new ModificationData(TargetType.ALL, OBEDIENCEMODIFICATION, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
       
        modifications.add(new ModificationData(TargetType.ALLHOUSE, 0.8f + 
        		getCharacter().getAttribute(SpecializationAttribute.COOKING).getValue() / 50.0f, EssentialAttributes.HEALTH));
        modifications.add(new ModificationData(TargetType.ALLHOUSE, 4 + 
        		getCharacter().getAttribute(SpecializationAttribute.COOKING).getValue() / 30.0f, EssentialAttributes.ENERGY));
  
        return modifications;
    }
    
    @Override
    public MessageData getBaseMessage() {
        Charakter character = getCharacters().get(0);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, character);;
        String message =  TextUtil.t("cook.basic", character);
        return new MessageData(message, image, null);
    }
}
