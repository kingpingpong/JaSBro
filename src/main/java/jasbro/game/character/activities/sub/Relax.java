package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.RunningActivity.ModificationData;
import jasbro.game.character.activities.RunningActivity.TargetType;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.SunEffect;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Relax extends RunningActivity {
	private int actionType=0;
	private Map<Charakter, relaxAction> characterAction=new HashMap<Charakter, relaxAction>();	
	private enum relaxAction {
		NAP, SNACK, SING, READ, READEVERYONE, MASTURBATE, COOLDOWN, DANCE, DANCESHOW, DIP, CAT, CLEAN, BARBECCUE, FIGHT, SMOKE, NAILS, NAILSEVERYONE, TAN, CHAT, NURSE
	}

	@Override
	public void init() {
		List<relaxAction> action = new ArrayList<relaxAction>();
		Charakter character = getCharacters().get(0);
		Long servedToday=character.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString());
		action.add(relaxAction.NAP);
		if("POND".equals(this.getRoom().getRoomInfo().getId()))
			action.add(relaxAction.DIP);
		action.add(relaxAction.SING);
		action.add(relaxAction.SNACK);
		if(Jasbro.getInstance().getData().getTime()!=Time.NIGHT)
			action.add(relaxAction.TAN);
		if(character.getFinalValue(SpecializationAttribute.SEDUCTION)>15)
			action.add(relaxAction.NAILS);
		if(character.getFinalValue(SpecializationAttribute.SEDUCTION)>40 && this.getRoom().getAmountPeople()>2)
			action.add(relaxAction.NAILSEVERYONE);
		if(character.getFinalValue(SpecializationAttribute.STRIP)>15)
			action.add(relaxAction.DANCE);
		if(character.getFinalValue(SpecializationAttribute.STRIP)>45 && this.getRoom().getAmountPeople()>1)
			action.add(relaxAction.DANCESHOW);
		if(character.getFinalValue(SpecializationAttribute.CLEANING)>15 || character.getTraits().contains(Trait.HELPFUL))
			action.add(relaxAction.CLEAN);
		if((character.getFinalValue(SpecializationAttribute.COOKING)>40 || character.getTraits().contains(Trait.RESTAURATEUR))&& this.getRoom().getAmountPeople()>2 )
			action.add(relaxAction.BARBECCUE);
		if(character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)>15 || character.getTraits().contains(Trait.CLEVER))
			action.add(relaxAction.READ);
		if((character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)>15 || character.getTraits().contains(Trait.CLEVER)) && this.getRoom().getAmountPeople()>2)
			action.add(relaxAction.READEVERYONE);
		if(character.getFinalValue(SpecializationAttribute.VETERAN)>30)
			action.add(relaxAction.FIGHT);
		if(character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE)>25)
			action.add(relaxAction.SMOKE);
		if((servedToday<2 && character.getTraits().contains(Trait.NYMPHO)) || character.getTraits().contains(Trait.INSATIABLE))
			action.add(relaxAction.MASTURBATE);
		if(servedToday>7)
			action.add(relaxAction.COOLDOWN);
		if(this.getRoom().getAmountPeople()>1)
			action.add(relaxAction.CHAT);
		if((character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE)>25 && character.getFinalValue(SpecializationAttribute.MAGIC)>25 || character.getTraits().contains(Trait.ALTRUISTIC)) && this.getRoom().getAmountPeople()>1)
			action.add(relaxAction.NURSE);

		characterAction.put(character, action.get(Util.getInt(0, action.size())));
	}


	@Override
	public MessageData getBaseMessage() {
		Charakter character = getCharacters().get(0);
		List<Charakter> characters = this.getRoom().getCurrentUsage().getCharacters();
		String message="";
		int a=Util.getInt(0, characters.size());
		message = TextUtil.t("relax.basic", character);
		message += "\n";
		ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
		for (Condition condition : character.getConditions()) {
			if (condition instanceof Buff) {
				if(((Buff) condition).getNameKey()=="RoughenedUp")
					character.removeCondition(condition);
			}
		}
		switch(characterAction.get(character)){
		case NAP:
			message += TextUtil.t("relax.nap", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
			break;
		case DIP:
			message += TextUtil.t("relax.dip", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SWIM, character);
			break;
		case SING:
			message += TextUtil.t("relax.sing", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
			break;
		case SNACK:
			message += TextUtil.t("relax.snack", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
			break;
		case TAN:
			message += TextUtil.t("relax.tan", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SUNBATHE, character);
			if(Util.getInt(0, 100)>60){
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
					if(isTanned=false)
						getCharacter().addCondition(new Buff.LightTan());
				}
				else
					getCharacter().addCondition(new Buff.Tanlines());
				message += "\n";
				message += TextUtil.t("relax.tanned", character);
			}
			break;
		case NAILS:
			message += TextUtil.t("relax.nails", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
			break;
		case NAILSEVERYONE:			
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
			do{
				a=Util.getInt(0, characters.size());
			}while(characters.get(a)==character);
			this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, characters.get(a)));
			
			message += TextUtil.t("relax.nailseveryone", character, characters.get(a));
			break;
		case DANCE:
			message += TextUtil.t("relax.dance", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
			break;
		case DANCESHOW:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
			for(Charakter target : this.getRoom().getCurrentUsage().getCharacters()){
				if(target.getName()!=character.getName()){

					this.getAttributeModifications().add(new AttributeModification(0.4f,SpecializationAttribute.STRIP, target));
					this.getAttributeModifications().add(new AttributeModification(0.5f,EssentialAttributes.MOTIVATION, target));
				}
			}
			message += TextUtil.t("relax.danceshow", character);
			break;
		case CLEAN:
			message += TextUtil.t("relax.clean", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEAN, character);
			this.getHouse().modDirt(-15);
			break;
		case BARBECCUE:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, character);
			for(Charakter target : this.getRoom().getCurrentUsage().getCharacters()){
				if(target!=character){
					this.getAttributeModifications().add(new AttributeModification(10.1f,EssentialAttributes.HEALTH, target));
					this.getAttributeModifications().add(new AttributeModification(0.5f,EssentialAttributes.MOTIVATION, target));
					character.addCondition(new Buff.Satiated(30,character));
				}
			}
			message += TextUtil.t("relax.barbeccue", character);
			break;
		case READ:
			message += TextUtil.t("relax.read", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STUDY, character);
			break;
		case READEVERYONE:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, character);
			for(Charakter target : this.getRoom().getCurrentUsage().getCharacters()){
				if(target.getName()!=character.getName()){
					this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, target));
					this.getAttributeModifications().add(new AttributeModification(0.5f,EssentialAttributes.MOTIVATION, target));
				}
			}
			message += TextUtil.t("relax.readeveryone", character);
			break;
		case FIGHT:
			message += TextUtil.t("relax.fight", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VICTORIOUS, character);
			break;
		case SMOKE:
			message += TextUtil.t("relax.smoke", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
			character.addCondition(new Buff.Stoned(10, character, Util.getInt(-50, 50)));
			break;
		case MASTURBATE:
			message += TextUtil.t("relax.masturbate", character);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character);
			break;
		case COOLDOWN:
			Long servedToday=character.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString());
			Object arg[] ={servedToday};
			message += TextUtil.t("relax.cooldown", character);
			if(servedToday>character.getStamina())
				message += TextUtil.t("relax.cooldown.enough", character, arg);
			else
				message += TextUtil.t("relax.cooldown.more", character, arg);
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.AFTERSEX, character);
			break;
		case CHAT:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
			do{
				a=Util.getInt(0, characters.size());
			}while(characters.get(a).getName()==character.getName());
			this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, characters.get(a)));
			this.getAttributeModifications().add(new AttributeModification(0.5f,EssentialAttributes.MOTIVATION, characters.get(a)));
			message += TextUtil.t("relax.chat", character, characters.get(a));
			break;
		case NURSE:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.NURSE, character);
			for(Charakter target : this.getRoom().getCurrentUsage().getCharacters()){
				if(target.getName()!=character.getName()){
					this.getAttributeModifications().add(new AttributeModification(10.1f,EssentialAttributes.HEALTH, target));
					this.getAttributeModifications().add(new AttributeModification(10.1f,EssentialAttributes.ENERGY, target));
					this.getAttributeModifications().add(new AttributeModification(0.5f,EssentialAttributes.MOTIVATION, target));
				}
			}
			message += TextUtil.t("relax.nurse", character);
			break;
		}






		return new MessageData(message, image, getCharacter().getBackground());
	}


	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, 40, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 5.0f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.HEALTH));
		switch(characterAction.get(getCharacter())){
		case NAP:
			modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.HEALTH));
			modifications.add(new ModificationData(TargetType.ALL, 3.0f, EssentialAttributes.MOTIVATION));
			break;
		case DIP:
			modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.HEALTH));
			modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 2.0f, EssentialAttributes.MOTIVATION));
			break;
		case SNACK:
			modifications.add(new ModificationData(TargetType.ALL, 10, EssentialAttributes.HEALTH));
			modifications.add(new ModificationData(TargetType.ALL, 10, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 2.0f, EssentialAttributes.MOTIVATION));
			break;
		case TAN:
			modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
			
			break;
		case NAILS:
			modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
			break;
		case NAILSEVERYONE:
			modifications.add(new ModificationData(TargetType.ALL, -10, EssentialAttributes.ENERGY));
			break;
		case DANCE:
			modifications.add(new ModificationData(TargetType.ALL, -10, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 0.8f, SpecializationAttribute.STRIP));
			modifications.add(new ModificationData(TargetType.ALL, 2.0f, EssentialAttributes.MOTIVATION));
			break;
		case DANCESHOW:
			modifications.add(new ModificationData(TargetType.ALL, -15, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 0.8f, SpecializationAttribute.STRIP));
			modifications.add(new ModificationData(TargetType.ALL, 3.0f, EssentialAttributes.MOTIVATION));
			break;
		case CLEAN:
			modifications.add(new ModificationData(TargetType.ALL, -5, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 0.8f, SpecializationAttribute.CLEANING));
			break;
		case BARBECCUE:
			modifications.add(new ModificationData(TargetType.ALL, -10, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 0.8f, SpecializationAttribute.COOKING));
			modifications.add(new ModificationData(TargetType.ALL, 3.0f, EssentialAttributes.MOTIVATION));
			break;
		case READ:
			modifications.add(new ModificationData(TargetType.ALL, 0.15f, BaseAttributeTypes.INTELLIGENCE));
			break;
		case READEVERYONE:
			modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.INTELLIGENCE));
			modifications.add(new ModificationData(TargetType.ALL, 2.0f, EssentialAttributes.MOTIVATION));
			break;
		case FIGHT:
			modifications.add(new ModificationData(TargetType.ALL, -15, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 1.01f, SpecializationAttribute.VETERAN));
			break;
		case MASTURBATE:
			modifications.add(new ModificationData(TargetType.ALL, 1.0f, Sextype.FOREPLAY));
			modifications.add(new ModificationData(TargetType.ALL, 3.0f, EssentialAttributes.MOTIVATION));
			break;
		case COOLDOWN:
			modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 2.0f, EssentialAttributes.MOTIVATION));
			break;
		case NURSE:
			modifications.add(new ModificationData(TargetType.ALL, -10, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.ALL, 1.0f, SpecializationAttribute.MEDICALKNOWLEDGE));
			modifications.add(new ModificationData(TargetType.ALL, 1.0f, SpecializationAttribute.MAGIC));

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