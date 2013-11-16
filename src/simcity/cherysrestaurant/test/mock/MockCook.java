package simcity.cherysrestaurant.test.mock;

import simcity.cherysrestaurant.interfaces.*;

public class MockCook extends Mock implements Cook
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	//public Cashier cashier;

	public MockCook(String name)
	{
		super(name);
	}

	@Override
	public void msgCookThis(Waiter w, String choice, int table)
	{
		log.add(new LoggedEvent("Received msgCookThis from waiter. Choice = " + choice + ". Table = " + table));
	}
	@Override
	public void msgDelivery(Market m, String f, int numberDelivered)
	{
		log.add(new LoggedEvent("Received msgDelivery from market. Food = " + f + ". Amount = " + numberDelivered));
	}
	@Override
	public void msgStockTooLow(Market m, String f, int numberAvailible)
	{
		log.add(new LoggedEvent("Received msgStockTooLow from market. Food = " + f + ". Amount left = " + numberAvailible));
	}
}
