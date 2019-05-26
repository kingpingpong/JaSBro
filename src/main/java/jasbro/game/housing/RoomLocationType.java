package jasbro.game.housing;

import jasbro.game.interfaces.LocationTypeInterface;

public class RoomLocationType implements LocationTypeInterface {
	private final String id;
	
	public RoomLocationType(final String id) {
		this.id = id;
	}

	@Override
	public boolean isValidLocation(LocationTypeInterface location) {
		if(location instanceof RoomLocationType) {
			RoomLocationType roomLocation = (RoomLocationType) location;
			if(this.id.equals(roomLocation.id)) {
				return true;
			}
		}
		return false;
	}

}
