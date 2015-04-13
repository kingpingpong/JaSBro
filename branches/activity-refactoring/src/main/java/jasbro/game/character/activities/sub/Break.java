package jasbro.game.character.activities.sub;

import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Break extends RunningActivity {
	private Charakter trainer;
	private Charakter slave;
	
	
    @Override
    public void init() {
    	if (getCharacters().get(0).getType() == CharacterType.TRAINER) {
    		trainer = getCharacters().get(0);
    		slave = getCharacters().get(1);
    	}
    	else {
    		trainer = getCharacters().get(1);
    		slave = getCharacters().get(0);
    	}
    }
	
    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        float obedienceMod = 0.05f;
        float modObLow = (12 - (slave.getObedience() * 1.5f)) / 15.0f;
        if (modObLow > 0) {
        	 obedienceMod += modObLow;
        }
        obedienceMod += trainer.getCommand() / 50.0;
        obedienceMod += trainer.getFinalValue(SpecializationAttribute.DOMINATE) / 100.0;
        
        modifications.add(new ModificationData(TargetType.SLAVE, obedienceMod, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new ModificationData(TargetType.TRAINER, 0.2f, BaseAttributeTypes.COMMAND));
        
        modifications.add(new ModificationData(TargetType.SLAVE, -30, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.SLAVE, -15, EssentialAttributes.HEALTH));
        modifications.add(new ModificationData(TargetType.TRAINER, -20, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.TRAINER, 1, SpecializationAttribute.DOMINATE));
        modifications.add(new ModificationData(TargetType.ALL, 0.5f, Sextype.BONDAGE));
        return modifications;
    }
    
    @Override
    public MessageData getBaseMessage() {
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BONDAGE, slave);;
        String message = TextUtil.t("break.basic", trainer, slave);
        return new MessageData(message, image, null);
    }
}
