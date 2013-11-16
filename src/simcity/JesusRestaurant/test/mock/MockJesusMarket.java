package simcity.JesusRestaurant.test.mock;


import simcity.mock.EventLog;
import simcity.mock.Mock;
import simcity.JesusRestaurant.JesusCookRole;
import simcity.JesusRestaurant.interfaces.JesusCashier;
import simcity.JesusRestaurant.interfaces.JesusMarket;

public class MockJesusMarket extends Mock implements JesusMarket {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public JesusCashier cashier;
	public EventLog log = new EventLog();

	public MockJesusMarket(String n) {
		super(n);
	}

	@Override
	public void msgNeedRestock(JesusCookRole cook, String choice, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayingBill(int id, double amount) {
		// TODO Auto-generated method stub
		
	}
	
}
