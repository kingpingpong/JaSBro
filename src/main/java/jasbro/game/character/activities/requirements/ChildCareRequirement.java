package jasbro.game.character.activities.requirements;

import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

import java.util.List;

public class ChildCareRequirement implements ActivityRequirement {

	@Override
	public boolean isValid(ActivityType activity, List<Charakter> characters, TypeAmounts typeAmounts) {
		if (typeAmounts.getInfantAmount() > 0) {
			if (typeAmounts.getChildAmount() == 0 && typeAmounts.getTeenAmount() == 0 && typeAmounts.isAdultPresent()) {
				return true;
			}
			return false;
		}
		return true;
	}

}
