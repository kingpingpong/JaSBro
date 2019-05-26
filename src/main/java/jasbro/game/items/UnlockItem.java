package jasbro.game.items;

import jasbro.game.interfaces.UnlockObject;

public class UnlockItem extends Item {
	private UnlockObject unlockObject;
	private String unlockMessage;
	
	public UnlockItem(String id) {
		super(id, ItemType.UNLOCK);
	}
	
	public UnlockItem(Item item) {
		super(item);
		setType(ItemType.UNLOCK);
	}
	
	public UnlockObject getUnlockObject() {
		return unlockObject;
	}
	
	public void setUnlockObject(UnlockObject unlockObject) {
		this.unlockObject = unlockObject;
	}
	
	@Override
	public String getText() {
		return getDescription();
	}
	
	public String getUnlockMessage() {
		return unlockMessage;
	}
	
	public void setUnlockMessage(String unlockMessage) {
		this.unlockMessage = unlockMessage;
	}
}