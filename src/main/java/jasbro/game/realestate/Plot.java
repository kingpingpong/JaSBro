package jasbro.game.realestate;

import jasbro.game.housing.House;

/**
 * A plot of land. Contains a house of some capped size.
 * 
 * @author somextra
 */
public class Plot {
	private final String id;
	private final int maxSize;
	private final int cost;
	private final int quality;
	private House house;
	
	public Plot(final String id, final int maxSize, final int cost, final int quality, final House house) {
		this.id = id;
		this.maxSize = maxSize;
		this.cost = cost;
		this.quality = quality;
		this.house = house;
	}

	public Plot(final String id, final int maxSize, final int cost, final House house) {
		this(id, maxSize, cost, 0, house);
	}

	public Plot(final String id, final int maxSize, final int cost) {
		this(id, maxSize, cost, null);
	}
	
	public String getId() {
		return this.id;
	}

	public int getMaxSize() {
		return this.maxSize;
	}

	public int getCost() {
		return this.cost;
	}
	
	public int getQuality() {
		return this.quality;
	}

	public void setHouse(final House house) {
		this.house = house;
	}

	public House getHouse() {
		return this.house;
	}
}
