package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.texts.TextUtil;

public class EquipmentChangeCalculatedAttributeFixed extends EquipmentEffect {
	
	private CalculatedAttribute attributeType;
	private double amount;
	
	@Override
	public EquipmentEffectType getType() {
		return EquipmentEffectType.CHANGECALCULATEDATTRIBUTEFIXED;
	}
	
	@Override
	public double modifyCalculatedAttribute(CalculatedAttribute attribute, double value, Charakter character) {
		if (attribute != null && attribute == attributeType && amount != 0) {
			return value + amount;
		}
		else {
			return super.modifyCalculatedAttribute(attribute, value, character);
		}
	}
	
	public CalculatedAttribute getAttributeType() {
		return attributeType;
	}
	
	public void setAttributeType(CalculatedAttribute attribute) {
		this.attributeType = attribute;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Override
	public String getDescription() {
		if (attributeType == null) {
			return "";
		}
		else if (attributeType == CalculatedAttribute.HIT || attributeType == CalculatedAttribute.DODGE ||
				attributeType == CalculatedAttribute.BLOCKAMOUNT || attributeType == CalculatedAttribute.BLOCKCHANCE ||
				attributeType == CalculatedAttribute.CRITCHANCE || attributeType == CalculatedAttribute.CRITDAMAGEAMOUNT ||
				attributeType == CalculatedAttribute.CHANCEADDITIONALCHILD || attributeType == CalculatedAttribute.PREGNANCYCHANCE) {
			if (amount < 0) {
				return TextUtil.t("equipment.percentMinus", new Object[]{attributeType.getText(), 
						amount});
			}
			else {
				return TextUtil.t("equipment.percentPlus", new Object[]{attributeType.getText(), 
						amount});
			} 
		}
		else {
			if (amount < 0) {
				return TextUtil.t("equipment.valueMinus", new Object[]{attributeType.getText(), 
						amount});
			}
			else {
				return TextUtil.t("equipment.valuePlus", new Object[]{attributeType.getText(), 
						amount});
			}
		}
	}
	
	@Override
	public double getValue() {
		if (attributeType == null) {
			return 0;
		}
		switch (attributeType) {
		case DAMAGE:
			return 150;
		case ARMORVALUE:
			return 15;
		case ARMORPERCENT:
			return 10000;
		case HIT:
			return 75;
		case DODGE:
			return 100;
		case BLOCKAMOUNT:
		case BLOCKCHANCE:
		case CRITCHANCE:
		case CRITDAMAGEAMOUNT:
			return 50;
		case SPEED:
			return 50;
		case MINCHILDREN:
			return 1000;
		case MAXCHILDREN:
			return 1000;
		case AMOUNTCUSTOMERSPERSHIFT:
			return 10000;
		case ITEMLOOTCHANCEMODIFIER:
			return 100;
		case PREGNANCYCHANCE:
			return 10;
		case CHANCEADDITIONALCHILD:
			return 100;
		case CONTROL:
			return 250;
		default:
			return 10;
		}
	}
	
	@Override
	public int getAmountEffects() {
		if (attributeType == null) {
			return 0;
		}
		else if (attributeType == CalculatedAttribute.DAMAGE) {
			return (int) (amount * 10);
		}
		else if (attributeType == CalculatedAttribute.PREGNANCYDURATIONMODIFIER) {
			return (int) (-amount);
		}
		else {
			return(int) (amount);
		}
	}
}