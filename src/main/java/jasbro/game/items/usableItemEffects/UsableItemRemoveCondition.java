package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.conditions.ConditionType;
import jasbro.game.items.Item;

public class UsableItemRemoveCondition extends UsableItemEffect {
	private ConditionType conditionType;

	@Override
	public String getName() {
		return "Remove Condition";
	}

	@Override
	public void apply(Charakter character, Item item) {
		if (conditionType != null) {
			for (int i = 0; i < character.getConditions().size(); i++) {
				Condition condition = character.getConditions().get(i);
				if (conditionType.getConditionClass().isInstance(condition)) {
					character.getConditions().remove(condition);
					break;
				}
			}
		}
	}

	@Override
	public UsableItemEffectType getType() {
		return UsableItemEffectType.REMOVECONDITION;
	}

	public ConditionType getConditionType() {
		return conditionType;
	}

	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}
	
	
}
