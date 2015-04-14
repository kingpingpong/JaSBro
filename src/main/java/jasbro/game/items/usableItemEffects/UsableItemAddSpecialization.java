package jasbro.game.items.usableItemEffects;

import jasbro.game.character.Charakter;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.items.Item;

public class UsableItemAddSpecialization extends UsableItemEffect {
    private SpecializationType specializationType;
    
    @Override
    public String getName() {
        return "Add specialization";
    }

    @Override
    public void apply(Charakter character, Item item) {
        if (specializationType != null && !character.getSpecializations().contains(specializationType)) {
            character.getSpecializations().add(specializationType);
        }
    }

    @Override
    public UsableItemEffectType getType() {
        return UsableItemEffectType.ADDSPECIALIZATION;
    }

    public SpecializationType getSpecializationType() {
        return specializationType;
    }

    public void setSpecializationType(SpecializationType specializationType) {
        this.specializationType = specializationType;
    }

    
}
