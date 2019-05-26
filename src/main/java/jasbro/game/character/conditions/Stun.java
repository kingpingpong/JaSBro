package jasbro.game.character.conditions;

import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.Unit;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

public class Stun extends BattleCondition {
	private int duration;
	
	public Stun(Unit unit, int duration) {
		super(unit);
		this.duration = duration;
	}
	
	@Override
	public void handleEvent(MyEvent e) {
		if (e.getType() == EventType.NEXTSHIFT) {
			getUnit().removeCondition(this);
		}
		if (e.getType() == EventType.ATTACK) {
			Battle battle = (Battle) e.getSource();
			if (battle.isAttacker(getUnit())) {
				battle.getAttack().setAbort(true);
				battle.addToCombatText(getUnit().getName() + " is stunned.");
				duration--;
				if (duration == 0) {
					getUnit().removeCondition(this);
				}
			}
		}
	}
}