package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.Util.TypeAmounts;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityDetails;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.quests.BetTrainAmountQuest;
import jasbro.game.quests.Quest;
import jasbro.game.quests.SellSlaveQuest;
import jasbro.game.quests.StandardSlaveQuest;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.Time;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.CustomQuestTemplate;
import jasbro.game.world.customContent.WorldEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class QuestManager implements CentralEventlistener {
	private final static Logger log = Logger.getLogger(QuestManager.class);
	private List<Quest> activeQuests = new ArrayList<Quest>();
	private List<Quest> possibleQuests = null;
	private transient List<Quest> inactiveQuests;
	private Map<String, Integer> solvedQuests;
	private int difficultyModifier = 0;
	
	
	public QuestManager() {
		Jasbro.getInstance().getData().getEventManager().addListener(this);		
	}
	
	public List<Quest> getPossibleQuests() {
		if (possibleQuests == null) {
			possibleQuests = new ArrayList<Quest>();
			for (int i = 0; i < Math.min(10, Math.max(4, 
                    ((int) Math.sqrt(Jasbro.getInstance().getData().getProtagonist().getFame().getFame()) / 40))); 
                    i++) {
				possibleQuests.add(generateQuest());
			}
		}
		return possibleQuests;
	}
	
	public void activateQuest(Quest quest) {
		getActiveQuests().add(quest);
		difficultyModifier += 2;
		quest.init();
		getPossibleQuests().remove(quest);
		getInactiveQuests().remove(quest);
	}
	
	public Quest generateQuest() {
		if (Util.getRnd().nextInt(100) < 5) {
			return SellSlaveQuest.generate(difficultyModifier);
		}
		
		return StandardSlaveQuest.generate(difficultyModifier);
	}

	public List<Quest> getActiveQuests() {
		return activeQuests;
	}

	public int getDifficultyModifier() {
		return difficultyModifier;
	}
	
	@Override
	public void handleCentralEvent(MyEvent e) {	    
	    if (e.getType().isCustomContentRelevant()) {
	        //log.debug("Questmanager handle event: " + e.getType());
    		try {
    	        if (e.getType() == EventType.NEXTDAY) {
    	            possibleQuests = null;
    	            difficultyModifier++;
    	            getPossibleQuests();
    	        }
    		    
    			List<Quest> inactiveQuests = new ArrayList<Quest>();
    			inactiveQuests.addAll(getInactiveQuests());
    			List<Quest> activeQuests = new ArrayList<Quest>();
    			activeQuests.addAll(getActiveQuests());
    			
    			for (int i = 0; i < inactiveQuests.size(); i++) {
    			    Quest quest = null;
    			    try {
        			    quest = inactiveQuests.get(i);
        				quest.handleEvent(e);
    			    }
    			    catch (Exception ex) {
    			        log.error("Error in quest " + quest.getTitle(), ex);
    			        getInactiveQuests().remove(quest);
    			    }
                    finally {
                        if (quest != null && quest instanceof CustomQuest) {
                            ((CustomQuest)quest).reset();
                        }
                    }
    			}
                for (int i = 0; i < activeQuests.size(); i++) {
                    Quest quest = null;
                    try {
                        quest = activeQuests.get(i);
                        quest.handleEvent(e);
                    }
                    catch (Exception ex) {
                        log.error("Error in quest " + quest.getTitle(), ex);
                        getActiveQuests().remove(quest);
                    }
                    finally {
                        if (quest != null && quest instanceof CustomQuest) {
                            ((CustomQuest)quest).reset();
                        }
                    }
                }
                for (WorldEvent event : Jasbro.getInstance().getWorldEvents().values()) {
                    try {
                        event.handleEvent(e);
                    }
                    catch (Exception ex) {
                        log.error("Error in event " + event.getId(), ex);
                        Jasbro.getInstance().getWorldEvents().remove(event.getId());
                    }
                    finally {
                        event.reset();
                    }
                }
    		}
    		catch (Exception ex) {
    			log.error("Error while handling quests", ex);
    		}
        }
	}

	public void setSolved(Quest quest) {
		activeQuests.remove(quest);
	}
	
	public void setSolved(Quest quest, int difficultyModifier) {
		activeQuests.remove(quest);
		addToModifier(difficultyModifier);
	}
	
	public void addToModifier(int mod) {
		difficultyModifier += mod;
	}

	public List<Quest> getInactiveQuests() {
		if (inactiveQuests == null) {
			inactiveQuests = new ArrayList<Quest>();
			inactiveQuests.add(new BetTrainAmountQuest());
			
			for (CustomQuestTemplate questTemplate : Jasbro.getInstance().getCustomQuestTemplates().values()) {
			    if (questTemplate.getQuestStages().size() > 0 && 
			            questTemplate.getQuestStages().get(0).getTriggers().size() > 0) {
			        boolean questExists = false;
			        for (String solvedQuest : getSolvedQuests().keySet()) {
			            if (solvedQuest.equals(questTemplate.getId())) {
			                questExists = true;
			                break;
			            }
			        }
			        if (!questExists) {
    			        for (Quest quest : getActiveQuests()) {
    			            if (quest instanceof CustomQuest) {
    			                if (((CustomQuest)quest).getTemplate().getId().equals(questTemplate.getId())) {
    			                    questExists = true;
    	                            break;
    			                }
    			            }
    			        }
			        }
			        if (!questExists) {
	                    inactiveQuests.add(new CustomQuest(questTemplate.getId()));
			        }
			    }
			}
		}
		return inactiveQuests;
	}

    public void setInactiveQuests(List<Quest> inactiveQuests) {
        this.inactiveQuests = inactiveQuests;
    }
    
    public Map<String, Integer> getSolvedQuests() {
        if (solvedQuests == null) {
            solvedQuests = new HashMap<String, Integer>();
        }
        return solvedQuests;
    }

    public void modifyActivities(List<ActivityDetails> activityDetails, Time time, List<Charakter> characters, 
            TypeAmounts typeAmounts, CharacterLocation characterLocation) {
        //log.debug("Modify activities call");
        try {
            List<Quest> inactiveQuests = new ArrayList<Quest>();
            inactiveQuests.addAll(getInactiveQuests());
            List<Quest> activeQuests = new ArrayList<Quest>();
            activeQuests.addAll(getActiveQuests());
            
            for (int i = 0; i < inactiveQuests.size(); i++) {
                Quest quest = null;
                try {
                    quest = inactiveQuests.get(i);
                    if (quest instanceof CustomQuest) {
                        ((CustomQuest) quest).modifyActivities(activityDetails, time, characters, typeAmounts, characterLocation);
                    }
                }
                catch (Exception ex) {
                    log.error("Error in quest", ex);
                    getInactiveQuests().remove(quest);
                }
                finally {
                    if (quest != null && quest instanceof CustomQuest) {
                        ((CustomQuest)quest).reset();
                    }
                }
            }
            for (int i = 0; i < activeQuests.size(); i++) {
                Quest quest = null;
                try {
                    quest = activeQuests.get(i);
                    if (quest instanceof CustomQuest) {
                        ((CustomQuest) quest).modifyActivities(activityDetails, time, characters, typeAmounts, characterLocation);
                    }
                }
                catch (Exception ex) {
                    log.error("Error in quest", ex);
                    getActiveQuests().remove(quest);
                }
                finally {
                    if (quest != null && quest instanceof CustomQuest) {
                        ((CustomQuest)quest).reset();
                    }
                }
            }
            for (WorldEvent event : Jasbro.getInstance().getWorldEvents().values()) {
                try {
                    event.modifyActivities(activityDetails, time, characters, typeAmounts, characterLocation);
                }
                catch (Exception ex) {
                    log.error("Error in event", ex);
                    Jasbro.getInstance().getWorldEvents().remove(event.getId());
                }
                finally {
                    event.reset();
                }
            }
        }
        catch (Exception ex) {
            log.error("Error while handling quests", ex);
        }
    }
    
    
	
}
