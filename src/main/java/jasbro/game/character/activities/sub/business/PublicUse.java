package jasbro.game.character.activities.sub.business;


import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.CharacterStuffCounter.CounterNames;
import jasbro.game.character.CharacterType;
import jasbro.game.character.activities.BusinessSecondaryActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.sub.business.Bartend.BarAction;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerStatus;
import jasbro.game.events.business.CustomerType;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;




import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PublicUse extends RunningActivity implements BusinessSecondaryActivity {

	public enum PublicUseEvent {
		ROUGHCUSTOMER, GENTLECUSTOMER, LINE, SWARM, NOBREAK, NORMAL, NICECUSTOMER,
		ALLANAL, ALLVAGINAL, BUKKAKE, OVERTIME, ALL, ALONE, NONE, NOCUSTOMER
		;
	}

	private MessageData messageData;
	private int bonus;
	private Charakter thatOneGirl=null;
	private List<Charakter> girls = new ArrayList<Charakter>();
	private Map<Charakter, Short> groupSize=new HashMap<Charakter, Short>();
	private Map<Charakter, Short> remainingTime=new HashMap<Charakter, Short>();
	private Map<Charakter, Short> totalServed=new HashMap<Charakter, Short>();
	private Map<Charakter, Short> energy=new HashMap<Charakter, Short>();
	private Map<Charakter, Short> energySpent=new HashMap<Charakter, Short>();
	private Map<Charakter, Short> maxEnergy=new HashMap<Charakter, Short>();
	private Map<Charakter, PublicUseEvent> event=new HashMap<Charakter, PublicUseEvent>();
	private Map<Charakter, Short> girlStatus=new HashMap<Charakter, Short>(); //0= okay, 1=timeout, 2=passed out
	@Override
	public void init() {
		girls.addAll(getCharacters());
		Collections.shuffle(girls);
		short en=0;
		boolean someoneTookAll=false;
		for(Charakter currentGirl : girls){
			en=(short) (100+currentGirl.getStamina()/10);
			if(currentGirl.getTraits().contains(Trait.SEXADDICT))
				en+=25;
			if(currentGirl.getTraits().contains(Trait.GANGBANGQUEEN))
				en+=25;
			if(currentGirl.getTraits().contains(Trait.KEEPEMCOMING))
				en+=25;
			if(currentGirl.getTraits().contains(Trait.PERSEVERING))
				en+=10;
			if(currentGirl.getTraits().contains(Trait.NYMPHO))
				en+=10;
			if(currentGirl.getTraits().contains(Trait.SEXADDICT))
				en+=25;
			if(currentGirl.getTraits().contains(Trait.FRAGILE))
				en-=25;
			if(currentGirl.getTraits().contains(Trait.FLABBY))
				en-=25;		
			if(currentGirl.getTraits().contains(Trait.SINGLEMINDED))
				en-=25;
			energy.put(currentGirl, en);
			energySpent.put(currentGirl, (short) 0);
			maxEnergy.put(currentGirl, en);
			totalServed.put(currentGirl, (short) 0);
			girlStatus.put(currentGirl, (short) 0);
			if(currentGirl.getTraits().contains(Trait.PUBLICUSE))
				remainingTime.put(currentGirl, (short) 400);
			else
				remainingTime.put(currentGirl, (short) 300);



			//events
			List<PublicUseEvent> actions = new ArrayList<PublicUseEvent>();
			actions.add(PublicUseEvent.ROUGHCUSTOMER);
			actions.add(PublicUseEvent.GENTLECUSTOMER);
			if(getCustomers().size()>25)
				actions.add(PublicUseEvent.LINE);
			if(getCustomers().size()>25)
				actions.add(PublicUseEvent.SWARM);
			actions.add(PublicUseEvent.NOBREAK);
			actions.add(PublicUseEvent.NICECUSTOMER);
			actions.add(PublicUseEvent.NOBREAK);
			actions.add(PublicUseEvent.ALLANAL);
			actions.add(PublicUseEvent.ALLVAGINAL);
			actions.add(PublicUseEvent.BUKKAKE);
			actions.add(PublicUseEvent.NORMAL);
			actions.add(PublicUseEvent.NORMAL);
			if(getCustomers().size()>25)
				actions.add(PublicUseEvent.OVERTIME);
			if(currentGirl.getTraits().contains(Trait.SEXADDICT) && getCustomers().size()>25  && Util.getInt(0, 100)<5){
				event.put(currentGirl, PublicUseEvent.ALL);	
				remainingTime.put(currentGirl, (short) 30000);
			}
			else if(currentGirl.getTraits().contains(Trait.GANGBANGQUEEN) && getCharacters().size()!=1 && someoneTookAll==false && getCustomers().size()>25 && Util.getInt(0, 100)<5){
				event.put(currentGirl, PublicUseEvent.ALONE);	
				thatOneGirl=currentGirl;
				remainingTime.put(currentGirl, (short) (remainingTime.get(currentGirl)*150/100));
				someoneTookAll=true;
				for(Charakter otherGirl : getCharacters()){
					if(otherGirl!=currentGirl)
						event.put(otherGirl, PublicUseEvent.NONE);

				}
			}
			if(event.get(currentGirl)!=PublicUseEvent.ALONE && event.get(currentGirl)!=PublicUseEvent.NONE && event.get(currentGirl)!=PublicUseEvent.ALL)
				event.put(currentGirl, actions.get(Util.getInt(0, actions.size())));
		}
	}

	@Override
	public void perform() {
		if(getCustomers().size()>=5){

			short totalTips=0;
			short timeTaken=0;
			short energyBefore;
			short spentEnergy=0;
			short customersServed=0;
			short servedThisRound=1;
			short breakTime=100;
			short energyCostFactor=100;
			Sextype sex = null;
			short rand=0;
			while(customersServed<=getCustomers().size() && isTimeLeft(getCharacters())){
				for(Charakter currentGirl : girls){
					servedThisRound=0;

					//Number of customers taken each round
					groupSize.put(currentGirl, (short) (Util.getInt(2, 3)+currentGirl.getFinalValue(Sextype.GROUP)/20));
					if(currentGirl.getTraits().contains(Trait.GANGBANGQUEEN))
						groupSize.put(currentGirl, (short) (groupSize.get(currentGirl)+Util.getInt(0, 1)));
					if(currentGirl.getTraits().contains(Trait.PUBLICUSE))
						groupSize.put(currentGirl, (short) (groupSize.get(currentGirl)+Util.getInt(0, 2)));
					if(currentGirl.getTraits().contains(Trait.MULTIFACETED))
						groupSize.put(currentGirl, (short) (groupSize.get(currentGirl)+Util.getInt(0, 1)));
					//event
					switch(event.get(currentGirl)){
					case ROUGHCUSTOMER:
						energyCostFactor=130;
						break;
					case GENTLECUSTOMER:
						energyCostFactor=70;
						break;
					case NICECUSTOMER:
						breakTime=130;
						break;
					case LINE:
						groupSize.put(currentGirl, (short) 1);
						energyCostFactor=30;
						breakTime=5;
						break;
					case ALLANAL:
						if(currentGirl.getFinalValue(Sextype.ANAL)>90 && Util.getInt(0, 100)>20)
							groupSize.put(currentGirl, (short) 2);
						else if(currentGirl.getFinalValue(Sextype.ANAL)>90 && Util.getInt(0, 100)>50)
							groupSize.put(currentGirl, (short) 2);
						else
							groupSize.put(currentGirl, (short) 1);
						energyCostFactor=110;
						breakTime=10;
						break;
					case ALLVAGINAL:
						if(currentGirl.getFinalValue(Sextype.VAGINAL)>90 && Util.getInt(0, 100)>20)
							groupSize.put(currentGirl, (short) 2);
						else if(currentGirl.getFinalValue(Sextype.VAGINAL)>90 && Util.getInt(0, 100)>50)
							groupSize.put(currentGirl, (short) 2);
						else
							groupSize.put(currentGirl, (short) 1);
						energyCostFactor=110;
						breakTime=5;
						break;
					case BUKKAKE:
						groupSize.put(currentGirl, (short) (2+currentGirl.getFinalValue(Sextype.ORAL)/20));
						energyCostFactor=30;
						breakTime=80;
						break;
					case SWARM:
						groupSize.put(currentGirl, (short) (groupSize.get(currentGirl)+2));
						energyCostFactor=110;
						break;
					case NOBREAK:
						if(energy.get(currentGirl)>10)
							breakTime=0;
						break;
					case OVERTIME:
						remainingTime.put(currentGirl, (short) (remainingTime.get(currentGirl)*150/100));
						break;
					case ALONE:
						remainingTime.put(currentGirl, (short) (remainingTime.get(currentGirl)*130/100));
						breakTime=2;
						groupSize.put(currentGirl, (short) (groupSize.get(currentGirl)+2));
						break;
					default:
						break;

					}
					if(groupSize.get(currentGirl)>getCustomers().size())
						groupSize.put(currentGirl, (short) getCustomers().size());
					energyCostFactor+=totalServed.get(currentGirl);
					if(energy.get(currentGirl)>5 && remainingTime.get(currentGirl)>10 && event.get(currentGirl)!=PublicUseEvent.NONE){

						for(short i=customersServed; i<getCustomers().size(); i++){
							//sextype
							rand=(short) Util.getInt(0, 100);
							if(rand<20 || event.get(currentGirl)==PublicUseEvent.BUKKAKE)
								sex=Sextype.ORAL;
							else if(rand<60 || event.get(currentGirl)==PublicUseEvent.ALLVAGINAL)
								sex=Sextype.VAGINAL;
							else
								sex=Sextype.ANAL;

							if(event.get(currentGirl)==PublicUseEvent.BUKKAKE)
								sex=Sextype.ORAL;
							if(event.get(currentGirl)==PublicUseEvent.ALLVAGINAL)
								sex=Sextype.VAGINAL;
							if(event.get(currentGirl)==PublicUseEvent.ALLANAL)
								sex=Sextype.ANAL;
							energyBefore=energy.get(currentGirl);
							//serve one customer
							currentGirl.getCounter().add(CounterNames.CUSTOMERSSERVEDTODAY.toString(), (long) 1);
							totalServed.put(currentGirl, (short) (totalServed.get(currentGirl)+1));
							servedThisRound++;
							customersServed++;
							timeTaken=(short) (12-groupSize.get(currentGirl)/2);
							timeTaken-=timeTaken*(currentGirl.getFinalValue(sex)+currentGirl.getFinalValue(Sextype.GROUP))/250;
							totalTips+=getCustomers().get(i).payFixed(Util.getInt(1, 5+currentGirl.getFinalValue(sex)));
							getCustomers().get(i).addToSatisfaction(2+currentGirl.getFinalValue(sex)/16, this);
							remainingTime.put(currentGirl, (short) (remainingTime.get(currentGirl)-timeTaken));
							this.getAttributeModifications().add(new AttributeModification(0.015f,BaseAttributeTypes.OBEDIENCE, currentGirl));
							this.getAttributeModifications().add(new AttributeModification(0.1f,sex, currentGirl));
							this.getAttributeModifications().add(new AttributeModification(0.1f,Sextype.GROUP, currentGirl));
							this.getAttributeModifications().add(new AttributeModification(0.02f,BaseAttributeTypes.STAMINA, currentGirl));
							if(Util.getInt(0, 100)<100)
								currentGirl.getFame().modifyFame(1);
							this.getHouse().modDirt(2);
							if(servedThisRound==groupSize.get(currentGirl)){
								//finish round
								spentEnergy=(short) (servedThisRound*10*(energyCostFactor+Util.getInt(-10, 10))/100);
								energy.put(currentGirl, (short) (energy.get(currentGirl)-spentEnergy));	
								energySpent.put(currentGirl, (short) (energySpent.get(currentGirl)+spentEnergy));
								if(energyBefore>0 && energy.get(currentGirl)<=0)
									girlStatus.put(currentGirl, (short) (girlStatus.get(currentGirl)+1));
								break;
							}						
						}					
					}
					//break time!
					if(energy.get(currentGirl)<10)
						breakTime+=100;
					if(currentGirl.getTraits().contains(Trait.PUBLICUSE))
						remainingTime.put(currentGirl, (short) (remainingTime.get(currentGirl)-(breakTime/30)-1));
					else
						remainingTime.put(currentGirl, (short) (remainingTime.get(currentGirl)-(breakTime/20)-1));
					energy.put(currentGirl, (short) (energy.get(currentGirl)+5+(currentGirl.getStamina()/5+getCharacters().size())*breakTime/100));



				}


			}//Either all customers are served or time ran out
			modifyIncome(totalTips);
			for(Charakter character:getCharacters()){
				if(totalServed.get(character)<groupSize.get(character))
					groupSize.put(character, totalServed.get(character));
				if(totalServed.get(character)==0 && event.get(character)!=PublicUseEvent.NONE)
					event.put(character, PublicUseEvent.NOCUSTOMER);

				Object arguments[] = {totalServed.get(character), groupSize.get(character), girlStatus.get(character)};
				messageData.addToMessage("\n");
				switch(event.get(character)){
				case NORMAL:
					messageData.addToMessage("\n"+TextUtil.t("public.normal",character, arguments));
					break;
				case ROUGHCUSTOMER:
					messageData.addToMessage("\n"+TextUtil.t("public.rough",character, arguments));
					break;
				case GENTLECUSTOMER:
					messageData.addToMessage("\n"+TextUtil.t("public.gentle",character, arguments));
					break;
				case NICECUSTOMER:
					messageData.addToMessage("\n"+TextUtil.t("public.nice",character, arguments));
					break;
				case NOBREAK:
					messageData.addToMessage("\n"+TextUtil.t("public.nobreak",character, arguments));
					break;
				case ALLANAL:
					messageData.addToMessage("\n"+TextUtil.t("public.anal",character, arguments));
					break;
				case ALLVAGINAL:
					messageData.addToMessage("\n"+TextUtil.t("public.vaginal",character, arguments));
					break;
				case BUKKAKE:
					messageData.addToMessage("\n"+TextUtil.t("public.bukkake",character, arguments));
					break;
				case LINE:
					messageData.addToMessage("\n"+TextUtil.t("public.line",character, arguments));
					break;
				case SWARM:
					messageData.addToMessage("\n"+TextUtil.t("public.swarm",character, arguments));
					break;
				case OVERTIME:
					messageData.addToMessage("\n"+TextUtil.t("public.overtime",character, arguments));
					break;
				case NONE:
					messageData.addToMessage("\n"+TextUtil.t("public.none",character, thatOneGirl, arguments));
					break;
				case NOCUSTOMER:
					messageData.addToMessage("\n"+TextUtil.t("public.nocustomer",character, thatOneGirl, arguments));
					break;
				case ALONE:
					messageData.addToMessage("\n"+TextUtil.t("public.alone",character, arguments));
					if(totalServed.get(character)==getCustomers().size())
						messageData.addToMessage("\n"+TextUtil.t("public.alone.success",character, arguments));
					else
						messageData.addToMessage("\n"+TextUtil.t("public.alone.failure",character, arguments));
					break;
				case ALL:
					messageData.addToMessage("\n"+TextUtil.t("public.all",character, arguments));
					break;
				}
				if(totalServed.get(character)!=0){
					if(energySpent.get(character)<maxEnergy.get(character)){
						messageData.addToMessage("\n"+TextUtil.t("public.stamina.easy",character, arguments));
						this.getAttributeModifications().add(new AttributeModification(-30.0f-girlStatus.get(character)*2.0f,EssentialAttributes.ENERGY, character));
					}
					else if(energySpent.get(character)<maxEnergy.get(character)*15/10){
						this.getAttributeModifications().add(new AttributeModification(-45.0f-girlStatus.get(character)*3.0f,EssentialAttributes.ENERGY, character));
						messageData.addToMessage("\n"+TextUtil.t("public.stamina.normal",character, arguments));
						if(girlStatus.get(character)==1)
							messageData.addToMessage(" "+TextUtil.t("public.stamina.normal.faint.once",character, arguments));
						if(girlStatus.get(character)==2)
							messageData.addToMessage(" "+TextUtil.t("public.stamina.normal.faint.twice",character, arguments));
						if(girlStatus.get(character)>2)
							messageData.addToMessage(" "+TextUtil.t("public.stamina.normal.faint.more",character, arguments));
					}
					else if(energySpent.get(character)<maxEnergy.get(character)*2){
						this.getAttributeModifications().add(new AttributeModification(-60.0f-girlStatus.get(character)*4.0f,EssentialAttributes.ENERGY, character));
						messageData.addToMessage("\n"+TextUtil.t("public.stamina.hard",character, arguments));
						if(girlStatus.get(character)==1)
							messageData.addToMessage(" "+TextUtil.t("public.stamina.hard.faint.once",character, arguments));
						if(girlStatus.get(character)==2)
							messageData.addToMessage(" "+TextUtil.t("public.stamina.hard.faint.twice",character, arguments));
						if(girlStatus.get(character)>2)
							messageData.addToMessage(" "+TextUtil.t("public.stamina.hard.faint.more",character, arguments));
					}
					else{
						this.getAttributeModifications().add(new AttributeModification(-75.0f-girlStatus.get(character)*5.0f,EssentialAttributes.ENERGY, character));
						messageData.addToMessage("\n"+TextUtil.t("public.stamina.exhausted",character, arguments));
						if(girlStatus.get(character)==1)
							messageData.addToMessage(" "+TextUtil.t("public.stamina.exhausted.faint.once",character, arguments));
						if(girlStatus.get(character)==2)
							messageData.addToMessage(" "+TextUtil.t("public.stamina.exhausted.faint.twice",character, arguments));
						if(girlStatus.get(character)>2)
							messageData.addToMessage(" "+TextUtil.t("public.stamina.exhausted.faint.more",character, arguments));
					}
				}
			}
			Object arguments[] = {totalTips};

			messageData.addToMessage("\n\n"+TextUtil.t("public.result.final", arguments));

		}
		else{
			messageData.addToMessage(TextUtil.t("public.notenoughcustomers"));
		}
	}


	@Override
	public MessageData getBaseMessage() {
		Object arguments[] = {TextUtil.listCharacters(getCharacters()) , getCustomers().size()};
		String messageText = TextUtil.t("public.basic", arguments);
		this.messageData = new MessageData(messageText,null, getBackground());

		for(Charakter character : getCharacters()){
			if(getCustomers().size()>=5){
				switch(event.get(character)){
				case NORMAL:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
					break;
				case GENTLECUSTOMER:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
					break;
				case ALL:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
					break;
				case ALONE:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
					break;
				case ROUGHCUSTOMER:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
					break;
				case NICECUSTOMER:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
					break;
				case OVERTIME:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
					break;
				case NOBREAK:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
					break;
				case SWARM:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, character));
					break;
				case LINE:
					if(Util.getInt(0, 100)<50)
						this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character));
					else
						this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character));
					break;
				case ALLANAL:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, character));
					break;
					
				case ALLVAGINAL:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, character));
					break;
				case BUKKAKE:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.BUKKAKE, character));
					break;
				default:
					this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character));
					break;


				}
			}
			else
				this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, character));
		}


		return this.messageData;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		if(getCustomers().size() >= 10){

			for(Charakter character : getCharacters()){
				if(character.getTraits().contains(Trait.SEXADDICT))
					modifications.add(new ModificationData(TargetType.SINGLE,character, -0.75f, EssentialAttributes.MOTIVATION));

				else
					modifications.add(new ModificationData(TargetType.SINGLE,character, -1.5f, EssentialAttributes.MOTIVATION));
				if(character.getType()==CharacterType.TRAINER && !character.getTraits().contains(Trait.LEGACYWHORE))
					modifications.add(new ModificationData(TargetType.SINGLE,character, -.5f, BaseAttributeTypes.COMMAND));
			}
		}
		return modifications;
	}


	@Override
	public int getAppeal() {
		return (1);
	}


	@Override
	public int getMaxAttendees() {
		return 60+getCharacters().size()*40;
	}


	public int getBonus() {
		return bonus;
	}


	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	private boolean isTimeLeft(List<Charakter> list){
		for(Charakter character : list){
			if(remainingTime.get(character)>0)
				return true;

		}
		return false;
	}

}