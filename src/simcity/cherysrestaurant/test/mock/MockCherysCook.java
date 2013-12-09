package simcity.Anjalirestaurant.test.mock;

import simcity.Anjalirestaurant.interfaces.*;

public class MockAnjaliCook extends Mock implements AnjaliCook
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	//public Cashier cashier;

	public MockAnjaliCook(String name)
	{
		super(name);
	}

	@Override
	public void msgCookThis(AnjaliWaiter w, String choice, int table)
	{
		log.add(new LoggedEvent("Received msgCookThis from waiter. Choice = " + choice + ". Table = " + table));
	}
	@Override
	public void msgDelivery(AnjaliMarket m, String f, int numberDelivered)
	{
		log.add(new LoggedEvent("Received msgDelivery from market. Food = " + f + ". Amount = " + numberDelivered));
	}
	@Override
	public void msgStockTooLow(AnjaliMarket m, String f, int numberAvailible)
	{
		log.add(new LoggedEvent("Received msgStockTooLow from market. Food = " + f + ". Amount left = " + numberAvailible));
	}
}
