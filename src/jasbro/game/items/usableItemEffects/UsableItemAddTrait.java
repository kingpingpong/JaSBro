package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.game.items.Item;

public class UsableItemAddTrait extends UsableItemEffect {
	private Trait trait;
	
	@Override
	public void apply(Charakter character, Item item) {
		if (trait != null) {
			character.addTrait(trait);
		}
	}

	@Override
	public String getName() {
		return "Add Trait";
	}

	public Trait getTrait() {
		return trait;
	}

	public void setTrait(Trait trait) {
		this.trait = trait;
	}

	@Override
	public UsableItemEffectType getType() {
		return UsableItemEffectType.ADDTRAIT;
	}
}
