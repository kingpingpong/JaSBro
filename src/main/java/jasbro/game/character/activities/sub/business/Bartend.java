package jasbro.game.character.activities.sub.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
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

public class Bartend extends RunningActivity implements BusinessSecondaryActivity {
	private int bonus;
	public enum BarAction {
		FLIP, GROPE, LOOK, CROWD, SLAP, DODGE,
		SITLAP, CHAT, SITGROUP, TEASELOT,
		BLOWJOB, FUCK, GROUP, DUMBBJ, DUMBFUCK,
		SMALLTALK, LOUDBUNCH, SEXSMELL,
		BREAKSTUFF, BIGBOOB, SMALLBOOB, LOLI, TIRED, SHY, SNACKS, 
		LAGOMORPHQUICKIE, LAGOMORPHORGY
		;
	}
	private Map<Charakter, BarAction> characterAction=new HashMap<Charakter, BarAction>();
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

		int pay=0;
		float portionPay=0;
		for (Customer customer : getCustomers()) {
			bartender = bartenders.get(Util.getInt(0, bartenders.size()));
			int skill=5+bartender.getFinalValue(BaseAttributeTypes.CHARISMA)/10+ bartender.getFinalValue(SpecializationAttribute.BARTENDING)/10+ bartender.getFinalValue(BaseAttributeTypes.INTELLIGENCE)/10;


			switch(customer.getType()){
			case PEASANT:
				portionPay=0.03f;
				break;
			case SOLDIER:
				portionPay=0.020f;
				break;
			case MERCHANT:
				portionPay=0.015f;
				break;
			case BUSINESSMAN:
				portionPay=0.01f;
				break;
			case MINORNOBLE:
				portionPay=0.005f;
				break;
			case LORD:
				portionPay=0.001f;
				break;
			case CELEBRITY:
				portionPay=0.0005f;
				break;
			default:
				portionPay=0.09f;
			}
			customer.addToSatisfaction((int) (skill*portionPay*10), this);
			pay=(int) (customer.getMoney()*portionPay*Util.getInt(50, 125)/100);
			pay+=5+customer.getMoney()*bartender.getFinalValue(SpecializationAttribute.BARTENDING)/1200;
			customer.payFixed(pay);

			amountEarned += pay;
			customer.changePayModifier(0.2f);
		}
		modifyIncome(amountEarned);

