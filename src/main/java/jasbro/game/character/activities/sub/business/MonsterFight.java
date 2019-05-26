package jasbro.game.character.activities.sub.business;

import java.util.ArrayList;
import java.util.List;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterSpawner;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Item;
import jasbro.game.items.ItemType;
import jasbro.game.items.SummoningItem;
import jasbro.game.world.customContent.npc.ComplexEnemy;
import jasbro.gui.pages.SelectionData;
import jasbro.gui.pages.SelectionScreen;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class MonsterFight extends RunningActivity implements BusinessMainActivity, BusinessSecondaryActivity {
	private List<Charakter> fighters;
	private ComplexEnemy monster;
	private MessageData message;
	private boolean playerWon;
	String rapeText;
	private SummoningItem item;
	private String itemName;
	private String monsterID = null;

    
	@Override
	public void init() {
		
		fighters = getCharacters();
		
		List<SelectionData<SummoningItem>> options = getItemOptions(ItemType.SUMMONING);			
		if (getPlannedActivity().getSelectedOption() == null && options.size() > 0) {
			if(fighters.size()>1){
				SelectionData<SummoningItem> selectedOption = new SelectionScreen<SummoningItem>().select(options, 
						ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, fighters.get(0)), 
						ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, fighters.get(1)), 
						fighters.get(0).getBackground(), TextUtil.t("fight.monster.option.description", fighters));	
				item = selectedOption.getSelectionObject();
			}
			else{
				SelectionData<SummoningItem> selectedOption = new SelectionScreen<SummoningItem>().select(options, 
						ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, fighters.get(0)), null, 
						fighters.get(0).getBackground(), TextUtil.t("fight.monster.option.description", fighters.get(0)));
				item = selectedOption.getSelectionObject();
			}
			spawnEnemy(true);
		}
		else if(getPlannedActivity().getSelectedOption() != null) {
			item = (SummoningItem) getPlannedActivity().getSelectedOption().getSelectionObject();
			spawnEnemy(true);
		}
		else{
			spawnEnemy(false);		
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
			ImageData imageM = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, monster);
			message.addImage(imageM);
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
		if(!(getCharacter().getTraits().contains(Trait.LEGACYADVENTURER))){
			modifications.add(new ModificationData(TargetType.TRAINER, -0.5f, BaseAttributeTypes.COMMAND));
		}	
		return modifications;
	}
    
    
	@Override
	public int getAppeal() {
		return Util.getInt(2, 8);
	}
    
	@Override
	public void perform() {
		Battle battle = new Battle();
		if (getMainCustomers().size() > 0) {
			int i = 0;
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
				int a = 0;
				for (Charakter fighter : fighters) {
					if (fighters.get(a).getTraits().contains(Trait.REPTILIANMOTIVATION)) {
						addAttributeModification(this,2.0f, fighters.get(a), EssentialAttributes.MOTIVATION);
					}	
					a++;
				}		
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
                       
			ImageData image = null;
			MessageData messageData = new MessageData(null, null, getCharacter().getBackground());
			if (charactersWon) {
				for (Charakter fighter : fighters) {
					List<ImageTag> tags = getCharacter().getBaseTags();
					tags.add(0, ImageTag.MONSTER);
					tags.add(ImageTag.FORCED);
					messageData.addImage(ImageUtil.getInstance().getImageDataByTags(tags, fighter.getImages()));
					addAttributeModification(this,1.0f, fighter, Sextype.MONSTER);			
					messageData.addImage(image);
				}
				messageData.setMessage(message);
				getMessages().add(messageData);
			}
			else {
				if(Util.getInt(1, 100) <= 100){
					if (fighters.size() == 1) {
						if(fighters.get(0).getGender() == Gender.MALE){
							rapeText = monster.getMaleRape();
							if(rapeText == ""){
								rapeText = TextUtil.t("fight.monster.rape.male.generic", getCharacter(), monster.getName());
							}
						}
						else{
							rapeText = monster.getFemaleRape();
							if(rapeText == ""){
								rapeText = TextUtil.t("fight.monster.rape.female.generic", getCharacter(), monster.getName());
							}
						}
						message += "\n\n" + TextUtil.t("fight.monster.lost", getCharacter(), monster);
						message += "\n\n" + TextUtil.getInstance().applyTemplates(rapeText, fighters);		            
					}
					else {
						Object monsterName [] = {monster.getName()};
						
						int primaryFighter=Util.getInt(0, 2);
						if (fighters.get(primaryFighter).getGender() == Gender.MALE){
							rapeText = monster.getMaleRape();
							if(rapeText == ""){
								rapeText = TextUtil.t("fight.monster.rape.male.generic", fighters.get(primaryFighter), monster.getName());
							}
						}
						else{
							rapeText = monster.getFemaleRape();
							if(rapeText == ""){
								rapeText = TextUtil.t("fight.monster.rape.female.generic", fighters.get(primaryFighter), monster.getName());
							}
						}
						
						if(monster.getGender()==Gender.MALE){
							if(primaryFighter == 0){
								message += "\n\n" + TextUtil.t("fight.monster.lost2.char1.maleMonster", fighters, monsterName);	
							}
							else{
								message += "\n\n" + TextUtil.t("fight.monster.lost2.char2.maleMonster", fighters, monsterName);	
							}

						}
						else{
							if(primaryFighter == 0){
								message += "\n\n" + TextUtil.t("fight.monster.lost2.char1.femaleMonster", fighters, monsterName);	
							}
							else{
								message += "\n\n" + TextUtil.t("fight.monster.lost2.char2.femaleMonster", fighters, monsterName);
							}
						}

						message += "\n\n" + TextUtil.getInstance().applyTemplates(rapeText, fighters);
					}
                    
					for (Charakter fighter : fighters) {
						List<ImageTag> tags = getCharacter().getBaseTags();
						tags.add(0, ImageTag.MONSTER);
						tags.add(ImageTag.FORCED);
						messageData.addImage(ImageUtil.getInstance().getImageDataByTags(tags, fighter.getImages()));
                        
						addAttributeModification(this,1.0f, fighter, Sextype.MONSTER);
					}

					setSextype(Sextype.MONSTER);
				} else {
					for (Charakter fighter : fighters) {
						messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, fighter));
					}
				}
				ImageData imageM = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, monster);
				messageData.addImage(imageM);
				messageData.setMessage(message);
				getMessages().add(messageData);
			}
                
		}
	}   
	
	public class MonsterTextUtil {
		public String MonsterRape(String text, List<? extends Person> people) {
			return text;
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
	
	@Override
	public List<SelectionData<?>> getSelectionOptions(PlannedActivity plannedActivity) {
			return new ArrayList<SelectionData<?>>(getItemOptions(ItemType.SUMMONING));
	}
	
	public List<SelectionData<SummoningItem>> getItemOptions(ItemType itemtype) {
		List<SelectionData<SummoningItem>> options = new ArrayList<SelectionData<SummoningItem>>();
		for (Item item : Jasbro.getInstance().getData().getInventory().getExistingItems()) {
			if (item.getType() == itemtype) {
				itemName = item.getName();
				SelectionData<SummoningItem> option = new SelectionData<SummoningItem>();
				option.setSelectionObject((SummoningItem) item);
				option.setButtonText(TextUtil.t("fight.monster.name", itemName));
				option.setShortText(item.getText());
				options.add(option);
			}
		}
		return options;
	}
	
	public void spawnEnemy(boolean ownMonster) {	
		if(ownMonster==true){
			monsterID = item.getSummonedMonster();
			monster = CharacterSpawner.getEnemy(monsterID);
		}
		else{
			monster = CharacterSpawner.spawnCustomerEnemy();
		}
		if(fighters.size() > 1){
			monster.setHitpoints(monster.getHitpoints()*2);
			monster.setAttribute(CalculatedAttribute.SPEED, monster.getAttribute(CalculatedAttribute.SPEED)*2);
			monster.setAttribute(CalculatedAttribute.DAMAGE, monster.getAttribute(CalculatedAttribute.DAMAGE)*1.5);
			monster.setAttribute(CalculatedAttribute.CRITCHANCE, monster.getAttribute(CalculatedAttribute.CRITCHANCE)*1.5);
			monster.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, monster.getAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT)*2);
		}
	}
}