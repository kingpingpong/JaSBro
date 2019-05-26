package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.events.MessageData;
import jasbro.game.items.Item;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;



public class Fish extends RunningActivity {
	
	private final static String smallFish = "FISH_Small_Fish";
	private final static String mediumFish = "FISH_Medium_Fish";
	private final static String bigFish = "FISH_Big_Fish";
	
	@Override
	public MessageData getBaseMessage() {
		String message = TextUtil.t("fish.basic", getCharacter());
		message += "\n";
		int fishCatch=Util.getInt(0, 8);
		int amount=Util.getInt(2, 3);
		Object arguments[] = { amount, getCharacter()};
		switch (fishCatch)
		{
		case 1:
			message += TextUtil.t("fish.none", getCharacter(), arguments);
			break;
		case 2:
			message += TextUtil.t("fish.small",getCharacter(), arguments);
			break;
		case 3:
			message += TextUtil.t("fish.medium",getCharacter(), arguments);
			break;
		case 4:
			message += TextUtil.t("fish.big",getCharacter(), arguments);
			break;
		case 5:
			message += TextUtil.t("fish.asleep", getCharacter(),arguments);
			break;
		default:
			message += TextUtil.t("fish.none", getCharacter(),arguments);
			break;
		}
		message += "\n";
		if(fishCatch>1 && fishCatch<5){
			if(Util.getInt(0, 10)<5){
				message += TextUtil.t("fish.eat", getCharacter());
				getCharacter().addCondition(new Buff.Satiated(fishCatch,getCharacter()));
			}
			else{
				message += TextUtil.t("fish.keep", getCharacter());
				
				if (Jasbro.getInstance().getItems().containsKey(smallFish)
						&& Jasbro.getInstance().getItems().containsKey(bigFish)
						&&Jasbro.getInstance().getItems().containsKey(mediumFish)) {
					Item item = Jasbro.getInstance().getItems().get(smallFish);
					switch (fishCatch)
					{
					case 2:
						Jasbro.getInstance().getData().getInventory().addItems(item, amount);
						break;
					case 3:
						item = Jasbro.getInstance().getItems().get(mediumFish);
						Jasbro.getInstance().getData().getInventory().addItems(item, amount);
						break;
					case 4:
						item = Jasbro.getInstance().getItems().get(bigFish);
						Jasbro.getInstance().getData().getInventory().addItems(item, amount);
						break;
					}
				}
				
				
			}
		}
		
		return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SWIM, getCharacter()),
				getCharacterLocation().getImage());
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, -10, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 2.6f, EssentialAttributes.MOTIVATION));
		
		
		return modifications;
	}
}