package jasbro.game.items.usableItemEffects;

import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemAddConditionPanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemAddGoldPanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemAddSpecializationPanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemAddTraitPanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemChangeAttributeMaxPanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemChangeAttributePanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemCooldownPanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemEffectChancePanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemRemoveConditionPanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemRemoveTraitPanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemShowMessagePanel;
import jasbro.util.itemEditor.usableItemEffectPanel.UsableItemSpeedUpPregnancyPanel;

import javax.swing.JPanel;

public enum UsableItemEffectType {
	EFFECTCONTAINER(UsableItemEffectContainerImpl.class, null), 
	EFFECTCHANCE(UsableItemEffectChance.class, UsableItemEffectChancePanel.class), 
	CHOOSEONEFFECT(UsableItemChooseOneEffectContainer.class, null),
	ADDCONDITION(UsableItemAddCondition.class, UsableItemAddConditionPanel.class), 
	ADDGOLD(UsableItemAddGold.class, UsableItemAddGoldPanel.class), 
	ADDTRAIT(UsableItemAddTrait.class, UsableItemAddTraitPanel.class),
	ADDSPECIALIZATION(UsableItemAddSpecialization.class, UsableItemAddSpecializationPanel.class), 
	CHANGEATTRIBUTE(UsableItemChangeAttribute.class, UsableItemChangeAttributePanel.class), 
	CHANGEATTRIBUTEMAX(UsableItemChangeAttributeMax.class, UsableItemChangeAttributeMaxPanel.class),
	REMOVECONDITION(UsableItemRemoveCondition.class, UsableItemRemoveConditionPanel.class), 
	REMOVETRAIT(UsableItemRemoveTrait.class, UsableItemRemoveTraitPanel.class),
	SHOWMESSAGE(UsableItemShowMessage.class, UsableItemShowMessagePanel.class),
	COOLDOWN(UsableItemCooldown.class, UsableItemCooldownPanel.class),
	SPEEDUPPREGNANCY(UsableItemSpeedUpPregnancy.class, UsableItemSpeedUpPregnancyPanel.class),
	ADVANCEAGE(UsableItemAdvanceAge.class, null),
	;
	
	private Class<? extends UsableItemEffect> itemEffectClass;
	private Class<? extends JPanel> itemEffectPanelClass;
	
	private UsableItemEffectType(Class<? extends UsableItemEffect> itemEffectClass,
			Class<? extends JPanel> itemEffectPanelClass) {
		this.itemEffectClass = itemEffectClass;
		this.itemEffectPanelClass = itemEffectPanelClass;
	}
	
	public Class<? extends UsableItemEffect> getItemEffectClass() {
		return itemEffectClass;
	}
	
	public Class<? extends JPanel> getItemEffectPanelClass() {
		return itemEffectPanelClass;
	}
}