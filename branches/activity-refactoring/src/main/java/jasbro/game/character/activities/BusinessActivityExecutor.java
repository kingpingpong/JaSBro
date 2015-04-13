package jasbro.game.character.activities;

import jasbro.game.character.Charakter;
import jasbro.game.events.business.Customer;

import java.util.List;

public class BusinessActivityExecutor extends DefaultExecutor {

	public void addMainCustomer(Customer customer) {
		runningActivity.addMainCustomer(customer);
		
	}

	public boolean hasMainCustomer() {
		return runningActivity.hasMainCustomer();
	}

	public Charakter getCharacter() {
		return runningActivity.getCharacter();
	}

	public int rateCustomer(Customer curCustomer) {
		return ((BusinessMainActivity)runningActivity).rateCustomer(curCustomer);
	}

	public List<Charakter> getCharacters() {
		return runningActivity.getCharacters();
	}

}
