package jasbro.game.housing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.EventType;
import jasbro.game.events.rooms.ClassRoomEventHandler;
import jasbro.game.events.rooms.Crypt;
import jasbro.game.events.rooms.EmptyRoomEventHandler;
import jasbro.game.events.rooms.Garden;
import jasbro.game.events.rooms.MasterBedroomEventHandler;
import jasbro.game.events.rooms.RoomEventHandler;
import jasbro.game.events.rooms.SexSatisfactionEventHandler;
import jasbro.game.events.rooms.SickroomEventHandler;
import jasbro.game.world.RoomLoader;

/**
 * This class is a small bit of glue between existing code and the new XML room
 * loading.
 * 
 * @author somextra
 */
public class RoomInfoUtil {

	private static final Map<String, RoomInfo> roomInfos = RoomLoader.loadRooms(null);
	private static final Map<String, RoomUnlock> roomUnlocks = new HashMap<>();

	static {
		roomInfos.get("EMPTYROOM").setEventHandler(new EmptyRoomEventHandler());
		roomInfos.get("MASTERBEDROOM").setEventHandler(new MasterBedroomEventHandler());
		roomInfos.get("SICKROOM").setEventHandler(new SickroomEventHandler());
		roomInfos.get("DUNGEON")
				.setEventHandler(new SexSatisfactionEventHandler(EventType.ACTIVITY, Sextype.BONDAGE, 30));
		roomInfos.get("CLASSROOM").setEventHandler(new ClassRoomEventHandler());

		for (final String key : roomInfos.keySet()) {
			roomUnlocks.put(key, new RoomUnlock(key));
		}
	}
	
	public static RoomInfo getRoomInfo(final String id) {
		return roomInfos.get(id);
	}

	public static Collection<RoomInfo> getRoomInfos() {
		return roomInfos.values();
	}

	public static RoomUnlock getRoomUnlock(final String id) {
		return roomUnlocks.get(id);
	}
	
	public static Collection<RoomUnlock> getRoomUnlocks() {
		return roomUnlocks.values();
	}
	
	// TODO There's got to be a better way to do this
	public static Room newRoom(final String id) {
		if("GARDEN".equals(id) || "BIGGARDEN".equals(id)) {
			return new Garden(roomInfos.get(id));
		}
		if("CRYPT".equals(id)) {
			return new Crypt(roomInfos.get(id));
		}
		return new ConfigurableRoom(roomInfos.get(id));
	}
}
