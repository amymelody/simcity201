package simcity.market.test.mock;


import simcity.market.Order;
import simcity.market.interfaces.MarketCashier;
import simcity.market.interfaces.MarketEmployee;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockMarketEmployee extends Mock implements MarketEmployee {

	/**
	 * References for unit testing
	 */
	public MarketCashier cashier;
	public String name;

	public MockMarketEmployee(String n) {
		super(n);

		name = n;
	}

	@Override
	public void msgGetItems(Order o) {
		log.add(new LoggedEvent("Received order."));
		
	}

	@Override
	public void msgPay() {
		// TODO Auto-generated method stub
		
	}
	
}
