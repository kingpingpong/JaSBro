package jasbro.game.housing;

import jasbro.texts.TextUtil;

public enum CleanState {
	
	SPOTLESS(6), CLEAN(3), TIDY(0), MESSY(-5), DIRTY(-10), FILTHY(-50);
	
	private int satisfactionModifier;
	
	private CleanState(int satisfactionModifier) {
		this.satisfactionModifier = satisfactionModifier;
	}
	
	public String getText() {
		return TextUtil.t(this.toString());
	}
	
	public int getSatisfactionModifier() {
		return satisfactionModifier;
	}
	
	public static CleanState calcState(House house) {
		int amount = house.getDirt() / house.getRoomAmount();
		if (amount < 5) {
			return SPOTLESS;
		}
		if (amount < 15) {
			return CLEAN;
		}
		if (amount < 25) {
			return TIDY;
		}
		if (amount < 35) {
			return MESSY;
		}
		if (amount < 45) {
			return DIRTY;
		}
		else {
			return FILTHY;
		}
	}
}