package jasbro.game.character.activities.sub;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.MessageData;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class Sex extends RunningActivity {
	private Sextype sexType;
	private Charakter character1;
	private Charakter character2;
	
    @Override
    public void init() {
        
        if (getCharacters().get(0).getGender() == Gender.MALE || 
        		getCharacters().get(0).getGender() == getCharacters().get(1).getGender() ||
        		(getCharacters().get(0).getGender() == Gender.FUTA && 
        		getCharacters().get(1).getGender() != Gender.MALE)  ) {
        	character1 = getCharacters().get(0);
        	character2 = getCharacters().get(1);
        }
        else {
        	character1 = getCharacters().get(1);
        	character2 = getCharacters().get(0);
        }
        
        if (getPlannedActivity().getSelectedOption() != null) {
            sexType = (Sextype) getPlannedActivity().getSelectedOption().getSelectionObject();
        }
        else {
            List<SelectionData<Sextype>> options = getSextypeOptions(character1, character2);

            List<ImageTag> tags1 = character1.getBaseTags();
            List<ImageTag> tags2 = character2.getBaseTags();
            tags1.add(0, ImageTag.NAKED);
            tags1.add(1, ImageTag.CLEANED);
            tags2.add(0, ImageTag.NAKED);
            tags2.add(1, ImageTag.CLEANED);
            
            SelectionData<Sextype> selectedOption = new SelectionScreen<Sextype>().select(options, 
                    ImageUtil.getInstance().getImageDataByTags(tags1, character1.getImages()),
                    ImageUtil.getInstance().getImageDataByTags(tags2, character2.getImages()),
                    character1.getBackground(), TextUtil.t("sex.option.description", character1, character2));
            sexType = selectedOption.getSelectionObject();   
        }   
    }
    
    @Override
    public MessageData getBaseMessage() {
        setMinimumObedience(sexType.getObedienceRequired());
        MessageData messageData = new MessageData();
        
        Future<MessageData> future = Jasbro.getThreadpool().submit(new ImageUtil.BestImageSelection(sexType, character1, character2));
        messageData.addFuture(future);
        return messageData;
    }
    
    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        
        modifications.add(new ModificationData(TargetType.ALL, 4f, sexType));
        
        if (sexType != Sextype.BONDAGE) {
            modifications.add(new ModificationData(TargetType.ALL, 0.01f, BaseAttributeTypes.OBEDIENCE));
            modifications.add(new ModificationData(TargetType.ALL, 0.03f, BaseAttributeTypes.STAMINA));
        }
        else {
        	modifications.add(new ModificationData(TargetType.ALL, 0.03f, BaseAttributeTypes.OBEDIENCE));
            modifications.add(new ModificationData(TargetType.ALL, 0.01f, BaseAttributeTypes.STAMINA));
        }
        modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
        return modifications;
    }
    
    @Override
    public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
        if (plannedActivity.getCharacters().size() < 2) {
            return null;
        }
        else {
            return new ArrayList<SelectionData<?>>(
                    getSextypeOptions(plannedActivity.getCharacters().get(0), plannedActivity.getCharacters().get(1)));
        }
    }
    
    public List<SelectionData<Sextype>> getSextypeOptions(Charakter character1, Charakter character2) {
        List<Sextype> possibleSextypes = Sextype.getPossibleSextypes(character1.getGender(), character2.getGender());
        List<SelectionData<Sextype>> options = new ArrayList<SelectionData<Sextype>>();
        for (Sextype curSexType : possibleSextypes) {
            SelectionData<Sextype> option = new SelectionData<Sextype>();
            option.setSelectionObject(curSexType);
            option.setButtonText(TextUtil.t("sex.option."+curSexType.toString(), character1, character2));
            option.setShortText(curSexType.getText());
            options.add(option);
        }
        return options;
    }

    @Override
    public Sextype getSextype() {
        return sexType;
    }
    
    
}
