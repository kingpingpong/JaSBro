package jasbro.game.character.activities;

import jasbro.game.housing.House;

public class HouseCustomerProvider implements CustomerProvider {
	private House house;

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}
	
	
}
