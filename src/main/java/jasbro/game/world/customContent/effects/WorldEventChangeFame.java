package jasbro.game.world.customContent.effects;

import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEvent.WorldEventVariables;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import bsh.EvalError;

public class WorldEventChangeFame extends WorldEventEffect {
	private int value = 0;
	private String target = WorldEventVariables.character.toString();
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		Charakter character = (Charakter)worldEvent.getAttribute(target);
		character.getFame().modifyFame(value);
	}
	
	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.CHANGEFAME;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	
}