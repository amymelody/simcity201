package simcity.Anjalirestaurant.test.mock;

import simcity.Anjalirestaurant.AnjaliCashierCheck;
import simcity.Anjalirestaurant.interfaces.*;

public class MockAnjaliHost extends Mock implements AnjaliHost
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	//public Cashier cashier;

	public MockAnjaliHost(String name)
	{
		super(name);
	}

	@Override
	public void msgImHungry(AnjaliCustomer c)
	{
		log.add(new LoggedEvent("Received msgImHungry from customer."));
	}
	@Override
	public void msgTableFree(int t, AnjaliWaiter w, AnjaliCustomer c)
	{
		log.add(new LoggedEvent("Received msgTableFree from waiter. Table = " + t));
	}
	@Override
	public void msgMayIGoOnBreak(AnjaliWaiter w)
	{
		log.add(new LoggedEvent("Received msgMayIGoOnBreak from waiter."));
	}
	@Override
	public void msgBackFromBreak(AnjaliWaiter w)
	{
		log.add(new LoggedEvent("Received msgBackFromBreak from waiter."));
	}
}
