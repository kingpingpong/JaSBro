package jasbro.game.world.customContent.requirements;

import jasbro.game.world.customContent.TriggerParent;

import java.util.ArrayList;
import java.util.List;

import bsh.EvalError;

public abstract class TriggerRequirement {
	public abstract boolean isValid(TriggerParent triggerParent) throws EvalError;
	
	public boolean canAddRequirement(TriggerRequirement triggerRequirement) {
	    return false;
	}
	
	public List<TriggerRequirement> getSubRequirements() {
	    return new ArrayList<TriggerRequirement>();
	}
	
	public abstract TriggerRequirementType getType();
	
    public static enum Comparison {
        GREATERTHAN(">"), LESSTHAN("<"), EQUAL("=");
        
        private String displayName;

        private Comparison(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
