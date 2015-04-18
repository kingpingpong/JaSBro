package jasbro.game.character.traits;

import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.interfaces.AttributeType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PerkHandler implements Serializable {
    
    public static List<SkillTree> getSkillTrees(Charakter character) {
        List<SkillTree> skillTrees = new ArrayList<SkillTree>();
        for (SpecializationType specializationType : character.getSpecializations()) {
            if (specializationType.getAssociatedSkillTree() != null) {
                skillTrees.add(specializationType.getAssociatedSkillTree());
            }
        }
        for (Trait trait : character.getTraits()) {
            if (trait.getAssociatedSkillTree() != null) {
                skillTrees.add(trait.getAssociatedSkillTree());
            }
        }
        return skillTrees;
    }
    
    public static int getSkillPoints(Charakter character) {
        int amount = character.getBonusPerks();
        for (SpecializationType specializationType : character.getSpecializations()) {
            List<AttributeType> attributeTypes = specializationType.getAssociatedAttributes();
            if (attributeTypes.size() > 0) {
                float sum = 0;
                for (AttributeType attributeType : attributeTypes) {
                    if (attributeType instanceof BaseAttributeTypes) {
                        sum += (int) (character.getAttribute(attributeType).getInternValue());
                    }
                    else {
                        sum += character.getAttribute(attributeType).getInternValue();
                    }
                }
                if (specializationType != SpecializationType.SLAVE && specializationType != SpecializationType.TRAINER) {
                    amount += (int) (sum / attributeTypes.size() / 50);
                }
                else {
                    amount += (int) (sum / 20);
                }
            }
        }
        return amount;
    }
    
    public static int getUsedSkillPoints(Charakter character) {
        int amount = 0;
        for (Trait trait : character.getTraitsInternal()) {
            if (trait.isPerk()) {
                amount++;
            }
        }
        return amount;
    }
    
    public static int getLevel(SkillTreeItem skillTreeItem, SkillTree skillTree) {
        int level = 1;
        do {
            if (skillTreeItem.getParentItems().size() == 0) {
                return level;
            }
            else {
                skillTreeItem = skillTreeItem.getParentItems().get(0);
            }
            level++;
        }
        while (true);
    }
    
    public static int requiredSkill(SkillTreeItem skillTreeItem, SkillTree skillTree) {
        return (getLevel(skillTreeItem, skillTree) - 1) * 50;
    }
    
    public static int getBaseRequirement(SkillTreeItem skillTreeItem, SkillTree skillTree, Charakter character) {
        int level = getLevel(skillTreeItem, skillTree);
        int requirement;
        if (skillTree != SkillTree.TRAINER && skillTree != SkillTree.SLAVE) {
            requirement = (level - 1) * 50;
        }
        else {
            requirement = (level - 1) * 10;
        }
        List<SkillTreeItem> items = new ArrayList<SkillTreeItem>();
        items.add(skillTree.getFirstItem());
        if (level > 1) {
            for (int i = 0; i < level - 1; i++) {
                List<SkillTreeItem> nextItems = new ArrayList<SkillTreeItem>();
                for (SkillTreeItem curSkillTreeItem : items) {
                    nextItems.addAll(curSkillTreeItem.getNextItems());
                }
                items = nextItems;
            }
            List<Trait> traits = character.getTraits();
            for (SkillTreeItem item : items) {
                if (traits.contains(item.getPerk())) {
                    requirement = requirement * 2;
                    break;
                }
            }
        }


        return requirement;
    }
    
    public static SpecializationType getConnectedSpecializationType(SkillTree skillTree) {
        SpecializationType specializationType = null;
        for (SpecializationType curSpecializationType : SpecializationType.values()) {
            if (curSpecializationType.toString().equals(skillTree.toString())) {
                specializationType = curSpecializationType;
                break;
            }
        }
        return specializationType;
    }
    
    public static void resetPerks(Charakter character) {
        List<Trait> traits = new ArrayList<Trait>(character.getTraits());
        for (Trait trait : traits) {
        	if (trait == Trait.ANTHRO)
        		character.removeTrait(Trait.ANTHRO);
            if (trait.isPerk()) {
                character.removeTrait(trait);
            }
        }
    }
}