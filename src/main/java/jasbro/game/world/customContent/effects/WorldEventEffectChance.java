package jasbro.game.world.customContent.effects;

import jasbro.Util;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

import java.util.ArrayList;
import java.util.List;

import bsh.EvalError;

public class WorldEventEffectChance extends WorldEventEffect {
	private List<WorldEventEffect> subEffects = new ArrayList<WorldEventEffect>();
	private int chance;
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		if (Util.getInt(0, 100) < chance) {
			for (WorldEventEffect itemEffect : subEffects) {
				itemEffect.perform(worldEvent);
			}
		}
	}
	
	public void applyOverride(WorldEvent worldEvent) throws EvalError {
		for (WorldEventEffect itemEffect : subEffects) {
			itemEffect.perform(worldEvent);
		}
	}	

	public void addEffect(WorldEventEffect itemEffect) {
		subEffects.add(itemEffect);
	}

	public List<WorldEventEffect> getSubEffects() {
		return subEffects;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.EFFECTCHANCE;
	}
}
