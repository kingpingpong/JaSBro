package jasbro.game.events.business;

import jasbro.texts.TextUtil;

public enum Satisfaction {
	OUTRAGED(0, 1, -3), 
	FURIOUS(3, 2, -2.5f), 
	BITTER(6, 5, -2.0f), 
	ANGRY(9, 7, -1.5f), 
	FRUSTRATED(12, 10, -1.0f),
	DISGRUNTLED(15, 12, -0.5f), 
	GRUMPY(18, 20, -0.1f), 
	DISAPPOINTED(21, 30, -0.05f), 
	BORED(24, 60, 0.05f), 
	UNSATISFIED(27, 80, 0.1f), 
	INDIFFERENT(30, 100, 0.2f), 
	SATISFIED(35, 120, 0.4f), 
	CONTENT(40, 140, 0.5f), 
	PLEASED(45, 160, 0.6f), 
	CHEERFUL(50, 180, 0.8f), 
	HAPPY(55, 200, 1f), 
	EXCITED(60, 250, 2f), 
	ECSTATIC(70, 400, 3f), 
	EXHILARATED(80, 500, 4f), 
	OVERJOYED(90, 1000, 10f), 
	ENTHRALLED(100, 2000, 15f);
	
	private int moneyModifierPercent;
	private float fameModifier;
	private int minSatisfaction;
	
	private Satisfaction(int minSatisfaction, int moneyModifierPercent, float fameModifier) {
		this.minSatisfaction = minSatisfaction;
		this.moneyModifierPercent = moneyModifierPercent;
		this.fameModifier = fameModifier;
	}
	
	public String getText() {
		return TextUtil.t(this.toString());
	}
	
	public int getMoneyModifierPercent() {
		return moneyModifierPercent;
	}
	
	public static Satisfaction getSatisfaction(int satisfactionValue) {
		Satisfaction previousValue = null;
		for (Satisfaction satisfaction : Satisfaction.values()) {
			if (previousValue != null && satisfaction.getMinSatisfaction() > satisfactionValue) {
				return previousValue;
			}
			previousValue = satisfaction;
		}
		return ENTHRALLED;
	}
	
	public float getFameModifier() {
		return fameModifier;
	}
	
	public int getMinSatisfaction() {
		return minSatisfaction;
	}
	
	
}