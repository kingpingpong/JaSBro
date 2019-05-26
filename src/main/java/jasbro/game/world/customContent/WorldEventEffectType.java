package jasbro.game.world.customContent;

import jasbro.game.world.customContent.effects.WorldEventAbortActivity;
import jasbro.game.world.customContent.effects.WorldEventAddCondition;
import jasbro.game.world.customContent.effects.WorldEventAddToMessage;
import jasbro.game.world.customContent.effects.WorldEventCallAnotherEvent;
import jasbro.game.world.customContent.effects.WorldEventChangeAttribute;
import jasbro.game.world.customContent.effects.WorldEventChangeFame;
import jasbro.game.world.customContent.effects.WorldEventChangeMoney;
import jasbro.game.world.customContent.effects.WorldEventChooseOneEffectContainer;
import jasbro.game.world.customContent.effects.WorldEventCode;
import jasbro.game.world.customContent.effects.WorldEventComment;
import jasbro.game.world.customContent.effects.WorldEventCondition;
import jasbro.game.world.customContent.effects.WorldEventCreateQuest;
import jasbro.game.world.customContent.effects.WorldEventEffectChance;
import jasbro.game.world.customContent.effects.WorldEventEffectContainer;
import jasbro.game.world.customContent.effects.WorldEventGainItem;
import jasbro.game.world.customContent.effects.WorldEventLoadVariable;
import jasbro.game.world.customContent.effects.WorldEventProtectCharacter;
import jasbro.game.world.customContent.effects.WorldEventRemoveCharacter;
import jasbro.game.world.customContent.effects.WorldEventSaveVariable;
import jasbro.game.world.customContent.effects.WorldEventSetQuestStage;
import jasbro.game.world.customContent.effects.WorldEventSetQuestStatus;
import jasbro.game.world.customContent.effects.WorldEventSimpleMessage;
import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.effectPanels.EventEffectAddConditionPanel;
import jasbro.util.eventEditor.effectPanels.EventEffectAddToMessagePanel;
import jasbro.util.eventEditor.effectPanels.EventEffectCallAnotherEventPanel;
import jasbro.util.eventEditor.effectPanels.EventEffectChangeAttributePanel;
import jasbro.util.eventEditor.effectPanels.EventEffectChangeFamePanel;
import jasbro.util.eventEditor.effectPanels.EventEffectChangeMoneyPanel;
import jasbro.util.eventEditor.effectPanels.EventEffectCodePanel;
import jasbro.util.eventEditor.effectPanels.EventEffectCommentPanel;
import jasbro.util.eventEditor.effectPanels.EventEffectConditionPanel;
import jasbro.util.eventEditor.effectPanels.EventEffectCreateQuestPanel;
import jasbro.util.eventEditor.effectPanels.EventEffectGainItemPanel;
import jasbro.util.eventEditor.effectPanels.EventEffectLoadVariablePanel;
import jasbro.util.eventEditor.effectPanels.EventEffectProtectCharacterPanel;
import jasbro.util.eventEditor.effectPanels.EventEffectRemoveCharacterPanel;
import jasbro.util.eventEditor.effectPanels.EventEffectSaveVariablePanel;
import jasbro.util.eventEditor.effectPanels.EventEffectSetQuestStagePanel;
import jasbro.util.eventEditor.effectPanels.EventEffectSetQuestStatusPanel;
import jasbro.util.eventEditor.effectPanels.EventEffectSimpleMessagePanel;

import javax.swing.JPanel;

public enum WorldEventEffectType {
	EFFECTCONTAINER(WorldEventEffectContainer.class, null),
	CONDITION(WorldEventCondition.class, EventEffectConditionPanel.class),
	CHOOSEONEEFFECT(WorldEventChooseOneEffectContainer.class, null),
	EFFECTCHANCE(WorldEventEffectChance.class, null),
	MESSAGE(WorldEventSimpleMessage.class, EventEffectSimpleMessagePanel.class),
	ADDTOMESSAGE(WorldEventAddToMessage.class, EventEffectAddToMessagePanel.class),
	CODE(WorldEventCode.class, EventEffectCodePanel.class),
	COMMENT(WorldEventComment.class, EventEffectCommentPanel.class),
	CHANGEATTRIBUTE(WorldEventChangeAttribute.class, EventEffectChangeAttributePanel.class),
	CHANGEFAME(WorldEventChangeFame.class, EventEffectChangeFamePanel.class),
	SETQUESTSTAGE(WorldEventSetQuestStage.class, EventEffectSetQuestStagePanel.class),
	SETQUESTSTATUS(WorldEventSetQuestStatus.class, EventEffectSetQuestStatusPanel.class),
	SAVEVARIABLE(WorldEventSaveVariable.class, EventEffectSaveVariablePanel.class),
	LOADVARIABLE(WorldEventLoadVariable.class, EventEffectLoadVariablePanel.class),
	GAINITEM(WorldEventGainItem.class, EventEffectGainItemPanel.class),
	CHANGEMONEY(WorldEventChangeMoney.class, EventEffectChangeMoneyPanel.class),
	ADDCONDITION(WorldEventAddCondition.class, EventEffectAddConditionPanel.class),
	ABORTACTIVITY(WorldEventAbortActivity.class, null),
	REMOVECHARACTER(WorldEventRemoveCharacter.class, EventEffectRemoveCharacterPanel.class),
	PROTECTCHARACTER(WorldEventProtectCharacter.class, EventEffectProtectCharacterPanel.class),
	CREATEQUEST(WorldEventCreateQuest.class, EventEffectCreateQuestPanel.class),
	CALLANOTHEREVENT(WorldEventCallAnotherEvent.class, EventEffectCallAnotherEventPanel.class),
	
	
	
	
	;
	
	
	
	private Class<? extends WorldEventEffect> eventClass;
	private Class<? extends JPanel> eventPanelClass;
	
	private WorldEventEffectType(Class<? extends WorldEventEffect> eventClass, Class<? extends JPanel> eventPanelClass) {
		this.eventClass = eventClass;
		this.eventPanelClass = eventPanelClass;
	}
	
	public String getText() {
		return TextUtil.t(this.toString());
	}
	
	public Class<? extends WorldEventEffect> getEventClass() {
		return eventClass;
	}
	public Class<? extends JPanel> getEventPanelClass() {
		return eventPanelClass;
	}
	
	
}