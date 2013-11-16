package simcity.market.test.mock;

import simcity.market.Order;
import simcity.market.interfaces.MarketCashier;
import simcity.market.interfaces.MarketCustomer;
import simcity.market.interfaces.MarketDeliverer;

public class MockMarketDeliverer extends Mock implements MarketDeliverer {

	/**
	 * References for unit testing
	 */
	public MarketCashier cashier;
	public MarketCustomer customer;
	public String name;

	public MockMarketDeliverer(String n) {
		super(n);

		name = n;
	}

	@Override
	public void msgDeliverItems(Order o) {
		log.add(new LoggedEvent("Received order."));
		
	}

	@Override
	public void msgPayment(MarketCustomer c, int money) {
		log.add(new LoggedEvent("Received payment."));
		
	}

	@Override
	public void msgSignedInvoice(MarketCustomer c) {
		log.add(new LoggedEvent("Delivery confirmed by customer."));
		
	}

}
