package simcity.interfaces;

import simcity.joshrestaurant.interfaces.JoshCustomer;
import simcity.joshrestaurant.interfaces.JoshWaiter;

public interface RestCashier {
	
	public abstract void msgStartShift();
	
	public abstract void msgEndShift();
	
	public abstract void msgDelivery(int bill, MarketDeliverer deliverer);
	
	public abstract void msgThankYou(int change);
	
}