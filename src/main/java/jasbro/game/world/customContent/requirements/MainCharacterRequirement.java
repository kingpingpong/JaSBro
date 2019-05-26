package jasbro.game.world.customContent.requirements;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class MainCharacterRequirement extends TriggerRequirementContainer implements CharacterRequirement {
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		if (getSubRequirements().size() < 1) {
			return false;
		}
		return ((CharacterRequirement)getSubRequirements().get(0)).isValid(Jasbro.getInstance().getData().getProtagonist(), 
				triggerParent);
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
		return TriggerRequirementType.MAINCHARACTERREQUIREMENT;
	}
	
	@Override
	public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
		return character == Jasbro.getInstance().getData().getProtagonist();
	}
}