package jasbro.game.world;

import jasbro.texts.TextUtil;

/**
 * 
 * @author Azrael
 */
public enum Time {
	MORNING, AFTERNOON, NIGHT;

	public String getText() {
		return TextUtil.t(this.toString());
	}

	public Time getNextTimeOfDay() {
		switch (this) {
		case MORNING:
			return AFTERNOON;
		case AFTERNOON:
			return NIGHT;
		case NIGHT:
			return MORNING;
		}
		return null;
	}

	public Time getPreviousTimeOfDay() {
		switch (this) {
		case MORNING:
			return NIGHT;
		case AFTERNOON:
			return MORNING;
		case NIGHT:
			return AFTERNOON;
		}
		return null;
	}

	public boolean isNewDay() {
		return this == MORNING;
	}

}
