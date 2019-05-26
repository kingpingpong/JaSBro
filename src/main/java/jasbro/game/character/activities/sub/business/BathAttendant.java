package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.housing.Room;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Activity class for serving customers who take a bath in the spa.
 */
public class BathAttendant extends RunningActivity implements BusinessSecondaryActivity  {
	private enum Action {
		NORMAL, 
		SWIMSUIT, NAKED, OIL,
		LAZY, RELAX, 
		CLEAN, COCKTAILS, 
		MASSAGE, SOAP, INCENCE, 
	}
	private Map<Charakter, Action> characterAction=new HashMap<Charakter, Action>();
	private MessageData messageData;
	@Override
	public int getAppeal() {
		int appeal = Util.getInt(0, 8);
		for (Charakter character : getCharacters()) {
			appeal += (character.getCharisma() + character.getFinalValue(SpecializationAttribute.SEDUCTION) / 4 )/ 6;
		}
		return appeal;
	}
	@Override
	public int getMaxAttendees() {
		return 15+getCharacters().size()*5;
	}

	@Override
	public void init() {
		List<Charakter> characters = new ArrayList<Charakter>(getCharacters());


		for(Charakter character : characters){
			List<Action> actions = new ArrayList<Action>();
			actions.add(Action.NORMAL);
			actions.add(Action.LAZY);
			actions.add(Action.RELAX);
			if(character.getFinalValue(SpecializationAttribute.STRIP)>60
					|| character.getTraits().contains(Trait.EXHIBITIONIST)
					|| character.getTraits().contains(Trait.NICEBODY)
					|| character.getTraits().contains(Trait.UNINHIBITED)){
				actions.add(Action.NAKED);
				actions.add(Action.SWIMSUIT);
			}
			if(character.getFinalValue(SpecializationAttribute.CLEANING)>60){
				actions.add(Action.CLEAN);
			}
			if(character.getFinalValue(SpecializationAttribute.BARTENDING)>60){
				actions.add(Action.COCKTAILS);
			}
			if(character.getTraits().contains(Trait.MANUALLABOR)){
				actions.add(Action.MASSAGE);
			}
			if(character.getTraits().contains(Trait.OILY)){
				actions.add(Action.OIL);
			}
			if(character.getTraits().contains(Trait.SOAPY)){
				actions.add(Action.SOAP);
			}
			if(character.getTraits().contains(Trait.CALMINGINCENCES)){
				actions.add(Action.INCENCE);
			}

			characterAction.put(character, actions.get(Util.getInt(0, actions.size())));

		}



	}

