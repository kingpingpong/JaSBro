package jasbro.game.character.activities;

public interface ActivityExecutor {

	PlannedActivity getPlannedActivity();

	void setPlannedActivity(PlannedActivity plannedActivity);

	Shift getShift();

	void setShift(Shift shift);

	void init();
	
	void execute();

	void cleanup();

	boolean isMainBusinessActivity();

	boolean isSecondaryBusinessActivity();
}
