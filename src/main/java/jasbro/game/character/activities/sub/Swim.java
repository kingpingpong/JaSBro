package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

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
		if(getCharacters().get(0).getTraits().contains(Trait.FIT))
			modifications.add(new ModificationData(TargetType.ALL, 8.0f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.ALL, -25, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
		modifications.add(new ModificationData(TargetType.ALL, 0.4f, BaseAttributeTypes.STAMINA));
		modifications.add(new ModificationData(TargetType.ALL, 0.2f, BaseAttributeTypes.STRENGTH));
		return modifications;
	}
}