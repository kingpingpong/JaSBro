package jasbro.game.character;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AgeProgressionData {
	private String infantBase;
	private String childBase;
	private String teenagerBase;
	private String sonDaughterBase;
	
	private List<String> adultBases = new ArrayList<String>();
	
	private String nameMother;
	private String nameFather;
	private WeakReference<Charakter> mother;
	private WeakReference<Charakter> father;
	
	public AgeProgressionData() {
	}
	
	public AgeProgressionData(AgeProgressionData ageProgressionData) {
		this.infantBase = ageProgressionData.infantBase;
		this.childBase = ageProgressionData.childBase;
		this.teenagerBase = ageProgressionData.teenagerBase;
		this.sonDaughterBase = ageProgressionData.sonDaughterBase;
		adultBases.addAll(ageProgressionData.adultBases);
	}
	
	public String getInfantBase() {
		return infantBase;
	}
	
	public void setInfantBase(String infantBase) {
		this.infantBase = infantBase;
	}
	
	public String getChildBase() {
		return childBase;
	}
	
	public void setChildBase(String childBase) {
		this.childBase = childBase;
	}
	
	public String getTeenagerBase() {
		return teenagerBase;
	}
	
	public void setTeenagerBase(String teenBase) {
		this.teenagerBase = teenBase;
	}
	
	public List<String> getAdultBases() {
		return adultBases;
	}
	
	public void setAdultBases(List<String> adultBases) {
		this.adultBases = adultBases;
	}
	
	public String getSonDaughterBase() {
		return sonDaughterBase;
	}
	
	public void setSonDaughterBase(String sunDaughterBase) {
		this.sonDaughterBase = sunDaughterBase;
	}
	
	public String getNameMother() {
		return nameMother;
	}
	
	public void setNameMother(String nameMother) {
		this.nameMother = nameMother;
	}
	
	public String getNameFather() {
		return nameFather;
	}
	
	public void setNameFather(String nameFather) {
		this.nameFather = nameFather;
	}
	
	public WeakReference<Charakter> getMother() {
		return mother;
	}
	
	public void setMother(WeakReference<Charakter> mother) {
		this.mother = mother;
	}
	
	public WeakReference<Charakter> getFather() {
		return father;
	}
	
	public void setFather(WeakReference<Charakter> father) {
		this.father = father;
	}
}