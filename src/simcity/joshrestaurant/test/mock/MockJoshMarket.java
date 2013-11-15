package simcity.joshrestaurant.test.mock;

import mock.Mock;
import mock.LoggedEvent;
import simcity.joshrestaurant.interfaces.JoshCashier;
import simcity.joshrestaurant.interfaces.JoshMarket;

public class MockJoshMarket extends Mock implements JoshMarket {
	
	public JoshCashier cashier;

	public MockJoshMarket(String name) {
		super(name);
	}
	
	@Override
	public void msgPayment(int cash) {
		log.add(new LoggedEvent("Received msgPayment from cashier. Cash = $" + cash));
	}
	
	public String toString() {
		return getName();
	}
	
}
