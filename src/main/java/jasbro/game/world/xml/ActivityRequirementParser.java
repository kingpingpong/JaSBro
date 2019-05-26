package jasbro.game.world.xml;

import org.w3c.dom.Element;

import jasbro.game.character.activities.requirements.ActivityRequirement;

public interface ActivityRequirementParser {
	ActivityRequirement parse(final Element requirementElement);
}
