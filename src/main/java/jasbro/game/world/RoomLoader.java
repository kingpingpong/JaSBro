package jasbro.game.world;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.housing.RoomInfo;
import jasbro.game.housing.RoomSlotType;
import jasbro.game.world.xml.ActivityRequirementParser;
import jasbro.game.world.xml.AllCharacterRequirementParser;
import jasbro.game.world.xml.AndActivityRequirementParser;
import jasbro.game.world.xml.CharacterRequirementParser;
import jasbro.game.world.xml.CharacterTypeRequirementParser;
import jasbro.game.world.xml.ChildCareRequirementParser;
import jasbro.game.world.xml.ExactOccupantRequirementParser;
import jasbro.game.world.xml.MaximumOccupantRequirementParser;
import jasbro.game.world.xml.MinimumCharacterRequirementParser;
import jasbro.game.world.xml.MinimumOccupantRequirementParser;
import jasbro.game.world.xml.NoActivityRequirementParser;
import jasbro.game.world.xml.OrCharacterRequirementParser;
import jasbro.game.world.xml.SpecializationRequirementParser;
import jasbro.game.world.xml.TraitRequirementParser;

public class RoomLoader {
	private static final Logger LOG = LogManager.getLogger(RoomLoader.class);
	private static final String DEFAULT_ROOM_FILE = "rooms.xml";

	private static final Map<String, ActivityRequirementParser> ACT_REQUIREMENTS = new HashMap<>();
	private static final Map<String, CharacterRequirementParser> CHAR_REQUIREMENTS = new HashMap<>();

	static {
		ACT_REQUIREMENTS.put("all-character", new AllCharacterRequirementParser());
		ACT_REQUIREMENTS.put("and", new AndActivityRequirementParser());
		ACT_REQUIREMENTS.put("child-care", new ChildCareRequirementParser());
		ACT_REQUIREMENTS.put("exact-occupant", new ExactOccupantRequirementParser());
		ACT_REQUIREMENTS.put("max-occupant", new MaximumOccupantRequirementParser());
		ACT_REQUIREMENTS.put("min-character", new MinimumCharacterRequirementParser());
		ACT_REQUIREMENTS.put("min-occupant", new MinimumOccupantRequirementParser());
		ACT_REQUIREMENTS.put("none", new NoActivityRequirementParser());

		CHAR_REQUIREMENTS.put("char-type", new CharacterTypeRequirementParser());
		CHAR_REQUIREMENTS.put("or", new OrCharacterRequirementParser());
		CHAR_REQUIREMENTS.put("specialization", new SpecializationRequirementParser());
		CHAR_REQUIREMENTS.put("trait", new TraitRequirementParser());
	}

	public static Map<String, RoomInfo> loadRooms(final String customFile) {
		final Map<String, RoomInfo> rooms = new HashMap<>();
		final String file = customFile == null ? DEFAULT_ROOM_FILE : customFile;
		LOG.info("Loading rooms from file '{}'", file);
		Document doc = null;

		try {
			final InputStream fileStream = new FileInputStream(file);
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileStream);
		} catch (final FileNotFoundException e) {
			LOG.error("Failed to find room file", e);
		} catch (SAXException e) {
			LOG.error("Failed to parse room file", e);
		} catch (IOException e) {
			LOG.error("Failed to read room file", e);
		} catch (ParserConfigurationException e) {
			LOG.error("Failed to create parser for room file", e);
		}

		parseDocument(doc, rooms);

