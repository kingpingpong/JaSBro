/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jasbro.game.character.attributes;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.io.Serializable;

/**
 *
 * @author Azrael
 */
public class Attribute implements Serializable {
	private AttributeType attributeType;
	private float internValue = 0;
	private transient int minValue = 0; //If ever something else than 0 is possible, remove transient
	private int maxValue = 100;
	private Charakter character;
	
	public Attribute(Charakter character, AttributeType attributeType) {
		super();
		this.character = character;
		this.attributeType = attributeType;
		this.maxValue = attributeType.getDefaultMax();
		this.minValue = attributeType.getDefaultMin();
		this.internValue = attributeType.getStartValue();
	}
	
	public Attribute(AttributeType attributeType, float internValue, Charakter character) {
		this(character, attributeType);
		this.internValue = internValue;
		this.maxValue = attributeType.getDefaultMax();
		this.minValue = attributeType.getDefaultMin();
	}
	
	public int getValue() {
		return getCharacter().getFinalValue(getAttributeType());
	}
	
	public int getBonus() {
		return getCharacter().getBonus(getAttributeType());
	}
	
	public float addToValue(float modValue) {
		return addToValue(modValue, null);
	}
	
	public float addToValue(float modValue, boolean fixed) {
		return addToValue(modValue, null, fixed);
	}
	
	public float addToValue(float modValue, RunningActivity activity) {
		return addToValue(modValue, activity, false);
	}
	
	public float addToValue(float modValue, RunningActivity activity, boolean fixed) {
		if (!(attributeType instanceof EssentialAttributes)) {
			modValue = modValue / 10.0f;
			for (SpecializationType specializationType : character.getSpecializations()) {
				if (specializationType.getAssociatedAttributes().contains(attributeType)) {
					modValue = modValue * 10;
					break;
				}
			}
			
			if (!fixed) {
				float factor=1;
				if(this.getInternValue()<10)
					factor=1.0f;
				else if(this.getInternValue()<20)
					factor=0.8f;
				else if(this.getInternValue()<30)
					factor=0.6f;
				else if(this.getInternValue()<40)
					factor=0.4f;
				else if(this.getInternValue()<60)
					factor=0.2f;
				else if(this.getInternValue()<80)
					factor=0.1f;
				else
					factor=0.0f;
				if (attributeType instanceof SpecializationAttribute) {
					modValue *= factor;
					//Intelligence influence
					modValue = modValue + Math.abs(modValue) * (character.getIntelligence() - 5) / 2f / 100f;
					

					//Training level influence
					if (internValue > attributeType.getDefaultMax()) {
						int level = (int) (1 + (internValue - attributeType.getDefaultMax()) / attributeType.getRaiseMaxBy());
						int modPercent = level * 5;
						if (level > 10) {
							modPercent -= (level - 10);
						}
						if (level > 15) {
							modPercent -= (level - 15);
						}
						modValue = modValue - (modPercent * modValue / 100f);
						
					}					
				}
				if  (attributeType instanceof BaseAttributeTypes || attributeType instanceof Sextype)
					modValue *= factor;
			}
		}
		else if (attributeType == EssentialAttributes.ENERGY && !fixed) {
			if (modValue >= 0) {
				modValue = modValue + Math.abs(modValue) * ((character.getStamina() - 5) * 0.5f) / 100f * 0.9f;
			}
			else {
				modValue = modValue + Math.abs(modValue) * ((character.getStamina() - 5) * 0.5f) / 100f * 1.1f;
			}
		}
		else if (attributeType == EssentialAttributes.HEALTH && !fixed) {
			modValue = modValue + Math.abs(modValue) * (character.getStrength() - 5) * 0.5f / 100f;
		}
		
		internValue = internValue + modValue;
		if (internValue < minValue) {
			modValue = modValue - (internValue - minValue);
			internValue = minValue;
		} else if (internValue > maxValue) {
			modValue = modValue - (internValue - maxValue);
			internValue = maxValue;
		}
		
		if (modValue != 0 || internValue == minValue) {
			MyEvent event = new AttributeChangedEvent(EventType.ATTRIBUTECHANGED, this, modValue, activity);
			getCharacter().handleEvent(event);
		}
		
		return modValue;
	}
	
	public String getNameResolved() {
		return TextUtil.t(attributeType.toString());
	}
	
	public float getInternValue() {
		return internValue;
	}
	public void setInternValue(float internValue) {
		this.internValue = internValue;
	}
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	public String getAttributeName() {
		return attributeType.toString();
	}
	
	public ImageData getIcon() {
		return new ImageData("images/icons/"+attributeType.toString().toLowerCase()+".png");
	}
	
	public int getMinValue() {
		return minValue;
	}
	
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	
	public Charakter getCharacter() {
		return character;
	}
	
	public AttributeType getAttributeType() {
		return attributeType;
	}
	
	public boolean isMaxed() {
		return getMaxValue() == internValue;
	}
	
	
}