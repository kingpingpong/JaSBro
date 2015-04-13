package jasbro.game.character.battle;

import jasbro.game.character.Condition;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.conditions.BattleCondition;
import jasbro.game.events.MyEvent;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class Enemy implements Unit {
    private List<BattleCondition> conditions = new ArrayList<BattleCondition>();
    private Map<CalculatedAttribute, Double> attributeMap = new EnumMap<>(CalculatedAttribute.class);
    private int hitpoints = 100;
    private int maxHitpoints = 100;
    private boolean combatInitialized = false;
    
    public abstract void initCombat();
    
    private synchronized void initializeCombat() {
        if (!combatInitialized) {
            combatInitialized = true;
            initCombat();
        }
    }
    
    @Override
    public void handleEvent(MyEvent event) {
        List<BattleCondition> conditions = new ArrayList<BattleCondition>(this.conditions);
        for (Condition condition : conditions) {
            condition.handleEvent(event);
        }
    }
    
    @Override
    public void addCondition(Condition condition) {
        if (condition instanceof BattleCondition) {
            conditions.add((BattleCondition) condition);
        }
    }
    
    @Override
    public void removeCondition(Condition condition) {
        conditions.remove(condition);
    }
    
    @Override
    public int getDodge() {
        return (int) getAttribute(CalculatedAttribute.DODGE);
    }
    @Override
    public int getHit() {
        return (int) getAttribute(CalculatedAttribute.HIT);
    }
    
    @Override
    public int getSpeed() {
        return (int) getAttribute(CalculatedAttribute.SPEED);
    }
    
    @Override
    public int getCritDamageBonus() {
        return (int) getAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT);
    }
    @Override
    public int getBlockChance() {
        return (int) getAttribute(CalculatedAttribute.BLOCKCHANCE);
    }
    @Override
    public int getBlockAmount() {
        return (int) getAttribute(CalculatedAttribute.BLOCKAMOUNT);
    }
    
    @Override
    public Attack getAttack(Battle battle) {
        return new Attack(this);
    }
    
    @Override
    public float getDamage() {
        return (float) getAttribute(CalculatedAttribute.DAMAGE);
    }
    
    @Override
    public int getArmor() {
        return (int) getAttribute(CalculatedAttribute.ARMORPERCENT);
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
            return (int) getAttribute(calculatedAttribute);
        }
        else {
            return 0;
        }
    }
    
    public double getAttribute(CalculatedAttribute calculatedAttribute) {
        initializeCombat();
        double value;
        if (attributeMap.containsKey(calculatedAttribute)) {
            value = attributeMap.get(calculatedAttribute);
        }
        else {
            value = 0d;
        }
        for (Condition condition : conditions) {
            value = condition.modifyCalculatedAttribute(calculatedAttribute, value, this);
        }
        return value;
    }
    
    public void setAttribute(CalculatedAttribute attribute, Double value) {
        attributeMap.put(attribute, value);
    }
    

    @Override
    public int getHitpoints() {
        initializeCombat();
        return hitpoints;
    }
    @Override
    public int getMaxHitpoints() {
        initializeCombat();
        return maxHitpoints;
    }
    @Override
    public float modifyHitpoints(float modifier) {
        hitpoints += modifier;
        return modifier;
    }
    
    @Override
    public float takeDamage(float power) {
        return modifyHitpoints(-power);
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public void setMaxHitpoints(int maxHitpoints) {
        this.maxHitpoints = maxHitpoints;
    }
    
    @Override
    public int getCritChance() {
        return (int) getAttribute(CalculatedAttribute.CRITCHANCE);
    }
}
