package jasbro.game.character.battle;

import jasbro.game.character.Condition;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;


/**
 * 
 * @author Azrael
 */
public interface Unit extends Person {
  
  public int getHitpoints();
  public int getMaxHitpoints();
  public float modifyHitpoints(float modifier);
  public float getDamage();
  public int getArmor();
  public float takeDamage(float power);
  public int getDodge();
  public int getHit();
  public int getSpeed();
  public int getCritChance();
  public int getCritDamageBonus();
  public int getBlockChance();
  public int getBlockAmount();  
  public Attack getAttack(Battle battle);
  public void handleEvent(MyEvent event);
  public int getResistance(DamageType damageType);
  public void addCondition(Condition condition);
  public void removeCondition(Condition condition);
}
