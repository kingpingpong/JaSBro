package jasbro.game.world.customContent.effects;

import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEvent.WorldEventVariables;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import bsh.EvalError;

public class WorldEventCreateQuest extends WorldEventEffect {
	private String questId;
	
	@Override
	public void perform(WorldEvent worldEvent) throws EvalError {
		CustomQuest quest = new CustomQuest(questId);
		worldEvent.putAttribute(WorldEventVariables.questInstance, quest);
	}
	
	@Override
	public WorldEventEffectType getType() {
		return WorldEventEffectType.CREATEQUEST;
	}
	
	public String getQuestId() {
		return questId;
	}
	
	public void setQuestId(String questId) {
		this.questId = questId;
	}
	
	
}