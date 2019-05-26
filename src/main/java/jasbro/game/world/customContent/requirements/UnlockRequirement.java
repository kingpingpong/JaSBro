package jasbro.game.world.customContent.requirements;

import jasbro.Jasbro;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class UnlockRequirement extends TriggerRequirement {
	private UnlockObject unlockObject;
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		if (unlockObject == null) {
			return false;
		}
		if (!unlockObject.isLocked()) {
			return true;
		}
		else {
			return Jasbro.getInstance().getData().getUnlocks().getUnlockedObjects().contains(unlockObject);
		}
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.UNLOCKREQUIREMENT;
	}
	
	public UnlockObject getUnlockObject() {
		return unlockObject;
	}
	
	public void setUnlockObject(UnlockObject unlockObject) {
		this.unlockObject = unlockObject;
	}
}