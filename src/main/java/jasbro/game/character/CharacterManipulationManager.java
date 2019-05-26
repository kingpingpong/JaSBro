package jasbro.game.character;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.PerkHandler;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;


public class CharacterManipulationManager {
	
	public static boolean advanceAge(Charakter character) {
		if (character.getType() == CharacterType.INFANT) {
			changeBase(character, getChildBase(character));
			changeType(character, CharacterType.CHILD);
		}
		else if (character.getType() == CharacterType.CHILD) {
			changeBase(character, getTeenagerBase(character));
			changeType(character, CharacterType.TEENAGER);
		}
		else if (character.getType() == CharacterType.TEENAGER) {
			List<SelectionData<CharacterType>> options = new ArrayList<SelectionData<CharacterType>>();
			changeBase(character, getAdultBase(character));
			options.add(new SelectionData<CharacterType>(CharacterType.TRAINER, CharacterType.TRAINER.getText()));
			options.add(new SelectionData<CharacterType>(CharacterType.SLAVE, CharacterType.SLAVE.getText()));
			SelectionData<CharacterType> selectedOption = new SelectionScreen<CharacterType>().select(options, 
					ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character), null, 
					new ImageData("images/backgrounds/sky.jpg"), 
					TextUtil.t("child.ofAge", character));
			CharacterType newType = selectedOption.getSelectionObject();
			changeType(character, newType);
			character.setOwnership(Ownership.OWNED);
		}
		return false;
	}
	
	public static boolean reduceAge(Charakter character) {
		//TODO
		return false;
	}
	
	public static boolean changeType(Charakter character, CharacterType newType) {
		if (character.getType() == newType) {
			return false;
		}
		character.setType(newType);
		character.removeSpecialization(SpecializationType.SLAVE);
		character.removeSpecialization(SpecializationType.TRAINER);
		character.removeSpecialization(SpecializationType.UNDERAGE);
		//character.getSpecializations().remove(SpecializationType.SLAVE);
		//character.getSpecializations().remove(SpecializationType.TRAINER);
		//character.getSpecializations().remove(SpecializationType.UNDERAGE);
		
		switch (newType) {
		case INFANT:
		case CHILD:
		case TEENAGER:
			character.addSpecialization(SpecializationType.UNDERAGE);
			//character.getSpecializations().add(SpecializationType.UNDERAGE);
			break;
		case SLAVE:
			character.addSpecialization(SpecializationType.SLAVE);
			//character.getSpecializations().add(SpecializationType.SLAVE);
			if (character.getObedience() < 1) {
				character.getAttribute(BaseAttributeTypes.OBEDIENCE).setInternValue(1);
			}
			if (!character.getSpecializations().contains(SpecializationType.SEX)) {
				character.addSpecialization(SpecializationType.SEX);
				//character.getSpecializations().add(SpecializationType.SEX);
			}
			break;
		case TRAINER:
			character.addSpecialization(SpecializationType.TRAINER);
			//character.getSpecializations().add(SpecializationType.TRAINER);
			if (character.getCommand() < 1) {
				character.getAttribute(BaseAttributeTypes.COMMAND).setInternValue(1);
			}
			if (!character.getSpecializations().contains(SpecializationType.SEX)) {
				character.addSpecialization(SpecializationType.SEX);
				//character.getSpecializations().add(SpecializationType.SEX);
			}
			break;
		default:
			break;
		}
		
		PerkHandler.resetPerks(character);
		
		return true;
	}
	
	public static void changeBase(Charakter character, CharacterBase base) {
		character.setBase(base);
		character.setBaseId(base.getId());
		character.setIcon(null);
	}
	
	public static CharacterBase getInfantBase(Charakter character) {
		String baseId = character.getAgeProgressionData().getInfantBase();
		if (baseId != null) {
			for (CharacterBase base : Jasbro.getInstance().getCharacterBases()) {
				if (base.getId().equals(baseId)) {
					return base;
				}
			}
		}
		List<CharacterBase> options = Util.getBasesByTypeAndGender(CharacterType.INFANT, character.getGender(), Jasbro.getInstance().getCharacterBases());
		CharacterBase base = options.get(Util.getInt(0, options.size()));
		character.getAgeProgressionData().setInfantBase(base.getId());
		return base;
	}
	
	public static CharacterBase getChildBase(Charakter character) {
		String baseId = character.getAgeProgressionData().getChildBase();
		if (baseId != null) {
			for (CharacterBase base : Jasbro.getInstance().getCharacterBases()) {
				if (base.getId().equals(baseId)) {
					return base;
				}
			}
		}
		List<CharacterBase> options = Util.getBasesByTypeAndGender(CharacterType.CHILD, character.getGender(), Jasbro.getInstance().getCharacterBases());
		CharacterBase base = options.get(Util.getInt(0, options.size()));
		character.getAgeProgressionData().setChildBase(base.getId());
		return base;
	}
	
	public static CharacterBase getTeenagerBase(Charakter character) {
		String baseId = character.getAgeProgressionData().getTeenagerBase();
		if (baseId != null) {
			for (CharacterBase base : Jasbro.getInstance().getCharacterBases()) {
				if (base.getId().equals(baseId)) {
					return base;
				}
			}
		}
		List<CharacterBase> options = Util.getBasesByTypeAndGender(CharacterType.TEENAGER, character.getGender(), Jasbro.getInstance().getCharacterBases());
		CharacterBase base = options.get(Util.getInt(0, options.size()));
		character.getAgeProgressionData().setTeenagerBase(base.getId());
		return base;
	}
	
	public static CharacterBase getAdultBase(Charakter character) {
		for (String baseId : character.getAgeProgressionData().getAdultBases()) {
			for (CharacterBase base : Jasbro.getInstance().getCharacterBases()) {
				if (base.getId().equals(baseId)) {
					return base;
				}
			}
		}
		List<CharacterBase> options = Util.getBasesByTypeAndGender(null, character.getGender(), Jasbro.getInstance().getCharacterBases());
		CharacterBase base = options.get(Util.getInt(0, options.size()));
		character.getAgeProgressionData().getAdultBases().clear();
		character.getAgeProgressionData().getAdultBases().add(base.getId());
		return base;
	}
}