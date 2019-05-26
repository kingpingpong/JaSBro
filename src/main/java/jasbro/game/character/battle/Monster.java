package jasbro.game.character.battle;

import jasbro.game.character.Gender;
import jasbro.game.character.attributes.CalculatedAttribute;

public class Monster extends Enemy {
	private MonsterType monsterType;
	
	public Monster(MonsterType monsterType) {
		this.monsterType = monsterType;
	}
	
	@Override
	public Gender getGender() {
		return Gender.MALE;
	}
	
	@Override
	public String getName() {
		return monsterType.toString();
	}
	
	@Override
	public void initCombat() {
		if (monsterType == MonsterType.DEMONFOX) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 30d);
			setAttribute(CalculatedAttribute.HIT, 5d);
		} 
		if (monsterType == MonsterType.WEREWOLF) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 10d);
			setAttribute(CalculatedAttribute.HIT, 5d);
		}
		if (monsterType == MonsterType.MUDWALKER) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 5d);
			setAttribute(CalculatedAttribute.HIT, 5d);
		}
		if (monsterType == MonsterType.DRAGON) {
			setHitpoints(300);
			setAttribute(CalculatedAttribute.DAMAGE, 3d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 35d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 30d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 50d);
			setAttribute(CalculatedAttribute.DODGE, 0d);
			setAttribute(CalculatedAttribute.HIT, 0d);
		}
		if (monsterType == MonsterType.CENTAUR) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 3d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 10d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 5d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 10d);
			setAttribute(CalculatedAttribute.HIT, 2d);
		}
		if (monsterType == MonsterType.NAGA) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 0d);
			setAttribute(CalculatedAttribute.HIT, 0d);
		}
		if (monsterType == MonsterType.SPRIGGAN) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 0d);
			setAttribute(CalculatedAttribute.HIT, 0d);
		}
		if (monsterType == MonsterType.GARGOYLE) {
			setHitpoints(400);
			setAttribute(CalculatedAttribute.DAMAGE, 5d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 35d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 5d);
			setAttribute(CalculatedAttribute.HIT, 5d);
		}
		if (monsterType == MonsterType.NINETAILS) {
			setHitpoints(300);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 15d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 20d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 40d);
			setAttribute(CalculatedAttribute.DODGE, 45d);
			setAttribute(CalculatedAttribute.HIT, 0d);
		}
		if (monsterType == MonsterType.ALPHAWEREWOLF) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 0d);
			setAttribute(CalculatedAttribute.HIT, 0d);
		}
		if (monsterType == MonsterType.SWAMPWALKER) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 0d);
			setAttribute(CalculatedAttribute.HIT, 0d);
		}
		if (monsterType == MonsterType.ELDERDRAGON) {
			setHitpoints(500);
			setAttribute(CalculatedAttribute.DAMAGE, 6d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 40d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 30d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 50d);
			setAttribute(CalculatedAttribute.DODGE, 5d);
			setAttribute(CalculatedAttribute.HIT, 5d);
		}
		if (monsterType == MonsterType.SAGITARIUS) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 0d);
			setAttribute(CalculatedAttribute.HIT, 0d);
		}
		if (monsterType == MonsterType.NAGAELITE) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 0d);
			setAttribute(CalculatedAttribute.HIT, 0d);
		}
		if (monsterType == MonsterType.SPRIGGANQUEEN) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 0d);
			setAttribute(CalculatedAttribute.HIT, 0d);
		}
		if (monsterType == MonsterType.GRANITGARGOYLE) {
			setHitpoints(150);
			setAttribute(CalculatedAttribute.DAMAGE, 4d);
			setAttribute(CalculatedAttribute.BLOCKCHANCE, 5d);
			setAttribute(CalculatedAttribute.BLOCKAMOUNT, 50d);
			setAttribute(CalculatedAttribute.ARMORPERCENT, 10d);
			setAttribute(CalculatedAttribute.CRITCHANCE, 10d);
			setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
			setAttribute(CalculatedAttribute.DODGE, 0d);
			setAttribute(CalculatedAttribute.HIT, 0d);
		}
		setMaxHitpoints(getHitpoints());
	}
	
	public static enum MonsterType {
		DEMONFOX,
		WEREWOLF,
		MUDWALKER,
		DRAGON,
		CENTAUR,
		NAGA,
		SPRIGGAN,
		GARGOYLE,
		NINETAILS,
		ALPHAWEREWOLF,
		SWAMPWALKER,
		ELDERDRAGON,
		SAGITARIUS,
		NAGAELITE,
		SPRIGGANQUEEN,
		GRANITGARGOYLE,
	}
}