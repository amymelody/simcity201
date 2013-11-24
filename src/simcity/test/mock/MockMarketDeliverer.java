package simcity.test.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketDeliverer;
import simcity.market.Order;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockMarketDeliverer extends Mock implements MarketDeliverer {

	/**
	 * References for unit testing
	 */
	public MarketCashier cashier;
	public MarketCustomer customer;
	public List<Order> orders =  Collections.synchronizedList(new ArrayList<Order>());
	public String name;

	public MockMarketDeliverer(String n) {
		super(n);

		name = n;
	}

	@Override
	public void msgDeliverItems(Order o) {
		orders.add(o);
		log.add(new LoggedEvent("Received order"));
	}

	@Override
	public void msgPayment(MarketCustomer c, int money) {
		log.add(new LoggedEvent("Received payment"));
	}

	@Override
	public void msgPay() {
		log.add(new LoggedEvent("Paid"));
	}

}
