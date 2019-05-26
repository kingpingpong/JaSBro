package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Walk extends RunningActivity {
	
	
	
	
	@Override
	public MessageData getBaseMessage() {
		StringBuilder builder = new StringBuilder(TextUtil.t("walk.basic", getCharacters()));
		builder.append(" ");
		
		List<ImageData> images = new ArrayList<ImageData>();
		for (Charakter character : getCharacters()) {
			images.addAll(character.getImages());
		}
		
		return new MessageData(builder.toString(), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, images), getCharacterLocation().getImage());
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		
		List<ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, 8.0f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.ALL, -5.0f, EssentialAttributes.ENERGY));
		
		
		return modifications;
	}
}