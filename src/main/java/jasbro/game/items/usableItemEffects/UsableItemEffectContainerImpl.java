package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.items.Item;

import java.util.ArrayList;
import java.util.List;

public class UsableItemEffectContainerImpl extends UsableItemEffect implements UsableItemEffectContainer {
	private List<UsableItemEffect> subEffects = new ArrayList<UsableItemEffect>();
	
	@Override
	public void addEffect(UsableItemEffect itemEffect) {
		subEffects.add(itemEffect);
	}
	
	@Override
	public String getName() {
		return "Effect container";
	}
	
	@Override
	public void apply(Charakter character, Item item) {
		for (UsableItemEffect itemEffect : subEffects) {
			((UsableItemEffect)itemEffect).apply(character, item);
		}
	}
	
	@Override
	public UsableItemEffectType getType() {
		return UsableItemEffectType.EFFECTCONTAINER;
	}
	
	public List<UsableItemEffect> getSubEffects() {
		return subEffects;
	}
	
	
}