package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

public class WorldEventAbortActivity extends WorldEventEffect {
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		worldEvent.getActivity().setAbort(true);
	}
	
	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.ABORTACTIVITY;
	}
	
}