package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.specialization.SpecializationAttribute;
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

public class Study extends RunningActivity {

	private SpecializationType specialization;
	
	@Override
	public void init() {
		
		List<SelectionData<SpecializationType>> options = new ArrayList<SelectionData<SpecializationType>>();
		for (SpecializationType specialization : Jasbro.getInstance().getData().getUnlocks().getAvailableSpecializations()) {
			if (specialization != SpecializationType.TRAINER && specialization != SpecializationType.SLAVE &&
			        specialization != SpecializationType.UNDERAGE && specialization.isTeachable()			        
					) {
				SelectionData<SpecializationType> option = new SelectionData<SpecializationType>();
				option.setSelectionObject(specialization);				
				
				String type;
				if (specialization == SpecializationType.SEX || specialization == SpecializationType.KINKYSEX) {
					type = "sex";
				}
				else {
					type = "specialization";
				}
				if (isBasicTraining(specialization, getCharacter())) {
					Object arguments[] = { specialization.getText() };
					option.setButtonText(TextUtil.t("study.option."+ type +".free", getCharacter(), arguments));
				} else {
					int price = 1000;
					if (isAdvancedTraining(specialization, getCharacter())) {
						price = 10000;
					}
					Object arguments[] = { specialization.getText(), price};
					option.setButtonText(TextUtil.t("study.option."+ type +".costs", getCharacter(), arguments));
					if (!Jasbro.getInstance().getData().canAfford(price)) {
						option.setEnabled(false);
					}
				}

				options.add(option);
			}
		}
		SelectionData<SpecializationType> option = new SelectionData<SpecializationType>();
		option.setButtonText(TextUtil.t("ui.cancel"));
		options.add(option);
		
		SelectionData<SpecializationType> selectedOption = new SelectionScreen<SpecializationType>()
				.select(options,
						ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter()), null,
				getCharacter().getBackground(), TextUtil.t("study.option.description", getCharacter()));
		specialization = selectedOption.getSelectionObject();
		
		if (specialization != null) {
			if (isAdvancedTraining(specialization, getCharacter())) {
				modifyIncome(-10000);
			}
			else if (!isBasicTraining(specialization, getCharacter())) {
				modifyIncome(-1000);
			}
		}
	}
	
	@Override
    public MessageData getBaseMessage() {
		if (specialization != null) {
	    	String message = TextUtil.t("study.basic", getCharacter(), specialization.getText());
	        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STUDY, getCharacter());
	        return new MessageData(message, image, getCharacter().getBackground());
		}
		else {
			return new MessageData(TextUtil.t("idle.basic", getCharacter()), 
			        ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter()), 
			        getCharacter().getBackground(), true);
		}
    }
    
    @Override
    public List<ModificationData> getStatModifications() {
    	if (specialization != null) {
	    	List<ModificationData> modifications = new ArrayList<ModificationData>();
	    	modifications.add(new ModificationData(TargetType.ALL, -25, EssentialAttributes.ENERGY));
	    	modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.INTELLIGENCE));
			if (specialization != null) {
				float change = (10f +  specialization.getAssociatedAttributes().size() -1) / specialization.getAssociatedAttributes().size();
				for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
					modifications.add(new ModificationData(TargetType.ALL, change, attributeType));
				}
			}
	    	return modifications;
    	}
    	else {
    		return new Idle().getStatModifications();
    	}
    }
    
    private boolean isBasicTraining(SpecializationType specialization, Charakter character) {
    	if (character.getSpecializations().contains(specialization)) {
    		return false;
    	}
    	
        if (specialization == SpecializationType.DOMINATRIX) {
            if (character.getAttribute(SpecializationAttribute.DOMINATE).getInternValue() > 1f) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
                if (character.getAttribute(attributeType).getInternValue() > 1.0f) {
                    return false;
                }
            }
            return true;
        }
    }
    
    private boolean isAdvancedTraining(SpecializationType specialization, Charakter character) {
    	for (AttributeType attributeType : specialization.getAssociatedAttributes()) {
			if (character.getAttribute(attributeType).getInternValue() >= 100f) {
				return true;
			}
    	}
    	return false;
    }

}
