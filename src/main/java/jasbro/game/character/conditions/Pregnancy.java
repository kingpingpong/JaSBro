package jasbro.game.character.conditions;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterSpawner;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.Person;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.gui.GuiUtil;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Pregnancy extends Condition implements PregnancyInterface {
    private final static Logger log = Logger.getLogger(Pregnancy.class);
    private int days = 271;
    private List<Charakter> children = new ArrayList<Charakter>();
    private Charakter mother;
    private String father;
    
    public Pregnancy() {
    }
    
    public Pregnancy(Charakter mother, Person otherParent, MyEvent event) {
        this(mother, otherParent, event, true);
    }
    
    public Pregnancy(Charakter mother, Person otherParent, MyEvent event, boolean message) {
        this.mother = mother;
        if (otherParent != null) {
            this.father = otherParent.getName();
        }
        int minChildren = mother.getMinChildren();
        int maxChildren = mother.getMaxChildren();
        int chanceAdditionalChild = mother.getChanceAdditionalChild();
        
        if (otherParent instanceof Charakter) {
            Charakter otherParentCharakter = (Charakter) otherParent;
            minChildren = (minChildren + otherParentCharakter.getMinChildren()) / 2;
            maxChildren = (maxChildren + otherParentCharakter.getMaxChildren()) / 2;
            chanceAdditionalChild = (chanceAdditionalChild + otherParentCharakter.getChanceAdditionalChild()) / 2;
            days = (int)(((days * mother.getPregnancyDurationModifier() / 100f) + 
                    (days * otherParentCharakter.getPregnancyDurationModifier() / 100f)) / 2);
        }
        else {
            days = (int)(days * mother.getPregnancyDurationModifier() / 100f);
        }
        
        if (minChildren < 1) {
            minChildren = 1;
        }
        
        for (int i = 0; i < minChildren; i++) {
            children.add(CharacterSpawner.spawnChild(mother, otherParent));
        }
        for (int i = minChildren; i < maxChildren; i++) {
            if (Util.getInt(0, 100) < chanceAdditionalChild) {
                children.add(CharacterSpawner.spawnChild(mother, otherParent));
            }
            else {
                break;
            }
        }
        
        if (message) {
            MessageData messageData = new MessageData();
            messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, mother));
            messageData.addToMessage(TextUtil.t("pregnancy.pregnant", mother, otherParent));
            if (otherParent instanceof Charakter) {
                messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, (Charakter) otherParent));
            }
            messageData.setBackground(mother.getBackground());
            messageData.setPriorityMessage(true);
            GuiUtil.addMessageToEvent(messageData, event);
            
        }
        mother.addCondition(new ItemCooldown(7, "Elixir_of_growth"));

    }
    
    @Override
    public void handleEvent(MyEvent e) {
        if (e.getType() == EventType.NEXTDAY) {
            days--;
            if (days <= 0) {
                getCharacter().getConditions().remove(this);
                Jasbro.getInstance().getData().getCharacters().addAll(children);
                MessageData messageData = new MessageData();
                Object[] parameters = {TextUtil.listCharacters(children)};
                messageData.addToMessage(TextUtil.t("pregnancy.birth", mother, parameters));
                messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, mother));
                mother.getCounter().add(CounterNames.CHILDREN, (long)children.size());
                
                try {
                    if (children.get(0).getAgeProgressionData().getFather() != null) {
                        Charakter father = children.get(0).getAgeProgressionData().getFather().get();
                        father.getCounter().add(CounterNames.CHILDREN, (long)children.size());
                        messageData.addToMessage(TextUtil.t("pregnancy.fatherKnown", father));
                        messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, father));
                    }
                    else {
                        if (father != null) {
                            parameters[0] = children.get(0).getAgeProgressionData().getNameFather();
                            if (children.size() < 2) {
                                messageData.addToMessage(TextUtil.t("pregnancy.fatherUnknown.singleChild", children.get(0), parameters));
                            }
                            else {
                                messageData.addToMessage(TextUtil.t("pregnancy.fatherUnknown.children", parameters));
                            }
                        }
                        else {
                            if (children.size() < 2) {
                                messageData.addToMessage(TextUtil.t("pregnancy.nofather.singleChild", children.get(0)));
                            }
                            else {
                                messageData.addToMessage(TextUtil.t("pregnancy.nofather.children", parameters));
                            }
                        }
                    }
                }
                catch (Exception ex) {
                    log.error("Error when creating children message", ex);
                }
                
                for (Charakter child : children) {
                    messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, child));
                }
                messageData.setBackground(mother.getBackground());
                messageData.setPriorityMessage(true);
                GuiUtil.addMessageToEvent(messageData, e);
            }
        }
    }
    
    @Override
    public ImageData getIcon() {
        return new ImageData("images/icons/pregnant.png");
    }

    public int getDays() {
        return days;
    }

    public List<Charakter> getChildren() {
        return children;
    }
    
    public void reduceDays(int amount) {
        days -= amount;
    }
    
    @Override
    public String getName() {
        return TextUtil.t("pregnancy", mother);
    }
    
    @Override
    public String getDescription() {
        return getName() + "\n" +
                TextUtil.t("pregnancy.description", new Object[]{getMonthsRemaining(), father});
    }
    
    public int getMonthsRemaining() {
        return Math.max(0, days / 30);
    }

    public void modifyDays(int amount) {
        days += amount;
    }

    public Charakter getMother() {
        return mother;
    }

    public void setMother(Charakter mother) {
        this.mother = mother;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }
    
    @Override
    public void modifyImageTags(List<ImageTag> imageTags) {
        if (days < 180) {
            imageTags.add(ImageTag.PREGNANT);
        }
    }
}
