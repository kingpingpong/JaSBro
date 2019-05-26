package jasbro.game.world.customContent.requirements;

import jasbro.Util;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class ChanceRequirement extends TriggerRequirement {
	private int chance = 100;
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		return Util.getInt(0, 100) < chance;
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.CHANCEREQUIREMENT;
	}
	
	public int getChance() {
		return chance;
	}
	
	public void setChance(int chance) {
		this.chance = chance;
	}
	
	
}