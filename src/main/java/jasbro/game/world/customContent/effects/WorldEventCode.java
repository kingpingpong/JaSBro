package jasbro.game.world.customContent.effects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bsh.EvalError;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;

public class WorldEventCode extends WorldEventEffect {
	private final static Logger log = LogManager.getLogger(WorldEventCode.class);
	private String code;
	private int displayHeight;
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		if (code != null && !code.equals("")) {
			try {
				worldEvent.getInterpreter().eval(code);
			} catch (EvalError e) {
				log.error("({}} Error in this code: {}", worldEvent.getId(), code);
				throw log.throwing(e);
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