package jasbro.game.world.customContent;

import jasbro.game.quests.Quest;

public class EventActivityData {
    private Quest quest;
    private String eventId;
    private String activityName;
    private String toolTip;
    
    public Quest getQuest() {
        return quest;
    }
    public void setQuest(Quest quest) {
        this.quest = quest;
    }
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public String getActivityName() {
        return activityName;
    }
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    public String getToolTip() {
        return toolTip;
    }
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
    
}
