package jasbro.game.events.rooms;

import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

/**
 * This will get generalized for parsing eventually.
 * @author somextra
 *
 */
public class EmptyRoomEventHandler extends AbstractRoomEventHandler {
	
	public EmptyRoomEventHandler() {
		setHandledType(EventType.ACTIVITYPERFORMED);
	}
	
	@Override
	protected void handleEventInternal(MyEvent event) {
		RunningActivity activity = (RunningActivity) event.getSource();
		if (activity.getPlannedActivity().getType() == ActivityType.SLEEP) {
			for (AttributeModification attributeModification : activity.getAttributeModifications()) {
				if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
					attributeModification.setBaseAmount(attributeModification.getBaseAmount() / 2);
				}
			}
		}
		else if (activity.getPlannedActivity().getType() == ActivityType.WHORE) {
			activity.setIncome(activity.getIncome() / 2);
		}
	}

}
