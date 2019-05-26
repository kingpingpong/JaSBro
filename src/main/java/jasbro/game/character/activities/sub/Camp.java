package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Illness.Flu;
import jasbro.game.events.MessageData;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Camp extends RunningActivity {
	@Override
	public MessageData getBaseMessage() {
		String message = TextUtil.t("camp", getCharacters());
		MessageData messageData = new MessageData(message, null, getCharacters().get(0).getBackground());
		
		for (Charakter character : getCharacters()) {
			List<ImageTag> tags = character.getBaseTags();
			tags.add(0, ImageTag.SLEEP);
			tags.add(1, ImageTag.CLEANED);
			ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, character.getImages());
			messageData.addImage(image);
		}
		return messageData;
	}
	
	@Override
	public void perform() {
		modifyIncome(-25 * getCharacters().size());
		for (Charakter character : getCharacters()) {
			if (Jasbro.getInstance().getData().getTime() == Time.NIGHT && Util.getInt(0, 100) < 5) {
				Flu flu = new Flu(false);
				character.addCondition(flu);
				getMessages().get(0).addToMessage("\n\n" + flu.getTextStart());
			}
		}
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, 40, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.HEALTH));
		modifications.add(new ModificationData(TargetType.ALL, Util.getInt(-2, 3)*0.2f, EssentialAttributes.MOTIVATION));
		return modifications;
	}
}