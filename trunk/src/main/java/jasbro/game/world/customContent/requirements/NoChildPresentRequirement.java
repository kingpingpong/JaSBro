package jasbro.game.world.customContent.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;

import java.util.List;

import bsh.EvalError;

public class NoChildPresentRequirement extends TriggerRequirement {


    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        List<Charakter> characters = triggerParent.getCharacters();
        if (characters == null) {
            return true;
        }
        for (Charakter character : characters) {
            if (character.getType().isChildType()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.NOCHILDPRESENTREQUIREMENT;
    }
}
