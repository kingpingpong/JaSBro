package jasbro.game.world.customContent.effects;

import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import bsh.EvalError;

public class WorldEventProtectCharacter extends WorldEventEffect {
    private String target = "character";

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        worldEvent.getProtectedCharacters().add((Charakter)worldEvent.getAttribute(target));
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.PROTECTCHARACTER;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
    
    
}
