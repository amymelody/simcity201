package simcity;

import role.Role;
import interfaces.RestCustomer;

public abstract class RestCustomerRole extends Role implements RestCustomer {

	public RestCustomerRole() {
		// TODO Auto-generated constructor stub
	}

	public abstract void gotHungry();
}
