package jasbro.game.character.activities;

import jasbro.game.character.Charakter;
import jasbro.game.events.business.Customer;

import java.util.List;

/**
 * Activity which occupies a customer for one shift
 * @author Azrael
 *
 */
public interface BusinessMainActivity extends BusinessActivity {
	public int rateCustomer(Customer customer);
	public void addMainCustomer(Customer customer);
	public boolean hasMainCustomer();
	public List<Customer> getMainCustomers();
	public List<Charakter> getCharacters();
}
