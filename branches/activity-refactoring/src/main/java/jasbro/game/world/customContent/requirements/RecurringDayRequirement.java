package jasbro.game.world.customContent.requirements;

import jasbro.Jasbro;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class RecurringDayRequirement extends TriggerRequirement {
    private int everyXDays;
    private int offset;
    
    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        if (everyXDays == 0) {
            return false;
        }
        int currentDay = Jasbro.getInstance().getData().getDay();
        if (currentDay == 0) {
            return false;
        }
        if (currentDay < offset / everyXDays) {
            return false;
        }
        else {
            return currentDay % everyXDays == (offset % everyXDays);
        }
    }
    
    public int getEveryXDays() {
        return everyXDays;
    }
    public void setEveryXDays(int everyXDays) {
        this.everyXDays = everyXDays;
    }
    public int getOffset() {
        return offset;
    }
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.RECURRINGDAYREQUIREMENT;
    }
}
