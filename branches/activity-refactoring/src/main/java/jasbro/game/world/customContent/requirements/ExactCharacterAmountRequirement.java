package jasbro.game.world.customContent.requirements;

import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class ExactCharacterAmountRequirement extends TriggerRequirement {

	private int count;

	public ExactCharacterAmountRequirement(final int count) {
		this.count = count;
	}

	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		return triggerParent.getCharacters().size() == count;
	}

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.EXACTCHARACTERAMOUNTREQUIREMENT;
    }
}
