package simcity.anjalirestaurant.interfaces;


/**
 * Restaurant Cashier Agent, Scenario 1
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios
public interface AnjaliCook {
	
/////MESSAGES////////
	public abstract void msgHereIsOrder(String name, String choice, int tableNumber, AnjaliWaiter waiter);
		
	
	public abstract void msgHereIsMoreFood(String food);
		
	public abstract void msgOrderFromMe(AnjaliMarket m);
	
	public abstract void msgPartOrderFulfilled(String food);
	
	public abstract void msgNoMarketSupply(String food);
}