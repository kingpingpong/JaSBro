package jasbro.game.quests;

import jasbro.Jasbro;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.MyEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Quest implements MyEventListener, Serializable {
	private Map<String, Object> variables;
	private int currentStage = 0;
	private transient List<QuestStage> stages;
	
	public abstract List<QuestStage> getInitStages();
	
	public void init() {
		if (getCurrentStage() != null) {
			getCurrentStage().init(this);
		}
	}
	
	public List<QuestStage> getStages() {
		if (stages == null) {
			stages = getInitStages();
		}
		return stages;
	}
	public void setStages(List<QuestStage> stages) {
		this.stages = stages;
	}
	public QuestStage getCurrentStage() {
		return getStages().get(currentStage);
	}
	public void setCurrentStage(int currentStage) {
		this.currentStage = currentStage;
	}
	
	public void setResolved() {
		Jasbro.getInstance().getData().getQuestManager().setSolved(this);
	}
	
	public void setActive() {
		Jasbro.getInstance().getData().getQuestManager().activateQuest(this);
	}
	
	@Override
	public void handleEvent(MyEvent e) {
		if (getCurrentStage() != null) {
			getCurrentStage().handleEvent(e, this);
		}
	}
	
	public String getTitle() {
		return getCurrentStage().getTitle(this);
	}
	
	public String getDescription() {
		return getCurrentStage().getDescription(this);
	}
	
	public boolean canFinishEarly() {
		return getCurrentStage().canFinishEarly(this);
	}
	
	public void finish() {
		getCurrentStage().finish(this);
	}
	
	public Map<String, Object> getVariables() {
		if (variables == null) {
			variables = new HashMap<String, Object>();
		}
		return variables;
	}
	
	public void setVariable(String key, Object value) {
		getVariables().put(key, value);
	}
	
	public Object getVariable(String key) {
		if (getVariables().containsKey(key)) {
			return getVariables().get(key);
		}
		else {
			return null;
		}
	}
	
	public boolean showInQuestLog() {
		return true;
	}
	
	public int getCurrentStageNumber() {
		return currentStage;
	}
}