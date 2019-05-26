package jasbro.game.character.activities.sub.whore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.housing.House;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;
import jasbro.util.ConfigHandler;

public class Whore extends RunningActivity implements BusinessMainActivity {
	private final static Logger log = LogManager.getLogger(Whore.class);

	private House house;
	private MessageData messageData = new MessageData();
	private Sextype sexType;
	private float amountActions = 0f;
	private float energyMultiplier = 1f;
	private int executionTime=10;
	private int cooldownTime=0;
	private float executionModifier=0f;
	private float cooldownModifier=0f;

	@Override
	public void init() {
		house = getHouse();
		sexType = getMainCustomer().getPreferredSextype();
		Charakter whore = getCharacters().get(0);
		if(sexType==Sextype.GROUP){
			CustomerGroup group=(CustomerGroup)getMainCustomer();
			energyMultiplier=group.getCustomers().size()/1.3f;}
		if(getMainCustomer().getStatus()==CustomerStatus.HORNYSTATUS){
			setExecutionModifier(getExecutionModifier()+0.6f);
			setCooldownModifier(getCooldownModifier()+0.3f);
			energyMultiplier*=1.7f;}
		if(getMainCustomer().getStatus()==CustomerStatus.VERYHORNY){
			setExecutionModifier(getExecutionModifier()+0.8f);
			setCooldownModifier(getCooldownModifier()+0.4f);
			energyMultiplier*=2.4f;}
		if(getMainCustomer().getStatus()==CustomerStatus.STRONGSTATUS){
			setExecutionModifier(getExecutionModifier()+0.1f);
			setCooldownModifier(getCooldownModifier()+0.3f);
			energyMultiplier*=1.3f;}
		if(getMainCustomer().getStatus()==CustomerStatus.LIVELY){
			setExecutionModifier(getExecutionModifier()+0.2f);
			setCooldownModifier(getCooldownModifier()+0.2f);
			energyMultiplier*=1.1f;}
		if(getMainCustomer().getStatus()==CustomerStatus.TIRED){
			setExecutionModifier(getExecutionModifier()-0.2f);
			setCooldownModifier(getCooldownModifier()-0.2f);
			energyMultiplier*=0.7f;}
		if(getMainCustomer().getStatus()==CustomerStatus.SHYSTATUS){
			setExecutionModifier(getExecutionModifier()+Util.getInt(-1, 1)*0.1f);
			setCooldownModifier(getCooldownModifier()+0.0f);
			energyMultiplier*=0.8f;}





		String locationName;
		if (house != null) {
			locationName = house.getName();
		}
		else {
			locationName = getCharacterLocation().getName();
		}
		String message;
		if (house == null || house.getInternName() == null || house.getInternName().trim().equals("")) {
			message = TextUtil.t("whore.basic1", whore, getMainCustomer(), getMainCustomer().getStatusName(), getMainCustomer().getName(), locationName) + " ";
		}
		else {
			message = TextUtil.t("whore.basic2", whore, getMainCustomer(), getMainCustomer().getStatusName(), getMainCustomer().getName(), locationName) + " ";
		}

		message += TextUtil.t("whore.service", whore, getMainCustomer()); 

		if((getMainCustomer().getStatus()==CustomerStatus.DRUNK && Util.getInt(1, 15)==1) || (getMainCustomer().getStatus()==CustomerStatus.DRUNK && Util.getInt(1, 10)==1)){
			message+="\n" + TextUtil.t("whore.drunkcustomer");
			energyMultiplier=0.1f;
			setExecutionModifier(getExecutionModifier()-0.7f);
			setCooldownModifier(getCooldownModifier()-0.7f);
		}
		message += "\n";

		CharacterLocation characterLocation = whore.getActivity().getSource();

		message += checkPossible(this, whore);

		messageData = new MessageData(message, null, characterLocation.getImage());

		if (sexType != null) {
			//Calculate satisfaction
			int satisfaction = 5 + whore.getFinalValue(sexType) / 10;
			satisfaction += whore.getFinalValue(BaseAttributeTypes.OBEDIENCE)/10;
			satisfaction += whore.getFinalValue(BaseAttributeTypes.CHARISMA)/10;
			satisfaction += whore.getFinalValue(SpecializationAttribute.SEDUCTION) / 10;
			if (getHouse() != null) {
				satisfaction += getHouse().getCleanState().getSatisfactionModifier();
			}
			if(getMainCustomer().getStatus()==CustomerStatus.PISSED || getMainCustomer().getStatus()==CustomerStatus.SAD){
				satisfaction/=2;
			}

			getMainCustomer().addToSatisfaction(satisfaction, this);
			setMinimumObedience(sexType.getObedienceRequired());
		}
	}