		if (bartenders.size() < 2) {
			messageData.addToMessage("\n\n" + TextUtil.t("bartend.result", getCharacter(), getCustomers().size(), getIncome()));
		} else {
			messageData.addToMessage("\n\n" + TextUtil.t("bartend.result.group", getCustomers().size(), getIncome()));
		}
		for(Charakter bartender2 : getCharacters()){
			List<BarAction> actions = new ArrayList<BarAction>();

			if(bartender2.getTraits().contains(Trait.DATASS)){
				actions.add(BarAction.GROPE);
				actions.add(BarAction.FLIP);
				actions.add(BarAction.LOOK);
				actions.add(BarAction.CROWD);
				actions.add(BarAction.SLAP);				
			}
			if(bartender2.getTraits().contains(Trait.FLIRTY)){
				actions.add(BarAction.SITLAP);
				actions.add(BarAction.SITGROUP);
				actions.add(BarAction.CHAT);
				actions.add(BarAction.TEASELOT);
			}
			if(bartender2.getTraits().contains(Trait.UNDERTHETABLE)){
				actions.add(BarAction.FUCK);
				actions.add(BarAction.BLOWJOB);
				actions.add(BarAction.GROUP);				
			}
			if(bartender2.getTraits().contains(Trait.UNDERTHETABLE) && bartender2.getTraits().contains(Trait.WENCH)){
				actions.add(BarAction.FUCK);
				actions.add(BarAction.BLOWJOB);
				actions.add(BarAction.GROUP);				
			}
			if(bartender2.getTraits().contains(Trait.OUTGOING)){				
				actions.add(BarAction.LOUDBUNCH);	
				actions.add(BarAction.SMALLTALK);			
			}
			if(bartender2.getTraits().contains(Trait.STUPID)){				
				actions.add(BarAction.DUMBBJ);	
				actions.add(BarAction.DUMBFUCK);			
			}
			if(bartender2.getIntelligence()<5){				
				actions.add(BarAction.DUMBBJ);	
				actions.add(BarAction.DUMBFUCK);				
			}
			if(bartender2.getTraits().contains(Trait.OUTGOING) && bartender2.getFinalValue(SpecializationAttribute.BARTENDING)>300){				
				actions.add(BarAction.LOUDBUNCH);	
				actions.add(BarAction.SMALLTALK);			
			}
			if(bartender2.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString())>5)
				actions.add(BarAction.SEXSMELL);
			if(bartender2.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString())>10)
				actions.add(BarAction.SEXSMELL);
			if(bartender2.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString())>15)
				actions.add(BarAction.SEXSMELL);
			if(bartender2.getTraits().contains(Trait.BIGBOOBS))
				actions.add(BarAction.BIGBOOB);
			if(bartender2.getTraits().contains(Trait.SMALLBOOBS))
				actions.add(BarAction.SMALLBOOB);
			if(bartender2.getTraits().contains(Trait.LOLI))
				actions.add(BarAction.LOLI);
			if(bartender2.getTraits().contains(Trait.CLUMSY))
				actions.add(BarAction.BREAKSTUFF);
			if(bartender2.getTraits().contains(Trait.FRAGILE))
				actions.add(BarAction.TIRED);
			if(bartender2.getTraits().contains(Trait.SHY))
				actions.add(BarAction.SHY);
			if(bartender2.getTraits().contains(Trait.LAGOMORPHQUICKY))
				actions.add(BarAction.LAGOMORPHQUICKIE);
			if(bartender2.getTraits().contains(Trait.LAGOMORPHORGY) && Util.getInt(0, 100)<70)
				actions.add(BarAction.LAGOMORPHORGY);
			if(bartender2.getTraits().contains(Trait.RESTAURATEUR) || bartender2.getFinalValue(SpecializationAttribute.COOKING)>150)
				actions.add(BarAction.SNACKS);

			if(Util.getInt(0, 100)<10+actions.size()*6 && getCustomers().size()>10 && actions.size()>0)
			{
				characterAction.put(bartender2, actions.get(Util.getInt(0, actions.size())));
				String message = null;
				ImageData image;
				image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, getCharacter());
				int a=Util.getInt(0, getCustomers().size()-1);
				Object arg[] = {getCustomers().get(a).getName()};
				int rnd=0;


				switch(characterAction.get(bartender2)){
				case FLIP:				
					message=TextUtil.t("barevent.flip", bartender2,arg);
					if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>7 
							|| bartender2.getTraits().contains(Trait.OPENMINDED)
							|| bartender2.getTraits().contains(Trait.OBEDIENT)){
						if(Util.getInt(0, 100)>50){
							this.getAttributeModifications().add(new AttributeModification(0.05f,BaseAttributeTypes.OBEDIENCE, bartender2));
							this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));
							message+="\n" + TextUtil.t("barevent.flip.win", bartender2);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
							getCustomers().get(Util.getInt(1, getCustomers().size()-1)).addToSatisfaction(5,this);
						}
						else{
							this.getAttributeModifications().add(new AttributeModification(0.05f,BaseAttributeTypes.OBEDIENCE, bartender2));
							message+="\n" + TextUtil.t("barevent.flip.okay", bartender2);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
							for(Customer cust : getCustomers()){
								if((cust.getStatus()==CustomerStatus.LIVELY || cust.getStatus()==CustomerStatus.HAPPY || cust.getStatus()==CustomerStatus.DRUNK) && Util.getInt(0, 100)>50)
									cust.addToSatisfaction(5, this);
							}
						}

					}
					else{
						this.getAttributeModifications().add(new AttributeModification(-0.05f,BaseAttributeTypes.OBEDIENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(-0.5f,EssentialAttributes.MOTIVATION, bartender2));
						message+="\n" + TextUtil.t("barevent.flip.lose", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						rnd=Util.getInt(1, getCustomers().size()-1);
						getCustomers().get(rnd).setStatus(CustomerStatus.PISSED);
						getCustomers().get(rnd).addToSatisfaction(-20, this);
						if(Util.getInt(1, 4)==1 && (getCustomers().get(rnd).getStatus()==CustomerStatus.DRUNK || getCustomers().get(rnd).getStatus()==CustomerStatus.VERYDRUNK)){getCustomers().get(rnd).setStatus(CustomerStatus.TIRED);}

					}
					break;
				case GROPE:
					message=TextUtil.t("barevent.grope", bartender2,arg);
					if((bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>7 
							|| bartender2.getTraits().contains(Trait.WENCH)
							|| bartender2.getTraits().contains(Trait.TOUCHYFEELY) && Util.getInt(0, 100)>50)){
						this.getAttributeModifications().add(new AttributeModification(0.07f,BaseAttributeTypes.OBEDIENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));

						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
						bartender2.getFame().modifyFame(15);
						int income=bartender2.getCharisma()/2;
						for (Customer customer : getCustomers()) {
							if((Util.getInt(1, 10)>4 && customer.getStatus()==CustomerStatus.HORNYSTATUS) || customer.getStatus()==CustomerStatus.VERYHORNY){
								customer.payFixed(bartender2.getCharisma()/2);
								modifyIncome(bartender2.getCharisma()/2);
								customer.addToSatisfaction(5, this);
								income+=bartender2.getCharisma()/2;
							}
						}
						Object gropeincome[]={income};
						message+="\n" + TextUtil.t("barevent.grope.winwin", bartender2, gropeincome);
					}
					else if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>5 
							|| bartender2.getTraits().contains(Trait.WENCH)
							|| bartender2.getTraits().contains(Trait.TOUCHYFEELY)){
						this.getAttributeModifications().add(new AttributeModification(0.07f,BaseAttributeTypes.OBEDIENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));
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
						this.getAttributeModifications().add(new AttributeModification(-0.5f,EssentialAttributes.MOTIVATION, bartender2));
						message+="\n" + TextUtil.t("barevent.grope.lose", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(-10, this);
						}
					}
					break;
				case LOOK:
					message=TextUtil.t("barevent.look", bartender2);
					if(bartender2.getFinalValue(SpecializationAttribute.BARTENDING)>Util.getInt(10, 25) 
							|| bartender2.getTraits().contains(Trait.OUTGOING)
							|| bartender2.getTraits().contains(Trait.CROWDLOVER)){
						this.getAttributeModifications().add(new AttributeModification(0.08f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));
						message+="\n" + TextUtil.t("barevent.look.win", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA))/5, this);
							if(Util.getInt(1, 6)==1 && customer.getStatus()==CustomerStatus.HORNYSTATUS){customer.setStatus(CustomerStatus.VERYHORNY);}
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SAD || customer.getStatus()==CustomerStatus.PISSED)){customer.setStatus(CustomerStatus.HAPPY);}
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.LIVELY || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.HORNYSTATUS);}

						}
					}
					else{
						message+="\n" + TextUtil.t("barevent.look.lose", bartender2);
						this.getAttributeModifications().add(new AttributeModification(-0.3f,EssentialAttributes.MOTIVATION, bartender2));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(-10, this);
						}
					}
					break;
				case CROWD:
					message=TextUtil.t("barevent.crowd", bartender2);
					if(bartender2.getFinalValue(SpecializationAttribute.STRIP)>30 || bartender2.getTraits().contains(Trait.WILD)){
						this.getAttributeModifications().add(new AttributeModification(0.05f,BaseAttributeTypes.STAMINA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(1.05f,SpecializationAttribute.BARTENDING, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.55f,SpecializationAttribute.STRIP, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.7f,EssentialAttributes.MOTIVATION, bartender2));
						message+="\n" + TextUtil.t("barevent.crowd.win", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(bartender2.getFinalValue(SpecializationAttribute.STRIP)/9, this);
							if(Util.getInt(1, 6)==1 && customer.getStatus()==CustomerStatus.HORNYSTATUS){customer.setStatus(CustomerStatus.VERYHORNY);}
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SAD || customer.getStatus()==CustomerStatus.PISSED)){customer.setStatus(CustomerStatus.HAPPY);}
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.LIVELY || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.HORNYSTATUS);}
						}
					}
					else{
						this.getAttributeModifications().add(new AttributeModification(0.05f,BaseAttributeTypes.OBEDIENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.05f,Sextype.FOREPLAY, bartender2));
						this.getAttributeModifications().add(new AttributeModification(-0.5f,EssentialAttributes.MOTIVATION, bartender2));
						message+="\n" + TextUtil.t("barevent.crowd.lose", bartender2,arg);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(Util.getInt(-10, 15), this);
							if(Util.getInt(1, 6)==1 && customer.getStatus()==CustomerStatus.SHYSTATUS){customer.setStatus(CustomerStatus.HORNYSTATUS);}
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SHYSTATUS || customer.getStatus()==CustomerStatus.LIVELY)){customer.setStatus(CustomerStatus.PISSED);}
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.HYPED || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.PISSED);}
							if(Util.getInt(1, 6)==1 && customer.getStatus()==CustomerStatus.HORNYSTATUS){customer.setStatus(CustomerStatus.VERYHORNY);}
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SAD || customer.getStatus()==CustomerStatus.PISSED)){customer.setStatus(CustomerStatus.HAPPY);}
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.LIVELY || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.HORNYSTATUS);}
						}
					}
					break;
				case SITLAP:
					message=TextUtil.t("barevent.sitlap", bartender2);
					if(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)>15 && Util.getInt(1, 4)==2){
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.65f,SpecializationAttribute.BARTENDING, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));
						message+="\n" + TextUtil.t("barevent.sitlap.win", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						rnd=Util.getInt(0, getCustomers().size());
						getCustomers().get(rnd).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)/2+bartender2.getFinalValue(SpecializationAttribute.SEDUCTION)/7)/2, this);
						getCustomers().get(rnd).payFixed(200);
						if(Util.getInt(1, 6)==1 && getCustomers().get(rnd).getStatus()==CustomerStatus.HORNYSTATUS){getCustomers().get(rnd).setStatus(CustomerStatus.VERYHORNY);}
						if(Util.getInt(1, 4)==1 && (getCustomers().get(rnd).getStatus()==CustomerStatus.SAD || getCustomers().get(rnd).getStatus()==CustomerStatus.PISSED)){getCustomers().get(rnd).setStatus(CustomerStatus.HAPPY);}
						if(Util.getInt(1, 4)==1 && (getCustomers().get(rnd).getStatus()==CustomerStatus.LIVELY || getCustomers().get(rnd).getStatus()==CustomerStatus.DRUNK)){getCustomers().get(rnd).setStatus(CustomerStatus.HORNYSTATUS);}
						modifyIncome(200);
					}
					else{
						message+="\n" + TextUtil.t("barevent.sitlap.lose", bartender2);
						this.getAttributeModifications().add(new AttributeModification(-0.2f,EssentialAttributes.MOTIVATION, bartender2));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
					}
					break;
				case CHAT:
					message=TextUtil.t("barevent.chat", bartender2,arg);
					this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
					this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
					this.getAttributeModifications().add(new AttributeModification(0.85f,SpecializationAttribute.BARTENDING, bartender2));
					if(Util.getInt(0, 20)>10){
						message+="\n" + TextUtil.t("barevent.chat.win", bartender2);
						this.getAttributeModifications().add(new AttributeModification(0.2f,EssentialAttributes.MOTIVATION, bartender2));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						getCustomers().get(a).addToSatisfaction(5, this);
						if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.SAD || getCustomers().get(a).getStatus()==CustomerStatus.PISSED)){getCustomers().get(a).setStatus(CustomerStatus.HAPPY);}
						if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.HAPPY || getCustomers().get(a).getStatus()==CustomerStatus.SHYSTATUS)){getCustomers().get(a).setStatus(CustomerStatus.LIVELY);}
					}
					else{
						message+="\n" + TextUtil.t("barevent.chat.lose", bartender2);
						this.getAttributeModifications().add(new AttributeModification(-0.1f,EssentialAttributes.MOTIVATION, bartender2));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
					}
					break;
				case SITGROUP:
					message=TextUtil.t("barevent.sitgroup", bartender2);
					if(Util.getInt(1, 100)>50){
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.85f,SpecializationAttribute.BARTENDING, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));
						Object arg2[] = {getCustomers().size()*10-10};
						message+="\n" + TextUtil.t("barevent.sitgroup.win", bartender2,arg2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						for(int b=0; b<getCustomers().size()/10; b++){
							getCustomers().get(b).payFixed(100);
							getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(BaseAttributeTypes.INTELLIGENCE))/3, this);
							modifyIncome(100);
							if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()==CustomerStatus.SAD || getCustomers().get(b).getStatus()==CustomerStatus.PISSED)){getCustomers().get(b).setStatus(CustomerStatus.HAPPY);}
							if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()==CustomerStatus.SHYSTATUS || getCustomers().get(b).getStatus()==CustomerStatus.HAPPY)){getCustomers().get(b).setStatus(CustomerStatus.DRUNK);}
							if(Util.getInt(1, 4)==1 && getCustomers().get(b).getStatus()==CustomerStatus.DRUNK){getCustomers().get(b).setStatus(CustomerStatus.VERYDRUNK);}
						}
					}
					else{
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.75f,Sextype.FOREPLAY, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));
						Object arg2[] = {getCustomers().size()*40-40};
						message+="\n" + TextUtil.t("barevent.sitgroup.lose", bartender2,arg2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						for(int b=0; b<getCustomers().size(); b++){
							if(a<getCustomers().size()/10){
								getCustomers().get(b).payFixed(400);
								getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(BaseAttributeTypes.INTELLIGENCE))/3, this);
								modifyIncome(400);
								if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()==CustomerStatus.SAD || getCustomers().get(b).getStatus()==CustomerStatus.PISSED)){getCustomers().get(b).setStatus(CustomerStatus.HAPPY);}
								if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()==CustomerStatus.SHYSTATUS || getCustomers().get(b).getStatus()==CustomerStatus.HAPPY)){getCustomers().get(b).setStatus(CustomerStatus.DRUNK);}
								if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()!=CustomerStatus.STRONGSTATUS && getCustomers().get(b).getStatus()!=CustomerStatus.VERYHORNY)){getCustomers().get(b).setStatus(CustomerStatus.HORNYSTATUS);}
							}
							else{
								getCustomers().get(a).addToSatisfaction(-10, this);
								if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.HYPED || getCustomers().get(a).getStatus()==CustomerStatus.LIVELY)){getCustomers().get(a).setStatus(CustomerStatus.PISSED);}
							}
						}
					}
					break;
				case TEASELOT:
					message=TextUtil.t("barevent.teaselot", bartender2);
					this.getAttributeModifications().add(new AttributeModification(0.3f,EssentialAttributes.MOTIVATION, bartender2));
					if(Util.getInt(1, 100)>50){
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.55f,SpecializationAttribute.SEDUCTION, bartender2));
						message+="\n" + TextUtil.t("barevent.teaselot.win", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						for(int b=0; b<getCustomers().size(); b++){
							getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)/10+bartender2.getFinalValue(SpecializationAttribute.SEDUCTION)/10), this);

							if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(b).getStatus()==CustomerStatus.HYPED)){getCustomers().get(b).setStatus(CustomerStatus.VERYHORNY);}
							if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()==CustomerStatus.SAD || getCustomers().get(b).getStatus()==CustomerStatus.PISSED)){getCustomers().get(b).setStatus(CustomerStatus.HORNYSTATUS);}
						}
					}
					else{
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.INTELLIGENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.75f,SpecializationAttribute.BARTENDING, bartender2));
						message+="\n" + TextUtil.t("barevent.teaselot.lose", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						for(int b=0; b<getCustomers().size(); b++){
							getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)/10+bartender2.getFinalValue(SpecializationAttribute.BARTENDING)/10), this);
							if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()==CustomerStatus.SAD || getCustomers().get(b).getStatus()==CustomerStatus.PISSED)){getCustomers().get(a).setStatus(CustomerStatus.HAPPY);}
						}
					}
					break;
				case BLOWJOB:
					message=TextUtil.t("barevent.ninjablowjob", bartender2,arg);
					Customer cust = getCustomers().get(a);
					int blowjobPay=25+bartender2.getFinalValue(Sextype.ORAL)*bartender2.getFinalValue(Sextype.ORAL)/10;
					Object arg3[] = {getCustomers().get(a).getName(), blowjobPay};
					if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>6 || bartender2.getTraits().contains(Trait.WENCH)){
						bartender2.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1);
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.6f,Sextype.ORAL, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.65f,SpecializationAttribute.SEDUCTION, bartender2));
						message+="\n" + TextUtil.t("barevent.ninjablowjob.win", bartender2, cust,arg3);
						message+="\n" + TextUtil.t("barevent.ninjablowjob.gold", bartender2, arg3);
						if(cust.getGender()==Gender.MALE){
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, bartender2);
						}
						else{
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, bartender2);
						}
						cust.addToSatisfaction(bartender2.getFinalValue(Sextype.ORAL)/6, this);
						cust.payFixed(blowjobPay);
						modifyIncome(blowjobPay);
						if(bartender2.getTraits().contains(Trait.SLURPYSLURP) && bartender2.getFinalValue(Sextype.ORAL)>25 && Util.getInt(0, 100)>50){
							int i=Util.getInt(1, 5);
							int total=0;
							for(int z=0; z<i+bartender2.getFinalValue(Sextype.ORAL)/20; z++){
								getCustomers().get(z).payFixed(blowjobPay*(100-i)/100);
								modifyIncome(blowjobPay*(100-i)/100);
								total+=blowjobPay*(100-i)/100;
								getCustomers().get(z).addToSatisfaction(bartender2.getFinalValue(Sextype.ORAL)/6, this);
								if(cust.getStatus()==CustomerStatus.HORNYSTATUS){cust.setStatus(CustomerStatus.LIVELY);}
								if(cust.getStatus()==CustomerStatus.SAD || cust.getStatus()==CustomerStatus.PISSED){cust.setStatus(CustomerStatus.HAPPY);}
							}
							Object multiBJincome[]={total};
							message+="\n" + TextUtil.t("barevent.ninjablowjob.winmore", bartender2, cust,multiBJincome);
							if(Util.getInt(0, 100)>50)
								message+="\n" + TextUtil.t("barevent.ninjablowjob.drink", bartender2);
							else
								message+="\n" + TextUtil.t("barevent.ninjablowjob.bukkake", bartender2);
						}

						if(cust.getStatus()==CustomerStatus.HORNYSTATUS){cust.setStatus(CustomerStatus.LIVELY);}
						if(cust.getStatus()==CustomerStatus.SAD || cust.getStatus()==CustomerStatus.PISSED){cust.setStatus(CustomerStatus.HAPPY);}
					}
					else{
						message+="\n" + TextUtil.t("barevent.ninjablowjob.lose", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						cust.addToSatisfaction(-100, this);
						cust.payFixed(100);
						modifyIncome(100);
						cust.setStatus(CustomerStatus.PISSED);
					}
					break;
				case LAGOMORPHQUICKIE:
					int quickieAmount=0;
					Sextype sex2 = null;
					int tips=0;
					int tip=0;
					if(Util.getInt(1, 100)>50) 
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2);
					else
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
					for(Customer cust2 : getCustomers()){ 
						if(((cust2.getType()==CustomerType.SOLDIER || cust2.getStatus()==CustomerStatus.STRONGSTATUS) && (Util.getInt(0, 100)<30 || quickieAmount<4) && quickieAmount<3+((bartender2.getFinalValue(Sextype.ANAL)+bartender2.getFinalValue(Sextype.VAGINAL))/5))){
							if(Util.getInt(1, 100)>50) 
								sex2= Sextype.VAGINAL;
							else 
								sex2 = Sextype.ANAL;
							this.getAttributeModifications().add(new AttributeModification(0.2f,sex2, bartender2));
							cust2.addToSatisfaction(5, this);
							quickieAmount++;
							if(Util.getInt(0, 100)<45){
								tip=Util.getInt(5, 10)+bartender2.getFinalValue(sex2)/3;
								tip=cust2.payFixed(tip);
								tips+=tip;
							}
						}
					}
					Object quickieArgs[]={quickieAmount, tips};
					message= TextUtil.t("barevent.lagomorphquickie", bartender2,quickieArgs);
					break;
				case DUMBFUCK:
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
					this.getAttributeModifications().add(new AttributeModification(1.1f,Sextype.VAGINAL, bartender2));
					getCustomers().get(Util.getInt(0, getCustomers().size()-1)).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/2, this);
					message= TextUtil.t("barevent.dumbfuck", bartender2);
					break;
				case DUMBBJ:
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
					this.getAttributeModifications().add(new AttributeModification(1.1f,Sextype.ORAL, bartender2));
					getCustomers().get(Util.getInt(0, getCustomers().size()-1)).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/2, this);
					message= TextUtil.t("barevent.bj", bartender2);
					break;
				case LAGOMORPHORGY:
					int rand2=Util.getInt(10, 20);
					int fraction=Util.getInt(7, 10)-bartender2.getFinalValue(SpecializationAttribute.TRANSFORMATION)/10;
					Object arg5[] = {1+getCustomers().size()/fraction, getCustomers().size()*rand2/fraction};
					message= TextUtil.t("barevent.lagomorphorgy", bartender2, arg5);
					bartender2.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1+getCustomers().size()/10);
					this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
					this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.OBEDIENCE, bartender2));
					this.getAttributeModifications().add(new AttributeModification(1.1f,Sextype.GROUP, bartender2));
					this.getAttributeModifications().add(new AttributeModification(0.85f,SpecializationAttribute.SEDUCTION, bartender2));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
					for(int b=0; b<getCustomers().size()/fraction; b++){
						getCustomers().get(b).payFixed(rand2);
						getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/2, this);
						if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()!=CustomerStatus.STRONGSTATUS)){getCustomers().get(b).setStatus(CustomerStatus.TIRED);}
					}
					break;
				case FUCK:
					Customer cust2 = getCustomers().get(a);
					Sextype sex = null;
					if(Util.getInt(1, 100)>50) 
						sex= Sextype.VAGINAL;
					else 
						sex = Sextype.ANAL;
					int fuckPay=bartender2.getFinalValue(sex)*bartender2.getFinalValue(sex)/8;
					fuckPay+=100;
					fuckPay=Math.min(fuckPay, cust2.getMoney());
					Object arg4[] = {getCustomers().get(a).getName(),fuckPay};
					message= TextUtil.t("barevent.ninjablowjob", bartender2,arg4);
					if(bartender2.getTraits().contains(Trait.WENCH) && Util.getInt(1, 100)>50){
						bartender2.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1);
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(1.1f,sex, bartender2));
						this.getAttributeModifications().add(new AttributeModification(1.05f,SpecializationAttribute.SEDUCTION, bartender2));
						this.getAttributeModifications().add(new AttributeModification(1.8f,EssentialAttributes.MOTIVATION, bartender2));
						message+="\n" + TextUtil.t("barevent.fuck.wench", bartender2,arg4);
						if(sex==Sextype.VAGINAL){image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);}
						else{image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2);}
						cust2.addToSatisfaction(bartender2.getFinalValue(sex)/4, this);
						cust2.payFixed(fuckPay);
						modifyIncome(fuckPay);
						if(Util.getInt(0, 100)<20+bartender2.getFinalValue(SpecializationAttribute.SEDUCTION)/2){
							int linefuck=0;
							int total=0;
							for(int z=0; z<getCustomers().size(); z++){
								if((linefuck<=(bartender2.getFinalValue(Sextype.VAGINAL)+bartender2.getFinalValue(Sextype.ANAL))/5 && (getCustomers().get(z).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(z).getStatus()==CustomerStatus.VERYHORNY)) || linefuck<3){
									linefuck++;
									getCustomers().get(z).addToSatisfaction(bartender2.getFinalValue(sex)-bartender2.getFinalValue(sex)*linefuck/50, this);
									getCustomers().get(z).payFixed(fuckPay-fuckPay*linefuck/50);
									total+=fuckPay-fuckPay*linefuck/50;

									bartender2.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1);
									
								}				
							}
							if(linefuck>4){
								this.getAttributeModifications().add(new AttributeModification(linefuck*0.4f,sex, bartender2));
								this.getAttributeModifications().add(new AttributeModification(linefuck*(-1.8f),EssentialAttributes.ENERGY, bartender2));
								Object LinefuckTotal[]={linefuck};
								message+="\n" + TextUtil.t("barevent.fuck.line", bartender2,LinefuckTotal);
								Object LinefuckPay[]={total};
								message+="\n" + TextUtil.t("barevent.fuck.line.pay", bartender2,LinefuckPay);
								modifyIncome(total);
							}
						}
						else{
							for(int z=0; z<getCustomers().size(); z++){
								getCustomers().get(z).payFixed(100);
								getCustomers().get(z).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(sex)/8)/4, this);

								if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(a).getStatus()==CustomerStatus.HYPED)){getCustomers().get(a).setStatus(CustomerStatus.VERYHORNY);}
								if(Util.getInt(1, 4)==1 && (getCustomers().get(a).getStatus()==CustomerStatus.HAPPY || getCustomers().get(a).getStatus()==CustomerStatus.LIVELY)){getCustomers().get(a).setStatus(CustomerStatus.HORNYSTATUS);}
							}
						}
					}
					else if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>9 || bartender2.getTraits().contains(Trait.WENCH)){
						bartender2.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1);
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.5f,sex, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.5f,SpecializationAttribute.SEDUCTION, bartender2));
						message+="\n" + TextUtil.t("barevent.fuck.win", bartender2,arg4);
						if(sex==Sextype.VAGINAL)
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
						else
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, bartender2);
						cust2.addToSatisfaction(bartender2.getFinalValue(sex)/5, this);
						cust2.payFixed(fuckPay);
						modifyIncome(fuckPay);
						if(cust2.getStatus()==CustomerStatus.HORNYSTATUS){cust2.setStatus(CustomerStatus.LIVELY);}
						if(cust2.getStatus()==CustomerStatus.SAD || cust2.getStatus()==CustomerStatus.PISSED){cust2.setStatus(CustomerStatus.HAPPY);}
					}
					else{
						message+="\n" + TextUtil.t("barevent.ninjablowjob.lose", bartender2);
						this.getAttributeModifications().add(new AttributeModification(-0.5f,EssentialAttributes.MOTIVATION, bartender2));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						cust2.addToSatisfaction(-50, this);
						cust2.payFixed(Math.min(100, cust2.getMoney()));
						modifyIncome(Math.min(100, cust2.getMoney()));
						cust2.setStatus(CustomerStatus.PISSED);
					}
					break;
				case GROUP:
					long servedToday2=bartender2.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString());
					int rand=Util.getInt(100, 101+bartender2.getFinalValue(Sextype.GROUP)+bartender2.getFinalValue(SpecializationAttribute.SEDUCTION));
					int size=Util.getInt(8, 10);
					Object arg2[] = {1+getCustomers().size()/size, getCustomers().get(Util.getInt(0, getCustomers().size()-1)).getName(), getCustomers().size()*rand/size};
					message= TextUtil.t("barevent.group", bartender2, arg2);
					if(bartender2.getTraits().contains(Trait.SEXADDICT) && Util.getInt(1, 100)>50){
						bartender2.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1+getCustomers().size()/10);
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.OBEDIENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.GROUP, bartender2));
						this.getAttributeModifications().add(new AttributeModification(1.55f,SpecializationAttribute.SEDUCTION, bartender2));
						this.getAttributeModifications().add(new AttributeModification(2.0f,EssentialAttributes.MOTIVATION, bartender2));
						message+="\n" + TextUtil.t("barevent.group.queen", bartender2,arg2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
						for(int b=0; b<getCustomers().size()/size; b++){
							getCustomers().get(b).payFixed(Math.min(rand, getCustomers().get(b).getMoney()));
							getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/2, this);
							if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()!=CustomerStatus.STRONGSTATUS)){getCustomers().get(b).setStatus(CustomerStatus.TIRED);}
						}
						for(int z=getCustomers().size()/size; z<getCustomers().size(); z++){
							getCustomers().get(z).addToSatisfaction(15+(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/4, this);
							if(Util.getInt(1, 4)==1 && (getCustomers().get(z).getStatus()==CustomerStatus.HORNYSTATUS || getCustomers().get(z).getStatus()==CustomerStatus.HYPED)){getCustomers().get(z).setStatus(CustomerStatus.VERYHORNY);}
							if(Util.getInt(1, 4)==1 && (getCustomers().get(z).getStatus()==CustomerStatus.HAPPY || getCustomers().get(z).getStatus()==CustomerStatus.LIVELY)){getCustomers().get(z).setStatus(CustomerStatus.HORNYSTATUS);}
						}
					}
					else if(servedToday2>7){
						bartender2.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1+getCustomers().size()/10);
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.OBEDIENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(1.1f,Sextype.GROUP, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.85f,SpecializationAttribute.SEDUCTION, bartender2));
						message+="\n" + TextUtil.t("barevent.group.lotalready", bartender2,arg2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
						for(int b=0; b<getCustomers().size()/size; b++){
							getCustomers().get(b).payFixed(Math.min(rand, getCustomers().get(b).getMoney()));
							getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/2, this);
							if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()!=CustomerStatus.STRONGSTATUS)){getCustomers().get(b).setStatus(CustomerStatus.TIRED);}
						}
					}
					else if(bartender2.getFinalValue(Sextype.GROUP)>35 || bartender2.getFinalValue(SpecializationAttribute.SEDUCTION)>35){
						bartender2.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1+getCustomers().size()/10);
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.OBEDIENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(1.1f,Sextype.GROUP, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.85f,SpecializationAttribute.SEDUCTION, bartender2));
						message+="\n" + TextUtil.t("barevent.group.win.usedtoit", bartender2,arg2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
						for(int b=0; b<getCustomers().size()/size; b++){
							getCustomers().get(b).payFixed(Math.min(rand, getCustomers().get(b).getMoney()));
							getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/2, this);
							if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()!=CustomerStatus.STRONGSTATUS)){getCustomers().get(b).setStatus(CustomerStatus.TIRED);}
						}
					}
					else if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>15 || bartender2.getTraits().contains(Trait.NYMPHO)){
						bartender2.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1+getCustomers().size()/10);
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.1f,BaseAttributeTypes.OBEDIENCE, bartender2));
						this.getAttributeModifications().add(new AttributeModification(1.1f,Sextype.GROUP, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.85f,SpecializationAttribute.SEDUCTION, bartender2));
						message+="\n" + TextUtil.t("barevent.group.win", bartender2,arg2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
						for(int b=0; b<getCustomers().size()/size; b++){
							getCustomers().get(b).payFixed(Math.min(rand, getCustomers().get(b).getMoney()));
							getCustomers().get(b).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/2, this);
							if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()!=CustomerStatus.STRONGSTATUS)){getCustomers().get(b).setStatus(CustomerStatus.TIRED);}
						}
					}
					else{
						message+="\n" + TextUtil.t("barevent.group.lose", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						for(int b=0; b<getCustomers().size()/15; b++){
							getCustomers().get(b).addToSatisfaction(-10, this);
							if(Util.getInt(1, 4)==1 && (getCustomers().get(b).getStatus()!=CustomerStatus.STRONGSTATUS)){getCustomers().get(b).setStatus(CustomerStatus.PISSED);}
						}
					}
					break;
				case BIGBOOB:
					message=TextUtil.t("barevent.bigboob", bartender2);
					if(bartender2.getFinalValue(SpecializationAttribute.BARTENDING)>Util.getInt(20, 120) 
							|| bartender2.getTraits().contains(Trait.MEATBUNS)
							|| bartender2.getTraits().contains(Trait.LEWD)){
						this.getAttributeModifications().add(new AttributeModification(0.08f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));

						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
						int boobPay=0;
						int boobPayTotal=0;
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA))/8, this);
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SAD || customer.getStatus()==CustomerStatus.PISSED)){customer.setStatus(CustomerStatus.HAPPY);}
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.LIVELY || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.HORNYSTATUS);}
							boobPay=Util.getInt(1, 10);
							customer.payFixed(boobPay);
							boobPayTotal+=boobPay;

						}
						modifyIncome(boobPayTotal);
						Object arg6[] ={boobPayTotal};
						message+="\n" + TextUtil.t("barevent.bigboob.win", bartender2, arg6);
					}
					else{
						message+="\n" + TextUtil.t("barevent.bigboob.lose", bartender2);
						this.getAttributeModifications().add(new AttributeModification(-0.5f,EssentialAttributes.MOTIVATION, bartender2));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(-10, this);
						}
					}
					break;
				case SMALLBOOB:
					message=TextUtil.t("barevent.smallboob", bartender2);
					if(bartender2.getFinalValue(SpecializationAttribute.BARTENDING)>Util.getInt(20, 70) 
							|| bartender2.getTraits().contains(Trait.OUTGOING)
							|| bartender2.getTraits().contains(Trait.CROWDLOVER)){
						this.getAttributeModifications().add(new AttributeModification(0.08f,BaseAttributeTypes.CHARISMA, bartender2));
						this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));
						message+="\n" + TextUtil.t("barevent.smallboob.win", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA))/8, this);
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.SAD || customer.getStatus()==CustomerStatus.PISSED)){customer.setStatus(CustomerStatus.HAPPY);}
							if(Util.getInt(1, 4)==1 && (customer.getStatus()==CustomerStatus.LIVELY || customer.getStatus()==CustomerStatus.DRUNK)){customer.setStatus(CustomerStatus.HORNYSTATUS);}

						}
					}
					else{
						message+="\n" + TextUtil.t("barevent.smallboob.lose", bartender2);
						this.getAttributeModifications().add(new AttributeModification(-0.7f,EssentialAttributes.MOTIVATION, bartender2));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(-10, this);
						}
					}
					break;
				case BREAKSTUFF:
					message=TextUtil.t("barevent.breakstuff", bartender2);
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLEAN, bartender2);
					this.modifyIncome(-200);
					break;
				case LOLI:
					message=TextUtil.t("barevent.loli", bartender2);
					if(bartender2.getFinalValue(SpecializationAttribute.BARTENDING)>Util.getInt(20, 120) ){
						this.getAttributeModifications().add(new AttributeModification(0.08f,BaseAttributeTypes.CHARISMA, bartender2));
						message+="\n" + TextUtil.t("barevent.loli.win", bartender2);
						this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(5, this);

						}
					}
					else{
						message+="\n" + TextUtil.t("barevent.loli.lose", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
						this.getAttributeModifications().add(new AttributeModification(-0.7f,EssentialAttributes.MOTIVATION, bartender2));
						for (Customer customer : getCustomers()) {
							if(Util.getInt(0, 12)==2)
								customer.addToSatisfaction(-5, this);
							else
								customer.addToSatisfaction(-15, this);
						}
					}
					break;
				case TIRED:
					this.getAttributeModifications().add(new AttributeModification(-0.5f,EssentialAttributes.MOTIVATION, bartender2));
					if(getCustomers().size()>90){
						message= TextUtil.t("barevent.tired.one", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.SLEEP, bartender2);
						this.getAttributeModifications().add(new AttributeModification(-10.0f,EssentialAttributes.ENERGY, bartender2));
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(-5, this);
						}
					}
					else if(getCustomers().size()>50){
						this.getAttributeModifications().add(new AttributeModification(-10.5f,EssentialAttributes.ENERGY, bartender2));
						message=TextUtil.t("barevent.tired.two", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(-10, this);
						}
					}

					break;
				case SHY:
					message=TextUtil.t("barevent.shy", bartender2);
					if(Util.getInt(0, 10)>5 && !bartender2.getTraits().contains(Trait.OUTGOING)){
						this.getAttributeModifications().add(new AttributeModification(0.08f,BaseAttributeTypes.CHARISMA, bartender2));
						message+="\n" + TextUtil.t("barevent.shy.winwin", bartender2);
						this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(10, this);

						}
					}
					else if(Util.getInt(0, 10)>5){
						message+="\n" + TextUtil.t("barevent.shy.win", bartender2);
						this.getAttributeModifications().add(new AttributeModification(0.5f,EssentialAttributes.MOTIVATION, bartender2));
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(5, this);
						}
					}
					else{
						message+="\n" + TextUtil.t("barevent.shy.lose", bartender2);

						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, bartender2);
						for (Customer customer : getCustomers()) {
							customer.addToSatisfaction(-5, this);
						}
					}
					break;
				case SMALLTALK:
					this.getAttributeModifications().add(new AttributeModification(0.2f,EssentialAttributes.MOTIVATION, bartender2));
					message=TextUtil.t("barevent.smalltalk", bartender2);
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartender2);
					for (Customer customer : getCustomers()) {
						customer.addToSatisfaction(5, this);
					}
					break;
				case SEXSMELL:
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartender2);
					this.getAttributeModifications().add(new AttributeModification(0.2f,EssentialAttributes.MOTIVATION, bartender2));
					long servedToday=bartender2.getCounter().get(CounterNames.CUSTOMERSSERVEDTODAY.toString());
					Object ar[]={servedToday, Util.getInt(3, 7)};
					message=TextUtil.t("barevent.sexsmell", bartender2)+"\n";
					if(servedToday>20+bartender2.getStamina()/3 &&
							(bartender2.getTraits().contains(Trait.SEXADDICT) 
									|| bartender2.getTraits().contains(Trait.SLUT)
									|| bartender2.getTraits().contains(Trait.KEEPEMCOMING)
									|| bartender2.getTraits().contains(Trait.NYMPHO)
									|| (bartender2.getTraits().contains(Trait.FLIRTY) && Util.getInt(0, 100)<50))){
						message+=TextUtil.t("barevent.sexsmell.satisfied", bartender2, ar);
					}
					else if(servedToday>15+bartender2.getStamina()/5 &&
							(bartender2.getTraits().contains(Trait.SEXADDICT) 
									|| bartender2.getTraits().contains(Trait.SLUT)
									|| bartender2.getTraits().contains(Trait.KEEPEMCOMING)
									|| bartender2.getTraits().contains(Trait.NYMPHO)
									|| (bartender2.getTraits().contains(Trait.FLIRTY) && Util.getInt(0, 100)<50))){
						message+=TextUtil.t("barevent.sexsmell.coulddomore", bartender2, ar);
						if(Util.getInt(0, 100)<50){
							message+="\n"+TextUtil.t("barevent.sexsmell.coulddomore.okay", bartender2, ar);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, bartender2);
							this.getAttributeModifications().add(new AttributeModification(0.28f,Sextype.VAGINAL, bartender2));
						}
					}
						
					else if(servedToday<15+bartender2.getStamina()/5 &&
							(bartender2.getTraits().contains(Trait.SEXADDICT) 
									|| bartender2.getTraits().contains(Trait.SLUT)
									|| bartender2.getTraits().contains(Trait.NYMPHO))){
						message+=TextUtil.t("barevent.sexsmell.wantedmore", bartender2, ar);
						if(Util.getInt(0, 100)<50){
							message+="\n"+TextUtil.t("barevent.sexsmell.wantedmore.okay", bartender2, ar);
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, bartender2);
							this.getAttributeModifications().add(new AttributeModification(0.18f,Sextype.GROUP, bartender2));
						}
					}
						
					else if(servedToday>7+bartender2.getStamina())
						message+=TextUtil.t("barevent.sexsmell.waytoomuch", bartender2, ar);
					else
						message+=TextUtil.t("barevent.sexsmell.notthatmuch", bartender2, ar);
					
					for (Customer customer : getCustomers()) {
						customer.addToSatisfaction(13, this);
					}
					break;
				case SNACKS:
					message=TextUtil.t("barevent.snacks", bartender2);
					this.getAttributeModifications().add(new AttributeModification(1.1f,SpecializationAttribute.COOKING, bartender2));
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.COOK, bartender2);
					for (Customer customer : getCustomers()) {
						customer.addToSatisfaction((bartender2.getFinalValue(SpecializationAttribute.COOKING))/8, this);
					}
					break;
				case LOUDBUNCH:
					int randomChat=Util.getInt(0, 9);
					int chatBonus=0;
					this.getAttributeModifications().add(new AttributeModification(1.0f,SpecializationAttribute.BARTENDING, bartender2));
					this.getAttributeModifications().add(new AttributeModification(0.08f,BaseAttributeTypes.CHARISMA, bartender2));
					this.getAttributeModifications().add(new AttributeModification(0.8f,EssentialAttributes.MOTIVATION, bartender2));
					message=TextUtil.t("barevent.loudbunch", bartender2);
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, bartender2);
					if(bartender2.getFinalValue(SpecializationAttribute.SEDUCTION)>100 && randomChat==1){
						chatBonus=bartender2.getFinalValue(SpecializationAttribute.SEDUCTION)/10;
						message+=TextUtil.t("barevent.loudbunch.whore", bartender2);
					}
					else if(bartender2.getFinalValue(Sextype.GROUP)>20 && randomChat==1){
						chatBonus=bartender2.getFinalValue(Sextype.GROUP)/10;
						message+=TextUtil.t("barevent.loudbunch.group", bartender2);
					}
					else if(bartender2.getFinalValue(Sextype.MONSTER)>35 && randomChat==2){
						chatBonus=bartender2.getFinalValue(Sextype.MONSTER)/10;
						message+=TextUtil.t("barevent.loudbunch.monster", bartender2);
					}
					else if(bartender2.getFinalValue(SpecializationAttribute.STRIP)>20 && randomChat==3){
						chatBonus=bartender2.getFinalValue(SpecializationAttribute.STRIP)/10;
						message+=TextUtil.t("barevent.loudbunch.dance", bartender2);
						image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
					}
					else if(bartender2.getFinalValue(SpecializationAttribute.VETERAN)>20 && randomChat==4){
						chatBonus=bartender2.getFinalValue(SpecializationAttribute.VETERAN)/10;
						message+=TextUtil.t("barevent.loudbunch.fight", bartender2);
					}
					else if(bartender2.getFinalValue(BaseAttributeTypes.STRENGTH)>25 && randomChat==5){
						chatBonus=bartender2.getFinalValue(BaseAttributeTypes.STRENGTH)/5;
						message+=TextUtil.t("barevent.loudbunch.strength", bartender2);
					}
					else if(randomChat==6){
						chatBonus=10;
						message+=TextUtil.t("barevent.loudbunch.drink", bartender2);
						this.modifyIncome(800);
					}
					else{
						chatBonus=5;
						message+=TextUtil.t("barevent.loudbunch.fun", bartender2);
					}
					for (Customer customer : getCustomers()) {
						customer.addToSatisfaction(1+chatBonus, this);
					}
					break;

				}
				if(message!=null){getMessages().add(new MessageData(message, image, bartender2.getBackground()));}
			}
		}
	}

	@Override
	public MessageData getBaseMessage() {
		Object arguments[] = { TextUtil.listCharacters(bartenders) };
		String messageText = TextUtil.t("bartend.basic", arguments);
		this.messageData = new MessageData(messageText, null, getBackground());
		for(Charakter character : bartenders){
			this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, character.getImages()));
		}
		return this.messageData;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, -20, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, -0.5f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.ALL, 0.6f, SpecializationAttribute.BARTENDING));
		if(!(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.LEGACYBARTENDER))){
			modifications.add(new ModificationData(TargetType.TRAINER, -0.15f, BaseAttributeTypes.COMMAND));
		}	
		return modifications;
	}

	@Override
	public int getAppeal() {
		int app=0;
		for(Charakter chara : getCharacters()){
			app+=chara.getCharisma()+chara.getFinalValue(SpecializationAttribute.BARTENDING);
			app/=2;
		}
		return app;
	}

	@Override
	public int getMaxAttendees() {
		int amount = 0;
		for (Charakter bartender : getCharacters()) {
			amount += 10 + bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 3;
		}
		amount+=bonus;
		return amount;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
}