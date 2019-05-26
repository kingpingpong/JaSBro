package jasbro.game.world.customContent.requirements;

import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class MaximumCharacterAmountRequirement extends TriggerRequirement {
	
	private int maximum;
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		return triggerParent.getCharacters().size() <= maximum;
	}
	
	public int getMaximum() {
		return maximum;
	}
	
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.MAXIMUMCHARACTERAMOUNTREQUIREMENT;
	}
}