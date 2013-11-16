package simcity.market.interfaces;

import simcity.market.test.mock.EventLog;
import simcity.market.Order;

public interface MarketDeliverer {
	
	public EventLog log = new EventLog();
	
	public abstract void msgDeliverItems(Order o);

	public abstract void msgPayment(MarketCustomer c, int money);

	public abstract void msgSignedInvoice(MarketCustomer c);

	public abstract void msgPay();
	
}