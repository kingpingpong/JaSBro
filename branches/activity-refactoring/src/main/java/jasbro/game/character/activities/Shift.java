package jasbro.game.character.activities;

import jasbro.game.world.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * The shift is used to keep data used by activity executors while it is being performed.
 * @author Aiko
 *
 */
public class Shift {
	private int day;
	
	private Time time;
	
	private List<ActivityExecutor> activityExecutors=new ArrayList<ActivityExecutor>();

	public Shift(int day, Time time) {
		this.day=day;
		this.time=time;
	}
	
	public int getDay() {
		return day;
	}

	public Time getTime() {
		return time;
	}

	public List<ActivityExecutor> getActivityExecutors() {
		return activityExecutors;
	}

	public void setActivityExecutors(List<ActivityExecutor> activityExecutors) {
		this.activityExecutors = activityExecutors;
	}
	
	
}
