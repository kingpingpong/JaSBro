package jasbro.game.world.customContent.requirements;

import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class AndRequirement extends TriggerRequirementContainer {
	
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		for (TriggerRequirement ar : getSubRequirements()) {
			if (!ar.isValid(triggerParent)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.ANDREQUIREMENT;
	}
}