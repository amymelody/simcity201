package simcity.cherysrestaurant.test.mock;

import simcity.cherysrestaurant.interfaces.*;

public class MockMarket extends Mock implements Market
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();

	public MockMarket(String name)
	{
		super(name);
	}

	@Override
	public void msgPlaceOrder(String f, int orderNumber)
	{
		log.add(new LoggedEvent("Received msgPlaceOrder from cook. Food = " + f + ". Amount = " + orderNumber));
	}
	@Override
	public void msgPaymentForDelivery(double payment)
	{
		log.add(new LoggedEvent("Received msgPaymentForDelivery from cashier. Payment = " + payment));
	}
	
	@Override
	public EventLog getLog()
	{
		return log;
	}
}
