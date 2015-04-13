package jasbro.game.world.customContent.effects;

import bsh.EvalError;
import jasbro.Jasbro;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import jasbro.game.world.market.QuestManager;

public class WorldEventSetQuestStatus extends WorldEventEffect {
    private QuestStatus questStatus;
    
    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        if (questStatus != null) {
            CustomQuest quest = worldEvent.getQuest();
            QuestManager questManager = Jasbro.getInstance().getData().getQuestManager();
            if (questStatus == QuestStatus.ACTIVE) {
                questManager.activateQuest(quest);
            }
            else if (questStatus == QuestStatus.SOLVED) {
                questManager.setSolved(quest);
            }
            else if (questStatus == QuestStatus.INACTIVE) {
                questManager.getActiveQuests().remove(quest);
                questManager.getInactiveQuests().add(quest);
                quest.getVariables().clear();
            }
            else if (questStatus == QuestStatus.SOLVEDFORGOOD) {
                questManager.setSolved(quest);
                questManager.getSolvedQuests().put(quest.getTemplate().getId(), quest.getCurrentStageNumber());
            }
            else if (questStatus == QuestStatus.ACTIVATABLE) {
                questManager.getInactiveQuests().remove(quest);
                questManager.getActiveQuests().remove(quest);
                questManager.getPossibleQuests().add(quest);
            }
        }
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.SETQUESTSTATUS;
    }   
    
    public QuestStatus getQuestStatus() {
        return questStatus;
    }

    public void setQuestStatus(QuestStatus questStatus) {
        this.questStatus = questStatus;
    }

    public static enum QuestStatus {
        ACTIVE, SOLVED, INACTIVE, SOLVEDFORGOOD, ACTIVATABLE
    }
}
