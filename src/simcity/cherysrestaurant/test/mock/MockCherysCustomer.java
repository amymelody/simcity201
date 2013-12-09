package simcity.Anjalirestaurant.test.mock;


import java.util.List;
import java.util.Map;

import simcity.Anjalirestaurant.AnjaliCashierCheck;
import simcity.Anjalirestaurant.AnjaliWaiterFood;
import simcity.Anjalirestaurant.gui.AnjaliCustomerGui;
import simcity.Anjalirestaurant.interfaces.*;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockAnjaliCustomer extends Mock implements AnjaliCustomer
{

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public AnjaliCashier cashier;
	public EventLog log = new EventLog();

	public MockAnjaliCustomer(String name)
	{
		super(name);
	}

	@Override
	public void msgFollowMe(AnjaliWaiter w, Map<Integer, AnjaliWaiterFood> m, List<String> foo)
	{
		log.add(new LoggedEvent("Received msgFollowMe from waiter."));
	}
	@Override
	public void msgWhatIsYourOrder(List<String> foo)
	{
		log.add(new LoggedEvent("Received msgWhatIsYourOrder from waiter."));
	}
	@Override
	public void msgOrderServed(String ch)
	{
		log.add(new LoggedEvent("Received msgOrderServed from waiter. Order served = "+ ch));
	}
	@Override
	public void msgHereIsCheck(AnjaliCashierCheck ch)
	{
		log.add(new LoggedEvent("Received msgHereIsCheck from waiter. Order = " + ch.order + ". Total = " + ch.total));	
		//make this part a psuedo-scheduler
	}
	@Override
	public void msgChange(double change)
	{
		log.add(new LoggedEvent("Received msgChange from cashier. Change = " + change));
	}
	@Override
	public EventLog getLog()
	{
		return log;
	}
	@Override
	public AnjaliCustomerGui getGui()
	{
		return null;
	}
}
