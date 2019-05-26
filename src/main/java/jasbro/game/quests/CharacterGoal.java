package jasbro.game.quests;

import jasbro.game.character.Charakter;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharacterGoal {
	private HashMap<AttributeType, Integer> attributeValueMap = new HashMap<AttributeType, Integer>();
	private List<SpecializationType> specializations = new ArrayList<SpecializationType>();
	
	public boolean goalReached(Charakter character) {
		for (SpecializationType specialization : specializations) {
			if (!character.getSpecializations().contains(specialization)) {
				return false;
			}
		}
		for (AttributeType attributeType : attributeValueMap.keySet()) {
			if (character.getFinalValue(attributeType) < attributeValueMap.get(attributeType)) {
				return false;
			}
		}
		return true;
	}
	
	public HashMap<AttributeType, Integer> getAttributeValueMap() {
		return attributeValueMap;
	}
	public List<SpecializationType> getSpecializations() {
		return specializations;
	}
	
	public String getDescription() {
		String message = TextUtil.t("quest.requirements") + "\n";
		for (SpecializationType specializationType : getSpecializations()) {
			message += TextUtil.t("quest.requirementSpecialization") + " " + specializationType.getText() + "\n";
		}
		for (AttributeType attributeType : attributeValueMap.keySet()) {
			message += attributeType.getText() + ": " + attributeValueMap.get(attributeType) + " ";
		}
		message += "\n";
		return message;
	}
}