package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Eat extends RunningActivity {
	int FoodRank=0;
	long Pay = 20+Jasbro.getInstance().getData().getMoney()/100;
	{	
	if (Pay>=500){FoodRank=50;}
	else if (Pay>=320){FoodRank=40;}
	else if (Pay>=180){FoodRank=30;}
	else if (Pay>=80){FoodRank=20;}
	else if (Pay>=20 && Jasbro.getInstance().getData().canAfford(20)){FoodRank=10;}
	else {FoodRank=0;}
	}

	@Override
	public MessageData getBaseMessage() {
		String message = TextUtil.t("eat.basic", getCharacters());
		message+=" ";

		List<ImageData> images = new ArrayList<ImageData>();
		
		if (FoodRank==50){ message += TextUtil.t("eat.MOAB");}
		else if (FoodRank==40){ message += TextUtil.t("eat.assortment");}
		else if (FoodRank==30){ message += TextUtil.t("eat.steak");}
		else if (FoodRank==20){ message += TextUtil.t("eat.pasta");}
		else if (FoodRank==10){ message += TextUtil.t("eat.rice");}
		else {message += TextUtil.t("eat.nothing");}
		modifyIncome( - 20*FoodRank * getCharacters().size());
		
		for (Charakter character : getCharacters()) {
			images.addAll(character.getImages());
			character.addCondition(new Buff.Satiated(FoodRank,character));
		}
		
		return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, images), getCharacterLocation().getImage());
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		
        List<ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new ModificationData(TargetType.ALL, 0.4f*FoodRank, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.ALL, 0.4f*FoodRank, EssentialAttributes.HEALTH));
        
        return modifications;
	}
}
