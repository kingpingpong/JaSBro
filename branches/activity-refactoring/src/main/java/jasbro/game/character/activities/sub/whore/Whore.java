package jasbro.game.character.activities.sub.whore;

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
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.housing.House;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

public class Whore extends RunningActivity implements BusinessMainActivity {
	private final static Logger log = Logger.getLogger(Whore.class);
	
	private House house;
	private MessageData messageData = new MessageData();
	private Sextype sexType;
	private float amountActions = 1f;
	private float groupSize = 1f;
	
	@Override
	public void init() {	
		house = getHouse();
		sexType = getMainCustomer().getPreferredSextype();
    	Charakter whore = getCharacters().get(0);
    	if(sexType==Sextype.GROUP){
			CustomerGroup group=(CustomerGroup)getMainCustomer();
        	groupSize=group.getCustomers().size();}

    	
    	String locationName;
    	if (house != null) {
    		locationName = house.getName();
    	}
    	else {
    		locationName = getCharacterLocation().getName();
    	}
    	String arguments[] = {getMainCustomer().getName(), locationName};
        String message;        
        if (house == null || house.getInternName() == null || house.getInternName().trim().equals("")) {
	        message = TextUtil.t("whore.basic1", whore, getMainCustomer(), arguments) + " ";
        }
        else {
        	message = TextUtil.t("whore.basic2", whore, getMainCustomer(), arguments) + " ";
        }
        
        message += TextUtil.t("whore.service", whore, getMainCustomer());    
        message += "\n";

        CharacterLocation characterLocation = whore.getActivity().getSource();
        
        message += checkPossible(this, whore);
                
        messageData = new MessageData(message, null, characterLocation.getImage());
        
        if (sexType != null) {
	        //Calculate satisfaction
	        int satisfaction = whore.getFinalValue(sexType) / 4;
	        satisfaction += whore.getFinalValue(BaseAttributeTypes.OBEDIENCE);
	        satisfaction += whore.getFinalValue(BaseAttributeTypes.CHARISMA);
	        satisfaction += whore.getFinalValue(SpecializationAttribute.SEDUCTION) / 4;
	        if (getHouse() != null) {
		        satisfaction += getHouse().getCleanState().getSatisfactionModifier();
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
    			log.error("No possible sextypes " + getCharacter().getName() + " " + getMainCustomer().getName());
    			this.sexType = null;
    			return "";
    		}
        	
        	if (obedienceTooLow) {
	        	getMainCustomer().addToSatisfaction(-10 + (whore.getObedience() - whore.getRealMinObedience(sexType.getObedienceRequired(), activity)) * 10, 
	        			activity); //Make customer pissed off
        	}
        	else if (isSextypeNotAllowed) {
	        	getMainCustomer().addToSatisfaction( -40, activity); //Make customer pissed off
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
            	getMainCustomer().addToSatisfaction(-200, activity); //Make customer completely pissed off
            	message += " " + TextUtil.t("whore.noreplacement", whore, getMainCustomer());
        	}
        }
        else {
	        message += TextUtil.t("whore.sextype."+sexType.toString(), whore, getMainCustomer()) + ".";
        }
        return message;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modificationData  = new ArrayList<ModificationData>();
		
		if (sexType != null) {
			modificationData.add(new ModificationData(TargetType.ALL, 1f, sexType));
			modificationData.add(new ModificationData(TargetType.ALL, 0.005f, BaseAttributeTypes.STAMINA));
			modificationData.add(new ModificationData(TargetType.ALL, 0.3f, SpecializationAttribute.SEDUCTION));
			float obedienceModifier = 0.01f;
	        switch (sexType) {
	        case GROUP:
	        	modificationData.add(new ModificationData(TargetType.ALL, -3*groupSize, EssentialAttributes.ENERGY));
	        	modificationData.add(new ModificationData(TargetType.ALL, -1, EssentialAttributes.HEALTH));
	        	break;
	        case MONSTER:
	        	modificationData.add(new ModificationData(TargetType.ALL, -35, EssentialAttributes.ENERGY));
	        	modificationData.add(new ModificationData(TargetType.ALL, -15, EssentialAttributes.HEALTH));
	        	break;
	        case BONDAGE:
	        	modificationData.add(new ModificationData(TargetType.ALL, -10, EssentialAttributes.ENERGY));
	        	modificationData.add(new ModificationData(TargetType.ALL, -5, EssentialAttributes.HEALTH));
	        	obedienceModifier = 0.05f;
	            break;
	        default:
	        	modificationData.add(new ModificationData(TargetType.ALL, -5, EssentialAttributes.ENERGY));
	        }
	        modificationData.add(new ModificationData(TargetType.SLAVE, obedienceModifier, BaseAttributeTypes.OBEDIENCE));
	        modificationData.add(new ModificationData(TargetType.TRAINER, -0.2f, BaseAttributeTypes.COMMAND));
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
                    ImageTag specificTag = ImageTag.getSpecificTag(sexType.getAssociatedImageTag(), image.getTags());
                    String message = null;
                    if (image.getTags().contains(ImageTag.DOMINANTPOSITION)) {
                        message = TextUtil.t("whore.sextype2.dominant." + specificTag, getCharacter(), getMainCustomer(), false);
                    }
                    else if (image.getTags().contains(ImageTag.SUBMISSIVEPOSITION)) {
                        message = TextUtil.t("whore.sextype2.submissive." + specificTag, getCharacter(), getMainCustomer(), false);
                    }                        
                    if (message == null) {
                        message = TextUtil.t("whore.sextype2." + specificTag, getCharacter(), getMainCustomer(), false);
                    }
                    
                    if (message != null && !message.startsWith("whore.sextype2.")) {
                        return new MessageData(message, null, null, null);
                    }
                    else {
                        return null;
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
			if(sexType==Sextype.GROUP){
				CustomerGroup group=(CustomerGroup)getMainCustomer();
				int payment = getMainCustomer().pay(getCharacter().getMoneyModifier()*group.getCustomers().size()/1.8f);
				modifyIncome(payment);}
			else{
				int payment = getMainCustomer().pay(getCharacter().getMoneyModifier());
				modifyIncome(payment);
			}
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
        if(sexType==Sextype.GROUP){//Groups count as several actions.
        	CustomerGroup group=(CustomerGroup)getMainCustomer();
        	if(group.getCustomers().size()>=5){setAmountActions(getAmountActions()+2);}
        	if(group.getCustomers().size()>2 && group.getCustomers().size()<5){setAmountActions(getAmountActions()+1);}
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
}
