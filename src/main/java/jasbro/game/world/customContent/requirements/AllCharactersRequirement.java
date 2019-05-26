package jasbro.game.world.customContent.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class AllCharactersRequirement extends TriggerRequirementContainer {
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		if (getSubRequirements().size() < 1 ||
				triggerParent.getCharacters() == null || 
				triggerParent.getCharacters().size() == 0) {
			return false;
		}
		for (Charakter c : triggerParent.getCharacters()) {
			if (!((CharacterRequirement)getSubRequirements().get(0)).isValid(c, triggerParent)) {
				return false;
			}
		}
		return true;
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
		return TriggerRequirementType.ALLCHARACTERSREQUIREMENT;
	}
}