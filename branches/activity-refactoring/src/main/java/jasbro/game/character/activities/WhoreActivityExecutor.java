package jasbro.game.character.activities;

import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.attributes.Sextype;

public class WhoreActivityExecutor extends BusinessActivityExecutor {

	public Float getAmountActions() {
		return ((Whore)runningActivity).getAmountActions();
	}

	public Sextype getSexType() {
		return ((Whore)runningActivity).getSexType();
	}

}
