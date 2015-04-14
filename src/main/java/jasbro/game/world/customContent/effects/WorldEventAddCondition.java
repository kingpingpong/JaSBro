package jasbro.game.world.customContent.effects;

import jasbro.MyException;
import jasbro.game.character.Condition;
import jasbro.game.character.conditions.ConditionType;
import jasbro.game.character.conditions.Illness.Smallpox;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import bsh.EvalError;

public class WorldEventAddCondition extends WorldEventEffect {
    private ConditionType conditionType;

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        if (conditionType != null) {
            try {
                Condition condition = conditionType.getConditionClass().newInstance();
                if (conditionType == ConditionType.SMALLPOX) {
                    ((Smallpox)condition).setShowMessage(false); //Messes up message order
                }
                worldEvent.getCharacters().get(0).addCondition(condition);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new MyException("Failed creating condition", e);
            }
        }
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.ADDCONDITION;
    }
    
    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }
}
