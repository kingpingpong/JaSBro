package jasbro.game.world.customContent.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class MinimumCharactersMatchRequirement extends TriggerRequirementContainer {

	private int minimum;

	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		int count = 0;
		for(Charakter c : triggerParent.getCharacters()) {
			if(((CharacterRequirement)getSubRequirements().get(0)).isValid(c, triggerParent)) {
				++count;
			}
		}
		
		return count >= minimum;
	}


    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
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
        return TriggerRequirementType.MINIMUMCHARACTERSMATCHREQUIREMENT;
    }
}
