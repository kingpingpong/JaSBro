package jasbro.game.character.battle;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.world.Time;

import java.util.EnumMap;
import java.util.Map;


public class Attack {
	private Map<DamageType, Float> damageMap = new EnumMap<>(DamageType.class);
	private boolean dodgeable = true;
	private boolean blockable = true;
	private boolean canCrit = true;
	private boolean isCrit = false;
	private int hit;
	private int critChance;
	private int critBonus;
	private int selectionModifier = 10;
	private boolean abort = false;

	private String attackMessageKey = "fight.combatText";
	private String hitMessageKey = "fight.combatText.hit";

	public Attack() {
	}

	public Attack(DamageType damageType, Float damage, boolean dodgeable, boolean blockable, int hit, int critChance, int critBonus) {
		super();
		this.damageMap.put(damageType, damage);
		this.dodgeable = dodgeable;
		this.blockable = blockable;
		this.hit = hit;
		this.critChance = critChance;
		this.critBonus = critBonus;
	}

	public Attack(Unit unit) {
		this.damageMap.put(DamageType.REGULAR, unit.getDamage());
		this.hit = unit.getHit();
		this.critChance = unit.getCritChance();
		this.critBonus = unit.getCritDamageBonus();
	}

	public void addDamageModifier(DamageType damageType, Float damage) {
		if (damageMap.containsKey(damageType)) {
			damageMap.put(damageType, damageMap.get(damageType) + damage);
		}
		else {
			damageMap.put(damageType, damage);
		}
	}

	public String getText() {
		return null; //TODO
	}

	public void attackHits(Defender target, Battle battle) {
	}

	public boolean isDodgeable() {
		return dodgeable;
	}

	public void setDodgeable(boolean dodgeable) {
		this.dodgeable = dodgeable;
	}

	public boolean isBlockable() {
		return blockable;
	}

