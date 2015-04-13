package jasbro.game.character;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.DefaultPreferences;
import jasbro.game.GameObject;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.DamageType;
import jasbro.game.character.battle.Unit;
import jasbro.game.character.conditions.StartingAtTheBottom;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.PerkHandler;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.character.traits.Trait;
import jasbro.game.character.warnings.Severity;
import jasbro.game.character.warnings.Warning;
import jasbro.game.events.AttributeChangedEvent;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.AllowedServices;
import jasbro.game.events.business.Fame;
import jasbro.game.housing.Room;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.game.items.CharacterInventory;
import jasbro.game.items.Equipment;
import jasbro.game.items.EquipmentSlot;
import jasbro.game.world.Time;
import jasbro.gui.pages.MessageScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

public class Charakter extends GameObject implements Unit, HasImagesInterface {
    private String name;
    private ImageData icon;
    private Map<AttributeType, Attribute> attributes = new HashMap<AttributeType, Attribute>();
    private Map<Time, PlannedActivity> activities = new HashMap<Time, PlannedActivity>();
    private List<SpecializationType> specializations = new ArrayList<SpecializationType>();
    private List<Condition> conditions = new ArrayList<Condition>();
    private CharacterType type;
    private Gender gender;
    private Fame fame = new Fame();
    private Ownership ownership = Ownership.OWNED;
    private List<Trait> traits;
    private String baseId;
    private AllowedServices allowedServices;
    private CharacterInventory characterInventory;
    private CharacterStuffCounter counter;
    private int bonusPerks = 0;
    private AgeProgressionData ageProgressionData;
    private Boolean usesContraceptives;
        
    private transient CharacterBase base;
    private transient Map<Object, Object> cache;
    

    
    public Charakter(CharacterBase base) {
        this.base = base;
    }

    public CharacterBase getBase() {
    	if (base == null) {
    		List<CharacterBase> bases = Jasbro.getInstance().getCharacterBases();
            for (CharacterBase base : bases) {
                if (getBaseId().equals(base.getId())) {
                    setBase(base);
                    break;
                }
            }
    	}
        return base;
    }

    public void setBase(CharacterBase base) {
        this.base = base;
    }

    public ImageData getIcon() {
    	if (!ImageUtil.getInstance().exists(icon)) {
    		icon = ImageUtil.getInstance().getImageDataByTag(ImageTag.ICON, getImages());
    	}
        return icon;
    }

    public void setIcon(ImageData icon) {
        this.icon = icon;
    }

    public String getName() {
        return this.name;
    }

    public CharacterType getType() {
        return type;
    }
    
    public boolean hasAttribute(String attributeName) {
        return attributes.containsKey(attributeName);
    }

    protected void addAttribute(Attribute attribute) {
        attributes.put(attribute.getAttributeType(), attribute);
    }
    
    public PlannedActivity getActivity() {
        Time time = Jasbro.getInstance().getData().getTime();
        if (activities.containsKey(time)) {
            return activities.get(time);
        }
        else {
            return null;
        }
    }
    
    public Attribute getAttribute(AttributeType attributeType) {
        if (! getAttributes().containsKey(attributeType)) {
            if (!(attributeType instanceof CalculatedAttribute)) {
                Attribute attribute = new Attribute(this, attributeType);
                attributes.put(attributeType, attribute); 
            }
            else {
                return null;
            }           
        }
        return attributes.get(attributeType);
    }

    public void setActivity(PlannedActivity activity) {
        Time time = Jasbro.getInstance().getData().getTime();
        PlannedActivity curActivity = getActivity();
        getActivities().put(time, activity);
        MyEvent event = new MyEvent(EventType.ACTIVITYCHANGE, this);
        if (curActivity != null && curActivity != activity && curActivity.getCharacters().contains(this)) {
            curActivity.removeCharacter(this);
        }
        handleEvent(event);
        if (activity != null && activity != curActivity) {
            activity.getSource().fireEvent(event);
        }
    }
    
    public void removeActivity(PlannedActivity activity) {
    	for (Time time : Time.values()) {
    		if (activities.get(time) == activity) {
    			activities.put(time, null);
    		}
    	}
    }

    public Map<Time, PlannedActivity> getActivities() {
        return activities;
    }

    public void setActivities(Map<Time, PlannedActivity> activities) {
        this.activities = activities;
    }
    
    public Map<AttributeType, Attribute> getAttributes() {
        return attributes;
    }
    
