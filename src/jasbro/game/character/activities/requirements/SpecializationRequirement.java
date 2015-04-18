package jasbro.game.character.activities.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.specialization.SpecializationType;

/**
 * Requirement that a character has a given specialization.
 * @author somextra
 *
 */
public class SpecializationRequirement implements CharacterRequirement {
	
	final SpecializationType specialization;
	
	public SpecializationRequirement(final SpecializationType specialization) {
		this.specialization = specialization;
	}

	@Override
	public boolean isValid(ActivityType activity, Charakter character) {
		return character.getSpecializations().contains(specialization);
	}

}
