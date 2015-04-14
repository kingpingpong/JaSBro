package jasbro.game.world.locations;

import jasbro.Jasbro;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;

import java.util.EnumMap;
import java.util.Map;

public abstract class OtherLocation extends CharacterLocation {
    private Map<Time, PlannedActivity> usageMap = new EnumMap<>(Time.class);

    public PlannedActivity getCurrentUsage() {
        return getUsageMap().get(Jasbro.getInstance().getData().getTime());
    }
    
    public Map<Time, PlannedActivity> getUsageMap() {
        return usageMap;
    }
}
