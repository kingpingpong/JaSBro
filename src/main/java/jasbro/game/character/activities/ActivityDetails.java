package jasbro.game.character.activities;

import jasbro.game.world.customContent.CustomQuest;

public class ActivityDetails {
    private ActivityType activityType;
    private CustomQuest quest;
    private String eventId;
    private String label;
    private String title;
    
    public ActivityDetails() {
    }
    
    public ActivityDetails(ActivityType activityType) {
        this.activityType = activityType;
    }
    
    public ActivityDetails(ActivityType activityType, String eventId) {
        super();
        this.activityType = activityType;
        this.eventId = eventId;
    }    
    
    public ActivityDetails(ActivityType activityType, String eventId, String label) {
        super();
        this.activityType = activityType;
        this.eventId = eventId;
        this.label = label;
    }
    
    public ActivityDetails(ActivityType activityType, String eventId, String label, String title) {
        super();
        this.activityType = activityType;
        this.eventId = eventId;
        this.label = label;
        this.title = title;
    }

    public ActivityDetails(ActivityType activityType, CustomQuest quest, String eventId, String label, String title) {
        super();
        this.activityType = activityType;
        this.quest = quest;
        this.eventId = eventId;
        this.label = label;
        this.title = title;
    }

    public String getText() {
        if (label != null) {
            return label;
        }
        else {
            return activityType.getText();
        }
    }
    public String getDescription() {
        if (title != null) {
            return title;
        }
        else {
            return activityType.getDescription();
        }
    }
    
    
    public ActivityType getActivityType() {
        return activityType;
    }
    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public CustomQuest getQuest() {
        return quest;
    }

    public void setQuest(CustomQuest quest) {
        this.quest = quest;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activityType == null) ? 0 : activityType.hashCode());
        result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ActivityDetails other = (ActivityDetails) obj;
        if (activityType != other.activityType)
            return false;
        if (eventId == null) {
            if (other.eventId != null)
                return false;
        } else if (!eventId.equals(other.eventId))
            return false;
        return true;
    }

    
    
}
