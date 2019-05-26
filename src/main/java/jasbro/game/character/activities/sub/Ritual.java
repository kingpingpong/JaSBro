package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.List;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.rooms.Crypt;
import jasbro.game.housing.Room;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Ritual extends RunningActivity {

	private final static float OBEDIENCEMODIFICATION = 0.01f;
	private MessageData messageData;
	private String demon ="";

	public String getDemon() {
		return demon;
	}

	public void setDemon(String demon) {
		this.demon = demon;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();


		modifications.add(new ModificationData(TargetType.SLAVE, OBEDIENCEMODIFICATION, BaseAttributeTypes.OBEDIENCE));
		modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 0.5f, SpecializationAttribute.MAGIC));

		return modifications;
	}

	@Override
	public void init() {

	}

	@Override
	public void perform() {
		Crypt room = (Crypt) getCharacterLocation();

		setDemon(room.getDemonName());
		if(room.getDemonName()=="nobody" || room.getRitualAdvancment()==0){
			List<SelectionData<Integer>> options = new ArrayList<SelectionData<Integer>>();
			options.add(new SelectionData<Integer>(0, TextUtil.t("crypt.selectdemon.communication")));
			options.add(new SelectionData<Integer>(1, TextUtil.t("crypt.selectdemon.debauchery")));
			options.add(new SelectionData<Integer>(2, TextUtil.t("crypt.selectdemon.trade")));
			options.add(new SelectionData<Integer>(3, TextUtil.t("crypt.selectdemon.war")));
			options.add(new SelectionData<Integer>(4, TextUtil.t("crypt.selectdemon.deception")));
			options.add(new SelectionData<Integer>(5, TextUtil.t("crypt.selectdemon.parties")));
			options.add(new SelectionData<Integer>(6, TextUtil.t("crypt.selectdemon.arcanes")));

			SelectionData<Integer> selectedOption = new SelectionScreen<Integer>()
					.select(options, ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter()), null,
							getCharacters().get(0).getBackground(), TextUtil.t("crypt.selectdemon", getCharacters().get(0)));
			Integer selected = selectedOption.getSelectionObject();

			if(selected==0){room.setDemonName("communication");}
			if(selected==1){room.setDemonName("debauchery");}
			if(selected==2){room.setDemonName("trade");}
			if(selected==3){room.setDemonName("war");}
			if(selected==4){room.setDemonName("deception");}
			if(selected==5){room.setDemonName("parties");}
			if(selected==6){room.setDemonName("arcanes");}
		}
		room.setRitualAdvancment(room.getRitualAdvancment()+1);
		if(getCharacter().getTraits().contains(Trait.DARKRITUAL))
			room.setRitualAdvancment(room.getRitualAdvancment()+1);

		if(room.getRitualAdvancment()>=13){
			

			if(Util.getInt(0, 100)<66){
				Object args[]={room.getDemonName()};
				messageData.addToMessage("\n"+TextUtil.t("ritual.success",getCharacter(), args));
				switch(room.getDemonName()){
				case "communication":
					messageData.addToMessage("\n"+TextUtil.t("ritual.communication",getCharacter()));
					for(Charakter character : Jasbro.getInstance().getData().getCharacters()){
						character.getAttribute(SpecializationAttribute.BARTENDING).addToValue(1.0f, true);
						character.getAttribute(SpecializationAttribute.ADVERTISING).addToValue(1.0f, true);
						character.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(1.0f, true);
						character.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(1.0f, true);
					}
					break;
				case "debauchery":
					messageData.addToMessage("\n"+TextUtil.t("ritual.debauchery",getCharacter()));
					for(Charakter character : Jasbro.getInstance().getData().getCharacters()){
						character.getAttribute(SpecializationAttribute.SEDUCTION).addToValue(1.0f, true);
						character.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(1.0f, true);
						character.getAttribute(BaseAttributeTypes.STAMINA).addToValue(1.0f, true);
						character.getAttribute(Sextype.VAGINAL).addToValue(1.0f, true);
						character.getAttribute(Sextype.ANAL).addToValue(1.0f, true);
						character.getAttribute(Sextype.ORAL).addToValue(1.0f, true);
						character.getAttribute(Sextype.TITFUCK).addToValue(1.0f, true);
						character.getAttribute(Sextype.FOREPLAY).addToValue(1.0f, true);
						character.getAttribute(Sextype.GROUP).addToValue(1.0f, true);
						character.addCondition(new Buff.HornyBuff(character));
					}
					break;
				case "trade":
					int amount=0;
					for(Charakter character : Jasbro.getInstance().getData().getCharacters()){
						amount+=character.getCharisma()+character.getObedience()+character.getCommand()+character.getStamina()+character.getIntelligence()+character.getStrength();

					}
					amount*=Util.getInt(10, 15);
					Object arg[]={amount};
					messageData.addToMessage("\n"+TextUtil.t("ritual.trade",getCharacter(), arg));
					Jasbro.getInstance().getData().earnMoney(amount, this);
					break;
				case "war":
					messageData.addToMessage("\n"+TextUtil.t("ritual.war",getCharacter()));
					for(Charakter character : Jasbro.getInstance().getData().getCharacters()){
						character.getAttribute(SpecializationAttribute.VETERAN).addToValue(1.0f, true);
						character.getAttribute(BaseAttributeTypes.STRENGTH).addToValue(1.0f, true);
					}
					break;
				case "deception":
					messageData.addToMessage("\n"+TextUtil.t("ritual.deception",getCharacter()));
					for(Charakter character : Jasbro.getInstance().getData().getCharacters()){
						character.getAttribute(SpecializationAttribute.PICKPOCKETING).addToValue(1.0f, true);
						character.getAttribute(SpecializationAttribute.AGILITY).addToValue(1.0f, true);
						character.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(1.0f, true);
						character.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(1.0f, true);
					}
					break;
				case "parties":
					messageData.addToMessage("\n"+TextUtil.t("ritual.parties",getCharacter()));
					for(Charakter character : Jasbro.getInstance().getData().getCharacters()){
						character.getAttribute(SpecializationAttribute.STRIP).addToValue(1.0f, true);
						character.getAttribute(SpecializationAttribute.BARTENDING).addToValue(1.0f, true);
						character.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(1.0f, true);
					}
					break;
				case "arcanes":
					messageData.addToMessage("\n"+TextUtil.t("ritual.arcanes",getCharacter()));
					for(Charakter character : Jasbro.getInstance().getData().getCharacters()){
						character.getAttribute(SpecializationAttribute.MEDICALKNOWLEDGE).addToValue(1.0f, true);
						character.getAttribute(SpecializationAttribute.MAGIC).addToValue(1.0f, true);
						character.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(1.0f, true);
					}
					break;
				}
			}
			else{//rape \o/
				if(!getCharacter().getTraits().contains(Trait.PERSONNALOFFERING))
					messageData.addToMessage("\n"+TextUtil.t("ritual.failure.lose",getCharacter()));
				else
					messageData.addToMessage("\n"+TextUtil.t("ritual.failure.win",getCharacter()));
				messageData.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.MONSTER, getCharacter()));
				List<Charakter> potentialVictims = new ArrayList<Charakter>();
				for(Room room2:this.getHouse().getRooms()){
					for(Charakter character2: room2.getCurrentUsage().getCharacters()){
						potentialVictims.add(character2);
					}
				}

				Charakter character = potentialVictims.get(Util.getInt(0, potentialVictims.size()));
				if(character.getName()!=getCharacter().getName() && Util.getInt(0, 100)<70 && !getCharacter().getTraits().contains(Trait.PERSONNALOFFERING)){
					messageData.addToMessage("\n"+TextUtil.t("ritual.failure.othervictim",character, getCharacter()));
					messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.MONSTER, character));
					this.getAttributeModifications().add(new AttributeModification(1.07f,Sextype.MONSTER, character));
					this.getAttributeModifications().add(new AttributeModification(-41.07f,EssentialAttributes.ENERGY, character));
					this.getAttributeModifications().add(new AttributeModification(-11.07f,EssentialAttributes.HEALTH, character));
				}

				if(!getCharacter().getTraits().contains(Trait.PERSONNALOFFERING)){
					this.getAttributeModifications().add(new AttributeModification(1.07f,Sextype.MONSTER, getCharacter()));				
					this.getAttributeModifications().add(new AttributeModification(-41.07f,EssentialAttributes.ENERGY, getCharacter()));
					this.getAttributeModifications().add(new AttributeModification(-11.07f,EssentialAttributes.HEALTH, getCharacter()));}
				else{

					this.getAttributeModifications().add(new AttributeModification(3.07f,Sextype.MONSTER, getCharacter()));				
					this.getAttributeModifications().add(new AttributeModification(-21.07f,EssentialAttributes.ENERGY, getCharacter()));
					getCharacter().addCondition(new Buff.DarkGodSeed());
				}
			}
			room.setRitualAdvancment(0);
		}
		else{
			messageData.addToMessage("\n"+TextUtil.t("ritual.advancment",getCharacter()));
		}
	}

	@Override
	public MessageData getBaseMessage() {
		Crypt room = (Crypt) getCharacterLocation();
		Charakter character = getCharacters().get(0);
		Object argument[]={room.getDemonName()};
		String message = TextUtil.t("ritual.basic", character, argument);
		this.messageData = new MessageData(message,null, getBackground());
		messageData.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter()));


		return this.messageData;
	}


}