package simcity.JesusRestaurant.test.mock;


import simcity.mock.EventLog;
import simcity.mock.Mock;
import simcity.JesusRestaurant.interfaces.JesusCashier;
import simcity.JesusRestaurant.interfaces.JesusCustomer;
import simcity.JesusRestaurant.interfaces.JesusWaiter;

public class MockJesusWaiter extends Mock implements JesusWaiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public JesusCashier cashier;
	public EventLog log = new EventLog();

	public MockJesusWaiter(String name) {
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
	public void msgCheckComputed(JesusCustomer cust, double amount, String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(String n) {
		// TODO Auto-generated method stub
		
	}

}
