package jasbro.game.character.activities.sub.whore;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.PlannedActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.activities.WhoreActivityExecutor;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.MessageData;
import jasbro.game.events.business.Customer;
import jasbro.game.events.business.CustomerType;
import jasbro.game.events.business.SpawnData;
import jasbro.game.world.customContent.RapeEvent;

public class WhoreStreets extends RunningActivity {

	@Override
	public MessageData getBaseMessage() {
		return null;
	}

	@Override
	public void perform() {
		Charakter character = getCharacter();
		float remainingActions = character.getPossibleAmountCustomers();
		SpawnData spawnData = new SpawnData();
		spawnData.getGenderSpawnMap().put(Gender.MALE, 60);
		spawnData.getGenderSpawnMap().put(Gender.FEMALE, 20);
		spawnData.getGenderSpawnMap().put(Gender.FUTA, 20);
		int i = 0;
		do {
			Customer customer = spawnData.spawn(1, character.getFame()).get(0);
			PlannedActivity plannedActivity = new PlannedActivity(ActivityType.WHORE, getPlannedActivity(), character);
			WhoreActivityExecutor runningActivity = (WhoreActivityExecutor)plannedActivity.getActivityExecutor(getActivityExecutor().getShift());
			
			if (runningActivity.rateCustomer(customer) > 0) {
				int bonus=0;
				if(character.getTraits().contains(Trait.STREETSMARTS)){bonus=character.getIntelligence();}
				if ((customer.getType() != CustomerType.GROUP && Util.getInt(0, 100) > 30 - character.getStrength() - bonus) 
						|| (customer.getType() == CustomerType.GROUP && Util.getInt(0, 100) > 80 - character.getStrength()-bonus)) {
					runningActivity.addMainCustomer(customer);
					runningActivity.execute();
					remainingActions -= runningActivity.getAmountActions();
					
					if(character.getTraits().contains(Trait.QUICKIE) && (runningActivity.getSexType()==Sextype.ORAL || runningActivity.getSexType()==Sextype.FOREPLAY)){remainingActions+=0.5f;}
					if(character.getTraits().contains(Trait.THATGIRL)){character.getFame().modifyFame(Jasbro.getInstance().getData().getDay()/5);}
				}
				else {	
					RapeEvent rapeEvent = new RapeEvent();
					rapeEvent.addMainCustomer(customer);
					rapeEvent.setPlannedActivity(new PlannedActivity(ActivityType.EVENT, getPlannedActivity(), character));
					rapeEvent.performActivity();
					remainingActions -= rapeEvent.getAmountActions();
				}
			}
			i++;
		}
		while (remainingActions > 0.5f && character.getEnergy() > 10 && character.getHealth() > 15 && i < 30);
	}
}