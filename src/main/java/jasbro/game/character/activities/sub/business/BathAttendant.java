package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
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

	/**
	 * Further differentiate attend activities. 
	 */
	private enum AttendType {
		/**
		 * No customers.
		 */
		BORED,
		
		/**
		 * Business as usual.
		 */
		BASIC,
		
		/**
		 * Attendant joins the bathing customers for some foreplay.
		 */
		FOREPLAY,

		/**
		 * Attendant joins the bathing customers for oral sex.
		 */
		ORAL,
		
		/**
		 * Attendant joins the bathing customers and lets them have fun with her tits.
		 */
		TITFUCK,
		
		/**
		 * Attendant gets dragged into bath by customers and lets them do as they please.
		 */
		SUBMISSIVE,
		
		SEXY,
		VAGINAL,
		ANAL,
		
		
	}
	
	private final Logger log = Logger.getLogger(BathAttendant.class);
	
	private MessageData messageData;

	private Map<Charakter, AttendType> characterAttendTypeMap=new HashMap<Charakter, AttendType>();
	
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

	/**
	 * Initialization figures out what each slave does exactly.
	 */
	@Override
	public void init() {
		List<Charakter> characters = new ArrayList<Charakter>(getCharacters());
		final Map<AttendType, Integer> supplyCounters=new EnumMap<>(AttendType.class);
		final Map<AttendType, Integer> demandCounters=new EnumMap<>(AttendType.class);
		final Map<Gender, Integer> genderCounters=new EnumMap<>(Gender.class);
		
		// initialize attend type counters 
		for (AttendType attendType: AttendType.values()) {
			supplyCounters.put(attendType, 1);  // demand will be divided by supply (keep this value above zero)
			demandCounters.put(attendType, 0);
		}
		for (Gender gender: Gender.values()) {
			genderCounters.put(gender, 0);
		}
		
		// figure out customer demand
		for (Customer customer: getCustomers()) {
			switch (customer.getPreferredSextype()) {
			case BONDAGE:
				demandCounters.put(AttendType.SUBMISSIVE, demandCounters.get(AttendType.SUBMISSIVE)+30);
				demandCounters.put(AttendType.SEXY, demandCounters.get(AttendType.SEXY)+20);
				break;
			case GROUP:
				demandCounters.put(AttendType.SUBMISSIVE, demandCounters.get(AttendType.SUBMISSIVE)+Util.getInt(15, 30));
				demandCounters.put(AttendType.VAGINAL, demandCounters.get(AttendType.VAGINAL)+Util.getInt(5, 15));
				demandCounters.put(AttendType.ANAL, demandCounters.get(AttendType.ANAL)+Util.getInt(5, 15));
				demandCounters.put(AttendType.ORAL, demandCounters.get(AttendType.ORAL)+Util.getInt(5, 15));
				break;
			case MONSTER:
				demandCounters.put(AttendType.SUBMISSIVE, demandCounters.get(AttendType.SUBMISSIVE)+45);
				demandCounters.put(AttendType.SEXY, demandCounters.get(AttendType.SEXY)+40);
				break;
			case FOREPLAY:
				demandCounters.put(AttendType.FOREPLAY, demandCounters.get(AttendType.FOREPLAY)+30);
				demandCounters.put(AttendType.SEXY, demandCounters.get(AttendType.SEXY)+20);
				break;
			case VAGINAL:
				demandCounters.put(AttendType.SUBMISSIVE, demandCounters.get(AttendType.SUBMISSIVE)+20);
				demandCounters.put(AttendType.VAGINAL, demandCounters.get(AttendType.VAGINAL)+30);
				break;
			case ORAL:
				demandCounters.put(AttendType.ORAL, demandCounters.get(AttendType.ORAL)+30);
				if (customer.getGender()==Gender.FEMALE) {
					demandCounters.put(AttendType.FOREPLAY, demandCounters.get(AttendType.FOREPLAY)+30);
				} 
				else {
					demandCounters.put(AttendType.ORAL, demandCounters.get(AttendType.ORAL)+30);
				}
				break;
			case ANAL:
				
				if (customer.getGender()==Gender.FEMALE) {
					demandCounters.put(AttendType.FOREPLAY, demandCounters.get(AttendType.FOREPLAY)+30);
				} 
				else {
					demandCounters.put(AttendType.ANAL, demandCounters.get(AttendType.ANAL)+30);
				}
				demandCounters.put(AttendType.SUBMISSIVE, demandCounters.get(AttendType.SUBMISSIVE)+20);
				break;
			case TITFUCK:
				if (customer.getGender()==Gender.FEMALE) {
					demandCounters.put(AttendType.FOREPLAY, demandCounters.get(AttendType.FOREPLAY)+30);
				} 
				else {
					demandCounters.put(AttendType.TITFUCK, demandCounters.get(AttendType.TITFUCK)+40);
				}
				demandCounters.put(AttendType.ORAL, demandCounters.get(AttendType.ORAL)+40);
				break;
			default:
				// this should never happen
				log.error("Don't know how to handle customer with preference for "+customer.getPreferredSextype()+".");
				break;
			}
			
			genderCounters.put(customer.getGender(), genderCounters.get(customer.getGender())+1);
		}
		demandCounters.put(AttendType.BASIC, getCustomers().size()*12);  // basic services are always needed
		
		// process characters in random order
		Collections.shuffle(characters);
		
		for (Charakter character: characters) {
			List<AttendType> qualifiedFor=new ArrayList<AttendType>();
			AttendType selected;
			
			if (getCustomers().size()>0) {
				int chance = Util.getInt(0, 99);
				int servicableCustomers=0;
				
				if (character.getAllowedServices().isServiceMales()) {
					servicableCustomers+=genderCounters.get(Gender.MALE);
				}
				if (character.getAllowedServices().isServiceFemales()) {
					servicableCustomers+=genderCounters.get(Gender.FEMALE);
				}
				if (character.getAllowedServices().isServiceFutas()) {
					servicableCustomers+=genderCounters.get(Gender.FUTA);
				}

				
				// check which attend types are suitable for the character 
				qualifiedFor.add(AttendType.BASIC);
				
				// only added sex activities if at least one customer of allowed gender is present
				if (servicableCustomers>0) {
					if (chance + character.getCharisma() > 40 && character.getAllowedServices().isAllowed(Sextype.FOREPLAY) && character.getTraits().contains(Trait.MANUALLABOR)) {
						qualifiedFor.add(AttendType.FOREPLAY);
					}
					if (chance + character.getCharisma() > 40) {
						qualifiedFor.add(AttendType.SEXY);
					}
					if (chance + character.getFinalValue(SpecializationAttribute.SEDUCTION) > 50 && character.getAllowedServices().isAllowed(Sextype.ORAL) && character.getTraits().contains(Trait.DEEPBREATH)) {
						qualifiedFor.add(AttendType.ORAL);
					}
					if (chance + character.getFinalValue(SpecializationAttribute.SEDUCTION) > 50 && character.getAllowedServices().isAllowed(Sextype.ANAL) && character.getTraits().contains(Trait.NAIAD)) {
						qualifiedFor.add(AttendType.ANAL);
					}
					if (chance + character.getFinalValue(SpecializationAttribute.SEDUCTION) > 50 && character.getAllowedServices().isAllowed(Sextype.VAGINAL) && character.getTraits().contains(Trait.NAIAD)) {
						qualifiedFor.add(AttendType.VAGINAL);
					}
					if (chance + servicableCustomers>=3 && character.getCharisma() > 50 
							&& character.getRealMinObedience(Sextype.GROUP.getObedienceRequired(), this) <= character.getObedience()
							 && character.getAllowedServices().isAllowed(Sextype.VAGINAL)  && character.getAllowedServices().isAllowed(Sextype.ORAL)
							 && character.getAllowedServices().isAllowed(Sextype.ANAL)) {
						qualifiedFor.add(AttendType.SUBMISSIVE);
					}
					if (character.getTraits().contains(Trait.NAIAD) && character.getGender()!=Gender.MALE && chance + character.getFinalValue(SpecializationAttribute.SEDUCTION) > 50 
							&& character.getAllowedServices().isAllowed(Sextype.TITFUCK) && !character.getTraits().contains(Trait.LOLI)) {
						qualifiedFor.add(AttendType.TITFUCK);
					}
				}
			}
			else {
				// nothing to do
				qualifiedFor.add(AttendType.BORED);
			}
				
			// sort attend types so highest demand comes first
			Collections.sort(qualifiedFor, new Comparator<AttendType>() {

				@Override
				public int compare(AttendType o1, AttendType o2) {
					int weighedDemand1=demandCounters.get(o1) / supplyCounters.get(o1);
					int weighedDemand2=demandCounters.get(o2) / supplyCounters.get(o2);
					return weighedDemand2-weighedDemand1;
				}
				
			});

			// pick highest rated attend type
			selected = qualifiedFor.get(0);
			characterAttendTypeMap.put(character, selected);
			supplyCounters.put(selected, supplyCounters.get(selected)+1);
		}
	}

	/**
	 * Earns 
	 */
	@Override
	public void perform() {
		for (Charakter character: getCharacters()) {
			int skill; 
	        int amountEarned = 0;
	        int amountHappy = 0;
	        int overalltips = 0;

	        switch (characterAttendTypeMap.get(character)) {
			case BASIC:
				skill = character.getCharisma() +1;
				break;
			case FOREPLAY:
				skill = (character.getCharisma() + character.getFinalValue(Sextype.FOREPLAY)) / 2;
				break;
			case ORAL:
				skill = (character.getCharisma() + character.getFinalValue(Sextype.ORAL)) / 2;
				break;
			case TITFUCK:
				skill = (character.getCharisma() + character.getFinalValue(Sextype.TITFUCK)) / 2;
				break;
			case SUBMISSIVE:
				skill = (character.getCharisma() + character.getFinalValue(Sextype.GROUP) + character.getObedience()) / 3;
				break;
			case ANAL:
				skill = (character.getCharisma() + character.getFinalValue(Sextype.ANAL) + character.getObedience()) / 3;
				break;
			case VAGINAL:
				skill = (character.getCharisma() + character.getFinalValue(Sextype.VAGINAL) + character.getObedience()) / 3;
				break;
			case SEXY:
				skill = (character.getCharisma() + character.getFinalValue(SpecializationAttribute.STRIP));
				break;
			case BORED:
				skill = 1;
				break;
			default:
				// this should never happen
				log.error("Don't know how to handle bath attendant activity subtype "+characterAttendTypeMap.get(character)+".");
				skill=1;
				break;
	        }
	        
	        for (Customer customer : getCustomers()) {
				if (Util.getInt(0, 50) + skill + customer.getSatisfactionAmount() > 50) {
					amountHappy++;
					customer.addToSatisfaction(skill, this);
					int tips = (int)(customer.getMoney() / (200000.0 / skill) + Util.getInt(1, 5)+20);
					tips = customer.pay(tips, character.getMoneyModifier());
					overalltips += tips;
					amountEarned +=  tips;
					customer.changePayModifier(0.3f);
				}
				else {
					customer.addToSatisfaction(skill / 2, this);
				}
				if(Util.getInt(1, 2)==1 && customer.getStatus()==CustomerStatus.TIRED){customer.setStatus(CustomerStatus.LIVELY);}
				if(Util.getInt(1, 2)==1 && (customer.getStatus()==CustomerStatus.DRUNK || customer.getStatus()==CustomerStatus.VERYDRUNK)){customer.setStatus(CustomerStatus.TIRED);}
			}
			modifyIncome(amountEarned);
			
	    	Object arguments[] = {getCustomers().size(), amountHappy, getIncome(), overalltips};
	    	messageData.addToMessage("\n    ");
	        switch (characterAttendTypeMap.get(character)) {
	        
			case BASIC:
	    	    messageData.addToMessage(TextUtil.t("attendBath.result.basic", character, arguments));
				break;
			case FOREPLAY:
	    	    messageData.addToMessage(TextUtil.t("attendBath.result.join", character, arguments));
				break;
			case ORAL:
	    	    messageData.addToMessage(TextUtil.t("attendBath.result.oral", character, arguments));
				break;
			case TITFUCK:
	    	    messageData.addToMessage(TextUtil.t("attendBath.result.titfuck", character, arguments));
				break;
			case ANAL:
	    	    messageData.addToMessage(TextUtil.t("attendBath.result.anal", character, arguments));
				break;
			case VAGINAL:
	    	    messageData.addToMessage(TextUtil.t("attendBath.result.vaginal", character, arguments));
				break;
			case SEXY:
	    	    messageData.addToMessage(TextUtil.t("attendBath.result.sexy", character, arguments));
				break;
			case SUBMISSIVE:
				if (character.getRealMinObedience(Sextype.GROUP.getObedienceRequired(), this)*3 <= character.getObedience() ) {
					messageData.addToMessage(TextUtil.t("attendBath.result.submissive2", character, arguments));
				}
				else {
					messageData.addToMessage(TextUtil.t("attendBath.result.submissive1", character, arguments));
				}
				break;
			case BORED:
				// base message already says it all
				break;
	        }
    	    if (overalltips>0) {
    	    	messageData.addToMessage(" "+TextUtil.t("attendBath.result.tips", character, arguments));
    	    }
		}
	}

    @Override
    public List<ModificationData> getStatModifications() {
        List<ModificationData> modifications = new ArrayList<ModificationData>();
        for (Charakter character : getCharacters()) {
        	if (characterAttendTypeMap.get(character)!=AttendType.BORED) {
	            modifications.add(new ModificationData(TargetType.SINGLE, character, -30, EssentialAttributes.ENERGY));
	            modifications.add(new ModificationData(TargetType.SINGLE, character, 0.02f, BaseAttributeTypes.CHARISMA));
        	}
        	else {
        		// nothing to do: lose less energy
	            modifications.add(new ModificationData(TargetType.SINGLE, character, -5, EssentialAttributes.ENERGY));
        	}
	        switch (characterAttendTypeMap.get(character)) {
			case BASIC:
	            modifications.add(new ModificationData(TargetType.SINGLE, character, 0.02f, BaseAttributeTypes.OBEDIENCE));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.005f, SpecializationAttribute.SEDUCTION));
				break;
			case FOREPLAY:
	            modifications.add(new ModificationData(TargetType.SINGLE, character, 1.00f, Sextype.FOREPLAY));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, SpecializationAttribute.SEDUCTION));
				break;
			case VAGINAL:
	            modifications.add(new ModificationData(TargetType.SINGLE, character, 1.00f, Sextype.VAGINAL));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.02f, SpecializationAttribute.SEDUCTION));
				break;
			case TITFUCK:
	            modifications.add(new ModificationData(TargetType.SINGLE, character, 1.00f, Sextype.TITFUCK));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, SpecializationAttribute.SEDUCTION));
				break;
			case ORAL:
	            modifications.add(new ModificationData(TargetType.SINGLE, character, 1.00f, Sextype.ORAL));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, SpecializationAttribute.SEDUCTION));
				break;
			case ANAL:
	            modifications.add(new ModificationData(TargetType.SINGLE, character, 1.00f, Sextype.ANAL));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, SpecializationAttribute.SEDUCTION));
				break;
			case SEXY:
	            modifications.add(new ModificationData(TargetType.SINGLE, character, 1.00f, SpecializationAttribute.STRIP));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.05f, SpecializationAttribute.SEDUCTION));
				break;
			case SUBMISSIVE:
	            modifications.add(new ModificationData(TargetType.SINGLE, character, 0.75f, Sextype.GROUP));
				modifications.add(new ModificationData(TargetType.SINGLE, character, 0.01f, SpecializationAttribute.SEDUCTION));
	            modifications.add(new ModificationData(TargetType.SINGLE, character, 0.30f, BaseAttributeTypes.OBEDIENCE));
	            modifications.add(new ModificationData(TargetType.SINGLE, character, -5, EssentialAttributes.HEALTH));
				break;
			case BORED:
				// no stat improvements
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
        	messageText=TextUtil.t("attendBath.basic", new Object[]{TextUtil.listCharacters(getCharacters()), numCustomers});
        }
        else {
        	messageText=TextUtil.t("attendBath.nobody", new Object[]{TextUtil.listCharacters(getCharacters()), numCustomers});
        }
        this.messageData = new MessageData(messageText, null, getBackground());
        for (Charakter character : getCharacters()) {
        	List<ImageTag> tags=new ArrayList<ImageTag>();
        	
        	if (characterAttendTypeMap.get(character)==AttendType.BORED) {
        		tags.add(ImageTag.STANDARD);
        	} else {
        		
    	        switch (characterAttendTypeMap.get(character)) {
    			case FOREPLAY:
    				tags.add(ImageTag.KISS);
    				tags.add(ImageTag.FOREPLAY);
    				tags.add(ImageTag.TOUCHING);
    				tags.add(ImageTag.LICKING);
    				tags.add(ImageTag.LAPDANCE);
    				break;
    			case ORAL:
    				tags.add(ImageTag.BLOWJOB);
    				break;
    			case ANAL:
    				tags.add(ImageTag.ANAL);
    				break;
    			case VAGINAL:
    				tags.add(ImageTag.VAGINAL);
    				break;
    			case SEXY:
    				tags.add(ImageTag.SWIMSUIT);
    				tags.add(ImageTag.NAKED);
    				tags.add(ImageTag.DANCE);
    				break;
    			case TITFUCK:
    				tags.add(ImageTag.TITFUCK);
    				break;
    			case SUBMISSIVE:
    				tags.add(ImageTag.GROUP);
    				break;
    			default:
    				tags.add(ImageTag.BATHE);
    				// no additional tags
    				break;
    	        }
        	}
            this.messageData.addImage(ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()));
        }
        return this.messageData;
	}
	
	/**
	 * Check whether the given character enters the pool.
	 */
	public boolean getsWet(Charakter character) {
		return characterAttendTypeMap.containsKey(character) && characterAttendTypeMap.get(character)!=AttendType.BASIC && characterAttendTypeMap.get(character)!=AttendType.BORED;
	}
}
