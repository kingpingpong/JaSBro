package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Illness.Flu;
import jasbro.game.events.MessageData;
import jasbro.game.housing.CleanState;
import jasbro.game.interfaces.Person;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Sleep extends RunningActivity {

	private MessageData messageData;
	
	@Override
	public MessageData getBaseMessage() {
		String message="";

		List<Charakter> characters = getCharacters();
		this.messageData = new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, getCharacter()),getBackground());
		for(int i=1; i<characters.size(); i++){
			this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, getCharacters().get(i)));
		}

		if(getCharacters().size()==1){		
			if (Jasbro.getInstance().getData().getTime() == Time.MORNING) 
				message = TextUtil.t("sleepin.solo", getCharacters().get(0));
			if (Jasbro.getInstance().getData().getTime() == Time.AFTERNOON)  
				message = TextUtil.t("nap.solo", getCharacters().get(0));
			if (Jasbro.getInstance().getData().getTime() == Time.NIGHT)  
				message = TextUtil.t("sleep.solo", getCharacters().get(0));
		}
		else{
			if (Jasbro.getInstance().getData().getTime() == Time.MORNING) 
				message = TextUtil.t("sleepin.group", getCharacters());
			if (Jasbro.getInstance().getData().getTime() == Time.AFTERNOON)  
				message = TextUtil.t("nap.group", getCharacters());
			if (Jasbro.getInstance().getData().getTime() == Time.NIGHT)  
				message = TextUtil.t("sleep.group", getCharacters());
		}

		messageData.addToMessage(message);
		return this.messageData;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, 40, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.HEALTH));
		modifications.add(new ModificationData(TargetType.ALL, 0.15f, EssentialAttributes.MOTIVATION));
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