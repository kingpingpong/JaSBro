package jasbro.game.world.customContent.effects;

import jasbro.Jasbro;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import bsh.EvalError;

public class WorldEventChangeMoney extends WorldEventEffect {
	private long moneyModifier;
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		if (moneyModifier < 0) {
			Jasbro.getInstance().getData().spendMoney(-moneyModifier, null);
		}
		else {
			Jasbro.getInstance().getData().earnMoney(moneyModifier, null);
		}
	}
	
	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.CHANGEMONEY;
	}
	
	public long getMoneyModifier() {
		return moneyModifier;
	}
	
	public void setMoneyModifier(long moneyModifier) {
		this.moneyModifier = moneyModifier;
	}
	
	
}