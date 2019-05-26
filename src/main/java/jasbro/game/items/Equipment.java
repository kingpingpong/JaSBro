package jasbro.game.items;

import jasbro.game.character.AttributeModifier;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MyEvent;
import jasbro.game.items.equipmentEffect.EquipmentEffect;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Equipment extends Item implements AttributeModifier {
	
	private List<EquipmentEffect> equipmentEffects = new ArrayList<EquipmentEffect>();
	private EquipmentType equipmentType = EquipmentType.ACCESSORY;
	private AccessoryType accessoryType;
	
	public Equipment(String id) {
		super(id, ItemType.EQUIPMENT);
	}
	
	public Equipment(Item item) {
		super(item);
		setType(ItemType.EQUIPMENT);
	}
	
	@Override
	public String getText() {
		String text = "<b>"+getName() + "</b>\n" +
				TextUtil.t("slotItem", new Object[]{equipmentType.getText()}) + "\n";
		if (equipmentType == EquipmentType.ACCESSORY && accessoryType != null) {
			text +=   TextUtil.t("accessoryType", new Object[]{accessoryType.getText()}) + "\n";
		}
		text += TextUtil.t("valueItem", new Object[]{getValue()}) + "\n" +
				getDescription() + "\n\n";
		for (EquipmentEffect effect : equipmentEffects) {
			text += effect.getDescription() + "\n";
		}
		return text;
	}
	
	@Override
	public float getAttributeModifier(Attribute attribute) {
		float modifier = 0;
		for (EquipmentEffect effect : equipmentEffects) {
			modifier += effect.getAttributeModifier(attribute);
		}
		return modifier;
	}
	
	public void handleEvent(MyEvent e, Charakter character) {
		for (EquipmentEffect effect : equipmentEffects) {
			effect.handleEvent(e, character);
		}
	}
	
	public boolean equip(EquipmentSlot equipmentSlot, Charakter character) {
		boolean equipSuccess = true;
		for (EquipmentEffect effect : equipmentEffects) {
			if (!effect.canEquip(character)) {
				equipSuccess = false;
				break;
			}
		}
		if (equipSuccess) {
			for (EquipmentEffect effect : equipmentEffects) {
				effect.doAtEquip(character);
			}
		}
		return equipSuccess;
	}
	
	public void unequip(EquipmentSlot equipmentSlot, Charakter character) {
		if (character.getCharacterInventory().getItem(equipmentSlot) == this) {
			for (EquipmentEffect effect : equipmentEffects) {
				effect.doAtUnEquip(character);
			}
		}
	}
	
	public void modifyImageTags(List<ImageTag> imageTags) {
		for (EquipmentEffect effect : equipmentEffects) {
			effect.modifyImageTags(imageTags);
		}
	}
	
	public void modifyTraits(List<Trait> traits, Charakter character) {
		for (EquipmentEffect effect : equipmentEffects) {
			effect.modifyTraits(traits, character);
		}
	}
	
	public List<EquipmentEffect> getEquipmentEffects() {
		return equipmentEffects;
	}
	
	public void setEquipmentEffects(List<EquipmentEffect> equipmentEffects) {
		this.equipmentEffects = equipmentEffects;
	}
	
	public EquipmentType getEquipmentType() {
		return equipmentType;
	}
	
	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}
	
	public double modifyCalculatedAttribute(CalculatedAttribute attribute, double value, Charakter character) {
		for (EquipmentEffect effect : equipmentEffects) {
			value = effect.modifyCalculatedAttribute(attribute, value, character);
		}
		return value;
	}
	
	public AccessoryType getAccessoryType() {
		return accessoryType;
	}
	
	public void setAccessoryType(AccessoryType accessoryType) {
		this.accessoryType = accessoryType;
	}
	
	public long calculateValue() {
		double valueSum = 0;
		double valueExp = 1;
		for (EquipmentEffect effect : equipmentEffects) {
			valueSum += effect.getValue() * effect.getAmountEffects();
			for (int i = 0; i < effect.getAmountEffects(); i++)  {
				if (effect.getValue() >= 0) {
					valueExp *= (1 + effect.getValueExponential());
				}
				else {
					valueExp /= - (1 + effect.getValueExponential());
				}
			}
		}
		double valueFinal = (long) (valueSum * valueExp);
		if (equipmentType == EquipmentType.ACCESSORY && accessoryType == AccessoryType.ONEHANDED) {
			valueFinal = valueFinal * 4 / 5;
		}
		else if (equipmentType == EquipmentType.ACCESSORY && accessoryType == AccessoryType.TWOHANDED) {
			valueFinal = valueFinal * 2 / 3;
		}
		
		return (long) valueFinal;
	}
}