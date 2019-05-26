package jasbro;

import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Inventory.ItemData;
import jasbro.game.world.CharacterLocation;
import scala.concurrent.forkjoin.ThreadLocalRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Util {
	private static List<AttributeType> attributeTypes;
	
	public static Random getRnd() {
		return ThreadLocalRandom.current();
	}
	
	public static int getInt(int start, int end) {
		int number = getRnd().nextInt(end-start);
		number += start;
		return number;
	}
	
	public static double getPercent(double amount, int percent) {
		return (amount * (percent / 100.0));
	}
	
	public static List<AttributeType> getAttributeTypes() {
		if (attributeTypes == null) {
			attributeTypes = new ArrayList<AttributeType>();
			for (AttributeType attributeType : EssentialAttributes.values()) {
				attributeTypes.add(attributeType);
			}
			for (AttributeType attributeType : BaseAttributeTypes.values()) {
				attributeTypes.add(attributeType);
			}
			for (AttributeType attributeType : Sextype.values()) {
				attributeTypes.add(attributeType);
			}
			for (AttributeType attributeType : SpecializationAttribute.values()) {
				attributeTypes.add(attributeType);
			}
		}
		return attributeTypes;
	}
	
	public static List<Charakter> getSlaves(List<Charakter> characters) {
		List<Charakter> slaves = new ArrayList<Charakter>();
		for (Charakter character : characters) {
			if (character.getType() == CharacterType.SLAVE) {
				slaves.add(character);
			}
		}
		return slaves;
	}
	
	public static List<Charakter> getTrainers(List<Charakter> characters) {
		List<Charakter> trainers = new ArrayList<Charakter>();
		for (Charakter character : characters) {
			if (character.getType() == CharacterType.TRAINER) {
				trainers.add(character);
			}
		}
		return trainers;
	}
	
	public static GenderAmounts getGenderAmounts(List<Person> people) {
		GenderAmounts genderAmounts = new GenderAmounts();
		for (Person person : people) {
			if (!(person instanceof CustomerGroup)) {
				genderAmounts.add(person.getGender());
			}
			else {
				CustomerGroup customerGroup = (CustomerGroup) person;
				for (Person member : customerGroup.getCustomers()) {
					genderAmounts.add(member.getGender());
				}
			}
		}
		return genderAmounts;
	}
	
	public static class GenderAmounts {
		private Map<Gender, Integer> genderAmountMap = new HashMap<Gender, Integer>();
		
		public GenderAmounts() {
			for (Gender gender : Gender.values()) {
				genderAmountMap.put(gender, 0);
			}
		}
		
		public int getGenderAmount(Gender gender) {
			return genderAmountMap.get(gender);
		}
		
		public void add(Gender gender) {
			genderAmountMap.put(gender, genderAmountMap.get(gender) + 1);
		}
	}
	
	public static List<ItemData> getItemListNormalized(List<ItemData> items) {
		for (int i = 0; i < items.size(); i++) {
			for (int j = i + 1; j < items.size(); j++) {
				if (items.get(i).getItem().getId().equals(items.get(j).getItem().getId())) {
					items.get(i).setAmount(items.get(i).getAmount() + items.get(j).getAmount());
					items.remove(j);
					j--;
				}
			}
		}
		return items;
	}
	
	public static float getAverage(SpecializationType specializationType, Charakter character) {
		List<AttributeType> attributeTypes = specializationType.getAssociatedAttributes();
		if (attributeTypes.size() > 0) {
			float sum = 0;
			for (AttributeType attributeType : attributeTypes) {
				sum += character.getAttribute(attributeType).getInternValue();
			}
			return sum / attributeTypes.size();
		}
		else {
			return 0;
		}
	}
	
	public static boolean isValidFileName(String filename) {
		if (filename != null) {
			return filename.matches("[a-zA-Z0-9_\\-]+");
		}
		else {
			return false;
		}
	}
	
	public static List<Charakter> getAllCharactersSuperLocation(CharacterLocation location) {
		List<Charakter> characters = new ArrayList<Charakter>();
		if (location instanceof Room) {
			List<Room> rooms = ((Room) location).getHouse().getRooms();
			for (Room room : rooms) {
				characters.addAll(room.getCurrentUsage().getCharacters()); 
			}
		}
		else {
			characters.addAll(location.getCurrentUsage().getCharacters());
		}
		return characters;
	}
	
	
	public static TypeAmounts getTypeAmounts(List<Charakter> people) {
		TypeAmounts typeAmounts = new TypeAmounts();
		for (Charakter person : people) {
			typeAmounts.add(person.getType());
			if (person.getType().isChildType()) {
				typeAmounts.setChildPresent(true);
			}
			else {
				typeAmounts.setAdultPresent(true);
			}
		}
		return typeAmounts;
	}
	
	public static class TypeAmounts {
		private Map<CharacterType, Integer> typeAmountMap = new HashMap<CharacterType, Integer>();
		private boolean childPresent = false;
		private boolean adultPresent = false;
		
		public TypeAmounts() {
			for (CharacterType type : CharacterType.values()) {
				typeAmountMap.put(type, 0);
			}
		}
		
		public void add(CharacterType type) {
			typeAmountMap.put(type, typeAmountMap.get(type) + 1);
		}
		
		public boolean isChildPresent() {
			return childPresent;
		}
		
		public void setChildPresent(boolean childPresent) {
			this.childPresent = childPresent;
		}
		
		public Map<CharacterType, Integer> getTypeAmountMap() {
			return typeAmountMap;
		}
		
		public int getTrainerAmount() {
			return typeAmountMap.get(CharacterType.TRAINER);
		}
		
		public int getSlaveAmount() {
			return typeAmountMap.get(CharacterType.SLAVE);
		}
		
		public int getInfantAmount() {
			return typeAmountMap.get(CharacterType.INFANT);
		}
		
		public int getChildAmount() {
			return typeAmountMap.get(CharacterType.CHILD);
		}
		
		public int getTeenAmount() {
			return typeAmountMap.get(CharacterType.TEENAGER);
		}
		
		public boolean isAdultPresent() {
			return adultPresent;
		}
		
		public void setAdultPresent(boolean adultPresent) {
			this.adultPresent = adultPresent;
		}
	}
	
	
	public static List<CharacterBase> getBasesByTypeAndGender(CharacterType characterType, Gender gender, List<CharacterBase> bases) {
		if (gender == Gender.FUTA) {
			gender = Gender.FEMALE;
		}
		List<CharacterBase> retBases = new ArrayList<CharacterBase>();
		for (CharacterBase base : bases) {
			if (characterType == base.getType() && gender == base.getGender()) {
				retBases.add(base);
			}
			else if (characterType == null && gender == base.getGender() &&
					(base.getType() == CharacterType.SLAVE || base.getType() == CharacterType.TRAINER)) {
				retBases.add(base);
			}
		}
		return retBases;
	}
}