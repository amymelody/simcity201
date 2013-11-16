package simcity.cherysrestaurant.test.mock;

import java.util.List;

import simcity.cherysrestaurant.CashierCheck;
import simcity.cherysrestaurant.interfaces.*;

public class MockWaiter extends Mock implements Waiter
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public CashierCheck check;
	public EventLog log = new EventLog();

	public MockWaiter(String name)
	{
		super(name);
	}

	@Override
	public void msgPleaseSeatCustomer(Customer c, int table)
	{
		log.add(new LoggedEvent("Received msgPleaseSeatCustomer from host. Table = " + table));
	}
	@Override
	public void msgReadyToOrder(Customer c)
	{
		log.add(new LoggedEvent("Received msgReadyToOrder from customer."));
	}
	@Override
	public void msgHereIsMyOrder(Customer c, String choice)
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
	public void msgDoneEating(Customer c)
	{
		log.add(new LoggedEvent("Received msgDoneEating from customer."));
	}
	@Override
	public void msgHereIsCheck(CashierCheck ch)
	{
		log.add(new LoggedEvent("Received msgHereIsCheck from cashier. Order = " + ch.order + ". Total = " + ch.total));
		System.out.println(name + " recieved HereIsCheck");
		check = ch;
	}
	@Override
	public void msgLeavingRestaurant(Customer c)
	{
		log.add(new LoggedEvent("Received msgLeavingRestaurant from customer."));
	}
	@Override
 	public void msgGoOnBreak(boolean tf)
	{
		log.add(new LoggedEvent("Received msgGoOnBreak from host. Allowed? = " + tf));
	}
	@Override
	public CashierCheck getCheck()
	{
		return check;
	}
	@Override
	public EventLog getLog()
	{
		return log;
	}
}
