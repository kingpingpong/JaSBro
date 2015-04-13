package jasbro.game.housing;

import jasbro.game.character.CharacterType;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.AllCharacterRequirement;
import jasbro.game.character.activities.requirements.AndActivityRequirement;
import jasbro.game.character.activities.requirements.CharacterTypeRequirement;
import jasbro.game.character.activities.requirements.ChildCareRequirement;
import jasbro.game.character.activities.requirements.ExactOccupantRequirement;
import jasbro.game.character.activities.requirements.MaximumOccupantRequirement;
import jasbro.game.character.activities.requirements.MinimumCharacterRequirement;
import jasbro.game.character.activities.requirements.MinimumOccupantRequirement;
import jasbro.game.character.activities.requirements.NoActivityRequirement;
import jasbro.game.character.activities.requirements.OrCharacterRequirement;
import jasbro.game.character.activities.requirements.OrSpecializationRequirement;
import jasbro.game.character.activities.requirements.SpecializationRequirement;
import jasbro.game.character.activities.requirements.TraitRequirement;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.rooms.ClassRoomEventHandler;
import jasbro.game.events.rooms.EmptyRoomEventHandler;
import jasbro.game.events.rooms.MasterBedroomEventHandler;
import jasbro.game.events.rooms.OrgyRoomEventHandler;
import jasbro.game.events.rooms.SexSatisfactionEventHandler;
import jasbro.game.events.rooms.SickroomEventHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Important! This class will (eventually) be removed once parsing is completed.
 * This is only as an in-between while it activity requirements and parsing are
 * being developed.
 * 
 * It's also a terrible design. Don't try this at home!
 * 
 * @author somextra
 *
 */
public class RoomInfoUtil {

	private static final Map<RoomType, RoomInfo> roomInfos = new HashMap<RoomType, RoomInfo>();

	private static final ActivityRequirement NO_REQUIREMENT = new NoActivityRequirement();
	private static final ActivityRequirement SEX_REQUIREMENT = new ExactOccupantRequirement(2);
	private static final ActivityRequirement THREESOME_REQUIREMENT = new ExactOccupantRequirement(3);
	private static final ActivityRequirement ORGY_REQUIREMENT = new MinimumOccupantRequirement(4);
	private static final ActivityRequirement TRAIN_REQUIREMENT = new AndActivityRequirement(
			new ExactOccupantRequirement(2), new MinimumCharacterRequirement(new CharacterTypeRequirement(
					CharacterType.TRAINER), 1));
	private static final ActivityRequirement TALK_REQUIREMENT = TRAIN_REQUIREMENT;
	private static final ActivityRequirement NURSE_REQUIREMENT = new AndActivityRequirement(
			new MinimumOccupantRequirement(2), new MinimumCharacterRequirement(new SpecializationRequirement(
					SpecializationType.NURSE), 1));
	private static final ActivityRequirement PAMPER_REQUIREMENT = NURSE_REQUIREMENT;
	private static final ActivityRequirement BODYWRAP_REQUIREMENT = new AndActivityRequirement(
			new MinimumOccupantRequirement(2), new MinimumCharacterRequirement(new TraitRequirement(Trait.BEAUTICIAN),
					1));
	private static final ActivityRequirement CHILDCARE_SLEEP_REQUIREMENT = new ChildCareRequirement();
	private static final ActivityRequirement CHILDCARE_PLAY_REQUIREMENT = CHILDCARE_SLEEP_REQUIREMENT;
	private static final ActivityRequirement CHILDCARE_NURSE_REQUIREMENT = new MinimumCharacterRequirement(
			new SpecializationRequirement(SpecializationType.NURSE), 1);

