package jasbro.game.character.activities;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import jasbro.game.character.activities.sub.Harvest;
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
import jasbro.game.character.activities.sub.Ritual;
import jasbro.game.character.activities.sub.Rob;
import jasbro.game.character.activities.sub.Sex;
import jasbro.game.character.activities.sub.Sleep;
import jasbro.game.character.activities.sub.Soak;
import jasbro.game.character.activities.sub.Study;
import jasbro.game.character.activities.sub.Sunbathe;
import jasbro.game.character.activities.sub.Swim;
import jasbro.game.character.activities.sub.Talk;
import jasbro.game.character.activities.sub.Train;
import jasbro.game.character.activities.sub.TrainToFight;
import jasbro.game.character.activities.sub.Walk;
import jasbro.game.character.activities.sub.WorkForGuild;
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
import jasbro.game.character.activities.sub.whore.Struggle;
import jasbro.game.character.activities.sub.whore.Suck;
import jasbro.game.character.activities.sub.whore.Tease;
import jasbro.game.character.activities.sub.whore.Whore;
import jasbro.game.character.activities.sub.whore.WhoreStreets;
import jasbro.game.events.business.Customer;
import jasbro.gui.pages.SelectionData;
import jasbro.texts.TextUtil;

public enum ActivityType {
	REFUSEDTOWORK(RefusedToWork.class, true),
	SLEEP(Sleep.class, true), IDLE(Idle.class, false), CAMP(Camp.class, true), 
	SEX(Sex.class, true, false, 3, true), THREESOME(Orgy.class, true, 5), ORGY(Orgy.class, true, 8), 
	COOK(Cook.class, true, 1), CLEAN(Clean.class, false, 1), GOVERN(Govern.class, false, false, 0, true),
	SUNBATHE(Sunbathe.class, false), EAT(Eat.class, false), WALK(Walk.class, false),  ROB(Rob.class, false),
	TRAINTOFIGHT(TrainToFight.class, false), BATHE(Sleep.class, false), READ(Read.class, false),
	TRAIN(Train.class, true, false, 0, true), TEACH(Train.class, true, false, 0, true), 
	TALK(Talk.class, true, false, 0, true),   RELAX(Relax.class, false), FISH(Fish.class, false), GARDENING(Gardening.class, false), RITUAL(Ritual.class, false), PRACTICE(Practice.class, false),
	WHORE(Whore.class, false, true, 4), WHORESTREETS(WhoreStreets.class, false, true, 4), PRAY(Pray.class, false),   OFFERINGS(Offerings.class, false, true, 1),
	BARTEND(Bartend.class, true, true, 1), SOAK(Soak.class, false),
	SUBMITTOMONSTER(SubmitToMonster.class, false, true, 10, true), STUDY(Study.class, false), WORKGUILD(WorkForGuild.class, false),
	SWIM(Swim.class, false), NURSE(Nurse.class, true), SELLFOOD(SellFood.class, false, true, 1),HARVEST(Harvest.class, false),
	STRIP(Strip.class, false, true, 8), CATSHOW(Catshow.class, false, true, 8),
	BREAK(Break.class, true),
	DOMINATE(Dominate.class, false, true), PUBLICUSE(PublicUse.class, true, true, 8),
	SUBMIT(Submit.class, false, true, 9),SUCK(Suck.class, false, true, 4),
	TEASE(Tease.class, false, true, 4), PAMPER(Pamper.class,true), BODYWRAP(BodyWrap.class,true),
	FIGHT(Fight.class, true, true, 2), ATTEND(Attend.class, true, true, 5),
	ADVERTISE(Advertise.class, false, 1),
	BATHATTENDANT(BathAttendant.class, true, true, 4),
	MASSAGE(Massage.class, false, true, 3),
	EVENT(null, false),
	CUSTOMEVENT(null, false),
	MONSTERFIGHT(MonsterFight.class, true, true, 8, true), PUBLICIZE(Publicize.class, false, 1),
	PLAY(Play.class, true),
	EXPLORE(Explore.class, false),
	STRUGGLE(Struggle.class, false, true);
	
	private final static Logger log = LogManager.getLogger(ActivityType.class);
	private Class<? extends RunningActivity> activityClass;
	private boolean groupActivity;
	private boolean customerDependent;
	private int minimumObedience = 0;
	private boolean hasSelectionOptions = false;
	
	
	
	
	private ActivityType(Class<? extends RunningActivity> activityClass, boolean groupActivity, boolean customerDependent) {
		this.activityClass = activityClass;
		this.groupActivity = groupActivity;
		this.customerDependent = customerDependent;
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
	
	private ActivityType(Class<? extends RunningActivity> activityClass, boolean groupActivity, boolean customerDependent, 
			int minimumObedience, boolean hasSelectionOptions) {
		this(activityClass, groupActivity, customerDependent, minimumObedience);
		this.hasSelectionOptions = hasSelectionOptions;
	}
	
	
	public String getText() {
		return TextUtil.t(this.toString());
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
	
	public RunningActivity getActivity() {
		try {
			return activityClass.newInstance();
		} catch (InstantiationException e) {
			log.error("Error on instantiating Activity", e);
		} catch (IllegalAccessException e) {
			log.error("Error on instantiating Activity", e);
		}
		return null;
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
}