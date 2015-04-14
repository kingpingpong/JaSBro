package jasbro.game.world.customContent.requirements;

import jasbro.Util.TypeAmounts;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class ChildCareRequirement extends TriggerRequirement {

	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
	    TypeAmounts typeAmounts = triggerParent.getTypeAmounts();
	    if (typeAmounts == null) {
	        return false;
	    }
		if (typeAmounts.getInfantAmount() > 0) {
			if (typeAmounts.getChildAmount() == 0 && typeAmounts.getTeenAmount() == 0 && typeAmounts.isAdultPresent()) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public TriggerRequirementType getType() {
	    return TriggerRequirementType.CHILDCAREREQUIREMENT;
	}
}
