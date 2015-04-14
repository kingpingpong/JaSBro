package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.texts.TextUtil;

public class EquipmentChangeArmor extends EquipmentEffect {
	private int amount;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@Override
	public EquipmentEffectType getType() {
		return EquipmentEffectType.CHANGEARMOR;
	}
	
	@Override
	public double modifyCalculatedAttribute(CalculatedAttribute attribute, double value, Charakter character) {
        if (attribute == CalculatedAttribute.ARMORVALUE && amount != 0) {
            return value + amount;
        }
        else {
            return super.modifyCalculatedAttribute(attribute, value, character);
        }
	}
	
    @Override
    public String getDescription() {
        if (amount == 0) {
            return "";
        }
        else if (amount < 0) {
            return TextUtil.t("equipment.valueMinus", new Object[]{CalculatedAttribute.ARMORVALUE.getText(), 
                    amount});
        }
        else {
            return TextUtil.t("equipment.valuePlus", new Object[]{CalculatedAttribute.ARMORVALUE.getText(), 
                    amount});
        }
    }
    
    @Override
    public double getValue() {
        return 15;
    }
    
    @Override
    public int getAmountEffects() {
        return amount;
    }
}
