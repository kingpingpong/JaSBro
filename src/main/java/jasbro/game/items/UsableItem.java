package jasbro.game.items;

import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.conditions.ItemCooldown;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.items.usableItemEffects.UsableItemEffect;
import jasbro.texts.TextUtil;

public class UsableItem extends Item {
	
	public UsableItem(String id) {
		super(id, ItemType.USABLE);
	}	
	
	public UsableItem(Item item) {
		super(item);
		setType(ItemType.USABLE);
	}
	
	private UsableItemEffect itemEffect;
	
	
	public boolean use(Charakter character) {
		for (Condition condition : character.getConditions()) {
			if (condition instanceof ItemCooldown) {
				ItemCooldown cooldown = (ItemCooldown) condition;
				if (cooldown.getItemId().equals(getId())) {
					return false;
				}
			}
		}
		
		itemEffect.apply(character, this);
		MyEvent event = new MyEvent(EventType.ITEMUSED, character);
		character.handleEvent(event);
		character.fireEvent(event);
		return true;
	}
	
	@Override
	public String getText() {
		return "<b>" + getName() + "</b>\n" +
				TextUtil.t("typeConsumable") + "\n" +
				TextUtil.t("valueItem", new Object[]{getValue()}) + "\n" +
				getDescription();
	}
	
	public UsableItemEffect getItemEffect() {
		return itemEffect;
	}
	
	public void setItemEffect(UsableItemEffect itemEffect) {
		this.itemEffect = itemEffect;	}
	
	
}