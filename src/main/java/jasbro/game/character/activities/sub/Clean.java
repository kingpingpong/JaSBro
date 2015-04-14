package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Clean extends RunningActivity {
    
    private final static float BASEMODIFICATION = 1f;
    private final static float OBEDIENCEMODIFICATION = 0.01f;
    private float dirtModification = -1;
    
    
    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        float modification = BASEMODIFICATION;
        
        modifications.add(new ModificationData(TargetType.ALL, modification, SpecializationAttribute.CLEANING));
        modifications.add(new ModificationData(TargetType.SLAVE, OBEDIENCEMODIFICATION, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new ModificationData(TargetType.TRAINER, -0.05f, BaseAttributeTypes.COMMAND));
        modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
        return modifications;
    }
    
    @Override
    public void init() {
    	dirtModification = -20 - 1 * (getCharacters().get(0).getFinalValue(
    			SpecializationAttribute.CLEANING) / 2);
    }
    
    @Override
    public void perform() {
    	House house = ((Room)getCharacterLocation()).getHouse();
    	house.modDirt((int) dirtModification);
    }
    
    @Override
    public MessageData getBaseMessage() {
        Charakter character = getCharacters().get(0);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEAN, character);;
        String message = TextUtil.t("clean.basic", character);
        return new MessageData(message, image, null);
    }

    
    
    public float getDirtModification() {
        return dirtModification;
    }

    public void setDirtModification(float dirtModification) {
        this.dirtModification = dirtModification;
    }
}