		return rooms;
	}

	private static void parseDocument(final Document doc, final Map<String, RoomInfo> rooms) {
		Element root = doc.getDocumentElement();
		NodeList children = root.getChildNodes();

		for (int i = 0; i < children.getLength(); ++i) {
			Node n = children.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				parseRoomElement((Element) n, rooms);
			}
		}
	}

	private static void parseRoomElement(final Element ele, final Map<String, RoomInfo> rooms) {
		if (!validateRoomElement(ele)) {
			LOG.error("Room element failed validation, aborting room loading");
			return;
		}

		int maxOccupancy = 0;
		int cost = 0;
		String id = null;
		String imageLocation = null;

		LOG.debug("Parsing room element: {}", ele);

		id = ele.getAttribute("id");

		if (rooms.containsKey(id)) {
			LOG.warn("Duplicate room ID '{}' found, overwriting", id);
		}

		maxOccupancy = Integer.parseInt(ele.getAttribute("max-occupancy"));
		cost = Integer.parseInt(ele.getAttribute("cost"));
		id = ele.getAttribute("id");
		imageLocation = ele.getAttribute("image");

		RoomInfo info = new RoomInfo(maxOccupancy, cost, id, imageLocation);

		Element activities = (Element) ele.getElementsByTagName("activities").item(0);
		Element slots = (Element) ele.getElementsByTagName("slots").item(0);

		parseSlotTypes(slots, info);
		parseActivities(activities, info);

		rooms.put(id, info);
	}

	private static boolean validateRoomElement(final Element ele) {
		boolean valid = true;
		if (!ele.hasAttribute("id")) {
			LOG.error("Room element missing 'id' attribute");
			valid = false;
		}
		if (!ele.hasAttribute("cost")) {
			LOG.error("Room element missing 'cost' attribute");
			valid = false;
		}
		if (!ele.hasAttribute("max-occupancy")) {
			LOG.error("Room element missing 'max-occupancy' attribute");
			valid = false;
		}
		if (!ele.hasAttribute("image")) {
			LOG.error("Room element missing 'image' attribute");
			valid = false;
		}

		return valid;
	}

	private static void parseSlotTypes(final Element slots, final RoomInfo info) {
		if (slots == null) {
			LOG.info("Room '{}' does not have a 'slots' element, using defaults", info.getId());
			info.addSlotType(RoomSlotType.SMALLROOM);
			info.addSlotType(RoomSlotType.LARGEROOM);
			return;
		}

		NodeList children = slots.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				parseSlotType((Element) child, info);
			}
		}

		if (info.getSlotTypes().isEmpty()) {
			LOG.info(
					"Room '{}' did not have any 'slot' elements under 'slots', or was not able to parse them. Using defaults",
					info.getId());
			info.addSlotType(RoomSlotType.SMALLROOM);
			info.addSlotType(RoomSlotType.LARGEROOM);
		}

		if (info.getSlotTypes().contains(RoomSlotType.SMALLROOM)) {
			info.addSlotType(RoomSlotType.LARGEROOM);
		}
	}

	private static void parseSlotType(final Element slot, final RoomInfo info) {
		String type = slot.getAttribute("type");
		Validate.notBlank(type, "'slot' element has missing or blank attribute 'type'");

		RoomSlotType roomSlotType = RoomSlotType.valueOf(type);
		info.addSlotType(roomSlotType);
	}

	private static void parseActivities(final Element ele, final RoomInfo info) {
		Validate.notNull(ele, "Required element 'activities' missing for room '%s'", info.getId());

		NodeList children = ele.getChildNodes();
		for (int i = 0; i < children.getLength(); ++i) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				parseActivity((Element) child, info);
			}
		}
	}

	private static void parseActivity(final Element ele, final RoomInfo info) {
		String id = ele.getAttribute("id");
		Validate.notBlank(id, "'activity' element has missing or blank attribute 'id'. Given value was '%s'", id);

		ActivityType type = ActivityType.valueOf(id);

		Element requirement = (Element) ele.getElementsByTagName("requirement").item(0);
		Validate.notNull(requirement, "Required element 'requirement' 'missing under 'activity' for room '%s'",
				info.getId());

		ActivityRequirement actRequirement = parseActivityRequirement(requirement);
		if (ele.getTagName().equals("child-activity")) {
			info.addChildCareActivity(type, actRequirement);
		} else {
			info.addActivity(type, actRequirement);
		}
	}

	public static ActivityRequirement parseActivityRequirement(final Element ele) {
		String requirementType = ele.getAttribute("type");
		Validate.notBlank(requirementType, "Required attribute 'type' missing on element 'requirement'");

		Validate.isTrue(ACT_REQUIREMENTS.containsKey(requirementType),
				"Attribute 'type' with value '%s' does not map to any known parser", requirementType);
		return ACT_REQUIREMENTS.get(requirementType).parse(ele);
	}

	public static CharacterRequirement parseCharacterRequirement(final Element ele) {
		String requirementType = ele.getAttribute("type");
		Validate.notBlank(requirementType, "Required attribute 'type' missing on element 'requirement'");

		Validate.isTrue(CHAR_REQUIREMENTS.containsKey(requirementType),
				"Attribute 'type' with value '%s' does not map to any known parser", requirementType);
		return CHAR_REQUIREMENTS.get(requirementType).parse(ele);
	}
}
