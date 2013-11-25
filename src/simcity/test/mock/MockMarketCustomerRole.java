package simcity.test.mock;

import simcity.interfaces.MarketCustomer;
import simcity.mock.LoggedEvent;
import simcity.role.Role;

import java.util.*;

import simcity.ItemOrder;

public class MockMarketCustomerRole extends Role implements MarketCustomer {
	String name;
	List<ItemOrder> foods = new ArrayList<ItemOrder>();

	public MockMarketCustomerRole(String name) {
		this.name = name;
	}
	
	public void msgOrderItems(List<ItemOrder> i) {
		for (ItemOrder item : i) {
			foods.add(item);
		}
		log.add(new LoggedEvent("Received msgOrderItems"));
	}
	
	public void msgIWantCar() {
		log.add(new LoggedEvent("Received msgIWantCar"));
	}
	
	public boolean pickAndExecuteAnAction() {
		if (log.containsString("Received msgOrderItems")) {
			person.msgReceivedItems(foods);
			person.msgExpense(30);
			person.msgLeftDestination(this);
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "customer " + getName();
	}

	@Override
	public void msgHereIsWhatICanFulfill(List<ItemOrder> orders,
			boolean canFulfill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereAreItemsandPrice(List<ItemOrder> i, int price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgThankYou(int change) {
		// TODO Auto-generated method stub
		
	}

}
