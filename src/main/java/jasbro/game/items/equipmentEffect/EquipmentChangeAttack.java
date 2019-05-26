package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.texts.TextUtil;

public class EquipmentChangeAttack extends EquipmentEffect {
	private float amount;
	
	@Override
	public double modifyCalculatedAttribute(CalculatedAttribute attribute, double value, Charakter character) {
		if (attribute != null && attribute == CalculatedAttribute.DAMAGE && amount != 0) {
			return value + amount;
		}
		else {
			return super.modifyCalculatedAttribute(attribute, value, character);
		}
	}
	
	public float getAmount() {
		return amount;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	@Override
	public EquipmentEffectType getType() {
		return EquipmentEffectType.CHANGEATTACK;
	}
	
	@Override
	public String getDescription() {
		if (amount == 0) {
			return "";
		}
		else if (amount < 0) {
			return TextUtil.t("equipment.valueMinus", new Object[]{CalculatedAttribute.DAMAGE.getText(), 
					amount});
		}
		else {
			return TextUtil.t("equipment.valuePlus", new Object[]{CalculatedAttribute.DAMAGE.getText(), 
					amount});
		}
	}
	
	@Override
	public double getValue() {
		return 150;
	}
	
	@Override
	public int getAmountEffects() {
		return (int) (amount * 10);
	}
}