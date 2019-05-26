package jasbro.game.world.customContent.requirements;

import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class OrRequirement extends TriggerRequirementContainer {
	
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		for (TriggerRequirement ar : getSubRequirements()) {
			if (ar.isValid(triggerParent)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.ORREQUIREMENT;
	}
}