package jasbro.game.character.activities.requirements;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

import java.util.List;

/**
 * Requirement specifying the maximum number of people (inclusive) that can be
 * present for an activity.
 * 
 * @author somextra
 *
 */
public class MaximumOccupantRequirement implements ActivityRequirement {
	
	private int maximum;
	
	public MaximumOccupantRequirement(final int maximum) {
		this.maximum = maximum;
	}
	
	@Override
	public boolean isValid(ActivityType activity, List<Charakter> characters, TypeAmounts typeAmounts) {
		return characters.size() <= maximum;
	}
	
}