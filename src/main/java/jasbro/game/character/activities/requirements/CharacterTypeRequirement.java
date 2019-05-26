package jasbro.game.character.activities.requirements;

import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

/**
 * Requirement that a character is of a certain type.
 * 
 * @author somextra
 *
 */
public class CharacterTypeRequirement implements CharacterRequirement {
	
	private final CharacterType type;
	
	public CharacterTypeRequirement(final CharacterType type) {
		this.type = type;
	}
	
	@Override
	public boolean isValid(ActivityType activity, Charakter character) {
		return character.getType().equals(type);
	}
	
}