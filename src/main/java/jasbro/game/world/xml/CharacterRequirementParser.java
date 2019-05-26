package jasbro.game.world.xml;

import org.w3c.dom.Element;

import jasbro.game.character.activities.requirements.CharacterRequirement;

public interface CharacterRequirementParser {
	CharacterRequirement parse(final Element requirementElement);
}
