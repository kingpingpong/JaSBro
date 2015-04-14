package jasbro.game.world.customContent;

import jasbro.Jasbro;
import jasbro.MyException;
import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;
import jasbro.game.quests.Quest;
import jasbro.game.quests.QuestStage;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.WorldEvent.WorldEventVariables;
import jasbro.texts.TextUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import bsh.EvalError;

public class CustomQuestStage extends QuestStage implements Serializable {
    private final static Logger log = Logger.getLogger(CustomQuestStage.class);
    
    private String title;
    private String description;
    private boolean showInQuestLog;
    
    private List<Trigger> triggers;
    private Map<Trigger, String> triggerToWorldEventMap = new HashMap<Trigger, String>();
    
    public void handleEvent(MyEvent e, Quest questTmp) {
        if (getTriggers().size() > 0) {
            CustomQuest quest = (CustomQuest) questTmp;
            quest.putAttribute(WorldEventVariables.event, e);
            try {
                if (e.getSource() instanceof RunningActivity) {
                    RunningActivity activity = (RunningActivity) e.getSource();
                    quest.putAttribute(WorldEventVariables.activity, activity);
                    quest.putAttribute(WorldEventVariables.activitytype, activity.getType());
                    quest.putAttribute(WorldEventVariables.character, activity.getCharacters().get(0));
                    quest.putAttribute(WorldEventVariables.characters, activity.getCharacters());
                }
                else if (e.getSource() instanceof Charakter) {
                    quest.putAttribute(WorldEventVariables.character, (Charakter) e.getSource());
                }
                for (Trigger trigger : getTriggers()) {
                    if (trigger.isTriggered(quest)) {
                        WorldEvent worldEvent = Jasbro.getInstance().getWorldEvents().get(triggerToWorldEventMap.get(trigger));
                        
                        worldEvent.setAttributeMap(quest.getAttributeMap());
                        if (quest.isInterpreterInitialized()) {
                            worldEvent.setInterpreter(quest.getInterpreter());
                        }
                        quest.getTriggeredEvents().add(worldEvent);
                        
                        worldEvent.execute();
                        break;
                    }
                }
            } catch (EvalError ex) {
                log.error("Error, while evaluating custom code", ex);
                throw new MyException("Custom code failed", ex);
            }
        }
    }
    
    public void modifyActivities(List<ActivityDetails> activityDetails, Time time, List<Charakter> characters, 
            TypeAmounts typeAmounts, CharacterLocation characterLocation, Quest questTmp) {
        if (getTriggers().size() > 0) {
            CustomQuest quest = (CustomQuest) questTmp;
            try {
                quest.putAttribute(WorldEventVariables.activitytype, ActivityType.CUSTOMEVENT);
                quest.putAttribute(WorldEventVariables.typeamounts, typeAmounts);
                quest.putAttribute(WorldEventVariables.location, characterLocation.getLocationType());
                if (characters != null) {
                    quest.putAttribute(WorldEventVariables.characters, characters);
                    if (characters.size() > 0) {
                        quest.putAttribute(WorldEventVariables.character, characters.get(0));
                    }
                }
                for (Trigger trigger : getTriggers()) {
                    if (trigger.isTriggered(quest)) {
                        WorldEvent worldEvent = Jasbro.getInstance().getWorldEvents().get(triggerToWorldEventMap.get(trigger));
                        ActivityDetails activityDetail = new ActivityDetails(ActivityType.CUSTOMEVENT, quest, worldEvent.getId(), 
                                customize(trigger.getActivityLabel(), quest), customize(trigger.getActivityDescription(), quest));
                        activityDetails.add(activityDetail);
                        
                        //customize activity label
                    }
                }
            } catch (EvalError ex) {
                log.error("Error, while evaluating custom code", ex);
                throw new MyException("Custom code failed", ex);
            }
        }
    }
    
    public List<Trigger> getTriggers() {
        if (triggers == null) {
            triggers = new ArrayList<Trigger>();
        }
        return triggers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShowInQuestLog() {
        return showInQuestLog;
    }

    public void setShowInQuestLog(boolean showInQuestLog) {
        this.showInQuestLog = showInQuestLog;
    }

    public Map<Trigger, String> getTriggerToWorldEventMap() {
        return triggerToWorldEventMap;
    }
    
    public String getEventName(Trigger trigger) {
        if (triggerToWorldEventMap.containsKey(trigger)) {
            return getTriggerToWorldEventMap().get(trigger);
        }
        else {
            return null;
        }
    }
    
    public void removeTrigger(Trigger trigger) {
        if (triggerToWorldEventMap.containsKey(trigger)) {
            triggerToWorldEventMap.remove(trigger);
        }
        triggers.remove(trigger);
    }
    
    public String getTitle(Quest quest) {
        return customize(getTitle(), quest);
    }
    
    public String getDescription(Quest quest) {
        return customize(getDescription(), quest);
    }
    
    @SuppressWarnings("unchecked")
    public String customize(String text, Quest quest) {
        try {
            List<? extends Person> people = null;
            if (quest.getVariables().containsKey(WorldEventVariables.characters)) {
                people = (List<? extends Person>)quest.getVariables().get(WorldEventVariables.characters);
            }            
            return TextUtil.getInstance().applyTemplates(text, people, quest.getVariables());
        }
        catch (Exception e) {
            log.error("Error while customizing quest: ", e);
            return text;
        }
    }
    
}
