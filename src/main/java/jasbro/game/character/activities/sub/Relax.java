package jasbro.game.character.activities.sub;

import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Relax extends RunningActivity {
	private int actionType=0;
	private List<String> actions = new ArrayList<String>();
	
    @Override
    public void init() {
    	
    	actions.add("relax.nap");
    	actions.add("relax.snack");
    	actions.add("relax.sing");
		if(getCharacter().getTraits().contains(Trait.NYMPHO) && getCharacter().getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString())==0)
		{
			actions.add("relax.masturbate");
		}
		if(getCharacter().getIntelligence()>20)
		{
			actions.add("relax.book");
		}
		if(getCharacter().getCharisma()>20)
		{
			actions.add("relax.beauty");
		}
		if(getCharacter().getFinalValue(SpecializationAttribute.STRIP)>50)
		{
			actions.add("relax.dance");
		}
		if(getCharacter().getFinalValue(SpecializationAttribute.CATGIRL)>50)
		{
			actions.add("relax.cat");
		}
		if(getCharacter().getFinalValue(SpecializationAttribute.CLEANING)>50)
		{
			actions.add("relax.clean");
		}
		
		int choice = (int) (Math.random() * actions.size());
		
		if(actions.get(choice)=="relax.nap"){
			setactionType(1);
		}
		if(actions.get(choice)=="relax.snack"){
			setactionType(2);
		}
		if(actions.get(choice)=="relax.sing"){
			setactionType(3);
		}
		if(actions.get(choice)=="relax.masturbate"){
			setactionType(4);
		}
		if(actions.get(choice)=="relax.book"){
			setactionType(5);
		}
		if(actions.get(choice)=="relax.beauty"){
			setactionType(6);
		}
		if(actions.get(choice)=="relax.dance"){
			setactionType(7);
		}
		if(actions.get(choice)=="relax.cat"){
			setactionType(8);
		}
		if(actions.get(choice)=="relax.clean"){
			setactionType(9);
		}

    }
	
    @Override
    public MessageData getBaseMessage() {
    	Charakter character = getCharacters().get(0);
    	String message="";
    	message = TextUtil.t("relax.basic", character);
    	

        ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);

		
		switch(getactionType()){
		case 1:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, character);
			message+=("\n\n" + TextUtil.t( "relax.nap",character));
			break;
		case 2:
			message+=("\n\n" + TextUtil.t( "relax.snack",character));
			break;
		case 3:
			message+=("\n\n" + TextUtil.t( "relax.sing",character));
			break;
		case 4:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character);
			message+=("\n\n" + TextUtil.t( "relax.masturbate",character));
			break;
		case 5:
			message+=("\n\n" + TextUtil.t( "relax.book",character));
			break;
		case 6:
			message+=("\n\n" + TextUtil.t( "relax.beauty",character));
			break;
		case 7:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
			message+=("\n\n" + TextUtil.t( "relax.dance",character));
			break;
		case 8:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CATGIRL, character);
			message+=("\n\n" + TextUtil.t( "relax.cat",character));
			break;
		case 9:
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MAID, character);
			message+=("\n\n" + TextUtil.t( "relax.clean",character));
			break;

		}
		
    	
        
        return new MessageData(message, image, getCharacter().getBackground());
    }
    
    
    @Override
    public List<ModificationData> getStatModifications() {
    	List<ModificationData> modifications = new ArrayList<ModificationData>();
        modifications.add(new ModificationData(TargetType.ALL, 40, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.HEALTH));
        switch (getactionType()){
        case 1:
        	modifications.add(new ModificationData(TargetType.ALL, 10, EssentialAttributes.ENERGY));
            modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.HEALTH));
        	break;
        case 2:        	
        	modifications.add(new ModificationData(TargetType.ALL, 5, EssentialAttributes.HEALTH));
        	break;
        case 3:
        	modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
        	break;
        case 4:
        	modifications.add(new ModificationData(TargetType.ALL, 0.9f, Sextype.FOREPLAY));
            break;
        case 5:
        	modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.INTELLIGENCE));
        	
        	break;
        case 6:
        	modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));
        	break;
        case 7:
        	modifications.add(new ModificationData(TargetType.ALL, 0.8f, SpecializationAttribute.STRIP));
        	break;
        case 8:
        	modifications.add(new ModificationData(TargetType.ALL, 0.8f, SpecializationAttribute.CATGIRL));
        	break;
        case 9:
        	modifications.add(new ModificationData(TargetType.ALL, 0.8f, SpecializationAttribute.CLEANING));
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
