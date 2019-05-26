package jasbro.game.character.activities.requirements;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

import java.util.List;

/**
 * Requirement for all characters in a room to pass the given requirement.
 * 
 * @author somextra
 *
 */
public class AllCharacterRequirement implements ActivityRequirement {
	
	private final CharacterRequirement requirement;
	
	public AllCharacterRequirement(final CharacterRequirement requirement) {
		this.requirement = requirement;
	}
	
	@Override
	public boolean isValid(ActivityType activity, List<Charakter> characters, TypeAmounts typeAmounts) {
		for (Charakter c : characters) {
			if (!requirement.isValid(activity, c)) {
				return false;
			}
		}
		
		return true;
	}
	
}