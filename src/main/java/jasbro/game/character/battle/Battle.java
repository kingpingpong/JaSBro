package jasbro.game.character.battle;

import jasbro.Util;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Battle {
	private List<Unit> sideA = new ArrayList<Unit>();
	private List<Unit> sideB = new ArrayList<Unit>();
	private String combatText = "";
	private int round = 1;
	
	private List<Unit> order = new ArrayList<Unit>();
	private Attack attack;
	private List<Unit> attackers = new ArrayList<Unit>();
	private List<Unit> targets = new ArrayList<Unit>();
	private List<Defender> targetData = new ArrayList<Defender>();
	
	public Battle() {
	}
	
	public Battle(Unit combatant1, Unit combatant2) {
		sideA.add(combatant1);
		sideB.add(combatant2);
	}
	
	public void doRound() {
		List<Unit> allCombatantsUnordered = new ArrayList<Unit>();
		allCombatantsUnordered.addAll(sideA);
		allCombatantsUnordered.addAll(sideB);
		order = new ArrayList<Unit>();
		
		int i = 0;
		do {
			if (allCombatantsUnordered.size() == 1) {
				order.add(allCombatantsUnordered.get(0));
				allCombatantsUnordered.clear();
			}
			else {
				int sum = 0;
				for (Unit unit : allCombatantsUnordered) {
					sum += unit.getSpeed();
				}
				int rndInt = Util.getRnd().nextInt(sum);
				sum = 0;
				for (Unit unit : allCombatantsUnordered) {
					if (unit.getSpeed() > rndInt) {
						order.add(unit);
						allCombatantsUnordered.remove(unit);
						break;
					}
					else {
						sum += unit.getSpeed();
					}
				}
			}
			i++;
		} while (allCombatantsUnordered.size() > 0 && i < 20);
		order.addAll(allCombatantsUnordered);
		
		
		for (int j = 0; j < order.size(); j++) {
			Unit unit = order.get(j);
			if (unit.getHitpoints() > 0 && !isOver()) {
				attackers.add(unit);
				if (sideA.contains(unit)) {
					Unit target;
					do {
						target = sideB.get(Util.getInt(0, sideB.size()));
					}
					while(target.getHitpoints() < 1);
					targets.add(target);
					targetData.add(new Defender(target));
				}
				else {
					Unit target;
					do {
						target = sideA.get(Util.getInt(0, sideA.size()));
					}
					while(target.getHitpoints() < 1);
					targets.add(target);
					targetData.add(new Defender(target));
				}
				
				attack = unit.getAttack(this);
				MyEvent event = new MyEvent(EventType.ATTACK, this);
				for (Unit curUnit : attackers) {
					curUnit.handleEvent(event);
				}
				for (Unit curUnit : targets) {
					curUnit.handleEvent(event);
				}
				
				if (!attack.isAbort()) {
					for (Defender target : targetData) {
						Object arguments[] = {unit.getHitpoints()};
						addToCombatText(TextUtil.firstCharUpper(
								TextUtil.t(attack.getAttackMessageKey(), unit, target.getUnit(), arguments) + " "));
						if (Util.getInt(0, 100) < 100 + attack.getHit() - target.getDodge()) { //Attack hits
							boolean crit = false;
							boolean block = false;
							if (attack.isCanCrit() && Util.getInt(0, 100) < attack.getCritChance()) { // attack crits
								crit = true;
							}
							if (attack.isBlockable() && Util.getInt(0, 100) < target.getBlockChance()) { // attack blocked
								block = true;
							}
							
							
								
							if (crit == block) {
								crit = false;
								block = false;
								attack.setCrit(false);
								event = new MyEvent(EventType.ATTACKHIT, this);
							}
							else if (!crit && block) {
								attack.setCrit(false);
								target.setBlockSuccessful(true);
								event = new MyEvent(EventType.ATTACKBLOCK, this);
							}
							else {
								attack.setCrit(true);
								event = new MyEvent(EventType.ATTACKCRIT, this);
							}
							for (Unit curUnit : attackers) {
								curUnit.handleEvent(event);
							}
							target.getUnit().handleEvent(event);
							
							float damage = target.takeAttack(attack);
							attack.attackHits(target, this);
							Object arguments2[] = {Math.round(Math.abs(damage) * 100) / 100f, target.getHitpoints()};
							if (crit == block) {
								addToCombatText(TextUtil.firstCharUpper(
										TextUtil.t(attack.getHitMessageKey(), unit, target.getUnit(), arguments2)));
							}
							else if (!crit && block) {
								addToCombatText(TextUtil.t("fight.combatText.block", unit, target.getUnit(), arguments2));
							}
							else {
								addToCombatText(TextUtil.t("fight.combatText.crit", unit, target.getUnit(), arguments2));
							}
						}
						else { //Attack misses
							if(attack.getHit()>-100){
							event = new MyEvent(EventType.ATTACKMISS, this);
							for (Unit curUnit : attackers) {
								curUnit.handleEvent(event);
							}
							target.getUnit().handleEvent(event);
							addToCombatText(TextUtil.t("fight.combatText.miss", unit, target.getUnit(), arguments));
							}
						}
					}
				}
				
				addToCombatText("\n");
				targets.clear();
				targetData.clear();
				attackers.clear();
				attack = null;
			}
		}		
		round++;
	}
	
	public String getCombatText() {
		return combatText;
	}
	
	public int getRound() {
		return round;
	}
	
	public List<Unit> getSideA() {
		return sideA;
	}
	
	public List<Unit> getSideB() {
		return sideB;
	}
	
	public List<Unit> getEnemies(Unit unit) {
		if (sideA.contains(unit)) {
			return sideB;
		}
		else if (sideB.contains(unit)) {
			return sideA;
		}
		else {
			return null;
		}
	}
	
	public boolean isTarget(Unit unit) {
		if (targets.contains(unit)) {
			return true;
		}
		return false;
	}
	
	public boolean isAttacker(Unit unit) {
		if (attackers.contains(unit)) {
			return true;
		}
		return false;
	}
	
	public Attack getAttack() {
		return attack;
	}
	
	public void setAttack(Attack attack) {
		this.attack = attack;
	}
	
	public List<Unit> getAttackers() {
		return attackers;
	}
	
	public List<Defender> getTargetData() {
		return targetData;
	}
	
	public void addToCombatText(String message) {
		this.combatText += message;
		if (this.combatText.length() > 0) {
			if (this.combatText.charAt(this.combatText.length()-1) != '\n' && this.combatText.charAt(this.combatText.length()-1) != ' ') {
				this.combatText += " ";
			}
		}
	}
	
	public List<Unit> getOrder() {
		return order;
	}
	
	public boolean isOver() {
		boolean alive = false;
		for (Unit unit : sideA) {
			if (unit.getHitpoints() > 0) {
				alive = true;
				break;
			}
		}
		if (!alive) {
			return true;
		}
		for (Unit unit : sideB) {
			if (unit.getHitpoints() > 0) {
				return false;
			}
		}
		return true;
	}
}