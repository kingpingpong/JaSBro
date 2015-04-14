package jasbro.game.character.activities.sub;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.SunEffect;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Sunbathe extends RunningActivity {

	@Override
	public MessageData getBaseMessage() {		
		String message = TextUtil.t("sunbathe.beach.basic", getCharacter());
		
		return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SUNBATHE, getCharacter()), 
				getCharacterLocation().getImage());
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.ALL, 0.4f, BaseAttributeTypes.CHARISMA));
        return modifications;
	}
	
	@Override
	public void perform() {
	    getCharacter().addCondition(new SunEffect());
	}
}
