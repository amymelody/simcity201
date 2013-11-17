package simcity.test.mock;

import simcity.interfaces.Resident;
import simcity.mock.LoggedEvent;
import simcity.role.Role;
import java.util.*;
import simcity.ItemOrder;

public class MockResidentRole extends Role implements Resident {
	String name;

	public MockResidentRole(String name) {
		this.name = name;
	}
	
	public void msgGroceries(List<ItemOrder> i) {
		log.add(new LoggedEvent("Received msgGroceries"));
	}
	
	public void msgImHome() {
		log.add(new LoggedEvent("Received msgImHome"));
	}
	
	public void msgEat() {
		log.add(new LoggedEvent("Received msgEat"));
	}
	
	public void msgLeave() {
		log.add(new LoggedEvent("Received msgLeave"));
	}
	
	public void msgAtLandlord() {
		log.add(new LoggedEvent("Received msgAtLandlord"));
	}
	
	public boolean pickAndExecuteAnAction() {
		if (log.containsString("Received msgOrderItems")) {
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
