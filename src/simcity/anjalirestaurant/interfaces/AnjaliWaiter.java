package simcity.anjalirestaurant.interfaces;

import simcity.anjalirestaurant.AnjaliCookRole.Order;
import simcity.anjalirestaurant.AnjaliHostRole.Table;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios

public interface AnjaliWaiter {
	
	//////////// MESSAGES MESSAGES MESSAGES MESSAGES ///////////
	
	public abstract void msgSitThisCustomer(AnjaliCustomer c, Table t);
	public abstract void msgReadyForCheck(AnjaliCustomer c);
	public abstract void msgReadyToOrder(AnjaliCustomer c);
	public abstract void msgBreakAllowed();
	public abstract void msgHereIsMyChoice(AnjaliCustomer c, String food);
	public abstract void msgHereIsCheck(int tn, double amount);
	public abstract String getName();
	public abstract void msgNoMoreFood(Order o);
	public abstract void msgRemoveItem(String food);
	public abstract void msgOrderIsReady(int tableNumber);
	public abstract void msgLeavingTable(AnjaliCustomer c);

}