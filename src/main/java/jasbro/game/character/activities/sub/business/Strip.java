package jasbro.game.character.activities.sub.business;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Strip extends RunningActivity implements BusinessSecondaryActivity {

	private MessageData messageData;
	private int bonus;
	public enum StripAction {
		EXTRAS, 
		SHY, FRAGILE, STIFF, CLUMSY, SUBMISSIVE,
		UNHINIBITED, FIT, WILD, OILY, BIGTITS,
		RUBCLIT, LICKPOLE, CUMDRINK, FONDLE,
		CREAM, SEXSMELL, FELINEORGY
		;
	}
	private Map<Charakter, StripAction> characterAction=new HashMap<Charakter, StripAction>();

	@Override
	public void perform() {
		Charakter character = getCharacter();
		int skill = character.getCharisma()/5 + character.getFinalValue(SpecializationAttribute.STRIP) / 5 + 1;


		int amountEarned = 0;
		int amountHappy = 0;
		int overalltips = 0;
		int tip=0;
		int chanceOfTip=25+character.getCharisma()+character.getFinalValue(SpecializationAttribute.STRIP);
		float chanceModifier=1;
		for (Customer customer : getCustomers()) {
			switch(customer.getType()){
			case PEASANT:
				chanceModifier=1.7f;
				break;
			case SOLDIER:
				chanceModifier=1.2f;
				break;
			case MERCHANT:
				chanceModifier=1.0f;
				break;
			case BUSINESSMAN:
				chanceModifier=0.8f;
				break;
			case MINORNOBLE:
				chanceModifier=0.6f;
				break;
			case LORD:
				chanceModifier=0.4f;
				break;
			case CELEBRITY:
				chanceModifier=0.2f;
				break;
			default:
				chanceModifier=2f;
			}
			
			if(Util.getInt(0, 100)<chanceModifier*chanceOfTip){
				tip=customer.getMoney()*Util.getInt(6, 12)/100;				
				tip = customer.pay(tip, getCharacter().getMoneyModifier());
				amountHappy++;
				customer.addToSatisfaction(skill, this);
				overalltips += tip;
				amountEarned +=  tip;
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

		List<StripAction> actions = new ArrayList<StripAction>();

		if(character.getTraits().contains(Trait.EXTRAS)){
			actions.add(StripAction.EXTRAS);
			actions.add(StripAction.EXTRAS);
			actions.add(StripAction.EXTRAS);
			actions.add(StripAction.EXTRAS);
		}

		if(character.getTraits().contains(Trait.SHY))
			actions.add(StripAction.SHY);
		if(character.getTraits().contains(Trait.UNINHIBITED)){
			actions.add(StripAction.UNHINIBITED);
			actions.add(StripAction.LICKPOLE);
		}
		if(character.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString())>7)
			actions.add(StripAction.SEXSMELL);
		if(character.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString())>14)
			actions.add(StripAction.SEXSMELL);
		if(character.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString())>14)
			actions.add(StripAction.SEXSMELL);
		if(character.getTraits().contains(Trait.STIFF))
			actions.add(StripAction.STIFF);
		if(character.getTraits().contains(Trait.HORNY))
			actions.add(StripAction.RUBCLIT);
		if(character.getTraits().contains(Trait.FIT))
			actions.add(StripAction.FIT);
		if(character.getTraits().contains(Trait.FRAGILE))
			actions.add(StripAction.FRAGILE);
		if(character.getTraits().contains(Trait.OILY))
			actions.add(StripAction.OILY);
		if(character.getTraits().contains(Trait.SUBMISSIVE))
			actions.add(StripAction.SUBMISSIVE);
		if(character.getTraits().contains(Trait.CUMSLUT))
			actions.add(StripAction.CUMDRINK);
		if(character.getTraits().contains(Trait.AFLEURDEPEAU))
			actions.add(StripAction.FONDLE);
		if(character.getTraits().contains(Trait.FELINEHEAT) && Jasbro.getInstance().getData().getDay()%15==0){
			actions.add(StripAction.FELINEORGY);
			actions.add(StripAction.FELINEORGY);
			actions.add(StripAction.FELINEORGY);
			actions.add(StripAction.FELINEORGY);
			actions.add(StripAction.FELINEORGY);
			actions.add(StripAction.FELINEORGY);
			actions.add(StripAction.FELINEORGY);
			actions.add(StripAction.FELINEORGY);
		}
		if(character.getFinalValue(SpecializationAttribute.COOKING)>255)
			actions.add(StripAction.CREAM);




		if(Util.getInt(0, 100)<10+actions.size()*6 && getCustomers().size()>10 && actions.size()>0)
		{
			characterAction.put(character, actions.get(Util.getInt(0, actions.size())));
			String message = null;
			ImageData image;
			image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, getCharacter());
			int a=Util.getInt(0, getCustomers().size()-1);
			Object arg[] = {getCustomers().get(a).getName()};
			int rnd=0;
			switch(characterAction.get(character)){
			case EXTRAS:
				if(getCustomers().get(a).getType()!=CustomerType.BUM){
					character.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1);
					int extra = (int)(getCustomers().get(a).getMoney() * character.getFinalValue(SpecializationAttribute.STRIP)/300);
					modifyIncome(extra);
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
					message=TextUtil.t("STRIP.extras", character,getCustomers().get(a).getStatusName(),getCustomers().get(a).getName(), extra);
					if(getCustomers().get(a).getPreferredSextype()==Sextype.VAGINAL && character.getAllowedServices().isAllowed(Sextype.VAGINAL)){
						if(getCustomers().get(a).getGender()==Gender.MALE){
							if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.NYMPHO)
									|| character.getTraits().contains(Trait.SLUT)
									|| character.getTraits().contains(Trait.HORNY))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.VAGINAL, character));
								this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
								message+="\n" + TextUtil.t("STRIP.extras.vaginal.male.two", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL)/5,this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
								character.getFame().modifyFame(50);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL)/10, this);
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
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL)/7, this);
							}
						}
						if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
							if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.NYMPHO)
									|| character.getTraits().contains(Trait.KINKY)
									|| character.getTraits().contains(Trait.SENSITIVE))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.VAGINAL, character));
								this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
								message+="\n" + TextUtil.t("STRIP.extras.vaginal.female.two", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/5,this);
								character.getFame().modifyFame(50);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/10, this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.VAGINAL, character));
								message+="\n" + TextUtil.t("STRIP.extras.vaginal.female.one", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/7, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							}
						}
					}

					else if(getCustomers().get(a).getPreferredSextype()==Sextype.ANAL && character.getAllowedServices().isAllowed(Sextype.ANAL)){
						if(getCustomers().get(a).getGender()==Gender.MALE && Util.getInt(0, 10)>5){
							if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.ROWDYRUMP)
									|| character.getTraits().contains(Trait.AMBITOUSLOVER)
									|| character.getTraits().contains(Trait.HORNY))){
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ANAL, character));
								this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
								message+="\n" + TextUtil.t("STRIP.extras.anal.male.two", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ANAL)/5,this);
								character.getFame().modifyFame(50);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL)/10, this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ANAL, character));
								message+="\n" + TextUtil.t("STRIP.extras.anal.male.one", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ANAL)/7, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL)/7, this);
								}
							}
						}
						else {
							int analBead=2+character.getFinalValue(Sextype.ANAL)/10;
							if(character.getTraits().contains(Trait.DEEPLOVE))
								analBead*=1.5;
							Object arg2[] = {analBead, getCustomers().get(Util.getInt(0, getCustomers().size())).getName()};
							if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.KINKY)
									|| character.getTraits().contains(Trait.SUBMISSIVE)
									|| character.getTraits().contains(Trait.UNINHIBITED))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.ANAL, character));
								this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
								message+="\n" + TextUtil.t("STRIP.extras.anal.female.two", character,arg2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
								character.getFame().modifyFame(50);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(analBead+character.getFinalValue(Sextype.ANAL)/12, this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ANAL, character));
								message+="\n" + TextUtil.t("STRIP.extras.anal.female.one", character,arg2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ANAL)/7, this);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL)/12, this);
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
								this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
								message+="\n" + TextUtil.t("STRIP.extras.oral.male.two", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ORAL)/5,this);
								character.getFame().modifyFame(50);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ORAL)/10, this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ORAL, character));
								message+="\n" + TextUtil.t("STRIP.extras.oral.male.one", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ORAL)/7, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							}
						}
						if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
							if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.SENSUALTONGUE)
									|| character.getTraits().contains(Trait.OPENMINDED)
									|| character.getTraits().contains(Trait.HORNY))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.ORAL, character));
								this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
								message+="\n" + TextUtil.t("STRIP.extras.oral.female.two", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/5,this);
								character.getFame().modifyFame(50);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/10, this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ORAL, character));
								message+="\n" + TextUtil.t("STRIP.extras.oral.female.one", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.ORAL)/7, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							}
						}
					}
					else if(getCustomers().get(a).getPreferredSextype()==Sextype.TITFUCK && character.getAllowedServices().isAllowed(Sextype.TITFUCK) && character.getGender()==Gender.FEMALE && (character.getTraits().contains(Trait.SMALLBOOBS) || character.getTraits().contains(Trait.BIGBOOBS))){
						if(getCustomers().get(a).getGender()==Gender.MALE){
							if(character.getTraits().contains(Trait.SMALLBOOBS)){
								if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.CUMSLUT)
										|| character.getTraits().contains(Trait.WILD)
										|| character.getTraits().contains(Trait.OILY))){
									this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.TITFUCK, character));
									this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
									message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.two.small", character);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
									getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK)/5,this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
									if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
									for(int cust=0; cust<getCustomers().size(); cust++){
										getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK)/10, this);
										if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
										if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
									}
									character.getFame().modifyFame(50);
								}
								else{
									this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, character));
									message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.one.small", character);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
									getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK)/7, this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
									if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
								}
							}
							else if(character.getTraits().contains(Trait.BIGBOOBS)){
								if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.WENCH)
										|| character.getTraits().contains(Trait.WENCH)
										|| character.getTraits().contains(Trait.COMETOMOMMY))){
									this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.TITFUCK, character));
									this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
									message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.two.big", character);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
									getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK)/5,this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
									if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
									for(int cust=0; cust<getCustomers().size(); cust++){
										getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK)/10, this);
										if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
										if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
									}
									character.getFame().modifyFame(50);
								}
								else{
									this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, character));
									message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.one.big", character);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
									getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK)/7, this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
									if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
									for(int cust=0; cust<getCustomers().size(); cust++){
										getCustomers().get(cust).addToSatisfaction(Util.getInt(-15, 15), this);
									}
								}
							}
						}
						if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
							if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.EXHIBITIONIST)
									|| character.getTraits().contains(Trait.UNINHIBITED)
									|| character.getTraits().contains(Trait.TOUCHYFEELY))){
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, character));
								this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
								message+="\n" + TextUtil.t("STRIP.extras.titfuck.female.two", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/5,this);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/10, this);
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.LIVELY || getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(cust).setStatus(CustomerStatus.HORNYSTATUS);}
									if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(cust).getStatus()==CustomerStatus.HYPED)){getCustomers().get(cust).setStatus(CustomerStatus.VERYHORNY);}
								}
								character.getFame().modifyFame(50);
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, character));
								message+="\n" + TextUtil.t("STRIP.extras.titfuck.female.one", character);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, character);
								getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/7, this);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(10+character.getFinalValue(Sextype.FOREPLAY), this);
								}
							}
						}
					}

					else if(Util.getInt(0, 10)>4 || getCustomers().size()<8){
						if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.SENSITIVE)
								|| character.getTraits().contains(Trait.NICEBODY)
								|| character.getTraits().contains(Trait.TOUCHYFEELY))){
							this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.FOREPLAY, character));
							this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
							message+="\n" + TextUtil.t("STRIP.extras.lapdance.two", character,getCustomers().get(a));
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, character);
							character.getFame().modifyFame(50);
							if(Util.getInt(1, 3)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.LIVELY || getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
							if(Util.getInt(1, 2)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction((character.getFinalValue(Sextype.FOREPLAY)+character.getFinalValue(SpecializationAttribute.STRIP))/10, this);
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
								getCustomers().get(cust).addToSatisfaction((character.getFinalValue(Sextype.FOREPLAY)+character.getFinalValue(SpecializationAttribute.STRIP))/15, this);
							}
						}

					}

					else{//group
						if(Util.getInt(0, 10)>5 && (character.getTraits().contains(Trait.GANGBANGQUEEN)
								|| character.getTraits().contains(Trait.INSATIABLE)
								|| character.getTraits().contains(Trait.MULTIFACETED))){
							character.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) getCustomers().size()-1);
							this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.GROUP, character));
							this.getAttributeModifications().add(new AttributeModification(2.0f,EssentialAttributes.MOTIVATION, character));
							message+="\n" + TextUtil.t("STRIP.extras.group.two", character,getCustomers().get(a));
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
							this.getHouse().modDirt(70);
							character.getFame().modifyFame(450);
							character.addCondition(new Buff.RoughenedUp());
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(10+character.getFinalValue(Sextype.GROUP)/5, this);
								if(Util.getInt(1, 3)==1 && (getCustomers().get(cust).getStatus()!=CustomerStatus.STRONGSTATUS )){getCustomers().get(cust).setStatus(CustomerStatus.TIRED);}
							}
						}
						else{
							character.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) Util.getInt(1, 2+getCustomers().size()/4));
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.GROUP, character));
							message+="\n" + TextUtil.t("STRIP.extras.group.one", character,getCustomers().get(a));
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
							character.getFame().modifyFame(100);
							for(int cust=0; cust<8; cust++){
								getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.GROUP)/7, this);
							}
							for(int cust=8; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(-15, this);
							}
						}

					}

				}
				break;
			case SHY:
				if(character.getTraits().contains(Trait.PURE)){
					this.getAttributeModifications().add(new AttributeModification(0.2f,EssentialAttributes.MOTIVATION, character));
					message=TextUtil.t("STRIP.event.shy.win", character,getCustomers().get(a));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
					for(int cust=0; cust<getCustomers().size(); cust++){
						getCustomers().get(cust).addToSatisfaction(10, this);
					}
				}
				else{
					this.getAttributeModifications().add(new AttributeModification(-0.1f,EssentialAttributes.MOTIVATION, character));
					message=TextUtil.t("STRIP.event.shy.lose", character,getCustomers().get(a));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
					for(int cust=0; cust<getCustomers().size(); cust++){
						getCustomers().get(cust).addToSatisfaction(-10, this);
					}
				}
				break;
			case UNHINIBITED:
				if(character.getTraits().contains(Trait.LEWD) || Util.getInt(0, 3)==2){
					this.getAttributeModifications().add(new AttributeModification(0.1f,EssentialAttributes.MOTIVATION, character));
					message= TextUtil.t("STRIP.event.lewd.win", character,getCustomers().get(a));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
					for(int cust=0; cust<getCustomers().size(); cust++){
						getCustomers().get(cust).addToSatisfaction(10, this);
					}
				}
				else{
					message= TextUtil.t("STRIP.event.lewd.lose", character,getCustomers().get(a));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
					for(int cust=0; cust<getCustomers().size(); cust++){
						getCustomers().get(cust).addToSatisfaction(Util.getInt(-5, 5), this);
					}
				}
				break;
			case FRAGILE:
				if(character.getTraits().contains(Trait.LASCIVIOUS)){
					this.getAttributeModifications().add(new AttributeModification(0.1f,EssentialAttributes.MOTIVATION, character));
					message= TextUtil.t("STRIP.event.fragile.win", character,getCustomers().get(a));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
					for(int cust=0; cust<getCustomers().size(); cust++){
						getCustomers().get(cust).addToSatisfaction(7, this);

					}
				}
				else{
					this.getAttributeModifications().add(new AttributeModification(-0.1f,EssentialAttributes.MOTIVATION, character));
					message=TextUtil.t("STRIP.event.fragile.lose", character,getCustomers().get(a));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
					for(int cust=0; cust<getCustomers().size(); cust++){
						getCustomers().get(cust).addToSatisfaction(-15, this);
					}
				}
				break;
			case FIT:
				if(character.getTraits().contains(Trait.PERFECTCONDITION) && Util.getInt(0, 3)==1){
					this.getAttributeModifications().add(new AttributeModification(0.2f,EssentialAttributes.MOTIVATION, character));
					message= TextUtil.t("STRIP.event.fit.win", character,getCustomers().get(a));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
					for(int cust=0; cust<getCustomers().size(); cust++){
						getCustomers().get(cust).addToSatisfaction(skill/5, this);
					}
				}
				else{					
					message= TextUtil.t("STRIP.event.fit.winwin", character,getCustomers().get(a));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
					for(int cust=0; cust<getCustomers().size(); cust++){
						getCustomers().get(cust).addToSatisfaction(10, this);
						if(getCustomers().get(cust).getStatus()==CustomerStatus.SHYSTATUS && Util.getInt(1, 4)==2)
							getCustomers().get(cust).setStatus(CustomerStatus.TIRED);
						if(getCustomers().get(cust).getStatus()==CustomerStatus.TIRED && Util.getInt(1, 4)==2)
							getCustomers().get(cust).setStatus(CustomerStatus.LIVELY);
					}
				}
				break;
			case STIFF:
				message= TextUtil.t("STRIP.event.stiff", character,getCustomers().get(a));
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character);
				for(int cust=0; cust<getCustomers().size(); cust++){
					getCustomers().get(cust).addToSatisfaction(-skill/2, this);
				}				
				break;
			case BIGTITS:
				message= TextUtil.t("STRIP.event.bigtits", character,getCustomers().get(a));
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, character);
				for(int cust=0; cust<getCustomers().size(); cust++){
					getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.TITFUCK)/7, this);
				}				
				break;
			case CLUMSY:
				if(Util.getInt(0, 3)==2){
					this.getAttributeModifications().add(new AttributeModification(-0.1f,EssentialAttributes.MOTIVATION, character));
					message= TextUtil.t("STRIP.event.clumsy.win", character,getCustomers().get(a));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
					for(int cust=0; cust<getCustomers().size(); cust++){
						getCustomers().get(cust).addToSatisfaction(Util.getInt(-5, 7), this);
					}
				}
				else{
					this.getAttributeModifications().add(new AttributeModification(-0.3f,EssentialAttributes.MOTIVATION, character));
					message= TextUtil.t("STRIP.event.clumsy.lose", character,getCustomers().get(a));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
					for(int cust=0; cust<getCustomers().size(); cust++){
						getCustomers().get(cust).addToSatisfaction(-15, this);
					}
				}
				break;
			case RUBCLIT:
				message=TextUtil.t("STRIP.event.rubclit", character,getCustomers().get(a));
				this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character);
				for(int cust=0; cust<getCustomers().size(); cust++){
					getCustomers().get(cust).addToSatisfaction(5+character.getFinalValue(Sextype.FOREPLAY)/10, this);
				}				
				break;
			case LICKPOLE:
				message=TextUtil.t("STRIP.event.lickpole", character,getCustomers().get(a));
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
				for(int cust=0; cust<getCustomers().size(); cust++){
					getCustomers().get(cust).addToSatisfaction(5+character.getFinalValue(SpecializationAttribute.SEDUCTION)/10, this);
				}				
				break;
			case OILY:
				message=TextUtil.t("STRIP.event.oily", character,getCustomers().get(a));
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character);
				for(int cust=0; cust<getCustomers().size(); cust++){
					getCustomers().get(cust).addToSatisfaction(5+character.getCharisma()/5, this);
				}				
				break;
			case FELINEORGY:
				message=TextUtil.t("STRIP.event.felineorgy", character,getCustomers().get(a));
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character);
				for(int cust=0; cust<getCustomers().size(); cust++){
					getCustomers().get(cust).addToSatisfaction(character.getCharisma()/5, this);
					getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ANAL)/5, this);
					getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.VAGINAL)/5, this);
					getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.ORAL)/5, this);
					this.getAttributeModifications().add(new AttributeModification(-0.5f,EssentialAttributes.ENERGY, character));
					this.getAttributeModifications().add(new AttributeModification(0.5f,Sextype.GROUP, character));
					if(Util.getInt(0, 100)<getCustomers().size())
						character.addCondition(new Buff.Exhausted());
					character.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1);
				}				
				break;
			case CUMDRINK:
				message=TextUtil.t("STRIP.event.cumdrink", character,getCustomers().get(a));
				this.getAttributeModifications().add(new AttributeModification(1.0f,EssentialAttributes.MOTIVATION, character));
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BUKKAKE, character);
				character.getFame().modifyFame(250);
				for(int cust=0; cust<getCustomers().size(); cust++){
					getCustomers().get(cust).addToSatisfaction(10+(character.getFinalValue(Sextype.ORAL)+character.getFinalValue(Sextype.FOREPLAY))/12, this);
				}				
				break;
			case SEXSMELL:
				if(Util.getInt(0, 2)==0)
					message=TextUtil.t("STRIP.event.sexsmell.smell", character,getCustomers().get(a));
				else
					message=TextUtil.t("STRIP.event.sexsmell.move", character,getCustomers().get(a));
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.AFTERSEX, character);
				character.getFame().modifyFame(150);
				for(int cust=0; cust<getCustomers().size(); cust++){
					getCustomers().get(cust).addToSatisfaction(15, this);
				}				
				break;
			case FONDLE:
				rnd=Util.getInt(0, 3);
				if(character.getTraits().contains(Trait.BIGBOOBS) && rnd==0)
					message=TextUtil.t("STRIP.event.fondle.bigbreasts", character,getCustomers().get(a));
				if(character.getTraits().contains(Trait.SMALLBOOBS) && rnd==1)
					message=TextUtil.t("STRIP.event.fondle.smallbreasts", character,getCustomers().get(a));
				else
					message=TextUtil.t("STRIP.event.fondle.butt", character,getCustomers().get(a));
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, character);
				for(int cust=0; cust<getCustomers().size(); cust++){
					if(getCustomers().get(cust).getType()==CustomerType.MINORNOBLE || 
							getCustomers().get(cust).getType()==CustomerType.BUSINESSMAN || 
							getCustomers().get(cust).getType()==CustomerType.LORD || 
							getCustomers().get(cust).getType()==CustomerType.CELEBRITY  )
						getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/5, this);
				}				
				break;
			case CREAM:
				message=TextUtil.t("STRIP.event.cream", character,getCustomers().get(a));
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.MASTURBATION, character);
				for(int cust=0; cust<getCustomers().size(); cust++){
					getCustomers().get(cust).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/7, this);
				}				
				break;
			case SUBMISSIVE:
				message=TextUtil.t("STRIP.event.submissive", character,arg) + "\n";
				character.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1);
				rnd=Util.getInt(0, 3);
				switch(rnd){
				case 0:
					if(character.getGender()!=Gender.MALE){
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character);
						message+=TextUtil.t("STRIP.event.submissive.vaginal", character,arg);
						getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/7, this);
					}
					break;
				case 1:
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character);
					message+=TextUtil.t("STRIP.event.submissive.anal", character,arg);
					getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/7, this);
					break;
				default:
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, character);
					message+=TextUtil.t("STRIP.event.submissive.oral", character,arg);
					getCustomers().get(a).addToSatisfaction(character.getFinalValue(Sextype.FOREPLAY)/7, this);
				}



				break;


			}
			if(message!=null){getMessages().add(new MessageData(message, image, character.getBackground()));}

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
		modifications.add(new ModificationData(TargetType.ALL, -0.7f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.ALL, 0.5f, SpecializationAttribute.STRIP));
		modifications.add(new ModificationData(TargetType.ALL, 0.02f, BaseAttributeTypes.STRENGTH));
		modifications.add(new ModificationData(TargetType.ALL, 0.1f, BaseAttributeTypes.STAMINA));
		modifications.add(new ModificationData(TargetType.ALL, 0.05f, BaseAttributeTypes.CHARISMA));
		modifications.add(new ModificationData(TargetType.SLAVE, 0.02f, BaseAttributeTypes.OBEDIENCE));
		if(!(getCharacter().getTraits().contains(Trait.LEGACYSTRIPPER))){
			modifications.add(new ModificationData(TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
		}	
		return modifications;
	}

	@Override
	public int getAppeal() {
		return (getCharacter().getCharisma() + getCharacter().getFinalValue(SpecializationAttribute.STRIP));
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