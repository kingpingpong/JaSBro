package jasbro.game.character.activities;

import jasbro.Util;
import jasbro.game.character.activities.sub.Advertise;
import jasbro.game.character.activities.sub.BodyWrap;
import jasbro.game.character.activities.sub.Break;
import jasbro.game.character.activities.sub.Camp;
import jasbro.game.character.activities.sub.Clean;
import jasbro.game.character.activities.sub.Cook;
import jasbro.game.character.activities.sub.Eat;
import jasbro.game.character.activities.sub.Explore;
import jasbro.game.character.activities.sub.Fish;
import jasbro.game.character.activities.sub.Gardening;
import jasbro.game.character.activities.sub.Govern;
import jasbro.game.character.activities.sub.Idle;
import jasbro.game.character.activities.sub.Nurse;
import jasbro.game.character.activities.sub.Orgy;
import jasbro.game.character.activities.sub.Pamper;
import jasbro.game.character.activities.sub.Practice;
import jasbro.game.character.activities.sub.Pray;
import jasbro.game.character.activities.sub.Publicize;
import jasbro.game.character.activities.sub.Read;
import jasbro.game.character.activities.sub.RefusedToWork;
import jasbro.game.character.activities.sub.Relax;
import jasbro.game.character.activities.sub.Sex;
import jasbro.game.character.activities.sub.Sleep;
import jasbro.game.character.activities.sub.Soak;
import jasbro.game.character.activities.sub.Study;
import jasbro.game.character.activities.sub.Sunbathe;
import jasbro.game.character.activities.sub.Swim;
import jasbro.game.character.activities.sub.Talk;
import jasbro.game.character.activities.sub.Train;
import jasbro.game.character.activities.sub.TrainToFight;
import jasbro.game.character.activities.sub.business.Attend;
import jasbro.game.character.activities.sub.business.Bartend;
import jasbro.game.character.activities.sub.business.BathAttendant;
import jasbro.game.character.activities.sub.business.Catshow;
import jasbro.game.character.activities.sub.business.Fight;
import jasbro.game.character.activities.sub.business.Massage;
import jasbro.game.character.activities.sub.business.MonsterFight;
import jasbro.game.character.activities.sub.business.Offerings;
import jasbro.game.character.activities.sub.business.PublicUse;
import jasbro.game.character.activities.sub.business.SellFood;
import jasbro.game.character.activities.sub.business.Strip;
import jasbro.game.character.activities.sub.business.Submit;
import jasbro.game.character.activities.sub.business.SubmitToMonster;
import jasbro.game.character.activities.sub.childcare.Play;
import jasbro.game.character.activities.sub.whore.Dominate;
import jasbro.game.character.activities.sub.whore.Suck;
import jasbro.game.character.activities.sub.whore.Tease;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.activities.sub.whore.WhoreStreets;
import jasbro.game.events.business.Customer;
import jasbro.gui.pages.SelectionData;
import jasbro.texts.TextUtil;

import java.util.List;

import org.apache.log4j.Logger;

