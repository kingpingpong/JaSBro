package jasbro.game.character.activities.requirements;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

import java.util.List;

/**
 * Not a requirement in itself, but an AND operation between all operations set
 * in this requirement. In short, all requirements in this object must be valid
 * for an activity to be valid.
 * 
 * Note that for simplicity, if nothing is passed it always returns true. Please
 * use {@link NoActivityRequirement} for that functionality, though.
 * 
 * @author somextra
 *
 */
public class AndActivityRequirement implements ActivityRequirement {
	
	final ActivityRequirement[] requirements;
	
	public AndActivityRequirement(final ActivityRequirement... requirements) {
		this.requirements = requirements;
	}
	
	@Override
	public boolean isValid(ActivityType activity, List<Charakter> characters, TypeAmounts typeAmounts) {
		for (ActivityRequirement ar : requirements) {
			if (!ar.isValid(activity, characters, typeAmounts)) {
				return false;
			}
		}
		
		return true;
	}
	
}