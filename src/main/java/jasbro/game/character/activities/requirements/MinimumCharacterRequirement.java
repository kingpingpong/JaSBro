package jasbro.game.character.activities.requirements;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

import java.util.List;

/**
 * Requirement for a given minimum (inclusive) of characters to have the configured requirement.
 * 
 * @author somextra
 *
 */
public class MinimumCharacterRequirement implements ActivityRequirement {

	final CharacterRequirement requirement;
	final int minimum;

	public MinimumCharacterRequirement(final CharacterRequirement requirement, final int minimum) {
		this.requirement = requirement;
		this.minimum = minimum;
	}

	@Override
	public boolean isValid(ActivityType activity, List<Charakter> characters, TypeAmounts typeAmounts) {
		int count = 0;
		for(Charakter c : characters) {
			if(requirement.isValid(activity, c)) {
				++count;
			}
		}
		
		return count >= minimum;
	}

}
