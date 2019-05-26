package jasbro.game.world.customContent;

import jasbro.game.events.EventType;
import jasbro.game.world.customContent.requirements.AndRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;
import bsh.EvalError;


public class Trigger {
	private TriggerRequirement requirement = new AndRequirement();
	private TriggerType triggerType = TriggerType.ACTIVITYPERFORMED;
	private String activityLabel;
	private String activityDescription;
	
	
	public boolean isTriggered(TriggerParent triggerParent) throws EvalError {
		if (triggerType == TriggerType.CUSTOMACTIVITY && triggerParent.getEvent() == null ) {
			if (requirement == null) {
				return true;
			}
			else  {
				return requirement.isValid(triggerParent);
			}
		}
		else if (triggerParent.getEvent() != null && triggerType.getEventType() == triggerParent.getEvent().getType()) {
			if (requirement == null) {
				return true;
			}
			else  {
				return requirement.isValid(triggerParent);
			}
		}
		else {
			return false;
		}
	}
	
	public TriggerType getTriggerType() {
		return triggerType;
	}
	
	public void setTriggerType(TriggerType triggerType) {
		this.triggerType = triggerType;
	}
	
	
	public TriggerRequirement getRequirement() {
		return requirement;
	}
	
	public void setRequirement(TriggerRequirement requirement) {
		this.requirement = requirement;
	}
	
	
	
	
	
	public static enum TriggerType {
		NEXTSHIFT(EventType.NEXTSHIFT), NEXTDAY(EventType.NEXTDAY),
		ACTIVITY(EventType.ACTIVITY), ACTIVITYPERFORMED(EventType.ACTIVITYPERFORMED), 
		ACTIVITYFINISHED(EventType.ACTIVITYFINISHED), ACTIVITYCREATED(EventType.ACTIVITYCREATED),
		
		SHIFTSTART(EventType.SHIFTSTART), CUSTOMERSARRIVE(EventType.CUSTOMERSARRIVE),
		GAMESTART(EventType.GAMESTART), 
		CHARACTERGAINED(EventType.CHARACTERGAINED), CHARACTERLOST(EventType.CHARACTERLOST),
		
		CUSTOMACTIVITY,
		
		;
		
		private TriggerType() {
		}
		private TriggerType(EventType eventType) {
			this.eventType = eventType;
		}
		
		private EventType eventType;
		
		public EventType getEventType() {
			return eventType;
		}
	}
	
	
	
	
	
	public String getActivityLabel() {
		return activityLabel;
	}
	
	public void setActivityLabel(String activityLabel) {
		this.activityLabel = activityLabel;
	}
	
	public String getActivityDescription() {
		return activityDescription;
	}
	
	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}
}