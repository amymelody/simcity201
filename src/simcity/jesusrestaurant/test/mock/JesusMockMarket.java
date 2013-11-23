package simcity.jesusrestaurant.test.mock;

import simcity.jesusrestaurant.JesusCookRole;
import simcity.jesusrestaurant.JesusMenu;
import simcity.jesusrestaurant.interfaces.JesusCashier;
import simcity.jesusrestaurant.interfaces.JesusCustomer;
import simcity.jesusrestaurant.interfaces.JesusMarket;
import simcity.jesusrestaurant.interfaces.JesusWaiter;
import simcity.mock.EventLog;
import simcity.mock.Mock;

public class JesusMockMarket extends Mock implements JesusMarket {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public JesusCashier cashier;
	public EventLog log = new EventLog();

	public JesusMockMarket(String n) {
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
