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
import jasbro.game.character.activities.sub.Idle;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.MonsterDickType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class SubmitToMonster extends RunningActivity implements BusinessMainActivity, BusinessSecondaryActivity {
	private MessageData messageData;
	private monsterKind monster;
	private float HpAndEnCost;
	public float getHpAndEnCost() {
		return HpAndEnCost;
	}

	public void setHpAndEnCost(float HpAndEnCost) {
		this.HpAndEnCost = HpAndEnCost;
	}

	public monsterKind getMonster() {
		return monster;
	}

	public void setMonster(monsterKind monster) {
		this.monster = monster;
	}

	private enum monsterKind {
		GOBELIN(5, MonsterDickType.NORMAL), PACKOF3GOBELINS(15, MonsterDickType.NORMAL), PACKOF10GOBELINS(50, MonsterDickType.NORMAL),
		TENTACLE(10, MonsterDickType.TENTACLE), WIGGLER(15, MonsterDickType.TENTACLE), FATTENTACLE(30, MonsterDickType.TENTACLE), FATWIGGLER(60, MonsterDickType.TENTACLE), MEATCATCHER(80, MonsterDickType.TENTACLE),
		OGRE(15, MonsterDickType.NORMAL), TWOOGRES(30, MonsterDickType.NORMAL), 
		GIANT(20, MonsterDickType.NORMAL), TWOGIANTS(40, MonsterDickType.NORMAL), 
		SLIME(7, MonsterDickType.FLUID), KINGSLIME(28, MonsterDickType.FLUID), BREEDERSLIME(56, MonsterDickType.FLUID),
		MINORAUR(45, MonsterDickType.NORMAL), REDMINOTAUR(90, MonsterDickType.NORMAL),
		WOLF(6, MonsterDickType.NORMAL), ALPHAWOLF(18, MonsterDickType.NORMAL);

		private int monsterRank;
		private MonsterDickType monsterDick;

		private monsterKind(int monsterRank, MonsterDickType monsterDick){
			this.monsterRank = monsterRank;
			this.monsterDick = monsterDick;
		}
	}

	@Override
	public void init() {
		List <monsterKind> possibleMonsters = new ArrayList<monsterKind>();
		if(getMainCustomer()!=null){
			for(monsterKind mon : monsterKind.values()){
				if(getMainCustomer().getMoney()>mon.monsterRank*2*mon.monsterRank)
					possibleMonsters.add(mon);
			}

			setMonster(possibleMonsters.get(Util.getInt(0, possibleMonsters.size())));
		}
	}

	@Override
	public MessageData getBaseMessage() {
		if (getMainCustomer() != null) {
			List<ImageTag> tags = getCharacter().getBaseTags();
			tags.add(0, ImageTag.MONSTER);
			tags.add(ImageTag.FORCED);



			Object arguments[] = {getMainCustomer().getName() ,getCustomers().size()};

			messageData = new MessageData(TextUtil.t("submitToMonster.basic", getCharacter(), arguments), null, 
					getBackground());
		}
		else {
			messageData = new MessageData(TextUtil.t("submitToMonster.nocustomer", getCharacter()), null, 
					getBackground());
		}
		return messageData;

	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		if (getMainCustomer() != null) {
			if(getCharacter().getTraits().contains(Trait.MONSTERSOW))
				modifications.add(new ModificationData(TargetType.ALL, -0.3f, EssentialAttributes.MOTIVATION));
			else
				modifications.add(new ModificationData(TargetType.ALL, -2.3f, EssentialAttributes.MOTIVATION));
			modifications.add(new ModificationData(TargetType.SLAVE, 0.01f, BaseAttributeTypes.OBEDIENCE));
			if(!(getCharacter().getTraits().contains(Trait.LEGACYADVENTURER))){
				modifications.add(new ModificationData(TargetType.TRAINER, -0.5f, BaseAttributeTypes.COMMAND));
			}	

		}
		else {
			return new Idle().getStatModifications();
		}
		return modifications;
	}

	@Override
	public void perform() {
		if (getMainCustomer() != null) {
			monsterKind monster = getMonster();
			int monsterRank = monster.monsterRank;
			List<ImageTag> tags=new ArrayList<ImageTag>();			
			MonsterDickType monsterDick=monster.monsterDick;
			Charakter character = getCharacter();
			String monsterName=TextUtil.t(monster.toString(), character);
			Object argument[]= {monsterName, 2*monsterRank*monsterRank+500};
			messageData.addToMessage("\n"+TextUtil.t("submitToMonster.monsterkind", character, getMainCustomer(), argument));
			int skill=character.getFinalValue(Sextype.MONSTER)+character.getStamina()+character.getStrength();
			skill/=3;
			if(skill<monsterRank/3){
				messageData.addToMessage("\n\n"+TextUtil.t("submitToMonster.tooscary", character, argument));
				getMainCustomer().addToSatisfaction(-50, this);
				setHpAndEnCost(0);
				tags.add(ImageTag.STANDARD);
				for (Customer customer : getCustomers()) {
					customer.addToSatisfaction(-15, this);
				}
			}
			else if(skill<monsterRank/2){
				tags.add(ImageTag.MONSTER);
				messageData.addToMessage("\n\n"+TextUtil.t("submitToMonster.try", character, argument));
				getMainCustomer().addToSatisfaction(15+monsterRank, this);
				setHpAndEnCost(60);
				setIncome(2*monsterRank*monsterRank+500);
				for (Customer customer : getCustomers()) {
					customer.addToSatisfaction(10+monsterRank/2, this);
				}
			}

			else if(skill>monsterRank*3){
				tags.add(ImageTag.MONSTER);
				setIncome(2*monsterRank*monsterRank+500);
				messageData.addToMessage("\n\n"+TextUtil.t("submitToMonster.dry", character, argument));
				getMainCustomer().addToSatisfaction(25+monsterRank, this);
				setHpAndEnCost(15);
				for (Customer customer : getCustomers()) {
					customer.addToSatisfaction(20+monsterRank/2, this);
				}
			}
			else if(skill>monsterRank*2){
				tags.add(ImageTag.MONSTER);
				setIncome(2*monsterRank*monsterRank+500);
				messageData.addToMessage("\n\n"+TextUtil.t("submitToMonster.cake", character, argument));
				getMainCustomer().addToSatisfaction(20+monsterRank, this);
				setHpAndEnCost(30);
				for (Customer customer : getCustomers()) {
					customer.addToSatisfaction(15+monsterRank/2, this);
				}
			}
			else{
				tags.add(ImageTag.MONSTER);
				setIncome(2*monsterRank*monsterRank+500);
				messageData.addToMessage("\n\n"+TextUtil.t("submitToMonster.okay", character, argument));
				getMainCustomer().addToSatisfaction(15+monsterRank, this);
				setHpAndEnCost(45);
				for (Customer customer : getCustomers()) {
					customer.addToSatisfaction(10+monsterRank/2, this);
				}
			}
			if(!character.getTraits().contains(Trait.MONSTERSOW)){
				this.getAttributeModifications().add(new AttributeModification(-monsterRank*getHpAndEnCost()/100,EssentialAttributes.ENERGY, character));
				this.getAttributeModifications().add(new AttributeModification(-monsterRank*getHpAndEnCost()/100,EssentialAttributes.HEALTH, character));
			}
			else
				this.getAttributeModifications().add(new AttributeModification(-monsterRank*getHpAndEnCost()/200,EssentialAttributes.ENERGY, character));

			this.getAttributeModifications().add(new AttributeModification(2.0f,Sextype.MONSTER, character));
			this.getAttributeModifications().add(new AttributeModification(0.09f,BaseAttributeTypes.STAMINA, character));

			switch(monsterDick){
			case NORMAL:
				tags.add(ImageTag.NORMALDICK);
				break;
			case FLUID:
				tags.add(ImageTag.FLUID);
				break;
			case TENTACLE:
				tags.add(ImageTag.TENTACLE);
				break;
			default:
				break;
			}
			messageData.setImage(ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()));



		}
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
	public int getAppeal() {
		return Util.getInt(1, 7);
	}

	@Override
	public int getMaxAttendees() {
		return 40;
	}




}