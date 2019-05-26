package jasbro.game.world.customContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bsh.EvalError;

public abstract class WorldEventEffect implements Serializable {
	
	public abstract void perform(WorldEvent worldEvent) throws EvalError;
	
	public abstract WorldEventEffectType getType();
	
	public List<WorldEventEffect> getSubEffects() {
		return new ArrayList<WorldEventEffect>();
	}
	
	public String getName() {
		return getType().getText();
	}
	
	public boolean canAddSubEffect() {
		return false;
	}
	
}