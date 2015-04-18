package jasbro.game.interfaces;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;

public interface MinObedienceModifier {
	public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity);
}