    @Transient
    @XmlTransient
    public List<ImageData> getImages() {
        return getBase().getImages();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setType(CharacterType type) {
        this.type = type;
    }

    public String getBaseId() {
        return baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }
    
    public List<SpecializationType> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<SpecializationType> specializations) {
        this.specializations = specializations;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }
    
    public CharacterStuffCounter getCounter() {
    	if (counter == null) {
    		counter = new CharacterStuffCounter();
    	}
		return counter;
	}

    public void addTrait(Trait trait) {
    	getTraits(); //Just make sure it's initialized
    	if (!traits.contains(trait)) {
    		if (trait.addTrait(this, traits)) {
        		traits.add(trait);
                fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
    		}
    	}
    	getCache().clear();
    }
    
    public void removeTrait(Trait trait) {
    	getTraits(); //Just make sure it's initialized
    	boolean contains = traits.contains(trait);
    	if (contains) {
    		if (trait.removeTrait(this)) {
    			traits.remove(trait);
                fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
    		}
    	}
    	getCache().clear();
    }

    public int getHealth() {
        return getFinalValue(EssentialAttributes.HEALTH);
    }
    
    public int getEnergy() {
        return getFinalValue(EssentialAttributes.ENERGY);
    }
    
    public int getStrength() {
        return getFinalValue(BaseAttributeTypes.STRENGTH);
    }    
    public int getCharisma() {
        return getFinalValue(BaseAttributeTypes.CHARISMA);
    }    
    public int getObedience() {
        return getFinalValue(BaseAttributeTypes.OBEDIENCE);
    }    
    public int getStamina() {
        return getFinalValue(BaseAttributeTypes.STAMINA);
    }    
    public int getIntelligence() {
        return getFinalValue(BaseAttributeTypes.INTELLIGENCE);
    }
    public int getCommand() {
        return getFinalValue(BaseAttributeTypes.COMMAND);
    }  
    public Fame getFame() {
    	if (this.fame == null) {
    		this.fame = new Fame();
    	}
		return fame;
	}
    
	public int getFinalValue(AttributeType attributeType) {    
	    Integer cachedValue = (Integer) getCachedValue(attributeType);
	    if (cachedValue != null) {
	        return cachedValue;
	    }
	    
        Attribute attribute = getAttribute(attributeType);
        float value = attribute.getInternValue();
        
        for (Trait trait : getTraits()) {
            value += trait.getAttributeModifier(attribute);
        }
        
        for (Condition condition : getConditions()) {
        	value += condition.getAttributeModifier(attribute);
        }
        
        for (Equipment equipment : getCharacterInventory().listEquipment()) {
        	value += equipment.getAttributeModifier(attribute);
        }
        
        getCache().put(attributeType, ((int) value));
        
        if (value < attribute.getMinValue()) {
            value = attribute.getMinValue();
        }
        return (int) value;
    }
	
	public double getAnyAttributeValue(AttributeType attributeType) {
        if (attributeType instanceof CalculatedAttribute) {
            switch ((CalculatedAttribute) attributeType) {
            case SKILLPOINTS:
                return getUnspentPerkPoints();
            case ARMORPERCENT:
                return getArmor();
            case ARMORVALUE:
                return getArmorValue();
            case BLOCKAMOUNT:
                return getBlockAmount();
            case BLOCKCHANCE:
                return getBlockChance();
            case CRITDAMAGEAMOUNT:
                return getCritDamageBonus();
            case CRITCHANCE:
                return getCritChance();
            case DAMAGE:
                return getDamage();
            case HIT:
                return getHit();
            case DODGE:
                return getDodge();
            case SPEED:
                return getSpeed();
            case ITEMLOOTCHANCEMODIFIER:
                return getItemLootChanceModifier();
            case STEALCHANCE:
                return getStealChance();
            case STEALAMOUNTMODIFIER:
                return getStealAmountModifier();
            case STEALITEMCHANCE:
                return getStealItemChance();
            case PREGNANCYCHANCE:
                return getPregnancyChance();
            case MINCHILDREN:
                return getMinChildren();
            case MAXCHILDREN:
                return getMaxChildren();
            case CONTROL:
                return getControl();
            case CHANCEADDITIONALCHILD:
                return getChanceAdditionalChild();
            case PREGNANCYDURATIONMODIFIER:
                return getPregnancyDurationModifier();
            case AMOUNTCUSTOMERSPERSHIFT:
                return getPossibleAmountCustomers();
            case HOLYRESISTANCE:
                return getResistance(DamageType.HOLY);
            case DARKNESSRESISTANCE:
                return getResistance(DamageType.DARKNESS);
            case FIRERESISTANCE:
                return getResistance(DamageType.FIRE);
            case WATERRESISTANCE:
                return getResistance(DamageType.WATER);
            case WINDRESISTANCE:
                return getResistance(DamageType.WIND);
            case EARTHRESISTANCE:
                return getResistance(DamageType.EARTH);
            case MAGICRESISTANCE:
                return getResistance(DamageType.MAGIC);
            case LIGHTNINGRESISTANCE:
                return getResistance(DamageType.LIGHTNING);
            default:
                return 0;
            }
        } else {
            return getFinalValue(attributeType);
        }
	}

