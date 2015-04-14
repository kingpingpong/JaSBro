package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.housing.House;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Publicize extends RunningActivity{

	private MessageData message;
    private Charakter character;
    private int effectiveness = 1;

    @Override
    public void init() {
        character = getCharacter();
    }
	@Override
	public MessageData getBaseMessage() {
        if (message == null){
            List<ImageTag> tags = character.getBaseTags();
            tags.add(1, ImageTag.CLEANED);	                      
            Object arguments[] = {getCharacterLocation().getName()};
            List<String> actions = new ArrayList<String>();

			if(character.getSpecializations().contains(SpecializationType.CATGIRL) && character.getTraits().contains(Trait.CATCHY) &&
					character.getFinalValue(SpecializationAttribute.CATGIRL) > 30)
			{
				actions.add("publicize.catgirl");
			}
			if(character.getSpecializations().contains(SpecializationType.DANCER) && character.getTraits().contains(Trait.SHOWINGTHEGOODS) && character.getFinalValue(SpecializationAttribute.STRIP) > 40)
			{
				int randomstrip = Util.getInt(0, 1);
				switch(randomstrip){
				case 0:
					actions.add("publicize.dance1");
					break;
				case 1:
					actions.add("publicize.dance2");	
					break;
				}

			}
			if(character.getTraits().contains(Trait.SHOWOFF) &&	character.getFinalValue(SpecializationAttribute.VETERAN) > 20)
			{
				actions.add("publicize.fighter");
			}
			if(character.getType() == CharacterType.SLAVE && character.getTraits().contains(Trait.SAMPLINGTHEGOODS) &&
					character.getFinalValue(SpecializationAttribute.SEDUCTION) > 15)
			{
				actions.add("publicize.whore");
			}
			if(character.getTraits().contains(Trait.HEYGUYSBOOSE) && character.getFinalValue(SpecializationAttribute.BARTENDING) > 10)
			{
				actions.add("publicize.bartend");
			}
			if(character.getFinalValue(SpecializationAttribute.CLEANING) > 150)
			{
				actions.add("publicize.maid");
			}
			if(character.getFinalValue(Sextype.BONDAGE) > 150)
			{
				actions.add("publicize.bondage");
			}
			if(character.getFinalValue(SpecializationAttribute.ADVERTISING) >= 150)
			{
				int randomstrip=Util.getInt(0, 3);				
				switch(randomstrip){
				case 0:
					actions.add("publicize.basic1");
					break;
				case 1:
					actions.add("publicize.basic2");	
					break;
				case 2:
					actions.add("publicize.basic3");	
					break;	
				case 3:
					actions.add("publicize.basic4");	
					break;						
				}
			}
			if(!character.getSpecializations().contains(SpecializationType.MARKETINGEXPERT) || character.getFinalValue(SpecializationAttribute.ADVERTISING) < 150)
			{
				actions.add("publicize.unskilled");
			}

			int choice = (int) (Math.random() * actions.size());

			if(actions.get(choice) == "publicize.catgirl"){
				tags.add(0, ImageTag.CATGIRL);		
	            tags.add(1, ImageTag.CLEANED);	
			}
			if(actions.get(choice) == "publicize.dance1"){
				tags.add(0, ImageTag.DANCE);
	            tags.add(1, ImageTag.CLEANED);	
			}
			if(actions.get(choice) == "publicize.dance2"){
				tags.add(0, ImageTag.DANCE);
	            tags.add(1, ImageTag.CLEANED);	
			}			
			if(actions.get(choice) == "publicize.fighter"){
				tags.add(0, ImageTag.FIGHT);	
	            tags.add(1, ImageTag.CLEANED);	
			}
			if(actions.get(choice) == "publicize.whore"){
				tags.add(0, ImageTag.BLOWJOB);	
	            tags.add(1, ImageTag.CLEANED);	
			}
			if(actions.get(choice) == "publicize.bartend"){
				tags.add(0, ImageTag.BARTEND);
	            tags.add(1, ImageTag.CLEANED);	
			}
			if(actions.get(choice) == "publicize.maid"){
				tags.add(0, ImageTag.MAID);	
	            tags.add(1, ImageTag.CLEANED);	
			}
			if(actions.get(choice) == "publicize.bondage"){
				tags.add(0, ImageTag.BONDAGE);	
	            tags.add(1, ImageTag.CLEANED);	
			}
			if(actions.get(choice) == "publicize.basic1"){
				tags.add(0, ImageTag.CLOTHED);
	            tags.add(1, ImageTag.CLEANED);	
			}
			if(actions.get(choice) == "publicize.basic2"){
				tags.add(0, ImageTag.CLOTHED);
	            tags.add(1, ImageTag.CLEANED);	
			}
			if(actions.get(choice) == "publicize.basic3"){
				tags.add(0, ImageTag.CLOTHED);
	            tags.add(1, ImageTag.CLEANED);	
			}	
			if(actions.get(choice) == "publicize.basic4"){
				tags.add(0, ImageTag.CLOTHED);	
	            tags.add(1, ImageTag.CLEANED);	
			}			
			if(actions.get(choice) == "publicize.unskilled"){
				tags.add(0, ImageTag.CLOTHED);
	            tags.add(1, ImageTag.CLEANED);	
			}

			message = new MessageData(TextUtil.t( actions.get(choice), character, arguments), ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()), getCharacterLocation().getImage());
        }
        return message;
	}

    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        if (character.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)) {
            modifications.add(new ModificationData(TargetType.ALL, 1.1f, SpecializationAttribute.ADVERTISING));
        }
        else {
            modifications.add(new ModificationData(TargetType.ALL, 0.1f, SpecializationAttribute.ADVERTISING));
        }
        modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
        modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.CHARISMA));

        return modifications;
    }

	public void perform() {

        long skill = effectiveness;

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

		skill *= 10;
        skill += character.getCharisma();
        skill += character.getFinalValue(SpecializationAttribute.ADVERTISING);
        skill += character.getFinalValue(SpecializationAttribute.STRIP) >> 1;
        skill += character.getFinalValue(SpecializationAttribute.SEDUCTION) >> 2;
        skill += character.getFinalValue(SpecializationAttribute.CATGIRL) >> 2;

        List<House> houses = Jasbro.getInstance().getData().getHouses();
        long increaseFameBy = (skill << 2) / houses.size();
        for (House house : houses) {
            house.getFame().modifyFame(increaseFameBy);
        }
        character.getFame().modifyFame(skill);
	}
}
