package simcity.anjalirestaurant.interfaces;

import restaurant.HostAgent.Table;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios

public interface Host {
	
	//////////// MESSAGES MESSAGES MESSAGES MESSAGES ///////////
	
	
	public abstract void msgIWantFood(Customer cust);
		
	
	public abstract void msgIWillWait(final Customer c);
	
	public abstract void msgWantBreak(Waiter w);
	
	
	public abstract void msgWaiterBreakDone(Waiter w);
	
	public abstract void msgTableIsFree(Table t, Waiter w);
}