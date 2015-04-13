package jasbro.game.items.equipmentEffect;

import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public class EquipmentChangeAttribute extends EquipmentEffect {
	private AttributeType attributeType;
	private int amount;
	
	@Override
	public float getAttributeModifier(Attribute attribute) {
		if (attribute.getAttributeType() == this.attributeType) {
			return amount;
		}
		else {
			return 0;
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
		return EquipmentEffectType.CHANGEATTRIBUTE;
	}
	
    @Override
    public String getDescription() {
        if (attributeType == null) {
            return "";
        }
        else if (amount < 0) {
            return TextUtil.t("equipment.valueMinus", new Object[]{attributeType.getText(), 
                    amount});
        }
        else {
            return TextUtil.t("equipment.valuePlus", new Object[]{attributeType.getText(), 
                    amount});
        }
    }
    
    @Override
    public double getValue() {
        if (attributeType == null) {
            return 0;
        }
        else if (attributeType instanceof BaseAttributeTypes) {
            return 200;
        }
        else {
            return 25;
        }
    }
    
    @Override
    public int getAmountEffects() {
        return amount;
    }
}
