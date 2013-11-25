package simcity;

import simcity.role.JobRole;
import simcity.interfaces.MarketDeliverer;
import simcity.interfaces.RestCashier;

public abstract class RestCashierRole extends JobRole implements RestCashier {

	public RestCashierRole() {
		// TODO Auto-generated constructor stub
	}

	public abstract void msgDelivery(int bill, MarketDeliverer deliverer);
	
	public abstract void msgThankYou(int change);
}
