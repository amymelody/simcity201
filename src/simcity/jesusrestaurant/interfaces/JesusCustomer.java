package simcity.jesusrestaurant.interfaces;

import simcity.jesusrestaurant.JesusMenu;

public interface JesusCustomer {
	
	public abstract void msgSitAtTable (JesusWaiter w, int tNum, JesusMenu menu);
	
	public abstract void msgTakeOrder ();

	public abstract void msgRetakeOrder (JesusMenu m);
	
	public abstract void msgNoFood ();
	
	public abstract void msgEnjoyOrder (String choice);
	
	public abstract void msgHereIsCheck (int aDue);
	
	public abstract void msgChange (int change);
	
	public abstract void msgPayNextTime (int amount);

}