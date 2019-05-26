package jasbro.game.world.customContent.requirements;

import jasbro.Jasbro;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class MoneyRequirement extends TriggerRequirement {
	private long money = 0;
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		return money <= Jasbro.getInstance().getData().getMoney();
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.MONEYREQUIREMENT;
	}
	
	public long getMoney() {
		return money;
	}
	
	public void setMoney(long money) {
		this.money = money;
	}
	
	
	
	
}