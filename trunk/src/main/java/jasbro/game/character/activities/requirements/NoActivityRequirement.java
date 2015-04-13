package jasbro.game.character.activities.requirements;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

import java.util.List;

/**
 * The drawback of interfaces: the simple case. This is for use when there is no
 * requirement on an activity. It allows the code to go through the same
 * motions, but just always returns {@code true}.
 * 
 * @author somextra
 *
 */
public class NoActivityRequirement implements ActivityRequirement {

	@Override
	public boolean isValid(ActivityType activity, List<Charakter> characters, TypeAmounts typeAmounts) {
		return true;
	}

}
