package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.texts.TextUtil;

public class EquipmentTraitRequirement extends EquipmentEffect {
    private Trait trait;

    @Override
    public EquipmentEffectType getType() {
        return EquipmentEffectType.TRAITREQUIREMENT;
    }
    
    @Override
    public boolean canEquip(Charakter character) {
        if (trait != null) {
            if (!character.getTraits().contains(trait)) {
                return false;
            }
        }
        return true;
    }

    public Trait getTrait() {
        return trait;
    }

    public void setTrait(Trait trait) {
        this.trait = trait;
    }
    
    @Override
    public String getDescription() {
        if (trait == null) {
            return "";
        }
        return TextUtil.t("equipment.traitRequirement", new Object[]{trait.getText()});
    }
    
    @Override
    public double getValue() {
        return 0;
    }
    
    @Override
    public int getAmountEffects() {
        return 0;
    }
}
