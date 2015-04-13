package jasbro.game.character.battle;

import jasbro.game.character.Charakter;

import java.util.HashMap;
import java.util.Map;


public class Attack {
    private Map<DamageType, Float> damageMap = new HashMap<DamageType, Float>();
    private boolean dodgeable = true;
    private boolean blockable = true;
    private boolean canCrit = true;
    private boolean isCrit = false;
    private int hit;
    private int critChance;
    private int critBonus;
    private int selectionModifier = 10;
    private boolean abort = false;
    
    private String attackMessageKey = "fight.combatText";
    private String hitMessageKey = "fight.combatText.hit";
    
    public Attack() {
    }
    
    public Attack(DamageType damageType, Float damage, boolean dodgeable, boolean blockable, int hit, int critChance, int critBonus) {
        super();
        this.damageMap.put(damageType, damage);
        this.dodgeable = dodgeable;
        this.blockable = blockable;
        this.hit = hit;
        this.critChance = critChance;
        this.critBonus = critBonus;
    }
    
    public Attack(Unit unit) {
        this.damageMap.put(DamageType.REGULAR, unit.getDamage());
        this.hit = unit.getHit();
        this.critChance = unit.getCritChance();
        this.critBonus = unit.getCritDamageBonus();
    }
    
    public void addDamageModifier(DamageType damageType, Float damage) {
        if (damageMap.containsKey(damageType)) {
            damageMap.put(damageType, damageMap.get(damageType) + damage);
        }
        else {
            damageMap.put(damageType, damage);
        }
    }
    
    public String getText() {
        return null; //TODO
    }
    
    public void attackHits(Defender target, Battle battle) {
    }

    public boolean isDodgeable() {
        return dodgeable;
    }

    public void setDodgeable(boolean dodgeable) {
        this.dodgeable = dodgeable;
    }

    public boolean isBlockable() {
        return blockable;
    }

    public void setBlockable(boolean blockable) {
        this.blockable = blockable;
    }

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public int getCritChance() {
        return critChance;
    }

    public void setCritChance(int critChance) {
        this.critChance = critChance;
    }

    public int getCritBonus() {
        return critBonus;
    }

    public void setCritBonus(int critBonus) {
        this.critBonus = critBonus;
    }
    
    public int getSelectionModifier() {
        return selectionModifier;
    }

    public void setSelectionModifier(int selectionModifier) {
        this.selectionModifier = selectionModifier;
    }

    public boolean isCanCrit() {
        return canCrit;
    }

    public void setCanCrit(boolean canCrit) {
        this.canCrit = canCrit;
    }

    public boolean isCrit() {
        return isCrit;
    }

    public void setCrit(boolean isCrit) {
        this.isCrit = isCrit;
    }

    public boolean isAbort() {
        return abort;
    }

    public void setAbort(boolean abort) {
        this.abort = abort;
    }

    public Map<DamageType, Float> getDamageMap() {
        return damageMap;
    }

    public String getAttackMessageKey() {
        return attackMessageKey;
    }

    public void setAttackMessageKey(String attackMessageKey) {
        this.attackMessageKey = attackMessageKey;
    }

    public String getHitMessageKey() {
        return hitMessageKey;
    }

    public void setHitMessageKey(String hitMessageKey) {
        this.hitMessageKey = hitMessageKey;
    }
    
    
    public static class StandardAttack extends Attack {

        public StandardAttack() {
        }

        public StandardAttack(DamageType damageType, Float damage, boolean dodgeable, boolean blockable, int hit, int critChance,
                int critBonus) {
            super(damageType, damage, dodgeable, blockable, hit, critChance, critBonus);
        }

        public StandardAttack(Unit unit) {
            super(unit);
        }        
    }
    
    public static class FlameBreath extends Attack {
        public FlameBreath(Charakter character) {
            float damage = 5f + character.getStrength() / 15f + character.getStamina() / 15f;
            getDamageMap().put(DamageType.FIRE, damage);
            setHit(character.getHit() + 10);
            setCrit(false);
            setSelectionModifier(2);
            setAttackMessageKey("FLAMEBREATH.attack");
        }
    }
    
    public static class ClawAttack extends Attack {

        public ClawAttack(Unit unit) {
            super(unit);
            getDamageMap().put(DamageType.REGULAR, getDamageMap().get(DamageType.REGULAR) * 1.5f);
            setAttackMessageKey("EXTRACTABLECLAWS.attack");
        }        
    }
}


