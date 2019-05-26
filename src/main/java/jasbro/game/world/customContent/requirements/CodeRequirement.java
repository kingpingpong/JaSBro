package jasbro.game.world.customContent.requirements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bsh.EvalError;
import jasbro.game.world.customContent.TriggerParent;

public class CodeRequirement extends TriggerRequirement {
	private final static Logger log = LogManager.getLogger(CodeRequirement.class);
	private String code;
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		if (code != null && !code.equals("")) {
			try {
				return (Boolean) triggerParent.getInterpreter().eval(code);
			} catch (EvalError e) {
				log.error("Error in this code: " + code);
				throw e;
			}
		}
		return false;
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.CODEREQUIREMENT;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
	
}