	public String checkPossible(RunningActivity activity, Charakter whore) {
		String message = "";

		List<Sextype> possibleSextypes = getPossibleSextypes();
		boolean obedienceTooLow = whore.getRealMinObedience(sexType.getObedienceRequired(), activity) > whore.getObedience();
		boolean isSextypeNotAllowed = !whore.getAllowedServices().isAllowed(sexType);
		boolean notPossible = !possibleSextypes.contains(getMainCustomer().getPreferredSextype());
		if (obedienceTooLow || isSextypeNotAllowed || notPossible) {

			int minReqObdience = Integer.MAX_VALUE;
			for (Sextype sextype : possibleSextypes) {
				int obedienceRequired = whore.getRealMinObedience(sextype.getObedienceRequired(), activity);
				if (obedienceRequired < minReqObdience) {
					minReqObdience = obedienceRequired;
				}
			}

			if (!isSextypeNotAllowed && whore.getRealMinObedience(sexType.getObedienceRequired(), activity) == minReqObdience) {
				return ""; //even though obedience is too low, this is the sextype with lowest obedience requirement. Use this, but action might be aborted later
			}
			if (possibleSextypes.size() == 0) {
				log.error("No possible sextypes {} {}", getCharacter().getName(), getMainCustomer().getName());
				this.sexType = null;
				return "";
			}

			if (obedienceTooLow) {
				getMainCustomer().addToSatisfaction(-10 + (whore.getObedience() - whore.getRealMinObedience(sexType.getObedienceRequired(), activity)) * 10, 
						activity); //Make customer pissed off
				if(getMainCustomer().getStatus()==CustomerStatus.PISSED || getMainCustomer().getStatus()==CustomerStatus.HORNYSTATUS){
					getMainCustomer().addToSatisfaction( -100, activity);
				}
			}
			else if (isSextypeNotAllowed) {
				getMainCustomer().addToSatisfaction( -40, activity); //Make customer pissed off
				if(getMainCustomer().getStatus()==CustomerStatus.PISSED || getMainCustomer().getStatus()==CustomerStatus.HORNYSTATUS){
					getMainCustomer().addToSatisfaction( -100, activity);
				}
			}
			else { //Unable to do sextype
				getMainCustomer().addToSatisfaction( -15, activity);
			}

			message += TextUtil.t("whore.sextype."+sexType.toString(), whore, getMainCustomer());
			if (obedienceTooLow || isSextypeNotAllowed) {
				message += TextUtil.t("whore.refuse", whore, getMainCustomer());
			}
			else {
				message += TextUtil.t("whore.notpossible", whore, getMainCustomer());
			}

			do {
				sexType = possibleSextypes.get(Util.getInt(0, possibleSextypes.size()));
			}
			while (whore.getRealMinObedience(sexType.getObedienceRequired(), activity) > whore.getObedience()
					&& whore.getRealMinObedience(sexType.getObedienceRequired(), activity) != minReqObdience);

			if (sexType != null) {
				message += " " + TextUtil.t("whore.replacement."+sexType.toString(), whore, getMainCustomer());
			}
			else {
				getMainCustomer().addToSatisfaction(-100, activity); //Make customer completely pissed off
				message += " " + TextUtil.t("whore.noreplacement", whore, getMainCustomer());
			}
		}
		else {
			message += TextUtil.t("whore.sextype."+sexType.toString(), whore, getMainCustomer()) + ".";
		}
		switch(sexType){
		case VAGINAL:
			setExecutionTime(25);
			setCooldownTime(25);
			break;
		case ANAL:
			setExecutionTime(20);
			setCooldownTime(30);
			break;
		case ORAL:
			setExecutionTime(25);
			setCooldownTime(15);
			break;
		case FOREPLAY:
			setExecutionTime(30);
			setCooldownTime(10);
			break;
		case TITFUCK:
			setExecutionTime(25);
			setCooldownTime(15);
			break;
		case BONDAGE:
			setExecutionTime(30);
			setCooldownTime(50);
			break;
		case GROUP:
			CustomerGroup group=(CustomerGroup)getMainCustomer();
			setExecutionTime(20 + 5 * group.getCustomers().size());
			setCooldownTime(20 + 5 * group.getCustomers().size());
			break;
		default:
			break;
		}

		return message;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modificationData  = new ArrayList<ModificationData>();

		if (sexType != null) {

			modificationData.add(new ModificationData(TargetType.ALL, energyMultiplier*0.5f, sexType));
			modificationData.add(new ModificationData(TargetType.ALL, 0.01f, BaseAttributeTypes.STAMINA));
			modificationData.add(new ModificationData(TargetType.ALL, energyMultiplier*0.2f, SpecializationAttribute.SEDUCTION));
			if(getCharacter().getTraits().contains(Trait.NYMPHO) ||
					getCharacter().getTraits().contains(Trait.SEXADDICT) ||
					getCharacter().getTraits().contains(Trait.BEDROOMPRINCESS))
				modificationData.add(new ModificationData(TargetType.ALL, -0.08f*energyMultiplier, EssentialAttributes.MOTIVATION));
			else
				modificationData.add(new ModificationData(TargetType.ALL, -0.1f*energyMultiplier, EssentialAttributes.MOTIVATION));
			float obedienceModifier = 0.01f;
			switch (sexType) {
			case GROUP:
				modificationData.add(new ModificationData(TargetType.ALL, -7f*energyMultiplier, EssentialAttributes.ENERGY));
				modificationData.add(new ModificationData(TargetType.ALL, -1, EssentialAttributes.HEALTH));
				break;
			case MONSTER:
				modificationData.add(new ModificationData(TargetType.ALL, -35, EssentialAttributes.ENERGY));
				modificationData.add(new ModificationData(TargetType.ALL, -15, EssentialAttributes.HEALTH));
				modificationData.add(new ModificationData(TargetType.ALL, -5.0f, EssentialAttributes.MOTIVATION));
				break;
			case BONDAGE:
				modificationData.add(new ModificationData(TargetType.ALL, -15, EssentialAttributes.ENERGY));
				modificationData.add(new ModificationData(TargetType.ALL, -5, EssentialAttributes.HEALTH));
				modificationData.add(new ModificationData(TargetType.ALL, -0.3f*energyMultiplier, EssentialAttributes.MOTIVATION));
				obedienceModifier = 0.05f;
				break;
			default:
				modificationData.add(new ModificationData(TargetType.ALL, -6*energyMultiplier, EssentialAttributes.ENERGY));
			}
			modificationData.add(new ModificationData(TargetType.SLAVE, obedienceModifier, BaseAttributeTypes.OBEDIENCE));
			if(!(getCharacter().getTraits().contains(Trait.LEGACYWHORE))){
				modificationData.add(new ModificationData(TargetType.TRAINER, -0.2f, BaseAttributeTypes.COMMAND));
			}	
		}
		return modificationData;
	}

