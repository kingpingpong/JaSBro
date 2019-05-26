package jasbro.game.world.customContent.requirements;

import jasbro.game.character.activities.ActivityType;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class ActivityRequirement extends TriggerRequirement {
	private ActivityType activityType;
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		return triggerParent.getActivity().getType() == activityType;
	}
	
	public ActivityType getActivityType() {
		return activityType;
	}
	
	public void setActivityType(ActivityType activityType) {
		this.activityType = activityType;
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.ACTIVITYREQUIREMENT;
	}
	
	
}