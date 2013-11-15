package simcity.market.test.mock;


import restaurant.CookAgent;
import restaurant.Menu;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;

public class MockEmployee extends Mock implements Employee {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();

	public MockEmployee(String n) {
		super(n);
	}

	@Override
	public void msgNeedRestock(CookAgent cook, String choice, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayingBill(int id, double amount) {
		// TODO Auto-generated method stub
		
	}
	
}
