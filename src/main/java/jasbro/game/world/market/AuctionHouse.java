/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameObject;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Azrael
 */
public class AuctionHouse extends GameObject implements CentralEventlistener {
	private List<Charakter> slaves;
	private List<Charakter> trainers;
	private Charakter slave;
	private static String slaveJob;
	
	public AuctionHouse() {
		Jasbro.getInstance().addCentralListener(this);
		Jasbro.getInstance().getData().setAuctionHouse(this);
		slaves = getSlaves();
		trainers = getTrainers();
	}
	
	@Override
	public void handleCentralEvent(MyEvent e) {
		if (e.getType() == EventType.NEXTDAY) {
			slaves = null;
			trainers = null;
			getSlaves();
			getTrainers();
		}
	}
	
	public List<Charakter> getSlaves() {
		if (slaves == null) {
			slaves = new ArrayList<Charakter>();
			int amountSlaves = 4;
			if(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.BENEFACTORSLAVEMARKET))
				amountSlaves=8;
			for (int i = 0; i < amountSlaves; i++) {
				
				slave = Jasbro.getInstance().generateBasicSlave();
				
				if(slave.getTraits().contains(Trait.UNSELLABLE)){
					i--;
				}
				else{ 
					int day = Jasbro.getInstance().getData().getDay()+Util.getInt(0, 30);
					day/=3;
					day++;
					int random =  Util.getInt(0, 2);
					int specialization = day;
					
					if(specialization > 75){
						specialization = 75;
					}
					
					int basevalue = 30;
					if (day > 100 && day < 200)
						basevalue = 60;
					else if (day >= 200)
						basevalue = 90;
					
					int specvalue = 30;
					if (day > 100 && day < 200)
						specvalue = 60;
					else if (day >= 200)
						specvalue = 90;
					
					slave.getAttribute(BaseAttributeTypes.CHARISMA).setMaxValue(basevalue);
					slave.getAttribute(BaseAttributeTypes.STRENGTH).setMaxValue(basevalue);
					slave.getAttribute(BaseAttributeTypes.OBEDIENCE).setMaxValue(basevalue);
					slave.getAttribute(BaseAttributeTypes.STAMINA).setMaxValue(basevalue);
					slave.getAttribute(BaseAttributeTypes.INTELLIGENCE).setMaxValue(basevalue);
					
					slave.getAttribute(Sextype.VAGINAL).setMaxValue(specvalue);
					slave.getAttribute(Sextype.ANAL).setMaxValue(specvalue);
					slave.getAttribute(Sextype.ORAL).setMaxValue(specvalue);
					slave.getAttribute(Sextype.FOREPLAY).setMaxValue(specvalue);
					slave.getAttribute(Sextype.TITFUCK).setMaxValue(specvalue);
					
					if(slave.getSpecializations().contains(SpecializationType.WHORE)){	
						
						slave.getAttribute(SpecializationAttribute.SEDUCTION).setMaxValue(specvalue);
						slave.getAttribute(SpecializationAttribute.SEDUCTION).addToValue(specialization);
						
						if(random == 1){ //The classy one					
							changeSlaveBase(slave, day, 2, 5, 5, 5, 7);
							slave.setSlaveJob("ClassyWhore");
			
							slave.getAttribute(Sextype.VAGINAL).addToValue(specialization*1.2f);
							slave.getAttribute(Sextype.ANAL).addToValue(specialization*1.2f);
							slave.getAttribute(Sextype.ORAL).addToValue(specialization*1.2f);
							slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization*1.2f);
							slave.getAttribute(Sextype.TITFUCK).addToValue(specialization*1.2f);
							if(slave.getTraits().contains(Trait.NATURAL)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/5);
								slave.getAttribute(Sextype.TITFUCK).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.FRIGID)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
								slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSITIVE)){
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.NUMB)){
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.BITER)){
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSUALTONGUE)){
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.AMBITOUSLOVER)){
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.DEADFISH)){
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
							}

						}
						else{ //The cum bucket						
							changeSlaveBase(slave, day, 3, 5, 3, 3, 9);
							slave.setSlaveJob("CumBucket");
							
							slave.getAttribute(Sextype.VAGINAL).addToValue(specialization*1.4f);
							slave.getAttribute(Sextype.ANAL).addToValue(specialization*1.4f);
							slave.getAttribute(Sextype.ORAL).addToValue(specialization*1.2f);
							slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization*0.8f);
							slave.getAttribute(Sextype.TITFUCK).addToValue(specialization*0.8f);
							
							if(slave.getTraits().contains(Trait.NATURAL)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/4);
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/4);
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/4);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/4);
								slave.getAttribute(Sextype.TITFUCK).addToValue(specialization/4);
							}
							if(slave.getTraits().contains(Trait.FRIGID)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
								slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSITIVE)){
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/4);
							}
							if(slave.getTraits().contains(Trait.NUMB)){
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.BITER)){
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSUALTONGUE)){
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/4);
							}
							if(slave.getTraits().contains(Trait.AMBITOUSLOVER)){
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/4);
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/4);
							}
							if(slave.getTraits().contains(Trait.DEADFISH)){
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
							}
						}					
					}
					if(slave.getSpecializations().contains(SpecializationType.BARTENDER)){
						
						slave.getAttribute(SpecializationAttribute.BARTENDING).setMaxValue(specvalue);
						slave.getAttribute(SpecializationAttribute.BARTENDING).addToValue(specialization);
						
						if(random == 1){ //The one who serves drunkards					
							changeSlaveBase(slave, day, 3, 5, 5, 6, 6);
							slave.setSlaveJob("ServesDrunkards");

							slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/2);
							slave.getAttribute(Sextype.ANAL).addToValue(specialization/2);
							slave.getAttribute(Sextype.ORAL).addToValue(specialization/2);
							if(slave.getTraits().contains(Trait.NATURAL)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/8);
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/8);
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/8);

							}
							if(slave.getTraits().contains(Trait.FRIGID)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);

							}
							if(slave.getTraits().contains(Trait.BITER)){
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSUALTONGUE)){
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/8);
							}
							if(slave.getTraits().contains(Trait.AMBITOUSLOVER)){
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/8);
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/8);
							}
							if(slave.getTraits().contains(Trait.DEADFISH)){
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
							}
						}
						else{ //The classy one							
							changeSlaveBase(slave, day, 2, 5, 5, 7, 5);
							slave.setSlaveJob("ClassyBartender");
						}	
					}
					else if(slave.getSpecializations().contains(SpecializationType.DANCER)){
						
						slave.getAttribute(SpecializationAttribute.STRIP).setMaxValue(specvalue);
						slave.getAttribute(SpecializationAttribute.STRIP).addToValue(specialization);
						
						if(random == 1){ //The one who gives extras					
							changeSlaveBase(slave, day, 3, 5, 4, 4, 7);	
							slave.setSlaveJob("GivesExtras");
							
							slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/2);
							slave.getAttribute(Sextype.ANAL).addToValue(specialization/2);
							slave.getAttribute(Sextype.ORAL).addToValue(specialization/3);
							slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/2);
							slave.getAttribute(Sextype.TITFUCK).addToValue(specialization/2);
							if(slave.getTraits().contains(Trait.NATURAL)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/7);
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/7);
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/7);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/7);
								slave.getAttribute(Sextype.TITFUCK).addToValue(specialization/7);
							}
							if(slave.getTraits().contains(Trait.FRIGID)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
								slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSITIVE)){
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/7);
							}
							if(slave.getTraits().contains(Trait.NUMB)){
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.BITER)){
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSUALTONGUE)){
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/7);
							}
							if(slave.getTraits().contains(Trait.AMBITOUSLOVER)){
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/7);
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/7);
							}
							if(slave.getTraits().contains(Trait.DEADFISH)){
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
							}
						}
						else{ //The performer
							changeSlaveBase(slave, day, 2, 5, 6, 5, 6);	
							slave.setSlaveJob("Performer");
						}	
					}
					else if(slave.getSpecializations().contains(SpecializationType.FIGHTER)){
						
						slave.getAttribute(SpecializationAttribute.VETERAN).setMaxValue(specvalue);
						slave.getAttribute(SpecializationAttribute.VETERAN).addToValue(specialization);
						
						if(random == 1){ //The brawler						
							changeSlaveBase(slave, day, 7, 2, 8, 2, 8);
							slave.setSlaveJob("Brawler");
						}
						else{ //The mage					
							changeSlaveBase(slave, day, 7, 7, 7, 7, 2);
							slave.setSlaveJob("Mage");
						}	
					}
					else if(slave.getSpecializations().contains(SpecializationType.NURSE) || slave.getSpecializations().contains(SpecializationType.ALCHEMIST)){
						
						slave.getAttribute(SpecializationAttribute.MEDICALKNOWLEDGE).setMaxValue(specvalue);
						slave.getAttribute(SpecializationAttribute.MAGIC).setMaxValue(specvalue);
						slave.getAttribute(SpecializationAttribute.MEDICALKNOWLEDGE).addToValue(specialization);
						slave.getAttribute(SpecializationAttribute.MAGIC).addToValue(specialization);
						
						if(random == 1){//The bookworm					
							changeSlaveBase(slave, day, 5, 6, 4, 7, 2);
							slave.setSlaveJob("BookWorm");
						}
						else{ //The one who does what she's asked to
							
							changeSlaveBase(slave, day, 3, 6, 2, 6, 4);
							slave.setSlaveJob("DoesWhatShesAskedTo");
							
							slave.getAttribute(Sextype.VAGINAL).addToValue(specialization*1.1f);
							slave.getAttribute(Sextype.ANAL).addToValue(specialization*1.1f);
							slave.getAttribute(Sextype.ORAL).addToValue(specialization*1.1f);
							slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization*1.1f);
							slave.getAttribute(Sextype.TITFUCK).addToValue(specialization*1.1f);
							if(slave.getTraits().contains(Trait.NATURAL)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/6);
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/6);
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/6);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/6);
								slave.getAttribute(Sextype.TITFUCK).addToValue(specialization/6);
							}
							if(slave.getTraits().contains(Trait.FRIGID)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
								slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSITIVE)){
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/6);
							}
							if(slave.getTraits().contains(Trait.NUMB)){
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.BITER)){
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSUALTONGUE)){
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/6);
							}
							if(slave.getTraits().contains(Trait.AMBITOUSLOVER)){
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/6);
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/6);
							}
							if(slave.getTraits().contains(Trait.DEADFISH)){
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
							}
						}	
					}
					else if(slave.getSpecializations().contains(SpecializationType.KINKYSEX)){
						
						slave.getAttribute(Sextype.GROUP).setMaxValue(specvalue);
						slave.getAttribute(Sextype.MONSTER).setMaxValue(specvalue);
						
						if(random == 1){ //The one who mostly fucked monsters
							
							changeSlaveBase(slave, day, 5, 3, 1, 2, 9);
							slave.setSlaveJob("FuckedByMonsters");
							
							slave.getAttribute(Sextype.GROUP).addToValue(day/4);
							slave.getAttribute(Sextype.MONSTER).addToValue(day/2);
							
							slave.getAttribute(Sextype.VAGINAL).addToValue(specialization*1.5f);
							slave.getAttribute(Sextype.ANAL).addToValue(specialization*1.5f);
							slave.getAttribute(Sextype.ORAL).addToValue(specialization*1.5f);
							if(slave.getTraits().contains(Trait.NATURAL)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/5);
								slave.getAttribute(Sextype.TITFUCK).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.FRIGID)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
								slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.BITER)){
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSUALTONGUE)){
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.AMBITOUSLOVER)){
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.DEADFISH)){
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
							}
							
						}
						else{ //The one who fucked a LOT of people (At once)
							
							changeSlaveBase(slave, day, 4, 3, 2, 1, 4);
							slave.setSlaveJob("FuckedByGroup");
							
							slave.getAttribute(Sextype.GROUP).addToValue(day/2);
							slave.getAttribute(Sextype.MONSTER).addToValue(day/4);
							
							slave.getAttribute(Sextype.VAGINAL).addToValue(specialization*1.5f);
							slave.getAttribute(Sextype.ANAL).addToValue(specialization*1.5f);
							slave.getAttribute(Sextype.ORAL).addToValue(specialization*1.5f);
							slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization*0.8f);
							slave.getAttribute(Sextype.TITFUCK).addToValue(specialization*0.8f);
							if(slave.getTraits().contains(Trait.NATURAL)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/5);
								slave.getAttribute(Sextype.TITFUCK).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.FRIGID)){
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
								slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSITIVE)){
								slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.NUMB)){
								slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.BITER)){
								slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
							}
							if(slave.getTraits().contains(Trait.SENSUALTONGUE)){
								slave.getAttribute(Sextype.ORAL).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.AMBITOUSLOVER)){
								slave.getAttribute(Sextype.ANAL).addToValue(specialization/5);
								slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/5);
							}
							if(slave.getTraits().contains(Trait.DEADFISH)){
								slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
								slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
							}
						}	
					}
					else if(slave.getSpecializations().contains(SpecializationType.MAID)){
						
						slave.getAttribute(SpecializationAttribute.CLEANING).setMaxValue(specialization);
						slave.getAttribute(SpecializationAttribute.COOKING).setMaxValue(specialization);	
						slave.getAttribute(SpecializationAttribute.CLEANING).addToValue(specialization);
						slave.getAttribute(SpecializationAttribute.COOKING).addToValue(specialization);
						
						if(random == 1){//the cosette						
							changeSlaveBase(slave, day, 5, 3, 2, 5, 6);
							slave.setSlaveJob("Cosette");
						}
						else{//The head Maid					
							changeSlaveBase(slave, day, 3, 3, 4, 7, 3);
							slave.setSlaveJob("HeadMaid");
						}	
					}
					else if(slave.getSpecializations().size()<3 || slave.getSpecializations().contains(SpecializationType.FURRY) || slave.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)){
						
						changeSlaveBase(slave, day, 5, 5, 5, 5, 5);
						slave.setSlaveJob("None");
						
						slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/1.5f);
						slave.getAttribute(Sextype.ANAL).addToValue(specialization/1.5f);
						slave.getAttribute(Sextype.ORAL).addToValue(specialization/1.5f);
						slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/1.5f);
						slave.getAttribute(Sextype.TITFUCK).addToValue(specialization/1.5f);
						if(slave.getTraits().contains(Trait.NATURAL)){
							slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/6.5F);
							slave.getAttribute(Sextype.ANAL).addToValue(specialization/6.5F);
							slave.getAttribute(Sextype.ORAL).addToValue(specialization/6.5F);
							slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/6.5F);
							slave.getAttribute(Sextype.TITFUCK).addToValue(specialization/6.5F);
						}
						if(slave.getTraits().contains(Trait.FRIGID)){
							slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
							slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
							slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
							slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
							slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization/5);
						}
						if(slave.getTraits().contains(Trait.SENSITIVE)){
							slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization/6.5F);
						}
						if(slave.getTraits().contains(Trait.NUMB)){
							slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization/5);
						}
						if(slave.getTraits().contains(Trait.BITER)){
							slave.getAttribute(Sextype.ORAL).addToValue(-specialization/5);
						}
						if(slave.getTraits().contains(Trait.SENSUALTONGUE)){
							slave.getAttribute(Sextype.ORAL).addToValue(specialization/6.5F);
						}
						if(slave.getTraits().contains(Trait.AMBITOUSLOVER)){
							slave.getAttribute(Sextype.ANAL).addToValue(specialization/6.5F);
							slave.getAttribute(Sextype.VAGINAL).addToValue(specialization/6.5F);
						}
						if(slave.getTraits().contains(Trait.DEADFISH)){
							slave.getAttribute(Sextype.ANAL).addToValue(-specialization/5);
							slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization/5);
						}
					}
					slaves.add(slave);				
				}				
			}
		}
		return slaves;
	}
	
	public void setSlaves(List<Charakter> slaves) {
		this.slaves = slaves;
	}
	
	public List<Charakter> getTrainers() {
		if (trainers == null) {
			trainers = new ArrayList<Charakter>();
			int amountTrainers = Math.min(10, Math.max(3, 
					((int) Math.sqrt(Jasbro.getInstance().getData().getProtagonist().getFame().getFame()) / 50)));
			for (int i = 0; i < amountTrainers; i++) {
				trainers.add(Jasbro.getInstance().generateBasicTrainer());
			}
		}
		return trainers;
	}
	
	public void setTrainers(List<Charakter> trainers) {
		this.trainers = trainers;
	}
	
	private void changeSlaveBase(Charakter slave, int day, int charisma, int strength, int obedience, int stamina, int intelligence) {
		if(slave.getTraits().contains(Trait.UGLY))
			charisma+=1;
		if(slave.getTraits().contains(Trait.LOVELY))
			charisma-=1;
		if(slave.getTraits().contains(Trait.OBEDIENT))
			obedience-=1;
		if(slave.getTraits().contains(Trait.FEISTY))
			obedience+=1;
		if(slave.getTraits().contains(Trait.FIT))
			stamina-=1;
		if(slave.getTraits().contains(Trait.UNFIT))
			stamina+=1;
		if(slave.getTraits().contains(Trait.CLEVER))
			intelligence-=1;
		if(slave.getTraits().contains(Trait.STUPID))
			intelligence+=1;
		if(slave.getTraits().contains(Trait.STRONG))
			strength-=1;
		if(slave.getTraits().contains(Trait.WEAK))
			strength+=1;
		if(charisma==0)
			charisma=1;
		if(strength==0)
			strength=1;
		if(stamina==0)
			stamina=1;
		if(obedience==0)
			obedience=1;
		if(intelligence==0)
			intelligence=1;
		slave.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(+day/charisma);
		slave.getAttribute(BaseAttributeTypes.STRENGTH).addToValue(+day/strength);
		slave.getAttribute(BaseAttributeTypes.OBEDIENCE).addToValue(+day/obedience);
		slave.getAttribute(BaseAttributeTypes.STAMINA).addToValue(+day/stamina);
		slave.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(+day/intelligence);
	}
	
	public static String getSlaveJob() {
		return slaveJob;
	}
	
	
}