package simcity.anjalirestaurant.interfaces;

import simcity.interfaces.MarketDeliverer;


/**
 * Restaurant Cashier Agent, Scenario 1
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios
public interface AnjaliCashier {
	
/////MESSAGES////////
	public abstract void msgMakeCheck(AnjaliCustomer c, String choice, int tableNumber, AnjaliWaiter w);
		
	public abstract void msgStartShift();
	
	public abstract void msgEndShift();
	
	public abstract void msgIHaveNoMoney(AnjaliCustomer c);
		
	public abstract void msgDelivery(int bill, MarketDeliverer deliverer);
	
}