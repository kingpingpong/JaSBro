package jasbro.game.world.customContent.requirements;

import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.TriggerParent;
import jasbro.game.world.customContent.WorldEvent.WorldEventVariables;
import bsh.EvalError;

public class SameCharacterRequirement extends TriggerRequirement {
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		CustomQuest quest = triggerParent.getQuest();
		if (quest != null) {
			return quest.getVariable(WorldEventVariables.character.toString()) == triggerParent.getCharacter();
		}
		return false;
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.SAMECHARACTERREQUIREMENT;
	}
}