	// If there is a programming god, I have angered him here
	static {
		RoomInfo emptyRoom = new RoomInfo(6, 10, "EMPTYROOM", "images/backgrounds/emptyroom.jpg");
		populateBedroomActivities(emptyRoom);
		emptyRoom.setEventHandler(new EmptyRoomEventHandler());
		roomInfos.put(RoomType.EMPTYROOM, emptyRoom);

		RoomInfo smallBedroom = new RoomInfo(2, 500, "SMALLBEDROOM", "images/backgrounds/smallbedroom.png");
		populateBedroomActivities(smallBedroom);
		roomInfos.put(RoomType.SMALLBEDROOM, smallBedroom);

		RoomInfo normalBedroom = new RoomInfo(4, 1000, "BEDROOM", "images/backgrounds/bedroom.jpg");
		populateBedroomActivities(normalBedroom);
		roomInfos.put(RoomType.BEDROOM, normalBedroom);

		RoomInfo largeBedroom = new RoomInfo(6, 2000, "LARGEBEDROOM", "images/backgrounds/bigbedroom.jpg");
		populateBedroomActivities(largeBedroom);
		roomInfos.put(RoomType.LARGEBEDROOM, largeBedroom);

		RoomInfo masterBedroom = new RoomInfo(2, 5000, "MASTERBEDROOM", "images/backgrounds/MasterBedroom.jpg");
		populateBedroomActivities(masterBedroom);
		masterBedroom.setEventHandler(new MasterBedroomEventHandler());
		roomInfos.put(RoomType.MASTERBEDROOM, masterBedroom);

		RoomInfo sickroom = new RoomInfo(4, 4000, "SICKROOM", "images/backgrounds/sickroom.jpg");
		populateBedroomActivities(sickroom);
		sickroom.setEventHandler(new SickroomEventHandler());
		roomInfos.put(RoomType.SICKROOM, sickroom);

		RoomInfo orgyroom = new RoomInfo(10, 12000, "ORGYROOM", "images/backgrounds/orgyroom.jpg");
		populateBedroomActivities(orgyroom);
		orgyroom.setEventHandler(new OrgyRoomEventHandler());
		orgyroom.addActivity(ActivityType.PUBLICUSE,NO_REQUIREMENT);
		roomInfos.put(RoomType.ORGYROOM, orgyroom);

		RoomInfo kitchen = new RoomInfo(1, 650, "KITCHEN", "images/backgrounds/kitchen.jpg");
		kitchen.addActivity(ActivityType.COOK, NO_REQUIREMENT);
		kitchen.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		kitchen.addActivity(ActivityType.SELLFOOD, new MinimumCharacterRequirement(new SpecializationRequirement(
				SpecializationType.MAID), 1));
		roomInfos.put(RoomType.KITCHEN, kitchen);

		RoomInfo arena = new RoomInfo(2, 6000, "ARENA", "images/backgrounds/arena.jpg");
		arena.addActivity(ActivityType.TRAINTOFIGHT, new AllCharacterRequirement(new OrCharacterRequirement(
				new SpecializationRequirement(SpecializationType.FIGHTER), new CharacterTypeRequirement(
						CharacterType.TRAINER))));
		arena.addActivity(ActivityType.FIGHT, new AllCharacterRequirement(new SpecializationRequirement(
				SpecializationType.FIGHTER)));
		arena.addActivity(ActivityType.SUBMITTOMONSTER, new MaximumOccupantRequirement(1));
		arena.addActivity(ActivityType.MONSTERFIGHT, new AllCharacterRequirement(new OrCharacterRequirement(
                new SpecializationRequirement(SpecializationType.FIGHTER), new CharacterTypeRequirement(
                        CharacterType.TRAINER))));
		roomInfos.put(RoomType.ARENA, arena);

		RoomInfo bar = new RoomInfo(2, 1800, "BAR", "images/backgrounds/bar.jpg");
		bar.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		bar.addActivity(ActivityType.BARTEND, NO_REQUIREMENT);
		roomInfos.put(RoomType.BAR, bar);

		RoomInfo stage = new RoomInfo(1, 2500, "STAGE", "images/backgrounds/stripclub.jpg");
		stage.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		stage.addActivity(ActivityType.STRIP, NO_REQUIREMENT);
		stage.addActivity(ActivityType.CATSHOW, new AllCharacterRequirement (new OrSpecializationRequirement(
                new SpecializationRequirement(SpecializationType.CATGIRL), new TraitRequirement(Trait.SMELLSLIKEKITTEN))));
		roomInfos.put(RoomType.STAGE, stage);

		RoomInfo dungeon = new RoomInfo(2, 4500, "DUNGEON", "images/backgrounds/dungeon.jpg");
		dungeon.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		dungeon.addActivity(ActivityType.BREAK, new AndActivityRequirement(new MinimumCharacterRequirement(
				new CharacterTypeRequirement(CharacterType.TRAINER), 1), new MinimumCharacterRequirement(
				new CharacterTypeRequirement(CharacterType.SLAVE), 1)));
		dungeon.addActivity(ActivityType.DOMINATE, new AndActivityRequirement(new ExactOccupantRequirement(1),
				new MinimumCharacterRequirement(new SpecializationRequirement(SpecializationType.DOMINATRIX), 1)));
		dungeon.addActivity(ActivityType.SUBMIT, new ExactOccupantRequirement(1));
		dungeon.setEventHandler(new SexSatisfactionEventHandler(EventType.ACTIVITY, Sextype.BONDAGE, 30));
		roomInfos.put(RoomType.DUNGEON, dungeon);

		RoomInfo glassroom = new RoomInfo(6, 4000, "GLASSROOM", "images/backgrounds/glassroom.jpg");
		glassroom.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		glassroom.addActivity(ActivityType.TEASE, NO_REQUIREMENT);
		roomInfos.put(RoomType.GLASSROOM, glassroom);

		RoomInfo gloryhole = new RoomInfo(2, 4500, "GLORYHOLE", "images/backgrounds/dungeon.jpg");
		gloryhole.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		gloryhole.addActivity(ActivityType.SUCK, NO_REQUIREMENT);
		roomInfos.put(RoomType.GLORYHOLE, gloryhole);

		RoomInfo cabaret = new RoomInfo(6, 6000, "CABARET", "images/backgrounds/cabaret.png");
		cabaret.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		cabaret.addActivity(ActivityType.STRIP, new MaximumOccupantRequirement(1));
		cabaret.addActivity(ActivityType.ATTEND, new AndActivityRequirement(new MinimumOccupantRequirement(2),
				new MinimumCharacterRequirement(new SpecializationRequirement(SpecializationType.DANCER), 1)));
		roomInfos.put(RoomType.CABARET, cabaret);

		RoomInfo spa = new RoomInfo(6, 4600, "SPAAREA", "images/backgrounds/spaarea.jpg");
		spa.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		spa.addActivity(ActivityType.BATHATTENDANT, NO_REQUIREMENT);
		spa.addActivity(ActivityType.MASSAGE, NO_REQUIREMENT);
		roomInfos.put(RoomType.SPAAREA, spa);

		RoomInfo smalllibrary = new RoomInfo(6, 3500, "SMALLLIBRARY", "images/backgrounds/smalllibrary.jpg");
		smalllibrary.addActivity(ActivityType.TEACH, new AndActivityRequirement(new MinimumOccupantRequirement(2),
				new MinimumCharacterRequirement(new CharacterTypeRequirement(CharacterType.TRAINER), 1)));
		smalllibrary.addActivity(ActivityType.TRAIN, TRAIN_REQUIREMENT);
		smalllibrary.addActivity(ActivityType.TALK, TALK_REQUIREMENT);
		smalllibrary.addActivity(ActivityType.READ, NO_REQUIREMENT);
		smalllibrary.addActivity(ActivityType.SEX, SEX_REQUIREMENT);
		smalllibrary.addActivity(ActivityType.THREESOME, THREESOME_REQUIREMENT);
		smalllibrary.addActivity(ActivityType.ORGY, ORGY_REQUIREMENT);
		roomInfos.put(RoomType.SMALLLIBRARY, smalllibrary);

		RoomInfo classroom = new RoomInfo(6, 2500, "CLASSROOM", "images/backgrounds/classroom.jpg");
		classroom.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		classroom.addActivity(ActivityType.TEACH, new AndActivityRequirement(new MinimumCharacterRequirement(
				new CharacterTypeRequirement(CharacterType.TRAINER), 1), new MinimumOccupantRequirement(2)));
		classroom.addActivity(ActivityType.TRAIN, TRAIN_REQUIREMENT);
		classroom.addActivity(ActivityType.TALK, TALK_REQUIREMENT);
		classroom.addActivity(ActivityType.SEX, SEX_REQUIREMENT);
		classroom.addActivity(ActivityType.THREESOME, THREESOME_REQUIREMENT);
		classroom.addActivity(ActivityType.ORGY, ORGY_REQUIREMENT);
		classroom.setEventHandler(new ClassRoomEventHandler());
		roomInfos.put(RoomType.CLASSROOM, classroom);
		
		RoomInfo lobby = new RoomInfo(4, 8400, "LOBBY", "images/backgrounds/lobby.jpg");
		lobby.addActivity(ActivityType.ADVERTISE, NO_REQUIREMENT);
		lobby.addActivity(ActivityType.PUBLICIZE, NO_REQUIREMENT);
		roomInfos.put(RoomType.LOBBY, lobby);
		
		RoomInfo garden = new RoomInfo(6, 0, "GARDEN", "images/backgrounds/garden.jpg");
		garden.addActivity(ActivityType.RELAX, NO_REQUIREMENT);
		garden.addActivity(ActivityType.GARDENING, NO_REQUIREMENT);
		roomInfos.put(RoomType.GARDEN, garden);
		
		RoomInfo biggarden = new RoomInfo(8, 0, "BIGGARDEN", "images/backgrounds/biggarden.jpg");
		biggarden.addActivity(ActivityType.RELAX, NO_REQUIREMENT);
		biggarden.addActivity(ActivityType.GARDENING, NO_REQUIREMENT);
		roomInfos.put(RoomType.BIGGARDEN, biggarden);
		
		RoomInfo pond = new RoomInfo(8, 0, "POND", "images/backgrounds/pond.jpg");
		pond.addActivity(ActivityType.RELAX, NO_REQUIREMENT);
		pond.addActivity(ActivityType.SOAK, NO_REQUIREMENT);
		pond.addActivity(ActivityType.FISH, NO_REQUIREMENT);
		roomInfos.put(RoomType.POND, pond);
		
		RoomInfo crypt = new RoomInfo(6, 0, "CRYPT", "images/backgrounds/crypt.png");
		crypt.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		crypt.addActivity(ActivityType.READ, NO_REQUIREMENT);
		roomInfos.put(RoomType.CRYPT, crypt);
		
		RoomInfo gym = new RoomInfo(5, 0, "GYM", "images/backgrounds/gym.jpg");
		gym.addActivity(ActivityType.PRACTICE, NO_REQUIREMENT);
		roomInfos.put(RoomType.GYM, gym);
		
		RoomInfo altar = new RoomInfo(4, 0, "ALTAR", "images/backgrounds/altar.jpg");
		altar.addActivity(ActivityType.PRAY, NO_REQUIREMENT);
		altar.addActivity(ActivityType.OFFERINGS, NO_REQUIREMENT);
		roomInfos.put(RoomType.ALTAR, altar);
		
		RoomInfo throneroom = new RoomInfo(1, 0, "THRONEROOM", "images/backgrounds/throneroom.jpg");
		throneroom.addActivity(ActivityType.GOVERN, new AllCharacterRequirement(new SpecializationRequirement(
				SpecializationType.TRAINER)));
		roomInfos.put(RoomType.THRONEROOM, throneroom);
		
		RoomInfo traininggrounds = new RoomInfo(8, 0, "TRAININGGROUNDS", "images/backgrounds/garrison_morning.jpg");
		traininggrounds.addActivity(ActivityType.TRAINTOFIGHT, NO_REQUIREMENT);
		traininggrounds.addActivity(ActivityType.FIGHT, new AllCharacterRequirement(new SpecializationRequirement(
				SpecializationType.FIGHTER)));
		roomInfos.put(RoomType.TRAININGGROUNDS, traininggrounds);
	}

