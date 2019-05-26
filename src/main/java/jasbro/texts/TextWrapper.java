package jasbro.texts;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.battle.Monster;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.interfaces.Person;
import jasbro.util.ConfigHandler;

public class TextWrapper {
	private Person object;
	private Boolean personIsPlayer = null;
	
	public TextWrapper(Person person) {
		this.object = person;
	}
	
	public String getGender() {
		return object.getGender().getText();
	}
	
	public String getName() {
		if (checkPersonIsPlayer()) {
			return TextUtil.t("You");
		}
		else {
			return object.getName();
		}
	}
	
	public String getType() {
		if (object instanceof Charakter) {
			Charakter character = (Charakter) object;
			return character.getType().getText();
		}
		else if (object instanceof CustomerGroup) {
			return TextUtil.t("customerGroup");
		}
		else if (object instanceof Monster) {
			return TextUtil.t("monster");
		}
		else {
			return TextUtil.t("customer");
		}
	}
	
	public Person getObject() {
		return object;
	}
	
	public String getHeshe() {
		if (!(object instanceof CustomerGroup)) {
			if (checkPersonIsPlayer()) {
				return TextUtil.t("you");
			}
			else if (object instanceof Monster) {
				return TextUtil.t("it");
			}
			else {
				if (object.getGender() == Gender.MALE) {
					return TextUtil.t("he");
				}
				else {
					return TextUtil.t("she");
				}
			}
		}
		else {
			return TextUtil.t("they");
		}
	}
	
	public String getAss() {
		int random=Util.getInt(1, 6);
		if(random==1){
			return TextUtil.t("backdoor");
		}
		if(random==2){
			return TextUtil.t("arse");
		}
		if(random==3){
			return TextUtil.t("fuckhole");
		}
		if(random==4 && object.getGender() == Gender.MALE){
			return TextUtil.t("boycunt");
		}
		else{
			return TextUtil.t("ass");
		}
	}
	
	public String getPenis() {
		int random=Util.getInt(1, 8);
		if(random==1){
			return TextUtil.t("penis");
		}
		if(random==2){
			return TextUtil.t("cock");
		}
		if(random==3){
			return TextUtil.t("errection");
		}
		if(random==4){
			return TextUtil.t("fleshrod");
		}
		if(random==5){
			return TextUtil.t("dick");
		}
		if(random==6){
			return TextUtil.t("member");
		}
		else{
			return TextUtil.t("meatstick");
		}
	}
	
	public String getPenispussy() {
		
		if (object.getGender() == Gender.MALE) {
			int random=Util.getInt(1, 8);
			if(random==1){
				return TextUtil.t("penis");
			}
			if(random==2){
				return TextUtil.t("cock");
			}
			if(random==3){
				return TextUtil.t("erection");
			}
			if(random==4){
				return TextUtil.t("fleshrod");
			}
			if(random==5){
				return TextUtil.t("dick");
			}
			if(random==6){
				return TextUtil.t("member");
			}
			else{
				return TextUtil.t("meatstick");
			}
		} else {
			int random=Util.getInt(1, 4);
			if(random==1){
				return TextUtil.t("pussy");
			}
			if(random==2){
				return TextUtil.t("vagina");
			}
			if(random==3){
				return TextUtil.t("fuckhole");
			}
			else{
				return TextUtil.t("cunt");
			}
		}
	}
	
	public String getSemen() {
		int random=Util.getInt(1, 8);
		if(random==1){
			return TextUtil.t("cum");
		}
		if(random==2){
			return TextUtil.t("cream");
		}
		if(random==3){
			return TextUtil.t("spunk");
		}
		if(random==4){
			return TextUtil.t("spooge");
		}
		if(random==5){
			return TextUtil.t("juice");
		}
		if(random==6){
			return TextUtil.t("semen");
		}
		else{
			return TextUtil.t("jism");
		}
	}
	
	public String getAsspussy() {
		if (object.getGender() == Gender.MALE) {
			int random=Util.getInt(1, 4);
			if(random==1){
				return TextUtil.t("backdoor");
			}
			if(random==2){
				return TextUtil.t("arse");
			}
			if(random==3){
				return TextUtil.t("fuckhole");
			}
			else{
				return TextUtil.t("ass");
			}
		} else {
			int random=Util.getInt(1, 4);
			if(random==1){
				return TextUtil.t("pussy");
			}
			if(random==2){
				return TextUtil.t("vagina");
			}
			if(random==3){
				return TextUtil.t("fuckhole");
			}
			else{
				return TextUtil.t("cunt");
			}
		}
	}
	
