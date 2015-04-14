package jasbro.game.character.activities.sub;

import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.MessageData;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Talk extends RunningActivity {

	private Charakter character1;
	private Charakter character2;
	private TalkType talkType;
	
	@Override
	public void init() {
		Charakter tmpCharacter1 = getCharacters().get(0);
		Charakter tmpCharacter2 = getCharacters().get(1);
		if ((tmpCharacter1.getType() == CharacterType.TRAINER && tmpCharacter2.getType() != CharacterType.TRAINER) || 
				(tmpCharacter1.calculateValue() > tmpCharacter2.calculateValue() && 
						!(tmpCharacter1.getType() != CharacterType.TRAINER && tmpCharacter2.getType() == CharacterType.TRAINER))) {
			this.character1 = tmpCharacter1;
			this.character2 = tmpCharacter2;
		}
		else  {
			this.character1 = tmpCharacter2;
			this.character2 = tmpCharacter1;
		}
		
		if (getPlannedActivity().getSelectedOption() == null) {
	        List<SelectionData<TalkType>> options = getTalkOptions(character1, character2);
	        
	        SelectionData<TalkType> selectedOption = new SelectionScreen<TalkType>().select(options, 
	                ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character1), null,
	                character1.getBackground(), TextUtil.t("talk.decision", character1, character2));
	        talkType = selectedOption.getSelectionObject();
		}
		else {
		    talkType = (TalkType) getPlannedActivity().getSelectedOption().getSelectionObject();
		}

	}
	
	@Override
    public MessageData getBaseMessage() {
    	String message = talkType.getTalkDescription(character1, character2);
        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character2);
        if (talkType != TalkType.PET) {
        	image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character2);
        }
        else {        	
        	image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CATGIRL, character2);
        }
        return new MessageData(message, image, getCharacter().getBackground());
    }
    
    @Override
    public List<ModificationData> getStatModifications() {
    	List<ModificationData> modifications = new ArrayList<ModificationData>();
		if (character1.getType() == CharacterType.TRAINER && character2.getType() == CharacterType.SLAVE) {
			modifications.add(new ModificationData(TargetType.SLAVE, 0.4f, BaseAttributeTypes.OBEDIENCE));
			modifications.add(new ModificationData(TargetType.TRAINER, 0.4f, BaseAttributeTypes.COMMAND));
		}
		modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 0.2f, BaseAttributeTypes.INTELLIGENCE));
		if (talkType == TalkType.PET) {
			modifications.add(new ModificationData(TargetType.SINGLE, character2, 15, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.SINGLE, character2, 1.5f, SpecializationAttribute.CATGIRL));
		}
    	return modifications;
    }
    
    @Override
    public void perform() {
        talkType.applyModifier(character1, character2);
    }
    
    public enum TalkType {
    	INTIMIDATE, MOTIVATE, PET;
    	
    	public String getText() {
    		return TextUtil.t("talk."+this.toString());
    	}
    	
    	public String getDescription(Charakter character1, Charakter character2) {
    		return TextUtil.t("talk."+this.toString()+".description", character1, character2);
    	}
    	
    	public void applyModifier(Charakter character1, Charakter character2) {
    		if (this == INTIMIDATE) {
    			character2.addCondition(new Buff.Intimidated(character1));
    		}
    		else if (this == MOTIVATE) {
    			character2.addCondition(new Buff.Motivated(character1));
    		}
    	}
    	
    	public String getTalkDescription(Charakter character1, Charakter character2) {
    		return TextUtil.t("talk."+this.toString()+".basic", character1, character2);
    	}
    }
    
    @Override
    public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
        if (plannedActivity.getCharacters().size() < 2) {
            return null;
        }
        else {
            return new ArrayList<SelectionData<?>>(
                    getTalkOptions(plannedActivity.getCharacters().get(0), plannedActivity.getCharacters().get(1)));
        }
    }
    
    public List<SelectionData<TalkType>> getTalkOptions(Charakter character1, Charakter character2) {
        if ((character1.getType() == CharacterType.TRAINER && character2.getType() != CharacterType.TRAINER) || 
                (character1.calculateValue() > character2.calculateValue() && 
                        !(character1.getType() != CharacterType.TRAINER && character2.getType() == CharacterType.TRAINER))) {
        }
        else  {
            Charakter tmpCharacter = character1;
            character1 = character2;
            character2 = tmpCharacter;
        }
        
        List<SelectionData<TalkType>> options = new ArrayList<SelectionData<TalkType>>();
        for (TalkType talk : TalkType.values()) {
            if (talk == TalkType.PET && !character2.getSpecializations().contains(SpecializationType.CATGIRL)) {
                continue;
            }
            SelectionData<TalkType> selectionData = new SelectionData<TalkType>();
            selectionData.setButtonText(talk.getText());
            selectionData.setSelectionObject(talk);
            selectionData.setTooltipText(talk.getDescription(character1, character2));
            selectionData.setShortText(talk.getText());
            options.add(selectionData);
        }
        return options;
    }

}
