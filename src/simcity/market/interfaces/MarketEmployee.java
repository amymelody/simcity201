package simcity.market.interfaces;

import simcity.market.test.mock.EventLog;
import simcity.market.Order;

public interface MarketEmployee {
	
	public EventLog log = new EventLog();
	
	public abstract void msgGetItems(Order o);

	public abstract void msgPay();
	
}