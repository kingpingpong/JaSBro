package jasbro.game.world.customContent.effects;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

import java.util.ArrayList;
import java.util.List;

import bsh.EvalError;

public class WorldEventEffectContainer extends WorldEventEffect {
	private List<WorldEventEffect> subEffects = new ArrayList<WorldEventEffect>();
	
	public void addEffect(WorldEventEffect worldEventEffect) {
		subEffects.add(worldEventEffect);
	}
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		for (WorldEventEffect eventEffect : subEffects) {
			((WorldEventEffect)eventEffect).perform(worldEvent);
		}
	}
	
	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.EFFECTCONTAINER;
	}
	
	public List<WorldEventEffect> getSubEffects() {
		return subEffects;
	}
	
	public boolean canAddSubEffect() {
		return true;
	}
	
}