package jasbro.game.character.attributes;

import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public enum BaseAttributeTypes implements AttributeType {
	CHARISMA, OBEDIENCE, COMMAND, STAMINA, INTELLIGENCE, STRENGTH;
	
	public String getText() {
		return TextUtil.t(this.toString());
	}
	
	@Override
	public int getDefaultMin() {
		return 0;
	}
	@Override
	public int getDefaultMax() {
		return 20;
	}
	@Override
	public int getRaiseMaxBy() {
		return 10;
	}
	
	@Override
	public int getStartValue() {
		return 1;
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