	@Override
	public MessageData getBaseMessage() {
		List<ImageTag> tags = getCharacter().getBaseTags();

		if (sexType != null) {
			tags.add(0, sexType.getAssociatedImageTag());
			tags.addAll(ImageTag.getAssociatedImageTags(getCharacter(), getMainCustomer()));
		} else {
			tags.add(0, ImageTag.STANDARD);
		}

		if (tags.contains(ImageTag.FUTA)) {
			tags.add(0, ImageTag.FUTA);
		}
		if (tags.contains(ImageTag.LESBIAN)) {
			tags.add(0, ImageTag.LESBIAN);
		}

		final ImageData image = ImageUtil.getInstance().getImageDataByTags(tags, getCharacter().getImages());
		messageData.setImage(image);


		if (sexType != null) {
			messageData.addFuture(Jasbro.getThreadpool().submit(new Callable<MessageData>() {
				@Override
				public MessageData call() throws Exception {
					int	chance = 5; //Util.getInt(1, 100);
					if(chance <= 4){
						ImageTag specificTag = ImageTag.getSpecificTag(sexType.getAssociatedImageTag(), image.getTags());
						String message = null;
						if (image.getTags().contains(ImageTag.DOMINANTPOSITION)) {
							message = "\n" + TextUtil.t("whore.sextype2.dominant." + specificTag, getCharacter(), getMainCustomer(), false);
						}
						if (image.getTags().contains(ImageTag.SELF)) {
							message = "\n" + TextUtil.t("whore.sextype2.self." + specificTag, getCharacter(), getMainCustomer(), false);
						}
						else if (image.getTags().contains(ImageTag.SUBMISSIVEPOSITION)) {
							message = "\n" + TextUtil.t("whore.sextype2.submissive." + specificTag, getCharacter(), getMainCustomer(), false);
						}                        
						if (message == null) {
							message = "\n" + TextUtil.t("whore.sextype2." + specificTag, getCharacter(), getMainCustomer(), false);
						}

						if (message != null && !message.startsWith("whore.sextype2.")) {
							return new MessageData(message, null, null, null);
						}
						else {
							return null;
						}						
					}
					else {
						ImageTag specificTag = ImageTag.getSpecificTag(sexType.getAssociatedImageTag(), image.getTags());
						String message = null;
						if (image.getTags().contains(ImageTag.DOMINANTPOSITION)) {
							message = "\n" + TextUtil.t("sex.specific.dominant." + specificTag, getCharacter(), getMainCustomer(), false);
						}
						if (image.getTags().contains(ImageTag.SELF)) {
							message = "\n" + TextUtil.t("sex.specific.self." + specificTag, getCharacter(), getMainCustomer(), false);
						}
						else if (image.getTags().contains(ImageTag.SUBMISSIVEPOSITION)) {
							message = "\n" + TextUtil.t("sex.specific.submissive." + specificTag, getCharacter(), getMainCustomer(), false);
						}    
						if (!image.getTags().contains(ImageTag.SUBMISSIVEPOSITION) || !image.getTags().contains(ImageTag.SELF) || !image.getTags().contains(ImageTag.DOMINANTPOSITION)) {
							message = "\n" + TextUtil.t("sex.specific.general." + specificTag, getCharacter(), getMainCustomer(), false);
						}
						if (message == null) {
							return null;
						}

						if (message != null && !message.startsWith("sex.sextype.")) {
							return new MessageData(message, null, null, null);
						}
						else {
							return null;
						}
					}
				}
			}));
		}
		return messageData;

	}

