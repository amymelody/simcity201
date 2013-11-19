package simcity.joshrestaurant.test.mock;

import simcity.joshrestaurant.interfaces.JoshCashier;
import simcity.joshrestaurant.interfaces.JoshCustomer;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockJoshCustomer extends Mock implements JoshCustomer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public JoshCashier cashier;
	private int charge;

	public MockJoshCustomer(String name) {
		super(name);

	}
	
	public int getCharge() {
		return charge;
	}
	
	public void setCharge(int c) {
		charge = c;
	}
	
	@Override
	public void msgChange(int change) {
		log.add(new LoggedEvent("Received msgChange from cashier. Change = $" + change));
	}
	
	public String toString() {
		return "customer " + getName();
	}
}
