package jasbro.game.world.customContent.effects;

import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import bsh.EvalError;

public class WorldEventComment extends WorldEventEffect {
	private String comment;
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
	}
	
	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.COMMENT;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}