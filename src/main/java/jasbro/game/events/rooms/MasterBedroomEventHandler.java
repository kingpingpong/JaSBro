package jasbro.game.events.rooms;

import jasbro.Util;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

/**
 * Placeholder event hanlder for the master bedroom.
 * @author somextra
 *
 */
public class MasterBedroomEventHandler implements RoomEventHandler {
	
	@Override
	public void handleEvent(MyEvent event) {
		if (event.getType() == EventType.ACTIVITYPERFORMED) {
			RunningActivity activity = (RunningActivity) event.getSource();
			if (activity.getPlannedActivity().getType() == ActivityType.SLEEP) {
				for (AttributeModification attributeModification : activity.getAttributeModifications()) {
					if (attributeModification.getAttributeType() == EssentialAttributes.ENERGY) {
						attributeModification.setBaseAmount((float) Util.getPercent(
								attributeModification.getBaseAmount(), 120));
					}
				}
			}
			else if (activity.getPlannedActivity().getType() == ActivityType.WHORE) {
				activity.setIncome((int) Util.getPercent(activity.getIncome(), 120));
			}
		}
		else if (event.getType() == EventType.ACTIVITY) {
			RunningActivity activity = (RunningActivity) event.getSource();
			if (activity.getPlannedActivity().getType() == ActivityType.WHORE) {
				Whore whore = (Whore) activity;
				whore.getMainCustomer().addToSatisfaction(15, this);
			}
		}
	}
	
}