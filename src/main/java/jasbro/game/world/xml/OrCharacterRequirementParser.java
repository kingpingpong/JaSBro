package jasbro.game.world.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.character.activities.requirements.OrCharacterRequirement;
import jasbro.game.world.RoomLoader;

public class OrCharacterRequirementParser implements CharacterRequirementParser {

	@Override
	public CharacterRequirement parse(Element requirementElement) {
		List<CharacterRequirement> requirements = new ArrayList<>();
		
		NodeList children = requirementElement.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				requirements.add(RoomLoader.parseCharacterRequirement((Element) n));
			}
		}

		return new OrCharacterRequirement(requirements.toArray(new CharacterRequirement[requirements.size()]));
	}

}
