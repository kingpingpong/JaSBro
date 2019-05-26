package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Read extends RunningActivity {
	private MessageData message;
	private Charakter character;
	
	@Override
	public void init() {
		character = getCharacter();
	}
	
	
	@Override
	public MessageData getBaseMessage() {
		if (message == null) {
			List<ImageTag> tags = character.getBaseTags();
			tags.add(0, ImageTag.STUDY);
			tags.add(1, ImageTag.CLOTHED);
			message = new MessageData(TextUtil.t("read.basic", character), ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()), 
					getCharacterLocation().getImage());
			message.addToMessage("\n\n");
			if(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)<5){message.addToMessage(TextUtil.t("read.toolow"+Util.getInt(1, 3), character));}
			else if(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)<10){message.addToMessage(TextUtil.t("read.easy"+Util.getInt(1, 3), character));}
			else if(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)<25){message.addToMessage(TextUtil.t("read.normal"+Util.getInt(1, 3), character));}
			else if(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)<50){message.addToMessage(TextUtil.t("read.hard"+Util.getInt(1, 3), character));}
			else{message.addToMessage(TextUtil.t("read.lunatic"+Util.getInt(0, 2), character));}
			
		}
		return message;
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, 10, EssentialAttributes.ENERGY));
		if(character.getTraits().contains(Trait.CLEVER))
			modifications.add(new ModificationData(TargetType.ALL, 0.8f, EssentialAttributes.MOTIVATION));
		else if(character.getTraits().contains(Trait.STUPID))
			modifications.add(new ModificationData(TargetType.ALL, -0.2f, EssentialAttributes.MOTIVATION));
		else
			modifications.add(new ModificationData(TargetType.ALL, 0.5f, EssentialAttributes.MOTIVATION));
		if(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)<5){modifications.add(new ModificationData(TargetType.ALL, 0.05f, BaseAttributeTypes.INTELLIGENCE));}
		else if(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)<10){modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.INTELLIGENCE));}
		else if(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)<25){modifications.add(new ModificationData(TargetType.ALL, 0.2f, BaseAttributeTypes.INTELLIGENCE));}
		else if(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)<50){modifications.add(new ModificationData(TargetType.ALL, 0.4f, BaseAttributeTypes.INTELLIGENCE));}
		else{modifications.add(new ModificationData(TargetType.ALL, 0.6f, BaseAttributeTypes.INTELLIGENCE));}
		
		return modifications;
	}
	
}