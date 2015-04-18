package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.Monster;
import jasbro.game.character.battle.Monster.MonsterType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class MonsterFight extends RunningActivity implements BusinessMainActivity, BusinessSecondaryActivity {
    private List<Charakter> fighters;
    private Monster monster;
    private MessageData message;
	private int monstertype = Util.getInt(1, 8);
    
    @Override
    public void init() {
        fighters = getCharacters();
        if(fighters.size() > 1 || getCharacter().getTraits().contains(Trait.ADRENALINEADDICT)){
            switch(monstertype){
    		case 1:  
    			monster = new Monster(MonsterType.NINETAILS);   
    			break;
    		case 2:
    			monster = new Monster(MonsterType.ALPHAWEREWOLF);   
    			break;
    		case 3:
    			monster = new Monster(MonsterType.SWAMPWALKER);   
    			break;
    		case 4:
    			monster = new Monster(MonsterType.ELDERDRAGON);   
    			break;
    		case 5:
    			monster = new Monster(MonsterType.SAGITARIUS);   
    			break;
    		case 6:
    			monster = new Monster(MonsterType.NAGAELITE);   
    			break;
    		case 7:
    			monster = new Monster(MonsterType.SPRIGGANQUEEN);   
    			break;
    		case 8:
    			monster = new Monster(MonsterType.GRANITGARGOYLE);   
    			break;
    		}	        	
        }
        else{
            switch(monstertype){
    		case 1:  
    			monster = new Monster(MonsterType.DEMONFOX);   
    			break;
    		case 2:
    			monster = new Monster(MonsterType.WEREWOLF);   
    			break;
    		case 3:
    			monster = new Monster(MonsterType.MUDWALKER);   
    			break;
    		case 4:
    			monster = new Monster(MonsterType.DRAGON);   
    			break;
    		case 5:
    			monster = new Monster(MonsterType.CENTAUR);   
    			break;
    		case 6:
    			monster = new Monster(MonsterType.NAGA);   
    			break;
    		case 7:
    			monster = new Monster(MonsterType.SPRIGGAN);   
    			break;
    		case 8:
    			monster = new Monster(MonsterType.GARGOYLE);   
    			break;
    		}	
        	
        }
	
    }
    
    @Override
    public MessageData getBaseMessage() {
        message = new MessageData();
        message.setBackground(getCharacter().getBackground());
        if (getMainCustomers().size() > 0) {
            Object arguments [] = {monster.getName(), getCustomers().size()};
            if (fighters.size() == 1) {
                ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, getCharacter());
                message.setImage(image);
                message.setMessage(TextUtil.t("fight.basic2", fighters.get(0), monster, arguments));
            }
            else {
                for (Charakter fighter : fighters) {
                    message.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, fighter));
                }
                message.setMessage(TextUtil.t("fight.basicmultivsmonster", fighters, arguments));
            }
        }
        else {
            ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter());
            message.setImage(image);
            message.setMessage(TextUtil.t("fight.noEnemy", getCharacter()));
        }
        return message;
    }
    
    @Override
    public List<ModificationData> getStatModifications() {
    	List<ModificationData> modifications = new ArrayList<ModificationData>();
        modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.STRENGTH));
        modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.STAMINA));
    	return modifications;
    }
    
    
    @Override
    public int getAppeal() {
        return Util.getInt(2, 8);
    }
    
    @Override
    public void perform() {
        if (getMainCustomers().size() > 0) {
            int i = 0;
            Battle battle = new Battle();
            List<Integer> startingHealths = new ArrayList<Integer>();
            battle.getSideA().addAll(fighters);
            battle.getSideB().add(monster);
            for (Charakter fighter : fighters) {
                startingHealths.add(fighter.getHitpoints());
            }

            boolean stop = false;
            do {
                battle.doRound();
                i++;
                for (Charakter character : fighters) {
                    if (character.getHitpoints() <= 10) {
                        stop = true;
                        break;
                    }
                }
            }
            while (monster.getHitpoints() > 10 && !stop);
            
            float entertainmentRating = i / 10f;
            int j = 0;
            for (Charakter fighter : fighters) {
                entertainmentRating += fighter.getDamage() / 25.0f + fighter.getArmor() / 1500f;
                addAttributeModification(this, startingHealths.get(j) - fighter.getHitpoints(), 
                        fighter, EssentialAttributes.HEALTH);
                j++;
            }
            
            for (Customer customer : getCustomers()) {
                customer.addToSatisfaction((int)(entertainmentRating * 30), this);
            }
            
            

            boolean charactersWon = false;
            
            if (monster.getHitpoints() <= 10) {
                charactersWon = true;
            }

            if (charactersWon) {
                getMainCustomer().addToSatisfaction(40, this);
            }
            else {
                getMainCustomer().addToSatisfaction(150, this);
            }
            
            message.addToMessage("\n\n" + battle.getCombatText());
            
            if (charactersWon) {
                entertainmentRating *= 2;
            }
            
            if (getCharacter().getTraits().contains(Trait.ADRENALINEADDICT)) {
            	entertainmentRating *= 1.3;
            }
            
            int winnings = 0;
            for (Customer customer : getCustomers()) {
                winnings += customer.pay(entertainmentRating);
            }
            modifyIncome(winnings);
            
            Object arguments [] = {winnings};
            String message = TextUtil.t("fight.result", arguments);
            if (charactersWon) {
                if (i < 8) {
                    message += "\n\n" + TextUtil.t("fight.onesided", new Object[]{TextUtil.listCharacters(fighters)});
                }
                else {
                    message += "\n\n" + TextUtil.t("fight.longMatch", new Object[]{TextUtil.listCharacters(fighters)});
                }
            }
            
            for (Charakter fighter : getCharacters()) {
                if(getCharacter().getTraits().contains(Trait.CONSUMEANDADAPT))
                {
                    int rand=Util.getInt(1, 7);
                    switch (rand){
                    case 1:
                        if(fighter.getFinalValue(BaseAttributeTypes.STRENGTH)>30){message += "\n\n" + TextUtil.t("fight.consumeandadapt.claws", fighter);}
                        break;
                    case 2:
                        if(fighter.getFinalValue(BaseAttributeTypes.STRENGTH)>30){message += "\n\n" + TextUtil.t("fight.consumeandadapt.tail", fighter);}
                        break;
                    case 3:
                        if(fighter.getFinalValue(BaseAttributeTypes.STAMINA)>50){message += "\n\n" + TextUtil.t("fight.consumeandadapt.scales", fighter);}
                        break;
                    case 4:
                        if(fighter.getFinalValue(Sextype.ORAL)>150){message += "\n\n" + TextUtil.t("fight.consumeandadapt.tongue", fighter);}
                        break;
                    case 5:
                        if(fighter.getFinalValue(BaseAttributeTypes.INTELLIGENCE)>50){message += "\n\n" + TextUtil.t("fight.consumeandadapt.firebreath", fighter);}
                        break;
                    case 6:
                        if(fighter.getFinalValue(BaseAttributeTypes.INTELLIGENCE)>50){message += "\n\n" + TextUtil.t("fight.consumeandadapt.wings", fighter);}
                        break;
                    case 7:
                        if(fighter.getFinalValue(BaseAttributeTypes.INTELLIGENCE)>50){message += "\n\n" + TextUtil.t("fight.consumeandadapt.slime", fighter);}
                        break;
                    }
                }
            }
            
            ImageData image = null;
            MessageData messageData = new MessageData(null, null, getCharacter().getBackground());
            if (charactersWon) {
                for (Charakter fighter : fighters) {
                    if (!fighter.getTraits().contains(Trait.WHENTHEHUNTERBECOMESPREY)) {
                        messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.VICTORIOUS, fighter));
                    }
                    else {
                        List<ImageTag> tags = getCharacter().getBaseTags();
                        tags.add(0, ImageTag.MONSTER);
                        tags.add(ImageTag.FORCED);
                        messageData.addImage(ImageUtil.getInstance().getImageDataByTags(tags, fighter.getImages()));
                        addAttributeModification(this,1.0f, fighter, Sextype.MONSTER);
                        if(monstertype == 1){
                            message+="\n" + TextUtil.t("fight.won.demonfox", fighter);
                        }
                        if(monstertype == 2){
                            message+="\n" + TextUtil.t("fight.won.werewolf", fighter);
                        }                           
                        if(monstertype == 3){
                            message+="\n" + TextUtil.t("fight.won.mudwalker", fighter);
                        }  
                        if(monstertype == 4){
                            message+="\n" + TextUtil.t("fight.won.dragon", fighter);
                        }                       
                        if(monstertype == 5){
                            message+="\n" + TextUtil.t("fight.won.centaur", fighter);
                        }  
                        if(monstertype == 6){
                            message+="\n" + TextUtil.t("fight.won.naga", fighter);
                        }  
                        if(monstertype == 7){
                            message+="\n" + TextUtil.t("fight.won.spriggan", fighter);
                        } 
                        if(monstertype == 8){
                            message+="\n" + TextUtil.t("fight.won.gargoyle", fighter);
                        }     
                    }
                    messageData.addImage(image);
                }
                messageData.setMessage(message);
                getMessages().add(messageData);
            }
            else {
            	 if(Util.getInt(1, 100) <= 100 || getCharacter().getTraits().contains(Trait.PHEROMONES) && Util.getInt(1, 100) <= 90){
                    if (fighters.size() == 1) {
                        message += "\n\n" + TextUtil.t("fight.monster.lost", getCharacter());
                    }
                    else {
                        message += "\n\n" + TextUtil.t("fight.monster.lost2", fighters);
                    }
                    
                    for (Charakter fighter : fighters) {
                        List<ImageTag> tags = getCharacter().getBaseTags();
                        tags.add(0, ImageTag.MONSTER);
                        tags.add(ImageTag.FORCED);
                        messageData.addImage(ImageUtil.getInstance().getImageDataByTags(tags, fighter.getImages()));
                        
                        addAttributeModification(this,1.0f, fighter, Sextype.MONSTER);

                        if(monstertype == 1){
                            message+="\n" + TextUtil.t("fight.lost.demonfox", fighter);
                        }
                        if(monstertype == 2){
                            message+="\n" + TextUtil.t("fight.lost.werewolf", fighter);
                        }                           
                        if(monstertype == 3){
                            message+="\n" + TextUtil.t("fight.lost.mudwalker", fighter);
                        }  
                        if(monstertype == 4){
                            message+="\n" + TextUtil.t("fight.lost.dragon", fighter);
                        }                       
                        if(monstertype == 5){
                            message+="\n" + TextUtil.t("fight.lost.centaur", fighter);
                        }  
                        if(monstertype == 6){
                            message+="\n" + TextUtil.t("fight.lost.naga", fighter);
                        }  
                        if(monstertype == 7){
                            message+="\n" + TextUtil.t("fight.lost.spriggan", fighter);
                        } 
                        if(monstertype == 8){
                            message+="\n" + TextUtil.t("fight.lost.gargoyle", fighter);
                        }  
                    }

                    setSextype(Sextype.MONSTER);
                } else {
                    for (Charakter fighter : fighters) {
                        messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, fighter));
                    }
                }
                messageData.setMessage(message);
                getMessages().add(messageData);

            }
                
            }
        }
    
    
    public void addAttributeModification(RunningActivity activity, float amount, Charakter character, AttributeType attributeType) {
        AttributeModification attributeModification = new AttributeModification(0, attributeType, character);
        attributeModification.setRealModification(amount);
        getAttributeModifications().add(attributeModification);
    }
    
    @Override
    public int rateCustomer(Customer customer) {
        if (customer.getMoney() > 500) {
            return customer.getMoney();
        }
        else {
            return 0;
        }
    }

    @Override
    public int getMaxAttendees() {
        return 40;
    }   
    
}
