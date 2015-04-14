package jasbro.game.world.customContent.requirements;

import jasbro.Jasbro;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class DayRequirement extends TriggerRequirement {
    private int day;
    private Comparison dayComparison = Comparison.GREATERTHAN;
    
    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        int currentDay = Jasbro.getInstance().getData().getDay();
        switch(dayComparison) {
            case GREATERTHAN:
                return currentDay > day;
            case LESSTHAN:
                return currentDay < day;
            case EQUAL:
                return currentDay == day;
        }
        return false;
    }
    
    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.DAYREQUIREMENT;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Comparison getDayComparison() {
        return dayComparison;
    }

    public void setDayComparison(Comparison dayComparison) {
        this.dayComparison = dayComparison;
    }
    
    
}
