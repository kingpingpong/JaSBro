package jasbro.game.housing;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * More glue code for trimmed enums
 * 
 * @author somextra
 */
public final class HouseUtil {
	private static transient final Logger LOG = LogManager.getLogger(HouseUtil.class);

	public static House newHouse(final HouseType type) {
		return loadHouseFromProperties(type.toString().toLowerCase());
	}

	private static House loadHouseFromProperties(final String id) {
		Properties houseProps = new Properties();

		try (InputStream is = HouseUtil.class.getResourceAsStream("/houses/" + id + ".properties")) {
			houseProps.load(is);
		} catch (IOException e) {
			LOG.error("Failed to load house with id '" + id + "' from properties", e);
			return null;
		}

		HouseType type = HouseType.valueOf(houseProps.getProperty("type"));
		int value = Integer.parseInt(houseProps.getProperty("value"));
		String morningImage = houseProps.getProperty("image.morning");
		String afternoonImage = houseProps.getProperty("image.afternoon");
		String nightImage = houseProps.getProperty("image.night");
		String[] rawSlotTypes = houseProps.getProperty("rooms.slots").split(",");
		String[] roomInfoIds = houseProps.getProperty("rooms.default").split(",");

		if (rawSlotTypes.length != roomInfoIds.length) {
			LOG.error("'rooms.slots' and 'rooms.default' do not have the same number if items in properties for '{}'", id);
			return null;
		}

		RoomSlotType[] slotTypes = new RoomSlotType[rawSlotTypes.length];
		for (int i = 0; i < rawSlotTypes.length; ++i) {
			// Account for spaces
			rawSlotTypes[i] = rawSlotTypes[i].trim();
			roomInfoIds[i] = roomInfoIds[i].trim();
			// Get enum value
			slotTypes[i] = RoomSlotType.valueOf(rawSlotTypes[i]);
		}

		return new House(type, value, morningImage, afternoonImage, nightImage, slotTypes, roomInfoIds);
	}
}
