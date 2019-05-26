package jasbro.game.world.customContent.npc;

import jasbro.game.character.Gender;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.battle.Attack;

import java.util.ArrayList;
import java.util.List;

import net.java.truevfs.access.TFile;

public class ComplexEnemyTemplate extends ComplexEnemy {
	private transient String id;
	private transient TFile file;
	private String rape;
	private String femSubmit;
	private String femRape;
	private String reverseFemRape;
	private String reverseFemRapeCapture;
	private String maleSubmit;
	private String maleRape;
	private String reverseMaleRape;
	private String reverseMaleRapeCapture;
	private String encounter;
	private String summoning;
	private String capture;
	private boolean customerMonster;
	private List<EnemySpawnData> spawnDataList = new ArrayList<EnemySpawnData>();
	private List<Attack> attackDataList = new ArrayList<Attack>();
	
	
	public ComplexEnemyTemplate() {
	}
	
	public ComplexEnemyTemplate(String id) {
		this.id = id;
		
		//First time init
		setHitpoints(100);
		setGender(Gender.MALE);
		setAttribute(CalculatedAttribute.DAMAGE, 1d);
		setAttribute(CalculatedAttribute.ARMORPERCENT, 5d);
		setAttribute(CalculatedAttribute.DODGE, 5d);
		setAttribute(CalculatedAttribute.CRITCHANCE, 5d);
		setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
	}
	
	/**
	 * Generates a copy for use in combat
	 * @return
	 */
	public ComplexEnemy generateEnemy() {
		ComplexEnemy complexEnemy = new ComplexEnemy();
		
		complexEnemy.setName(getName());
		complexEnemy.setGender(getGender());
		complexEnemy.setDick(getDick());
		complexEnemy.setHitpoints(getHitpoints());
		complexEnemy.setImages(getImages());
		complexEnemy.setDescription(getDescription());
		complexEnemy.setCharacterBaseId(getCharacterBaseId());
		complexEnemy.setFemaleRape(getFemaleRape());
		complexEnemy.setMaleRape(getMaleRape());
		complexEnemy.setFemaleSubmit(getFemaleSubmit());
		complexEnemy.setMaleSubmit(getMaleSubmit());
		complexEnemy.setReverseFemaleRape(getReverseFemaleRape());
		complexEnemy.setReverseMaleRape(getReverseMaleRape());
		
		for (CalculatedAttribute attribute : CalculatedAttribute.values()) {
			double value = getAttribute(attribute);
			if (value != 0) {
				complexEnemy.setAttribute(attribute, value);
			}
		}		
		return complexEnemy;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TFile getFile() {
		return file;
	}
	public void setFile(TFile file) {
		this.file = file;
	}
	public String getTextRape() {
		return this.rape;
	}
	public void setTextRape(String text) {
		rape = text;
	}
	public String getFemaleSubmit() {
		return this.femSubmit;
	}
	public void setFemaleSubmit(String text) {
		femSubmit = text;
	}
	public String getFemaleRape() {
		return this.femRape;
	}
	public void setFemaleRape(String text) {
		femRape = text;
	}
	public String getReverseFemaleRape() {
		return this.reverseFemRape;
	}
	public void setReverseFemaleRape(String text) {
		reverseFemRape = text;
	}
	public String getReverseFemaleRapeCapture() {
		return this.reverseFemRapeCapture;
	}
	public void setReverseFemaleRapeCapture(String text) {
		reverseFemRapeCapture = text;
	}
	public String getMaleSubmit() {
		return this.maleSubmit;
	}
	public void setMaleSubmit(String text) {
		maleSubmit = text;
	}
	public String getMaleRape() {
		return this.maleRape;
	}
	public void setMaleRape(String text) {
		maleRape = text;
	}
	public String getReverseMaleRape() {
		return this.reverseMaleRape;
	}
	public void setReverseMaleRape(String text) {
		reverseMaleRape = text;
	}	
	public String getReverseMaleRapeCapture() {
		return this.reverseMaleRapeCapture;
	}
	public void setReverseMaleRapeCapture(String text) {
		reverseMaleRapeCapture = text;
	}
	public String getTextCapture() {
		return this.capture;
	}
	public void setTextCapture(String text) {
		capture = text;
	}	
	public boolean setCustomerMonster(boolean customerMonster) {
		this.customerMonster = customerMonster;
		return customerMonster;
	}	
	public boolean isCustomerMonster() {
		return customerMonster;
	}
	
	@Override
	public String toString() {
		return id;
	}
	
	public List<EnemySpawnData> getSpawnDataList() {
		return spawnDataList;
	}
	
	public void setSpawnDataList(List<EnemySpawnData> spawnDataList) {
		this.spawnDataList = spawnDataList;
	}	
	public List<Attack> getAttackDataList() {
		return attackDataList;
	}
	
	public void setAttackDataList(List<Attack> attackDataList) {
		this.attackDataList = attackDataList;
	}

	public String getTextEncounter() {
		return encounter;
	}

	public void setTextEncounter(String encounter) {
		this.encounter = encounter;
	}
	
	public String getTextSummoning() {
		return summoning;
	}

	public void setTextSummoning(String summoning) {
		this.summoning = summoning;
	}
	
	
	
}