package jasbro.game.items;

public class ItemSpawnData {
	private ItemLocation itemLocation = ItemLocation.SHOP;
	private int chance;
	private int minAmount;
	private int maxAmount;
	
	
	public ItemLocation getItemLocation() {
		return itemLocation;
	}
	public void setItemLocation(ItemLocation itemLocation) {
		this.itemLocation = itemLocation;
	}
	public int getChance() {
		return chance;
	}
	public void setChance(int chance) {
		this.chance = chance;
	}
	public int getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(int minAmount) {
		this.minAmount = minAmount;
	}
	public int getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	
}