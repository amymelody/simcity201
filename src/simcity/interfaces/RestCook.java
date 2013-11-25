package simcity.interfaces;

import java.util.List;

import simcity.ItemOrder;
import simcity.joshrestaurant.JoshWaiterRole;

public interface RestCook {
	
	public abstract void msgStartShift();
	
	public abstract void msgEndShift();
	
	public abstract void addMarket(MarketCashier m);
	
	public abstract void msgHereIsOrder(JoshWaiterRole waiter, String choice, int table);
	
	public abstract void msgHereIsWhatICanFulfill(List<ItemOrder> orders, boolean canFulfill);
	
	public abstract void msgDelivery(List<ItemOrder> orders);
	
}