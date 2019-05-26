package jasbro.game.events.rooms;

import jasbro.Util;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class SickroomEventHandler extends AbstractRoomEventHandler {
	
	public SickroomEventHandler() {
		setHandledType(EventType.ACTIVITYPERFORMED);
	}
	
	@Override
	protected void handleEventInternal(final MyEvent event) {
		RunningActivity activity = (RunningActivity) event.getSource();
		for (AttributeModification attributeModification : activity.getAttributeModifications()) {
			if (attributeModification.getAttributeType() == EssentialAttributes.HEALTH) {
				attributeModification.setBaseAmount((float) Util.getPercent(attributeModification.getBaseAmount(), 200));
			}
		}
	}
	
}