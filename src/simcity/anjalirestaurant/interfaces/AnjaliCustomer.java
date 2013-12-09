package simcity.anjalirestaurant.interfaces;

import java.util.Map;


public interface AnjaliCustomer {
	
	public abstract void msgFollowMeToTable(int tableX, int tableY, Map<String, Double> m, AnjaliWaiter w);

	public abstract void msgRestaurantIsFull();
	
	public abstract void msgPayCheck(double price);
	
	public abstract void msgGoodToGo();
	
	public abstract void msgPayMeLater();
	
	public abstract void msgWhatWouldYouLike();

	public abstract void msgNoMoreFood();

	public abstract void msgHereIsYourFood();

	public abstract String getName();
		
}
