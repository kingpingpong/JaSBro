package jasbro.game.items.usableItemEffects;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.items.Item;

public class UsableItemChangeAttribute extends UsableItemEffect {
	private AttributeType attribute;
	private int minChange;
	private int maxChange;
	
	@Override
	public String getName() {
		return "Change attribute value effect";
	}
	@Override
	public void apply(Charakter character, Item item) {
		if (attribute != null) {
			if (maxChange < minChange) {
				int tmp = minChange;
				minChange = maxChange;
				maxChange = tmp;
			}
			character.getAttribute(attribute).addToValue(Util.getInt(minChange, maxChange+1), true);
		}
	}
	
	public AttributeType getAttribute() {
		return attribute;
	}
	public void setAttribute(AttributeType attribute) {
		this.attribute = attribute;
	}
	public int getMinChange() {
		return minChange;
	}
	public void setMinChange(int minChange) {
		this.minChange = minChange;
	}
	public int getMaxChange() {
		return maxChange;
	}
	public void setMaxChange(int maxChange) {
		this.maxChange = maxChange;
	}
	@Override
	public UsableItemEffectType getType() {
		return UsableItemEffectType.CHANGEATTRIBUTE;
	}
}