package jasbro.game.character.activities.sub;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Swim extends RunningActivity {

	@Override
	public MessageData getBaseMessage() {
		String message = TextUtil.t("swim.basic", getCharacter());
		
		return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SWIM, getCharacter()),
				getCharacterLocation().getImage());
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        modifications.add(new ModificationData(TargetType.ALL, -25, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.ALL, 0.2f, BaseAttributeTypes.CHARISMA));
        modifications.add(new ModificationData(TargetType.ALL, 0.3f, BaseAttributeTypes.STAMINA));
        modifications.add(new ModificationData(TargetType.ALL, 0.5f, BaseAttributeTypes.STRENGTH));
        return modifications;
	}
}
