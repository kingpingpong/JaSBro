package jasbro.game.items;

import jasbro.Jasbro;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
	private Map<String, Integer> items = new HashMap<String, Integer>();

	public void addItem(Item item) {
		String itemId = item.getId();
		if (checkTriggerUnlock(item)) {
		    unlock((UnlockItem)item);
		}
		else {
	        if (!items.containsKey(itemId)) {
	            items.put(itemId, 1);
	        }
	        else {
	            items.put(itemId, items.get(itemId) + 1);
	        }
		}
	}
	
	public void addItems(Item item, int amount) {
        if (amount > 0) {
            if (checkTriggerUnlock(item)) {
                unlock((UnlockItem) item);
            } else {
                String itemId = item.getId();
                if (!items.containsKey(itemId)) {
                    items.put(itemId, amount);
                } else {
                    items.put(itemId, items.get(itemId) + amount);
                }
            }
        }
	}
	

    public void addItems(List<ItemData> items) {
        for (ItemData itemData : items) {
            addItems(itemData.getItem(), itemData.getAmount());
        }
    }
	
	public void removeItem(Item item) {
		String itemId = item.getId();
		if (items.containsKey(itemId)) {
			items.put(itemId, items.get(itemId) - 1);
			if (items.get(itemId) < 1) {
				items.remove(itemId);
			}
		}
	}
	
    public void removeItems(Item item, int amount) {
        String itemId = item.getId();
        if (items.containsKey(itemId)) {
            items.put(itemId, items.get(itemId) - amount);
            if (items.get(itemId) < 1) {
                items.remove(itemId);
            }
        }
    }
	
	public int getAmount(Item item) {
	    if (item == null) {
	        return 0;
	    }
	    else if (!items.containsKey(item.getId())) {
			return 0;
		}
		else {
			return items.get(item.getId());
		}
	}
	
	public List<ItemData> getItems() {
		List<ItemData> itemList = new ArrayList<ItemData>();
		Map<String, Item> itemMap = Jasbro.getInstance().getItems();
		List<String> removeList = new ArrayList<String>();
		
		for (String itemId : this.items.keySet()) {
			if (itemMap.containsKey(itemId)) {
				itemList.add(new ItemData(itemMap.get(itemId), items.get(itemId)));
			}
			else {
				removeList.add(itemId);
			}
		}
		itemList.removeAll(removeList);
		Collections.sort(itemList);
		return itemList;
	}
	
	public List<Item> getExistingItems() {
		List<Item> itemList = new ArrayList<Item>();
		Map<String, Item> itemMap = Jasbro.getInstance().getItems();
		List<String> removeList = new ArrayList<String>();
		
		for (String itemId : this.items.keySet()) {
			if (itemMap.containsKey(itemId)) {
				itemList.add(itemMap.get(itemId));
			}
			else {
				removeList.add(itemId);
			}
		}
		itemList.removeAll(removeList);
		return itemList;
	}
	
	public boolean checkTriggerUnlock(Item item) {
	    if (item.getType() == ItemType.UNLOCK) {
	        if (this == Jasbro.getInstance().getData().getInventory()) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void unlock(UnlockItem item) {
	    Jasbro.getInstance().getData().getUnlocks().addUnlock(item.getUnlockObject());
	    MessageData messageData = new MessageData(item.getUnlockMessage(), item.getUnlockObject().getImage(), 
	            new ImageData("images/backgrounds/sky.jpg"), true);
	    messageData.createMessageScreen();
	}
	
	
	public static class ItemData implements Comparable<ItemData> {
		private Item item;
		private int amount;
		
		public ItemData() {
			super();
		}
		public ItemData(Item item, int amount) {
			super();
			this.item = item;
			this.amount = amount;
		}
		
		public Item getItem() {
			return item;
		}
		public void setItem(Item item) {
			this.item = item;
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
		
		@Override
		public String toString() {
			return item.getName() + " (" + getAmount() + ")";
		}
		@Override
		public int compareTo(ItemData o) {
			return item.getName().compareTo(o.getItem().getName());
		}
	}
}
