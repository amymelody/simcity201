package simcity.market.interfaces;

import simcity.market.Order;

public interface MarketEmployee {
	
	public abstract void msgGetItems(Order o);

	public abstract void msgPay();
	
}