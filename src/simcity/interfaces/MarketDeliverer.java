package simcity.interfaces;

import simcity.market.Order;

public interface MarketDeliverer {
	
	public abstract void msgDeliverItems(Order o);
	
	public abstract void msgPayment(RestCashier c, int money);
	
}