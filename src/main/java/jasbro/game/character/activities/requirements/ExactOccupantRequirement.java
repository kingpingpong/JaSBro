package jasbro.game.character.activities.requirements;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

import java.util.List;

/**
 * Requirement that a specific number of occupants are in the room.
 * 
 * @author somextra
 *
 */
public class ExactOccupantRequirement implements ActivityRequirement {
	
	private final int count;
	
	public ExactOccupantRequirement(final int count) {
		this.count = count;
	}
	
	@Override
	public boolean isValid(ActivityType activity, List<Charakter> characters, TypeAmounts typeAmounts) {
		return characters.size() == count;
	}
	
}