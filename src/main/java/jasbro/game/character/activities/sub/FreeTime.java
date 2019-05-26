package jasbro.game.character.activities.sub;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.events.MessageData;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.housing.RoomType;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FreeTime extends RunningActivity  implements BusinessSecondaryActivity {
    
    private final static float BASEMODIFICATION = 1f;
    private final static float OBEDIENCEMODIFICATION = 0.01f;
    private float dirtModification = -1;
    private int appeal = 0;
    private int maxCust = 0;
    
	private MessageData messageData;
	
	private enum Occupation {
		REST,
		CLEAN,
		COOK,
		WHORE,
		DANCE,
		BATHATTENDANT,
		BATHE,
		MASTURBATE,
		TRAIN,
		ERRAND,
		BEACH
		
	}

	private Map<Charakter, Occupation> characterOccupationMap=new HashMap<Charakter, Occupation>();
    

    
    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        float modification = BASEMODIFICATION;
        
        modifications.add(new ModificationData(TargetType.ALL, modification, SpecializationAttribute.CLEANING));
        modifications.add(new ModificationData(TargetType.SLAVE, OBEDIENCEMODIFICATION, BaseAttributeTypes.OBEDIENCE));
        modifications.add(new ModificationData(TargetType.TRAINER, -0.05f, BaseAttributeTypes.COMMAND));
        modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
        return modifications;
    }
    
    @Override
    public void init() {
    	//Go through the house, list available slots
    	House house = ((Room)getCharacterLocation()).getHouse();
    	boolean freekitchen=false;
    	boolean freestage=false;
    	boolean freearena=false;
    	boolean freebath=false;
    	int chance=0;

    	for(Room room: house.getRooms()){
    		if(room.getAmountPeople()==0){
    			if(room.getRoomType()==RoomType.STAGE){freestage=true;}
    			if(room.getRoomType()==RoomType.KITCHEN){freekitchen=true;}
    			if(room.getRoomType()==RoomType.ARENA){freearena=true;}
    			if(room.getRoomType()==RoomType.SPAAREA){freebath=true;}
    		}
    	}
    	
    	//List what the character can do
		final Map<Occupation, Integer> characterCanDo=new HashMap<Occupation, Integer>();
		
		Charakter character = getCharacters().get(0);
		
		
		for (Occupation attendType: Occupation.values()) {
			characterCanDo.put(attendType, 0); 
		}
		
		if(character.getFinalValue(EssentialAttributes.ENERGY)<=80){
			chance=Util.getInt(1,100);
			 
			characterCanDo.put(Occupation.REST, chance);
		}
		else{
			characterCanDo.put(Occupation.REST, 0);
		}
		if(character.getFinalValue(SpecializationAttribute.CLEANING)>=1 && house.getDirt()>30){
			chance=Util.getInt(1,100);
			 
			characterCanDo.put(Occupation.CLEAN, chance);
		}
		else{
			characterCanDo.put(Occupation.CLEAN, 0);
		}
		if(character.getFinalValue(SpecializationAttribute.SEDUCTION)>=1){
			chance=Util.getInt(1,100);
			 
			characterCanDo.put(Occupation.WHORE, chance);
			appeal=character.getFinalValue(SpecializationAttribute.SEDUCTION)/10;
			maxCust=1;
		}
		else{
			characterCanDo.put(Occupation.WHORE, 0);
		}
		if(character.getFinalValue(SpecializationAttribute.STRIP)>=1 && freestage==true){
			chance=Util.getInt(1,100);
			 
			characterCanDo.put(Occupation.DANCE, chance);
			appeal=character.getFinalValue(SpecializationAttribute.STRIP)/10;
			maxCust=15;
		}
		else{
			characterCanDo.put(Occupation.DANCE, 0);
		}
		if(character.getFinalValue(SpecializationAttribute.COOKING)>=1 && freekitchen==true){		
			chance=Util.getInt(1,100);
			 
			characterCanDo.put(Occupation.COOK, chance);
		}
		else{
			characterCanDo.put(Occupation.COOK, 0);
		}
		/*
		if(character.getFinalValue(SpecializationAttribute.WELLNESS)>=1 && freebath==true){ // TODO Figure out Wellness C-L
			chance=Util.getInt(1,100);
			 
			characterCanDo.put(Occupation.BATHATTENDANT, chance);
			appeal=character.getFinalValue(SpecializationAttribute.WELLNESS)/10;
			maxCust=15;
		}
		else{
			characterCanDo.put(Occupation.BATHATTENDANT, 0);
		}*/
		if(freebath==true){
			chance=Util.getInt(1,100);
			 
			characterCanDo.put(Occupation.BATHE, chance);
		}
		else{
			characterCanDo.put(Occupation.BATHE, 0);
		}
		if(character.getFinalValue(Sextype.FOREPLAY)>50 && character.getFinalValue(EssentialAttributes.ENERGY)>80){
			chance=Util.getInt(1,100);
			 
			characterCanDo.put(Occupation.MASTURBATE, chance);
		}
		else{
			characterCanDo.put(Occupation.MASTURBATE, 0);
		}
		if(character.getFinalValue(SpecializationAttribute.VETERAN)>=1 && freearena==true){
			chance=Util.getInt(1,100);
			 
			characterCanDo.put(Occupation.TRAIN, chance);
		}
		else{
			characterCanDo.put(Occupation.TRAIN, 0);
		}
		if(character.getFinalValue(EssentialAttributes.ENERGY)>80){
			chance=Util.getInt(1,100);
			characterCanDo.put(Occupation.ERRAND, chance);
			chance=Util.getInt(1,100);
			characterCanDo.put(Occupation.BEACH, chance);
		}
		else{
			characterCanDo.put(Occupation.ERRAND, 0);
			characterCanDo.put(Occupation.BEACH, 0);
		}

		int previous=0;
		int actual=0;
		for (Occupation occupation: Occupation.values()) {
			actual=characterCanDo.get(occupation);
			if(actual>previous && actual!=0){
				previous=actual;
				characterOccupationMap.put(character, occupation);
			}

		}
    }

    @Override
    public void perform() {
    	House house = ((Room)getCharacterLocation()).getHouse();
    	house.modDirt((int) -100);
    	Charakter character = getCharacters().get(0);
    	int numCustomers = getCustomers().size();
    	//Messages
    		switch (characterOccupationMap.get(character)) {
    		case REST:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.rest", character));
    			break;
    		case CLEAN:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.clean", character));
    			break;
    		case COOK:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.cook", character));
    			break;
    		case WHORE:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.whore", character));
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.whoreresult",  new Object[] {numCustomers}));
    			break;
    		case DANCE:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.dance", character));
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.danceresult",  new Object[] {numCustomers}));
    			break;
    		case BATHE:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.bathe", character));
    			break;
    		case BEACH:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.beach", character));
    			break;
    		case ERRAND:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.errand", character));
    			break;
    		case MASTURBATE:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.masturbate", character));
    			break;
    		case BATHATTENDANT:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.bathattendant", character));
    			break;
    		case TRAIN:
    			messageData.addToMessage("\n\n" + TextUtil.t("FREETIME.train", character));
    			break;
    			default:
    				messageData.addToMessage("\n\n" + TextUtil.t("fuck", character));
    				break;
    				
    		}
    	
    }
    
    @Override
    public MessageData getBaseMessage() {
        Charakter character = getCharacters().get(0);
        String message = TextUtil.t("FREETIME.basic", character);

        //Tags
        this.messageData = new MessageData(message, null, getBackground());
        List<ImageTag> tags=new ArrayList<ImageTag>();
    		switch (characterOccupationMap.get(character)) {
    		case REST:
    			tags.add(ImageTag.SLEEP);
    			break;
    		case CLEAN:
    			tags.add(ImageTag.CLEAN);
    			break;
    		case COOK:
    			tags.add(ImageTag.COOK);
    			break;
    		case WHORE:
    			tags.add(ImageTag.NAKED);
    			break;
    		case DANCE:
    			tags.add(ImageTag.DANCE);
    			break;
    		case BATHE:
    			tags.add(ImageTag.BATHE);
    			break;
    		case BEACH:
    			tags.add(ImageTag.SWIMSUIT);
    			break;
    		case ERRAND:
    			tags.add(ImageTag.CLOTHED);
    			break;
    		case MASTURBATE:
    			tags.add(ImageTag.MASTURBATION);
    			break;
    		case BATHATTENDANT:
    			tags.add(ImageTag.BATHE);
    			break;
    		case TRAIN:
    			tags.add(ImageTag.FIGHT);
    			break;
    		}
    		this.messageData.addImage(ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()));
        return this.messageData;
    }

    
    
    public float getDirtModification() {
        return dirtModification;
    }

    public void setDirtModification(float dirtModification) {
        this.dirtModification = dirtModification;
    }

    @Override
	public int getAppeal() {
		return appeal;
	}

	@Override
	public int getMaxAttendees() {
		return maxCust;
	}
}
