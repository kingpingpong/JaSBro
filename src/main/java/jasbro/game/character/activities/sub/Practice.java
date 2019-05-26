package jasbro.game.character.activities.sub;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Practice extends RunningActivity {
	private int actionType=0;
	private List<String> actions = new ArrayList<String>();

	@Override
	public void init() {
		if(getCharacter().getFinalValue(EssentialAttributes.MOTIVATION)<70)
			actions.add("practice.lazy");
		actions.add("practice.stamina");
		actions.add("practice.strength");
		if(getCharacter().getFinalValue(SpecializationAttribute.SEDUCTION)>20)
			actions.add("practice.stamina");
		if(getCharacter().getFinalValue(SpecializationAttribute.SEDUCTION)>40)
			actions.add("practice.stamina");
		if(getCharacter().getFinalValue(SpecializationAttribute.SEDUCTION)>60)
			actions.add("practice.stamina");
		if(getCharacter().getFinalValue(Sextype.GROUP)>50)
			actions.add("practice.stamina");
		if(getCharacter().getFinalValue(SpecializationAttribute.STRIP)>20)
		{
			actions.add("practice.dance");
			actions.add("practice.stamina");
		}
		if(getCharacter().getFinalValue(SpecializationAttribute.STRIP)>40)
		{
			actions.add("practice.dance");
		}
		if(getCharacter().getFinalValue(SpecializationAttribute.STRIP)>60)
		{
			actions.add("practice.dance");
		}
		if(getCharacter().getFinalValue(SpecializationAttribute.VETERAN)>20)
		{
			actions.add("practice.fight");
			actions.add("practice.strength");
		}
		if(getCharacter().getFinalValue(SpecializationAttribute.VETERAN)>40)
		{
			actions.add("practice.fight");
			actions.add("practice.strength");
		}
		if(getCharacter().getFinalValue(SpecializationAttribute.VETERAN)>60)
		{
			actions.add("practice.fight");
			actions.add("practice.strength");
		}

		int choice = (int) (Math.random() * actions.size());

		if(actions.get(choice)=="practice.lazy"){
			setactionType(1);
		}
		if(actions.get(choice)=="practice.stamina"){
			setactionType(2);
		}
		if(actions.get(choice)=="practice.strength"){
			setactionType(3);
		}
		if(actions.get(choice)=="practice.dance"){
			setactionType(4);
		}
		if(actions.get(choice)=="practice.fight"){
			setactionType(5);
		}


	}

	@Override
	public MessageData getBaseMessage() {
		Charakter character = getCharacters().get(0);
		String message="";
		message = TextUtil.t("practice.basic", character);


		ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);


		switch(getactionType()){
		case 1:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
			message+=("\n\n" + TextUtil.t( "practice.lazy",character));
			break;
		case 2:
			message+=("\n\n" + TextUtil.t( "practice.stamina",character));
			break;
		case 3:
			message+=("\n\n" + TextUtil.t( "practice.strength",character));
			break;
		case 4:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
			message+=("\n\n" + TextUtil.t( "practice.dance",character));
			break;
		case 5:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, character);
			message+=("\n\n" + TextUtil.t( "practice.fight",character));
			break;


		}



		return new MessageData(message, image, getCharacter().getBackground());
	}


	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, -5, EssentialAttributes.ENERGY));
		switch (getactionType()){
		case 1:
			modifications.add(new ModificationData(TargetType.ALL, 2, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, -0.5f, EssentialAttributes.MOTIVATION));
			break;
		case 2:
			modifications.add(new ModificationData(TargetType.ALL, -35, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 0.5f, BaseAttributeTypes.STAMINA));
			modifications.add(new ModificationData(TargetType.ALL, 1.1f, EssentialAttributes.MOTIVATION));
			break;
		case 3:
			modifications.add(new ModificationData(TargetType.ALL, -35, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 0.5f, BaseAttributeTypes.STRENGTH));
			modifications.add(new ModificationData(TargetType.ALL, 1.1f, EssentialAttributes.MOTIVATION));
			break;
		case 4:
			modifications.add(new ModificationData(TargetType.ALL, -35, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 1.0f, SpecializationAttribute.STRIP));
			modifications.add(new ModificationData(TargetType.ALL, 1.1f, EssentialAttributes.MOTIVATION));
			break;
		case 5:
			modifications.add(new ModificationData(TargetType.ALL, -35, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 1.0f, SpecializationAttribute.VETERAN));
			modifications.add(new ModificationData(TargetType.ALL, 1.1f, EssentialAttributes.MOTIVATION));
			break;

		}

		return modifications;
	}

	public int getactionType() {
		return actionType;
	}

	public void setactionType(int actionType) {
		this.actionType = actionType;
	}
}