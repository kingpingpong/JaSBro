package jasbro.game.world.customContent.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class SpecializationRequirement extends TriggerRequirement implements CharacterRequirement {
	
	private SpecializationType specialization;

	@Override
	public boolean isValid(Charakter character, TriggerParent triggerParent) {
		return character.getSpecializations().contains(specialization);
	}

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        return isValid(triggerParent.getCharacter(), triggerParent);
    }

    public SpecializationType getSpecialization() {
        return specialization;
    }

    public void setSpecialization(SpecializationType specialization) {
        this.specialization = specialization;
    }
    
    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.SPECIALIZATIONREQUIREMENT;
    }
}
