package simcity.Anjalirestaurant.test.mock;

import simcity.Anjalirestaurant.AnjaliCashierCheck;
import simcity.Anjalirestaurant.interfaces.*;

public class MockAnjaliCashier extends Mock implements AnjaliCashier
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	//public Cashier cashier;

	public MockAnjaliCashier(String name)
	{
		super(name);
	}

	@Override
	public void msgProduceCheck(AnjaliWaiter w, String choice, int table)
	{
		log.add(new LoggedEvent("Received msgProduceCheck from waiter. Choice = " + choice + ". Table = " + table));
	}
	@Override
	public void msgGiveCheck(AnjaliWaiter w, int table)
	{
		log.add(new LoggedEvent("Received msgGiveCheck from waiter. Table = " + table));
	}
	@Override
	public void msgPayment(AnjaliCustomer cust, AnjaliCashierCheck c, double cashGiven)
	{
		log.add(new LoggedEvent("Received msgPayment from customer. Payment = " + cashGiven));
	}
	@Override
	public void msgPayForDelivery(AnjaliMarket m, String foodType, int amountDelivered, double wholesalePercentage)
	{
		log.add(new LoggedEvent("Received msgPayForDelivery from market. Food = " + foodType + ". Amount = " + amountDelivered + ". Percentage = " + wholesalePercentage*100 + "%"));
	}
}
