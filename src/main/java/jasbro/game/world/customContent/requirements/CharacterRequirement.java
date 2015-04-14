package jasbro.game.world.customContent.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public interface CharacterRequirement {
	boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError;
}
