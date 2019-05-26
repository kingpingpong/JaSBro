package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.game.character.Condition;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.SunEffect;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Sunbathe extends RunningActivity {

	@Override
	public MessageData getBaseMessage() {
		String message = TextUtil.t("sunbathe.beach.basic", getCharacter());

		return new MessageData(message, ImageUtil.getInstance().getImageDataByTag(ImageTag.SUNBATHE, getCharacter()), 
				getCharacterLocation().getImage());
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, 1.3f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 0.4f, BaseAttributeTypes.CHARISMA));
		return modifications;
	}

	@Override
	public void perform() {
		
		if(!getCharacter().getTraits().contains(Trait.TANLINES)){
			boolean isTanned=false;
			for(Condition condition : getCharacter().getConditions()){
				if(condition instanceof Buff.Tan && !getCharacter().getTraits().contains(Trait.SKINCARE)){
					getCharacter().removeCondition(condition);
					getCharacter().addCondition(new Buff.Sunburn());
					isTanned=true;
				}
				else if(condition instanceof Buff.LightTan){
					getCharacter().removeCondition(condition);
					getCharacter().addCondition(new Buff.Tan());
					isTanned=true;
				}


			}
			if(isTanned==false)
				getCharacter().addCondition(new Buff.LightTan());
		}
		else
			getCharacter().addCondition(new Buff.Tanlines());
	}
}