package simcity.JesusRestaurant.interfaces;

import simcity.JesusRestaurant.JesusMenu;

public interface JesusCustomer {
	
	public abstract void msgSitAtTable (JesusWaiter w, int tNum, JesusMenu menu);
	
	public abstract void msgTakeOrder ();

	public abstract void msgRetakeOrder (JesusMenu m);
	
	public abstract void msgNoFood ();
	
	public abstract void msgEnjoyOrder (String choice);
	
	public abstract void msgHereIsCheck (double aDue);
	
	public abstract void msgChange (double change);
	
	public abstract void msgPayNextTime (double amount);

}