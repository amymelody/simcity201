package simcity.market.test.mock;


import restaurant.Menu;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

public class MockDeliverer extends Mock implements Deliverer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();

	public MockDeliverer(String name) {
		super(name);

	}

	@Override
	public void msgReadytoOrder(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgMyOrder(String name, String order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCheckComputed(Customer cust, double amount, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(String n) {
		// TODO Auto-generated method stub
		
	}

}
