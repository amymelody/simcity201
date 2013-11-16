package simcity.cherysrestaurant.test.mock;

import simcity.cherysrestaurant.CashierCheck;
import simcity.cherysrestaurant.interfaces.*;

public class MockCashier extends Mock implements Cashier
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	//public Cashier cashier;

	public MockCashier(String name)
	{
		super(name);
	}

	@Override
	public void msgProduceCheck(Waiter w, String choice, int table)
	{
		log.add(new LoggedEvent("Received msgProduceCheck from waiter. Choice = " + choice + ". Table = " + table));
	}
	@Override
	public void msgGiveCheck(Waiter w, int table)
	{
		log.add(new LoggedEvent("Received msgGiveCheck from waiter. Table = " + table));
	}
	@Override
	public void msgPayment(Customer cust, CashierCheck c, double cashGiven)
	{
		log.add(new LoggedEvent("Received msgPayment from customer. Payment = " + cashGiven));
	}
	@Override
	public void msgPayForDelivery(Market m, String foodType, int amountDelivered, double wholesalePercentage)
	{
		log.add(new LoggedEvent("Received msgPayForDelivery from market. Food = " + foodType + ". Amount = " + amountDelivered + ". Percentage = " + wholesalePercentage*100 + "%"));
	}
}
