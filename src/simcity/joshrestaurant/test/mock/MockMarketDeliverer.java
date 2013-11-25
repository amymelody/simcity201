package simcity.joshrestaurant.test.mock;

import simcity.interfaces.MarketDeliverer;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.RestCashier;
import simcity.market.Order;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockMarketDeliverer extends Mock implements MarketDeliverer {
	
	public RestCashier cashier;

	public MockMarketDeliverer(String name) {
		super(name);
	}
	
	@Override
	public void msgPayment(RestCashier ca, int cash) {
		log.add(new LoggedEvent("Received msgPayment from cashier. Cash = $" + cash));
	}
	
	public void msgPayment(MarketCustomer c, int cash) {
		
	}
	
	public void msgDeliverItems(Order o) {
		
	}
	
	public void msgPay() {
		
	}
	
	public String toString() {
		return getName();
	}
	
}
