package jasbro.game.world.market;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameObject;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import jasbro.game.items.ItemLocation;
import jasbro.game.items.ItemSpawnData;

public class Shop extends GameObject implements CentralEventlistener {
	private final static Logger log = LogManager.getLogger(Shop.class);
	
	private HashMap<ItemLocation, Inventory> shopInventories;
	public final static ItemLocation shops[] = {ItemLocation.SHOP, ItemLocation.CLOTHINGSTORE, ItemLocation.ADVENTURERSSHOP,
		ItemLocation.APOTHECARY, ItemLocation.ADULTSTORE, ItemLocation.BOOKSTORE, ItemLocation.TRAVELLINGMERCHANT};
	
	public Shop() {
		Jasbro.getInstance().addCentralListener(this);
	}
	
	@Override
	public void handleCentralEvent(MyEvent e) {
		if (e.getType() == EventType.NEXTDAY) {
			initInventories();
		}
	}
	
	public Inventory getInventory(ItemLocation shop) {
		if (!getShopInventories().containsKey(shop)) {
			Inventory inventory = new Inventory();
			int chanceModifier=1;
			int amountModifier=1;
			if(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.BENEFACTORSHOPS)){
				chanceModifier=2;
				amountModifier=2;
			}
				
			for (Item item : Jasbro.getInstance().getAvailableItemsByLocation(shop)) {
				for (ItemSpawnData itemSpawnData : item.getSpawnData()) {
					if (itemSpawnData.getItemLocation() == shop) {
						if (itemSpawnData.getChance() == 0 || Util.getInt(0, 100) < itemSpawnData.getChance()*chanceModifier) {
							try {
								inventory.addItems(item, amountModifier* Util.getInt(itemSpawnData.getMinAmount(), amountModifier* itemSpawnData.getMaxAmount() + 1));
							}
							catch (Exception e) {
								log.error("Most likely illegal spawn data: {}", item.getId());
								log.throwing(e);
							}
						}
					}
				}
			}
			if (shop == ItemLocation.BLACKMARKET) {
				return inventory;
			}
			else {
				getShopInventories().put(shop, inventory);
			}
		}		
		return getShopInventories().get(shop);
	}
	
	private void initInventories() {
		for (ItemLocation shop : shops) {
			getShopInventories().remove(shop);
			getInventory(shop);
		}
	}
	
	public HashMap<ItemLocation, Inventory> getShopInventories() {
		if (shopInventories == null) {
			shopInventories = new HashMap<ItemLocation, Inventory>();
		}
		return shopInventories;
	}
	
	
}