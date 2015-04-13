package jasbro.game.character;

import jasbro.Jasbro;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.warnings.Warning;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeModifier;
import jasbro.game.interfaces.MinObedienceModifier;
import jasbro.game.interfaces.MoneyEarnedModifier;
import jasbro.game.interfaces.MyEventListener;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;

import java.io.Serializable;
import java.util.List;

public abstract class Condition implements MyEventListener, Serializable, AttributeModifier, 
	    MoneyEarnedModifier, MinObedienceModifier {
    private Charakter character;
    
    @Override
    public void handleEvent(MyEvent e) {
    }

    public Charakter getCharacter() {
    	if (character == null) {
    		for (Charakter curCharacter : Jasbro.getInstance().getData().getCharacters()) {
    			if (curCharacter.getConditions().contains(this)) {
    				character = curCharacter;
    				break;
    			}
    		}
    	}
    	
        return character;
    }

    public void setCharacter(Charakter character) {
        this.character = character;
    }
    
    public String getName() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public ImageData getIcon() {
        return null;
    }
    
    public void init() {
		boolean existingCondition = false;
		for (Condition condition : getCharacter().getConditions()) {
			if (condition != this && this.getClass().isInstance(condition)) {
				existingCondition = true;
			}
		}
		if (existingCondition) {
			character.getConditions().remove(this);
		}
    }
    
    public void removeThis() {
        getCharacter().getConditions().remove(this);
    }
    
    @Override
    public float getAttributeModifier(Attribute attribute) {
    	return 0;
    }
    
    @Override
    public float getMoneyModifier(float currentModifier, Charakter character) {
    	return currentModifier;
    }
    
    @Override
    public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
    	return curMinObedience;
    }

    public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
        return currentValue;
    }

    public void modifyWarnings(List<Warning> warnings) {
    }
    
    public void modifyImageTags(List<ImageTag> imageTags) {
    }
}
