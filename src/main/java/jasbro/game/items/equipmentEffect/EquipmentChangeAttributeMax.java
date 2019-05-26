package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public class EquipmentChangeAttributeMax extends EquipmentEffect {
	private AttributeType attributeType;
	private int amount;
	
	@Override
	public void doAtEquip(Charakter character) {
		if (attributeType != null && amount != 0) {
			Attribute attribute = character.getAttribute(attributeType);
			attribute.setMaxValue(attribute.getMaxValue() + amount);
		}
	}
	
	@Override
	public void doAtUnEquip(Charakter character) {
		if (attributeType != null && amount != 0) {
			Attribute attribute = character.getAttribute(attributeType);
			attribute.setMaxValue(attribute.getMaxValue() - amount);
		}
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public AttributeType getAttributeType() {
		return attributeType;
	}
	
	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}
	
	@Override
	public EquipmentEffectType getType() {
		return EquipmentEffectType.CHANGEATTRIBUTEMAX;
	}
	
	@Override
	public String getDescription() {
		if (attributeType == null) {
			return "";
		}
		else if (amount < 0) {
			return TextUtil.t("equipment.attributeMaxMinus", new Object[]{attributeType.getText(), 
					amount});
		}
		else {
			return TextUtil.t("equipment.attributeMaxPlus", new Object[]{attributeType.getText(), 
					amount});
		}
	}
	
	@Override
	public double getValue() {
		if (attributeType == null) {
			return 0;
		}
		else if (attributeType instanceof BaseAttributeTypes) {
			return 300;
		}
		else {
			return 50;
		}
	}
	
	@Override
	public int getAmountEffects() {
		return amount;
	}
}
	