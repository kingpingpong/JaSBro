package jasbro.game.events.business;

import jasbro.texts.TextUtil;

public enum Satisfaction {
	OUTRAGED(-9999, 1, -3), 
	FURIOUS(-30, 2, -2.5f), 
	BITTER(-20, 5, -2.0f), 
	ANGRY(-10, 7, -1.5f), 
	FRUSTRATED(0, 10, -1.0f),
	DISGRUNTLED(10, 12, -0.5f), 
	GRUMPY(20, 20, -0.1f), 
	DISAPPOINTED(30, 30, -0.05f), 
	BORED(40, 60, 0.05f), 
	UNSATISFIED(50, 80, 0.1f), 
	INDIFFERENT(60, 100, 0.2f), 
	SATISFIED(70, 120, 0.4f), 
	CONTENT(100, 140, 0.5f), 
	PLEASED(150, 160, 0.6f), 
	CHEERFUL(200, 180, 0.8f), 
	HAPPY(250, 200, 1f), 
	EXCITED(300, 250, 2f), 
	ECSTATIC(350, 400, 3f), 
	EXHILARATED(400, 500, 4f), 
	OVERJOYED(800, 1000, 10f), 
	ENTHRALLED(1600, 2000, 15f),
	ASCENDED(3000, 5000, 30f);
	
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
