package jasbro.game.character.conditions;

import jasbro.Jasbro;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Nurse;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.Time;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class SleepDeprivation extends Condition {
    private int severity = 1;

    @Override
    public String getName() {
        return TextUtil.t("conditions.sleepDeprivation");
    }

    @Override
    public String getDescription() {
        Object arguments[] = {getStaminaDebuff()};
        return getName() + "\n" + TextUtil.t("conditions.sleepDeprivation.description", getCharacter(), arguments);

    }

    @Override
    public ImageData getIcon() {
        return new ImageData("images/icons/night.png");
    }
    
    @Override
    public void init() {
        super.init();
        if (getCharacter().getConditions().contains(this)) {
            new MessageScreen(TextUtil.t("conditions.sleepDeprivation.text", getCharacter()), ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, getCharacter()), getCharacter().getBackground(), true);
        }
    }
    
    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.ACTIVITYPERFORMED) {
            RunningActivity activity = (RunningActivity) e.getSource();
            if (activity.getType() == ActivityType.SLEEP) {
                severity -= 15;
            }
            else if (activity.getType() == ActivityType.NURSE) {
                Nurse nurse = (Nurse) activity;
                if (nurse.getNurse() != getCharacter()) {
                    severity -= 15;
                }
            }
            else if (activity.getType() == ActivityType.IDLE && Jasbro.getInstance().getData().getTime() == Time.NIGHT) {
                severity -= 15;
            }
            if (severity <= 0) {
                removeThis();
            }
        }
        else if (e.getType() == EventType.NEXTSHIFT) {
            severity++;
            getCharacter().getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(-0.5f);
            if (getCharacter().getFinalValue(BaseAttributeTypes.STAMINA) <= 0) {
                new MessageScreen(TextUtil.t("conditions.sleepDeprivation.deathStamina", getCharacter()), 
                        new ImageData("images/backgrounds/coffin.png"), getCharacter().getBackground(), true);
                Jasbro.getInstance().removeCharacter(getCharacter());
            }
            else if (getCharacter().getFinalValue(BaseAttributeTypes.INTELLIGENCE) <= 0) {
                Jasbro.getInstance().getData().spendMoney(-200, getCharacter().getName());
                new MessageScreen(TextUtil.t("conditions.sleepDeprivation.deathIntelligence", getCharacter()), 
                        new ImageData("images/backgrounds/coffin.png"), getCharacter().getBackground(), true);
                Jasbro.getInstance().removeCharacter(getCharacter());
            }
        }
    }
    
    @Override
    public float getAttributeModifier(Attribute attribute) {
        if (attribute.getAttributeType() == BaseAttributeTypes.STAMINA) {
            return - getStaminaDebuff();
        }
        else {
            return 0;
        }
    }
    
    public int getStaminaDebuff() {
        return severity / 2 + 1;
    }

}
