package jasbro.game.character.activities.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

/**
 * An OR operation on multiple character requirements. If any are valid, this
 * operator returns true;
 * 
 * @author somextra
 *
 */
public class OrCharacterRequirement implements CharacterRequirement {
	
	private final CharacterRequirement[] requirements;
	
	public OrCharacterRequirement(final CharacterRequirement... requirements) {
		this.requirements = requirements;
	}
	
	@Override
	public boolean isValid(ActivityType activity, Charakter character) {
		for(CharacterRequirement requirement : requirements) {
			if(requirement.isValid(activity, character)) {
				return true;
			}
		}
		
		return false;
	}
	
}