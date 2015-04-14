package jasbro.game.world.customContent.effects;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEvent.WorldEventVariables;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import bsh.EvalError;

public class WorldEventRemoveCharacter extends WorldEventEffect {
    private String target = WorldEventVariables.character.toString();
    
    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        Charakter character = (Charakter)worldEvent.getAttribute(target);
        GameData data = Jasbro.getInstance().getData();
        data.getCharacters().remove(character);
        for (PlannedActivity activity : character.getActivities().values()) {
            if (activity != null) {
                activity.removeCharacter(character);
            }
        }
    }
    
    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.REMOVECHARACTER;
    }    
    
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
}
