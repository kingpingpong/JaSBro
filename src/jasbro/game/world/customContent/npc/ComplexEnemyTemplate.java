package jasbro.game.world.customContent.npc;

import jasbro.game.character.Gender;
import jasbro.game.character.attributes.CalculatedAttribute;

import java.util.ArrayList;
import java.util.List;

import net.java.truevfs.access.TFile;

public class ComplexEnemyTemplate extends ComplexEnemy {
    private transient String id;
    private transient TFile file;
    private List<EnemySpawnData> spawnDataList = new ArrayList<EnemySpawnData>();

    
    public ComplexEnemyTemplate() {
    }
    
    public ComplexEnemyTemplate(String id) {
        this.id = id;
        
        //First time init
        setHitpoints(100);
        setGender(Gender.MALE);
        setAttribute(CalculatedAttribute.DAMAGE, 1d);
        setAttribute(CalculatedAttribute.ARMORPERCENT, 5d);
        setAttribute(CalculatedAttribute.DODGE, 5d);
        setAttribute(CalculatedAttribute.CRITCHANCE, 5d);
        setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25d);
    }
    
    /**
     * Generates a copy for use in combat
     * @return
     */
    public ComplexEnemy generateEnemy() {
        ComplexEnemy complexEnemy = new ComplexEnemy();
        
        complexEnemy.setName(getName());
        complexEnemy.setGender(getGender());
        complexEnemy.setHitpoints(getHitpoints());
        complexEnemy.setImages(getImages());
        complexEnemy.setDescription(getDescription());
        complexEnemy.setCharacterBaseId(getCharacterBaseId());
        
        for (CalculatedAttribute attribute : CalculatedAttribute.values()) {
            double value = getAttribute(attribute);
            if (value != 0) {
                complexEnemy.setAttribute(attribute, value);
            }
        }
        
        complexEnemy.getEncounterTexts().addAll(getEncounterTexts());
        complexEnemy.getRapeTexts().addAll(getRapeTexts());
        
        return complexEnemy;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public TFile getFile() {
        return file;
    }
    public void setFile(TFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return id;
    }

    public List<EnemySpawnData> getSpawnDataList() {
        return spawnDataList;
    }

    public void setSpawnDataList(List<EnemySpawnData> spawnDataList) {
        this.spawnDataList = spawnDataList;
    }
    

    
}
