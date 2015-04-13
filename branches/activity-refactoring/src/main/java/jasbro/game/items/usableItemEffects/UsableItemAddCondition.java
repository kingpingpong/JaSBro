package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.character.conditions.ConditionType;
import jasbro.game.items.Item;

public class UsableItemAddCondition extends UsableItemEffect {
	private ConditionType conditionType;

	@Override
	public String getName() {
		return "Add Condition";
	}

	@Override
	public void apply(Charakter character, Item item) {
		if (conditionType != null) {
			try {
				character.addCondition(conditionType.getConditionClass().newInstance());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public UsableItemEffectType getType() {
		return UsableItemEffectType.ADDCONDITION;
	}

	public ConditionType getConditionType() {
		return conditionType;
	}

	public void setConditionType(ConditionType conditionType) {
		this.conditionType = conditionType;
	}
	
	
}
