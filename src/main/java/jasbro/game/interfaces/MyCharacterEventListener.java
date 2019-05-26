package jasbro.game.interfaces;

import jasbro.game.character.Charakter;
import jasbro.game.events.MyEvent;

public interface MyCharacterEventListener {
	public void handleEvent(MyEvent e, Charakter character);
}