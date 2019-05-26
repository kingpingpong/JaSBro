package jasbro.game.world.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.AllCharacterRequirement;
import jasbro.game.world.RoomLoader;

public class AllCharacterRequirementParser implements ActivityRequirementParser {

	@Override
	public ActivityRequirement parse(final Element requirementElement) {
		Element charRequirementElement = null;
		NodeList children = requirementElement.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				charRequirementElement = (Element) n;
				break;
			}
		}
		return new AllCharacterRequirement(RoomLoader.parseCharacterRequirement(charRequirementElement));
	}

}
