/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.game.GameObject;
import jasbro.game.character.Charakter;
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
			int amountSlaves = Math.min(10, Math.max(3, 
                    ((int) Math.sqrt(Jasbro.getInstance().getData().getProtagonist().getFame().getFame()) / 50)));
			for (int i = 0; i < amountSlaves; i++) {
				slaves.add(Jasbro.getInstance().generateBasicSlave());
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

	
}
