package jasbro.game.character.conditions;

import jasbro.Util;
import jasbro.game.character.Condition;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;

import java.util.List;

public class OvipositionPregnancy extends Condition implements PregnancyInterface {
    private PregnancyInterface realPregnancy;
    
    @Override
    public void init() {
        super.init();
        int selection = Util.getInt(0, 10);
        if (selection < 2) {
        //if (Util.getRnd().nextBoolean()) {
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
    	if (e.getType() == EventType.NEXTDAY) {
    		modifyDays(-1);
    		if (this.getDays() <= 0) {
    			realPregnancy.handleEvent(e);
            	getCharacter().removeCondition(this);
    		}
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
    public String getName() {
    		return TextUtil.t("oviposition", getCharacter());
    }
    
    @Override
    public String getDescription() {
        return getName() + "\n" +
                TextUtil.t("oviposition.description", new Object[]{realPregnancy.getDays()});
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
