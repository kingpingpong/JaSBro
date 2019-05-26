package jasbro.game.items.equipmentEffect;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public class EquipmentChangeAttributeGain extends EquipmentEffect {
	private AttributeType attributeType;
	private int amountPercent;
	
	@Override
	public void handleEvent(MyEvent e, Charakter character) {
		if (attributeType != null && e.getType() == EventType.ATTRIBUTECHANGE) {
			AttributeModification attributeModification = (AttributeModification) e.getSource();
			if (attributeModification.getAttributeType() == attributeType) {
				float modification = attributeModification.getBaseAmount();
				float change = Math.abs(modification) * amountPercent / 100.0f;
				attributeModification.addModificator(change);
			}
		}
	}
	
	public AttributeType getAttributeType() {
		return attributeType;
	}
	
	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}
	
	public int getAmountPercent() {
		return amountPercent;
	}
	
	public void setAmountPercent(int amountPercent) {
		this.amountPercent = amountPercent;
	}
	
	@Override
	public EquipmentEffectType getType() {
		return EquipmentEffectType.CHANGEATTRIBUTEGAIN;
	}
	
	@Override
	public String getDescription() {
		if (attributeType == null) {
			return "";
		}
		else if (amountPercent < 0) {
			return TextUtil.t("equipment.attributeGainMinus", new Object[]{attributeType.getText(), 
					amountPercent});
		}
		else {
			return TextUtil.t("equipment.attributeGainPlus", new Object[]{attributeType.getText(), 
					amountPercent});
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
		else if (attributeType instanceof EssentialAttributes) {
			return 1000;
		}
		else {
			return 50;
		}
	}
	
	@Override
	public int getAmountEffects() {
		return amountPercent;
	}
}