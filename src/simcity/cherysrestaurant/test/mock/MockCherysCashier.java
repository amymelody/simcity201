package simcity.cherysrestaurant.test.mock;

import simcity.cherysrestaurant.CherysCashierCheck;
import simcity.cherysrestaurant.interfaces.*;

public class MockCherysCashier extends Mock implements CherysCashier
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	//public Cashier cashier;

	public MockCherysCashier(String name)
	{
		super(name);
	}

	@Override
	public void msgProduceCheck(CherysWaiter w, String choice, int table)
	{
		log.add(new LoggedEvent("Received msgProduceCheck from waiter. Choice = " + choice + ". Table = " + table));
	}
	@Override
	public void msgGiveCheck(CherysWaiter w, int table)
	{
		log.add(new LoggedEvent("Received msgGiveCheck from waiter. Table = " + table));
	}
	@Override
	public void msgPayment(CherysCustomer cust, CherysCashierCheck c, double cashGiven)
	{
		log.add(new LoggedEvent("Received msgPayment from customer. Payment = " + cashGiven));
	}
	@Override
	public void msgPayForDelivery(CherysMarket m, String foodType, int amountDelivered, double wholesalePercentage)
	{
		log.add(new LoggedEvent("Received msgPayForDelivery from market. Food = " + foodType + ". Amount = " + amountDelivered + ". Percentage = " + wholesalePercentage*100 + "%"));
	}
}
