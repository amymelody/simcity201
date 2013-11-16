package simcity.JesusRestaurant.test.mock;


import restaurant.Menu;
import simcity.cherysrestaurant.CookAgent;
import simcity.cherysrestaurant.interfaces.Cashier;
import simcity.cherysrestaurant.interfaces.Customer;
import simcity.cherysrestaurant.interfaces.Market;
import simcity.cherysrestaurant.interfaces.Waiter;

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
	public void msgNeedRestock(CookAgent cook, String choice, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayingBill(int id, double amount) {
		// TODO Auto-generated method stub
		
	}
	
}
