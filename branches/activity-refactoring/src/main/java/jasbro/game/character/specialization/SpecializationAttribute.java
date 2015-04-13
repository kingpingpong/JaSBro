package jasbro.game.character.specialization;

import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public enum SpecializationAttribute implements AttributeType {
    COOKING, CLEANING,
    SEDUCTION, 
    VETERAN, BARTENDING, PICKPOCKETING, CATGIRL,
    MEDICALKNOWLEDGE, WELLNESS,
    STRIP, DOMINATE,
    ADVERTISING, GENETICADAPTABILITY;
    
    public String getText() {
    	return TextUtil.t(this.toString());
    }
    
	@Override
	public int getDefaultMin() {
		return 0;
	}
	@Override
	public int getDefaultMax() {
	    if (this == GENETICADAPTABILITY) {
	        return 50;
	    }
	    else {
	        return 100;
	    }
	}
	@Override
	public int getRaiseMaxBy() {
		return 50;
	}	
	@Override
	public int getStartValue() {
	    return 0;
	}
}
