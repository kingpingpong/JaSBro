package jasbro.game.world.customContent.requirements;

import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class CharacterTypeRequirement extends TriggerRequirement implements CharacterRequirement {

	private CharacterType characerType;


	@Override
	public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
	    if (characerType == null) {
	        return true;
	    }
		return character.getType().equals(characerType);
	}

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        if (triggerParent.getCharacters() == null || triggerParent.getCharacters().size() == 0) {
            return false;
        }
        return isValid(triggerParent.getCharacters().get(0), triggerParent);
    }

    public CharacterType getCharacterType() {
        return characerType;
    }

    public void setCharacterType(CharacterType characerType) {
        this.characerType = characerType;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.CHARACTERTYPEREQUIREMENT;
    }
}
