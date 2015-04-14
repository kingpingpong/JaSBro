package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.items.Item;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextWrapper;

import org.stringtemplate.v4.ST;

public class UsableItemShowMessage extends UsableItemEffect {
	private String message;
	private ImageTag imageTag;
	
	@Override
	public String getName() {
		return "Show message";
	}

	@Override
	public void apply(Charakter character, Item item) {
		if (imageTag == null) {
			imageTag = ImageTag.STANDARD;
		}
		ST stringTemplate = new ST(message);
		if (character != null) {
			stringTemplate.add("name", character.getName());
			stringTemplate.add("type", character.getType().getText());
			TextWrapper textWrapper = new TextWrapper(character);
            stringTemplate.add("c", textWrapper);
		}		
		new MessageScreen(stringTemplate.render(), ImageUtil.getInstance().getImageDataByTag(imageTag, character),
				character.getBackground());
	}

	@Override
	public UsableItemEffectType getType() {
		return UsableItemEffectType.SHOWMESSAGE;
	}

	public String getMessage() {
		return message;
	}

	public ImageTag getImageTag() {
		return imageTag;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setImageTag(ImageTag imageTag) {
		this.imageTag = imageTag;
	}

	
}