	private static void populateBedroomActivities(final RoomInfo bedroomInfo) {
		bedroomInfo.addActivity(ActivityType.SLEEP, NO_REQUIREMENT);
		bedroomInfo.addActivity(ActivityType.CLEAN, NO_REQUIREMENT);
		bedroomInfo.addActivity(ActivityType.WHORE, new MaximumOccupantRequirement(bedroomInfo.getMaxOccupancy() / 2));
		bedroomInfo.addActivity(ActivityType.SEX, SEX_REQUIREMENT);
		bedroomInfo.addActivity(ActivityType.THREESOME, THREESOME_REQUIREMENT);
		bedroomInfo.addActivity(ActivityType.ORGY, ORGY_REQUIREMENT);
		bedroomInfo.addActivity(ActivityType.TRAIN, TRAIN_REQUIREMENT);
		bedroomInfo.addActivity(ActivityType.TALK, TALK_REQUIREMENT);
		bedroomInfo.addActivity(ActivityType.NURSE, NURSE_REQUIREMENT);
		bedroomInfo.addActivity(ActivityType.PAMPER, PAMPER_REQUIREMENT);
		bedroomInfo.addActivity(ActivityType.BODYWRAP, BODYWRAP_REQUIREMENT);

		bedroomInfo.addChildCareActivity(ActivityType.SLEEP, CHILDCARE_SLEEP_REQUIREMENT);
		bedroomInfo.addChildCareActivity(ActivityType.PLAY, CHILDCARE_PLAY_REQUIREMENT);
		bedroomInfo.addChildCareActivity(ActivityType.NURSE, CHILDCARE_NURSE_REQUIREMENT);
	}

	public static RoomInfo getRoomInfo(final RoomType type) {
		return roomInfos.get(type);
	}
}
