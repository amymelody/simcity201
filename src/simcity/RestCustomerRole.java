package simcity;

import simcity.interfaces.RestCustomer;
import simcity.role.Role;

public abstract class RestCustomerRole extends Role implements RestCustomer {

	public RestCustomerRole() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract void setCash(int c);

	public abstract void gotHungry();
}
