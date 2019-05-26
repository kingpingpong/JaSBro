package jasbro.game.world.xml;

import org.apache.commons.lang3.Validate;
import org.w3c.dom.Element;

import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.character.activities.requirements.SpecializationRequirement;
import jasbro.game.character.specialization.SpecializationType;

public class SpecializationRequirementParser implements CharacterRequirementParser {

	@Override
	public CharacterRequirement parse(Element requirementElement) {
		String specialization = requirementElement.getAttribute("specialization");
		Validate.notBlank(specialization);
		return new SpecializationRequirement(SpecializationType.valueOf(specialization));
	}

}
