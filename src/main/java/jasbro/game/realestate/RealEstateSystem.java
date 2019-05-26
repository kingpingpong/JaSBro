package jasbro.game.realestate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jasbro.game.GameData;
import jasbro.game.housing.House;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.HouseUtil;

/**
 * Logic code for plots of land and construction. Public functions should be
 * kept to a minimum, and preferably game mechanic related.
 * 
 * @author somextra
 */
public class RealEstateSystem {
	private static final transient Logger LOG = LogManager.getLogger(RealEstateSystem.class);

	private final Map<String, Plot> plots;
	private final List<Plot> ownedPlots;
	private final List<Plot> freePlots;

	public RealEstateSystem() {
		this.plots = new HashMap<>();
		this.ownedPlots = new ArrayList<>();
		this.freePlots = new ArrayList<>();

		// TODO move this somewhere else
		this.plots.put("starting-plot", new Plot("starting-plot", 4, 0, 0, HouseUtil.newHouse(HouseType.HUT)));
		this.ownedPlots.add(this.plots.get("starting-plot"));
	}

	public Plot createPlot(final String id, final int maxSize, final int cost, final int quality, final House house) {
		Validate.notBlank(id, "Attempted to create a plot with a blank ID");
		if (this.plots.containsKey(id)) {
			LOG.error("Attempted to create plot with duplicate id '%s'", id);
			return null;
		}
		Plot p = new Plot(id, maxSize, cost, quality, house);
		this.plots.put(id, p);
		return p;
	}

	public Plot createPlot(final String id, final int maxSize, final int cost, final int quality) {
		return createPlot(id, maxSize, cost, quality, null);
	}

	public Collection<Plot> getOwnedPlots() {
		return Collections.unmodifiableList(ownedPlots);
	}

	public Collection<Plot> getFreePlots() {
		return Collections.unmodifiableList(freePlots);
	}

	public Collection<Plot> getAllPlots() {
		return Collections.unmodifiableCollection(this.plots.values());
	}

	public Plot getPlot(final String id) {
		return this.plots.get(id);
	}

	public Plot getOwnedPlot(final String id) {
		Plot p = this.plots.get(id);
		if (this.ownedPlots.contains(p)) {
			return p;
		}
		return null;
	}

	public Plot getFreePlot(final String id) {
		Plot p = this.plots.get(id);
		if (this.freePlots.contains(p)) {
			return p;
		}
		return null;
	}

	/**
	 * Buys a plot of land, making it owned
	 * 
	 * @param id
	 *            The ID of the plot to buy
	 * @param data
	 *            The GameData to charge
	 */
	public void buyPlot(final String id, final GameData data) {
		Plot plot = getRequiredFreePlot(id);

		if (data.canAfford(plot.getCost())) {
			data.spendMoney(plot.getCost(), plot);
			ownedPlots.add(plot);
			freePlots.remove(plot);
		} else {
			LOG.warn("Cannot afford plot {}", id);
		}
	}

	/**
	 * Sells a plot of land and makes it free
	 * 
	 * @param id
	 *            The owned plot of land
	 * @param data
	 *            The GameData to credit
	 */
	public void sellPlot(final String id, final GameData data) {
		Plot plot = getRequiredOwnedPlot(id);

		data.earnMoney(plot.getCost(), plot);
		ownedPlots.remove(plot);
		freePlots.add(plot);
	}

	/**
	 * Places a house on a plot of land
	 * 
	 * @param plotId
	 *            The plot to build on
	 * @param house
	 *            The house to build
	 * @param data
	 *            The GameData for charging
	 */
	public void buildHouse(final String plotId, final House house, final GameData data) {
		// TODO how to handle the house? move house selection/creation here?
		// TODO charge them money

		Plot plot = getOwnedPlot(plotId);

		if (canPlaceHouse(plotId, house)) {
			plot.setHouse(house);
		} else {
			LOG.warn("Can't place house '%s' on plot '%s'. This probably shouldn't have been reached.",
					house.getHouseType(), plotId);
		}
	}

	/**
	 * Removes the house on the plot of land
	 * 
	 * @param plotId
	 *            The plot to clear
	 * @param data
	 *            The GameData for charging
	 */
	public void demolishHouse(final String plotId, final GameData data) {
		// TODO charge them money
	}

	private Plot getRequiredPlot(final String id) {
		Plot p = this.plots.get(id);
		Validate.notNull(p, "Given plot ID '%s' does not exist", id);
		return p;
	}

	private Plot getRequiredOwnedPlot(final String id) {
		Plot p = getRequiredPlot(id);
		Validate.isTrue(this.ownedPlots.contains(p), "Given ID '%s' does not match an owned plot of land", id);
		return p;
	}

	private Plot getRequiredFreePlot(final String id) {
		Plot p = getRequiredPlot(id);
		Validate.isTrue(this.freePlots.contains(p), "Given ID '%s' does not match a free plot of land", id);
		return p;
	}

	private boolean canPlaceHouse(final String plotId, final House house) {
		return getOwnedPlot(plotId).getMaxSize() >= house.getRoomAmount();
	}
}
