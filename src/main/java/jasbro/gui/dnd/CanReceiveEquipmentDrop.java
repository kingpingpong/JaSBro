package jasbro.gui.dnd;

import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;

public interface CanReceiveEquipmentDrop {
	public void receiveEquipmentDrop(Equipment equipment);
	public EquipmentSlot getEquipmentSlot();
}