	@Override
	public void perform() {
		int skill=0; 
		int amountEarned = 0;
		int amountHappy = 0;

		int amountVaginal=0;	int amountOral=0;		int amountGroup=0;
		int amountAnal=0;		int amountForeplay=0;
		for (Charakter character: getCharacters()) {
			 amountVaginal=0;	 amountOral=0;		 amountGroup=0;
			 amountAnal=0;		 amountForeplay=0;
			amountHappy=0; amountEarned=0;
			switch(characterAction.get(character)){
			case NORMAL:
				skill+=character.getCharisma()+character.getObedience();
				break;
			case LAZY:
				skill+=character.getCharisma()+character.getObedience();
				skill/=2;
				break;
			case RELAX:
				skill+=character.getCharisma()+character.getObedience()/2+Util.getInt(0, 25);
				break;
			case SWIMSUIT:
				skill+=character.getCharisma()*2+character.getObedience()/2+character.getFinalValue(SpecializationAttribute.STRIP)/3;
				break;
			case NAKED:
				skill+=character.getCharisma()*3+character.getObedience()/2+character.getFinalValue(SpecializationAttribute.SEDUCTION)/3;
				break;
			case OIL:
				skill+=character.getCharisma()*4+character.getObedience()/2+character.getStamina()/3;
				break;
			case INCENCE:
				skill+=character.getCharisma()+character.getObedience()/2+character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE)/2;
				break;
			case COCKTAILS:
				skill+=character.getCharisma()+character.getObedience()/2+character.getFinalValue(SpecializationAttribute.BARTENDING)/2;
				break;
			case CLEAN:
				skill+=character.getCharisma()+character.getObedience()/2+character.getFinalValue(SpecializationAttribute.CLEANING)/2;
				break;
			case SOAP:
				skill+=character.getCharisma()+character.getObedience()/2+character.getFinalValue(SpecializationAttribute.WELLNESS);
				break;
			case MASSAGE:
				skill+=character.getCharisma()+character.getObedience()/2+character.getFinalValue(SpecializationAttribute.WELLNESS);
				break;

			}
			for (Customer customer : getCustomers()) {
				int tips = (this.getHouse().getValue());
				for(Room room : this.getHouse().getRooms()){
					tips+=room.getRoomType().getCost();
				}
				tips/=10000;
				tips = customer.payFixed(tips);
				amountEarned +=  tips;
				if (Util.getInt(0, 50) + skill + customer.getSatisfactionAmount() > 50) {
					amountHappy++;
					customer.addToSatisfaction(skill, this);
					customer.changePayModifier(0.3f);
				}
				else {
					customer.addToSatisfaction(skill / 2, this);
				}
				if(Util.getInt(1, 2)==1 && customer.getStatus()==CustomerStatus.TIRED){customer.setStatus(CustomerStatus.LIVELY);}
				if(Util.getInt(1, 2)==1 && (customer.getStatus()==CustomerStatus.DRUNK || customer.getStatus()==CustomerStatus.VERYDRUNK)){customer.setStatus(CustomerStatus.TIRED);}

				//private service
				int obed=10;
				if(character.getType()==CharacterType.SLAVE){obed+=character.getObedience();}
				else{obed+=character.getCommand();}
				if(obed>50){obed=50;}
				if((obed/3 > Util.getInt(0, 100) || (obed/2>Util.getInt(0, 100) && character.getTraits().contains(Trait.SEXPERT)))){//customer gets private service
					switch(customer.getPreferredSextype()){
					case VAGINAL:
						if(character.getTraits().contains(Trait.NAIAD)){
						amountVaginal+=1;						
						customer.addToSatisfaction(character.getFinalValue(Sextype.VAGINAL), this);
						}
					case ANAL:
						if(character.getTraits().contains(Trait.NAIAD)){
						amountAnal+=1;
						customer.addToSatisfaction(character.getFinalValue(Sextype.ANAL), this);
						}
					case ORAL:
						if(character.getTraits().contains(Trait.DEEPBREATH)){
						amountOral+=1;
						customer.addToSatisfaction(character.getFinalValue(Sextype.ORAL), this);
						}
					case TITFUCK:
						if(character.getTraits().contains(Trait.DEEPBREATH)){
						amountForeplay+=1;
						customer.addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY), this);
						}
					case FOREPLAY:
						if(character.getTraits().contains(Trait.DEEPBREATH)){
						amountForeplay+=1;
						customer.addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY), this);
						}
					default:
						if(character.getTraits().contains(Trait.NAIAD)){
						amountGroup+=1;
						customer.addToSatisfaction(character.getFinalValue(Sextype.GROUP), this);
						if(amountGroup>getCustomers().size()){amountGroup=getCustomers().size()-1;}
						}
					}
				}
			}
			modifyIncome(amountEarned);
			Object arg[] = {amountVaginal, amountAnal, amountOral, amountForeplay, amountGroup, amountVaginal+amountAnal+amountOral+amountForeplay};
			if(amountVaginal > 2 && Util.getInt(0, 3)==1){
				String message = null;
				ImageData image;
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
				if(character.getFinalValue(Sextype.VAGINAL)>=300 && Util.getInt(1, 2)==1 && amountVaginal>5){
					message=TextUtil.t("attendBath.extras.vaginal3", character, arg);
				}
				else if (amountVaginal>4 && Util.getInt(1, 2)==1){
					message=TextUtil.t("attendBath.extras.vaginal2", character, arg);
				}
				else{
					message=TextUtil.t("attendBath.extras.vaginal1", character, arg);
				}
				getMessages().add(new MessageData(message, image, character.getBackground()));
				this.getAttributeModifications().add(new AttributeModification(amountVaginal*0.5f,Sextype.VAGINAL, character));
			}
			if(amountAnal > 2 && Util.getInt(0, 3)==1){
				String message = null;
				ImageData image;
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
				if(character.getFinalValue(Sextype.ANAL)>=300 && Util.getInt(1, 2)==1 && amountAnal>5){
					message=TextUtil.t("attendBath.extras.anal3", character, arg);
				}
				else if (amountAnal>4 && Util.getInt(1, 2)==1){
					message=TextUtil.t("attendBath.extras.anal2", character, arg);
				}
				else{
					message=TextUtil.t("attendBath.extras.anal1", character, arg);
				}
				getMessages().add(new MessageData(message, image, character.getBackground()));
				this.getAttributeModifications().add(new AttributeModification(amountAnal*0.5f,Sextype.ANAL, character));
			}
			if(amountOral > 2 && Util.getInt(0, 3)==1){
				String message = null;
				ImageData image;
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
				if(character.getFinalValue(Sextype.ORAL)>=300 && Util.getInt(1, 2)==1 && amountOral>5){
					message=TextUtil.t("attendBath.extras.oral2", character, arg);
				}
				else{
					message=TextUtil.t("attendBath.extras.oral1", character, arg);
				}
				getMessages().add(new MessageData(message, image, character.getBackground()));
				this.getAttributeModifications().add(new AttributeModification(amountOral*0.5f,Sextype.ORAL, character));
			}
			if(amountForeplay > 2 && Util.getInt(0, 3)==1){
				String message = null;
				ImageData image;
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, character);
				switch (Util.getInt(0, 3)){
				case 0:
					message=TextUtil.t("attendBath.extras.foreplay1", character, arg);
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, character);
					break;
				case 1:
					message=TextUtil.t("attendBath.extras.foreplay2", character, arg);
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, character);
					break;
				default:
					message=TextUtil.t("attendBath.extras.foreplay3", character, arg);
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.KISS, character);
					break;
				}
				
				getMessages().add(new MessageData(message, image, character.getBackground()));
				this.getAttributeModifications().add(new AttributeModification(amountForeplay*0.5f,Sextype.FOREPLAY, character));
			}
			if(amountGroup >= 2 && Util.getInt(0, 3)==1){
				String message = null;
				ImageData image;
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
				if(character.getFinalValue(Sextype.GROUP)>=300 && Util.getInt(1, 2)==1 && amountGroup>5){
					message=TextUtil.t("attendBath.extras.group3", character, arg);
				}
				else if (amountGroup>3 ){
					message=TextUtil.t("attendBath.extras.group2", character, arg);
				}
				else if (amountGroup==3){
					message=TextUtil.t("attendBath.extras.group11", character, arg);
				}
				else if (amountGroup==2){
					message=TextUtil.t("attendBath.extras.group1", character, arg);
				}
				if(message!=null){getMessages().add(new MessageData(message, image, character.getBackground()));}
				this.getAttributeModifications().add(new AttributeModification(amountGroup*0.5f,Sextype.GROUP, character));
			}

			messageData.addToMessage("\n");
			switch(characterAction.get(character)){
			case NORMAL:
				messageData.addToMessage(TextUtil.t("attendBath.result.normal", character));
				break;
			case LAZY:
				messageData.addToMessage(TextUtil.t("attendBath.result.lazy", character));			
				break;
			case RELAX:
				messageData.addToMessage(TextUtil.t("attendBath.result.relax", character));			
				break;
			case SWIMSUIT:
				messageData.addToMessage(TextUtil.t("attendBath.result.swimsuit", character));				
				break;
			case NAKED:
				messageData.addToMessage(TextUtil.t("attendBath.result.naked", character));			
				break;
			case OIL:
				messageData.addToMessage(TextUtil.t("attendBath.result.oil", character));			
				break;
			case INCENCE:
				messageData.addToMessage(TextUtil.t("attendBath.result.incence", character));			
				break;
			case COCKTAILS:
				messageData.addToMessage(TextUtil.t("attendBath.result.cocktails", character));			
				break;
			case CLEAN:
				messageData.addToMessage(TextUtil.t("attendBath.result.clean", character));			
				break;
			case SOAP:
				messageData.addToMessage(TextUtil.t("attendBath.result.soap", character));			
				break;
			case MASSAGE:
				messageData.addToMessage(TextUtil.t("attendBath.result.massage", character));			
				break;
			}
		}


	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		for (Charakter character : getCharacters()) {
			switch(characterAction.get(character)){
			case NORMAL:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -20, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				break;
			case LAZY:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -6, EssentialAttributes.ENERGY));			
				break;
			case RELAX:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -6, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 5, EssentialAttributes.HEALTH));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				break;
			case SWIMSUIT:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));			
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.1f, SpecializationAttribute.STRIP));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.CHARISMA));
				break;
			case NAKED:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));		
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.1f, SpecializationAttribute.STRIP));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.CHARISMA));
				break;
			case OIL:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));		
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.1f, SpecializationAttribute.STRIP));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.CHARISMA));
				break;
			case INCENCE:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -10, EssentialAttributes.ENERGY));		
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.1f, SpecializationAttribute.PLANTKNOWLEDGE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.INTELLIGENCE));
				break;
			case COCKTAILS:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.1f, SpecializationAttribute.BARTENDING));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				break;
			case CLEAN:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.1f, SpecializationAttribute.CLEANING));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				break;
			case SOAP:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -20, EssentialAttributes.ENERGY));		
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.1f, SpecializationAttribute.WELLNESS));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.08f, BaseAttributeTypes.OBEDIENCE));
				break;
			case MASSAGE:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -25, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.1f, SpecializationAttribute.WELLNESS));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				break;

			}

		}
		modifications.add(new ModificationData(TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
		return modifications;
	}


	@Override
	public MessageData getBaseMessage() {
		int numCustomers = getCustomers().size();
		String messageText;

		if (numCustomers>0) {
			int tips = (this.getHouse().getValue());
			for(Room room : this.getHouse().getRooms()){
				tips+=room.getRoomType().getCost();
			}
			tips/=10000;
			tips*=getCustomers().size();
			messageText=TextUtil.t("attendBath.basic", new Object[]{TextUtil.listCharacters(getCharacters()), numCustomers, tips});
		}
		else {
			messageText=TextUtil.t("attendBath.nobody", new Object[]{TextUtil.listCharacters(getCharacters()), numCustomers});
		}
		this.messageData = new MessageData(messageText, null, getBackground());
		for (Charakter character : getCharacters()) {
			List<ImageTag> tags=new ArrayList<ImageTag>();

			tags.add(ImageTag.BATHE);
			switch(characterAction.get(character)){
			case NORMAL:
				tags.add(ImageTag.STANDARD);
				break;
			case LAZY:
				tags.add(ImageTag.SLEEP);
				break;
			case RELAX:
				tags.add(ImageTag.SUNBATHE);
				break;
			case SWIMSUIT:
				tags.add(ImageTag.SWIMSUIT);	
				break;
			case NAKED:
				tags.add(ImageTag.NAKED);	
				break;
			case OIL:
				tags.add(ImageTag.DANCE);
				break;
			case INCENCE:
				tags.add(ImageTag.STANDARD);
				break;
			case COCKTAILS:
				tags.add(ImageTag.BARTEND);
				break;
			case CLEAN:
				tags.add(ImageTag.CLEAN);
				break;
			case SOAP:
				tags.add(ImageTag.STANDARD);		
				break;
			case MASSAGE:
				tags.add(ImageTag.NURSE);		
				break;

			}

			this.messageData.addImage(ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()));

		}


		return this.messageData;
	}

	/**
	 * Check whether the given character enters the pool.
	 */

}
