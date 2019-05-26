package jasbro.game.character.activities.requirements;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;

/**
 * TODO 
 * @author somextra
 *
 */
public interface CharacterRequirement {
	boolean isValid(final ActivityType activity, final Charakter character);
}