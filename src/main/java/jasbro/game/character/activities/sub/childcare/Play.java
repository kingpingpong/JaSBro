package jasbro.game.character.activities.sub.childcare;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Play extends RunningActivity {
	
	@Override
	public MessageData getBaseMessage() {
		MessageData messageData = new MessageData();
		for (Charakter character : getCharacters()) {
			messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.PLAY, character));
		}
		Object[] arguments = {getCharacterLocation()};
		if (getHouse() != null) {
			messageData.addToMessage(TextUtil.t("play.basic.room", getCharacters(), arguments));
		}
		else {
			messageData.addToMessage(TextUtil.t("play.basic.outside", getCharacters(), arguments));
		}
		return messageData;
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, -30, EssentialAttributes.ENERGY));
		return modifications;
	}
}