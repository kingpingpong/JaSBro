package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.Idle;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.Unit;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Fight extends RunningActivity implements BusinessMainActivity, BusinessSecondaryActivity {
	private Unit fighter1;
	private Unit fighter2;
	private MessageData message;
	
	@Override
	public void init() {
		fighter1 = getCharacter();
		if (getCharacters().size() == 2) {
			fighter2 = getCharacters().get(1);
		}
		else if (getMainCustomer() != null) {
			fighter2 = getMainCustomer();
		}
	}
	
	@Override
	public MessageData getBaseMessage() {
		message = new MessageData();
		message.setBackground(getCharacter().getBackground());
		if (fighter2 != null) {
			Object arguments [] = {getCustomers().size()};
			if (getCharacters().size() == 2) {
				ImageData image1 = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, getCharacters().get(0));
				ImageData image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, getCharacters().get(1));
				message.setImage(image1);
				message.setImage2(image2);
				message.setMessage(TextUtil.t("fight.basic1", getCharacter(), getCharacters().get(1), arguments));
			}
			else {
				ImageData image = ImageUtil.getInstance().getImageDataByTag(ImageTag.FIGHT, getCharacter());
				message.setImage(image);
				message.setMessage(TextUtil.t("fight.basic2", getCharacter(), getMainCustomer(), arguments));
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
		List<ModificationData> modificationData  = new ArrayList<ModificationData>();
		if (fighter2 != null) {
			modificationData.add(new ModificationData(TargetType.ALL, 0.2f, BaseAttributeTypes.STRENGTH));
			modificationData.add(new ModificationData(TargetType.ALL, 1.0f, SpecializationAttribute.VETERAN));
			modificationData.add(new ModificationData(TargetType.ALL, -40, EssentialAttributes.ENERGY));
		}
		else {
			return (new Idle()).getStatModifications();
		}
		return modificationData;
	}
	
	
	@Override
	public int getAppeal() {
		return Util.getInt(2, 8);
	}
	
	@Override
	public void perform() {
		if (fighter2 != null) {
			float entertainmentRating = 0.3f;
			int i = 0;
			float mod1 = 0;
			float mod2 = 0;
			int startHitPoints1 = fighter1.getHitpoints();
			int startHitPoints2 = fighter2.getHitpoints();
			Battle battle = new Battle(fighter1, fighter2);
			do {
				battle.doRound();
				entertainmentRating += 0.003f;
				i++;
			}
			while (i < 50 && fighter1.getHitpoints() > 20 && fighter2.getHitpoints() > 20 &&
					fighter1.getHitpoints() > startHitPoints1 - 45 && fighter2.getHitpoints() > startHitPoints2 - 45);
			mod1 = startHitPoints1 - fighter1.getHitpoints();
			mod2 = startHitPoints2 - fighter2.getHitpoints();
			
			entertainmentRating = entertainmentRating + (Math.abs((mod1 + mod2))) / 100.0f + 
					fighter1.getDamage() / 20.0f + fighter1.getArmor() / 1000f + 
					fighter2.getDamage() / 20.0f + fighter2.getArmor() / 1000f;
			for (Customer customer : getCustomers()) {
				customer.addToSatisfaction((int)(entertainmentRating * 25), this);
			}
			
			addAttributeModification(this, mod1, getCharacter(), EssentialAttributes.HEALTH);
			if (getCharacters().size() == 2) {
				addAttributeModification(this, mod2, getCharacters().get(1), EssentialAttributes.HEALTH);
			}
			
			int winnings = 0;
			for (Customer customer : getCustomers()) {
				winnings += customer.pay(entertainmentRating);
			}
			modifyIncome(winnings);
	
			Unit winner = null;
			if (i <= 100) {
				if (fighter1.getHitpoints() <= 20 || fighter1.getHitpoints() <= startHitPoints1 - 45) {
					winner = fighter2;
				}
				else {
					winner = fighter1;
				}
				
				if (getMainCustomer() != null) {
				    if (getMainCustomer().getType() == CustomerType.SOLDIER) {
				        getMainCustomer().addToSatisfaction(40, this);
				    }
				    if (winner == getMainCustomer()) {
				        getMainCustomer().addToSatisfaction(40, this);
				    }
				}
			}
			
			message.addToMessage("\n\n" + battle.getCombatText());
			
			Object arguments [] = {winnings};
			String message = TextUtil.t("fight.result", arguments);
			if (winner == null) {
				message += "\n\n" + TextUtil.t("fight.draw");
			}
			else {
				arguments[0] = winner.getName();
				if (i < 8) {
					message += "\n\n" + TextUtil.t("fight.onesided", arguments);
				}
				else {
					message += "\n\n" + TextUtil.t("fight.longMatch", arguments);
				}				
			}
			if(getCharacter().getTraits().contains(Trait.SHOWTIME))
			{
				int rand=Util.getInt(1, 10);
				switch (rand){
				case 1:
					if(getCharacter().getFinalValue(BaseAttributeTypes.STRENGTH)>50){message += "\n\n" + TextUtil.t("fight.showtime.strong", getCharacter());}
					break;
				case 2:
					if(getCharacter().getTraits().contains(Trait.DISTRACTION) && getCharacter().getGender()==Gender.FEMALE){message += "\n\n" + TextUtil.t("fight.showtime.distraction", getCharacter());}
					break;
				case 3:
					if(getCharacter().getGender()==Gender.FEMALE){message += "\n\n" + TextUtil.t("fight.showtime.loseclothes", getCharacter());}
					break;
				case 4:
					if(getCharacter().getFinalValue(BaseAttributeTypes.STAMINA)>50){message += "\n\n" + TextUtil.t("fight.showtime.finalform", getCharacter());}
					break;
				case 5:
					if(getCharacter().getFinalValue(BaseAttributeTypes.INTELLIGENCE)>50 && getCharacter().getTraits().contains(Trait.CASTTIME)){message += "\n\n" + TextUtil.t("fight.showtime.summon", getCharacter());}
					break;
				case 6:
					if(getCharacter().getFinalValue(SpecializationAttribute.SEDUCTION)>180 && getCharacter().getGender()==Gender.FEMALE){message += "\n\n" + TextUtil.t("fight.showtime.seduction", getCharacter());}
					break;
				case 7:
					if(getCharacter().getTraits().contains(Trait.TOUGH)){message += "\n\n" + TextUtil.t("fight.showtime.tough", getCharacter());}
					break;
				case 8:
					if(getCharacter().getFinalValue(BaseAttributeTypes.INTELLIGENCE)>50 && getCharacter().getTraits().contains(Trait.ELEMENTALSTUDY)){message += "\n\n" + TextUtil.t("fight.showtime.blazehug", getCharacter());}
					break;
				case 9:
					if(getCharacter().getFinalValue(BaseAttributeTypes.CHARISMA)>50){message += "\n\n" + TextUtil.t("fight.showtime.cool", getCharacter());}
					break;
				case 10:
					if(getCharacter().getTraits().contains(Trait.LOSTARTS)){message += "\n\n" + TextUtil.t("fight.showtime.lostarts", getCharacter());}
					break;
				default:
					break;


				}
				
				
				
			}
			
			if (getCharacters().size() > 1) {
				ImageData image1;
				ImageData image2;
				if (winner == null) {
					image1 = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, getCharacters().get(0));
					image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, getCharacters().get(1));
				}
				else  if (winner == getCharacters().get(0)) {
					image1 = ImageUtil.getInstance().getImageDataByTag(ImageTag.VICTORIOUS, getCharacters().get(0));
					image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, getCharacters().get(1));
				}
				else {
					image1 = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, getCharacters().get(0));
					image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.VICTORIOUS, getCharacters().get(1));
				}
				getMessages().add(new MessageData(message, image1, image2, getCharacter().getBackground()));
			}
			else {
				ImageData image;
				if (winner == getCharacters().get(0)) {
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VICTORIOUS, getCharacter());
					getMessages().add(new MessageData(message, image, getCharacter().getBackground()));
				}
				else {
					if(getCharacter().getGender()==Gender.FEMALE
							&& Util.getInt(1, 3)==2
							&& getMainCustomer().getGender()==Gender.MALE)
						{
							ImageData image1;
							ImageData image2;
							image1 = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, getCharacter());
							image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, getCharacter());
							message += "\n\n" + TextUtil.t("fight.lost", getCharacter());
							if(getMainCustomer().getPreferredSextype()==Sextype.ANAL){
								image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, getCharacter());
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.ANAL, getCharacter()));
								message+="\n" + TextUtil.t("fight.lost.anal", getCharacter());
							}
							else if(getMainCustomer().getPreferredSextype()==Sextype.VAGINAL){
								image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, getCharacter());
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.VAGINAL, getCharacter()));
								message+="\n" + TextUtil.t("fight.lost.vaginal", getCharacter());
							}
							else if(getMainCustomer().getPreferredSextype()==Sextype.ORAL){
								image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, getCharacter());
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.ORAL, getCharacter()));
								message+="\n" + TextUtil.t("fight.lost.oral", getCharacter());
							}
							else if(getMainCustomer().getPreferredSextype()==Sextype.BONDAGE){
								image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.BONDAGE, getCharacter());
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.BONDAGE, getCharacter()));
								message+="\n" + TextUtil.t("fight.lost.bondage", getCharacter());
							}
							else{
								image2 = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, getCharacter());
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.GROUP, getCharacter()));
								message+="\n" + TextUtil.t("fight.lost.group", getCharacter());
							}
							
							getMessages().add(new MessageData(message, image1, image2,  getCharacter().getBackground()));
						}
					else{
                        image = ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, getCharacter());
                        getMessages().add(new MessageData(message, image, getCharacter().getBackground()));
					}
				}
				
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
		if (getCharacters().size() > 1) {
			return 0;
		}
		else {
			if (customer.getType() == CustomerType.SOLDIER) {
				return 100;
			}
			else if (customer.getType() == CustomerType.GROUP && getCharacter().getFinalValue(SpecializationAttribute.VETERAN)>200) {
				return Util.getInt(90, 125);
			}
			else {
				return (int)customer.getImportance();
			}
		}
	}


	@Override
	public int getMaxAttendees() {
		return 40;
	}
}
