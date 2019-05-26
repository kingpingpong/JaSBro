package jasbro.game.world.customContent.effects;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import jasbro.game.world.customContent.requirements.AndRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import bsh.EvalError;

public class WorldEventCondition extends WorldEventEffectContainer {
	private TriggerRequirement requirement = new AndRequirement();
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		if (requirement == null) {
			return;
		}
		if (requirement.isValid(worldEvent)) {
			if (getSubEffects().size() > 0) {
				getSubEffects().get(0).perform(worldEvent);
			}
		}
		else {
			if (getSubEffects().size() > 1) {
				getSubEffects().get(1).perform(worldEvent);
			}
		}
		
	}
	
	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.CONDITION;
	}
	
	@Override
	public void addEffect(WorldEventEffect worldEventEffect) {
		if (getSubEffects().size() < 2) {
			super.addEffect(worldEventEffect);
		}
	}
	
	public boolean canAddSubEffect() {
		if (getSubEffects().size() < 2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public TriggerRequirement getRequirement() {
		return requirement;
	}
	
	public void setRequirement(TriggerRequirement requirement) {
		this.requirement = requirement;
	}
	
	
}