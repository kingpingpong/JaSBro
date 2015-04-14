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
	private final int foodRank;
	private final long pay;
	
	public Eat() {
		super();
		pay = Jasbro.getInstance().getData().getMoney()/100;
		if(pay < 100) {
			foodRank = 0;
		} else if (pay < 400) {
			if(Jasbro.getInstance().getData().canAfford(20)) {
				foodRank = 10;
			} else {
				foodRank = 0;
			}
		} else if(pay < 900) {
			foodRank = 20;
		} else if(pay < 1600 ) {
			foodRank = 30;
		} else if(pay < 2500) {
			foodRank = 40;
		} else {
			foodRank = 50;
		}
	}

	@Override
	public MessageData getBaseMessage() {
		StringBuilder builder = new StringBuilder(TextUtil.t("eat.basic", getCharacters()));
		builder.append(" ");

		List<ImageData> images = new ArrayList<ImageData>();
		
		switch(foodRank) {
		case 50:
			builder.append(TextUtil.t("eat.MOAB"));
			break;
		case 40:
			builder.append(TextUtil.t("eat.assortment"));
			break;
		case 30:
			builder.append(TextUtil.t("eat.steak"));
			break;
		case 20:
			builder.append(TextUtil.t("eat.pasta"));
			break;
		case 10:
			builder.append(TextUtil.t("eat.rice"));
			break;
		default:
			builder.append(TextUtil.t("eat.nothing"));
			break;
		}
		
		modifyIncome( -1 * foodRank * foodRank);
		
		for (Charakter character : getCharacters()) {
			images.addAll(character.getImages());
			character.addCondition(new Buff.Satiated(foodRank,character));
		}
		
		return new MessageData(builder.toString(), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, images), getCharacterLocation().getImage());
	}
	
	@Override
	public List<ModificationData> getStatModifications() {
		
        List<ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
        modifications.add(new ModificationData(TargetType.ALL, 0.4f*foodRank, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.ALL, 0.4f*foodRank, EssentialAttributes.HEALTH));
        
        return modifications;
	}
}
