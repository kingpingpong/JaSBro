package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Govern extends RunningActivity {
    
    
    
    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.COMMAND));
        modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
       
        modifications.add(new ModificationData(TargetType.ALLHOUSE, 0.2f , BaseAttributeTypes.OBEDIENCE));

  
        return modifications;
    }
    
    @Override
    public MessageData getBaseMessage() {
        Charakter character = getCharacters().get(0);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character);;
        String message =  TextUtil.t("govern.basic", character);
        return new MessageData(message, image, null);
    }
}