	@Override
	public void perform() {






		//calculate payment
		if (sexType != null) {
			int payment=0;
			if(sexType==Sextype.GROUP){
				CustomerGroup group=(CustomerGroup)getMainCustomer();
				for(Customer cust : group.getCustomers()){
					payment += 1 + cust.pay(getMainCustomer().getMoney()*getMainCustomer().getSatisfactionAmount()/200/(group.getCustomers().size()-1), getCharacter().getMoneyModifier());
				}
			}
			else{
				payment += 1 + getMainCustomer().pay(getMainCustomer().getMoney()*getMainCustomer().getSatisfactionAmount()/200, getCharacter().getMoneyModifier());

			}
			if(getMainCustomer().getStatus()==CustomerStatus.HORNYSTATUS)
				payment += 1 + payment;
			if(getMainCustomer().getStatus()==CustomerStatus.VERYHORNY){
				payment += 1 + payment;
				payment += 1 + payment;			}
			minPayment(payment);
			modifyIncome(payment);
		}

		String end;
		if (getMainCustomer().getPreferredSextype() == sexType) {
			end = "end";
		}
		else {
			end = "end2";
		}


		Object arguments[] = {getMainCustomer().getSatisfaction().getText(), getIncome()};
		messageData.addToMessage("\n\n" + TextUtil.t("whore." + end, getCharacter(), getMainCustomer(), arguments));
		


		if (sexType != null && getHouse() != null) {
			House house = getHouse();
			house.modDirt(1);
		}

		float mult=1f;
		mult=1 - (getCharacters().get(0).getFinalValue(sexType)-20)/100f+getExecutionModifier();
		if(mult<=0)
			mult=0.01f;
		setExecutionTime((int) (getExecutionTime() * mult));
		if(getExecutionTime()<5)
			setExecutionTime(5);
		mult=1 - (getCharacters().get(0).getFinalValue(BaseAttributeTypes.STAMINA)-20)/100f+getCooldownModifier();
		if(mult<=0)
			mult=0.01f;
		setCooldownTime((int) (getCooldownTime() * mult));
		setAmountActions(getExecutionTime()+getCooldownTime());

		if(!getCharacter().getTraits().contains(Trait.ONENIGHT)){
			if(getCooldownTime()<5){
				if(Util.getInt(0, 100)>50)
					messageData.addToMessage("\n" + TextUtil.t("whore.break.veryshort.one", getCharacter()));
				else
					messageData.addToMessage("\n" + TextUtil.t("whore.break.veryshort.two", getCharacter()));
			}
			else if(getCooldownTime()<15){
				if(Util.getInt(0, 100)>50)
					messageData.addToMessage("\n" + TextUtil.t("whore.break.short.one", getCharacter()));
				else
					messageData.addToMessage("\n" + TextUtil.t("whore.break.short.two", getCharacter()));
			}
			else if(getCooldownTime()<30){
				if(Util.getInt(0, 100)>50)
					messageData.addToMessage("\n" + TextUtil.t("whore.break.normal.one", getCharacter()));
				else
					messageData.addToMessage("\n" + TextUtil.t("whore.break.normal.two", getCharacter()));
			}
			else if(getCooldownTime()<60){
				if(Util.getInt(0, 100)>50)
					messageData.addToMessage("\n" + TextUtil.t("whore.break.long.one", getCharacter()));
				else
					messageData.addToMessage("\n" + TextUtil.t("whore.break.long.two", getCharacter()));
			}
			else{
				if(Util.getInt(0, 100)>50)
					messageData.addToMessage("\n" + TextUtil.t("whore.break.verylong.one", getCharacter()));
				else
					messageData.addToMessage("\n" + TextUtil.t("whore.break.verylong.two", getCharacter()));
			}

			if(ConfigHandler.isShowTimeTaken()){
				Object detail[]={getExecutionTime(), getCooldownTime()};
				messageData.addToMessage("\n" + TextUtil.t("whore.timetaken", getCharacter(), detail));
			}

		}



	}

