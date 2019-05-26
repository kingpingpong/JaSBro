package jasbro.game.world.customContent.requirements;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class AnyOfOwnedCharactersRequirement extends TriggerRequirementContainer {
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		for (Charakter c : Jasbro.getInstance().getData().getCharacters()) {
			if (((CharacterRequirement)getSubRequirements().get(0)).isValid(c, triggerParent)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
		if (triggerRequirement instanceof CharacterRequirement && getSubRequirements().size() < 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.ANYOFOWNEDCHARACTERSREQUIREMENT;
	}
}