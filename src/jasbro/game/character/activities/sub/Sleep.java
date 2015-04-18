package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Illness.Flu;
import jasbro.game.events.MessageData;
import jasbro.game.housing.CleanState;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Sleep extends RunningActivity {
	
    @Override
    public MessageData getBaseMessage() {
    	Charakter character = getCharacters().get(0);
    	String message="";
    	if (Jasbro.getInstance().getData().getTime() == Time.MORNING) { message = TextUtil.t("sleepin", character);}
    	if (Jasbro.getInstance().getData().getTime() == Time.AFTERNOON) { message = TextUtil.t("nap", character);}
    	if (Jasbro.getInstance().getData().getTime() == Time.NIGHT) { message = TextUtil.t("sleep", character);}
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
        return new MessageData(message, image, getCharacter().getBackground());
    }
    
    @Override
    public List<ModificationData> getStatModifications() {
    	List<ModificationData> modifications = new ArrayList<ModificationData>();
        modifications.add(new ModificationData(TargetType.ALL, 40, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.HEALTH));
    	return modifications;
    }
    
    @Override
    public void perform() {
        if (getHouse() != null) {
            CleanState state = CleanState.calcState(getHouse());
            if (state == CleanState.FILTHY && Util.getRnd().nextBoolean()) {
                Flu flu = new Flu(false);
                getCharacter().addCondition(flu);
                getMessages().get(0).setPriorityMessage(true);
                getMessages().get(0).addToMessage("\n\n" + TextUtil.t("flu.startDirt", getCharacter()));
            }
        }
    }
}
