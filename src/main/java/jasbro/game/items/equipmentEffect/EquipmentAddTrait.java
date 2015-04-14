package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.traits.Trait;
import jasbro.texts.TextUtil;

import java.util.List;

public class EquipmentAddTrait extends EquipmentEffect {
	private Trait trait;
	
	@Override
	public void modifyTraits(List<Trait> traits, Charakter character) {
		if (trait != null) {
			if (!traits.contains(trait)) {
				Trait opposedTrait = null;
				for (Trait curTrait : traits) {
					if (trait.isOpposed(curTrait)) {
						opposedTrait = curTrait;
						break;
					}
				}
				if (opposedTrait == null) {
					traits.add(trait);
				}
				else {
					traits.remove(opposedTrait);
				}
			}
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
		return EquipmentEffectType.ADDTRAIT;
	}
	
	@Override
	public String getDescription() {
	    if (trait == null) {
	        return "";
	    }
	    return TextUtil.t("equipment.addTrait", new Object[]{trait.getText()});
	}
	
    @Override
    public double getValue() {
        if (trait != null) {
            return trait.getValueModifier();
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
