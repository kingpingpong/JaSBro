package jasbro.game.world.customContent.requirements;

import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class NotRequirement extends TriggerRequirementContainer {
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		if (getSubRequirements().size() < 1) {
			return true;
		}
		else {
			return !getSubRequirements().get(0).isValid(triggerParent);
		}
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.NOTREQUIREMENT;
	}
	
	@Override
	public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
		return (getSubRequirements().size() < 1);
	}
}