public enum ActivityType {
    REFUSEDTOWORK(RefusedToWork.class, RefusedToWorkExecutor.class, true),
    SLEEP(Sleep.class, false), IDLE(Idle.class, false), CAMP(Camp.class, true), 
    SEX(Sex.class, true, false, 3, true), THREESOME(Orgy.class, true, 5), ORGY(Orgy.class, true, 8), 
    COOK(Cook.class, true, 1), CLEAN(Clean.class, false, 1), GOVERN(Govern.class, true),
    SUNBATHE(Sunbathe.class, false), EAT(Eat.class, false),
    TRAINTOFIGHT(TrainToFight.class, false), BATHE(Sleep.class, false), READ(Read.class, false),
    TRAIN(Train.class, BusinessActivityExecutor.class, true, false, 0, true), TEACH(Train.class, true, false, 0, true), 
    TALK(Talk.class, true, false, 0, true),   RELAX(Relax.class, false), FISH(Fish.class, false), GARDENING(Gardening.class, false), PRACTICE(Practice.class, false),
    WHORE(Whore.class, WhoreActivityExecutor.class, false, true, 4), WHORESTREETS(WhoreStreets.class, false, true, 4), PRAY(Pray.class, false),   OFFERINGS(Offerings.class, BusinessActivityExecutor.class, false, true, 1),
    BARTEND(Bartend.class, BusinessActivityExecutor.class, true, true, 1), SOAK(Soak.class, false),
    SUBMITTOMONSTER(SubmitToMonster.class, BusinessActivityExecutor.class, false, true, 10), STUDY(Study.class, false),
    SWIM(Swim.class, false), NURSE(Nurse.class, true), SELLFOOD(SellFood.class, BusinessActivityExecutor.class, false, true, 1),
    STRIP(Strip.class, BusinessActivityExecutor.class, false, true, 8), CATSHOW(Catshow.class, BusinessActivityExecutor.class, false, true, 8),
    BREAK(Break.class, true),
    DOMINATE(Dominate.class, BusinessActivityExecutor.class, false, true), PUBLICUSE(PublicUse.class, BusinessActivityExecutor.class, true, true, 8),
    SUBMIT(Submit.class, BusinessActivityExecutor.class, false, true, 9),SUCK(Suck.class, BusinessActivityExecutor.class, false, true, 4),
    TEASE(Tease.class, BusinessActivityExecutor.class, false, true, 4), PAMPER(Pamper.class,true), BODYWRAP(BodyWrap.class,true),
    FIGHT(Fight.class, BusinessActivityExecutor.class, true, true, 2), ATTEND(Attend.class, BusinessActivityExecutor.class, true, true, 5),
    ADVERTISE(Advertise.class, false, 1),
    BATHATTENDANT(BathAttendant.class, BusinessActivityExecutor.class, true, true, 4),
    MASSAGE(Massage.class, BusinessActivityExecutor.class, false, true, 3),
    EVENT(null, false),
    CUSTOMEVENT(null, false),
    MONSTERFIGHT(MonsterFight.class, BusinessActivityExecutor.class, true, true, 4), PUBLICIZE(Publicize.class, false, 1),
    PLAY(Play.class, true),
    EXPLORE(Explore.class, false);
    
    private final static Logger log = Logger.getLogger(ActivityType.class);
    private Class<? extends RunningActivity> activityClass;
    private Class<? extends ActivityExecutor> executorClass;
    private boolean groupActivity;
    private boolean customerDependent;
    private int minimumObedience = 0;
    private boolean hasSelectionOptions = false;

	private ActivityType(Class<? extends RunningActivity> activityClass, Class<? extends ActivityExecutor> executorClass, boolean groupActivity, boolean customerDependent) {
		this.activityClass = activityClass;
		this.setExecutorClass(executorClass);
		this.groupActivity = groupActivity;
		this.customerDependent = customerDependent;

	}

	private ActivityType(Class<? extends RunningActivity> activityClass, boolean groupActivity, boolean customerDependent) {
		this(activityClass, DefaultExecutor.class, groupActivity, customerDependent);
	}

	private ActivityType(Class<? extends RunningActivity> activityClass, Class<? extends ActivityExecutor> executorClass, boolean groupActivity) {
		this(activityClass, executorClass, groupActivity, false);
	}    

	private ActivityType(Class<? extends RunningActivity> activityClass, boolean groupActivity) {
		this(activityClass, groupActivity, false);
	}    

	private ActivityType(Class<? extends RunningActivity> activityClass, boolean groupActivity, int minimumObedience) {
		this(activityClass, groupActivity);
		this.minimumObedience = minimumObedience;
	}

	private ActivityType(Class<? extends RunningActivity> activityClass, boolean groupActivity, boolean customerDependent, int minimumObedience) {
		this(activityClass, groupActivity, customerDependent);
		this.minimumObedience = minimumObedience;
	}

	private ActivityType(Class<? extends RunningActivity> activityClass, Class<? extends ActivityExecutor> executorClass, boolean groupActivity, boolean customerDependent, int minimumObedience) {
		this(activityClass, executorClass, groupActivity, customerDependent);
		this.minimumObedience = minimumObedience;
	}

	private ActivityType(Class<? extends RunningActivity> activityClass, boolean groupActivity, boolean customerDependent, 
			int minimumObedience, boolean hasSelectionOptions) {
		this(activityClass, groupActivity, customerDependent, minimumObedience);
		this.hasSelectionOptions = hasSelectionOptions;
	}

	private ActivityType(Class<? extends RunningActivity> activityClass, Class<? extends ActivityExecutor> executorClass, boolean groupActivity, boolean customerDependent, 
			int minimumObedience, boolean hasSelectionOptions) {
		this(activityClass, executorClass, groupActivity, customerDependent, minimumObedience);
		this.hasSelectionOptions = hasSelectionOptions;
	}


