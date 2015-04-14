package jasbro.game.character.conditions;

import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.Gender;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Buff extends Condition {
	private String nameKey;
	private Map<AttributeType, Integer> attributeModifiers = new HashMap<>();
	private ImageData icon = new ImageData("imates/cons/plus.jpg");
	private int remainingTime = 10;

	public Buff() {
	}

	public Buff(String nameKey, ImageData icon, int time) {
		super();
		this.nameKey = nameKey;
		this.icon = icon;
		remainingTime = time;
	}

	@Override
	public String getName() {
		return TextUtil.t(nameKey);
	}

	@Override
	public String getDescription() {
		Object arguments[] = new Object[2];
		String text = getName() + "\n";
		text += TextUtil.t("buff.description");

		List<String> modifierStrings = new ArrayList<String>();

		boolean newline = false;
		for(Map.Entry<AttributeType, Integer> entry : getAttributeModifiers().entrySet()) {
			StringBuilder builder = new StringBuilder();
			if (newline) {
				builder.append('\n');
			}

			builder.append(TextUtil.t("buff.by", entry.getKey().getText(), entry.getValue()));

			modifierStrings.add(builder.toString());
			newline = !newline;
		}

		text += TextUtil.listStrings(modifierStrings);
		arguments[0] = remainingTime;
		text += "\n" + TextUtil.t("buff.duration", arguments) + " ";
		return text;
	}	

	@Override
	public ImageData getIcon() {
		return icon;
	}

	@Override
	public float getAttributeModifier(Attribute attribute) {
		int modifier = 0;
		for (Map.Entry<AttributeType, Integer> entry : attributeModifiers.entrySet()) {
			if(entry.getKey() == attribute.getAttributeType()) {
				modifier += entry.getValue();
			}
		}
		return modifier;
	}

	@Override
	public void handleEvent(MyEvent e) {
		if (e.getType() == EventType.NEXTSHIFT) {
			remainingTime--;
			if (remainingTime <= 0) {
				getCharacter().getConditions().remove(this);
			}
		}
	}

	public void addAttributeBuff(AttributeType attributeType, int amount) {
		attributeModifiers.put(attributeType, amount);
	}

	public String getNameKey() {
		return nameKey;
	}

	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}

	public Map<AttributeType, Integer> getAttributeModifiers() {
		return attributeModifiers;
	}

	public void setIcon(ImageData icon) {
		this.icon = icon;
	}


	public int getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	@Override
	public void init() {
		Buff existingBuff = getExistingBuff();
		if (existingBuff != null) {

			if (existingBuff.getRemainingTime() >= 3) {
				for(Map.Entry<AttributeType, Integer> entry : existingBuff.getAttributeModifiers().entrySet()) {
					if(entry.getKey() instanceof BaseAttributeTypes) {
						entry.setValue(entry.getValue() + 1);
					} else {
						entry.setValue(entry.getValue() + 10);
					}
				}
				getCharacter().getConditions().remove(this);
			} else {
				getCharacter().getConditions().remove(existingBuff);
			}

		}
	}

	public Buff getExistingBuff() {
		Buff existingBuff = null;
		for (Condition condition : getCharacter().getConditions()) {
			if (condition instanceof Buff && condition != this && condition.getClass().equals(getClass())) {
				existingBuff = (Buff) condition;
				break;
			}
		}
		return existingBuff;
	}


	public static class Intimidated extends Buff {
		public Intimidated(Charakter sourceCharacter) {
			super("buff.intimidated", new ImageData(), 10);
			setIcon(new ImageData("images/icons/intimidate.png"));
			int amount;
			int command = sourceCharacter.getCommand();

			if (command > 45) {
				amount = 7; 
			}
			else if (command > 30) {
				amount = 6; 
			}
			else if (command > 20) {
				amount = 5; 
			}
			else if (command > 12) {
				amount = 4; 
			}
			else if (command > 5) {
				amount = 3; 
			}
			else if (command > 3) {
				amount = 2; 
			}
			else {
				amount = 1;
			}
			addAttributeBuff(BaseAttributeTypes.OBEDIENCE, amount);
		}
	}

	public static class Motivated extends Buff {
		public Motivated(Charakter sourceCharacter) {
			super("buff.motivated", new ImageData(), 10);
			setIcon(new ImageData("images/icons/motivated.png"));
			int command = sourceCharacter.getCommand();

			if (command > 45) {
				addAttributeBuff(BaseAttributeTypes.STAMINA, 4);
				addAttributeBuff(BaseAttributeTypes.STRENGTH, 3);
				addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 3); 
			}
			else if (command > 30) {
				addAttributeBuff(BaseAttributeTypes.STAMINA, 3);
				addAttributeBuff(BaseAttributeTypes.STRENGTH, 2);
				addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 2);
			}
			else if (command > 20) {
				addAttributeBuff(BaseAttributeTypes.STAMINA, 2);
				addAttributeBuff(BaseAttributeTypes.STRENGTH, 2);
				addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 1);
			}
			else if (command > 12) {
				addAttributeBuff(BaseAttributeTypes.STAMINA, 2);
				addAttributeBuff(BaseAttributeTypes.STRENGTH, 1);
				addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 1);
			}
			else if (command > 5) {
				addAttributeBuff(BaseAttributeTypes.STAMINA, 1);
				addAttributeBuff(BaseAttributeTypes.STRENGTH, 1);
				addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 1);
			}
			else if (command > 3) {
				addAttributeBuff(BaseAttributeTypes.STAMINA, 1);
				addAttributeBuff(BaseAttributeTypes.STRENGTH, 1);
			}
			else {
				addAttributeBuff(BaseAttributeTypes.STAMINA, 1);

			}
		}
	}

	public static class Satiated extends Buff {
		public Satiated(int FoodRank,Charakter sourceCharacter) {
			super("buff.satiated", new ImageData(), 3);
			setIcon(new ImageData("images/icons/chicken.png"));

			addAttributeBuff(BaseAttributeTypes.STAMINA, 2+sourceCharacter.getStamina()*FoodRank/100);
			addAttributeBuff(BaseAttributeTypes.STRENGTH, 2+sourceCharacter.getStrength()*FoodRank/100);
			if(sourceCharacter.getTraits().contains(Trait.CATSAREASSHOLES)){
				addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 2+sourceCharacter.getObedience()/2);
			}


		}
	}

	public static class Pretty extends Buff {
		public Pretty(Charakter sourceCharacter, Charakter target) {
			super("buff.pretty", new ImageData(), 14);
			setIcon(new ImageData("images/icons/cucumber.png"));
			int skill = sourceCharacter.getFinalValue(SpecializationAttribute.WELLNESS)/10;
			int charismaIncrease;
			charismaIncrease=target.getCharisma()*skill/100;



			if (sourceCharacter.getTraits().contains(Trait.COSMETICS)) {
				charismaIncrease+=10;
			}

			addAttributeBuff(BaseAttributeTypes.CHARISMA, charismaIncrease);
		}
	}

	public static class SmoothSkin extends Buff {
		public SmoothSkin(int magnitude, Charakter target) {
			super("buff.smoothskin", new ImageData(), 4);
			setIcon(new ImageData("images/icons/cucumber.png"));

			addAttributeBuff(BaseAttributeTypes.CHARISMA, 2+target.getCharisma()*magnitude/100);	

		}
	}

	public static class AllDolledUp extends Buff {
		public AllDolledUp(int skill, Charakter target) {
			super("buff.dolledup", new ImageData(), 4);
			setIcon(new ImageData("images/icons/ribbon.png"));
			addAttributeBuff(BaseAttributeTypes.CHARISMA, 2+target.getCharisma()*skill/100);

		}
	}
	public static class Stoned extends Buff {
		public Stoned(int skill, Charakter target, int intellBonus) {
			super("buff.stoned", new ImageData(), 4);
			setIcon(new ImageData("images/icons/stoned.png"));
			addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 2+target.getObedience()*skill/100);
			addAttributeBuff(BaseAttributeTypes.STAMINA, 2);
			addAttributeBuff(BaseAttributeTypes.INTELLIGENCE, target.getIntelligence()*intellBonus/100);

		}
	}
	public static class RoughenedUp extends Buff {
		public RoughenedUp() {
			super("buff.roughenedup", new ImageData(), 6);
		}

		@Override
		public String getDescription() {
			int staminaLoss=0;
			int strengthLoss=0;
			staminaLoss=2+getCharacter().getStamina()/8;
			if(getCharacter().getStamina()<4){staminaLoss=2;}			
			strengthLoss=2+getCharacter().getStrength()/8;
			if(getCharacter().getStrength()<4){strengthLoss=2;}
			Object arguments[]={staminaLoss, strengthLoss};
			return TextUtil.t("buff.roughenedup.description", getCharacter(), arguments);
		}
		@Override
		public void init() {
			setIcon(new ImageData("images/icons/roughenedup.png"));
			int staminaLoss=0;
			int strengthLoss=0;
			
			staminaLoss=2+getCharacter().getStamina()/8;
			if(getCharacter().getStamina()<4){staminaLoss=2;}
			
			strengthLoss=2+getCharacter().getStrength()/8;
			if(getCharacter().getStrength()<4){strengthLoss=2;}
			
			addAttributeBuff(BaseAttributeTypes.STAMINA, -staminaLoss);
			addAttributeBuff(BaseAttributeTypes.STRENGTH, -strengthLoss);

			super.init();
		}
	}

	public static class HornyBuff extends Buff {
		public HornyBuff(Charakter sourceCharacter) {
			super("buff.horny", new ImageData(), 2);
			setIcon(new ImageData("images/icons/Hearts.png"));


			addAttributeBuff(BaseAttributeTypes.CHARISMA, 2+sourceCharacter.getCharisma()/4);
			addAttributeBuff(BaseAttributeTypes.STAMINA, 2+sourceCharacter.getStamina()/4);
		}
	}
	public static class Angry extends Buff {
		public Angry(Charakter sourceCharacter) {
			super("buff.angry", new ImageData(), 3);
			setIcon(new ImageData("images/icons/perks/evenassholeshavestandards.png"));

			addAttributeBuff(BaseAttributeTypes.CHARISMA, 2+sourceCharacter.getCharisma()/4);
		}
	}
	public static class MotivatedTwo extends Buff {
		public MotivatedTwo(Charakter sourceCharacter) {
			super("buff.motivated", new ImageData(), 3);
			setIcon(new ImageData("images/icons/motivated.png"));

			addAttributeBuff(BaseAttributeTypes.STAMINA, 2+sourceCharacter.getStamina()/4);
			addAttributeBuff(BaseAttributeTypes.STRENGTH, 2+sourceCharacter.getStrength()/4);
			addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 2+sourceCharacter.getObedience()/4);
		}
	}
	public static class Happy extends Buff {
		public Happy(Charakter sourceCharacter) {
			super("buff.happy", new ImageData(), 3);
			setIcon(new ImageData("images/icons/motivated.png"));

			addAttributeBuff(BaseAttributeTypes.STAMINA, 2+sourceCharacter.getStamina()/4);
			addAttributeBuff(BaseAttributeTypes.STRENGTH, 2+sourceCharacter.getStrength()/4);
			addAttributeBuff(BaseAttributeTypes.OBEDIENCE, 2+sourceCharacter.getObedience()/4);

		}
	}
	public static class Depressed extends Buff {
		public Depressed(Charakter sourceCharacter) {
			super("buff.depressed", new ImageData(), 3);
			setIcon(new ImageData("images/icons/motivated.png"));

			addAttributeBuff(BaseAttributeTypes.STAMINA, -1*sourceCharacter.getStamina()/4);
			addAttributeBuff(BaseAttributeTypes.STRENGTH, -1*sourceCharacter.getStrength()/4);
			addAttributeBuff(BaseAttributeTypes.OBEDIENCE, -1*sourceCharacter.getObedience()/4);
		}
	}
	public static class HornyTwoBuff extends Buff {
		public HornyTwoBuff(Charakter sourceCharacter) {
			super("buff.horny", new ImageData(), 3);
			setIcon(new ImageData("images/icons/Hearts.png"));


			addAttributeBuff(BaseAttributeTypes.CHARISMA, 2+sourceCharacter.getCharisma()/4);
			addAttributeBuff(BaseAttributeTypes.STAMINA, 2+sourceCharacter.getStamina()/4);
		}
	}
	public static class Seductive extends Buff {
		public Seductive(Charakter sourceCharacter) {
			super("buff.seductive", new ImageData(), 3);
			setIcon(new ImageData("images/icons/Hearts.png"));

			addAttributeBuff(BaseAttributeTypes.CHARISMA, 2+sourceCharacter.getCharisma()/4);
			addAttributeBuff(BaseAttributeTypes.STAMINA, 2+sourceCharacter.getStamina()/4);
		}
	}
	public static class Hyperactive extends Buff {
		public Hyperactive(Charakter sourceCharacter) {
			super("buff.hyperactive", new ImageData(), 3);
			setIcon(new ImageData("images/icons/Hearts.png"));
		}

		@Override
		public double modifyCalculatedAttribute(CalculatedAttribute calculatedAttribute, double currentValue, Person person) {
			if (calculatedAttribute == CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT) {
				return currentValue + 20;
			}						                
			else {
				return currentValue;
			}
		}
	}

	public static class Sore extends Buff {
		public Sore() {
			super("buff.sore", new ImageData(), 6);
		}

		@Override
		public String getDescription() {
			int sore = 0;
			if (getCharacter().getGender() == Gender.FEMALE) {
				sore = getCharacter().getFinalValue(Sextype.VAGINAL) / 4;
			} else {
				sore = getCharacter().getFinalValue(Sextype.ANAL) / 4;
			}
			Object arguments[] = {getRemainingTime(), sore};
			if(getCharacter().getGender() == Gender.FEMALE){
				return TextUtil.t("buff.sore.description.female", getCharacter(), arguments);
			}
			else{
				return TextUtil.t("buff.sore.description.male", getCharacter(), arguments);	            	
			}
		}

		@Override
		public void init() {
			setIcon(new ImageData("images/icons/perks/flame.png"));
			if (getCharacter().getGender() == Gender.FEMALE) {

				addAttributeBuff(Sextype.VAGINAL, -1 * getCharacter().getFinalValue(Sextype.VAGINAL) / 4);
			} else {
				addAttributeBuff(Sextype.ANAL, -1 * getCharacter().getFinalValue(Sextype.ANAL) / 4);
			}
			super.init();
		}
	}
}
