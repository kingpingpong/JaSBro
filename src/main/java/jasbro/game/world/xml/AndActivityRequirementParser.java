package jasbro.game.world.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.AndActivityRequirement;
import jasbro.game.world.RoomLoader;

public class AndActivityRequirementParser implements ActivityRequirementParser {

	@Override
	public ActivityRequirement parse(Element requirementElement) {
		List<ActivityRequirement> requirements = new ArrayList<>();
		
		NodeList children = requirementElement.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				requirements.add(RoomLoader.parseActivityRequirement((Element) n));
			}
		}

		return new AndActivityRequirement(requirements.toArray(new ActivityRequirement[requirements.size()]));
	}

}
