package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.game.items.Item;

public class UsableItemRemoveTrait extends UsableItemEffect {
	private Trait trait;
	
	@Override
	public void apply(Charakter character, Item item) {
		if (trait != null) {
			character.removeTrait(trait);
		}
	}
	
	@Override
	public String getName() {
		return "Remove Trait";
	}
	
	public Trait getTrait() {
		return trait;
	}
	
	public void setTrait(Trait trait) {
		this.trait = trait;
	}
	
	@Override
	public UsableItemEffectType getType() {
		return UsableItemEffectType.REMOVETRAIT;
	}
}