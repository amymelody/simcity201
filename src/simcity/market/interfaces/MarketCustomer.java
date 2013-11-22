package simcity.market.interfaces;

import java.util.List;

import simcity.ItemOrder;

public interface MarketCustomer {
	
	public abstract void msgOrderItems(List<ItemOrder> i, boolean d, String l);
	
	public abstract void msgHereAreItemsandPrice(List<ItemOrder> i, int price);
	
	public abstract void msgThankYou(int change);

}