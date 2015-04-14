package jasbro.game.items.equipmentEffect;

import jasbro.texts.TextUtil;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentAddImageTagPanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentAddTraitPanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentAttributeRequirementPanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentChangeArmorPanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentChangeAttackPanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentChangeAttributeGainPanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentChangeAttributeMaxPanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentChangeAttributePanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentChangeCalculatedAttributeFixedPanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentChangeCustomerTypeSatisfactionPanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentDailyAttributeChangePanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentRemoveTraitPanel;
import jasbro.util.itemEditor.equipmentEffectPanel.EquipmentTraitRequirementPanel;

import javax.swing.JPanel;

public enum EquipmentEffectType {
	CHANGEATTRIBUTE(EquipmentChangeAttribute.class, EquipmentChangeAttributePanel.class), 
	ADDIMAGETAG(EquipmentAddImageTag.class, EquipmentAddImageTagPanel.class), 
	CHANGEARMOR(EquipmentChangeArmor.class, EquipmentChangeArmorPanel.class),
	CHANGEATTACK(EquipmentChangeAttack.class, EquipmentChangeAttackPanel.class), 
	CHANGEATTRIBUTEGAIN(EquipmentChangeAttributeGain.class, EquipmentChangeAttributeGainPanel.class),
	CHANGEATTRIBUTEMAX(EquipmentChangeAttributeMax.class, EquipmentChangeAttributeMaxPanel.class),
	DAILYATTRIBUTECHANGE(EquipmentDailyAttributeChange.class, EquipmentDailyAttributeChangePanel.class),
	ADDTRAIT(EquipmentAddTrait.class, EquipmentAddTraitPanel.class), 
	REMOVETRAIT(EquipmentRemoveTrait.class, EquipmentRemoveTraitPanel.class),
	CHANGECUSTOMERTYPESATISFACTION(EquipmentChangeCustomerTypeSatisfaction.class, EquipmentChangeCustomerTypeSatisfactionPanel.class),
	CHANGECALCULATEDATTRIBUTEFIXED(EquipmentChangeCalculatedAttributeFixed.class, EquipmentChangeCalculatedAttributeFixedPanel.class),
	TRAITREQUIREMENT(EquipmentTraitRequirement.class, EquipmentTraitRequirementPanel.class),
	ATTRIBUTEREQUIREMENT(EquipmentAttributeRequirement.class, EquipmentAttributeRequirementPanel.class),;
	
	private Class<? extends EquipmentEffect> itemEffectClass;
	private Class<? extends JPanel> itemEffectPanelClass;
	
	private EquipmentEffectType(Class<? extends EquipmentEffect> itemEffectClass,
			Class<? extends JPanel> itemEffectPanelClass) {
		this.itemEffectClass = itemEffectClass;
		this.itemEffectPanelClass = itemEffectPanelClass;
	}

	public Class<? extends EquipmentEffect> getItemEffectClass() {
		return itemEffectClass;
	}

	public Class<? extends JPanel> getItemEffectPanelClass() {
		return itemEffectPanelClass;
	}
	
    public String getText() {
        if (TextUtil.containsKey(this.toString())) {
            return TextUtil.t(this.toString());
        }
        else {
            return this.toString();
        }
    }
}
