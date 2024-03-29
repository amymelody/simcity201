package simcity.joshrestaurant.test.mock;

import simcity.joshrestaurant.interfaces.JoshCashier;
import simcity.joshrestaurant.interfaces.JoshWaiter;
import simcity.joshrestaurant.interfaces.JoshCustomer;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockJoshWaiter extends Mock implements JoshWaiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public JoshCashier cashier;

	public MockJoshWaiter(String name) {
		super(name);

	}
	
	@Override
	public void msgHereIsCheck(JoshCustomer c, int charge) {
		log.add(new LoggedEvent("Received msgHereIsCheck from cashier. Customer = " + c.getName() + ". Charge = $" + charge));
	}
	
	public String toString() {
		return getName();
	}
	
}
