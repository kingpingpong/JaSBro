package jasbro.game.world.locations;

import jasbro.Jasbro;
import jasbro.game.GameData;
import jasbro.game.interfaces.AreaInterface;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.List;

public class DivLocations implements AreaInterface {
	private LocationType locationTypes[] = {LocationType.BEACH, LocationType.RESTAURANT, LocationType.LIBRARY, LocationType.STREETS};
	
	@Override
	public String getName() {
		return "Locations";
	}
	
	@Override
	public List<CharacterLocation> getLocations() {
		return getLocationList();
	}
	
	@Override
	public ImageData getImage() {
		return new ImageData("images/backgrounds/sky.jpg");
	}
	
	@Override
	public int getLocationAmount() {
		return locationTypes.length;
	}
	
	public List<CharacterLocation> getLocationList() {
		List<CharacterLocation> characterLocations = new ArrayList<CharacterLocation>();
		GameData gameData = Jasbro.getInstance().getData();
		for (LocationType locationType : locationTypes) {
			if (gameData.getOtherLocationMap().containsKey(locationType)) {
				characterLocations.add(gameData.getOtherLocationMap().get(locationType));
			}
			else {
				CharacterLocation location = locationType.getLocation();
				gameData.getOtherLocationMap().put(locationType, location);
				characterLocations.add(location);
			}
		}
		return characterLocations;
	}
}