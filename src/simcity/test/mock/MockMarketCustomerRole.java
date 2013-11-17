package simcity.test.mock;

import simcity.interfaces.MarketCustomer;
import simcity.mock.LoggedEvent;
import simcity.role.Role;
import java.util.*;
import simcity.ItemOrder;

public class MockMarketCustomerRole extends Role implements MarketCustomer {
	String name;

	public MockMarketCustomerRole(String name) {
		this.name = name;
	}
	
	public void msgOrderItems(List<ItemOrder> i) {
		log.add(new LoggedEvent("Received msgOrderItems"));
	}
	
	public void msgIWantCar() {
		log.add(new LoggedEvent("Received msgIWantCar"));
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
