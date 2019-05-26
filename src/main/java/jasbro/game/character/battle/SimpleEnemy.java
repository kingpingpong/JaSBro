package jasbro.game.character.battle;

import jasbro.game.character.Gender;
import jasbro.game.character.attributes.CalculatedAttribute;

public class SimpleEnemy extends Enemy {
	private Gender gender;
	private MonsterDickType dick;
	private DamageType damageType;
	private String name;
	private String femRape;
	private String femSubmit;
	private String reverseFemRape;
	private String maleRape;
	private String maleSubmit;
	private String reverseMaleRape;
	
	public SimpleEnemy() {
	}
	
	public SimpleEnemy(String name, 
			Gender gender,
			int hitpoints,
			double damage,
			double armorPercent,
			double dodge,
			double hit,
			double critChance,
			double critAmount,
			double blockChance,
			double blockAmount,
			double speed) {
		this.name = name;
		this.gender = gender;
		setHitpoints(hitpoints);
		setAttribute(CalculatedAttribute.DAMAGE, damage);
		setAttribute(CalculatedAttribute.BLOCKCHANCE, blockChance);
		setAttribute(CalculatedAttribute.BLOCKAMOUNT, blockAmount);
		setAttribute(CalculatedAttribute.ARMORPERCENT, armorPercent);
		setAttribute(CalculatedAttribute.CRITCHANCE, critChance);
		setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, critAmount);
		setAttribute(CalculatedAttribute.DODGE, dodge);
		setAttribute(CalculatedAttribute.HIT, hit);
		setAttribute(CalculatedAttribute.SPEED, speed);
	}
	
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public MonsterDickType getDick() {
		return dick;
	}
	public void setDick(MonsterDickType dick) {
		this.dick = dick;
	}
	public DamageType getElement() {
		return damageType;
	}
	public void setElement(DamageType damageType) {
		this.damageType = damageType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFemaleSubmit() {
		return femSubmit;
	}
	public void setFemaleSubmit(String femSubmit) {
		this.femSubmit = femSubmit;
	}
	public String getFemaleRape() {
		return femRape;
	}
	public void setFemaleRape(String femRape) {
		this.femRape = femRape;
	}
	public String getReverseFemaleRape() {
		return femRape;
	}
	public void setReverseFemaleRape(String reverseFemRape) {
		this.reverseFemRape = reverseFemRape;
	}
	public String getMaleSubmit() {
		return maleSubmit;
	}
	public void setMaleSubmit(String maleSubmit) {
		this.maleSubmit = maleSubmit;
	}
	public String getMaleRape() {
		return maleRape;
	}
	public void setMaleRape(String maleRape) {
		this.maleRape = maleRape;
	}
	public String getReverseMaleRape() {
		return reverseMaleRape;
	}
	public void setReverseMaleRape(String reverseMaleRape) {
		this.reverseMaleRape = reverseMaleRape;
	}
	@Override
	public void initCombat() {
	}
	
	
}