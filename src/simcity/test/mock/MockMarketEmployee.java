package simcity.test.mock;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	public List<Order> orders =  Collections.synchronizedList(new ArrayList<Order>());
	
	public MockMarketEmployee(String n) {
		super(n);

		name = n;
	}

	@Override
	public void msgGetItems(Order o) {
		log.add(new LoggedEvent("Received order"));
		orders.add(o);
	}

	@Override
	public void msgPay() {
		log.add(new LoggedEvent("Paid"));
	}
	
}
