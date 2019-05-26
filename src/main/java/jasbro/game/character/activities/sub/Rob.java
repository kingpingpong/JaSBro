package jasbro.game.character.activities.sub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Condition;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.AttributeModification;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Battle;
import jasbro.game.character.battle.Unit;
import jasbro.game.character.conditions.Buff;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.items.ItemType;
import jasbro.game.items.Inventory.ItemData;
import jasbro.game.world.Time;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.texts.TextUtil;

public class Rob extends RunningActivity {
	private enum EscapeRoutes{
		STEAL,
		CAUGHTANDFIGHT,
		RUN,
		FUCKANDFLEE,SEDUCEANDROB, FUCKANDROB, SEDUCEANDFLEE,
		CALLGUARDS
	}
	private MessageData messageData;
	private Map<Charakter, EscapeRoutes> characterAction=new HashMap<Charakter, EscapeRoutes>();

	@Override
	public void perform() {
		Charakter character=getCharacter();
		List<EscapeRoutes> actions = new ArrayList<EscapeRoutes>();
		int stealChance = 25+character.getStealChance();
		int stealAmount = 15+character.getStealAmountModifier();
		int stealItemChance = 10+character.getStealItemChance();
		int targetLevel=character.getFinalValue(SpecializationAttribute.PICKPOCKETING)+character.getFinalValue(SpecializationAttribute.AGILITY)+character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)+Util.getInt(-20, 20);
		targetLevel*=Util.getInt(40, 110)/100;
		int failureChance=(int) (100-(character.getFinalValue(SpecializationAttribute.AGILITY)/1.5)-character.getFinalValue(BaseAttributeTypes.INTELLIGENCE)/2);

		for(Condition condition: character.getConditions()){
			if(condition instanceof Buff.Watched)
				failureChance*=115/100;
		}
		
		Customer target = null;
		if(Jasbro.getInstance().getData().getTime()!=Time.NIGHT){
			//Rob people
			failureChance/=4;
			target=generateTarget(targetLevel);
			Object argument[]={target.getName()};
			messageData.addToMessage("\n"+TextUtil.t("rob.target.day", character, target, argument));
		}
		else{
			//Rob places
			stealChance*=3/2;
			stealAmount*=3/2;
			stealItemChance*=3/2;


			if(character.getTraits().contains(Trait.PHANTOMTHIEF)){
				stealChance*=2;
				stealAmount*=2;
				stealItemChance*=2;
				targetLevel*=2;
			}
			target=generateTarget(targetLevel+40);
			Object argument[]={target.getName()};
			messageData.addToMessage("\n"+TextUtil.t("rob.target.night", character, target, argument));
		}