	@Override
	public int rateCustomer(Customer customer) {
		Charakter character = getCharacter();

		if (customer.getGender() == Gender.MALE && character.getAllowedServices().isServiceMales() == false) {
			return 0;
		}
		else if (customer.getGender() == Gender.FEMALE && character.getAllowedServices().isServiceFemales() == false) {
			return 0;
		}
		else if (customer.getGender() == Gender.FUTA && character.getAllowedServices().isServiceFutas() == false) {
			return 0;
		}

		List<Sextype> sexTypes = getPossibleSextypes(customer);
		if (sexTypes.size() == 0) {
			return 0;
		}

		if (customer.getPreferredSextype().getObedienceRequired() > character.getObedience()) {
			int rating = 4 + (character.getObedience() - customer.getPreferredSextype().getObedienceRequired());
			if (rating < 1) {
				rating = 1;
			}
			return rating;
		}

		if (!sexTypes.contains(customer.getPreferredSextype())) {
			return 4;
		}

		int value = (int) (1 + customer.getImportance() + character.getObedience() + character.getCharisma() + 
				character.getFinalValue(SpecializationAttribute.SEDUCTION) / 5 );
		return value;
	}

	public List<Sextype> getPossibleSextypes(Customer customer) {
		List<Sextype> sextypes = Sextype.getPossibleSextypes(customer, getCharacter());
		if (getHouse() == null) {
			sextypes.remove(Sextype.BONDAGE);
		}
		return sextypes;
	}

	private List<Sextype> getPossibleSextypes() {
		List<Sextype> sextypes = getPossibleSextypes(getMainCustomer());
		return sextypes;
	}

	public Float getAmountActions() {
		return amountActions;
	}

	public Integer minPayment(int payment) {
		if(payment <= 6){
			payment = 7;
		}
		return payment;
	}

	public void setAmountActions(float amountActions) {
		this.amountActions = amountActions;
	}

	public Sextype getSexType() {
		return sexType;
	}

	public void setSexType(Sextype sexType) {
		this.sexType = sexType;
	}

	public MessageData getMessageData() {
		return messageData;
	}

	public int getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}

	public int getCooldownTime() {
		return cooldownTime;
	}

	public void setCooldownTime(int cooldownTime) {
		this.cooldownTime = cooldownTime;
	}
	public float getExecutionModifier() {
		return executionModifier;
	}

	public void setExecutionModifier(float f) {
		this.executionModifier = f;
	}

	public float getCooldownModifier() {
		return cooldownModifier;
	}

	public void setCooldownModifier(float f) {
		this.cooldownModifier = f;
	}
}