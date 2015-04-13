package jasbro.game.character.activities.sub.business;


import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;



import java.util.ArrayList;
import java.util.List;


public class PublicUse extends RunningActivity implements BusinessSecondaryActivity {

	private MessageData messageData;
	private int bonus;
	private int slaveMax;
	private List<Charakter> girls = new ArrayList<Charakter>();

	@Override
	public void init() {
		girls.addAll(getCharacters());
		int mult = 0;
		for(int i=0; i<girls.size(); i++){
			slaveMax = 10 + getCharacters().get(i).getStamina() / 5;       
			if(getCharacters().get(i).getTraits().contains(Trait.PUBLICUSE)){mult+=12;}
			if(getCharacters().get(i).getTraits().contains(Trait.GANGBANGQUEEN)){mult+=10;}
			if(getCharacters().get(i).getTraits().contains(Trait.MULTIFACETED)){mult+=5;}
			if(getCharacters().get(i).getTraits().contains(Trait.FRAGILE)){mult-=10;}
			if(getCharacters().get(i).getTraits().contains(Trait.NYMPHO)){mult+=6;}
			if(getCharacters().get(i).getTraits().contains(Trait.PERSEVERING)){mult+=3;}
			if(getCharacters().get(i).getTraits().contains(Trait.SLUT)){mult+=4;}
			if(getCharacters().get(i).getTraits().contains(Trait.SINGLEMINDED)){mult-=5;}
			if(getCharacters().get(i).getTraits().contains(Trait.SEXADDICT)){mult+=8;}
			if(getCharacters().get(i).getTraits().contains(Trait.CUMSLUT)){mult+=4;}
			if(getCharacters().get(i).getTraits().contains(Trait.FLESHTOY)){mult+=6;}
			if(getCharacters().get(i).getTraits().contains(Trait.FLABBY)){mult-=6;}
			slaveMax+=mult;
			slaveMax+=slaveMax*Util.getInt(-20, 20)/100;
		}
		if(slaveMax<5){slaveMax=Util.getInt(5, 7);}
	}

	@Override
	public void perform() {
		Charakter character = getCharacter();
		int skill = 0;
		int chance= 0;
		for(int i=0; i<girls.size(); i++){
			skill+=getCharacters().get(i).getCharisma() + character.getFinalValue(Sextype.GROUP) / 5 + 1;
			chance = getCharacters().get(i).getFinalValue(Sextype.GROUP) / 5;
		}
		skill/=girls.size();
		chance/=girls.size();


		int amountEarned = 0;
		int amountHappy = 0;
		int overalltips = 0;
		int served=0;
		for (Customer customer : getCustomers()) {
			if(served<slaveMax){
				served++;
				if (Util.getInt(1, 100)  <= chance) {
					amountHappy++;
					customer.addToSatisfaction(skill, this);
					int tips = (int)(customer.getMoney() / (1500.0 / skill) + Util.getInt(10, 20));
					tips = customer.pay(tips, getCharacter().getMoneyModifier());
					overalltips += tips;
					amountEarned +=  tips;
					if(customer.getType()==CustomerType.BUM){
						this.getHouse().getFame().modifyFame(5);
					}				
					if(customer.getType()==CustomerType.PEASANT){
						this.getHouse().getFame().modifyFame(2);
					}
					if(customer.getType()==CustomerType.SOLDIER){
						this.getHouse().getFame().modifyFame(1);
					}
				}
				else {
					customer.addToSatisfaction(skill / 4, this);
				}
			}
			else{
				customer.addToSatisfaction(-100, this);
			}
		}
		modifyIncome(amountEarned);
		Object arguments[] = {getCustomers().size(), amountHappy, getIncome(), overalltips, served};
		for(int i=0; i<girls.size(); i++){
			messageData.addToMessage("\n    ");
			int groupskill = getCharacters().get(i).getFinalValue(Sextype.GROUP);
			int groupsize = getCustomers().size();

			if (groupsize >= 1) {
				  	

				if ((getCharacters().get(i).getTraits().contains(Trait.CUMSLUT) || getCharacters().get(i).getTraits().contains(Trait.SEXADDICT) || getCharacters().get(i).getTraits().contains(Trait.SUBMISSIVE) || getCharacters().get(i).getTraits().contains(Trait.MEATTOILET) || getCharacters().get(i).getTraits().contains(Trait.FLESHTOY) || getCharacters().get(i).getTraits().contains(Trait.WILD)) && Util.getInt(0, 100) <50) {
					List<String> actions = new ArrayList<String>();

					if(character.getTraits().contains(Trait.CUMSLUT))
					{
						actions.add("public.perk.cumslut");
					}
					if(getCharacters().get(i).getTraits().contains(Trait.SUBMISSIVE))
					{
						actions.add("public.perk.submissive");
					}
					if(getCharacters().get(i).getTraits().contains(Trait.MEATTOILET))
					{
						actions.add("public.perk.fleshtoy");
					}
					if(getCharacters().get(i).getTraits().contains(Trait.FLESHTOY))
					{
						actions.add("public.perk.meattoilet");
					}
					if(getCharacters().get(i).getTraits().contains(Trait.SEXADDICT))
					{
						actions.add("public.perk.meattoilet");
					}
					if(getCharacters().get(i).getTraits().contains(Trait.WILD))
					{
						actions.add("public.trait.wild");
					}
					int choice = (int) (Math.random() * actions.size());
					messageData.addToMessage("\n" + TextUtil.t( actions.get(choice), getCharacters().get(i), arguments));   			
				}
				else{

					if (groupskill >= 300 && groupsize >= 26) {
						messageData.addToMessage("\n" + TextUtil.t("public.basic.highskill.highamount", getCharacters().get(i), arguments));
					}
					else if (groupskill >= 300 && groupsize <= 25) {
						messageData.addToMessage("\n" + TextUtil.t("public.basic.highskill.lowamount", getCharacters().get(i), arguments));
					}
					else if (groupskill >= 150 && groupsize >= 26) {
						messageData.addToMessage("\n" + TextUtil.t("public.basic.medskill.highamount", getCharacters().get(i), arguments));
					}
					else if (groupskill >= 150 && groupsize <= 25) {
						messageData.addToMessage("\n" + TextUtil.t("public.basic.medskill.lowamount", getCharacters().get(i), arguments));
					}
					else if (groupskill <= 149 && groupsize >= 26) {
						messageData.addToMessage("\n" + TextUtil.t("public.basic.lowskill.highamount", getCharacters().get(i), arguments));
					}
					else{
						messageData.addToMessage("\n" + TextUtil.t("public.basic.lowskill.lowamount", getCharacters().get(i), arguments));
					}
				}	



			}
			else{
				messageData.addToMessage("\n" + TextUtil.t("public.noCustomer", getCharacters().get(i), arguments));
			}

			


			
			if(Util.getInt(0, 100) >= 50 && !getCharacters().get(i).getTraits().contains(Trait.PUBLICUSE)){
				character.addCondition(new Buff.Sore());
				messageData.addToMessage("\n" + TextUtil.t("public.sore", getCharacters().get(i), arguments));
			} 
		}
		
		if (served <= slaveMax*2/3) {
				if(getCharacters().size()==0){messageData.addToMessage("\n\n" + TextUtil.t("public.stamina.high.one", getCharacters().get(0)));}
				else{messageData.addToMessage("\n\n" + TextUtil.t("public.stamina.high.more"));}
			}
			else if(served > slaveMax*2/3 && served!=slaveMax){
				if(getCharacters().size()==0){messageData.addToMessage("\n\n" + TextUtil.t("public.stamina.med.one", getCharacters().get(0)));}
				else{messageData.addToMessage("\n\n" + TextUtil.t("public.stamina.med.more"));}
			}
			else
			{
				if(getCharacters().size()==0){messageData.addToMessage("\n\n" + TextUtil.t("public.stamina.low.one", getCharacters().get(0)));}
				else{messageData.addToMessage("\n\n" + TextUtil.t("public.stamina.low.more",arguments));}
			}
		messageData.addToMessage("\n\n" + TextUtil.t("public.result.tips", arguments));
	}


