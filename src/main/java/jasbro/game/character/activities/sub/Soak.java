package jasbro.game.character.activities.sub;

import jasbro.Util;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.Buff.RoughenedUp;
import jasbro.game.character.conditions.Buff.Sore;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Soak extends RunningActivity {
	
	@Override
	public MessageData getBaseMessage() {
		String message = TextUtil.t("soak.basic", getCharacter());
		if(Util.getInt(0, 3)==2){
			getCharacter().addCondition(new Buff.SmoothSkin(4,getCharacter()));
		}

		
		
		return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SWIM, getCharacter()),
				getCharacterLocation().getImage());
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, 1.3f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.ALL, 20, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
		return modifications;
	}
}