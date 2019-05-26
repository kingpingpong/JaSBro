package jasbro.game.world.xml;

import org.apache.commons.lang3.Validate;
import org.w3c.dom.Element;

import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.character.activities.requirements.TraitRequirement;
import jasbro.game.character.traits.Trait;

public class TraitRequirementParser implements CharacterRequirementParser {

	@Override
	public CharacterRequirement parse(Element requirementElement) {
		String trait = requirementElement.getAttribute("trait");
		Validate.notBlank(trait);
		return new TraitRequirement(Trait.valueOf(trait));
	}

}
