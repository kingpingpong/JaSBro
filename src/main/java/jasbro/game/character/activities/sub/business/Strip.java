package jasbro.game.character.activities.sub.business;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.conditions.Buff.RoughenedUp;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Strip extends RunningActivity implements BusinessSecondaryActivity {

	private MessageData messageData;
	private int bonus;

	@Override
	public void perform() {
		Charakter character = getCharacter();
		int skill = character.getCharisma() + character.getFinalValue(SpecializationAttribute.STRIP) / 4 + 1;

		int amountEarned = 0;
		int amountHappy = 0;
		int overalltips = 0;
		for (Customer customer : getCustomers()) {
			if (Util.getInt(0, 50) + skill + customer.getSatisfactionAmount() > 50) {
				amountHappy++;
				customer.addToSatisfaction(skill, this);
				int tips = (int)(customer.getMoney() / (1500.0 / skill) + Util.getInt(10, 20));
				tips = customer.pay(tips, getCharacter().getMoneyModifier());
				overalltips += tips;
				amountEarned +=  tips;
			}
			else {
				customer.addToSatisfaction(skill / 4, this);
			}
		}
		modifyIncome(amountEarned);

		if (amountEarned > 0) {
			messageData.addToMessage("\n\n" + TextUtil.t("strip.result.owned", getCharacter(), getCustomers().size(), amountHappy, getIncome(), overalltips));
		}
		else {
			messageData.addToMessage("\n\n" + TextUtil.t("strip.result.basic", getCharacter(), getCustomers().size(), amountHappy, getIncome(), overalltips));    		
		}

		if(character.getTraits().contains(Trait.EXTRAS) && getCustomers().size()>1){
			int chance=character.getCharisma()/5+character.getFinalValue(SpecializationAttribute.STRIP)/10;
			if(chance>80){chance=80;}
			int a=Util.getInt(0, getCustomers().size()-1);
			if(Util.getInt(0, 100)<chance && getCustomers().get(a).getType()!=CustomerType.BUM){
				
				int extra = (int)(getCustomers().get(a).getMoney() / (800.0 / character.getFinalValue(SpecializationAttribute.STRIP)) + Util.getInt(10, 20));
				String message = null;
				ImageData image;
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
				message=TextUtil.t("STRIP.extras", character,getCustomers().get(a).getStatusName(),getCustomers().get(a).getName(), extra);
				if(getCustomers().get(a).getPreferredSextype()==Sextype.VAGINAL && character.getAllowedServices().isAllowed(Sextype.VAGINAL)){
					if(getCustomers().get(a).getGender()==Gender.MALE){
						if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.NYMPHO)
								|| character.getTraits().contains(Trait.SLUT)
								|| character.getTraits().contains(Trait.HORNY))){
							this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.VAGINAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.vaginal.male.two", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
							getCustomers().get(a).addToSatisfaction(150,this);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
							if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							character.getFame().modifyFame(50);
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(100+character.getFinalValue(Sextype.VAGINAL)/5, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
							}
						}
						else{
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.VAGINAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.vaginal.male.one", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
							if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							getCustomers().get(a).addToSatisfaction(100, this);
						}
					}
					if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
						if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.NYMPHO)
								|| character.getTraits().contains(Trait.KINKY)
								|| character.getTraits().contains(Trait.SENSITIVE))){
							this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.VAGINAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.vaginal.female.two", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, character);
							getCustomers().get(a).addToSatisfaction(100,this);
							character.getFame().modifyFame(50);
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(100+character.getFinalValue(Sextype.VAGINAL)/5, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
							}
						}
						else{
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.VAGINAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.vaginal.female.one", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, character);
							getCustomers().get(a).addToSatisfaction(100, this);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
							if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
						}
					}
				}

				else if(getCustomers().get(a).getPreferredSextype()==Sextype.ANAL && character.getAllowedServices().isAllowed(Sextype.ANAL)){
					if(getCustomers().get(a).getGender()==Gender.MALE){
						if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.ROWDYRUMP)
								|| character.getTraits().contains(Trait.AMBITOUSLOVER)
								|| character.getTraits().contains(Trait.HORNY))){
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ANAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.anal.male.two", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
							getCustomers().get(a).addToSatisfaction(150,this);
							character.getFame().modifyFame(50);
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(100+character.getFinalValue(Sextype.ANAL)/5, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
							}
						}
						else{
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ANAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.anal.male.one", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
							getCustomers().get(a).addToSatisfaction(100, this);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
							if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(15, this);
							}
						}
					}
					if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
						Object arg2[] = {2+character.getFinalValue(Sextype.ANAL)/50, getCustomers().get(Util.getInt(0, getCustomers().size())).getName()};
						if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.KINKY)
								|| character.getTraits().contains(Trait.SUBMISSIVE)
								|| character.getTraits().contains(Trait.UNINHIBITED))){
							this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.ANAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.anal.female.two", character,arg2);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
							character.getFame().modifyFame(50);
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(80+character.getFinalValue(Sextype.ANAL)/2, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
							}
						}
						else{
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ANAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.anal.female.one", character,arg2);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
							getCustomers().get(a).addToSatisfaction(100, this);
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(25+character.getFinalValue(Sextype.ANAL)/4, this);
							}
						}
					}
				}

				else if(getCustomers().get(a).getPreferredSextype()==Sextype.ORAL && character.getAllowedServices().isAllowed(Sextype.ORAL)){
					if(getCustomers().get(a).getGender()==Gender.MALE){
						if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.CUMSLUT)
								|| character.getTraits().contains(Trait.SENSUALTONGUE)
								|| character.getTraits().contains(Trait.ABSORPTION))){
							this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.ORAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.oral.male.two", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
							getCustomers().get(a).addToSatisfaction(120,this);
							character.getFame().modifyFame(50);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
							if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(80+character.getFinalValue(Sextype.ORAL)/2, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
							}
						}
						else{
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ORAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.oral.male.one", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
							getCustomers().get(a).addToSatisfaction(100, this);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
							if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
						}
					}
					if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
						if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.SENSUALTONGUE)
								|| character.getTraits().contains(Trait.OPENMINDED)
								|| character.getTraits().contains(Trait.HORNY))){
							this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.ORAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.oral.female.two", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
							getCustomers().get(a).addToSatisfaction(75,this);
							character.getFame().modifyFame(50);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
							if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(80+character.getFinalValue(Sextype.FOREPLAY)/2, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
							}
						}
						else{
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ORAL, character));
							message+="\n" + TextUtil.t("STRIP.extras.oral.female.one", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, character);
							getCustomers().get(a).addToSatisfaction(100, this);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
							if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
						}
					}
				}
				else if(getCustomers().get(a).getPreferredSextype()==Sextype.TITFUCK && character.getAllowedServices().isAllowed(Sextype.TITFUCK) && character.getGender()==Gender.FEMALE){
					if(getCustomers().get(a).getGender()==Gender.MALE){
						if(character.getTraits().contains(Trait.SMALLBOOBS)){
							if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.CUMSLUT)
									|| character.getTraits().contains(Trait.WILD)
									|| character.getTraits().contains(Trait.OILY))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.TITFUCK, character));
								message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.two.small", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
								getCustomers().get(a).addToSatisfaction(75,this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK)/2, this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
								}
								character.getFame().modifyFame(50);
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, character));
								message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.one.small", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
								getCustomers().get(a).addToSatisfaction(100, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							}
						}
						else{
							if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.WENCH)
									|| character.getTraits().contains(Trait.WENCH)
									|| character.getTraits().contains(Trait.COMETOMOMMY))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.TITFUCK, character));
								message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.two.big", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
								getCustomers().get(a).addToSatisfaction(75,this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(80+character.getFinalValue(Sextype.FOREPLAY), this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
								}
								character.getFame().modifyFame(50);
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, character));
								message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.one.big", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
								getCustomers().get(a).addToSatisfaction(100, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(Util.getInt(-25, 25), this);
								}
							}
						}
					}
					if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
						if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.EXHIBITIONIST)
								|| character.getTraits().contains(Trait.UNINHIBITED)
								|| character.getTraits().contains(Trait.TOUCHYFEELY))){
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, character));
							message+="\n" + TextUtil.t("STRIP.extras.titfuck.female.two", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
							getCustomers().get(a).addToSatisfaction(75,this);
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(80+character.getFinalValue(Sextype.FOREPLAY), this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
							}
							character.getFame().modifyFame(50);
						}
						else{
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, character));
							message+="\n" + TextUtil.t("STRIP.extras.titfuck.female.one", character);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
							getCustomers().get(a).addToSatisfaction(100, this);
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(25+character.getFinalValue(Sextype.FOREPLAY), this);
							}
						}
					}
				}

				else if(Util.getInt(0, 10)>4 || getCustomers().size()<8){
					if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.SENSITIVE)
							|| character.getTraits().contains(Trait.NICEBODY)
							|| character.getTraits().contains(Trait.TOUCHYFEELY))){
						this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.FOREPLAY, character));
						message+="\n" + TextUtil.t("STRIP.extras.lapdance.two", character,getCustomers().get(a));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, character);
						character.getFame().modifyFame(50);
						if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
						if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
						for(int cust=0; cust<getCustomers().size(); cust++){
							getCustomers().get(cust).addToSatisfaction(25+(character.getFinalValue(Sextype.FOREPLAY)+character.getFinalValue(SpecializationAttribute.STRIP))/2, this);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
							if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
						}
					}
					else{
						this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.FOREPLAY, character));
						message+="\n" + TextUtil.t("STRIP.extras.lapdance.one", character,getCustomers().get(a));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, character);
						character.getFame().modifyFame(20);
						if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
						if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
						for(int cust=0; cust<getCustomers().size(); cust++){
							getCustomers().get(cust).addToSatisfaction(25+(character.getFinalValue(Sextype.FOREPLAY)+character.getFinalValue(SpecializationAttribute.STRIP))/4, this);
						}
					}

				}

				else{//group
					if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.GANGBANGQUEEN)
							|| character.getTraits().contains(Trait.INSATIABLE)
							|| character.getTraits().contains(Trait.MULTIFACETED))){
						this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.GROUP, character));
						message+="\n" + TextUtil.t("STRIP.extras.group.two", character,getCustomers().get(a));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
						this.getHouse().modDirt(70);
						character.getFame().modifyFame(450);
						character.addCondition(new Buff.RoughenedUp());
						for(int cust=0; cust<getCustomers().size(); cust++){
							getCustomers().get(cust).addToSatisfaction(25+character.getFinalValue(Sextype.GROUP)*2, this);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()!=CustomerStatus.STRONGSTATUS )){getCustomers().get(cust).setStatus(CustomerStatus.TIRED);}
						}
					}
					else{
						this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.GROUP, character));
						message+="\n" + TextUtil.t("STRIP.extras.group.one", character,getCustomers().get(a));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
						character.getFame().modifyFame(100);
						for(int cust=0; cust<8; cust++){
							getCustomers().get(cust).addToSatisfaction(25+character.getFinalValue(Sextype.GROUP), this);
						}
						for(int cust=8; cust<getCustomers().size(); cust++){
							getCustomers().get(cust).addToSatisfaction(-50, this);
						}
					}

				}
				if(message!=null){getMessages().add(new MessageData(message, image, character.getBackground()));}
			}
		}
		
	}

	@Override
	public MessageData getBaseMessage() {
		String messageText = TextUtil.t("strip.basic", getCharacter());
		this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, getCharacter()), 
				getBackground());
		return this.messageData;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, -30, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 1.1f, SpecializationAttribute.STRIP));
		modifications.add(new ModificationData(TargetType.ALL, 0.02f, BaseAttributeTypes.STRENGTH));
		modifications.add(new ModificationData(TargetType.ALL, 0.02f, BaseAttributeTypes.STAMINA));
		modifications.add(new ModificationData(TargetType.ALL, 0.05f, BaseAttributeTypes.CHARISMA));
		modifications.add(new ModificationData(TargetType.SLAVE, 0.02f, BaseAttributeTypes.OBEDIENCE));
		modifications.add(new ModificationData(TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
		return modifications;
	}

	@Override
	public int getAppeal() {
		return (getCharacter().getCharisma() + getCharacter().getFinalValue(SpecializationAttribute.STRIP) / 4) / 4;
	}

	@Override
	public int getMaxAttendees() {
		return 20+bonus;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

}
