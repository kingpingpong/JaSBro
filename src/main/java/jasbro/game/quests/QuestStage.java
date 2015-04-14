package jasbro.game.quests;

import jasbro.game.events.MyEvent;

public class QuestStage {

	public void handleEvent(MyEvent e, Quest quest) {
	}
	
	public String getTitle(Quest quest) {
		return "";
	}
	
	public String getDescription(Quest quest) {
		return "";
	}
	
	public boolean showInQuestlog(Quest quest) {
		return false;
	}

	public void init(Quest quest) {		
	}
	
	public boolean canFinishEarly(Quest quest) {
		return false;
	}

	public void finish(Quest quest) {
	}
}
