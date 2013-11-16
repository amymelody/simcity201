package simcity.test.mock;

import simcity.interfaces.RestCustomer;
import simcity.mock.LoggedEvent;
import simcity.role.Role;

public class MockRestCustomerRole extends Role implements RestCustomer {
	String name;

	public MockRestCustomerRole(String name) {
		this.name = name;
	}
	
	public void gotHungry() {
		log.add(new LoggedEvent("Received gotHungry"));
	}
	
	public boolean pickAndExecuteAnAction() {
		
	}
	
	public String toString() {
		return "customer " + getName();
	}

}
