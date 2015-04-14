package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.texts.TextUtil;

import java.util.List;

public class EquipmentRemoveTrait extends EquipmentEffect {
	private Trait trait;
	
	@Override
	public void modifyTraits(List<Trait> traits, Charakter character) {
		if (trait != null) {
			traits.remove(trait);
		}
	}

	public Trait getTrait() {
		return trait;
	}

	public void setTrait(Trait trait) {
		this.trait = trait;
	}
	
	@Override
	public EquipmentEffectType getType() {
		return EquipmentEffectType.REMOVETRAIT;
	}
	
    @Override
    public String getDescription() {
        if (trait == null) {
            return "";
        }
        return TextUtil.t("equipment.removeTrait", new Object[] { trait.getText() });
    }
    
    @Override
    public double getValue() {
        if (trait != null) {
            return -trait.getValueModifier();
        }
        else {
            return 0;
        }
    }
    
    @Override
    public int getAmountEffects() {
        if (trait != null) {
            return 1;
        }
        else {
            return 0;
        }
    }
}
