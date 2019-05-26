package jasbro.game.world.customContent.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.customContent.TriggerParent;
import bsh.EvalError;

public class AttributeRequirement extends TriggerRequirement implements CharacterRequirement {
	private AttributeType attributeType;
	private int amount;
	private Comparison comparison = Comparison.GREATERTHAN;
	
	
	@Override
	public boolean isValid(Charakter character, TriggerParent triggerParent) throws EvalError {
		if (attributeType != null) {
			double attributeValue = character.getAnyAttributeValue(attributeType);
			switch(comparison) {
			case GREATERTHAN:
				return attributeValue > amount;
			case LESSTHAN:
				return attributeValue < amount;
			case EQUAL:
				return attributeValue == amount;
			}
		}
		return false;
	}
	
	@Override
	public boolean isValid(TriggerParent triggerParent) throws EvalError {
		if (triggerParent.getCharacters() == null || triggerParent.getCharacters().size() == 0) {
			return false;
		}
		return isValid(triggerParent.getCharacters().get(0), triggerParent);
	}
	
	@Override
	public TriggerRequirementType getType() {
		return TriggerRequirementType.ATTRIBUTEREQUIREMENT;
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
	
	public Comparison getComparison() {
		return comparison;
	}
	
	public void setComparison(Comparison comparison) {
		this.comparison = comparison;
	}
	
	
}