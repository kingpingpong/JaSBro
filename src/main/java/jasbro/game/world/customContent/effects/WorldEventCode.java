package jasbro.game.world.customContent.effects;

import jasbro.MyException;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

import org.apache.log4j.Logger;

import bsh.EvalError;

public class WorldEventCode extends WorldEventEffect {
    private final static Logger log = Logger.getLogger(WorldEventCode.class);
    private String code;
    private int displayHeight;
    
    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        if (code != null && !code.equals("")) {
            try {
                worldEvent.getInterpreter().eval(code);
            } catch (EvalError e) {
                log.error("(" + worldEvent.getId() +") Error in this code: " + code);
                throw e;
            }
            catch (NoClassDefFoundError e) {
                log.error("(" + worldEvent.getId() +") Error in this code: " + code);
                throw new MyException("Custom code noclassDefFound", e);
            }
        }
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.CODE;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDisplayHeight() {
        if (displayHeight < 1) {
            displayHeight = 1;
        }
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }
    
    
}
