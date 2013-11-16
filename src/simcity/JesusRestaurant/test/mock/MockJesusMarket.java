package simcity.JesusRestaurant.test.mock;


import restaurant.CookAgent;
import restaurant.Menu;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;

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
