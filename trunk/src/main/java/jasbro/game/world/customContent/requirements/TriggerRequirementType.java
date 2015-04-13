package jasbro.game.world.customContent.requirements;

import jasbro.texts.TextUtil;
import jasbro.util.eventEditor.triggerRequirementPanels.ActivityTypeRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.AttributeRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.ChanceRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.CharacterIdRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.CharacterTypeRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.CodeRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.DayRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.FameRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.LocationTypeRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.MaximumCharacterAmountRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.MinimumCharacterAmountRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.MinimumCharactersMatchRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.MoneyRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.RecurringDayRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.SpecializationRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.TraitRequirementPanel;
import jasbro.util.eventEditor.triggerRequirementPanels.UnlockRequirementPanel;

import javax.swing.JPanel;

public enum TriggerRequirementType {
    ANDREQUIREMENT(AndRequirement.class, null),
    ORREQUIREMENT(OrRequirement.class, null),
    NOTREQUIREMENT(NotRequirement.class, null),
    
    ACTIVITYREQUIREMENT(ActivityRequirement.class, ActivityTypeRequirementPanel.class),
    LOCATIONTYPEREQUIREMENT(LocationTypeRequirement.class, LocationTypeRequirementPanel.class),
    CHANCEREQUIREMENT(ChanceRequirement.class, ChanceRequirementPanel.class),
    
    NOCHILDPRESENTREQUIREMENT(NoChildPresentRequirement.class, null),
    SAMECHARACTERREQUIREMENT(SameCharacterRequirement.class, null),
    RECURRINGDAYREQUIREMENT(RecurringDayRequirement.class, RecurringDayRequirementPanel.class),
    DAYREQUIREMENT(DayRequirement.class, DayRequirementPanel.class),
    MONEYREQUIREMENT(MoneyRequirement.class, MoneyRequirementPanel.class),
    
    ALLCHARACTERSREQUIREMENT(AllCharactersRequirement.class, null),
    MINIMUMCHARACTERSMATCHREQUIREMENT(MinimumCharactersMatchRequirement.class, MinimumCharactersMatchRequirementPanel.class),
    ANYOFOWNEDCHARACTERSREQUIREMENT(AnyOfOwnedCharactersRequirement.class, null),
    MAINCHARACTERREQUIREMENT(MainCharacterRequirement.class, null),
    
    ATTRIBUTEREQUIREMENT(AttributeRequirement.class, AttributeRequirementPanel.class),
    CHARACTERTYPEREQUIREMENT(CharacterTypeRequirement.class, CharacterTypeRequirementPanel.class),
    TRAITREQUIREMENT(TraitRequirement.class, TraitRequirementPanel.class),
    SPECIALIZATIONREQUIREMENT(SpecializationRequirement.class, SpecializationRequirementPanel.class),
    FAMEREQUIREMENT(FameRequirement.class, FameRequirementPanel.class),
    CHARACTERIDREQUIREMENT(CharacterIdRequirement.class, CharacterIdRequirementPanel.class),
    
    EXACTCHARACTERAMOUNTREQUIREMENT(ExactCharacterAmountRequirement.class, null),
    MAXIMUMCHARACTERAMOUNTREQUIREMENT(MaximumCharacterAmountRequirement.class, MaximumCharacterAmountRequirementPanel.class),
    MINIMUMCHARACTERAMOUNTREQUIREMENT(MinimumCharacterAmountRequirement.class, MinimumCharacterAmountRequirementPanel.class),

    
    UNLOCKREQUIREMENT(UnlockRequirement.class, UnlockRequirementPanel.class),
    CHILDCAREREQUIREMENT(ChildCareRequirement.class, null),
	
    CODEREQUIREMENT(CodeRequirement.class, CodeRequirementPanel.class)
	;
	
    private Class<? extends TriggerRequirement> requirementClass;
    private Class<? extends JPanel> requirementPanelClass;
    
    private TriggerRequirementType(Class<? extends TriggerRequirement> requirementClass, Class<? extends JPanel> requirementPanelClass) {
        this.requirementClass = requirementClass;
        this.requirementPanelClass = requirementPanelClass;
    }
    
    public String getText() {
        return TextUtil.t(this.toString());
    }    
    
    public Class<? extends TriggerRequirement> getRequirementClass() {
        return requirementClass;
    }
    public Class<? extends JPanel> getEventPanelClass() {
        return requirementPanelClass;
    }
    
}
