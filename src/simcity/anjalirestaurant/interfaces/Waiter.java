package simcity.anjalirestaurant.interfaces;

import restaurant.CookAgent.Order;
import restaurant.HostAgent.Table;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios

public interface Waiter {
	
	//////////// MESSAGES MESSAGES MESSAGES MESSAGES ///////////
	
	public abstract void msgSitThisCustomer(Customer c, Table t);
	public abstract void msgReadyForCheck(Customer c);
	public abstract void msgReadyToOrder(Customer c);
	public abstract void msgBreakAllowed();
	public abstract void msgHereIsMyChoice(Customer c, String food);
	public abstract void msgHereIsCheck(int tn, double amount);
	public abstract String getName();
	public abstract void msgNoMoreFood(Order o);
	public abstract void msgRemoveItem(String food);
	public abstract void msgOrderIsReady(int tableNumber);
	public abstract void msgLeavingTable(Customer c);

}