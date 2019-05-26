package jasbro.game.character.activities.sub;

import java.util.List;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class RefusedToWork extends RunningActivity {
	private Charakter causedByCharacter;
	private ActivityType originalActivity;
	
	@Override
	public MessageData getBaseMessage() {
		MessageData messageData = new MessageData();
		messageData.setMessage(TextUtil.t("refusedtowork."+originalActivity.toString(), causedByCharacter));
		messageData.setBackground(causedByCharacter.getBackground());
		messageData.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, causedByCharacter));
		messageData.setPriorityMessage(true);
		return messageData;
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = super.getStatModifications();
		if (originalActivity != ActivityType.WHORE) {
			modifications.add(new ModificationData(TargetType.SINGLE, causedByCharacter, -0.25f, BaseAttributeTypes.OBEDIENCE));
		}
		else {
			modifications.add(new ModificationData(TargetType.SINGLE, causedByCharacter, -0.08f, BaseAttributeTypes.OBEDIENCE));
		}
		modifications.add(new ModificationData(TargetType.SINGLE, causedByCharacter, -0.3f, EssentialAttributes.MOTIVATION));
		return modifications;
	}
	
	public Charakter getCausedByCharacter() {
		return causedByCharacter;
	}
	
	public void setCausedByCharacter(Charakter causedByCharacter) {
		this.causedByCharacter = causedByCharacter;
	}
	
	public ActivityType getOriginalActivity() {
		return originalActivity;
	}
	
	public void setOriginalActivity(ActivityType activity) {
		this.originalActivity = activity;
	}
	
	
}