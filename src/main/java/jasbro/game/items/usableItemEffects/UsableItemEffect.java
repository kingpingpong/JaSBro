package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class UsableItemEffect implements Serializable {
	public abstract String getName();
	public abstract void apply(Charakter character, Item item);
	public abstract UsableItemEffectType getType();
	
	public List<UsableItemEffect> getSubEffects() {
		return new ArrayList<UsableItemEffect>();
	}
	

}
