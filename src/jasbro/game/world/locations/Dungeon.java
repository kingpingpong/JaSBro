package jasbro.game.world.locations;

import jasbro.Jasbro;
import jasbro.Util.TypeAmounts;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Dungeon extends OtherLocation {
    private transient Map<Time, ImageData> images;
    private LocationType type;

    public Dungeon() {
        Map<Time, PlannedActivity> roomUsageMap = getUsageMap();
        for (Time time : Time.values()) {
            roomUsageMap.put(time, new PlannedActivity(this, ActivityType.EXPLORE));
        }
    }

    @Override
    public ImageData getImage() {
        return getImages().get(Jasbro.getInstance().getData().getTime());
    }

    @Override
    public List<ActivityDetails> getPossibleActivities(Time time, TypeAmounts typeAmounts) {
        List<ActivityDetails> possibleActivities = new ArrayList<ActivityDetails>();
        possibleActivities.add(new ActivityDetails(ActivityType.EXPLORE));
        return possibleActivities;
    }

    private Map<Time, ImageData> getImages() {
        if (images == null) {
            images = new EnumMap<>(Time.class);

            images.put(Time.MORNING, new ImageData("images/backgrounds/dungeon/dungeon1.png"));
            images.put(Time.AFTERNOON, new ImageData("images/backgrounds/dungeon/dungeon1.png"));
            images.put(Time.NIGHT, new ImageData("images/backgrounds/dungeon/dungeon1.png"));
        }
        return images;
    }

    @Override
    public LocationTypeInterface getLocationType() {
        return type;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type.getText();
    }

    @Override
    public String getDescription() {
        return type.getDescription();
    }
    
    @Override
    public int getMaxPeople() {
        return 4;
    }
}
