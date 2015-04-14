package jasbro.game.items.usableItemEffects;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.items.Item;

import java.util.ArrayList;
import java.util.List;

public class UsableItemEffectChance extends UsableItemEffect implements UsableItemEffectContainer {
	private List<UsableItemEffect> subEffects = new ArrayList<UsableItemEffect>();
	private int chance;
	
	@Override
	public void apply(Charakter character, Item item) {
		if (Util.getInt(0, 100) < chance) {
			for (UsableItemEffect itemEffect : subEffects) {
				itemEffect.apply(character, item);
			}
		}
	}
	
	public void applyOverride(Charakter character, Item item) {
		for (UsableItemEffect itemEffect : subEffects) {
			itemEffect.apply(character, item);
		}
	}	

	@Override
	public void addEffect(UsableItemEffect itemEffect) {
		subEffects.add(itemEffect);
	}

	@Override
	public String getName() {
		return "Effect chance";
	}

	public List<UsableItemEffect> getSubEffects() {
		return subEffects;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

	@Override
	public UsableItemEffectType getType() {
		return UsableItemEffectType.EFFECTCHANCE;
	}
}
