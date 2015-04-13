package jasbro.game.character.activities;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.sub.RefusedToWork;

public class RefusedToWorkExecutor extends DefaultExecutor {

	public void setCausedByCharacter(Charakter obedienceTooLowCharacter) {
		RefusedToWork rtw=(RefusedToWork)runningActivity;
		rtw.setCausedByCharacter(obedienceTooLowCharacter);
	}

	public void setOriginalActivity(ActivityType type) {
		RefusedToWork rtw=(RefusedToWork)runningActivity;
		rtw.setOriginalActivity(type);
	}
}