	public AgeProgressionData getAgeProgressionData() {
	    if (ageProgressionData == null) {
	        ageProgressionData = new AgeProgressionData(base.getAgeProgressionData());
	        if (!getType().isChildType() && !ageProgressionData.getAdultBases().contains(baseId)) {
	            ageProgressionData.getAdultBases().add(baseId);
	        }
	    }
        return ageProgressionData;
    }

    public int getBonus(AttributeType attributeType) {
		Attribute attribute = getAttribute(attributeType);
		return (int) (getFinalValue(attributeType) - ((int)attribute.getInternValue()));
	}
    
    public ImageData getBackground() {
    	if (getActivity() != null) {
    		return getActivity().getSource().getImage();
    	}
    	else {
    		return new ImageData("images/backgrounds/sky.jpg");
    	}
    }
    
    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.ACTIVITYCHANGE) {
            getCache().remove(Warning.class);
            fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
            return;//nothing is actually happening, abort
        }
        else if (e.getType() == EventType.ATTRIBUTECHANGED) {
            AttributeChangedEvent attributeChangedEvent = (AttributeChangedEvent) e;
            Attribute attribute = attributeChangedEvent.getAttribute();
            resetAttributeCache(attribute.getAttributeType());
        }
        else if (e.getType() == EventType.ITEMUSED) {
            getCache().clear();
            fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
        }
        else if (e.getType() == EventType.SHIFTSTART) {
            getCache().clear();
            clearGuiListeners();
        }
        else if (e.getType() == EventType.NEXTSHIFTSTARTED) {
            if (getActivity() != null) {
                getActivity().checkPossibleActivities();
            }
        }
    	
    	List<Condition> conditions = new ArrayList<Condition>();
    	conditions.addAll(getConditions()); //Cheap way to prevent concurrentModification Errors
    	for (Condition condition : conditions) {
    		condition.handleEvent(e);
    	}
    	
    	for (Trait trait : getTraits()) {
    		trait.handleEvent(e, this);
    	}
    	
    	for (SpecializationType specialization : getSpecializations()) {
    		specialization.handleEvent(e, this);
    	}
    	
    	for (Equipment item : getCharacterInventory().listEquipment()) {
    		item.handleEvent(e, this);
    	}
    	
    	getCounter().handleEvent(e, this);
    	
    	if (e.getType() == EventType.ATTRIBUTECHANGED) {
    		AttributeChangedEvent attributeChangedEvent = (AttributeChangedEvent) e;
    		Attribute attribute = attributeChangedEvent.getAttribute();
    		if (attribute.getCharacter() == this) {
        		if (attributeChangedEvent.getAttribute().getAttributeType().equals(EssentialAttributes.ENERGY) 
        				&& getEnergy() + attribute.getValue() <= 0) {
        		    MyEvent event = new AttributeChangedEvent(EventType.ENERGYZERO, attribute, attributeChangedEvent.getAmount(), attributeChangedEvent.getActivity());
        		    handleEvent(event);
        			fireEvent(event);
        		}
        		else if (attribute.getAttributeType().equals(EssentialAttributes.HEALTH) 
        				&& getHealth() + attribute.getValue() <= 0) {
                    MyEvent event = new AttributeChangedEvent(EventType.HEALTHZERO, attribute, attributeChangedEvent.getAmount(), attributeChangedEvent.getActivity());
                    handleEvent(event);
        			fireEvent(event);
        		}
        		else {
        		    fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
        		}
    		}
    	}
    	else if (e.getType() == EventType.NEXTDAY) {
    		if (this.ownership == Ownership.CONTRACT) {
	    		int wage = 50 + (int) Math.sqrt(calculateValue());
	    		if (getTraits().contains(Trait.RAPACIOUS)) {
	    		    wage = (int) (wage + wage*0.3f);
	    		}
	    		else if (getTraits().contains(Trait.LOYAL)) {
	    		    wage = (int) (wage - wage*0.3f);
	    		}
	    		if (Jasbro.getInstance().getData().canAfford(wage)) {
	    			Jasbro.getInstance().getData().spendMoney(wage, this);
	    		}
	    		else {
	    			Jasbro.getInstance().removeCharacter(this);
	    			new MessageScreen(TextUtil.t("trainer.cannotafford", this), ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this), getBackground());
	    		}
	    		
                if (getType() == CharacterType.TRAINER && getAttribute(BaseAttributeTypes.COMMAND).getInternValue() < 1) {
                    List<AttributeModification> modifications = new ArrayList<AttributeModification>();
                    if (getActivity() == null) {
                        for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                            if (character.getType() == CharacterType.SLAVE) {
                                AttributeModification attributeModification = new AttributeModification(-0.1f, BaseAttributeTypes.OBEDIENCE, character);
                                attributeModification.applyModification();
                                modifications.add(attributeModification);
                            }
                        }
                    }
                    else {
                        for (Charakter character : Util.getAllCharactersSuperLocation(getActivity().getSource())) {
                            if (character.getType() == CharacterType.SLAVE) {
                                AttributeModification attributeModification = new AttributeModification(-0.1f, BaseAttributeTypes.OBEDIENCE, character);
                                attributeModification.applyModification();
                                modifications.add(attributeModification);
                            }
                        }
                    }
                    MessageData messageData = new MessageData(TextUtil.t("trainer.lowCommand", this), 
                            ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this), getBackground());
                    messageData.setAttributeModifications(modifications);
                    new MessageScreen(messageData);
                }
            } else if (this.ownership == Ownership.OWNED && getType() == CharacterType.TRAINER) {
                int wage = 20 + (int) Math.sqrt(calculateValue()) / 2;
                if (getTraits().contains(Trait.RAPACIOUS)) {
                    wage = (int) (wage + wage * 0.3f);
                } else if (getTraits().contains(Trait.LOYAL)) {
                    wage = (int) (wage - wage * 0.3f);
                }
                Jasbro.getInstance().getData().spendMoney(wage, this);

                if (getType() == CharacterType.TRAINER && getAttribute(BaseAttributeTypes.COMMAND).getInternValue() < 1) {
                    List<AttributeModification> modifications = new ArrayList<AttributeModification>();
                    for (Charakter character : Jasbro.getInstance().getData().getCharacters()) {
                        if (character.getType() == CharacterType.SLAVE) {
                            AttributeModification attributeModification = new AttributeModification(-0.25f, 
                                    BaseAttributeTypes.OBEDIENCE, character);
                            attributeModification.applyModification();
                            modifications.add(attributeModification);
                        }
                    }
                    MessageData messageData = new MessageData(TextUtil.t("trainer.avatarLowCommand", this), 
                            ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, this), getBackground());
                    messageData.setAttributeModifications(modifications);
                    new MessageScreen(messageData);
                }
    		}
    		else {
    			Jasbro.getInstance().getData().spendMoney(20, TextUtil.t("slaveupkeep")); //general expenses
    		}
    	}    	
    }
    
    @Override
    public String toString() {
    	return getName();
    }
    
	public void addCondition(Condition condition) {
		condition.setCharacter(this);
		addListener(condition);
		getConditions().add(condition);
		condition.init();
		getCache().clear();
        fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
	}
	
   public void removeCondition(Condition condition) {
        getConditions().remove(condition);
        getCache().clear();
        fireEvent(new MyEvent(EventType.STATUSCHANGE, this));
    }
	
	public long calculateValue() {
		long baseValue = 0;
		double expValue = 0.1;
		for (SpecializationType specializationType : getSpecializations()) {
			expValue *= 1.005;
			baseValue += 150;
			float divideBy = specializationType.getAssociatedAttributes().size() - ((specializationType.getAssociatedAttributes().size()-1)*0.2f);
			
			for (AttributeType attributeType : specializationType.getAssociatedAttributes()) {
				Attribute attribute = getAttribute(attributeType);
				double mod;
				if (specializationType == SpecializationType.SLAVE || specializationType == SpecializationType.TRAINER) {
					baseValue += (attribute.getInternValue() - 5) * 150;
					mod = (attribute.getInternValue() / 40.0f) + 1;
				}
				else {
					baseValue += attribute.getInternValue() * 30;
					mod = (attribute.getInternValue() / 1500.0f) / divideBy + 1;
				}
				expValue = expValue * mod;
			}
		}
		long value = (long) (baseValue + baseValue * expValue) - 250;
		
		getTraits(); //ensure traits is initialized
		for (Trait trait : traits) {
			value += trait.getValueModifier();
		}
		
		if (value < 100) {
			value = 100;
		}
		return value;
	}

	public List<Trait> getTraits() {
		if (traits == null) {
			traits = new ArrayList<Trait>();
	        for (Trait trait : getBase().getTraits()) {
	            addTrait(trait);
	        }
		}
		List<Trait> traitsLocal = new ArrayList<Trait>(traits);
		for (Equipment equipment : getCharacterInventory().listEquipment()) {
			equipment.modifyTraits(traitsLocal, this);
		}
		return traitsLocal;
	}
	
	public List<Trait> getTraitsInternal() {
	    getTraits();
	    return new ArrayList<Trait>(traits);
	}
	
	public Ownership getOwnership() {
		if (ownership == null) {
			ownership = Ownership.OWNED;
		}
		return ownership;
	}
	
	public void setOwnership(Ownership ownership) {
		this.ownership = ownership;
	}
	
	public boolean canSell() {
		return getOwnership().isSellable();
	}
	
	public void setUsesContraceptives(boolean usesContraceptives) {
	    this.usesContraceptives = usesContraceptives;
	}
	

	
    public Boolean isUsesContraceptives() {
        if (usesContraceptives == null) {
            usesContraceptives = true;
        }
        return usesContraceptives;
    }
    


    public CharacterInventory getCharacterInventory() {
    	if (characterInventory == null) {
    		characterInventory = new CharacterInventory(this);
    	}
		return characterInventory;
	}

	public float getMoneyModifier() {
    	float modifier = 1;
    	modifier = getActivity().getSource().getMoneyModifier(modifier, this);
    	for (SpecializationType specializationType : getSpecializations()) {
    		modifier = specializationType.getMoneyModifier(modifier, this);
    	}
    	for (Condition condition : getConditions()) {
    		modifier = condition.getMoneyModifier(modifier, this);
    	}
    	return modifier;
    }
    
    public ArrayList<ImageTag> getBaseTags() {
        ArrayList<ImageTag> imageTags = new ArrayList<ImageTag>();
        if (specializations.contains(SpecializationType.CATGIRL)) {
            imageTags.add(ImageTag.CATGIRL);
        }
        
        if (getCharacterInventory().getItem(EquipmentSlot.DRESS) != null || 
                getCharacterInventory().getItem(EquipmentSlot.UNDERWEAR) != null) {
            imageTags.add(ImageTag.CLOTHED);
        }
        else {
            imageTags.add(ImageTag.NAKED);
        }
        
        for (Condition condition : getConditions()) {
            condition.modifyImageTags(imageTags);
        }
        
        for (Equipment equipment : getCharacterInventory().listEquipment()) {
            equipment.modifyImageTags(imageTags);
        }       

    	return imageTags;
    }
    
    public int getRealMinObedience(int minObedience, RunningActivity activity) {
    	if (getType() == CharacterType.TRAINER || minObedience == 0) {
    		return 0;
    	}
    	else {
    		for (SpecializationType specializationType : getSpecializations()) {
    			minObedience = specializationType.getMinObedienceModified(minObedience, this, activity);
    		}
    		for (Condition condition : getConditions()) {
    			minObedience = condition.getMinObedienceModified(minObedience, this, activity);
    		}
    		for (Trait trait : getTraits()) {
    			minObedience = trait.getMinObedienceModified(minObedience, this, activity);
    		}
    	}
    	return minObedience;
    }
    
    @Override
	public int getHitpoints() {
		return getHealth();
	}

	@Override
	public int getMaxHitpoints() {
		return getAttribute(EssentialAttributes.HEALTH).getMaxValue();
	}

	@Override
	public float modifyHitpoints(float modifier) {
		return getAttribute(EssentialAttributes.HEALTH).addToValue(modifier);
	}

	@Override
	public float getDamage() {
        Float cachedValue = (Float) getCachedValue(CalculatedAttribute.DAMAGE);
        if (cachedValue != null) {
            return cachedValue;
        }
	    
    	float power =  0.5f + getStrength() / 40f;
    	power += getFinalValue(SpecializationAttribute.VETERAN) / 250f;
    	
    	power = (float) (getAttributeModified(CalculatedAttribute.DAMAGE, power));
    	
        getCache().put(CalculatedAttribute.DAMAGE, power);
		return power;
	}
	
	public int getArmorValue() {
	    return getIntCalculatedAttribute(CalculatedAttribute.ARMORVALUE, 0);
	}

	@Override
	public int getArmor() {
        Integer cachedValue = (Integer) getCachedValue(CalculatedAttribute.ARMORPERCENT);
        if (cachedValue != null) {
            return cachedValue;
        }
	    
		int amount = (int) (getStrength() / 5 + getFinalValue(SpecializationAttribute.VETERAN) / 10f);
		amount += getArmorValue();
		
		float value = 0.25f;
		float mitigation = 1;
		do {
			mitigation += value;
			amount--;
			value = value / 1.0075f;
		}
		while (amount > 0);
		value = (int) getAttributeModified(CalculatedAttribute.ARMORPERCENT, mitigation);
		
		getCache().put(CalculatedAttribute.ARMORPERCENT, ((int) mitigation));
		return (int) mitigation;
	}
	
    @Override
    public Attack getAttack(Battle battle) {
        List<Attack> attacks = getPossibleAttacks(battle);
        int sum = 0;
        for (Attack attack : attacks) {
            sum += attack.getSelectionModifier();
        }
        int rnd = Util.getInt(0, sum);
        sum = 0;
        for (Attack attack : attacks) {
            sum += attack.getSelectionModifier();
            if (sum >= rnd) {
                return attack;
            }
        }       
        return attacks.get(0);
    }
    
    public List<Attack> getPossibleAttacks(Battle battle) {
        List<Attack> attacks = new ArrayList<Attack>();
        attacks.add(new Attack.StandardAttack(this));
        
        for (Trait trait : getTraits()) {
            trait.modifyPossibleAttacks(attacks, this);
        }
        
        if (attacks.size() == 0) {
            attacks.add(new Attack.StandardAttack(this));
        }
        return attacks;
    }
	
	@Override
	public int getHit() {
	    return getIntCalculatedAttribute(CalculatedAttribute.HIT, 0);
	}
	
	@Override
	public int getDodge() {
	    return getIntCalculatedAttribute(CalculatedAttribute.DODGE, 1);
	}
	
    @Override
    public int getSpeed() {
        return getIntCalculatedAttribute(CalculatedAttribute.SPEED, 10);
    }
    
    @Override
    public int getCritChance() {
        return getIntCalculatedAttribute(CalculatedAttribute.CRITCHANCE, getIntelligence() / 10);
    }
    
    @Override
    public int getCritDamageBonus() {
        return getIntCalculatedAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 30);
    }

    @Override
    public int getBlockChance() {
        return getIntCalculatedAttribute(CalculatedAttribute.BLOCKCHANCE, 0);

    }

    @Override
    public int getBlockAmount() {
        return getIntCalculatedAttribute(CalculatedAttribute.BLOCKAMOUNT, 30);
    }
    
    public int getMinChildren() {
        return getIntCalculatedAttribute(CalculatedAttribute.MINCHILDREN, 1);
    }
    
    public int getMaxChildren() {
        return getIntCalculatedAttribute(CalculatedAttribute.MAXCHILDREN, 8);
    }
    
    public int getPregnancyChance() {
        return getIntCalculatedAttribute(CalculatedAttribute.PREGNANCYCHANCE, 10);
    }
    
    public int getChanceAdditionalChild() {
        return getIntCalculatedAttribute(CalculatedAttribute.CHANCEADDITIONALCHILD, 10);
    }
    
    public int getPregnancyDurationModifier() {
        return getIntCalculatedAttribute(CalculatedAttribute.PREGNANCYDURATIONMODIFIER, 100);
    }    
	
	@Override
	public float takeDamage(float damage) {
	    return modifyHitpoints(- damage);
	}
	
    public int getStealChance() {
        return getIntCalculatedAttribute(CalculatedAttribute.STEALCHANCE, 1 + getFinalValue(SpecializationAttribute.PICKPOCKETING) / 5);
    }
    
    public int getStealAmountModifier() {
        return getIntCalculatedAttribute(CalculatedAttribute.STEALAMOUNTMODIFIER, 20 + getFinalValue(SpecializationAttribute.PICKPOCKETING) / 5);
    }
    
    public int getStealItemChance() {
        return getIntCalculatedAttribute(CalculatedAttribute.STEALITEMCHANCE, 1 + getFinalValue(SpecializationAttribute.PICKPOCKETING) / 20);
    }
    
    public int getItemLootChanceModifier() {
        return getIntCalculatedAttribute(CalculatedAttribute.ITEMLOOTCHANCEMODIFIER, 30);
    }
    
    @Override
    public int getResistance(DamageType damageType) {
        CalculatedAttribute calculatedAttribute = null;
        switch (damageType) {
        case FIRE:
            calculatedAttribute = CalculatedAttribute.FIRERESISTANCE;
            break;
        case WATER:
            calculatedAttribute = CalculatedAttribute.WINDRESISTANCE;
            break;
        case WIND:
            calculatedAttribute = CalculatedAttribute.WINDRESISTANCE;
            break;
        case EARTH:
            calculatedAttribute = CalculatedAttribute.EARTHRESISTANCE;
            break;
        case MAGIC:
            calculatedAttribute = CalculatedAttribute.MAGICRESISTANCE;
            break;
        case HOLY:
            calculatedAttribute = CalculatedAttribute.HOLYRESISTANCE;
            break;
        case DARKNESS:
            calculatedAttribute = CalculatedAttribute.DARKNESSRESISTANCE;
            break;
        default:
            break;
        }
        
        if (calculatedAttribute != null) {
            return getIntCalculatedAttribute(calculatedAttribute, 0);
        }
        else {
            return 0;
        }
    }
    
    private int getIntCalculatedAttribute(CalculatedAttribute calculatedAttribute, int startValue) {
        Integer cachedValue = (Integer) getCachedValue(calculatedAttribute);
        if (cachedValue != null) {
            return cachedValue;
        }
        int value = Math.max(0, (int) getAttributeModified(calculatedAttribute, startValue));
        getCache().put(calculatedAttribute, value);
        return value;
    }
	
    
    public Float getPossibleAmountCustomers() {
        Float cachedValue = (Float) getCachedValue(CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT);
        if (cachedValue != null) {
            return cachedValue;
        }
        float amount = ((getStrength() + getStamina()) / 12) + 2f;
        amount = (float) getAttributeModified(CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT, amount);
        getCache().put(CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT, amount);
        return amount; 
    }
    
    public int getControl() {
        Integer cachedValue = (Integer) getCachedValue(CalculatedAttribute.CONTROL);
        if (cachedValue != null) {
            return cachedValue;
        }
        int startValue = 0;
        int value = 0;
        if (getOwnership() != Ownership.NOTOWNED && getOwnership() != Ownership.NOTOWNEDCANSELL) {
            if (getType() == CharacterType.TRAINER) {
                startValue = 10;
                startValue += getCommand();
                if (Jasbro.getInstance().getData().getProtagonist() == this) {
                    startValue *= 2;
                }
                value = (int) getAttributeModified(CalculatedAttribute.CONTROL, startValue, false);
            }
            else if (getType() == CharacterType.SLAVE) {
                startValue = (getObedience() - 110) / 10;
                value = (int) getAttributeModified(CalculatedAttribute.CONTROL, startValue, false);
            }            
        }
        getCache().put(CalculatedAttribute.CONTROL, value);
        return value;
    }

	public AllowedServices getAllowedServices() {
		if (allowedServices == null) {
			DefaultPreferences preferences = Jasbro.getInstance().getData().getDefaultPreferences();
			if (getGender() == Gender.FEMALE) {
				allowedServices = new AllowedServices(preferences.getAllowedServicesFemale());
			}
			else if (getGender() == Gender.MALE) {
				allowedServices = new AllowedServices(preferences.getAllowedServicesMale());
			}
			else {
				allowedServices = new AllowedServices(preferences.getAllowedServicesFuta());
			}
		}
		return allowedServices;
	}
	

	
	public void setAllowedServices(AllowedServices allowedServices) {
		this.allowedServices = allowedServices;
	}
	
    public void resetAttributeCache(AttributeType attributeType) {
        getCache().remove(attributeType);
    }
    
    private Object getCachedValue(AttributeType attributeType) {
        if (getCache().containsKey(attributeType)) {
            return getCache().get(attributeType);
        }
        else {
            return null;
        }
    }

    private Map<Object, Object> getCache() {
        if (cache == null) {
            cache = new HashMap<Object, Object>();
        }
        return cache;
    }

    public int getBonusPerks() {
        return bonusPerks;
    }

    public void addBonusPerk() {
        bonusPerks++;
    }
    
    public int getPerkPoints() {        
        return PerkHandler.getSkillPoints(this);
    }
    
    public int getUnspentPerkPoints() {
        return PerkHandler.getSkillPoints(this) - PerkHandler.getUsedSkillPoints(this);
    }
    
    public List<SkillTree> getSkillTrees() {
        return PerkHandler.getSkillTrees(this);
    }
    
    private double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue) {
        return getAttributeModified(calculatedAttribute, currentValue, true);
    }
    
    private double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, boolean zeroIsMin) {
        for (Trait trait : getTraits()) {
            currentValue = trait.getAttributeModified(calculatedAttribute, currentValue, this);
        }
        for (Condition condition : getConditions()) {
            currentValue = condition.modifyCalculatedAttribute(calculatedAttribute, currentValue, this);
        }
        for (Equipment equipment : getCharacterInventory().listEquipment()) {
            currentValue = equipment.modifyCalculatedAttribute(calculatedAttribute, currentValue, this);
        }
        if (currentValue < 0 && zeroIsMin) {
            return 0;
        }
        else {
            return currentValue;
        }        
    }
    
    
    /*
     * Character warnings
     */
    public ImageData getWarnImage() {
        List<Warning> warnings = getWarnings();
        return warnings.get(0).getIcon();
    }
    
    public String getWarnString() {
        List<Warning> warnings = getWarnings();
        String warningString = "";
        for (Warning warning : warnings) {
            warningString += warning.getMessage() + "\n";
        }
        return warningString;
    }
    
    public Severity getWarnLevel() {
        return getWarnings().get(0).getSeverity();
    }
    
    @SuppressWarnings("unchecked")
    public List<Warning> getWarnings() {
        if (getCache().containsKey(Warning.class)) {
            return (List<Warning>) cache.get(Warning.class);
        }

        
        List<Warning> warnings = new ArrayList<Warning>();
        PlannedActivity activity = getActivity();
        if (activity != null) {
        	if (activity.getType() == ActivityType.IDLE) {
                warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.idle", this)));
            }
            
            Object arguments[] = {activity.getActivityDetails().getText(), null};
            if (activity.getSource() instanceof Room) {
                Room room = (Room) activity.getSource();
                arguments[1] = room.getHouse();
            }
            else if (activity.getSource() != null) {
            	arguments[1] = activity.getSource().getName();
            }
            warnings.add(new Warning(Severity.ACTIVITY, TextUtil.t("warnings.activity", this, arguments)));
            
            if (getType() == CharacterType.TRAINER && !getTraits().contains(Trait.ONEOFUS)) {
                if (activity.getType() == ActivityType.CLEAN || activity.getType() == ActivityType.BARTEND || activity.getType() == ActivityType.BATHATTENDANT) {
                    boolean start = false;
                    for (Condition condition : getConditions()) {
                        if (condition instanceof StartingAtTheBottom) {
                            start = true;
                            break;
                        }
                    }
                    if (!start) {
                        warnings.add(new Warning(Severity.WARN, TextUtil.t("warnings.detrimentalCommand", this, arguments)));
                    }
                }
                else if (activity.getType() == ActivityType.WHORE || activity.getType() == ActivityType.SUBMIT ||
                        activity.getType() == ActivityType.SUBMITTOMONSTER || activity.getType() == ActivityType.STRIP ||
                        activity.getType() == ActivityType.TEASE) {
                    warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.catastrophicCommand", this, arguments)));
                }
            }
        }
        else {
            warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.idle", this)));
        }
        
        if (getHealth() < 20) {
            warnings.add(new Warning(Severity.DANGER, TextUtil.t("warnings.healthCritical", this)));
        }
        else if (getHealth() < 40) {
            warnings.add(new Warning(Severity.WARN, TextUtil.t("warnings.healthLow", this)));
        }
        
        for (Condition condition : getConditions()) {
            condition.modifyWarnings(warnings);
        }
        
        Collections.sort(warnings, Collections.reverseOrder());        getCache().put(Warning.class, warnings);
        return warnings;
    }
}
