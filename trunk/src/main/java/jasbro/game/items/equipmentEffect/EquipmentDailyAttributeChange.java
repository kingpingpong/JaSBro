package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public class EquipmentDailyAttributeChange extends EquipmentEffect {
	private AttributeType attributeType;
	private int amount;
	
	@Override
	public void handleEvent(MyEvent e, Charakter character) {
		if (attributeType != null && e.getType() == EventType.NEXTDAY) {
			character.getAttribute(attributeType).addToValue(amount);
		}
	}

	public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	@Override
	public EquipmentEffectType getType() {
		return EquipmentEffectType.DAILYATTRIBUTECHANGE;
	}
	
    @Override
    public String getDescription() {
        if (attributeType == null) {
            return "";
        }
        else if (amount < 0) {
            return TextUtil.t("equipment.dailyMinus", new Object[]{attributeType.getText(), 
                    amount});
        }
        else {
            return TextUtil.t("equipment.dailyPlus", new Object[]{attributeType.getText(), 
                    amount});
        }
    }
    
    @Override
    public double getValue() {
        if (attributeType == null) {
            return 0;
        }
        else if (attributeType instanceof BaseAttributeTypes) {
            return 10000;
        }
        else if (attributeType instanceof EssentialAttributes) {
            return 1000;
        }
        else {
            return 2500;
        }
    }
    
    @Override
    public int getAmountEffects() {
        return amount;
    }
}
