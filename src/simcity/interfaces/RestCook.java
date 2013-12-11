package simcity.interfaces;

import java.util.List;

import simcity.ItemOrder;
import simcity.joshrestaurant.JoshWaiterRole;

public interface RestCook {
	
	public abstract void msgStartShift();
	
	public abstract void msgEndShift();
	
	public abstract void addMarket(MarketCashier m, String n);
	
	public abstract void msgHereIsWhatICanFulfill(List<ItemOrder> orders, boolean canFulfill, MarketCashier mC);
	
	public abstract void msgDelivery(List<ItemOrder> orders);
	
}