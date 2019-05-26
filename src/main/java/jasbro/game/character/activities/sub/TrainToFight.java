package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.Jasbro;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class TrainToFight extends RunningActivity {
	
	@Override
	public MessageData getBaseMessage() {
		String message = TextUtil.t("traintofight.basic", getCharacter());
		ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, getCharacter());
		return new MessageData(message, image, getCharacter().getBackground());
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		if(Jasbro.getInstance().getData().getDay()%3==0)
			modifications.add(new ModificationData(TargetType.ALL, -3.0f, EssentialAttributes.MOTIVATION));
		else
			modifications.add(new ModificationData(TargetType.ALL, 0.8f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.ALL, -30, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 0.3f, BaseAttributeTypes.STRENGTH));
		modifications.add(new ModificationData(TargetType.ALL, 0.2f, BaseAttributeTypes.STAMINA));
		modifications.add(new ModificationData(TargetType.ALL, 0.7f, SpecializationAttribute.VETERAN));
		return modifications;
	}
	
}