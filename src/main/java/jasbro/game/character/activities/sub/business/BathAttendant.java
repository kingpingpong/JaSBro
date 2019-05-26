package jasbro.game.character.activities.sub.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
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

/**
 * Activity class for serving customers who take a bath in the spa.
 */
public class BathAttendant extends RunningActivity implements BusinessSecondaryActivity  {
	private enum Action {
		NORMAL, 
		SWIMSUIT, NAKED, OIL,
		LAZY, RELAX, 
		CLEAN, COCKTAILS, MASTURBATION,
		MASSAGE, SOAP, INCENCE, 
	}
	private Map<Charakter, Action> characterAction=new HashMap<Charakter, Action>();
	private MessageData messageData;



	@Override
	public int getAppeal() {
		int appeal = Util.getInt(0, 8);
		for (Charakter character : getCharacters()) {
			appeal += (character.getCharisma() )/ 6;
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
			actions.add(Action.SOAP);
			if(character.getFinalValue(SpecializationAttribute.STRIP)>60
					|| character.getTraits().contains(Trait.EXHIBITIONIST)
					|| character.getTraits().contains(Trait.NICEBODY)
					|| character.getTraits().contains(Trait.UNINHIBITED)){
				actions.add(Action.NAKED);
				actions.add(Action.SWIMSUIT);
			}
			if(character.getTraits().contains(Trait.EXHIBITIONIST)
					|| character.getTraits().contains(Trait.SEXADDICT)
					|| character.getTraits().contains(Trait.AFLEURDEPEAU)
					|| character.getTraits().contains(Trait.HORNY)
					|| character.getTraits().contains(Trait.TEASER)
					|| character.getTraits().contains(Trait.UNINHIBITED)){
				actions.add(Action.MASTURBATION);
			}
			if(character.getFinalValue(SpecializationAttribute.CLEANING)>60){
				actions.add(Action.CLEAN);
			}
			if(character.getFinalValue(SpecializationAttribute.BARTENDING)>60){
				actions.add(Action.COCKTAILS);
			}
			if(character.getTraits().contains(Trait.TANTRIC)){
				actions.add(Action.MASSAGE);
			}
			if(character.getTraits().contains(Trait.OILY)){
				actions.add(Action.OIL);
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
		int personalPay=0;
		int amountVaginal=0;	int amountOral=0;		int amountGroup=0;
		int amountAnal=0;		int amountForeplay=0;
		for (Charakter character: getCharacters()) {
			amountVaginal=0;	 amountOral=0;		 amountGroup=0;
			amountAnal=0;		 amountForeplay=0;
			amountEarned=0;	personalPay=0;
			switch(characterAction.get(character)){
			case NORMAL:
				skill+=character.getCharisma()+character.getObedience();
				skill/=15;
				break;
			case LAZY:
				skill+=character.getCharisma()+character.getObedience();
				skill/=20;
				break;
			case RELAX:
				skill+=character.getCharisma()+character.getObedience()+Util.getInt(0, 5);
				skill/=10;
				break;
			case SWIMSUIT:
				skill+=character.getCharisma()+character.getObedience()+character.getFinalValue(SpecializationAttribute.STRIP);
				skill/=14;
				break;
			case NAKED:
				skill+=character.getCharisma()+character.getObedience()+character.getFinalValue(SpecializationAttribute.SEDUCTION);
				skill/=12;
				break;
			case OIL:
				skill+=character.getCharisma()+character.getObedience()/10+character.getStamina();
				skill/=13;
				break;
			case INCENCE:
				skill+=character.getCharisma()+character.getObedience()+character.getFinalValue(SpecializationAttribute.PLANTKNOWLEDGE);
				skill/=20;
				break;
			case COCKTAILS:
				skill+=character.getCharisma()+character.getObedience()+character.getFinalValue(SpecializationAttribute.BARTENDING);
				skill/=16;
				break;
			case CLEAN:
				skill+=character.getCharisma()+character.getObedience()+character.getFinalValue(SpecializationAttribute.CLEANING);
				skill/=20;
				break;
			case SOAP:
				skill+=character.getCharisma()+character.getObedience()+character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE);
				skill/=13;
				break;
			case MASSAGE:
				skill+=character.getCharisma()+character.getObedience()+character.getFinalValue(SpecializationAttribute.MEDICALKNOWLEDGE);
				skill/=12;
				break;
			case MASTURBATION:
				skill+=character.getCharisma()+character.getObedience()+character.getFinalValue(Sextype.FOREPLAY);
				skill/=11;
				break;

			}


			for (Customer customer : getCustomers()) {
				int tips = 0;
				for(Room room : this.getHouse().getRooms()){
					tips+=room.getRoomInfo().getCost();
				}
				tips/=2000;
				tips = customer.payFixed(Math.min(tips, customer.getMoney()));
				amountEarned +=  tips;
				if (Util.getInt(0, 5) + skill + customer.getSatisfactionAmount() > 50) {
					customer.addToSatisfaction(skill, this);
					customer.changePayModifier(0.3f);
				}
				else {
					customer.addToSatisfaction(skill / 2, this);
				}
				if(Util.getInt(1, 2)==1 && customer.getStatus()==CustomerStatus.TIRED){customer.setStatus(CustomerStatus.LIVELY);}
				if(Util.getInt(1, 2)==1 && (customer.getStatus()==CustomerStatus.DRUNK || customer.getStatus()==CustomerStatus.VERYDRUNK)){customer.setStatus(CustomerStatus.TIRED);}

				//general service
				if((characterAction.get(character)==Action.MASSAGE || 
						characterAction.get(character)==Action.SOAP || 
						characterAction.get(character)==Action.COCKTAILS || 
						characterAction.get(character)==Action.OIL || 
						characterAction.get(character)==Action.SWIMSUIT || 
						characterAction.get(character)==Action.NAKED) && Util.getInt(0, 5)==2){
					personalPay+=customer.pay(skill*Util.getInt(0, 10)/30);;
				}


				//private service
				int obed=10;
				if(character.getType()==CharacterType.SLAVE)
					obed+=character.getObedience();
				else
					obed+=character.getCommand();
				if(obed>50){obed=50;}
				if((obed/2 > Util.getInt(0, 100))){//customer gets private service
					switch(customer.getPreferredSextype()){
					case VAGINAL:
						if(character.getTraits().contains(Trait.ONSENPRINCESS)){
							amountVaginal+=1;
							customer.addToSatisfaction(character.getFinalValue(Sextype.VAGINAL)/7, this);
						}
					case ANAL:
						if(character.getTraits().contains(Trait.ONSENPRINCESS)){
							amountAnal+=1;
							customer.addToSatisfaction(character.getFinalValue(Sextype.ANAL)/7, this);
						}
					case ORAL:
						if(character.getTraits().contains(Trait.ONSENPRINCESS)){
							amountOral+=1;
							customer.addToSatisfaction(character.getFinalValue(Sextype.ORAL)/7, this);
						}
					case TITFUCK:
						if(character.getTraits().contains(Trait.ONSENPRINCESS)){
							amountForeplay+=1;
							customer.addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/7, this);
						}
					case FOREPLAY:
						if(character.getTraits().contains(Trait.ONSENPRINCESS)){
							amountForeplay+=1;
							customer.addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/7, this);
						}
					default:
						int rand=Util.getInt(0, 4);
						if(character.getTraits().contains(Trait.ONSENPRINCESS) && Util.getInt(0, 10)>5){
							amountGroup+=1;
							customer.addToSatisfaction(character.getFinalValue(Sextype.GROUP)/7, this);
							if(amountGroup>getCustomers().size()){amountGroup=getCustomers().size()-1;}
						}
						else if(character.getTraits().contains(Trait.ONSENPRINCESS) && rand==2){amountForeplay++;}
						else if(character.getTraits().contains(Trait.ONSENPRINCESS) && rand==3){amountVaginal++;}
						else if(character.getTraits().contains(Trait.ONSENPRINCESS) && rand==1){amountAnal++;}
					}
				}
			}
			modifyIncome(amountEarned);
			modifyIncome(personalPay);


			int rand=0;
			int shownAct=Util.getInt(0, 100);

			if(amountVaginal > 2 && shownAct<20){
				String message = null;
				rand=Util.getInt(0, 90);
				ImageData image;
				character.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) amountVaginal);
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
				if(rand<30){
					message=TextUtil.t("attendBath.extras.vaginal3", character);
				}
				else if (rand<60){
					message=TextUtil.t("attendBath.extras.vaginal2", character);
				}
				else{
					message=TextUtil.t("attendBath.extras.vaginal1", character);

				}
				this.getAttributeModifications().add(new AttributeModification(amountVaginal*0.05f,Sextype.VAGINAL, character));
				getMessages().add(new MessageData(message, image, character.getBackground()));
			}
			else if(amountAnal > 2 && shownAct<40){
				String message = null;
				rand=Util.getInt(0, 90);
				ImageData image;
				character.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) amountVaginal);
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
				if(rand<30){
					message=TextUtil.t("attendBath.extras.anal3", character);
				}
				else if (rand<60){
					message=TextUtil.t("attendBath.extras.anal2", character);
				}
				else{
					message=TextUtil.t("attendBath.extras.anal1", character);

				}
				this.getAttributeModifications().add(new AttributeModification(amountVaginal*0.05f,Sextype.ANAL, character));
				getMessages().add(new MessageData(message, image, character.getBackground()));
			}
			else if(amountOral > 2 && shownAct<60){
				String message = null;
				rand=Util.getInt(0, 90);
				ImageData image;
				character.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) amountVaginal);
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
				if(rand<30){
					message=TextUtil.t("attendBath.result.oral", character);
				}
				else if (rand<60){
					message=TextUtil.t("attendBath.extras.oral2", character);
				}
				else{
					message=TextUtil.t("attendBath.extras.oral1", character);

				}
				this.getAttributeModifications().add(new AttributeModification(amountVaginal*0.05f,Sextype.ORAL, character));
				getMessages().add(new MessageData(message, image, character.getBackground()));
			}
			else if(amountForeplay > 2 && shownAct<80){
				String message = null;
				rand=Util.getInt(0, 100);
				ImageData image;
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FOREPLAY, character);
				if(rand<25){
					message=TextUtil.t("attendBath.extras.foreplay3", character);
				}
				else if (rand<50){
					message=TextUtil.t("attendBath.result.join", character);
				}
				else if (rand<75){
					message=TextUtil.t("attendBath.extras.foreplay2", character);
				}
				else{
					message=TextUtil.t("attendBath.extras.foreplay1", character);

				}
				this.getAttributeModifications().add(new AttributeModification(amountVaginal*0.05f,Sextype.FOREPLAY, character));
				getMessages().add(new MessageData(message, image, character.getBackground()));
			}
			else if(amountGroup > 2 && shownAct>80){
				String message = null;
				rand=Util.getInt(0, 100);
				ImageData image;
				character.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) amountGroup);
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
				if(rand<25){
					message=TextUtil.t("attendBath.extras.group1", character);
				}
				else if (rand<50){
					message=TextUtil.t("attendBath.extras.group11", character);
				}
				else if (rand<75){
					message=TextUtil.t("attendBath.extras.group2", character);
				}
				else{
					message=TextUtil.t("attendBath.extras.group3", character);

				}
				this.getAttributeModifications().add(new AttributeModification(amountVaginal*0.05f,Sextype.GROUP, character));
				getMessages().add(new MessageData(message, image, character.getBackground()));
			}


			Object argBis[]={personalPay};
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
				if(personalPay!=0){messageData.addToMessage(TextUtil.t("attendBath.result.swimsuit.pay", character, argBis));}
				break;
			case NAKED:
				messageData.addToMessage(TextUtil.t("attendBath.result.naked", character));
				if(personalPay!=0){messageData.addToMessage(TextUtil.t("attendBath.result.naked.pay", character, argBis));}
				break;
			case OIL:
				messageData.addToMessage(TextUtil.t("attendBath.result.oil", character));
				if(personalPay!=0){messageData.addToMessage(TextUtil.t("attendBath.result.oil.pay", character, argBis));}
				break;
			case INCENCE:
				messageData.addToMessage(TextUtil.t("attendBath.result.incence", character));
				break;
			case COCKTAILS:
				messageData.addToMessage(TextUtil.t("attendBath.result.cocktails", character));
				if(personalPay!=0){messageData.addToMessage(TextUtil.t("attendBath.result.cocktails.pay", character, argBis));}
				break;
			case CLEAN:
				messageData.addToMessage(TextUtil.t("attendBath.result.clean", character));
				break;
			case SOAP:
				messageData.addToMessage(TextUtil.t("attendBath.result.soap", character));
				if(personalPay!=0){messageData.addToMessage(TextUtil.t("attendBath.result.soap.pay", character, argBis));}
				break;
			case MASSAGE:
				messageData.addToMessage(TextUtil.t("attendBath.result.massage", character));
				if(personalPay!=0){messageData.addToMessage(TextUtil.t("attendBath.result.massage.pay", character, argBis));}
				break;
			case MASTURBATION:
				messageData.addToMessage(TextUtil.t("attendBath.result.masturbation", character));
				break;
			}
		}


	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		for (Charakter character : getCharacters()) {
			modifications.add(new ModificationData(TargetType.SINGLE, character, 0.7f, SpecializationAttribute.MEDICALKNOWLEDGE));
			if(character.getTraits().contains(Trait.BENEVOLENT))
				modifications.add(new ModificationData(TargetType.ALL, 0.2f, EssentialAttributes.MOTIVATION));
			if(character.getTraits().contains(Trait.MAGICALHEALING))
				modifications.add(new ModificationData(TargetType.ALL, 0.35f, SpecializationAttribute.MAGIC));

			switch(characterAction.get(character)){
			case NORMAL:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -20, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
				break;
			case LAZY:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -6, EssentialAttributes.ENERGY));
				modifications.add(new ModificationData(TargetType.ALL, 0.4f, EssentialAttributes.MOTIVATION));
				break;
			case RELAX:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -6, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 5, EssentialAttributes.HEALTH));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.ALL, 0.3f, EssentialAttributes.MOTIVATION));
				break;
			case SWIMSUIT:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));			
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f, SpecializationAttribute.STRIP));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.CHARISMA));
				modifications.add(new ModificationData(TargetType.ALL, -0.2f, EssentialAttributes.MOTIVATION));
				break;
			case NAKED:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));		
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f, SpecializationAttribute.STRIP));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.CHARISMA));
				modifications.add(new ModificationData(TargetType.ALL, -0.2f, EssentialAttributes.MOTIVATION));
				break;
			case OIL:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));		
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f, SpecializationAttribute.STRIP));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.CHARISMA));
				modifications.add(new ModificationData(TargetType.ALL, -0.4f, EssentialAttributes.MOTIVATION));
				break;
			case INCENCE:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -10, EssentialAttributes.ENERGY));		
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f, SpecializationAttribute.PLANTKNOWLEDGE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.INTELLIGENCE));
				modifications.add(new ModificationData(TargetType.ALL, 0.2f, EssentialAttributes.MOTIVATION));
				break;
			case COCKTAILS:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f, SpecializationAttribute.BARTENDING));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
				break;
			case CLEAN:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f, SpecializationAttribute.CLEANING));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.ALL, -0.2f, EssentialAttributes.MOTIVATION));
				break;
			case SOAP:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -20, EssentialAttributes.ENERGY));		
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.1f, SpecializationAttribute.MEDICALKNOWLEDGE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.08f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.ALL, -0.4f, EssentialAttributes.MOTIVATION));
				if(character.getTraits().contains(Trait.MAGICALHEALING))
					modifications.add(new ModificationData(TargetType.ALL, 0.55f, SpecializationAttribute.MAGIC));
				break;
			case MASSAGE:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -25, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 1.1f, SpecializationAttribute.MEDICALKNOWLEDGE));
				if(character.getTraits().contains(Trait.MAGICALHEALING))
					modifications.add(new ModificationData(TargetType.ALL, 0.55f, SpecializationAttribute.MAGIC));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.ALL, -0.4f, EssentialAttributes.MOTIVATION));
				break;
			case MASTURBATION:
				modifications.add(new ModificationData(TargetType.SINGLE, character, -15, EssentialAttributes.ENERGY));	
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.1f, Sextype.FOREPLAY));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.ALL, 1.0f, EssentialAttributes.MOTIVATION));
				break;

			}
			if(character==Jasbro.getInstance().getData().getProtagonist() && !(character.getTraits().contains(Trait.LEGACYMASSEUR))){
				modifications.add(new ModificationData(TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
			}
		}

		return modifications;
	}


	@Override
	public MessageData getBaseMessage() {
		int numCustomers = getCustomers().size();
		String messageText;

		if (numCustomers>0) {
			int pay=0;
			for (Customer customer : getCustomers()) {
				int tips = 0;
				for(Room room : this.getHouse().getRooms()){
					tips+=room.getRoomInfo().getCost();
				}
				tips/=2000;
				pay += Math.min(tips, customer.getMoney());
			}
			messageText=TextUtil.t("attendBath.basic", new Object[]{TextUtil.listCharacters(getCharacters()), numCustomers, pay});
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
				tags.add(ImageTag.BATHE);
				break;
			case LAZY:
				tags.add(ImageTag.SLEEP);
				break;
			case RELAX:
				tags.add(ImageTag.BATHE);
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
				tags.add(ImageTag.BATHE);
				break;
			case COCKTAILS:
				tags.add(ImageTag.BARTEND);
				break;
			case CLEAN:
				tags.add(ImageTag.CLEAN);
				break;
			case SOAP:
				tags.add(ImageTag.BATHE);
				break;
			case MASSAGE:
				tags.add(ImageTag.NURSE);
				break;
			case MASTURBATION:
				tags.add(ImageTag.MASTURBATION);
				break;
			default:
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