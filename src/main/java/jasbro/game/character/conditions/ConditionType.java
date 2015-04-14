package jasbro.game.character.conditions;

import jasbro.game.character.Condition;
import jasbro.game.character.conditions.Illness.Flu;
import jasbro.game.character.conditions.Illness.Smallpox;

public enum ConditionType {
	FLU(Flu.class), SMALLPOX(Smallpox.class), ILLNESS(Illness.class);
	
	private Class<? extends Condition> conditionClass;
	
	private ConditionType(Class<? extends Condition> conditionClass) {
		this.conditionClass = conditionClass;
	}
	
	public Class<? extends Condition> getConditionClass() {
		return conditionClass;
	}	
}
