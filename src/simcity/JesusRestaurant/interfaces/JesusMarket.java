package simcity.JesusRestaurant.interfaces;

import simcity.JesusRestaurant.JesusCookRole;

public interface JesusMarket {
	
	public abstract void msgNeedRestock (JesusCookRole cook, String choice, int amount);
	
	public abstract void msgPayingBill (int id, double amount);
	
}