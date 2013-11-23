package simcity.joshrestaurant.test.mock;

import simcity.interfaces.MarketDeliverer;
import simcity.joshrestaurant.interfaces.JoshCashier;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockMarketDeliverer extends Mock implements MarketDeliverer {
	
	public JoshCashier cashier;

	public MockMarketDeliverer(String name) {
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
