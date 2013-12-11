package simcity.anjalirestaurant.interfaces;

import simcity.anjalirestaurant.AnjaliHostRole.Table;
import simcity.anjalirestaurant.AnjaliWaiterRole;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios

public interface AnjaliHost {
	
	//////////// MESSAGES MESSAGES MESSAGES MESSAGES ///////////
	
	
	public abstract void msgIWantFood(AnjaliCustomer cust);
		
	
	public abstract void msgIWillWait(final AnjaliCustomer c);
	
	public abstract void msgWantBreak(AnjaliWaiterRole w);
	
	
	public abstract void msgWaiterBreakDone(AnjaliWaiterRole w);
	
	public abstract void msgTableIsFree(Table t, AnjaliWaiterRole w);
}