package simcity.cherysrestaurant.test.mock;

import java.util.List;

import simcity.cherysrestaurant.CherysCashierCheck;
import simcity.cherysrestaurant.interfaces.*;

public class MockCherysWaiter extends Mock implements CherysWaiter
{
	/**
	 * Reference to the agent under test that can be set by the unit test.
	 */
	public CherysCashier cashier;
	public CherysCashierCheck check;
	public EventLog log = new EventLog();

	public MockCherysWaiter(String name)
	{
		super(name);
	}

	@Override
	public void msgPleaseSeatCustomer(CherysCustomer c, int table)
	{
		log.add(new LoggedEvent("Received msgPleaseSeatCustomer from host. Table = " + table));
	}
	@Override
	public void msgReadyToOrder(CherysCustomer c)
	{
		log.add(new LoggedEvent("Received msgReadyToOrder from customer."));
	}
	@Override
	public void msgHereIsMyOrder(CherysCustomer c, String choice)
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
	public void msgDoneEating(CherysCustomer c)
	{
		log.add(new LoggedEvent("Received msgDoneEating from customer."));
	}
	@Override
	public void msgHereIsCheck(CherysCashierCheck ch)
	{
		log.add(new LoggedEvent("Received msgHereIsCheck from cashier. Order = " + ch.order + ". Total = " + ch.total));
		System.out.println(name + " recieved HereIsCheck");
		check = ch;
	}
	@Override
	public void msgLeavingRestaurant(CherysCustomer c)
	{
		log.add(new LoggedEvent("Received msgLeavingRestaurant from customer."));
	}
	@Override
 	public void msgGoOnBreak(boolean tf)
	{
		log.add(new LoggedEvent("Received msgGoOnBreak from host. Allowed? = " + tf));
	}
	@Override
	public CherysCashierCheck getCheck()
	{
		return check;
	}
	@Override
	public EventLog getLog()
	{
		return log;
	}
}
