package jasbro.game.character.specialization;

import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public enum SpecializationAttribute implements AttributeType {
	COOKING, CLEANING,
	SEDUCTION, 
	EXPERIENCE, 
	VETERAN, BARTENDING, PICKPOCKETING, CATGIRL, AGILITY,
	MEDICALKNOWLEDGE, MAGIC,
	STRIP, DOMINATE,
	PLANTKNOWLEDGE, 
	ADVERTISING, TRANSFORMATION, GENETICADAPTABILITY;
	
	public String getText() {
		return TextUtil.t(this.toString());
	}
	
	@Override
	public int getDefaultMin() {
		return 0;
	}
	@Override
	public int getDefaultMax() {
		if (this == EXPERIENCE) {
			return 20;
		}
		else {
			return 20;
		}
	}
	@Override
	public int getRaiseMaxBy() {
		return 10;
	}	
	@Override
	public int getStartValue() {
		return 0;
	}
}