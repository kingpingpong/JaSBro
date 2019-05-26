package jasbro.game.character.conditions;


import jasbro.game.character.Condition;
import jasbro.game.character.battle.Unit;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class BattleCondition extends Condition {
	private Unit unit;
	
	public BattleCondition(Unit unit) {
		this.unit = unit;
	}
	
	public Unit getUnit() {
		return unit;
	}
	
	@Override
	public void handleEvent(MyEvent e) {
		if (e.getType() == EventType.NEXTSHIFT) {
			getCharacter().removeCondition(this);
		}
		super.handleEvent(e);
	}
	
	
}