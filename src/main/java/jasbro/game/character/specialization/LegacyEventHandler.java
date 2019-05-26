package jasbro.game.character.specialization;

import jasbro.game.character.Charakter;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyCharacterEventListener;

public class LegacyEventHandler implements MyCharacterEventListener {
	
	@Override
	public void handleEvent(MyEvent e, Charakter character) {
		if (e.getType() == EventType.NEXTDAY) {
			//character.getAttribute(SpecializationAttribute.EXPERIENCE).addToValue(1);
		}
	}
}
