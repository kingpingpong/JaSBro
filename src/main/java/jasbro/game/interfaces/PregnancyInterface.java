package jasbro.game.interfaces;

import jasbro.game.character.Charakter;
import jasbro.game.events.MyEvent;

public interface PregnancyInterface {
	public void reduceDays(int amount);
	public void modifyDays(int amount);
	public int getDays();
	public void setCharacter(Charakter characer);
	public void handleEvent(MyEvent e);
}