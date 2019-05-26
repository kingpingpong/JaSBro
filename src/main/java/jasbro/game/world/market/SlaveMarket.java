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
public class SlaveMarket extends GameObject implements CentralEventlistener {
	private List<Charakter> slaves;
	private Charakter slave;
	
	public SlaveMarket() {
		Jasbro.getInstance().addCentralListener(this);
		Jasbro.getInstance().getData().setSlaveMarket(this);
		slaves = getSlaves();
	}
	
	@Override
	public void handleCentralEvent(MyEvent e) {
		if (e.getType() == EventType.NEXTDAY) {
			slaves = null;
			getSlaves();
		}
	}
	
	public List<Charakter> getSlaves() {
		if (slaves == null) {
			slaves = new ArrayList<Charakter>();
			int amountSlaves = 3;
			if(Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.BENEFACTORSLAVEMARKET))
				amountSlaves=6;
			for (int i = 0; i < amountSlaves; i++) {
				slave = Jasbro.getInstance().generateBasicSlave();
				if(slave.getTraits().contains(Trait.UNSELLABLE) || slave.getTraits().contains(Trait.FORMERNOBLE) || slave.getTraits().contains(Trait.RARESLAVE) || slave.getTraits().contains(Trait.EXTREMELYRARESLAVE) || slave.getTraits().contains(Trait.EXTREMELYRARESLAVE2)){
					i--;
				}
				else{
					slave.getAttribute(BaseAttributeTypes.OBEDIENCE).addToValue(-slave.getObedience() + Util.getInt(0, 5));
					slaves.add(slave);
				}
			}
		}
		return slaves;
	}
	
	public void setSlaves(List<Charakter> slaves) {
		this.slaves = slaves;
	}	
	
}