	public void setBlockable(boolean blockable) {
		this.blockable = blockable;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getCritChance() {
		return critChance;
	}

	public void setCritChance(int critChance) {
		this.critChance = critChance;
	}

	public int getCritBonus() {
		return critBonus;
	}

	public void setCritBonus(int critBonus) {
		this.critBonus = critBonus;
	}

	public int getSelectionModifier() {
		return selectionModifier;
	}

	public void setSelectionModifier(int selectionModifier) {
		this.selectionModifier = selectionModifier;
	}

	public boolean isCanCrit() {
		return canCrit;
	}

	public void setCanCrit(boolean canCrit) {
		this.canCrit = canCrit;
	}

	public boolean isCrit() {
		return isCrit;
	}

	public void setCrit(boolean isCrit) {
		this.isCrit = isCrit;
	}

	public boolean isAbort() {
		return abort;
	}

	public void setAbort(boolean abort) {
		this.abort = abort;
	}

	public Map<DamageType, Float> getDamageMap() {
		return damageMap;
	}

	public String getAttackMessageKey() {
		return attackMessageKey;
	}

	public void setAttackMessageKey(String attackMessageKey) {
		this.attackMessageKey = attackMessageKey;
	}

	public String getHitMessageKey() {
		return hitMessageKey;
	}

	public void setHitMessageKey(String hitMessageKey) {
		this.hitMessageKey = hitMessageKey;
	}


	public static class StandardAttack extends Attack {

		public StandardAttack() {
		}

		public StandardAttack(DamageType damageType, Float damage, boolean dodgeable, boolean blockable, int hit, int critChance,
				int critBonus) {
			super(damageType, damage, dodgeable, blockable, hit, critChance, critBonus);
		}

		public StandardAttack(Unit unit) {
			super(unit);
		}
	}

	public static class MightyStrike extends Attack {

		public MightyStrike(Unit unit) {

			getDamageMap().put(DamageType.REGULAR, unit.getDamage() * 1.5f);
			setAttackMessageKey("STRONGSTRIKE.attack");
		}
	}
	public static class Uppercut extends Attack {

		public Uppercut(Unit unit) {

			getDamageMap().put(DamageType.REGULAR, unit.getDamage() * 2.5f);
			setAttackMessageKey("STRONGSTRIKE.attack");
		}
	}
	public static class CleanHit extends Attack {

		public CleanHit(Unit unit) {

			getDamageMap().put(DamageType.REGULAR, unit.getDamage() * 1.5f);
			setCritChance(unit.getCritChance()+50);
			setCritBonus(200);
			setAttackMessageKey("PRECISESTRIKE.attack");
		}
	}
	public static class LowKick extends Attack {

		public LowKick(Unit unit) {

			getDamageMap().put(DamageType.REGULAR, unit.getDamage() * 1.5f);
			setBlockable(false);
			setAttackMessageKey("SWIFTSTRIKE.attack");
		}
	}
	public static class Firebolt extends Attack {

		public Firebolt(Unit unit) {

			getDamageMap().put(DamageType.FIRE, unit.getDamage() * 2.5f);
			setHit(unit.getHit() + 10);
			setCanCrit(false);
			setAttackMessageKey("FIREWAVE.attack");
		}
	}
	public static class Thunderbolt extends Attack {

		public Thunderbolt(Unit unit) {
			getDamageMap().put(DamageType.LIGHTNING, unit.getDamage() * 2.7f);
			setHit(250);
			setCanCrit(false);
			setBlockable(false);
			setAttackMessageKey("ROLLINGTHUNDER.attack");
		}
	}
	public static class FlameBreath extends Attack {
		public FlameBreath(Charakter character) {
			float damage = 5f + character.getStrength() / 15f + character.getStamina() / 15f;
			getDamageMap().put(DamageType.FIRE, damage);
			setHit(character.getHit() + 10);
			setCrit(false);
			setSelectionModifier(2);
			setAttackMessageKey("FLAMEBREATH.attack");
		}
	}
	public static class Heal extends Attack {
		public Heal(Charakter character) {
			getDamageMap().put(DamageType.HOLY, 0f);
			setHit(-1000);
			setSelectionModifier(2);
			character.modifyHitpoints(5f+(character.getFinalValue(SpecializationAttribute.MAGIC))/10);
			setAttackMessageKey("HEAL.attack");
		}
	}

	public static class ClawAttack extends Attack {

		public ClawAttack(Unit unit) {
			super(unit);
			getDamageMap().put(DamageType.REGULAR, getDamageMap().get(DamageType.REGULAR) * 1.5f);
			setAttackMessageKey("EXTRACTABLECLAWS.attack");
		}
	}
	public static class Heal2 extends Attack {

		public Heal2(Unit unit) {
			super(unit);
			getDamageMap().put(DamageType.HOLY, 0f);
			setHit(-1000);
			setSelectionModifier(2);
			unit.modifyHitpoints(10);
			setAttackMessageKey("HEAL.attack");
		}
	}

	public static class StrongStrike extends Attack {

		public StrongStrike(Charakter character) {

			getDamageMap().put(DamageType.REGULAR, character.getDamage() * 1.5f);
			setAttackMessageKey("STRONGSTRIKE.attack");
		}
	}

	public static class SwiftStrike extends Attack {

		public SwiftStrike(Charakter character) {

			getDamageMap().put(DamageType.REGULAR, (float) 2+character.getFinalValue(SpecializationAttribute.AGILITY)/8);
			setHit(60+character.getFinalValue(SpecializationAttribute.AGILITY)/2);
			setBlockable(false);
			setAttackMessageKey("SWIFTSTRIKE.attack");
		}
	}

	public static class PreciseStrike extends Attack {

		public PreciseStrike(Charakter character) {
			getDamageMap().put(DamageType.REGULAR, character.getDamage() * 0.7f);
			setCritChance(character.getCritChance()+50);
			setCritBonus(200);
			setAttackMessageKey("PRECISESTRIKE.attack");
		}
	}

	public static class ButtSmash extends Attack {

		public ButtSmash(Charakter character) {
			getDamageMap().put(DamageType.REGULAR, (float) ((character.getFinalValue(SpecializationAttribute.SEDUCTION)+character.getFinalValue(SpecializationAttribute.SEDUCTION))/10));
			setCanCrit(false);
			setBlockable(false);
			setAttackMessageKey("BUTTSMASH.attack");
		}
	}

	public static class EtherStrike extends Attack {

		public EtherStrike(Charakter character) {
			getDamageMap().put(DamageType.MAGIC, character.getFinalValue(SpecializationAttribute.MAGIC)/10f);
			setAttackMessageKey("ETHERSTRIKE.attack");
		}
	}

	public static class FireWave extends Attack {

		public FireWave(Charakter character) {
			getDamageMap().put(DamageType.FIRE, 5+character.getFinalValue(SpecializationAttribute.MAGIC)/10f);
			setAttackMessageKey("FIREWAVE.attack");
			setHit(character.getHit() + 10);
			setCanCrit(false);
		}
	}

	public static class IceStingers extends Attack {

		public IceStingers(Charakter character) {
			getDamageMap().put(DamageType.WATER, 7+character.getFinalValue(SpecializationAttribute.MAGIC)/10f);
			setCritChance(character.getCritChance()+20);
			setCritBonus(300);
			setAttackMessageKey("ICESTINGERS.attack");
		}
	}

	public static class RollingThunder extends Attack {

		public RollingThunder(Charakter character) {
			getDamageMap().put(DamageType.LIGHTNING, 6+character.getFinalValue(SpecializationAttribute.MAGIC)/7f);
			setHit(250);
			setCanCrit(false);
			setBlockable(false);			
			setAttackMessageKey("ROLLINGTHUNDER.attack");

		}
	}

	public static class StarlightBreaker extends Attack {

		public StarlightBreaker(Charakter character) {
			int bonus=0;
			if(Jasbro.getInstance().getData().getTime()!=Time.NIGHT)
				bonus=12;
			getDamageMap().put(DamageType.HOLY, bonus+character.getFinalValue(SpecializationAttribute.MAGIC)/6f);
			setAttackMessageKey("STARLIGHTBREAKER.attack");
		}
	}
	
	public static class Midnight extends Attack {

		public Midnight(Charakter character) {
			int bonus=0;
			if(Jasbro.getInstance().getData().getTime()==Time.NIGHT)
				bonus=13;
			getDamageMap().put(DamageType.DARKNESS, bonus+character.getFinalValue(SpecializationAttribute.MAGIC)/6f);
			setDodgeable(false);
			setAttackMessageKey("MIDNIGHT.attack");
		}
	}

	public static class GracefulKick extends Attack {

		public GracefulKick(Charakter character) {
			getDamageMap().put(DamageType.REGULAR, 1+character.getFinalValue(SpecializationAttribute.STRIP)/8f);
			setHit(character.getFinalValue(SpecializationAttribute.STRIP));
			setCritBonus(character.getFinalValue(SpecializationAttribute.STRIP)/10);
			setAttackMessageKey("GRACEFULKICKS.attack");
			setDodgeable(false);
		}
	}

	public static class ShadowSlicer extends Attack {

		public ShadowSlicer(Charakter character) {
			getDamageMap().put(DamageType.REGULAR, 2+character.getDamage() * (100f+character.getFinalValue(SpecializationAttribute.AGILITY))/100f);
			setAttackMessageKey("SHADOWSLICER.attack");
			setCritChance(100);
		}
	}


}