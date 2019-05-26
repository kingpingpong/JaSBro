package jasbro.game.character.activities.requirements;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

import java.util.List;

/**
 * Requirement specifying the lowest number of people required for an activity
 * to be performed. Only rooms with the given minimum or more occupants has the activity visible.
 * 
 * @author somextra
 */
public class MinimumOccupantRequirement implements ActivityRequirement {
	
	private final int minimum;
	
	/**
	 * Create a new requirement with the given minimum.
	 * 
	 * @param minimum
	 *            The minimum number of {@link Charakter}s required.
	 */
	public MinimumOccupantRequirement(final int minimum) {
		this.minimum = minimum;
	}
	
	@Override
	public boolean isValid(ActivityType activity, List<Charakter> characters, TypeAmounts typeAmounts) {
		return characters.size() >= minimum;
	}
	
}