package jasbro.game.character.conditions;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MessageData;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.PregnancyInterface;
import jasbro.game.items.Item;
import jasbro.gui.GuiUtil;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.List;

public class MonsterPregnancy extends Condition implements PregnancyInterface {
    private int days = 60;
    private int eggs = 1;
    private final static String itemString = "MonsterEgg";
    
    public MonsterPregnancy(Charakter mother, MyEvent event) {
        this(mother, event, true);
    }
    
    public MonsterPregnancy(Charakter mother, MyEvent event, boolean message) {
        //TODO draw monster information from event?
        
        int chanceAdditionalChild = mother.getChanceAdditionalChild() + 5;
        
        days = (int)(days * mother.getPregnancyDurationModifier() / 100f);
        int minChildren = mother.getMinChildren();
        int maxChildren = mother.getMaxChildren() + 5;
        
        if (mother.getTraits().contains(Trait.BEASTBREEDER)) {
            chanceAdditionalChild *= 2;
            maxChildren *= 2;
        }
        
        if (minChildren < 1) {
            minChildren = 1;
        }
        
        eggs = minChildren;
        for (int i = minChildren; i < maxChildren; i++) {
            if (Util.getInt(0, 100) < chanceAdditionalChild) {
                eggs++;
            }
            else {
                break;
            }
        }
        
        if (message) {
            MessageData messageData = new MessageData();
            messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.PREGNANT, mother));
            messageData.addToMessage(TextUtil.t("monsterpregnancy.pregnant", mother));
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
                if (Jasbro.getInstance().getItems().containsKey(itemString)) {
                    Item item = Jasbro.getInstance().getItems().get(itemString);
                    getCharacter().getConditions().remove(this);
                    MessageData messageData = new MessageData();
                    messageData.addToMessage(TextUtil.t("monsterpregnancy.birth", getCharacter(), new Object[]{eggs}));
                    messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.MONSTERBIRTH, getCharacter()));
                    getCharacter().getCounter().add(CounterNames.CHILDREN);
                    
                    messageData.setBackground(getCharacter().getBackground());
                    messageData.setPriorityMessage(true);
                    GuiUtil.addMessageToEvent(messageData, e);                   
                    
                    Jasbro.getInstance().getData().getInventory().addItems(item, eggs);
                }
            }
        }
    }
    
    @Override
    public ImageData getIcon() {
        return new ImageData("images/icons/perks/beastbreeder.png");
    }

    public int getDays() {
        return days;
    }
    
    public void reduceDays(int amount) {
        days -= amount;
    }
    
    @Override
    public String getName() {
        return TextUtil.t("monsterpregnancy", getCharacter());
    }
    
    @Override
    public String getDescription() {
        return getName() + "\n" +
                TextUtil.t("monsterpregnancy.description", new Object[]{days});
    }

    public void modifyDays(int amount) {
        days += amount;
    }
    
    @Override
    public void modifyImageTags(List<ImageTag> imageTags) {
        if (days < 55) {
            imageTags.add(ImageTag.PREGNANT);
        }
    }
}