	public String getBoygirl() {
		if (object.getGender() == Gender.MALE) {
			return TextUtil.t("boy");
		}
		else {
			return TextUtil.t("girl");
		}
	}
	
	public String getHeShe() {
		if (!(object instanceof CustomerGroup)) {
			if (checkPersonIsPlayer()) {
				return TextUtil.t("You");
			}
			else if (object instanceof Monster) {
				return TextUtil.t("It");
			}
			else {
				if (object.getGender() == Gender.MALE) {
					return TextUtil.t("He");
				}
				else {
					return TextUtil.t("She");
				}
			}
		}
		else {
			return TextUtil.t("They");
		}
	}
	
	public String getHisher() {
		if (!(object instanceof CustomerGroup)) {
			if (checkPersonIsPlayer()) {
				return TextUtil.t("your");
			}
			else if (object instanceof Monster) {
				return TextUtil.t("its");
			}
			else {
				if (object.getGender() == Gender.MALE) {
					return TextUtil.t("his");
				}
				else {
					return TextUtil.t("her");
				}
			}
		}
		else {
			return TextUtil.t("their");
		}
	}
	
	public String getHisHer() {
		if (!(object instanceof CustomerGroup)) {
			if (checkPersonIsPlayer()) {
				return TextUtil.t("Your");
			}
			else if (object instanceof Monster) {
				return TextUtil.t("Its");
			}
			else {
				if (object.getGender() == Gender.MALE) {
					return TextUtil.t("His");
				}
				else {
					return TextUtil.t("Her");
				}
			}
		}
		else {
			return TextUtil.t("Their");
		}
	}
	
	public String getHimher() {
		if (!(object instanceof CustomerGroup)) {
			if (checkPersonIsPlayer()) {
				return TextUtil.t("you");
			}
			else if (object instanceof Monster) {
				return TextUtil.t("it");
			}
			else {
				if (object.getGender() == Gender.MALE) {
					return TextUtil.t("him");
				}
				else {
					return TextUtil.t("her");
				}
			}
		}
		else {
			return TextUtil.t("them");
		}
	}
	
	public String getHimHer() {
		if (!(object instanceof CustomerGroup)) {
			if (checkPersonIsPlayer()) {
				return TextUtil.t("You");
			}
			else if (object instanceof Monster) {
				return TextUtil.t("It");
			}
			else {
				if (object.getGender() == Gender.MALE) {
					return TextUtil.t("Him");
				}
				else {
					return TextUtil.t("Her");
				}
			}
		}
		else {
			return TextUtil.t("Them");
		}
	}
	
	public String getHimselfherself() {
		if (!(object instanceof CustomerGroup)) {
			if (checkPersonIsPlayer()) {
				return TextUtil.t("yourself");
			}
			else if (object instanceof Monster) {
				return TextUtil.t("itself");
			}
			else {
				if (object.getGender() == Gender.MALE) {
					return TextUtil.t("himself");
				}
				else {
					return TextUtil.t("herself");
				}
			}
		}
		else {
			return TextUtil.t("themselfes");
		}
	}
	
	public String getHimselfHerself() {
		if (!(object instanceof CustomerGroup)) {
			if (checkPersonIsPlayer()) {
				return TextUtil.t("Yourself");
			}
			else if (object instanceof Monster) {
				return TextUtil.t("Itself");
			}
			else {
				if (object.getGender() == Gender.MALE) {
					return TextUtil.t("Himself");
				}
				else {
					return TextUtil.t("Herself");
				}
			}
		}
		else {
			return TextUtil.t("Themselfes");
		}
	}
	
	public String getIsare() {
		if (!(object instanceof CustomerGroup)) {
			if (checkPersonIsPlayer()) {
				return TextUtil.t("are");
			}
			else {
				return TextUtil.t("is");
			}
		}
		else {
			return TextUtil.t("are");
		}
	}
	
	public void setObject(Person object) {
		this.object = object;
	}
	
	private boolean checkPersonIsPlayer() {
		if (personIsPlayer == null) {
			if (!ConfigHandler.isProtagonistPlayer() || object != Jasbro.getInstance().getData().getProtagonist()) {
				personIsPlayer = false;
			}
			else {
				personIsPlayer = true;
			}
		}
		return personIsPlayer;
	}
	
	
	public boolean isMale() {
		return object.getGender() == Gender.MALE;
	}
	
	public boolean isFemale() {
		return object.getGender() != Gender.MALE;
	}
	
	public boolean isFuta() {
		return object.getGender() == Gender.FUTA;
	}
}