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
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

import java.util.ArrayList;
import java.util.List;

public class Attend extends RunningActivity implements BusinessSecondaryActivity {

	private MessageData messageData;
	private List<Charakter> dancers = new ArrayList<Charakter>();
	private List<Charakter> bartenders = new ArrayList<Charakter>();

	@Override
	public void init() {
		for (Charakter character : getCharacters()) {
			if (character.getSpecializations().contains(SpecializationType.DANCER) && dancers.size() < 2) {
				dancers.add(character);
			} else {
				bartenders.add(character);
			}
		}

	}

	@Override
	public void perform() {
		int skill = 0;
		for (Charakter dancer : dancers) {
			skill += dancer.getCharisma() + dancer.getFinalValue(SpecializationAttribute.STRIP) / 4;
		}
		for (Charakter bartender : bartenders) {
			skill += bartender.getCharisma() / 2 + bartender.getIntelligence() / 2 + 
					bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 4;
		}
		skill /= 3;

		int amountEarned = 0;
		for (Customer customer : getCustomers()) {
			customer.addToSatisfaction(skill, this);
			int tips = (int) (customer.getMoney() / (1000.0 / skill) + Util.getInt(10, 20));
			tips = customer.pay(tips, getCharacter().getMoneyModifier());
			amountEarned += tips;
		}
		modifyIncome(amountEarned);

		Object arguments[] = { TextUtil.listCharacters(dancers), TextUtil.listCharacters(bartenders), getCustomers().size(), amountEarned };

		messageData.addToMessage("\n" + TextUtil.t("cabaret.result", arguments));


		for(Charakter dancer2 : dancers){

			if(dancer2.getTraits().contains(Trait.EXTRAS) && getCustomers().size()>1){
				int chance=dancer2.getCharisma()/5+dancer2.getFinalValue(SpecializationAttribute.STRIP)/10;
				if(chance>80){chance=80;}

				if(Util.getInt(0, 100)<chance){
					int a=Util.getInt(0, getCustomers().size()-1);
					int extra = (int)(getCustomers().get(a).getMoney() / (800.0 / dancer2.getFinalValue(SpecializationAttribute.STRIP)) + Util.getInt(10, 20));
					Object arg[] = {getCustomers().get(a).getName(), extra};
					String message = null;
					ImageData image;
					image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, dancer2);
					message=TextUtil.t("STRIP.extras", dancer2,arg);
					if(getCustomers().get(a).getPreferredSextype()==Sextype.VAGINAL && dancer2.getAllowedServices().isAllowed(Sextype.VAGINAL)){
						if(getCustomers().get(a).getGender()==Gender.MALE){
							if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.NYMPHO)
									|| dancer2.getTraits().contains(Trait.SLUT)
									|| dancer2.getTraits().contains(Trait.HORNY))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.VAGINAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.vaginal.male.two", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, dancer2);
								getCustomers().get(a).addToSatisfaction(150,this);
								dancer2.getFame().modifyFame(50);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(100+dancer2.getFinalValue(Sextype.VAGINAL)/5, this);
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.VAGINAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.vaginal.male.one", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, dancer2);
								getCustomers().get(a).addToSatisfaction(100, this);
							}
						}
						if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
							if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.NYMPHO)
									|| dancer2.getTraits().contains(Trait.KINKY)
									|| dancer2.getTraits().contains(Trait.SENSITIVE))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.VAGINAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.vaginal.female.two", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DILDO, dancer2);
								getCustomers().get(a).addToSatisfaction(100,this);
								dancer2.getFame().modifyFame(50);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(100+dancer2.getFinalValue(Sextype.VAGINAL)/5, this);
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.VAGINAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.vaginal.female.one", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, dancer2);
								getCustomers().get(a).addToSatisfaction(100, this);
							}
						}
					}

					else if(getCustomers().get(a).getPreferredSextype()==Sextype.ANAL && dancer2.getAllowedServices().isAllowed(Sextype.ANAL)){
						if(getCustomers().get(a).getGender()==Gender.MALE){
							if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.ROWDYRUMP)
									|| dancer2.getTraits().contains(Trait.AMBITOUSLOVER)
									|| dancer2.getTraits().contains(Trait.HORNY))){
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ANAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.anal.male.two", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, dancer2);
								getCustomers().get(a).addToSatisfaction(150,this);
								dancer2.getFame().modifyFame(50);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(100+dancer2.getFinalValue(Sextype.ANAL)/5, this);
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ANAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.anal.male.one", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, dancer2);
								getCustomers().get(a).addToSatisfaction(100, this);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(15, this);
								}
							}
						}
						if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
							Object arg2[] = {2+dancer2.getFinalValue(Sextype.ANAL)/50, getCustomers().get(Util.getInt(0, getCustomers().size())).getName()};
							if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.KINKY)
									|| dancer2.getTraits().contains(Trait.SUBMISSIVE)
									|| dancer2.getTraits().contains(Trait.UNINHIBITED))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.ANAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.anal.female.two", dancer2,arg2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, dancer2);
								dancer2.getFame().modifyFame(50);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(80+dancer2.getFinalValue(Sextype.ANAL)/2, this);
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ANAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.anal.female.one", dancer2,arg2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, dancer2);
								getCustomers().get(a).addToSatisfaction(100, this);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(25+dancer2.getFinalValue(Sextype.ANAL)/4, this);
								}
							}
						}
					}

					else if(getCustomers().get(a).getPreferredSextype()==Sextype.ORAL && dancer2.getAllowedServices().isAllowed(Sextype.ORAL)){
						if(getCustomers().get(a).getGender()==Gender.MALE){
							if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.CUMSLUT)
									|| dancer2.getTraits().contains(Trait.SENSUALTONGUE)
									|| dancer2.getTraits().contains(Trait.ABSORPTION))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.ORAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.oral.male.two", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, dancer2);
								getCustomers().get(a).addToSatisfaction(120,this);
								dancer2.getFame().modifyFame(50);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(80+dancer2.getFinalValue(Sextype.ORAL)/2, this);
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ORAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.oral.male.one", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, dancer2);
								getCustomers().get(a).addToSatisfaction(100, this);
							}
						}
						if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
							if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.SENSUALTONGUE)
									|| dancer2.getTraits().contains(Trait.OPENMINDED)
									|| dancer2.getTraits().contains(Trait.HORNY))){
								this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.ORAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.oral.female.two", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, dancer2);
								getCustomers().get(a).addToSatisfaction(75,this);
								dancer2.getFame().modifyFame(50);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(80+dancer2.getFinalValue(Sextype.FOREPLAY)/2, this);
								}
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.ORAL, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.oral.female.one", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, dancer2);
								getCustomers().get(a).addToSatisfaction(100, this);
							}
						}
					}
					else if(getCustomers().get(a).getPreferredSextype()==Sextype.TITFUCK && dancer2.getAllowedServices().isAllowed(Sextype.TITFUCK) && dancer2.getGender()==Gender.FEMALE){
						if(getCustomers().get(a).getGender()==Gender.MALE){
							if(dancer2.getTraits().contains(Trait.SMALLBOOBS)){
								if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.CUMSLUT)
										|| dancer2.getTraits().contains(Trait.WILD)
										|| dancer2.getTraits().contains(Trait.OILY))){
									this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.TITFUCK, dancer2));
									message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.two.small", dancer2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, dancer2);
									getCustomers().get(a).addToSatisfaction(75,this);
									for(int cust=0; cust<getCustomers().size(); cust++){
										getCustomers().get(cust).addToSatisfaction(dancer2.getFinalValue(Sextype.TITFUCK)/2, this);
									}
									dancer2.getFame().modifyFame(50);
								}
								else{
									this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, dancer2));
									message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.one.small", dancer2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, dancer2);
									getCustomers().get(a).addToSatisfaction(100, this);
								}
							}
							else{
								if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.WENCH)
										|| dancer2.getTraits().contains(Trait.WENCH)
										|| dancer2.getTraits().contains(Trait.COMETOMOMMY))){
									this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.TITFUCK, dancer2));
									message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.two.big", dancer2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, dancer2);
									getCustomers().get(a).addToSatisfaction(75,this);
									for(int cust=0; cust<getCustomers().size(); cust++){
										getCustomers().get(cust).addToSatisfaction(80+dancer2.getFinalValue(Sextype.FOREPLAY), this);
									}
									dancer2.getFame().modifyFame(50);
								}
								else{
									this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, dancer2));
									message+="\n" + TextUtil.t("STRIP.extras.titfuck.male.one.big", dancer2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TITFUCK, dancer2);
									getCustomers().get(a).addToSatisfaction(100, this);
									for(int cust=0; cust<getCustomers().size(); cust++){
										getCustomers().get(cust).addToSatisfaction(Util.getInt(-25, 25), this);
									}
								}
							}
						}
						if(getCustomers().get(a).getGender()==Gender.FEMALE || getCustomers().get(a).getGender()==Gender.FUTA){
							if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.EXHIBITIONIST)
									|| dancer2.getTraits().contains(Trait.UNINHIBITED)
									|| dancer2.getTraits().contains(Trait.TOUCHYFEELY))){
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.titfuck.female.two", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, dancer2);
								getCustomers().get(a).addToSatisfaction(75,this);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(80+dancer2.getFinalValue(Sextype.FOREPLAY), this);
								}
								dancer2.getFame().modifyFame(50);
							}
							else{
								this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.TITFUCK, dancer2));
								message+="\n" + TextUtil.t("STRIP.extras.titfuck.female.one", dancer2);
								image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LESBIAN, dancer2);
								getCustomers().get(a).addToSatisfaction(100, this);
								for(int cust=0; cust<getCustomers().size(); cust++){
									getCustomers().get(cust).addToSatisfaction(25+dancer2.getFinalValue(Sextype.FOREPLAY), this);
								}
							}
						}
					}

					else if(Util.getInt(0, 10)>4 || getCustomers().size()<8){
						if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.SENSITIVE)
								|| dancer2.getTraits().contains(Trait.NICEBODY)
								|| dancer2.getTraits().contains(Trait.TOUCHYFEELY))){
							this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.FOREPLAY, dancer2));
							message+="\n" + TextUtil.t("STRIP.extras.lapdance.two", dancer2,getCustomers().get(a));
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, dancer2);
							dancer2.getFame().modifyFame(50);
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(25+(dancer2.getFinalValue(Sextype.FOREPLAY)+dancer2.getFinalValue(SpecializationAttribute.STRIP))/2, this);
							}
						}
						else{
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.FOREPLAY, dancer2));
							message+="\n" + TextUtil.t("STRIP.extras.lapdance.one", dancer2,getCustomers().get(a));
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.LAPDANCE, dancer2);
							dancer2.getFame().modifyFame(20);
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(25+(dancer2.getFinalValue(Sextype.FOREPLAY)+dancer2.getFinalValue(SpecializationAttribute.STRIP))/4, this);
							}
						}

					}

					else{//group
						if(Util.getInt(0, 10)>5 && (dancer2.getTraits().contains(Trait.GANGBANGQUEEN)
								|| dancer2.getTraits().contains(Trait.INSATIABLE)
								|| dancer2.getTraits().contains(Trait.MULTIFACETED))){
							this.getAttributeModifications().add(new AttributeModification(2.5f,Sextype.GROUP, dancer2));
							message+="\n" + TextUtil.t("STRIP.extras.group.two", dancer2,getCustomers().get(a));
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, dancer2);
							this.getHouse().modDirt(70);
							dancer2.getFame().modifyFame(450);
							dancer2.addCondition(new Buff.RoughenedUp());
							for(int cust=0; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(25+dancer2.getFinalValue(Sextype.GROUP)*2, this);
							}
						}
						else{
							this.getAttributeModifications().add(new AttributeModification(1.5f,Sextype.GROUP, dancer2));
							message+="\n" + TextUtil.t("STRIP.extras.group.one", dancer2,getCustomers().get(a));
							image = ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, dancer2);
							dancer2.getFame().modifyFame(100);
							for(int cust=0; cust<8; cust++){
								getCustomers().get(cust).addToSatisfaction(25+dancer2.getFinalValue(Sextype.GROUP), this);
							}
							for(int cust=8; cust<getCustomers().size(); cust++){
								getCustomers().get(cust).addToSatisfaction(-50, this);
							}
						}

					}

					if(message!=null){getMessages().add(new MessageData(message, image, dancer2.getBackground()));}
				}

			}

			for(Charakter bartender2 : bartenders){
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
								if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>7 || bartender2.getTraits().contains(Trait.WENCH)){
									this.getAttributeModifications().add(new AttributeModification(0.05f,BaseAttributeTypes.OBEDIENCE, bartender2));
									message+="\n" + TextUtil.t("barevent.flip.win", bartender2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
									getCustomers().get(Util.getInt(1, getCustomers().size()-1)).addToSatisfaction(75,this);

								}
								else{
									this.getAttributeModifications().add(new AttributeModification(-0.05f,BaseAttributeTypes.OBEDIENCE, bartender2));
									message+="\n" + TextUtil.t("barevent.flip.lose", bartender2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
									getCustomers().get(Util.getInt(1, getCustomers().size()-1)).addToSatisfaction(-100, this);
								}
							}
							if(i==12){
								//grope
								int a=Util.getInt(0, getCustomers().size()-1);
								Object arg[] = {getCustomers().get(a).getName()};
								message=TextUtil.t("barevent.grope", bartender2,arg);
								if(bartender2.getFinalValue(BaseAttributeTypes.OBEDIENCE)>8 || bartender2.getTraits().contains(Trait.WENCH)){
									this.getAttributeModifications().add(new AttributeModification(0.07f,BaseAttributeTypes.OBEDIENCE, bartender2));
									message+="\n" + TextUtil.t("barevent.grope.win", bartender2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.TOUCHING, bartender2);
									bartender2.getFame().modifyFame(15);
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
								if(bartender2.getFinalValue(SpecializationAttribute.BARTENDING)>Util.getInt(20, 70) || bartender2.getTraits().contains(Trait.OUTGOING)){
									this.getAttributeModifications().add(new AttributeModification(0.08f,BaseAttributeTypes.CHARISMA, bartender2));
									message+="\n" + TextUtil.t("barevent.look.win", bartender2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, bartender2);
									for (Customer customer : getCustomers()) {
										customer.addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA))/2, this);
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
									getCustomers().get(Util.getInt(0, getCustomers().size())).addToSatisfaction((bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(SpecializationAttribute.SEDUCTION)/5)/2, this);
									getCustomers().get(Util.getInt(0, getCustomers().size())).payFixed(200);
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
										}
										else{
											getCustomers().get(a).addToSatisfaction(-50, this);
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
									message+="\n" + TextUtil.t("barevent.ninjablowjob.gold",arg);
									if(cust.getGender()==Gender.MALE){
										image = ImageUtil.getInstance().getImageDataByTag(ImageTag.ORAL, bartender2);
									}
									else{
										image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CUNNILINGUS, bartender2);
									}
									cust.addToSatisfaction(bartender2.getFinalValue(Sextype.ORAL), this);
									cust.payFixed(bartender2.getFinalValue(Sextype.ORAL)*2);
									modifyIncome(bartender2.getFinalValue(Sextype.ORAL)*2);
								}
								else{
									message+="\n" + TextUtil.t("barevent.ninjablowjob.lose", bartender2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
									cust.addToSatisfaction(-200, this);
									cust.payFixed(100);
									modifyIncome(100);
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
								}
								else{
									message+="\n" + TextUtil.t("barevent.ninjablowjob.lose", bartender2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
									cust.addToSatisfaction(-200, this);
									cust.payFixed(100);
									modifyIncome(100);
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
									}
									for(int z=getCustomers().size()/10; z<getCustomers().size(); z++){
										getCustomers().get(z).addToSatisfaction(15+(bartender2.getFinalValue(BaseAttributeTypes.CHARISMA)+bartender2.getFinalValue(Sextype.GROUP)/8)/4, this);
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
									}
								}
								else{
									message+="\n" + TextUtil.t("barevent.group.lose", bartender2);
									image = ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, bartender2);
									for(int a=0; a<getCustomers().size()/15; a++){
										getCustomers().get(a).addToSatisfaction(-10, this);
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
	}

	@Override
	public MessageData getBaseMessage() {
		String messageText = TextUtil.t("cabaret.basic", getCharacters());
		messageText += "\n";
		this.messageData = new MessageData(messageText, null, getBackground());
		for (Charakter character : getCharacters()) {
			if (dancers.contains(character)) {
				this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.DANCE, character));
			}
			else {
				this.messageData.addImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.BARTEND, character));
			}
		}
		return this.messageData;
	}

	@Override
	public List<ModificationData> getStatModifications() {
		List<ModificationData> modifications = new ArrayList<ModificationData>();
		for (Charakter dancer : dancers) {
			modifications.add(new ModificationData(TargetType.SINGLE, dancer, -30, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.SINGLE, dancer, 1.0f, SpecializationAttribute.STRIP));
			modifications.add(new ModificationData(TargetType.SINGLE, dancer, 0.02f, BaseAttributeTypes.STAMINA));
			modifications.add(new ModificationData(TargetType.SINGLE, dancer, 0.02f, BaseAttributeTypes.CHARISMA));
		}
		for (Charakter bartender : bartenders) {
			modifications.add(new ModificationData(TargetType.SINGLE, bartender, -15, EssentialAttributes.ENERGY));
			modifications.add(new ModificationData(TargetType.SINGLE, bartender, 1.0f, SpecializationAttribute.BARTENDING));
			modifications.add(new ModificationData(TargetType.SINGLE, bartender, 0.02f, BaseAttributeTypes.INTELLIGENCE));
			modifications.add(new ModificationData(TargetType.SINGLE, bartender, 0.02f, BaseAttributeTypes.CHARISMA));
		}
		modifications.add(new ModificationData(TargetType.TRAINER, -0.3f, BaseAttributeTypes.COMMAND));
		return modifications;
	}

	@Override
	public int getAppeal() {
		int appeal = 0;
		for (Charakter dancer : dancers) {
			appeal += (dancer.getCharisma() + dancer.getFinalValue(SpecializationAttribute.STRIP) / 4) / 6;
		}
		for (Charakter bartender : bartenders) {
			appeal += (bartender.getCharisma() + bartender.getFinalValue(SpecializationAttribute.STRIP) / 4) / 6;
		}
		return appeal;
	}

	@Override
	public int getMaxAttendees() {
		int amount = 0;
		for (Charakter bartender : getCharacters()) {
			amount += 10 + bartender.getFinalValue(SpecializationAttribute.BARTENDING) / 10;
		}
		if(amount>=40*getCharacters().size()){
			amount=40*getCharacters().size();
		}
		return amount;
	}

}
