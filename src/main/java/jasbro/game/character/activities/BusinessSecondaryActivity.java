package jasbro.game.character.activities;

import jasbro.game.events.business.Customer;

import java.util.List;

/**
 * Each customer can attend one main and two(?) secondary activities
 * @author Azrael
 *
 */
public interface BusinessSecondaryActivity {
	public void addAttendingCustomer(Customer customer);
	public int getAppeal();
	public int getMaxAttendees();
	public List<Customer> getCustomers();
}