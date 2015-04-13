package jasbro.game.character.conditions;

import jasbro.Util;
import jasbro.game.character.Condition;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;

import java.util.List;

public class OvipositionPregnancy extends Condition implements PregnancyInterface {
    private PregnancyInterface realPregnancy;
    
    @Override
    public void init() {
        super.init();
        if (Util.getRnd().nextBoolean()) {
            realPregnancy = new Pregnancy(getCharacter(), null, null, false);
        }
        else {
            realPregnancy = new MonsterPregnancy(getCharacter(), null, false);
        }
        realPregnancy.modifyDays(-realPregnancy.getDays()+30);
        realPregnancy.setCharacter(getCharacter());
    }
    
    @Override
    public void handleEvent(MyEvent e) {
        realPregnancy.handleEvent(e);
        if (realPregnancy.getDays() <= 0 && e.getType() == EventType.NEXTDAY) {
            getCharacter().removeCondition(this);
        }
    }
    
    @Override
    public void reduceDays(int amount) {
        realPregnancy.reduceDays(amount);
    }

    @Override
    public void modifyDays(int amount) {
        realPregnancy.modifyDays(amount);
    }

    @Override
    public int getDays() {
        return realPregnancy.getDays();
    }

    @Override
    public ImageData getIcon() {
        return new ImageData("images/icons/perks/oviposition.png");
    }
    
    @Override
    public void modifyImageTags(List<ImageTag> imageTags) {
        imageTags.add(ImageTag.PREGNANT);
    }
}
