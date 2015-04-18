package jasbro.game.character.activities.requirements;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

import java.util.List;

/**
 * Interface for any requirements on an activity.
 * 
 * @author somextra
 *
 */
public interface ActivityRequirement {
	boolean isValid(ActivityType activity, List<Charakter> characters, TypeAmounts typeAmounts);
}
