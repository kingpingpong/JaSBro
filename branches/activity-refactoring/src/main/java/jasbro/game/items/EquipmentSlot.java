package jasbro.game.items;

import jasbro.texts.TextUtil;

public enum EquipmentSlot {
	HEAD(EquipmentType.HEAD), 
	DRESS(EquipmentType.DRESS), 
	UNDERWEAR(EquipmentType.UNDERWEAR), 
	SHOES(EquipmentType.SHOES), 
	ACCESSORY1(EquipmentType.ACCESSORY),
	ACCESSORY2(EquipmentType.ACCESSORY), 
	ACCESSORY3(EquipmentType.ACCESSORY), 
	ACCESSORY4(EquipmentType.ACCESSORY);
	
	private EquipmentType equipmentType;
	
	private EquipmentSlot(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}
	
    public String getText() {
    	return TextUtil.t(this.toString());
    }
}
