package jasbro.game.world.customContent.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class FameRequirement extends TriggerRequirement implements CharacterRequirement {

    private long fameRequired;


    @Override
    public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
        return character.getFame().getFame() >= fameRequired;
    }

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        if (triggerParent.getCharacters() == null || triggerParent.getCharacters().size() == 0) {
            return false;
        }
        return isValid(triggerParent.getCharacters().get(0), triggerParent);
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.FAMEREQUIREMENT;
    }

    public long getFameRequired() {
        return fameRequired;
    }

    public void setFameRequired(long fameRequired) {
        this.fameRequired = fameRequired;
    }
    
    
}
