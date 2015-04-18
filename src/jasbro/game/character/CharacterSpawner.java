package jasbro.game.character;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.conditions.ItemCooldown;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.game.world.customContent.npc.ComplexEnemy;
import jasbro.game.world.customContent.npc.ComplexEnemyTemplate;
import jasbro.game.world.customContent.npc.EnemySpawnData;
import jasbro.game.world.customContent.npc.EnemySpawnLocation;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class CharacterSpawner {
    private final static Logger log = Logger.getLogger(CharacterSpawner.class);
    
    public static Charakter create(CharacterBase base) {
        return create(base, base.getType());
    }
    
    public static Charakter create(String baseId, CharacterType type) { //help method
        if (baseId == null) {
            return null;
        }
        for (CharacterBase base : Jasbro.getInstance().getCharacterBases()) {
            if (base.getId().toLowerCase().equals(baseId.toLowerCase())) {
                return create(base, type);
            }
        }
        return null;
    }
    
    /**
     * Method for creating a new Character from a character base
     * @param base
     * @return
     */
    public static Charakter create(CharacterBase base, CharacterType type) {
        Charakter character = new Charakter(base);
        
        if (type == null) {
            type = CharacterType.SLAVE;
        }
        
        if (type == CharacterType.SLAVE) {
            character.getSpecializations().add(SpecializationType.SLAVE);
        }
        else {
            character.getSpecializations().add(SpecializationType.TRAINER);
        }
        character.getSpecializations().add(SpecializationType.SEX);
        
        character.setName(base.getName());
        character.setGender(base.getGender());
        character.setType(type);
        character.setBaseId(base.getId());
        
        if (base.getInitialSpecialization() != null) {
            SpecializationType specializationType = base.getInitialSpecialization();
            character.getSpecializations().add(specializationType);
            for (AttributeType attributeType : specializationType.getAssociatedAttributes()) {
                character.getAttribute(attributeType).addToValue(10, true);
            }
        }
        
        for (Trait trait : base.getTraits()) {
            character.addTrait(trait);
        }
        
        character.setIcon(ImageUtil.getInstance().getImageDataByTag(ImageTag.ICON, character.getImages()));
        for (Entry<BaseAttributeTypes, Integer> entry : base.getAttributes()) {
            if (entry.getKey() != BaseAttributeTypes.OBEDIENCE || type != CharacterType.TRAINER) {
                character.addAttribute(new Attribute(BaseAttributeTypes.valueOf(entry.getKey().toString()), 
                        entry.getValue(), character));
            }
            else { //Trainers get the command attribute instead of obedience
                character.addAttribute(new Attribute(BaseAttributeTypes.COMMAND, 
                        entry.getValue(), character));
            }
        }
        character.getAttribute(EssentialAttributes.HEALTH).setInternValue(character.getAttribute(EssentialAttributes.HEALTH).getMaxValue());
        character.getAttribute(EssentialAttributes.ENERGY).setInternValue(character.getAttribute(EssentialAttributes.ENERGY).getMaxValue());
        return character;
    }
    
    public static Charakter spawnChild(Charakter mother, Person otherParent) {
        CharacterBase characterBase = selectCharacterBase(mother, otherParent);
        return spawnChild(mother, otherParent, characterBase);
    }
    
    public static Charakter spawnChild(Charakter mother, Person otherParent, CharacterBase characterBase) {
        Charakter character = create(characterBase);
        for (AttributeType attributeType : BaseAttributeTypes.values()) {
            character.getAttribute(attributeType).setInternValue(1);
        }
        character.getSpecializations().clear();
        CharacterManipulationManager.changeBase(character, CharacterManipulationManager.getInfantBase(character));
        CharacterManipulationManager.changeType(character, CharacterType.INFANT);
        character.getAgeProgressionData().setMother(new WeakReference<Charakter>(mother));
        character.getAgeProgressionData().setNameMother(mother.getName());
        if (otherParent != null) {
            character.getAgeProgressionData().setNameFather(otherParent.getName());
        }
        
        //Let's mix the traits:
        for (Trait trait : character.getTraits()) {
            if (Util.getRnd().nextBoolean()) {
                character.removeTrait(trait);
            }
        }
        for (Trait trait : mother.getTraitsInternal()) {
            if (!trait.isPerk() && Util.getRnd().nextBoolean()) {
                character.addTrait(trait);
            }
        }
        if (otherParent instanceof Charakter) {
            Charakter otherParentChar = (Charakter) otherParent;
            character.getAgeProgressionData().setFather(new WeakReference<Charakter>(otherParentChar));
            for (Trait trait : otherParentChar.getTraitsInternal()) {
                if (!trait.isPerk() && Util.getRnd().nextBoolean()) {
                    character.addTrait(trait);
                }
            }
        }
        
        /*
         * Inheritance Perk
         * Every Child becomes a mutant and gets a random specialization of the mother. 
         */
        for (Trait trait : mother.getTraits()) {
        	if (trait == Trait.INHERITANCE) {
                character.getSpecializations().add(SpecializationType.UNDERAGE);
                character.getSpecializations().add(SpecializationType.MUTANT);
                if (mother.getSpecializations().size() > 3) {
                	SpecializationType spec = null;
                	while (true) {
                		spec = getMotherSpec(mother);
                		if (null == spec) continue;
                		else if (SpecializationType.SEX != spec) break;
                		else if (SpecializationType.SLAVE != spec) break;
                		else if (SpecializationType.TRAINER != spec) break;
                	}
                	character.getSpecializations().add(spec);
                }
                
        	}
        } // Inheritance END
        
        character.addCondition(new ItemCooldown(0, "Tome_of_time"));
                
        return character;
    }
    
    /**
     * A help function for the inheritance perk.
     * Selects an integer between 0 and the number of specializations.
     * If the selection is Mutant, Sex, Slave or Trainer it will return NULL
     * 
     * @author Scythless
     * 
     * @param mother
     * 			The mother of the character to get the specs for.
     * @return
     * 		returns a SpecializationType other than Mutant, Sex, Slave and Trainer. May be NULL.
     */
    private static SpecializationType getMotherSpec(Charakter mother) {
    	
    		int i = 0;
    		int selection = Util.getInt(0, mother.getSpecializations().size());
			for (SpecializationType spec : mother.getSpecializations()) {
    			if (spec == SpecializationType.MUTANT || spec == SpecializationType.SEX || spec == SpecializationType.SLAVE || spec == SpecializationType.TRAINER ) {
    				i += 1;
    				continue;
    			}
    			else {
    				if (i == selection) return spec;
    				else i += 1;
    			}
			}
			return null;
    }
    
    private static CharacterBase selectCharacterBase(Charakter mother, Person otherParent) {
        CharacterBase characterBase;
        characterBase = checkPredeterminedBase(mother.getAgeProgressionData().getSonDaughterBase());
        if (characterBase == null && otherParent instanceof Charakter) {
            Charakter otherParentChar = (Charakter) otherParent;
            characterBase = checkPredeterminedBase(otherParentChar.getAgeProgressionData().getSonDaughterBase());
        }
        if (characterBase == null) {
            List<CharacterBase> bases = Jasbro.getInstance().getUnusedBases();
            characterBase = bases.get(Util.getInt(0, bases.size()));
        }
        return characterBase;
    }
    
    private static CharacterBase checkPredeterminedBase(String baseName) {
        if (baseName == null) {
            return null;
        }
        List<CharacterBase> bases = Jasbro.getInstance().getUnusedBases();
        for (CharacterBase base : bases) {
            if (baseName.equals(base.getId())) {
                return base;
            }
        }
        return null;
    }
    
    public static ComplexEnemy spawnEnemy(EnemySpawnLocation spawnLocation) {
        List<ComplexEnemyTemplate> enemyTemplates= new ArrayList<ComplexEnemyTemplate>();
        List<EnemySpawnData> spawnChances = new ArrayList<EnemySpawnData>();
        int sum = 0;
        
        for (ComplexEnemyTemplate enemyTemplate : Jasbro.getInstance().getEnemyTemplates().values()) {
            for (EnemySpawnData enemySpawnData : enemyTemplate.getSpawnDataList()) {
                if (enemySpawnData.getEnemySpawnLocation() == spawnLocation) {
                    sum += enemySpawnData.getEncounterChanceModifier();
                    spawnChances.add(enemySpawnData);
                    enemyTemplates.add(enemyTemplate);
                }
            }
        }
        
        if (enemyTemplates.size() > 0) {            
            int selected = Util.getInt(0, sum);
            int curValue = 0;
            for (int i = 0; i < spawnChances.size(); i++) {
                curValue += spawnChances.get(i).getEncounterChanceModifier();
                if (curValue >= selected) {
                    return enemyTemplates.get(i).generateEnemy();
                }
            }
            log.error("Something went wrong, enemy should have been returned already");
            return enemyTemplates.get(enemyTemplates.size()-1).generateEnemy();
        }
        else {
            log.error("No enemy found for location: " + spawnLocation.toString());
            return new ArrayList<ComplexEnemyTemplate>(Jasbro.getInstance().getEnemyTemplates().values()).get(0).generateEnemy();
        }
    }
    
    public static ComplexEnemy getEnemy(String id) {
        Map<String, ComplexEnemyTemplate> enemyTemplates = Jasbro.getInstance().getEnemyTemplates();
        if (enemyTemplates.containsKey(id)) {
            return enemyTemplates.get(id).generateEnemy();
        }
        else {
            return null;
        }
    }
}
