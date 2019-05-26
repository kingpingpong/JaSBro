package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.game.housing.Room;
import jasbro.game.items.ItemType;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Govern extends RunningActivity {

	private SpecializationType specialization;
	private List<Charakter> students = new ArrayList<Charakter>();
	private BaseAttributeTypes statBoost;
	private SpecializationAttribute skillBoost1=null;
	private SpecializationAttribute skillBoost2=null;
	private Sextype sexBoost1=null;
	private Sextype sexBoost2=null;

	@Override
	public void init() {
		List<SelectionData<SpecializationType>> options = getSuperviseOptions(getCharacter());
		if (getPlannedActivity().getSelectedOption() == null) {
			SelectionData<SpecializationType> selectedOption = new SelectionScreen<SpecializationType>().select(options, 
					ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacters().get(0)), null, 
					getCharacters().get(0).getBackground(), TextUtil.t("train.option.description", getCharacters().get(0), TextUtil.listCharacters(students)));
			specialization = selectedOption.getSelectionObject();


		}
		else {
			specialization = (SpecializationType) getPlannedActivity().getSelectedOption().getSelectionObject();
		}
		for(Room room : this.getHouse().getRooms()){
			for(Charakter character : room.getCurrentUsage().getCharacters()){
				if(character.getActivity().getType()==ActivityType.WHORE && specialization == SpecializationType.WHORE){
					students.add(character);
					if(Util.getInt(0, 10)>5)
						statBoost=BaseAttributeTypes.CHARISMA;
					else
						statBoost=BaseAttributeTypes.STAMINA;
					skillBoost1=SpecializationAttribute.SEDUCTION;
					switch(Util.getInt(0, 7)){
					case 1:
						sexBoost1=Sextype.TITFUCK;
						break;
					case 2:
						sexBoost1=Sextype.ANAL;
						break;
					case 3:
						sexBoost1=Sextype.ORAL;
						break;
					case 4:
						sexBoost1=Sextype.ANAL;
						break;
					case 5:
						sexBoost2=Sextype.FOREPLAY;
						break;
					default:
						sexBoost1=Sextype.VAGINAL;
						break;

					}
					switch(Util.getInt(0, 7)){
					case 1:
						sexBoost2=Sextype.TITFUCK;
						break;
					case 2:
						sexBoost2=Sextype.ANAL;
						break;
					case 3:
						sexBoost2=Sextype.ORAL;
						break;
					case 4:
						sexBoost2=Sextype.ANAL;
						break;
					case 5:
						sexBoost2=Sextype.FOREPLAY;
						break;
					default:
						sexBoost2=Sextype.VAGINAL;
						break;

					}
				}	
				else if(character.getActivity().getType()==ActivityType.PUBLICUSE && specialization == SpecializationType.KINKYSEX){
					students.add(character);
					statBoost=BaseAttributeTypes.STAMINA;
					sexBoost1=Sextype.GROUP;

				}
				else if(character.getActivity().getType()==ActivityType.CLEAN && specialization == SpecializationType.KINKYSEX){
					students.add(character);
					statBoost=BaseAttributeTypes.OBEDIENCE;
					skillBoost1=SpecializationAttribute.CLEANING;

				}
				else if(character.getActivity().getType()==ActivityType.COOK && specialization == SpecializationType.KINKYSEX){
					students.add(character);
					statBoost=BaseAttributeTypes.OBEDIENCE;
					skillBoost1=SpecializationAttribute.COOKING;

				}
				else if(character.getActivity().getType()==ActivityType.SELLFOOD && specialization == SpecializationType.KINKYSEX){
					students.add(character);
					statBoost=BaseAttributeTypes.OBEDIENCE;
					skillBoost1=SpecializationAttribute.COOKING;

				}
				else if(character.getActivity().getType()==ActivityType.SUCK && specialization == SpecializationType.WHORE){
					students.add(character);
					statBoost=BaseAttributeTypes.STAMINA;
					skillBoost1=SpecializationAttribute.SEDUCTION;
					sexBoost1=Sextype.ORAL;

				}
				else if(character.getActivity().getType()==ActivityType.TEASE && specialization == SpecializationType.WHORE){
					students.add(character);
					statBoost=BaseAttributeTypes.STAMINA;
					skillBoost1=SpecializationAttribute.SEDUCTION;
					sexBoost1=Sextype.FOREPLAY;

				}
				else if(character.getActivity().getType()==ActivityType.BARTEND && specialization == SpecializationType.BARTENDER){
					students.add(character);
					if(Util.getInt(0, 10)>5)
						statBoost=BaseAttributeTypes.CHARISMA;
					else
						statBoost=BaseAttributeTypes.INTELLIGENCE;
					skillBoost1=SpecializationAttribute.BARTENDING;

				}
				else if(character.getActivity().getType()==ActivityType.STRIP && specialization == SpecializationType.DANCER){
					students.add(character);
					if(Util.getInt(0, 10)>5)
						statBoost=BaseAttributeTypes.CHARISMA;
					else
						statBoost=BaseAttributeTypes.STAMINA;
					skillBoost1=SpecializationAttribute.STRIP;

				}
				else if(specialization == SpecializationType.TRAINER){
					students.add(character);
					statBoost=BaseAttributeTypes.OBEDIENCE;


				}

			}
		}
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, 0.08f, BaseAttributeTypes.COMMAND));
		modifications.add(new ModificationData(TargetType.ALL, 0.08f, SpecializationAttribute.EXPERIENCE));
		modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
		if(students!=null){
			for(Charakter character : students){
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f, EssentialAttributes.MOTIVATION));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.02f, statBoost));
				if(skillBoost1!=null)
					modifications.add(new ModificationData(TargetType.SINGLE, character, 0.5f, skillBoost1));
				if(skillBoost2!=null)
					modifications.add(new ModificationData(TargetType.SINGLE, character, 0.5f, skillBoost2));
				if(sexBoost1!=null)
					modifications.add(new ModificationData(TargetType.SINGLE, character, 0.8f, sexBoost1));
				if(sexBoost2!=null)
					modifications.add(new ModificationData(TargetType.SINGLE, character, 0.8f, sexBoost2));
			}
		}


		return modifications;
	}

	@Override
	public MessageData getBaseMessage() {
		Charakter character = getCharacters().get(0);
		ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, character);;
		String message =  TextUtil.t("govern.basic1", character, character.getBackground());
		if(students.size()!=0 && specialization!=SpecializationType.TRAINER)
			message +=" "+  TextUtil.t("govern.specific", students);
		else if(specialization==SpecializationType.TRAINER)
			message =TextUtil.t("govern.basic2", character, character.getBackground());
		else
			message =TextUtil.t("govern.none", character, character.getBackground());
		return new MessageData(message, image, null);
	}

	@Override
	public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {

			return new ArrayList<SelectionData<?>>(getSuperviseOptions(plannedActivity.getCharacters().get(0)));
		
	}



	public List<SelectionData<SpecializationType>> getSuperviseOptions(Charakter character) {
		List<SelectionData<SpecializationType>> options = new ArrayList<SelectionData<SpecializationType>>();
		for (SpecializationType specialization : character.getSpecializations()) {

			if (!specialization.isTeachable() || !Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations().contains(specialization)) {

				continue;

			}
			if(specialization==SpecializationType.WHORE ||
					specialization==SpecializationType.BARTENDER ||
					specialization==SpecializationType.TRAINER ||
					specialization==SpecializationType.DANCER ||
					specialization==SpecializationType.KINKYSEX ||
					specialization==SpecializationType.NURSE){
				SelectionData<SpecializationType> option = new SelectionData<SpecializationType>();
				option.setSelectionObject(specialization);
				switch(specialization){
				case TRAINER:
					option.setButtonText(TextUtil.t("supervise.option.all"));
					option.setShortText(specialization.getText());
					break;
				case WHORE:
					option.setButtonText(TextUtil.t("supervise.option.whore"));
					option.setShortText(specialization.getText());
					break;				
				case BARTENDER:
					option.setButtonText(TextUtil.t("supervise.option.bartender"));
					option.setShortText(specialization.getText());
					break;
				case DANCER:
					option.setButtonText(TextUtil.t("supervise.option.dancer"));
					option.setShortText(specialization.getText());
					break;
				case KINKYSEX:
					option.setButtonText(TextUtil.t("supervise.option.kinky"));
					option.setShortText(specialization.getText());
					break;
				case NURSE:
					option.setButtonText(TextUtil.t("supervise.option.nurse"));
					option.setShortText(specialization.getText());
					break;
				default:
					break;

				}

				option.setShortText(specialization.getText());
				options.add(option);
			}
		}
		return options;
	}


}