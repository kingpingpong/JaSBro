package jasbro.game.events.rooms;

import jasbro.Util;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Orgy;
import jasbro.game.character.activities.sub.Sex;
import jasbro.game.character.activities.sub.Train;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class ClassRoomEventHandler extends AbstractRoomEventHandler {
	
	public ClassRoomEventHandler() {
		setHandledType(EventType.ACTIVITYPERFORMED);
	}
	
	@Override
	protected void handleEventInternal(MyEvent event) {
		RunningActivity activity = (RunningActivity) event.getSource();
		if (activity instanceof Train) {
			for (AttributeModification attributeModification : activity.getAttributeModifications()) {
				if (!(attributeModification.getAttributeType() instanceof EssentialAttributes)) {//10 % increase to train / teach
					attributeModification.addModificator((float) Util.getPercent(attributeModification.getBaseAmount(), 10));
				}
			}
		}
		else if (activity instanceof Sex || activity instanceof Orgy) {
			for (AttributeModification attributeModification : activity.getAttributeModifications()) {
				if (!(attributeModification.getAttributeType() instanceof EssentialAttributes)) {//10 % decrease in attribute gain
					attributeModification.addModificator((float) Util.getPercent(attributeModification.getBaseAmount(), -10));
				}
				else if (attributeModification.getAttributeType() instanceof EssentialAttributes && 
						attributeModification.getBaseAmount() < 0) { //10% increase to health / energy loss
					attributeModification.addModificator((float) Util.getPercent(attributeModification.getBaseAmount(), 10));
				}
			}
		}
	}
	
}