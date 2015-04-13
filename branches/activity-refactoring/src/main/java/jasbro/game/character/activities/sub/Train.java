package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Train extends RunningActivity {
	private Charakter teacher;
	private List<Charakter> students = new ArrayList<Charakter>();
	private SpecializationType specialization;
	
	@Override
	public void init() {
        List<SelectionData<SpecializationType>> options = getTrainOptions(getCharacters());
		if (getPlannedActivity().getSelectedOption() == null) {
	        String arguments[] = {TextUtil.listCharacters(students)};
	        SelectionData<SpecializationType> selectedOption = new SelectionScreen<SpecializationType>().select(options, 
	                ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, teacher), null, 
	                teacher.getBackground(), TextUtil.t("train.option.description", teacher, arguments));
	        specialization = selectedOption.getSelectionObject();
		}
		else {
		    specialization = (SpecializationType) getPlannedActivity().getSelectedOption().getSelectionObject();
		}		
	}
	
	@Override
    public MessageData getBaseMessage() {
		String arguments[] = {TextUtil.listCharacters(students)};
    	String message = TextUtil.t("train.basic", teacher, arguments);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TEACH, teacher);
        MessageData messageData = new MessageData(message, image, getCharacter().getBackground());
        
        for (Charakter character : students) {
            List<ImageTag> tags = character.getBaseTags();
            tags.add(0, ImageTag.STUDY);
            image = ImageUtil.getInstance().getImageDataByTags(tags, character.getImages());
            messageData.addImage(image);
        }
        return messageData;
    }
    
    @Override
    public List<ModificationData> getStatModifications() {
    	List<ModificationData> modifications = new ArrayList<ModificationData>();
		if (specialization != null) {
			modifications.add(new ModificationData(TargetType.SLAVE, 0.2f, BaseAttributeTypes.OBEDIENCE));
			modifications.add(new ModificationData(TargetType.TRAINER, 0.2f, BaseAttributeTypes.COMMAND));
			modifications.add(new ModificationData(TargetType.ALL, 0.05f, BaseAttributeTypes.INTELLIGENCE));
			modifications.add(new ModificationData(TargetType.ALL, -25f, EssentialAttributes.ENERGY));
			
			for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
                float modTeacher;
                if (attributeType instanceof BaseAttributeTypes) {
                    modTeacher = 0.005f;
                }
                else {
                    modTeacher = 0.05f;
                }
                modifications.add(new ModificationData(TargetType.SINGLE, teacher, modTeacher, attributeType));
            }
			
			float modifier = 1 - (0.1f * (students.size() - 1));
			for (Charakter student : students) {
    			for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
    			    float baseMod;
    				if (attributeType instanceof BaseAttributeTypes) {
    				    baseMod = 0.08f;
    				}
    				else {
    				    baseMod = 0.8f;
    				}
    				AttributeType attributeType2 = attributeType;
    				if (attributeType == BaseAttributeTypes.COMMAND && student.getType() == CharacterType.SLAVE) {
    					attributeType2 = BaseAttributeTypes.OBEDIENCE;
    				}
    				
    				int diff = teacher.getFinalValue(attributeType) - ((int)student.getAttribute(attributeType2).getInternValue());
    				if (diff > 0) {
    				    baseMod += diff / 25.0f;
    				}
                    baseMod += teacher.getFinalValue(attributeType) / 500f;

                    float expMod = 0;
    				expMod += baseMod * ((teacher.getIntelligence() + student.getIntelligence() - 12) / 2) / 100f;
    				if (teacher.getType() == CharacterType.TRAINER && student.getType() == CharacterType.SLAVE) {
    					expMod += baseMod * ((teacher.getCommand() + student.getObedience() - 12) / 2) / 100f;
    				}
    				modifications.add(new ModificationData(TargetType.SINGLE, student, (baseMod + expMod) * modifier, attributeType2));
    			}
			}
		}
    	return modifications;
    }
    
    
    @Override
    public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
        if (plannedActivity.getCharacters().size() < 2) {
            return null;
        }
        else {
            return new ArrayList<SelectionData<?>>(
                    getTrainOptions(plannedActivity.getCharacters()));
        }
    }
    
    public List<SelectionData<SpecializationType>> getTrainOptions(List<Charakter> characters) {
        for (Charakter character : characters) {
            if (teacher == null) {
                teacher = character;
            }
            else if ((character.getType() == CharacterType.TRAINER && teacher.getType() != CharacterType.TRAINER) || (
                    character.getType() == teacher.getType() &&
                    (character.getFinalValue(BaseAttributeTypes.COMMAND) > teacher.getFinalValue(BaseAttributeTypes.COMMAND) ||
                    (character.getFinalValue(BaseAttributeTypes.COMMAND) == teacher.getFinalValue(BaseAttributeTypes.COMMAND) &&
                    character.getFinalValue(BaseAttributeTypes.INTELLIGENCE) > teacher.getFinalValue(BaseAttributeTypes.INTELLIGENCE))))) {
                students.add(teacher);
                teacher = character;
            }
            else {
                students.add(character);
            }
        }
        
        List<SelectionData<SpecializationType>> options = new ArrayList<SelectionData<SpecializationType>>();
        for (SpecializationType specialization : teacher.getSpecializations()) {
            if (specialization.isLocked()) { //As long as a specialization is locked it can not be taught
                if (!specialization.isTeachable() || !Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations().contains(specialization)) {
                    continue;
                }
            }
            
            SelectionData<SpecializationType> option = new SelectionData<SpecializationType>();
            option.setSelectionObject(specialization);
            if (specialization == SpecializationType.TRAINER || specialization == SpecializationType.SLAVE) {
                option.setButtonText(TextUtil.t("train.option." + specialization.toString(), students));
            }
            else {
                String arguments[] = {TextUtil.listCharacters(students), specialization.getText()};
                option.setButtonText(TextUtil.t("train.option", arguments));
            }
            option.setShortText(specialization.getText());
            options.add(option);
        }
        return options;
    }	
}
