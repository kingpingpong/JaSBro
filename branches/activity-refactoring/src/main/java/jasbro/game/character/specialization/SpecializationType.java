package jasbro.game.character.specialization;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.MinObedienceModifier;
import jasbro.game.interfaces.MoneyEarnedModifier;
import jasbro.game.interfaces.MyCharacterEventListener;
import jasbro.game.interfaces.UnlockObject;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public enum SpecializationType implements MoneyEarnedModifier, MinObedienceModifier, UnlockObject, MyCharacterEventListener {

    SEX, 
    KINKYSEX, 
    TRAINER, 
    SLAVE, 
    UNDERAGE, 
    WHORE, 
    MAID, 
    FIGHTER, 
    BARTENDER, 
    NURSE, 
    DANCER, 
    DOMINATRIX, 
    THIEF(new ThiefEventHandler()), 
    CATGIRL, 
    MARKETINGEXPERT, 
    MUTANT(new MutantEventHandler(), false);

    private final static Logger log = Logger.getLogger(SpecializationType.class);
    private boolean locked = false;
    private boolean teachable = true;
    private MyCharacterEventListener eventListener = null;
    
    private SpecializationType() {
    }
    
    private SpecializationType(boolean teachable) {
        this.teachable = teachable;
    }
    
    private SpecializationType(MyCharacterEventListener eventListener) {
        this.eventListener = eventListener;
    }
    
    private SpecializationType(MyCharacterEventListener eventListener, boolean teachable) {
        this.teachable = teachable;
        this.eventListener = eventListener;
    }

    public List<AttributeType> getAssociatedAttributes() {
        List<AttributeType> attributes = new ArrayList<AttributeType>();
        if (this == SEX) {
            attributes.add(Sextype.VAGINAL);
            attributes.add(Sextype.ANAL);
            attributes.add(Sextype.ORAL);
            attributes.add(Sextype.TITFUCK);
            attributes.add(Sextype.FOREPLAY);
        } else if (this == KINKYSEX) {
            attributes.add(Sextype.BONDAGE);
            attributes.add(Sextype.MONSTER);
            attributes.add(Sextype.GROUP);
        } else if (this == TRAINER) {
            attributes.add(BaseAttributeTypes.CHARISMA);
            attributes.add(BaseAttributeTypes.COMMAND);
            attributes.add(BaseAttributeTypes.STAMINA);
            attributes.add(BaseAttributeTypes.INTELLIGENCE);
            attributes.add(BaseAttributeTypes.STRENGTH);
        } else if (this == SLAVE) {
            attributes.add(BaseAttributeTypes.CHARISMA);
            attributes.add(BaseAttributeTypes.OBEDIENCE);
            attributes.add(BaseAttributeTypes.STAMINA);
            attributes.add(BaseAttributeTypes.INTELLIGENCE);
            attributes.add(BaseAttributeTypes.STRENGTH);
        } else if (this == UNDERAGE) {
            attributes.add(BaseAttributeTypes.CHARISMA);
            attributes.add(BaseAttributeTypes.STAMINA);
            attributes.add(BaseAttributeTypes.INTELLIGENCE);
            attributes.add(BaseAttributeTypes.STRENGTH);
        } else if (this == MAID) {
            attributes.add(SpecializationAttribute.CLEANING);
            attributes.add(SpecializationAttribute.COOKING);
        } else if (this == WHORE) {
            attributes.add(SpecializationAttribute.SEDUCTION);
        } else if (this == FIGHTER) {
            attributes.add(SpecializationAttribute.VETERAN);
        } else if (this == BARTENDER) {
            attributes.add(SpecializationAttribute.BARTENDING);
        } else if (this == NURSE) {
            attributes.add(SpecializationAttribute.MEDICALKNOWLEDGE);
            attributes.add(SpecializationAttribute.WELLNESS);
        } else if (this == DANCER) {
            attributes.add(SpecializationAttribute.STRIP);
        } else if (this == DOMINATRIX) {
            attributes.add(SpecializationAttribute.DOMINATE);
            attributes.add(Sextype.BONDAGE);
        } else if (this == THIEF) {
            attributes.add(SpecializationAttribute.PICKPOCKETING);
        } else if (this == CATGIRL) {
            attributes.add(SpecializationAttribute.CATGIRL);
        } else if (this == MARKETINGEXPERT) {
            attributes.add(SpecializationAttribute.ADVERTISING);
        } else if (this == MUTANT) {
            attributes.add(SpecializationAttribute.GENETICADAPTABILITY);
        }
        else {
            log.error("No associated attributes: " + toString());
        }
        return attributes;
    }

    public String getText() {
        return TextUtil.t(this.toString());
    }

    public int getTrainingLevel(Charakter character) {
        int sumMaxChange = 0;

        for (AttributeType attributeType : getAssociatedAttributes()) {
            sumMaxChange += character.getAttribute(attributeType).getMaxValue() - attributeType.getDefaultMax();
        }

        return 1 + (sumMaxChange / getAssociatedAttributes().size()) / getAssociatedAttributes().get(0).getRaiseMaxBy();
    }

    @Override
    public float getMoneyModifier(float currentModifier, Charakter character) {
        if (this == CATGIRL) {
            return currentModifier * (1 + character.getFinalValue(SpecializationAttribute.CATGIRL) / 100.0f);
        } else {
            return currentModifier;
        }
    }

    @Override
    public int getMinObedienceModified(int minObedience, Charakter character, RunningActivity activity) {
        if (this == CATGIRL) {
            return minObedience + character.getFinalValue(SpecializationAttribute.CATGIRL) / 5;
        } else {
            return minObedience;
        }
    }

    public void handleEvent(MyEvent e, Charakter character) {
        if (eventListener != null) {
            eventListener.handleEvent(e, character);
        }
    }

    public SkillTree getAssociatedSkillTree() {
        for (SkillTree skillTree : SkillTree.values()) {
            if (skillTree.toString().equals(this.toString())) {
                return skillTree;
            }
        }
        return null;
    }

    @Override
    public String getDescription() {
        return ""; //TODO
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public ImageData getImage() {
        return getAssociatedSkillTree().getIcon();
    }

    @Override
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isTeachable() {
        return teachable;
    }

    public void setTeachable(boolean teachable) {
        this.teachable = teachable;
    }
    
    
}

