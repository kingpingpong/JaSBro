package jasbro.game.character.activities;

import org.apache.log4j.Logger;

public class DefaultExecutor implements ActivityExecutor {
    private final static Logger log = Logger.getLogger(DefaultExecutor.class);

    private Shift shift;
    
    private PlannedActivity plannedActivity;

	protected RunningActivity runningActivity;
	
	@Override
	public PlannedActivity getPlannedActivity() {
		return plannedActivity;
	}

	@Override
	public void setPlannedActivity(PlannedActivity plannedActivity) {
		this.plannedActivity = plannedActivity;
	}
	
	@Override
	public void init() {
		ActivityType type=plannedActivity.getActivityDetails().getActivityType();
    	Class<? extends RunningActivity> activityClass=type.getActivityClass();
    	
		try {
			runningActivity = activityClass.newInstance();
			runningActivity.setPlannedActivity(plannedActivity);
			runningActivity.setActivityExecutor(this);
		} catch (InstantiationException e) {
			log.error("Error on instantiating Activity", e);
		} catch (IllegalAccessException e) {
			log.error("Error on instantiating Activity", e);
		}
	}

	@Override
	public void execute() {
		runningActivity.performActivity();
	}

	@Override
	public void cleanup() {
		
	}

	@Override
	public Shift getShift() {
		return shift;
	}

	@Override
	public void setShift(Shift shift) {
		this.shift = shift;
	}
	
	@Override
	public boolean isMainBusinessActivity() {
		return runningActivity instanceof BusinessMainActivity;
	}
	
	@Override
	public boolean isSecondaryBusinessActivity() {
		return runningActivity instanceof BusinessSecondaryActivity;
	}
}
