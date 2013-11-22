package simcity;

import simcity.role.JobRole;
import simcity.interfaces.MarketDeliverer;

public abstract class RestCashierRole extends JobRole {

	public RestCashierRole() {
		// TODO Auto-generated constructor stub
	}

	public abstract void msgDelivery(int bill, MarketDeliverer deliverer);
	
	public abstract void msgChange(int change);
}
