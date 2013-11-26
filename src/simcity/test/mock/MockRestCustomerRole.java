package simcity.test.mock;

import simcity.interfaces.RestCustomer;
import simcity.mock.LoggedEvent;
import simcity.role.Role;

public class MockRestCustomerRole extends Role implements RestCustomer {
	String name;

	public MockRestCustomerRole(String name) {
		this.name = name;
	}
	
	public void setCash(int c) {
		
	}
	
	public void gotHungry() {
		log.add(new LoggedEvent("Received gotHungry"));
	}
	
	public boolean pickAndExecuteAnAction() {
		if (log.size() > 0) {
			person.msgDoneEating();
			person.msgExpense(16);
			person.msgLeftDestination(this);
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "customer " + getName();
	}

}
