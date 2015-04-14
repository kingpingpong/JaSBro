package jasbro.game.character.attributes;

import jasbro.game.interfaces.AttributeType;
import jasbro.texts.TextUtil;

public enum CalculatedAttribute implements AttributeType {
    DAMAGE, ARMORVALUE, ARMORPERCENT, 
    HIT, DODGE, 
    SPEED,
    BLOCKCHANCE, BLOCKAMOUNT,
    CRITCHANCE, CRITDAMAGEAMOUNT,
    SKILLPOINTS, 
    AMOUNTCUSTOMERSPERSHIFT,
    ITEMLOOTCHANCEMODIFIER, 
    STEALCHANCE, STEALAMOUNTMODIFIER, STEALITEMCHANCE,
    FIRERESISTANCE, WATERRESISTANCE, WINDRESISTANCE, EARTHRESISTANCE, LIGHTNINGRESISTANCE, MAGICRESISTANCE, 
    HOLYRESISTANCE, DARKNESSRESISTANCE,
    PREGNANCYCHANCE, MINCHILDREN, MAXCHILDREN, CHANCEADDITIONALCHILD,
    PREGNANCYDURATIONMODIFIER,
    CONTROL;

    @Override
    public String getText() {
        return TextUtil.t(this.toString());
    }

    @Override
    public int getDefaultMin() {
        return 0;
    }

    @Override
    public int getDefaultMax() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getRaiseMaxBy() {
        return 0;
    }
    
    @Override
    public int getStartValue() {
        return 0;
    }
}
