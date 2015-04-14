package jasbro.game.character.battle;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

public class Defender {
    private Unit unit;
    private int armorPercent;
    private int blockChance;
    private int blockAmount;
    private int dodge;
    private boolean blockSuccessful = false;
    private Map<DamageType, Integer> resistances = new EnumMap<>(DamageType.class);
    
    public Defender(Unit unit) {
        this.unit = unit;
        this.armorPercent = unit.getArmor();
        this.blockChance = unit.getBlockChance();
        this.blockAmount = unit.getBlockAmount();
        this.dodge = unit.getDodge();
        
        for (DamageType damageType : DamageType.values()) {
            if (damageType != DamageType.REGULAR) {
                resistances.put(damageType, unit.getResistance(damageType));
            }
        }
    }
    
    public float takeAttack(Attack attack) {
        float damageSum = 0;
        
        for (Entry<DamageType, Float> damageData : attack.getDamageMap().entrySet()) {
            float damage = damageData.getValue();
            if (attack.isCrit()) {
                damage = damage + damage * attack.getCritBonus() / 100f;
            }
            if (damageData.getKey() == DamageType.REGULAR) {
                if (isBlockSuccessful()) {
                    damage = damage - damage * blockAmount / 100f;
                }                
                damage = damage - damage * armorPercent / 100f;
                if (damage < 0) {
                    damage = 0;
                }
            }
            else {
                if (damage > 0) {
                    int resistance = resistances.get(damageData.getKey());
                    if (resistance < 200) {
                        damage = damage - damage * resistances.get(damageData.getKey()) / 100f;
                        if (damage < 0) {
                            damage = 0;
                        }
                    }
                    else { //resistance over 200 heals character instead
                        int bonus = resistance - 200;
                        if (bonus > 100) {
                            bonus = 100;
                        }
                        damage = - damage * resistances.get(damageData.getKey()) / 100f;
                    }
                }
            }            
            damageSum += damage;
        }
        
        unit.takeDamage(damageSum);
        return damageSum;
    }

    public int getBlockChance() {
        return blockChance;
    }

    public void setBlockChance(int blockChance) {
        this.blockChance = blockChance;
    }

    public int getBlockAmount() {
        return blockAmount;
    }

    public void setBlockAmount(int blockAmount) {
        this.blockAmount = blockAmount;
    }

    public int getDodge() {
        return dodge;
    }

    public void setDodge(int dodgeChance) {
        this.dodge = dodgeChance;
    }

    public Unit getUnit() {
        return unit;
    }

    public int getArmorPercent() {
        return armorPercent;
    }

    public void setArmorPercent(int armorPercent) {
        this.armorPercent = armorPercent;
    }

    public boolean isBlockSuccessful() {
        return blockSuccessful;
    }

    public void setBlockSuccessful(boolean blockSuccessful) {
        this.blockSuccessful = blockSuccessful;
    }

    public int getHitpoints() {
        return unit.getHitpoints();
    }

    public Map<DamageType, Integer> getResistances() {
        return resistances;
    }
}
