package simcity.jesusrestaurant.test.mock;

import simcity.jesusrestaurant.JesusMenu;
import simcity.jesusrestaurant.interfaces.JesusCashier;
import simcity.jesusrestaurant.interfaces.JesusCustomer;
import simcity.jesusrestaurant.interfaces.JesusWaiter;
import simcity.mock.EventLog;
import simcity.mock.Mock;

public class JesusMockWaiter extends Mock implements JesusWaiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public JesusCashier cashier;
	public EventLog log = new EventLog();

	public JesusMockWaiter(String name) {
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
