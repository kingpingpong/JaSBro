package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.RunningActivity.ModificationData;
import jasbro.game.character.activities.RunningActivity.TargetType;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.events.business.SpawnData.CustomerData;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Advertise extends RunningActivity {
	private MessageData message;
	private Charakter character;
	private int effectiveness = 8;

	public enum AdvAction {
		NORMAL, BIGBREASTS, LOLI, CLUMSY, SHY, BRIGHT, NICEBODY,
		NAKED, BODYPAINT, 
		DANCER, DANCERNAKED,
		BARTENDER, 
		WHOREBLOWJOB, WHOREFONDLE, WHOREFUCK, WHORETITFUCK, WHOREORGY,
		AVIANFLY, AVIANDRAG,
		ALCHEMISTDRUGS,
		;
	}

	private Map<Charakter, AdvAction> characterAction=new HashMap<Charakter, AdvAction>();
	@Override
	public void init() {
		character = getCharacter();
	}

	@Override
	public MessageData getBaseMessage() {
		if (message == null) {
			List<ImageTag> tags = character.getBaseTags();
			tags.add(0, ImageTag.ADVERTISE);
			tags.add(1, ImageTag.CLEANED);
			Object arguments[] = {getCharacterLocation().getName()};
			message = new MessageData(TextUtil.t("advertise.basic", character, arguments), ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()), 
					getCharacterLocation().getImage());
			if (!character.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)) {
				message.addToMessage(TextUtil.t("advertise.hintTraining", character));
			}
		}
		return message;
	}

	

	@Override
	public void perform() {
		List<AdvAction> actions = new ArrayList<AdvAction>();

		actions.add(AdvAction.NORMAL);
		actions.add(AdvAction.NORMAL);
		if(character.getTraits().contains(Trait.BIGBOOBS) && (character.getObedience()>10 || character.getType()==CharacterType.TRAINER))
			actions.add(AdvAction.BIGBREASTS);
		if(character.getTraits().contains(Trait.LOLI))
			actions.add(AdvAction.LOLI);
		if(character.getTraits().contains(Trait.CLUMSY))
			actions.add(AdvAction.CLUMSY);
		if(character.getTraits().contains(Trait.SHY) && character.getCharisma()>20 && character.getFinalValue(SpecializationAttribute.ADVERTISING)>30)
			actions.add(AdvAction.SHY);
		if(character.getTraits().contains(Trait.OUTGOING))
			actions.add(AdvAction.BRIGHT);
		if(character.getTraits().contains(Trait.UNINHIBITED))
			actions.add(AdvAction.BRIGHT);
		if(character.getTraits().contains(Trait.NICEBODY))
			actions.add(AdvAction.NICEBODY);
		if(character.getTraits().contains(Trait.EXHIBITIONIST))
			actions.add(AdvAction.NAKED);
		if(character.getTraits().contains(Trait.EXHIBITIONIST) && character.getTraits().contains(Trait.SEXFREAK))
			actions.add(AdvAction.BODYPAINT);
		if(character.getTraits().contains(Trait.SHOWINGTHEGOODS) && character.getTraits().contains(Trait.EXHIBITIONIST))
			actions.add(AdvAction.DANCERNAKED);
		if(character.getTraits().contains(Trait.SHOWINGTHEGOODS))
			actions.add(AdvAction.DANCER);
		if(character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && (character.getObedience()>10 ||character.getTraits().contains(Trait.SENSITIVE)))
			actions.add(AdvAction.WHOREFONDLE);
		if(character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && character.getFinalValue(SpecializationAttribute.SEDUCTION)>30 && (character.getObedience()>12 ||character.getTraits().contains(Trait.SENSUALTONGUE)))
			actions.add(AdvAction.WHOREBLOWJOB);
		if(character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && character.getFinalValue(SpecializationAttribute.SEDUCTION)>35 && (character.getObedience()>15 ||character.getTraits().contains(Trait.BIGBOOBS)))
			actions.add(AdvAction.WHORETITFUCK);
		if(character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && character.getFinalValue(SpecializationAttribute.SEDUCTION)>45 && (character.getObedience()>20 ||character.getTraits().contains(Trait.NATURAL)))
			actions.add(AdvAction.WHOREFUCK);
		if(character.getTraits().contains(Trait.SAMPLINGTHEGOODS) && character.getFinalValue(SpecializationAttribute.SEDUCTION)>60 && (character.getObedience()>25 ||character.getTraits().contains(Trait.SEXADDICT)))
			actions.add(AdvAction.WHOREORGY);
		if(character.getTraits().contains(Trait.HEYGUYSBOOSE))
			actions.add(AdvAction.BARTENDER);
		if(character.getTraits().contains(Trait.AVIANFLIGHT))
			actions.add(AdvAction.AVIANFLY);
		if(character.getTraits().contains(Trait.AVIANDRAG))
			actions.add(AdvAction.AVIANDRAG);
		if(character.getTraits().contains(Trait.APHRODISIACS))
			actions.add(AdvAction.ALCHEMISTDRUGS);

		characterAction.put(character, actions.get(Util.getInt(0, actions.size())));

		List<House> listHouses = new ArrayList<House>();
		List<CustomerData> bonusCustomers = new ArrayList<CustomerData>();
		int amountHouses;
		for (House house : Jasbro.getInstance().getData().getHouses()) {
			for (Room room : house.getRooms()) {
				if (room.getAmountPeople() > 0 && room.getSelectedActivity().isCustomerDependent()) {
					listHouses.add(house);
					break;
				}
			}
		}
		amountHouses = listHouses.size();

		long skill = effectiveness;
		int bonus=Util.getInt(80, 120);
		skill += character.getCharisma() / 4;
		skill += character.getFinalValue(SpecializationAttribute.ADVERTISING) / 4;

		switch(characterAction.get(character))
		{
		case NORMAL:
			message.addToMessage("\n\n" + TextUtil.t("advertise.normal", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character));
			bonus+=Util.getInt(-20, 20);
			break;
		case BIGBREASTS:
			message.addToMessage("\n\n" + TextUtil.t("advertise.bigbreasts", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character));
			bonus+=character.getCharisma()*4/5;
			break;
		case LOLI:
			message.addToMessage("\n\n" + TextUtil.t("advertise.loli", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character));
			bonus+=character.getCharisma()*3/5;
			break;
		case CLUMSY:
			message.addToMessage("\n\n" + TextUtil.t("advertise.clumsy", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character));
			bonus+=character.getCharisma()*Util.getInt(50, 100)/100;
			break;
		case SHY:
			message.addToMessage("\n\n" + TextUtil.t("advertise.shy", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character));
			bonus+=10+character.getCharisma()*2/5;
			break;
		case BRIGHT:
			message.addToMessage("\n\n" + TextUtil.t("advertise.bright", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character));
			bonus+=15+character.getCharisma()*2/5;
			break;
		case NICEBODY:
			message.addToMessage("\n\n" + TextUtil.t("advertise.nicebody", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character));
			bonus+=22+character.getCharisma()*4/5;
			break;
		case NAKED:
			message.addToMessage("\n\n" + TextUtil.t("advertise.naked", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, character));
			bonus+=40+character.getCharisma()/2;
			this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.OBEDIENCE, character));
			break;
		case BODYPAINT:
			message.addToMessage("\n\n" + TextUtil.t("advertise.bodypaint", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, character));
			bonus+=60+character.getCharisma();
			this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.OBEDIENCE, character));
			break;
		case DANCER:
			message.addToMessage("\n\n" + TextUtil.t("advertise.dancer", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character));
			bonus+=10+character.getFinalValue(SpecializationAttribute.STRIP)/2;
			this.getAttributeModifications().add(new AttributeModification(0.1f,SpecializationAttribute.STRIP, character));
			break;
		case DANCERNAKED:
			message.addToMessage("\n\n" + TextUtil.t("advertise.dancernaked", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character));
			bonus+=character.getCharisma()/2;
			bonus+=10+character.getFinalValue(SpecializationAttribute.STRIP)/2;
			this.getAttributeModifications().add(new AttributeModification(0.1f,SpecializationAttribute.STRIP, character));
			this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.OBEDIENCE, character));
			break;
		case WHOREFONDLE:
			message.addToMessage("\n\n" + TextUtil.t("advertise.whorefondle", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, character));
			bonus+=20+character.getCharisma()/4;
			bonus+=character.getFinalValue(SpecializationAttribute.SEDUCTION)/2;
			bonus+=character.getFinalValue(Sextype.FOREPLAY)/2;
			this.getAttributeModifications().add(new AttributeModification(0.1f,SpecializationAttribute.SEDUCTION, character));
			this.getAttributeModifications().add(new AttributeModification(0.1f,Sextype.FOREPLAY, character));
			break;
		case WHORETITFUCK:
			message.addToMessage("\n\n" + TextUtil.t("advertise.whorefondle", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, character));
			bonus+=30+character.getCharisma()/4;
			bonus+=character.getFinalValue(SpecializationAttribute.SEDUCTION)/2;
			bonus+=character.getFinalValue(Sextype.TITFUCK)/2;
			this.getAttributeModifications().add(new AttributeModification(0.1f,SpecializationAttribute.SEDUCTION, character));
			this.getAttributeModifications().add(new AttributeModification(0.1f,Sextype.TITFUCK, character));
			break;
		case WHOREBLOWJOB:
			message.addToMessage("\n\n" + TextUtil.t("advertise.whoreblowjob", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, character));
			bonus+=40+character.getCharisma()/4;
			bonus+=character.getFinalValue(SpecializationAttribute.SEDUCTION)/2;
			bonus+=character.getFinalValue(Sextype.ORAL)/2;
			this.getAttributeModifications().add(new AttributeModification(0.1f,SpecializationAttribute.SEDUCTION, character));
			this.getAttributeModifications().add(new AttributeModification(0.1f,Sextype.ORAL, character));
			break;
		case WHOREFUCK:
			message.addToMessage("\n\n" + TextUtil.t("advertise.whorefuck", character));
			if(Util.getInt(0, 100)<50){
				bonus+=character.getFinalValue(Sextype.VAGINAL)/2;
				message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character));
				this.getAttributeModifications().add(new AttributeModification(0.1f,Sextype.VAGINAL, character));
			}
			else{
				bonus+=character.getFinalValue(Sextype.ANAL)/2;
				message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character));
				this.getAttributeModifications().add(new AttributeModification(0.1f,Sextype.ANAL, character));
			}
			this.getAttributeModifications().add(new AttributeModification(0.1f,SpecializationAttribute.SEDUCTION, character));
			bonus+=50+character.getCharisma()/4;
			bonus+=character.getFinalValue(SpecializationAttribute.SEDUCTION)/2;			
			break;
		case WHOREORGY:
			message.addToMessage("\n\n" + TextUtil.t("advertise.whoreorgy", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
			bonus+=70+character.getCharisma()/4;
			bonus+=character.getFinalValue(SpecializationAttribute.SEDUCTION)/2;
			bonus+=character.getFinalValue(Sextype.GROUP)/2;
			this.getAttributeModifications().add(new AttributeModification(0.1f,Sextype.GROUP, character));
			this.getAttributeModifications().add(new AttributeModification(0.1f,SpecializationAttribute.SEDUCTION, character));
			break;
		case BARTENDER:
			message.addToMessage("\n\n" + TextUtil.t("advertise.bartending", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character));
			bonus+=character.getCharisma()/3;
			bonus+=10+character.getFinalValue(SpecializationAttribute.BARTENDING)/2;
			this.getAttributeModifications().add(new AttributeModification(0.1f,SpecializationAttribute.BARTENDING, character));
			this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, character));
			break;
		case ALCHEMISTDRUGS:
			message.addToMessage("\n\n" + TextUtil.t("advertise.alchemist", character));
			message.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character));
			bonus+=character.getCharisma();
			bonus+=10+character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE)*Util.getInt(50, 220)/100;
			this.getAttributeModifications().add(new AttributeModification(0.1f,SpecializationAttribute.PLANTKNOWLEDGE, character));
			break;
		}

		skill*=bonus/100;
		long fame = character.getFame().getFame();        
		if (fame > 100) {
			long fameBonus = 0;
			int divider = 500;
			do {
				fame = fame / divider;
				fameBonus++;
				divider *= 4;
			}
			while (fame > 0);
			skill += fameBonus;
		}

		if (amountHouses > 0) {
			long increasePercent = skill / amountHouses;
			if (increasePercent == 0) {
				increasePercent = 1;
			}




			Object arguments[] = {increasePercent};
			message.addToMessage("\n\n" + TextUtil.t("advertise.result", character, arguments));

			for (House house : listHouses) {
				SpawnData spawnData = house.getSpawnData();
				spawnData.addToModCustomerAmount(increasePercent / 100.0f);
				for (CustomerData bonusCustData : bonusCustomers) {
					spawnData.addFixedAmountCustomers(bonusCustData.getCustomerType(), bonusCustData.getValue());
				}
			}
		}
		else {
			List<House> houses = Jasbro.getInstance().getData().getHouses();
			long increaseFameBy = (skill * 50) / houses.size();
			for (House house : houses) {
				house.getFame().modifyFame(increaseFameBy);
			}
			Object arg[]={increaseFameBy};
			message.addToMessage("\n\n" + TextUtil.t("advertise.increaseFame", character, arg));
		}
		character.getFame().modifyFame(skill);
	}

	
	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		
		if (character.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)) {
			modifications.add(new ModificationData(TargetType.ALL, 0.51f, SpecializationAttribute.ADVERTISING));
		}
		else {
			modifications.add(new ModificationData(TargetType.ALL, 0.1f, SpecializationAttribute.ADVERTISING));
		}
		if(character.getTraits().contains(Trait.SPIRITED))
			modifications.add(new ModificationData(TargetType.ALL, 0.3f, EssentialAttributes.MOTIVATION));
		else
			modifications.add(new ModificationData(TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));

		return modifications;
	}
	public int getStartingEffectiveness() {
		return effectiveness;
	}

	public void setStartingEffectiveness(int effectiveness) {
		this.effectiveness = effectiveness;
	}



}