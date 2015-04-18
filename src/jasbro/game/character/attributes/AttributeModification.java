package jasbro.game.character.attributes;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;

public class AttributeModification {
    private float baseAmount;
    private AttributeType attributeType;
    private Charakter targetCharacter;
    private float fluctuation = 0.05f;
    private float realModification;
    private float modificators;
    private boolean cancelled = false;
    
    public AttributeModification(float baseAmount, AttributeType attributeType, Charakter targetCharakter) {
        super();
        this.baseAmount = baseAmount;
        this.attributeType = attributeType;
        this.targetCharacter = targetCharakter;
    }

    public void addModificator(Float value) {
        modificators += value;
    }
    
    public void applyModification() {
    	applyModification(null);
    }
    
    public void applyModification(RunningActivity activity) {
    	if (realModification == 0) {
	        MyEvent event = new MyEvent(EventType.ATTRIBUTECHANGE, this);
	        targetCharacter.handleEvent(event);
	        if (!cancelled) {
	            Attribute attribute = targetCharacter.getAttribute(attributeType);
	            realModification = attribute.addToValue(getFinalModification(), activity);
	        }
    	}
    }
    
    public Float getFinalModification() {
        float finalValue = baseAmount + modificators;
        
        float flucValue = Util.getRnd().nextFloat() * fluctuation;
        boolean plusMinus = Util.getRnd().nextBoolean();
        if (plusMinus) {
        	finalValue = finalValue + (finalValue * flucValue);
        }
        else {
        	finalValue = finalValue - (finalValue * flucValue);
        }
        return finalValue;
    }
    
    public float getBaseAmount() {
        return baseAmount;
    }
    public void setBaseAmount(float baseAmount) {
        this.baseAmount = baseAmount;
    }
    public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public Charakter getTargetCharacter() {
		return targetCharacter;
	}
	public float getRealModification() {
		return realModification;
	}

	public void setRealModification(float realModification) {
		this.realModification = realModification;
	}

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    
}
