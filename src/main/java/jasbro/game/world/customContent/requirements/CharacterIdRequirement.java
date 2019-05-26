package jasbro.game.world.customContent.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class CharacterIdRequirement extends TriggerRequirement implements CharacterRequirement {
	private String characterId;
	
	@Override
	public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
		if (characterId == null) {
			return false;
		}
		return character.getBaseId().toUpperCase().equals(characterId.toUpperCase());
	}
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		if (triggerParent.getCharacters() == null || triggerParent.getCharacters().size() == 0) {
			return false;
		}
		return isValid(triggerParent.getCharacters().get(0), triggerParent);
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.CHARACTERIDREQUIREMENT;
	}
	
	public String getCharacterId() {
		return characterId;
	}
	
	public void setCharacterId(String characterId) {
		this.characterId = characterId;
	}
	
	
	
}