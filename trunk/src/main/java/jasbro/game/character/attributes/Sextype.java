package jasbro.game.character.attributes;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.Gender;
import jasbro.game.character.conditions.Pregnancy;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public enum Sextype implements AttributeType {
    ORAL(5), TITFUCK(5), VAGINAL(6), BONDAGE(9), ANAL(7), GROUP(9), MONSTER(10), FOREPLAY(4);
    
    private int obedienceRequired;
    private static List<Sextype> possibleSextypesNormal = new ArrayList<Sextype>();
    private static List<Sextype> possibleSextypesGroup = new ArrayList<Sextype>();
    private static List<Sextype> possibleSextypesLesbian = new ArrayList<Sextype>();
    private static List<Sextype> possibleSextypesGay = new ArrayList<Sextype>();
    
    static {
        possibleSextypesNormal.add(FOREPLAY);
        possibleSextypesNormal.add(ORAL);
        possibleSextypesNormal.add(TITFUCK);
        possibleSextypesNormal.add(VAGINAL);
        possibleSextypesNormal.add(ANAL);
        possibleSextypesNormal.add(BONDAGE);
        
        possibleSextypesGroup.add(GROUP);
        
        possibleSextypesLesbian.add(FOREPLAY);
        possibleSextypesLesbian.add(ORAL);
        possibleSextypesLesbian.add(VAGINAL);
        possibleSextypesLesbian.add(ANAL);
        possibleSextypesLesbian.add(BONDAGE);
        
        possibleSextypesGay.add(FOREPLAY);
        possibleSextypesGay.add(ORAL);
        possibleSextypesGay.add(ANAL);
        possibleSextypesGay.add(BONDAGE);
    }
    
    private Sextype(int obedienceRequired) {
    	this.obedienceRequired = obedienceRequired;
    }
    
    public String getText() {
    	return TextUtil.t(this.toString());
    }
    
    public ImageTag getAssociatedImageTag() {
        switch (this) {
        case ORAL:
            return ImageTag.ORAL;
        case TITFUCK:
            return ImageTag.TITFUCK;
        case VAGINAL:
            return ImageTag.VAGINAL;
        case BONDAGE:
            return ImageTag.BONDAGE;
        case ANAL:
            return ImageTag.ANAL;
        case GROUP:
            return ImageTag.GROUP;
        case MONSTER:
            return ImageTag.MONSTER;
        case FOREPLAY:
            return ImageTag.FOREPLAY;
        default:
            return ImageTag.NAKED;
        }
    }
    
	@Override
	public int getDefaultMin() {
		return 0;
	}
	@Override
	public int getDefaultMax() {
		return 100;
	}
	@Override
	public int getRaiseMaxBy() {
		return 50;
	}
	
	@Override
	public int getStartValue() {
	    return 0;
	}

	public int getObedienceRequired() {
		return obedienceRequired;
	}

	public void setObedienceRequired(int obedienceRequired) {
		this.obedienceRequired = obedienceRequired;
	}
	
	public static Sextype getPreferredSextype(Customer customer) {
		if (customer.getType() == CustomerType.GROUP) {
			return Sextype.GROUP;
		}
		else {
			return randomAnySextype();
		}
	}
	
	public static List<Sextype> getPossibleSextypes(Customer customer) {
        if (customer.getType() == CustomerType.GROUP) {
            return new ArrayList<Sextype>(possibleSextypesGroup);
        }
        else {
            return new ArrayList<Sextype>(possibleSextypesNormal);
        }
	}
	
	public static List<Sextype> getPossibleSextypes(Gender gender) {
		return new ArrayList<Sextype>(possibleSextypesNormal);
	}
	
	public static List<Sextype> getPossibleSextypes(Gender gender1, Gender gender2) {
		if (gender1 == Gender.MALE && gender2 == Gender.MALE) {
			return new ArrayList<Sextype>(possibleSextypesGay);
		}
		else if (gender1 == Gender.FEMALE && gender2 == Gender.FEMALE) {
			return new ArrayList<Sextype>(possibleSextypesLesbian);
		}
		else {
			return new ArrayList<Sextype>(possibleSextypesNormal);
		}
	}
    
    public static Sextype randomGaySextype() {
        int chance = Util.getInt(0, 100);
        if (chance < 20) {
            return Sextype.FOREPLAY;
        }
        else if (chance < 40) {
            return Sextype.BONDAGE;
        }
        else if (chance < 60) {
            return Sextype.ORAL;
        }
        else {
            return Sextype.ANAL;
        }
    }
    
	private static Sextype randomAnySextype() {
        int chance = Util.getInt(0, 100);
        if (chance < 25) {
            return Sextype.FOREPLAY;
        }
        else if (chance < 45) {
            return Sextype.VAGINAL;
        }
        else if (chance < 60) {
            return Sextype.ANAL;
        }
        else if (chance < 75) {
            return Sextype.ORAL;
        }
        else if (chance < 90) {
            return Sextype.TITFUCK;
        } 
        else {
            return Sextype.BONDAGE;
        }
	}	
	
	public static List<Sextype> getPossibleSextypes(Customer customer, Charakter character) {
		List<Sextype> possibleSexTypes;
		if (customer.getType() == CustomerType.GROUP) {
		    possibleSexTypes = new ArrayList<Sextype>(possibleSextypesGroup);
		}
		else {
			possibleSexTypes = Sextype.getPossibleSextypes(customer.getGender(), character.getGender());
		}
    	for (int i = 0; i < possibleSexTypes.size(); i++) {
    		Sextype sextype = possibleSexTypes.get(i);
    		if (!character.getAllowedServices().isAllowed(sextype)) {
    			possibleSexTypes.remove(sextype);
    			i--;
    		}
    	}
    	return possibleSexTypes;
	}
	
	public static boolean isPregnancyPossible(Sextype sextype, Charakter character, Person otherPerson) {
	    if ((sextype == Sextype.VAGINAL || sextype == Sextype.GROUP) && !character.isUsesContraceptives() &&
	            character.getGender() != Gender.MALE && otherPerson.getGender() != Gender.FEMALE) {
	        for (Condition condition : character.getConditions()) {
	            if (condition instanceof Pregnancy) {
	                return false;
	            }
	        }
	        return true;
	    }
	    else {
	        return false;
	    }
	}
}
