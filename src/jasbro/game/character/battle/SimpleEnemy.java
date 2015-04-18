package jasbro.game.character.battle;

import jasbro.game.character.Gender;
import jasbro.game.character.attributes.CalculatedAttribute;

public class SimpleEnemy extends Enemy {
    private Gender gender;
    private String name;
    
    public SimpleEnemy() {
    }
    
    public SimpleEnemy(String name, 
            Gender gender, 
            int hitpoints,
            double damage,
            double armorPercent,
            double dodge,
            double hit,
            double critChance,
            double critAmount,
            double blockChance,
            double blockAmount,
            double speed) {
        this.name = name;
        this.gender = gender;
        setHitpoints(hitpoints);
        setAttribute(CalculatedAttribute.DAMAGE, damage);
        setAttribute(CalculatedAttribute.BLOCKCHANCE, blockChance);
        setAttribute(CalculatedAttribute.BLOCKAMOUNT, blockAmount);
        setAttribute(CalculatedAttribute.ARMORPERCENT, armorPercent);
        setAttribute(CalculatedAttribute.CRITCHANCE, critChance);
        setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, critAmount);
        setAttribute(CalculatedAttribute.DODGE, dodge);
        setAttribute(CalculatedAttribute.HIT, hit);
        setAttribute(CalculatedAttribute.SPEED, speed);
    }
    
    public Gender getGender() {
        return gender;
    }
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public void initCombat() {
    }
    
    
}
