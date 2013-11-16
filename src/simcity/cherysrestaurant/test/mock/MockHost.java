package simcity.cherysrestaurant.test.mock;

import simcity.cherysrestaurant.CashierCheck;
import simcity.cherysrestaurant.interfaces.*;

public class MockHost extends Mock implements Host
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	//public Cashier cashier;

	public MockHost(String name)
	{
		super(name);
	}

	@Override
	public void msgImHungry(Customer c)
	{
		log.add(new LoggedEvent("Received msgImHungry from customer."));
	}
	@Override
	public void msgTableFree(int t, Waiter w, Customer c)
	{
		log.add(new LoggedEvent("Received msgTableFree from waiter. Table = " + t));
	}
	@Override
	public void msgMayIGoOnBreak(Waiter w)
	{
		log.add(new LoggedEvent("Received msgMayIGoOnBreak from waiter."));
	}
	@Override
	public void msgBackFromBreak(Waiter w)
	{
		log.add(new LoggedEvent("Received msgBackFromBreak from waiter."));
	}
}
