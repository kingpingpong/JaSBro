package jasbro.game.world.customContent.effects;

import jasbro.Jasbro;
import jasbro.game.items.Item;
import jasbro.game.world.customContent.WorldEvent;
import jasbro.game.world.customContent.WorldEventEffect;
import jasbro.game.world.customContent.WorldEventEffectType;
import bsh.EvalError;

public class WorldEventGainItem extends WorldEventEffect {
    private String itemId;

    @Override
    public void perform(WorldEvent worldEvent) throws EvalError {
        Item item = Jasbro.getInstance().getItems().get(itemId);
        Jasbro.getInstance().getData().getInventory().addItem(item);
    }

    @Override
    public WorldEventEffectType getType() {
        return WorldEventEffectType.GAINITEM;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }



    
}
