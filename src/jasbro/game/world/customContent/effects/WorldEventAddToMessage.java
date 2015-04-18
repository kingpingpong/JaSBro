package jasbro.game.world.customContent.effects;

import jasbro.game.character.activities.RunningActivity;
import jasbro.game.events.MessageData;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import jasbro.texts.TextUtil;
import bsh.EvalError;

public class WorldEventAddToMessage extends WorldEventEffect {
    private String text;
    private boolean changeToPriorityMessage = false;

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        RunningActivity activity = worldEvent.getActivity();
        MessageData messageData = activity.getMessages().get(activity.getMessages().size()-1);
        if (text != null) {
            messageData.addToMessage(TextUtil.getInstance().applyTemplates(text, worldEvent.getPeople(), 
                    worldEvent.generateAttributeMap()));
        }
        if (changeToPriorityMessage) {
            messageData.setPriorityMessage(true);
        }
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.ADDTOMESSAGE;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChangeToPriorityMessage() {
        return changeToPriorityMessage;
    }

    public void setChangeToPriorityMessage(boolean changeToPriorityMessage) {
        this.changeToPriorityMessage = changeToPriorityMessage;
    }

    
}
