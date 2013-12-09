package simcity.Anjalirestaurant.test.mock;

import java.util.List;

import simcity.Anjalirestaurant.AnjaliCashierCheck;
import simcity.Anjalirestaurant.interfaces.*;

public class MockAnjaliWaiter extends Mock implements AnjaliWaiter
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	public AnjaliCashier cashier;
	public AnjaliCashierCheck check;
	public EventLog log = new EventLog();

	public MockAnjaliWaiter(String name)
	{
		super(name);
	}

	@Override
	public void msgPleaseSeatCustomer(AnjaliCustomer c, int table)
	{
		log.add(new LoggedEvent("Received msgPleaseSeatCustomer from host. Table = " + table));
	}
	@Override
	public void msgReadyToOrder(AnjaliCustomer c)
	{
		log.add(new LoggedEvent("Received msgReadyToOrder from customer."));
	}
	@Override
	public void msgHereIsMyOrder(AnjaliCustomer c, String choice)
	{
		log.add(new LoggedEvent("Received msgHereIsMyOrder from customer. Order = " + choice));
	}
	@Override
	public void msgOutOfFood(String choice, int table, List<String> foo)
	{
		log.add(new LoggedEvent("Received msgOutOfFood from cook. Food = " + choice + ". Table = " + table));
	}
	@Override
	public void msgOrderReady(String choice, int table, List<String> foo)
	{
		log.add(new LoggedEvent("Received msgOrderReady from cook. Order = " + choice + ". Table = " + table));
	}
	@Override
	public void msgDoneEating(AnjaliCustomer c)
	{
		log.add(new LoggedEvent("Received msgDoneEating from customer."));
	}
	@Override
	public void msgHereIsCheck(AnjaliCashierCheck ch)
	{
		log.add(new LoggedEvent("Received msgHereIsCheck from cashier. Order = " + ch.order + ". Total = " + ch.total));
		System.out.println(name + " recieved HereIsCheck");
		check = ch;
	}
	@Override
	public void msgLeavingRestaurant(AnjaliCustomer c)
	{
		log.add(new LoggedEvent("Received msgLeavingRestaurant from customer."));
	}
	@Override
 	public void msgGoOnBreak(boolean tf)
	{
		log.add(new LoggedEvent("Received msgGoOnBreak from host. Allowed? = " + tf));
	}
	@Override
	public AnjaliCashierCheck getCheck()
	{
		return check;
	}
	@Override
	public EventLog getLog()
	{
		return log;
	}
}
