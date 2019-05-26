package jasbro.game.world.xml;

import org.w3c.dom.Element;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.ChildCareRequirement;

public class ChildCareRequirementParser implements ActivityRequirementParser {

	@Override
	public ActivityRequirement parse(Element requirementElement) {
		return new ChildCareRequirement();
	}

}
