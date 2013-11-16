package simcity.market.interfaces;

import java.util.List;

import simcity.market.test.mock.EventLog;
import simcity.ItemOrder;

public interface MarketCustomer {
	
	public EventLog log = new EventLog();
	
	public abstract void msgOrderItems(List<ItemOrder> i, boolean d, String l);
	
	public abstract void msgHereAreItemsandPrice(List<ItemOrder> i, int price);
	
	public abstract void msgHereIsOrder(List<ItemOrder> i, int price, MarketDeliverer del);
	
	public abstract void msgThankYou(int change);

}