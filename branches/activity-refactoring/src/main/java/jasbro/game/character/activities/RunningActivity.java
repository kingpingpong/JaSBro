package jasbro.game.character.activities;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.Customer.SatisfactionModifier;
import jasbro.game.housing.House;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.world.CharacterLocation;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pictures.ImageData;
import jasbro.util.ConfigHandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class RunningActivity {
	private final static Logger log = Logger.getLogger(RunningActivity.class);
    private List<AttributeModification> attributeModifications = new ArrayList<AttributeModification>();
    private int income = 0;
    private PlannedActivity plannedActivity;
    private ActivityExecutor activityExecutor;
    private boolean abort = false;
    private List<MessageData> messages = new ArrayList<MessageData>();
    private float fameModifier = 1f;
    private List<SatisfactionModifier> initialSatisfactionModifiers = new ArrayList<SatisfactionModifier>();
    private List<Customer> mainCustomers = new ArrayList<Customer>() ;
    private List<Customer> customers = new ArrayList<Customer>();
    private int maxMainCustomers = 1;
    private Integer minimumObedience = null;
    private Sextype sextype;

    //Methods to overwrite
    public abstract MessageData getBaseMessage();
    
    public void init() {
    }
    
    public void perform() {        
    }
    
    public List<ModificationData> getStatModifications() {
        return new ArrayList<ModificationData>();
    }
        
    private void initActivity() {
        List<Charakter> slaves = Util.getSlaves(plannedActivity.getCharacters());
        List<Charakter> trainers = Util.getTrainers(plannedActivity.getCharacters());
        
        for (Customer customer : getCustomers()) {
            initialSatisfactionModifiers.addAll(customer.getSatisfactionModifiers());
        }
        for (Customer customer : getMainCustomers()) {
            initialSatisfactionModifiers.addAll(customer.getSatisfactionModifiers());
        }
        
        init();
        
        for (ModificationData modificationData : getStatModifications()) {
            List<Charakter> targets;
            if (modificationData.getTargetType() == TargetType.ALL) {
                targets = plannedActivity.getCharacters();
            }
            else  if (modificationData.getTargetType() == TargetType.SLAVE) {
                targets = slaves;
            }
            else  if (modificationData.getTargetType() == TargetType.TRAINER) {
                targets = trainers;
            }
            else  if (modificationData.getTargetType() == TargetType.SINGLE) {
            	AttributeModification modificator = new AttributeModification(modificationData.getAmount(), 
                        modificationData.getAttributeType(), modificationData.getTarget());
                attributeModifications.add(modificator);
                continue;
            }
            else if (modificationData.getTargetType() == TargetType.ALLHOUSE) {
            	targets = new ArrayList<Charakter>();
                House house = ((Room) plannedActivity.getSource()).getHouse();
                for (Room room : house.getRooms()) {
                	targets.addAll(room.getCurrentUsage().getCharacters());
                }  
            }
            else {
                targets = new ArrayList<Charakter>();
                log.error("No target for modification");
            }
            for (Charakter target : targets) {
                AttributeModification modificator = new AttributeModification(modificationData.getAmount(), 
                        modificationData.getAttributeType(), target);
                attributeModifications.add(modificator);
            }
        }
        
        messages.add(getBaseMessage());
    }
    
    
    public void performActivity() {
    	//Perform init
    	initActivity();
    	    	
    	MyEvent event = new MyEvent(EventType.ACTIVITY, this);
    	Jasbro.getInstance().getData().getEventManager().handleEvent(event);
    	if (getPlannedActivity().getSource() instanceof Room) {
    		House house = ((Room)getPlannedActivity().getSource()).getHouse();
    		house.handleEvent(event);
    	}
    	if (getPlannedActivity().getSource() != null) {
    		getPlannedActivity().getSource().handleEvent(event);
    	}
    	for (Charakter character : getCharacters()) {
    		character.handleEvent(event);
    	}
    	
    	//Check whether everyone is ready to perform this act
    	Charakter obedienceTooLowCharacter = null;
    	for (Charakter character : getCharacters()) {
    		if (character.getType() == CharacterType.SLAVE && character.getObedience() < getMinimumObedience()) {
    			int diff = character.getRealMinObedience(getMinimumObedience(), this) - character.getObedience();
    			if (diff == 1 && Util.getInt(0, 100) > 75) {
    				obedienceTooLowCharacter = character;
    			}
    			else if (diff == 2 && Util.getInt(0, 100) > 50) {
    				obedienceTooLowCharacter = character;
    			}
    			else if (diff == 3 && Util.getInt(0, 100) > 25) {
    				obedienceTooLowCharacter = character;
    			}
    			else if (diff > 3) {
    				obedienceTooLowCharacter = character;
    			}
    		}
    	}
    	
    	if (obedienceTooLowCharacter != null) {
    		PlannedActivity plannedActivity = new PlannedActivity(ActivityType.REFUSEDTOWORK, getPlannedActivity());
    		plannedActivity.getCharacters().clear();
    		plannedActivity.getCharacters().addAll(getCharacters());
    		ActivityExecutor activity = plannedActivity.getActivityExecutor(activityExecutor.getShift());
    		RefusedToWorkExecutor refusedToWorkActivity = (RefusedToWorkExecutor) activity;
    		activity.init();
    		refusedToWorkActivity.setCausedByCharacter(obedienceTooLowCharacter);
    		refusedToWorkActivity.setOriginalActivity(getType());
    		activity.execute();
    		abort = true;
    	}
    	
        if (!abort) {
            perform();
        	event = new MyEvent(EventType.ACTIVITYPERFORMED, this);
        	Jasbro.getInstance().getData().getEventManager().handleEvent(event);
        	if (getPlannedActivity().getSource() instanceof Room) {
        		House house = ((Room)getPlannedActivity().getSource()).getHouse();
        		house.handleEvent(event);
        	}
        	if (getPlannedActivity().getSource() != null) {
        		getPlannedActivity().getSource().handleEvent(event);
        	}
        	for (Charakter character : getCharacters()) {
        		character.handleEvent(event);
        	}
        	
            if (income > 0) {
            	Jasbro.getInstance().getData().earnMoney(income, this);
            }
            else if (income < 0) {
            	Jasbro.getInstance().getData().spendMoney(-income, this);
            }
            
            //Consolidate attribute modifications in case multiple of the same were added for some reason
            for (int i = 0; i < attributeModifications.size(); i++) {
            	AttributeModification attributeModification = attributeModifications.get(i);
                for (int j = 0; j < attributeModifications.size(); j++) {
                	if (i != j) {
                    	AttributeModification attributeModification2 = attributeModifications.get(j);
                    	if (attributeModification.getAttributeType() == attributeModification2.getAttributeType()
                    			&& attributeModification.getTargetCharacter() == attributeModification2.getTargetCharacter()) {
                    		attributeModification.addModificator(attributeModification2.getBaseAmount());
                    		attributeModifications.remove(attributeModification2);
                    		j--;                    		
                    	}
                	}
                }
            }
            
            //Apply attribute modifications
            for (int i = 0; i < attributeModifications.size(); i++) {
                AttributeModification modification = attributeModifications.get(i);
                modification.applyModification(this);
            }
                        
            for (int i = 0; i < messages.size(); i++) {
            	MessageData message = messages.get(i);
            	if (message != null) {
	            	if (message.getBackground() == null) {
	            		if (plannedActivity.getSource() != null) {
	            			message.setBackground(plannedActivity.getSource().getImage());
	            		}
	            		else {
	            			message.setBackground(getCharacters().get(0).getBackground());
	            		}            		
	            	}
	            	if (i == messages.size() - 1) {
	            		message.setAttributeModifications(attributeModifications);
	            	}
	            	if (getHouse() != null) {
		            	message.setMessageGroupObject(getHouse());
	            	}
	            	else {
	            		message.setMessageGroupObject(getCharacterLocation());
	            	}
	            	if(!(getPlannedActivity().getType()==ActivityType.SLEEP && !ConfigHandler.isShowSleep())){message.createMessageScreen();}
            	}
            }
            
            //modifiy fame
            if (this instanceof BusinessMainActivity) {
            	for (Customer customer : getMainCustomers()) {
    	            float fameMod = customer.getImportance() * customer.getSatisfaction().getFameModifier() * fameModifier;
    	            if (getHouse() != null) {
        	        	getHouse().getFame().modifyFame(fameMod);
    	            }
    	            for (Charakter character : getCharacters()) {
    	            	character.getFame().modifyFame(fameMod / getCharacters().size());
    	            }
            	}
            }
            
            if (this instanceof BusinessSecondaryActivity) {
            	for (Customer customer : getCustomers()) {            		
            		int satisfactionModifiedBy = 0;
        			for (SatisfactionModifier satisfactionModifier : customer.getSatisfactionModifiers()) {
        				if (!initialSatisfactionModifiers.contains(satisfactionModifier)) {
        					satisfactionModifiedBy += satisfactionModifier.getModifiedBy();
        				}
        			}            		
            		
    	            float fameMod = (customer.getImportance() * satisfactionModifiedBy * fameModifier) / 100.0f;
    	            if (getHouse() != null) {
        	        	getHouse().getFame().modifyFame(fameMod);
    	            }
    	            for (Charakter character : getCharacters()) {
    	            	character.getFame().modifyFame(fameMod / getCharacters().size());
    	            }
            	}
            }
            
            event = new MyEvent(EventType.ACTIVITYFINISHED, this);
            Jasbro.getInstance().getData().getEventManager().handleEvent(event);
            if (getPlannedActivity().getSource() instanceof Room) {
                House house = ((Room)getPlannedActivity().getSource()).getHouse();
                house.handleEvent(event);
            }
            if (getPlannedActivity().getSource() != null) {
                getPlannedActivity().getSource().handleEvent(event);
            }
            for (Charakter character : getCharacters()) {
                character.handleEvent(event);
            }
        }
        else {
        	if (getType() != ActivityType.WHORE) {
				for (Charakter character : getCharacters()) {
					character.getAttribute(EssentialAttributes.ENERGY).addToValue(-15);
				}
        	}
        }
    }
    
    public void applyChanges() {
        
    }
    
    public House getHouse() {
    	CharacterLocation CharacterLocation = plannedActivity.getSource();
    	if (CharacterLocation instanceof Room) {
    		return ((Room) CharacterLocation).getHouse();
    	}
    	else {
    		return null;
    	}
    }
    
    public Room getRoom() {
    	CharacterLocation CharacterLocation = plannedActivity.getSource();
    	if (CharacterLocation instanceof Room) {
    		return (Room) CharacterLocation;
    	}
    	else {
    		return null;
    	}
    }
    
    public void setPlannedActivity(PlannedActivity plannedActivity) {
        this.plannedActivity = plannedActivity;
        if (plannedActivity != null) {
            MyEvent event = new MyEvent(EventType.ACTIVITYCREATED, this);
            for (Charakter character : getCharacters()) {
                character.handleEvent(event);
            }
        }
    }
    
    public ActivityType getType() {
        return plannedActivity.getType();
    }
    public List<AttributeModification> getAttributeModifications() {
        return attributeModifications;
    }
    public PlannedActivity getPlannedActivity() {
        return plannedActivity;
    }
    public Charakter getCharacter() {
        return plannedActivity.getCharacters().get(0);
    }
    public List<Charakter> getCharacters() {
        return plannedActivity.getCharacters();
    }
    public CharacterLocation getCharacterLocation() {
        return plannedActivity.getSource();
    }

    public boolean isAbort() {
        return abort;
    }

    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    public List<MessageData> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageData> messages) {
        this.messages = messages;
    }

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
        this.income = income;
    }

    public void modifyIncome(int modValue) {
		this.income += modValue;
	}
    
    @Override
    public String toString() {
    	return getType().getText();
    }

    public float getFameModifier() {
        return fameModifier;
    }

    public void setFameModifier(float fameModifier) {
        this.fameModifier = fameModifier;
    }
    
    public Customer getMainCustomer() {
        if (mainCustomers.size() > 0) {
            return mainCustomers.get(0);
        }
        else {
            return null;
        }
    }

    public void addMainCustomer(Customer mainCustomer) {
        this.mainCustomers.add(mainCustomer);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public boolean hasMainCustomer() {
        return getMainCustomers().size() > 0;
    }
    
    public void addAttendingCustomer(Customer customer) {
        customers.add(customer);
    }

    public List<Customer> getMainCustomers() {
        return mainCustomers;
    }

    public void setMainCustomers(List<Customer> mainCustomers) {
        this.mainCustomers = mainCustomers;
    }

    public int getMaxMainCustomers() {
        return maxMainCustomers;
    }

    public void setMaxMainCustomers(int maxMainCustomers) {
        this.maxMainCustomers = maxMainCustomers;
    }

    public int getMinimumObedience() {
        if (minimumObedience == null) {
            if (getType() == null) {
                return 0;
            }
            minimumObedience = getType().getMinimumObedience();
        }
        return minimumObedience;
    }

    public void setMinimumObedience(Integer minimumObedience) {
        this.minimumObedience = minimumObedience;
    }

    public class ModificationData {
        private TargetType targetType;
        private Charakter target;
        private float amount;
        private AttributeType attributeType;
        
        public ModificationData() {
        }
        
        public ModificationData(TargetType targetType, float amount, AttributeType attributeType) {
            super();
            this.targetType = targetType;
            this.amount = amount;
            this.attributeType = attributeType;
        }

        public ModificationData(TargetType targetType, Charakter target,
                float amount, AttributeType attributeType) {
            super();
            this.targetType = targetType;
            this.target = target;
            this.amount = amount;
            this.attributeType = attributeType;
        }

        public TargetType getTargetType() {
            return targetType;
        }
        public void setTargetType(TargetType target) {
            this.targetType = target;
        }
        public float getAmount() {
            return amount;
        }
        public void setAmount(float amount) {
            this.amount = amount;
        }
        public AttributeType getAttributeType() {
            return attributeType;
        }
        public void setAttributeType(AttributeType attributeType) {
            this.attributeType = attributeType;
        }
        public Charakter getTarget() {
            return target;
        }
        public void setTarget(Charakter target) {
            this.target = target;
        }
        
        public List<ModificationData> getModificationForSpecialization(SpecializationType specialization, float amount, TargetType target) {
            List<ModificationData> modificationData = new ArrayList<ModificationData>();
            for (AttributeType specializationAttribute : specialization.getAssociatedAttributes()) {
                modificationData.add(new ModificationData(target, amount, specializationAttribute));
            }
            return modificationData;
        }
    }

    public enum TargetType {
        SLAVE, TRAINER, ALL, SINGLE, ALLHOUSE
    }

    public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
        return null;
    }
    
    public ImageData getBackground() {
        return getCharacter().getBackground();
    }

    public Sextype getSextype() {
        return sextype;
    }

    public void setSextype(Sextype sextype) {
        this.sextype = sextype;
    }

	public ActivityExecutor getActivityExecutor() {
		return activityExecutor;
	}

	public void setActivityExecutor(ActivityExecutor activityExecutor) {
		this.activityExecutor = activityExecutor;
	}
    
    
}
