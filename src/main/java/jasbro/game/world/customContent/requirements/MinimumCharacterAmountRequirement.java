package jasbro.game.world.customContent.requirements;

import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class MinimumCharacterAmountRequirement extends TriggerRequirement {

	private int minimum;


	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		return triggerParent.getCharacters().size() >= minimum;
	}

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }
    
    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.MINIMUMCHARACTERAMOUNTREQUIREMENT;
    }
}
