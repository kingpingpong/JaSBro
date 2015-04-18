package jasbro.game.items;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CharacterInventory {
    private Map<EquipmentSlot, String> itemMap = new HashMap<EquipmentSlot, String>();
    private Charakter character;

    public CharacterInventory(Charakter character) {
        this.character = character;
        itemMap.put(EquipmentSlot.DRESS, "RegularClothes");
    }

    public Equipment getItem(EquipmentSlot equipmentSlot) {
        if (itemMap.containsKey(equipmentSlot)) {
            return getEquipmentForId(itemMap.get(equipmentSlot));
        } else {
            return null;
        }
    }

    public List<Equipment> listEquipment() {
        List<Equipment> items = new ArrayList<Equipment>();
        Map<String, Item> globalItemMap = Jasbro.getInstance().getItems();
        for (String itemId : itemMap.values()) {
            if (itemId != null && globalItemMap.containsKey(itemId)) {
                items.add((Equipment) globalItemMap.get(itemId));
            }
        }
        return items;
    }

    public List<Equipment> equip(EquipmentSlot equipmentSlot, Equipment equipment) {
        List<Equipment> returnItems = new ArrayList<Equipment>();
        if (equipment.getEquipmentType() != equipmentSlot.getEquipmentType()) {
            returnItems.add(equipment);
            return returnItems;
        }
        if (itemMap.containsKey(equipmentSlot)) {
            returnItems.add(unequip(equipmentSlot));
        }
        boolean success = equipment.equip(equipmentSlot, getCharacter());
        if (success) {
            itemMap.put(equipmentSlot, equipment.getId());
            
            if (equipment.getEquipmentType() == EquipmentType.ACCESSORY) {
                returnItems.addAll(removeInvalidAccessory(equipmentSlot, equipment));
            }
            
            MyEvent event = new MyEvent(EventType.ITEMUSED, equipment);
            getCharacter().handleEvent(event);
            getCharacter().fireEvent(event);
            getCharacter().fireEvent(new MyEvent(EventType.STATUSCHANGE, getCharacter()));
            return returnItems;
        }
        else {
            if (returnItems.size() == 1) {
                returnItems.get(0).equip(equipmentSlot, character);
                itemMap.put(equipmentSlot, returnItems.get(0).getId());
                returnItems.clear();
            }
            returnItems.add(equipment);
            return returnItems;
        }
    }

    public Equipment unequip(EquipmentSlot equipmentSlot) {
        if (itemMap.containsKey(equipmentSlot)) {
            Equipment equipment = getEquipmentForId(itemMap.get(equipmentSlot));
            equipment.unequip(equipmentSlot, getCharacter());
            itemMap.remove(equipmentSlot);
            MyEvent event = new MyEvent(EventType.ITEMUSED, equipment);
            getCharacter().handleEvent(event);
            getCharacter().fireEvent(event);
            return equipment;
        } else {
            return null;
        }
    }

    private Equipment getEquipmentForId(String itemId) {
        Map<String, Item> globalItemMap = Jasbro.getInstance().getItems();
        if (globalItemMap.containsKey(itemId)) {
            Item item = globalItemMap.get(itemId);
            if (item instanceof Equipment) {
                return (Equipment) item;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private Charakter getCharacter() {
        if (character == null) {
            for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                if (character.getCharacterInventory() == this) {
                    this.character = character;
                }
            }
        }
        return character;
    }
    
    private List<Equipment> removeInvalidAccessory(EquipmentSlot nowEquippedSlot, Equipment nowEquippedItem) {
        List<Equipment> removedItems = new ArrayList<Equipment>();
        if (nowEquippedItem.getEquipmentType() == EquipmentType.ACCESSORY && nowEquippedItem.getAccessoryType() != null) {
            int hands = nowEquippedItem.getAccessoryType().getHandsUsed();       
        
            Set<EquipmentSlot> equipmentSlots = new HashSet<EquipmentSlot>(itemMap.keySet());
            for (EquipmentSlot equipmentSlot : equipmentSlots) {
                if (equipmentSlot.getEquipmentType() == EquipmentType.ACCESSORY && equipmentSlot != nowEquippedSlot) {
                    Equipment curEquipment = getItem(equipmentSlot);
                    if (curEquipment.getAccessoryType() != null) {
                        if (curEquipment.getAccessoryType() == nowEquippedItem.getAccessoryType() && 
                                curEquipment.getAccessoryType() != AccessoryType.ONEHANDED) {
                            removedItems.add(unequip(equipmentSlot));
                        }
                        else if (hands + curEquipment.getAccessoryType().getHandsUsed() > 2) {
                            removedItems.add(unequip(equipmentSlot));
                        }
                        else {
                            hands += curEquipment.getAccessoryType().getHandsUsed();
                        }
                    }
                }
            }
        }
        return removedItems;
    }
}
