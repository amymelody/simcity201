package simcity.cherysrestaurant.test.mock; 

import simcity.cherysrestaurant.CherysCashierCheck;
import simcity.cherysrestaurant.interfaces.*;

public class MockCherysHost extends Mock implements CherysHost
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	//public Cashier cashier;

	public MockCherysHost(String name)
	{
		super(name);
	}

	@Override
	public void msgImHungry(CherysCustomer c)
	{
		log.add(new LoggedEvent("Received msgImHungry from customer."));
	}
	@Override
	public void msgTableFree(int t, CherysWaiter w, CherysCustomer c)
	{
		log.add(new LoggedEvent("Received msgTableFree from waiter. Table = " + t));
	}
	@Override
	public void msgMayIGoOnBreak(CherysWaiter w)
	{
		log.add(new LoggedEvent("Received msgMayIGoOnBreak from waiter."));
	}
	@Override
	public void msgBackFromBreak(CherysWaiter w)
	{
		log.add(new LoggedEvent("Received msgBackFromBreak from waiter."));
	}
}