		target.setMaxHitpoints(target.getMaxHitpoints()+targetLevel);
		target.setHitpoints(target.getMaxHitpoints());
		if(target!=null){
			failureChance+=target.getInitialSatisfaction()/5;
			if(Util.getInt(0, 100)<failureChance){
				//get caught
				if(Util.getInt(0, 100)<10)
					character.addCondition(new Buff.Watched(character.getFinalValue(SpecializationAttribute.PICKPOCKETING)));
				messageData.addToMessage("\n"+TextUtil.t("rob.noticed", character, target));
				if(character.getTraits().contains(Trait.CLEVER))
					actions.add(EscapeRoutes.RUN);
				if(character.getTraits().contains(Trait.FIT))
					actions.add(EscapeRoutes.RUN);
				if(character.getTraits().contains(Trait.STUPID))
					actions.add(EscapeRoutes.CAUGHTANDFIGHT);
				actions.add(EscapeRoutes.CAUGHTANDFIGHT);
				actions.add(EscapeRoutes.RUN);
				actions.add(EscapeRoutes.RUN);
				if(target.getType()!=CustomerType.BUM)
					actions.add(EscapeRoutes.CALLGUARDS);
				if(target.getType()!=CustomerType.BUM)
					actions.add(EscapeRoutes.CALLGUARDS);
				if(character.getTraits().contains(Trait.NYMPHO) && character.getGender()!=target.getGender()){
					actions.add(EscapeRoutes.SEDUCEANDROB);
					actions.add(EscapeRoutes.SEDUCEANDFLEE);
					actions.add(EscapeRoutes.FUCKANDFLEE);
					actions.add(EscapeRoutes.FUCKANDROB);
				}
				if(character.getTraits().contains(Trait.SLUT) && character.getGender()!=target.getGender()){
					actions.add(EscapeRoutes.SEDUCEANDROB);
					actions.add(EscapeRoutes.SEDUCEANDFLEE);
					actions.add(EscapeRoutes.FUCKANDFLEE);
					actions.add(EscapeRoutes.FUCKANDROB);
				}
				if(character.getTraits().contains(Trait.LIAISONSDANGEREUSES) && character.getGender()!=target.getGender()){
					actions.add(EscapeRoutes.SEDUCEANDROB);
					actions.add(EscapeRoutes.SEDUCEANDFLEE);
					actions.add(EscapeRoutes.FUCKANDFLEE);
					actions.add(EscapeRoutes.FUCKANDROB);
					actions.add(EscapeRoutes.SEDUCEANDROB);
					actions.add(EscapeRoutes.SEDUCEANDFLEE);
					actions.add(EscapeRoutes.FUCKANDFLEE);
					actions.add(EscapeRoutes.FUCKANDROB);
				}
				characterAction.put(character, actions.get(Util.getInt(0, actions.size())));

			}
			else{
				//steal
				characterAction.put(character, EscapeRoutes.STEAL);
			}

			if(characterAction.get(character)==EscapeRoutes.CAUGHTANDFIGHT){
				messageData.addToMessage("\n"+TextUtil.t("rob.noticed.fight", character, target));
				this.getAttributeModifications().add(new AttributeModification(1.07f,SpecializationAttribute.VETERAN, character));
				//fight
				Unit fighter1 = character;
				Unit fighter2 = target;
				int i = 0;
				int startHitPoints1 = fighter1.getHitpoints();
				int startHitPoints2 = fighter2.getHitpoints();
				Battle battle = new Battle(fighter1, fighter2);
				do {
					battle.doRound();
					i++;
				} while (i < 1000 && fighter1.getHitpoints() > startHitPoints1 - 30 && fighter2.getHitpoints() > 10);
				getMessages().get(0).addToMessage("\n\n" + battle.getCombatText());
				if(character.getFinalValue(SpecializationAttribute.MAGIC)>10)
					this.getAttributeModifications().add(new AttributeModification(0.28f,SpecializationAttribute.MAGIC, character));

				if (fighter1.getHitpoints()/startHitPoints1 > fighter2.getHitpoints()/startHitPoints2) {
					messageData.addToMessage(TextUtil.t("rob.fight.run", character, target));
					if(Util.getInt(0, 100)<50){
						//win and escape
						characterAction.put(character, EscapeRoutes.RUN);
					}
					else {
						//winandrob
						characterAction.put(character, EscapeRoutes.STEAL);			
					}
				} 
				else 
				{
					messageData.addToMessage("\n"+TextUtil.t("rob.fight.defeat", character, target));
					if(Util.getInt(0, 100)<50){
						//call guards
						characterAction.put(character, EscapeRoutes.CALLGUARDS);
					}
					else if(Util.getInt(0, 100)<50){
						//asskick
						messageData.addToMessage("\n"+TextUtil.t("rob.defeat.meh", character, target));
						messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.HURT, getCharacter()));
					}
					else{
						//rape
						messageData.addToMessage("\n"+TextUtil.t("rob.defeat.rape", character, target));
						messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.ANAL, getCharacter()));
						this.getAttributeModifications().add(new AttributeModification(0.28f,Sextype.ANAL, character));

					}
				}
				if(character.getTraits().contains(Trait.FIRSTAID)){
					character.getAttribute(EssentialAttributes.HEALTH).addToValue(10f);
					messageData.addToMessage("\n"+TextUtil.t("nurse.selfheal", character));
					this.getAttributeModifications().add(new AttributeModification(0.28f,SpecializationAttribute.MEDICALKNOWLEDGE, character));
				}
			}


			if(characterAction.get(character)==EscapeRoutes.CALLGUARDS){
				messageData.addToMessage("\n"+TextUtil.t("rob.callguards", character, target));
				if((character.getTraits().contains(Trait.SLUT) || character.getTraits().contains(Trait.LIAISONSDANGEREUSES) || character.getTraits().contains(Trait.NYMPHO)) && Util.getInt(0, 110)<character.getFinalValue(SpecializationAttribute.SEDUCTION) ){
					//fuckguards
					messageData.addToMessage("\n"+TextUtil.t("rob.fuckguards", character, target));
					messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, getCharacter()));
					this.getAttributeModifications().add(new AttributeModification(0.5f,Sextype.GROUP, character));

				}
				else if (Util.getInt(0, 100)<30){

					//fine
					messageData.addToMessage("\n"+TextUtil.t("rob.police.pay", character, target));
					messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.CLOTHED, getCharacter()));
					setIncome(-1500);
				}
				else {
					//rape
					messageData.addToMessage("\n"+TextUtil.t("rob.defeat.police.lesson", character, target));
					messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.GROUP, getCharacter()));
					this.getAttributeModifications().add(new AttributeModification(0.5f,Sextype.GROUP, character));
				}
			}
			if(characterAction.get(character)==EscapeRoutes.FUCKANDROB){
				messageData.addToMessage("\n"+TextUtil.t("rob.fuckandrob", character, target));
				messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, getCharacter()));
				this.getAttributeModifications().add(new AttributeModification(0.48f,Sextype.VAGINAL, character));
				stealItemChance+=5;
				stealChance+=5;
				characterAction.put(character, EscapeRoutes.STEAL);
			}
			if(characterAction.get(character)==EscapeRoutes.SEDUCEANDROB){
				messageData.addToMessage("\n"+TextUtil.t("rob.seduceandrob", character, target));
				messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, getCharacter()));
				this.getAttributeModifications().add(new AttributeModification(0.28f,SpecializationAttribute.SEDUCTION, character));
				this.getAttributeModifications().add(new AttributeModification(0.28f,SpecializationAttribute.STRIP, character));
				stealItemChance+=5;
				stealChance+=5;
				characterAction.put(character, EscapeRoutes.STEAL);
			}
			if(characterAction.get(character)==EscapeRoutes.SEDUCEANDFLEE){
				messageData.addToMessage("\n"+TextUtil.t("rob.seduceandflee", character, target));
				messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.NAKED, getCharacter()));
				this.getAttributeModifications().add(new AttributeModification(0.28f,SpecializationAttribute.SEDUCTION, character));
				this.getAttributeModifications().add(new AttributeModification(0.28f,SpecializationAttribute.STRIP, character));
				characterAction.put(character, EscapeRoutes.RUN);
			}
			if(characterAction.get(character)==EscapeRoutes.FUCKANDFLEE){
				messageData.addToMessage(TextUtil.t("rob.fuckandflee", character, target));
				messageData.setImage2(ImageUtil.getInstance().getImageDataByTag(ImageTag.VAGINAL, getCharacter()));
				this.getAttributeModifications().add(new AttributeModification(0.48f,Sextype.VAGINAL, character));
				characterAction.put(character, EscapeRoutes.RUN);
			}

			if(characterAction.get(character)==EscapeRoutes.STEAL){
				int stolenAmount=0;
				ItemData stolenItem = null;
				if (Util.getInt(0, 100) < stealItemChance) {

					stolenItem = target.getItem();
					if (stolenItem != null) {
						this.getAttributeModifications().add(new AttributeModification(1.07f,EssentialAttributes.MOTIVATION, character));
						if (character.getTraits().contains(Trait.RESELLER) && stolenItem.getItem().getType() != ItemType.UNLOCK) 
							Jasbro.getInstance().getData().earnMoney(stolenItem.getItem().getValue() / 2, stolenItem);
						else 
							Jasbro.getInstance().getData().getInventory().addItems(stolenItem.getItem(), stolenItem.getAmount());		
						Object arg2[]={stolenItem.getItem().getName(), stolenItem.getAmount()};
						messageData.addToMessage("\n"+TextUtil.t("rob.steal.item", character, target, arg2));
					}					
				}
				if(Util.getInt(0, 100)<stealChance){
					this.getAttributeModifications().add(new AttributeModification(1.07f,EssentialAttributes.MOTIVATION, character));

					stolenAmount += target.payFixed((int) (stealAmount * 10 + target.getMoney() * stealAmount / 100f));
					Object arg[]={stolenAmount};
					Jasbro.getInstance().getData().earnMoney(stolenAmount, this);

					messageData.addToMessage("\n"+TextUtil.t("rob.steal.money", character, target, arg));

				}
				if(stolenAmount==0 && stolenItem==null)
					messageData.addToMessage("\n"+TextUtil.t("rob.steal.nothing", character, target));
			}

			if(characterAction.get(character)==EscapeRoutes.RUN){
				messageData.addToMessage("\n"+TextUtil.t("rob.noticed.run", character, target));
			}







		}
	}





	@Override
	public MessageData getBaseMessage() {
		Charakter character = getCharacters().get(0);
		String message = TextUtil.t("rob.basic", character);
		this.messageData = new MessageData(message,null, getBackground());
		messageData.setImage(ImageUtil.getInstance().getImageDataByTag(ImageTag.STANDARD, getCharacter()));


		return this.messageData;
	}

	@Override
	public List<ModificationData> getStatModifications() {

		List<ModificationData> modifications = new ArrayList<RunningActivity.ModificationData>();
		modifications.add(new ModificationData(TargetType.ALL, -0.2f, EssentialAttributes.MOTIVATION));
		modifications.add(new ModificationData(TargetType.ALL, -25.0f, EssentialAttributes.ENERGY));
		modifications.add(new ModificationData(TargetType.ALL, 0.5f, SpecializationAttribute.AGILITY));
		modifications.add(new ModificationData(TargetType.ALL, 0.5f, SpecializationAttribute.PICKPOCKETING));


		return modifications;
	}
	private Customer generateTarget(int skill){
		SpawnData spawnData = new SpawnData();
		if(skill<20)
			return spawnData.createCustomer(CustomerType.BUM);
		else if(skill<40)
			return spawnData.createCustomer(CustomerType.PEASANT); 
		else if(skill<60)
			return spawnData.createCustomer(CustomerType.MERCHANT); 
		else if(skill<80)
			return spawnData.createCustomer(CustomerType.BUSINESSMAN); 
		else if(skill<100)
			return spawnData.createCustomer(CustomerType.MINORNOBLE); 
		else if(skill<150)
			return spawnData.createCustomer(CustomerType.LORD); 
		else if(skill<190)
			return spawnData.createCustomer(CustomerType.CELEBRITY); 

		return spawnData.createCustomer(CustomerType.BUM);
	}
}