	public String getText() {
		return TextUtil.t(this.toString());
	}

	public static ActivityType getRandomSecondaryActivity(Customer customer){
		int chance=Util.getInt(0, 100);
		switch(customer.getType()){
		case BUM:
			if(chance>50){
				return ActivityType.BARTEND;
			}
			else if(chance>75){
				return ActivityType.SELLFOOD;
			}
			else if(chance>90){
				return ActivityType.PUBLICUSE;
			}
			else{
				return ActivityType.FIGHT;
			}
		case PEASANT:
			if(chance>50){
				return ActivityType.BARTEND;
			}
			else if(chance>75){
				return ActivityType.STRIP;
			}
			else if(chance>90){
				return ActivityType.PUBLICUSE;
			}
			else{
				return ActivityType.BATHATTENDANT;
			}
		case SOLDIER:
			if(chance>50){
				return ActivityType.BARTEND;
			}
			else if(chance>75){
				return ActivityType.STRIP;
			}
			else if(chance>90){
				return ActivityType.PUBLICUSE;
			}
			else{
				return ActivityType.FIGHT;
			}
		case MERCHANT:
			if(chance>50){
				return ActivityType.BARTEND;
			}
			else if(chance>75){
				return ActivityType.STRIP;
			}
			else if(chance>90){
				return ActivityType.SUBMITTOMONSTER;
			}
			else{
				return ActivityType.OFFERINGS;
			}
		case BUSINESSMAN:
			if(chance>50){
				return ActivityType.STRIP;
			}
			else if(chance>75){
				return ActivityType.BATHATTENDANT;
			}
			else if(chance>90){
				return ActivityType.SUBMITTOMONSTER;
			}
			else{
				return ActivityType.MONSTERFIGHT;
			}
		case MINORNOBLE:
			if(chance>50){
				return ActivityType.STRIP;
			}
			else if(chance>75){
				return ActivityType.BATHATTENDANT;
			}
			else if(chance>90){
				return ActivityType.BARTEND;
			}
			else{
				return ActivityType.FIGHT;
			}
		case LORD:
			if(chance>50){
				return ActivityType.ATTEND;
			}
			else if(chance>75){
				return ActivityType.STRIP;
			}
			else if(chance>90){
				return ActivityType.SUBMITTOMONSTER;
			}
			else{
				return ActivityType.FIGHT;
			}
		case CELEBRITY:
			if(chance>50){
				return ActivityType.STRIP;
			}
			else if(chance>75){
				return ActivityType.ATTEND;
			}
			else if(chance>90){
				return ActivityType.SUBMITTOMONSTER;
			}
			else{
				return ActivityType.MONSTERFIGHT;
			}
		default:
			return ActivityType.BARTEND;
		}

	}

	public String getDescription() {
		String text = TextUtil.tNoCheck(this.toString() + ".description");
		String hintObedience = null;
		if (minimumObedience > 0) {
			Object arguments [] = {getMinimumObedience()};
			hintObedience = TextUtil.t("activity.minimumObedience", arguments);
		}

		if (text == null && hintObedience == null) {
			return null;
		}
		else if (hintObedience == null) {
			return TextUtil.html(text);
		}
		else {
			if (text == null) {
				return TextUtil.html(hintObedience);
			}
			else {
				return TextUtil.html(text + hintObedience);
			}
		}

	}

	public boolean isGroupActivity() {
		return groupActivity;
	}

	public boolean isCustomerDependent() {
		return customerDependent;
	}

	public int getMinimumObedience() {
		return minimumObedience;
	}

	public Class<? extends RunningActivity> getActivityClass() {
		return activityClass;
	}

	public boolean isHasSelectionOptions() {
		return hasSelectionOptions;
	}

	public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
		if (hasSelectionOptions) {
			try {
				return activityClass.newInstance().getSelectionOptions(plannedActivity);
			} catch (Exception e) {
				log.error("Error trying to get selection options", e);
				return null;
			}
		}
		else {
			return null;
		}
	}

	public Class<? extends ActivityExecutor> getExecutorClass() {
		return executorClass;
	}

	public void setExecutorClass(Class<? extends ActivityExecutor> executorClass) {
		this.executorClass = executorClass;
	}
}
