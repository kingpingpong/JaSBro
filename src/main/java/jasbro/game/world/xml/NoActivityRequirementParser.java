package jasbro.game.world.xml;

import org.w3c.dom.Element;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.NoActivityRequirement;

public class NoActivityRequirementParser implements ActivityRequirementParser {

	@Override
	public ActivityRequirement parse(final Element requirementElement) {
		return new NoActivityRequirement();
	}

}
