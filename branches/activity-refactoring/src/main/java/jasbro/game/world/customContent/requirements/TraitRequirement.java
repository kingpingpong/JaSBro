package jasbro.game.world.customContent.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class TraitRequirement extends TriggerRequirement implements CharacterRequirement {

	private Trait trait;

	@Override
	public boolean isValid(Charakter character, TriggerParent triggerParent) {
		if(character.getTraits().contains(trait)) {
			return true;
		}
		
		return false;
	}

    @Override
    public boolean isValid(TriggerParent triggerParent) throws EvalError {
        return isValid(triggerParent.getCharacters().get(0), triggerParent);
    }
    
    @Override
    public TriggerRequirementType getType() {
        return TriggerRequirementType.TRAITREQUIREMENT;
    }

    public Trait getTrait() {
        return trait;
    }

    public void setTrait(Trait trait) {
        this.trait = trait;
    }
    
    
}
