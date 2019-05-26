package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public class EquipmentAttributeRequirement extends EquipmentEffect {
	private AttributeType attributeType;
	private int amount;
	
	@Override
	public boolean canEquip(Charakter character) {
		if (attributeType != null && amount > 0) {
			if (character.getAttribute(attributeType).getInternValue() < amount) {
				return false;
			}
		}
		return true;
	}
	
	public AttributeType getAttributeType() {
		return attributeType;
	}
	
	public void setAttributeType(AttributeType attribute) {
		this.attributeType = attribute;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@Override
	public EquipmentEffectType getType() {
		return EquipmentEffectType.ATTRIBUTEREQUIREMENT;
	}
	
	@Override
	public String getDescription() {
		if (attributeType == null) {
			return "";
		}
		return TextUtil.t("equipment.attributeRequirement", new Object[]{amount, attributeType.getText()});
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