package jasbro.game.character.attributes;

import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public enum EssentialAttributes implements AttributeType {
    HEALTH, ENERGY;
    
    private int startValue = 0;
    
    private EssentialAttributes() {
    }
    
    private EssentialAttributes(int startValue) {
        this.startValue = startValue;
    }
        
    public String getText() {
    	return TextUtil.t(this.toString());
    }
    
	@Override
	public int getDefaultMin() {
		return 0;
	}
	@Override
	public int getDefaultMax() {
		return 100;
	}
	@Override
	public int getRaiseMaxBy() {
		return 10;
	}
	
	@Override
	public int getStartValue() {
	    return startValue;
	}
	
    public String getDescription() {
        String text = TextUtil.tNoCheck(this.toString() + ".description");
        if (!(this.toString() + ".description").equals(text)) {
            return text;
        } else {
            return null;
        }
    }
}
