package jasbro.game.character.conditions;

import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

public class StartingAtTheBottom extends Condition {
	private int daysLeft = 20;
	
	@Override
	public void handleEvent(MyEvent e) {
		if (e.getType() == EventType.NEXTDAY) {
			daysLeft--;
			if (daysLeft < 1) {
				getCharacter().removeCondition(this);
			}
		}
		else if (e.getType() == EventType.ACTIVITY) {
			RunningActivity activity = (RunningActivity) e.getSource();
			if (activity.getType() == ActivityType.CLEAN || activity.getType() == ActivityType.COOK || 
					activity.getType() == ActivityType.SELLFOOD || activity.getType() == ActivityType.BARTEND) {
				for (AttributeModification attributeModification : activity.getAttributeModifications()) {
					if (attributeModification.getAttributeType() == BaseAttributeTypes.COMMAND && 
							attributeModification.getBaseAmount() < 0) {
						attributeModification.setCancelled(true);
						break;
					}
				}
			}
			else {
				for (AttributeModification attributeModification : activity.getAttributeModifications()) {
					if (attributeModification.getAttributeType() == BaseAttributeTypes.COMMAND && 
							attributeModification.getBaseAmount() < 0) {
						attributeModification.addModificator(- attributeModification.getBaseAmount() / 2);
						break;
					}
				}
			}
		}
	}
	
	@Override
	public String getName() {
		return TextUtil.t("conditions.startingAtTheBottom");
	}
	
	@Override
	public String getDescription() {
		Object arguments[] = {getName(), daysLeft};
		return TextUtil.t("conditions.startingAtTheBottom.description", arguments);
	}
	
	@Override
	public ImageData getIcon() {
		return new ImageData("images/icons/cent.png");
	}
}