package simcity.cherysrestaurant.test.mock; 


import java.util.List;
import java.util.Map;

import simcity.cherysrestaurant.CherysCashierCheck;
import simcity.cherysrestaurant.CherysWaiterFood;
import simcity.cherysrestaurant.gui.CherysCustomerGui;
import simcity.cherysrestaurant.interfaces.*;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCherysCustomer extends Mock implements CherysCustomer
{

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public CherysCashier cashier;
	public EventLog log = new EventLog();

	public MockCherysCustomer(String name)
	{
		super(name);
	}

	@Override
	public void msgFollowMe(CherysWaiter w, Map<Integer, CherysWaiterFood> m, List<String> foo)
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
	public void msgHereIsCheck(CherysCashierCheck ch)
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
	public CherysCustomerGui getGui()
	{
		return null;
	}
}
