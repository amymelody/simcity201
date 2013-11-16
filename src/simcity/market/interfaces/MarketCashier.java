package simcity.market.interfaces;

import java.util.List;

import simcity.market.test.mock.EventLog;
import simcity.ItemOrder;
import simcity.market.Order;

public interface MarketCashier {
	
	public EventLog log = new EventLog();
	
	public abstract void msgHired(MarketEmployee e, int salary);
	
	public abstract void msgHired(MarketDeliverer d, int salary);
	
	public abstract void msgOntheClock(MarketEmployee e);
	
	public abstract void msgOntheClock(MarketDeliverer d);
	
	public abstract void msgOfftheClock(MarketEmployee e);
	
	public abstract void msgOfftheClock(MarketDeliverer d);
	
	public abstract void msgDoneForTheDay();
	
	public abstract void msgWereOpen();
	
	public abstract void msgIWantItems(MarketCustomer c, List<ItemOrder> items);
	
	public abstract void msgHereAreItems(Order order, MarketEmployee e);
	
	public abstract void msgPayment(MarketCustomer c, int money);
	
	public abstract void msgIWantDelivery(MarketCustomer c, List<ItemOrder> i, String location);
	
	public abstract void msgDelivered(Order order, MarketDeliverer d);
	
}