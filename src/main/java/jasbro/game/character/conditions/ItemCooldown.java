package jasbro.game.character.conditions;

import jasbro.game.character.Condition;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

public class ItemCooldown extends Condition {

    private int daysRemaining;
    private String itemId;
    
    public ItemCooldown(int daysRemaining, String itemId) {
        this.daysRemaining = daysRemaining;
        this.itemId = itemId;
    }
    
    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.NEXTDAY) {
            daysRemaining--;
            if (daysRemaining <= 0) {
                getCharacter().getConditions().remove(this);
            }
        }
    }
    
    @Override
    public String getName() {
        return TextUtil.t("conditions.cooldown", new Object[]{itemId});
    }
    
    @Override
    public String getDescription() {
        return TextUtil.t("conditions.cooldown.description", new Object[]{itemId, daysRemaining});
    }
    
    @Override
    public ImageData getIcon() {
        return new ImageData("images/icons/cooldown.png");
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public String getItemId() {
        return itemId;
    }
    
    
}
