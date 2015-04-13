package jasbro.game.world.customContent.requirements;

import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class LocationTypeRequirement extends TriggerRequirement {
    private LocationTypeInterface locationType;

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        LocationTypeInterface locationTypeInterface = triggerParent.getLocation();
        if (locationTypeInterface == null) {
            return false;
        }
        return locationTypeInterface == locationType;
    }

    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.LOCATIONTYPEREQUIREMENT;
    }

    public LocationTypeInterface getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationTypeInterface locationType) {
        this.locationType = locationType;
    }
}