	@Override
	public MessageData getBaseMessage() {
		Object arguments[] = {TextUtil.listCharacters(girls) , getCustomers().size()};
		String messageText = TextUtil.t("public.basic", arguments);
		this.messageData = new MessageData(messageText, ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, getCharacter()), 
				getBackground());
		if(girls.size()>1){
			for(int i=1; i<girls.size(); i++){
				this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, getCharacters().get(i)));
			}
		}

		return this.messageData;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		if(getCustomers().size() >= 1){

			if(getCharacters().get(0).getTraits().contains(Trait.PUBLICUSE)){
				modifications.add(new ModificationData(TargetType.ALL, -getCustomers().size()*0.17f, EssentialAttributes.ENERGY));
			}
			else{
				modifications.add(new ModificationData(TargetType.ALL, -getCustomers().size()*1f, EssentialAttributes.ENERGY));   
				modifications.add(new ModificationData(TargetType.ALL, -getCustomers().size()*0.17f, EssentialAttributes.HEALTH));
			}
			modifications.add(new ModificationData(TargetType.ALL, 1.1f, Sextype.GROUP));
			modifications.add(new ModificationData(TargetType.ALL, 1.1f, Sextype.BONDAGE));
			if(getCharacter().getGender()==Gender.FEMALE){
				modifications.add(new ModificationData(TargetType.ALL, 1.1f, Sextype.VAGINAL));
			}
			modifications.add(new ModificationData(TargetType.ALL, 1.1f, Sextype.ANAL));
			modifications.add(new ModificationData(TargetType.ALL, 1.1f, Sextype.ORAL));
			modifications.add(new ModificationData(TargetType.ALL, 0.02f, BaseAttributeTypes.STRENGTH));
			modifications.add(new ModificationData(TargetType.ALL, 0.02f, BaseAttributeTypes.STAMINA));
			modifications.add(new ModificationData(TargetType.SLAVE, 0.02f, BaseAttributeTypes.OBEDIENCE));
			modifications.add(new ModificationData(TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));    		
		}
		else{
			modifications.add(new ModificationData(TargetType.ALL, -getCustomers().size()*0.17f, EssentialAttributes.ENERGY));
		}
		return modifications;
	}


	@Override
	public int getAppeal() {
		return (1);
	}


	@Override
	public int getMaxAttendees() {
		int max=50;
		for(int i=0; i<getCharacters().size(); i++){
		if(getCharacters().get(i).getTraits().contains(Trait.GANGBANGQUEEN)){max+=getCharacter().getFinalValue(Sextype.GROUP)/20;}
		}
		return max;
	}


	public int getBonus() {
		return bonus;
	}


	public void setBonus(int bonus) {
		this.bonus = bonus;
	}


}