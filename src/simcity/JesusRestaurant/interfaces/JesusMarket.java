package simcity.JesusRestaurant.interfaces;

import restaurant.CookAgent;

public interface JesusMarket {
	
	public abstract void msgNeedRestock (CookAgent cook, String choice, int amount);
	
	public abstract void msgPayingBill (int id, double amount);
	
}