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

public class Bartend extends RunningActivity implements BusinessSecondaryActivity {
	private final static int COSTDRINK = 6;
	private final static int PROFITDRINK = 4;
	private int bonus;

	private MessageData messageData;
	private List<Charakter> bartenders = new ArrayList<Charakter>();

	@Override
	public void init() {
		bartenders.addAll(getCharacters());
	}

	@Override
	public void perform() {

		int amountEarned = 0;
		Charakter bartender;
		for (Customer customer : getCustomers()) {
			bartender = bartenders.get(Util.getInt(0, bartenders.size()));
			customer.addToSatisfaction(5 + bartender.getFinalValue(BaseAttributeTypes.CHARISMA) / 8 + 
					bartender.getFinalValue(BaseAttributeTypes.INTELLIGENCE) / 10, this);
			int amountDrinks = Util.getInt(1, 4);
			if (customer.getType() == CustomerType.BUM) {
				amountDrinks++;
			}
			amountEarned += PROFITDRINK * amountDrinks;
			int tips = (customer.getMoney() / 200) + Util.getInt(1, 4);

			customer.payFixed(COSTDRINK * amountDrinks);
			tips = customer.pay(tips, bartender.getMoneyModifier());

			amountEarned += tips;
			customer.changePayModifier(0.1f);
		}
		modifyIncome(amountEarned);

		if (bartenders.size() < 2) {
			messageData.addToMessage("\n\n" + TextUtil.t("bartend.result", getCharacter(), getCustomers().size(), getIncome()));
		} else {
			messageData.addToMessage("\n\n" + TextUtil.t("bartend.result.group", getCustomers().size(), getIncome()));
		}
		for(Charakter bartender2 : getCharacters()){
			if((bartender2.getTraits().contains(Trait.DATASS)
					|| bartender2.getTraits().contains(Trait.UNDERTHETABLE)
					|| bartender2.getTraits().contains(Trait.FLIRTY)) && getCustomers().size()>5){
				int i=Util.getInt(0, 5);
				if(i<4){
					String message = null;
					ImageData image;
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, getCharacter());
					if(i==1 && bartender2.getTraits().contains(Trait.DATASS)){
						//DATASS
						i=Util.getInt(11, 15);
						if(i==11 && bartender2.getGender()==Gender.FEMALE){
							//flip
							int a=Util.getInt(0, getCustomers().size()-1);
							Object arg[] = {getCustomers().get(a).getName()};
							message=TextUtil.t("barevent.flip", bartender2,arg);
							if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>7 
									|| bartender2.getTraits().contains(Trait.OPENMINDED)
									|| bartender2.getTraits().contains(Trait.OBEDIENT)){
								this.getAttributeModifications().add(new AttributeModification(0.05f,BaseAttributeTypes.OBEDIENCE, bartender2));
								message+="\n" + TextUtil.t("barevent.flip.win", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
								getCustomers().get(Util.getInt(1, getCustomers().size()-1)).addToSatisfaction(75,this);

							}
							else{
								this.getAttributeModifications().add(new AttributeModification(-0.05f,BaseAttributeTypes.OBEDIENCE, bartender2));
								message+="\n" + TextUtil.t("barevent.flip.lose", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								int rnd=Util.getInt(1, getCustomers().size()-1);
								getCustomers().get(rnd).setStatus(CustomerStatus.PISSED);
								getCustomers().get(rnd).addToSatisfaction(-100, this);
								if(Util.getInt(1, 4)==1 && (getCustomers().get(rnd).getStatus()==CustomerStatus.DRUNK || getCustomers().get(rnd).getStatus()==CustomerStatus.VERYDRUNK)){getCustomers().get(rnd).setStatus(CustomerStatus.TIRED);}
								
							}
						}
						if(i==12){
							//grope
							int a=Util.getInt(0, getCustomers().size()-1);
							Object arg[] = {getCustomers().get(a).getName()};
							message=TextUtil.t("barevent.grope", bartender2,arg);
							if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>8 
									|| bartender2.getTraits().contains(Trait.WENCH)
									|| bartender2.getTraits().contains(Trait.FEELMEUP)){
								this.getAttributeModifications().add(new AttributeModification(0.07f,BaseAttributeTypes.OBEDIENCE, bartender2));
								message+="\n" + TextUtil.t("barevent.grope.win", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
								bartender2.getFame().modifyFame(15);
								for (Customer customer : getCustomers()) {
									if(Util.getInt(1, 6)==1 && customer.getStatus()==CustomerStatus.HORNYSTATUS){customer.setStatus(CustomerStatus.VERYHORNY);}
									if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SAD || customer.getStatus()==CustomerStatus.PISSED)){customer.setStatus(CustomerStatus.HAPPY);}
									if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.LIVELY || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.HORNYSTATUS);}
									
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(-0.07f,BaseAttributeTypes.OBEDIENCE, bartender2));
								message+="\n" + TextUtil.t("barevent.grope.lose", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								for (Customer customer : getCustomers()) {
									customer.addToSatisfaction(-10, this);
								}
							}
						}
						if(i==13){
							//look
							message=TextUtil.t("barevent.look", bartender2);
							if(bartender2.getFinalValue(SpecializationAttribute.BARTENDING)>Util.getInt(20, 70) 
									|| bartender2.getTraits().contains(Trait.OUTGOING)
									|| bartender2.getTraits().contains(Trait.CROWDLOVER)){
								this.getAttributeModifications().add(new AttributeModification(0.08f,BaseAttributeTypes.CHARISMA, bartender2));
								message+="\n" + TextUtil.t("barevent.look.win", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
								for (Customer customer : getCustomers()) {
									customer.addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA))/2, this);
									if(Util.getInt(1, 6)==1 && customer.getStatus()==CustomerStatus.HORNYSTATUS){customer.setStatus(CustomerStatus.VERYHORNY);}
									if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SAD || customer.getStatus()==CustomerStatus.PISSED)){customer.setStatus(CustomerStatus.HAPPY);}
									if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.LIVELY || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.HORNYSTATUS);}
									
								}
							}
							else{
								message+="\n" + TextUtil.t("barevent.look.lose", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								for (Customer customer : getCustomers()) {
									customer.addToSatisfaction(-15, this);
								}
							}
						}
						if(i>=14){
							//crowd
							message=TextUtil.t("barevent.crowd", bartender2);
							if(bartender2.getFinalValue(SpecializationAttribute.STRIP)>80 || bartender2.getTraits().contains(Trait.WILD)){
								this.getAttributeModifications().add(new AttributeModification(0.05f,BaseAttributeTypes.STAMINA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(2.05f,SpecializationAttribute.BARTENDING, bartender2));
								this.getAttributeModifications().add(new AttributeModification(1.55f,SpecializationAttribute.STRIP, bartender2));
								message+="\n" + TextUtil.t("barevent.crowd.win", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
								for (Customer customer : getCustomers()) {
									customer.addToSatisfaction(bartender2.getFinalValue(SpecializationAttribute.STRIP)/10, this);
									if(Util.getInt(1, 6)==1 && customer.getStatus()==CustomerStatus.HORNYSTATUS){customer.setStatus(CustomerStatus.VERYHORNY);}
									if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SAD || customer.getStatus()==CustomerStatus.PISSED)){customer.setStatus(CustomerStatus.HAPPY);}
									if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.LIVELY || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.HORNYSTATUS);}
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(0.05f,BaseAttributeTypes.OBEDIENCE, bartender2));
								this.getAttributeModifications().add(new AttributeModification(0.05f,Sextype.FOREPLAY, bartender2));
								int a=Util.getInt(0, getCustomers().size()-1);
								Object arg[] = {getCustomers().get(a).getName()};
								message+="\n" + TextUtil.t("barevent.crowd.lose", bartender2,arg);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
								for (Customer customer : getCustomers()) {
									customer.addToSatisfaction(Util.getInt(-100, 100), this);
									if(Util.getInt(1, 6)==1 && customer.getStatus()==CustomerStatus.SHYSTATUS){customer.setStatus(CustomerStatus.HORNYSTATUS);}
									if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SHYSTATUS || customer.getStatus()==CustomerStatus.LIVELY)){customer.setStatus(CustomerStatus.PISSED);}
									if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.HYPED || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.PISSED);}
									if(Util.getInt(1, 6)==1 && customer.getStatus()==CustomerStatus.HORNYSTATUS){customer.setStatus(CustomerStatus.VERYHORNY);}
									if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SAD || customer.getStatus()==CustomerStatus.PISSED)){customer.setStatus(CustomerStatus.HAPPY);}
									if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.LIVELY || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.HORNYSTATUS);}
								}
							}
						}
					}
					if(i==2 && bartender2.getTraits().contains(Trait.FLIRTY)){
						//flirty
						i=Util.getInt(11, 15);
						if(i==11){
							//sitlap
							message=TextUtil.t("barevent.sitlap", bartender2);
							if(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)>20 && Util.getInt(1, 3)==2){
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
								this.getAttributeModifications().add(new AttributeModification(2.05f,SpecializationAttribute.BARTENDING, bartender2));
								message+="\n" + TextUtil.t("barevent.sitlap.win", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								int rnd=Util.getInt(0, getCustomers().size());
								getCustomers().get(rnd).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(SpecializationAttribute.SEDUCTION)/5)/2, this);
								getCustomers().get(rnd).payFixed(200);
								if(Util.getInt(1, 6)==1 && getCustomers().get(rnd).getStatus()==CustomerStatus.HORNYSTATUS){getCustomers().get(rnd).setStatus(CustomerStatus.VERYHORNY);}
								if(Util.getInt(1, 4)==1 && (getCustomers().get(rnd).getStatus()==CustomerStatus.SAD || getCustomers().get(rnd).getStatus()==CustomerStatus.PISSED)){getCustomers().get(rnd).setStatus(CustomerStatus.HAPPY);}
								if(Util.getInt(1, 4)==1 && (getCustomers().get(rnd).getStatus()==CustomerStatus.LIVELY || getCustomers().get(rnd).getStatus()==CustomerStatus.DRUNK)){getCustomers().get(rnd).setStatus(CustomerStatus.HORNYSTATUS);}
								modifyIncome(200);
							}
							else{
								message+="\n" + TextUtil.t("barevent.sitlap.lose", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
							}
						}
						if(i==12){
							//chat
							int a=Util.getInt(0, getCustomers().size()-1);
							Object arg[] = {getCustomers().get(a).getName()};
							message=TextUtil.t("barevent.chat", bartender2,arg);
							this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
							this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
							this.getAttributeModifications().add(new AttributeModification(2.05f,SpecializationAttribute.BARTENDING, bartender2));
							if(Util.getInt(0, 20)>10){
								message+="\n" + TextUtil.t("barevent.chat.win", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								getCustomers().get(a).addToSatisfaction(150, this);
								if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.SAD || getCustomers().get(a).getStatus()==CustomerStatus.PISSED)){getCustomers().get(a).setStatus(CustomerStatus.HAPPY);}
								if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.HAPPY || getCustomers().get(a).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.LIVELY);}
							}
							else{
								message+="\n" + TextUtil.t("barevent.chat.lose", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
							}
						}
						if(i==13){
							//sitgroup
							
							message=TextUtil.t("barevent.sitgroup", bartender2);
							if(Util.getInt(1, 100)>50){
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
								this.getAttributeModifications().add(new AttributeModification(3.05f,SpecializationAttribute.BARTENDING, bartender2));
								Object arg[] = {getCustomers().size()*10-10};
								message+="\n" + TextUtil.t("barevent.sitgroup.win", bartender2,arg);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								for(int a=0; a<getCustomers().size()/10; a++){
									getCustomers().get(a).payFixed(100);
									getCustomers().get(a).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(BaseAttributeTypes.INTELLIGENCE))/2, this);
									modifyIncome(100);
									if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.SAD || getCustomers().get(a).getStatus()==CustomerStatus.PISSED)){getCustomers().get(a).setStatus(CustomerStatus.HAPPY);}
									if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.SHYSTATUS || getCustomers().get(a).getStatus()==CustomerStatus.HAPPY)){getCustomers().get(a).setStatus(CustomerStatus.DRUNK);}
									if(Util.getInt(1, 4)==1 && getCustomers().get(a).getStatus()==CustomerStatus.DRUNK){getCustomers().get(a).setStatus(CustomerStatus.VERYDRUNK);}
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
								this.getAttributeModifications().add(new AttributeModification(2.05f,Sextype.FOREPLAY, bartender2));
								Object arg[] = {getCustomers().size()*40-40};
								message+="\n" + TextUtil.t("barevent.sitgroup.lose", bartender2,arg);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								for(int a=0; a<getCustomers().size(); a++){
									if(a<getCustomers().size()/10){
										getCustomers().get(a).payFixed(400);
										getCustomers().get(a).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(BaseAttributeTypes.INTELLIGENCE))/2, this);
										modifyIncome(400);
										if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.SAD || getCustomers().get(a).getStatus()==CustomerStatus.PISSED)){getCustomers().get(a).setStatus(CustomerStatus.HAPPY);}
										if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.SHYSTATUS || getCustomers().get(a).getStatus()==CustomerStatus.HAPPY)){getCustomers().get(a).setStatus(CustomerStatus.DRUNK);}
										if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()!=CustomerStatus.STRONGSTATUS && getCustomers().get(a).getStatus()!=CustomerStatus.VERYHORNY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
									}
									else{
										getCustomers().get(a).addToSatisfaction(-50, this);
										if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.HYPED || getCustomers().get(a).getStatus()==CustomerStatus.LIVELY)){getCustomers().get(a).setStatus(CustomerStatus.PISSED);}
									}
								}
							}
						}
						if(i>=14){
							//teaselot
							message=TextUtil.t("barevent.teaselot", bartender2);
							if(Util.getInt(1, 100)>50){
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
								this.getAttributeModifications().add(new AttributeModification(2.05f,SpecializationAttribute.SEDUCTION, bartender2));
								message+="\n" + TextUtil.t("barevent.teaselot.win", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								for(int a=0; a<getCustomers().size(); a++){
									getCustomers().get(a).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(SpecializationAttribute.SEDUCTION)/8), this);
									
									if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(a).getStatus()==CustomerStatus.HYPED)){getCustomers().get(a).setStatus(CustomerStatus.VERYHORNY);}
									if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.SAD || getCustomers().get(a).getStatus()==CustomerStatus.PISSED)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
								this.getAttributeModifications().add(new AttributeModification(2.05f,SpecializationAttribute.BARTENDING, bartender2));
								message+="\n" + TextUtil.t("barevent.teaselot.lose", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								for(int a=0; a<getCustomers().size(); a++){
									getCustomers().get(a).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(SpecializationAttribute.BARTENDING)/8), this);
									if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.SAD || getCustomers().get(a).getStatus()==CustomerStatus.PISSED)){getCustomers().get(a).setStatus(CustomerStatus.HAPPY);}
								}
							}
						}

					}
					if(i==3 && bartender2.getTraits().contains(Trait.UNDERTHETABLE)){
						//UTT
						i=Util.getInt(11, 14);
						if(i==11){
							//ninjablowjob
							int a=Util.getInt(0, getCustomers().size()-1);
							Object arg[] = {getCustomers().get(a).getName(), bartender2.getFinalValue(Sextype.ORAL)*2};
							message=TextUtil.t("barevent.ninjablowjob", bartender2,arg);
							
							Customer cust = getCustomers().get(a);
							if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>10 || bartender2.getTraits().contains(Trait.WENCH)){
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(1.1f,Sextype.ORAL, bartender2));
								this.getAttributeModifications().add(new AttributeModification(1.05f,SpecializationAttribute.SEDUCTION, bartender2));
								message+="\n" + TextUtil.t("barevent.ninjablowjob.win", bartender2, cust,arg);
								message+="\n" + TextUtil.t("barevent.ninjablowjob.gold", bartender2,arg);
								if(cust.getGender()==Gender.MALE){
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, bartender2);
								}
								else{
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, bartender2);
								}
								cust.addToSatisfaction(bartender2.getFinalValue(Sextype.ORAL), this);
								cust.payFixed(bartender2.getFinalValue(Sextype.ORAL)*2);
								modifyIncome(bartender2.getFinalValue(Sextype.ORAL)*2);
								if(cust.getStatus()==CustomerStatus.HORNYSTATUS){cust.setStatus(CustomerStatus.LIVELY);}
								if(cust.getStatus()==CustomerStatus.SAD || cust.getStatus()==CustomerStatus.PISSED){cust.setStatus(CustomerStatus.HAPPY);}
							}
							else{
								message+="\n" + TextUtil.t("barevent.ninjablowjob.lose", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								cust.addToSatisfaction(-200, this);
								cust.payFixed(100);
								modifyIncome(100);
								cust.setStatus(CustomerStatus.PISSED);
							}
						}
						if(i==12){
							//outside fuck
							int a=Util.getInt(0, getCustomers().size()-1);	
							Customer cust = getCustomers().get(a);
							Sextype sex = null;
							if(Util.getInt(1, 100)>50){sex= Sextype.VAGINAL;}
							else{sex = Sextype.ANAL;}
							Object arg[] = {getCustomers().get(a).getName(),(bartender2.getFinalValue(sex))};
							message= TextUtil.t("barevent.ninjablowjob", bartender2,arg);
							if(bartender2.getTraits().contains(Trait.WENCH) && Util.getInt(1, 100)>50){
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(1.1f,sex, bartender2));
								this.getAttributeModifications().add(new AttributeModification(3.05f,SpecializationAttribute.SEDUCTION, bartender2));
								message+="\n" + TextUtil.t("barevent.fuck.wench", bartender2,arg);
								if(sex==Sextype.VAGINAL){image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);}
								else{image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2);}
								cust.addToSatisfaction(bartender2.getFinalValue(sex)*2, this);
								cust.payFixed(bartender2.getFinalValue(sex)*2);
								modifyIncome(bartender2.getFinalValue(sex)*2);
								for(int z=0; z<getCustomers().size(); z++){
									getCustomers().get(z).payFixed(100);
									getCustomers().get(z).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(sex)/8)/4, this);
									
									if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(a).getStatus()==CustomerStatus.HYPED)){getCustomers().get(a).setStatus(CustomerStatus.VERYHORNY);}
									if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.HAPPY || getCustomers().get(a).getStatus()==CustomerStatus.LIVELY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
								}
							}
							else if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>23 || bartender2.getTraits().contains(Trait.WENCH)){
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(1.1f,sex, bartender2));
								this.getAttributeModifications().add(new AttributeModification(1.05f,SpecializationAttribute.SEDUCTION, bartender2));
								message+="\n" + TextUtil.t("barevent.fuck.win", bartender2,arg);
								if(sex==Sextype.VAGINAL){image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);}
								else{image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2);}
								cust.addToSatisfaction(bartender2.getFinalValue(sex), this);
								cust.payFixed(bartender2.getFinalValue(sex));
								modifyIncome(bartender2.getFinalValue(sex));
								if(cust.getStatus()==CustomerStatus.HORNYSTATUS){cust.setStatus(CustomerStatus.LIVELY);}
								if(cust.getStatus()==CustomerStatus.SAD || cust.getStatus()==CustomerStatus.PISSED){cust.setStatus(CustomerStatus.HAPPY);}
							}
							else{
								message+="\n" + TextUtil.t("barevent.ninjablowjob.lose", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								cust.addToSatisfaction(-200, this);
								cust.payFixed(100);
								modifyIncome(100);
								cust.setStatus(CustomerStatus.PISSED);
							}
						}
						if(i>=13){
							//group
							int rand=Util.getInt(200, 300);
							Object arg[] = {1+getCustomers().size()/10, getCustomers().get(Util.getInt(0, getCustomers().size()-1)).getName(), getCustomers().size()*rand/10};
							message= TextUtil.t("barevent.group", bartender2, arg);
							if(bartender2.getTraits().contains(Trait.GANGBANGQUEEN) && Util.getInt(1, 100)>50){
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.OBEDIENCE, bartender2));
								this.getAttributeModifications().add(new AttributeModification(2.1f,Sextype.GROUP, bartender2));
								this.getAttributeModifications().add(new AttributeModification(5.05f,SpecializationAttribute.SEDUCTION, bartender2));
								message+="\n" + TextUtil.t("barevent.group.queen", bartender2,arg);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
								for(int a=0; a<getCustomers().size()/10; a++){
									getCustomers().get(a).payFixed(rand);
									getCustomers().get(a).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/2, this);
									if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()!=CustomerStatus.STRONGSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								}
								for(int z=getCustomers().size()/10; z<getCustomers().size(); z++){
									getCustomers().get(z).addToSatisfaction(15+(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/4, this);
									if(Util.getInt(1, 4)==1 && (getCustomers().get(z).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(z).getStatus()==CustomerStatus.HYPED)){getCustomers().get(z).setStatus(CustomerStatus.VERYHORNY);}
									if(Util.getInt(1, 4)==1 && (getCustomers().get(z).getStatus()==CustomerStatus.HAPPY || getCustomers().get(z).getStatus()==CustomerStatus.LIVELY)){getCustomers().get(z).setStatus(CustomerStatus.HORNYSTATUS);}
								}
							}
							else if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>20 || bartender2.getTraits().contains(Trait.NYMPHO)){
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
								this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.OBEDIENCE, bartender2));
								this.getAttributeModifications().add(new AttributeModification(2.1f,Sextype.GROUP, bartender2));
								this.getAttributeModifications().add(new AttributeModification(4.05f,SpecializationAttribute.SEDUCTION, bartender2));
								message+="\n" + TextUtil.t("barevent.group.win", bartender2,arg);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
								for(int a=0; a<getCustomers().size()/10; a++){
									getCustomers().get(a).payFixed(rand);
									getCustomers().get(a).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/2, this);
									if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()!=CustomerStatus.STRONGSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.TIRED);}
								}
							}
							else{
								message+="\n" + TextUtil.t("barevent.group.lose", bartender2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
								for(int a=0; a<getCustomers().size()/15; a++){
									getCustomers().get(a).addToSatisfaction(-10, this);
									if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()!=CustomerStatus.STRONGSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.PISSED);}
								}
							}
						}


					}


					if(message!=null){getMessages().add(new MessageData(message, image, bartender2.getBackground()));}
					i=100;
				}
			}
		}
	}

	@Override
	public MessageData getBaseMessage() {
		Object arguments[] = { TextUtil.listCharacters(bartenders) };
		String messageText = TextUtil.t("bartend.basic", arguments);
		this.messageData = new MessageData(messageText, ImageUtil.getInstance()
				.getImageDataByTag(ImageTag.BARTEND, getCharacter()), getBackground());
		if (bartenders.size() > 1) {
			messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartenders.get(1)));
		}
		return this.messageData;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 1.1f, SpecializationAttribute.BARTENDING));
		modifications.add(new ModificationData(TargetType.TRAINER, -0.15f, BaseAttributeTypes.COMMAND));
		return modifications;
	}

	@Override
	public int getAppeal() {
		return Util.getInt(2, 8);
	}

	@Override
	public int getMaxAttendees() {
		int amount = 0;
		for (Charakter bartender : getCharacters()) {
			amount += 5 + bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 5;
		}
		amount+=bonus;
		if(amount>=60*getCharacters().size()){
			amount=60*getCharacters().size();
		}
		return amount;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
}
