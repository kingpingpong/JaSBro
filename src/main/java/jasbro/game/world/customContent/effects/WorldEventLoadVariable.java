package jasbro.game.world.customContent.effects;

import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import bsh.EvalError;

public class WorldEventLoadVariable extends WorldEventEffect {
	private String source = "character";
	private String target = "character";
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		if (source != null && target != null) {
			worldEvent.putAttribute(target, ((CustomQuest) worldEvent.getQuest()).getVariable(source));
		}
	} 
	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.LOADVARIABLE;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
}