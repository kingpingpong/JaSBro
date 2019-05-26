package jasbro.game.world.locations;

import jasbro.Jasbro;
import jasbro.Util.TypeAmounts;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.interfaces.LocationTypeInterface;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Library extends OtherLocation {

	public Library() {
		Map<Time, PlannedActivity> roomUsageMap = getUsageMap();
		for (Time time : Time.values()) {
			roomUsageMap.put(time, new PlannedActivity(this, ActivityType.STUDY));
		}
	}

	@Override
	public ImageData getImage() {
		return new ImageData("images/backgrounds/library.jpg");
	}

	@Override
	public List<ActivityDetails> getPossibleActivities(Time time, TypeAmounts typeAmounts) {
		List<ActivityDetails> possibleActivities = new ArrayList<ActivityDetails>();
		possibleActivities.add(new ActivityDetails(ActivityType.STUDY));
		if(Jasbro.getInstance().getData().getTime()!=Time.NIGHT)
			possibleActivities.add(new ActivityDetails(ActivityType.WORKGUILD));

		//Slaves are not allowed in the library
		for (int i = 0; i < getCurrentUsage().getCharacters().size(); i++) {
			Charakter character = getCurrentUsage().getCharacters().get(i);
			if (character.getType() == CharacterType.SLAVE) {
				getCurrentUsage().removeCharacter(character);
				i--;
			}
		}

		return possibleActivities;
	}

	@Override
	public String getName() {
		return getType().getText();
	}

	@Override
	public String getDescription() {
		return getType().getDescription();
	}

	@Override
	public LocationTypeInterface getLocationType() {
		return getType();
	}

	public LocationType getType() {
		return LocationType.LIBRARY;
	}
}