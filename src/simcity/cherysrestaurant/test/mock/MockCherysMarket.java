package simcity.Anjalirestaurant.test.mock;

import simcity.Anjalirestaurant.interfaces.*;

public class MockAnjaliMarket extends Mock implements AnjaliMarket
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	public AnjaliCashier cashier;
	public EventLog log = new EventLog();

	public MockAnjaliMarket(String name)
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
