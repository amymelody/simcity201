package simcity.market.interfaces;

import java.util.List;

import simcity.ItemOrder;

public interface MarketCustomer {
	
	public abstract void msgOrderItems(List<ItemOrder> i);
	
	public abstract void msgHereIsWhatICanFulfill(List<ItemOrder> orders, boolean canFulfill);
	
	public abstract void msgHereAreItemsandPrice(List<ItemOrder> i, int price);
	
	public abstract void msgThankYou(int change);

}