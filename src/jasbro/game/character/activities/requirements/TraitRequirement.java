package jasbro.game.character.activities.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.traits.Trait;

public class TraitRequirement implements CharacterRequirement {

	private final Trait trait;

	public TraitRequirement(final Trait trait) {
		this.trait = trait;
	}

	@Override
	public boolean isValid(ActivityType activity, Charakter character) {
		if(character.getTraits().contains(trait)) {
			return true;
		}
		
		return false;
	}

}
