package simcity.cherysrestaurant;

import simcity.agent.Agent;
import simcity.cherysrestaurant.gui.CherysWaiterGui;
import simcity.cherysrestaurant.interfaces.*;
import simcity.mock.EventLog;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */
public class CherysWaiterRole extends Agent implements CherysWaiter
{
	private Semaphore atLobby = new Semaphore(0, true);
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atKitchen = new Semaphore(0, true);
	private Semaphore onBreak = new Semaphore(0, true);
	
	private String name;
	private List<MyCustomer> customers = new ArrayList<MyCustomer>();
	private class MyCustomer
	{
		CherysCustomer c;
		int table;
		String choice;
		CustomerState state;
		MyCustomer(CherysCustomer c, int t)
		{
			this.c = c;
			table = t;
			state = CustomerState.waiting;
		}
	}
	enum CustomerState
	{
		waiting,
		seated,
		askedToOrder,
		preparingToOrder,
		ordering,
		ordered,
		beingServed,
		eating,
		waitingForCheck,
		done,
		gone;
	}
	private List<Order> orders = new ArrayList<Order>();
	private class Order
	{
		String choice;
		int menuNumber;
		int table;
		boolean invalid = false;
		Order(String c, int t)
		{
			choice = c;
			table = t;
		}
	}
	Order currentOrder = null;
	CherysHost host;
	CherysCook cook;
	CherysCashier cashier;
	List<CherysCashierCheck> checks = new ArrayList<CherysCashierCheck>();
	Map<Integer, CherysWaiterFood> menu;
	private List<String> foodsOutOf = new ArrayList<String>();
	
	private enum Command
	{
		noCommand,
		getCustomer,
		serveCustomer,
		giveOrderToCook,
		askForOrder,
		pickUpOrder,
		deliverCheck,
		clearTable,
		goOnBreak;
	}
	private Command command = Command.noCommand;

	private double priceSteak = 15.99;
	private double priceChicken = 10.99;
	private double priceSalad = 5.99;
	private double pricePizza = 8.99;

	enum AgentState
	{
		toLobby,
		toTable,
		toKitchen,
		onBreak;
	}
	AgentState state = null;
	private boolean tired = false;
	private boolean permissionToBreak = false;
	private boolean denied = false;
	Timer timer = new Timer();
	
	public CherysWaiterGui waiterGui = null;

	public EventLog log = new EventLog();

	/**
	 * Constructor for the WaiterAgent class
	 * @param name name of the waiter agent
	 * @param h    reference to the host agent
	 * @param c    reference to the cook agent
	 */
	public CherysWaiterRole(String name, CherysHost h, CherysCook c, CherysCashier cash)
	{
		super();
		this.name = name;
		host = h;
		cook = c;
		cashier = cash;
		menu = new HashMap<Integer, CherysWaiterFood>();
		menu.put(0, new CherysWaiterFood("Steak", "ST", priceSteak));
		menu.put(1, new CherysWaiterFood("Chicken", "CK", priceChicken));
		menu.put(2, new CherysWaiterFood("Salad", "SA", priceSalad));
		menu.put(3, new CherysWaiterFood("Pizza", "PZ", pricePizza));
	}

	public String getName()
	{
		return name;
	}
	public List getCustomers()
	{
		return customers;
	}
	
	// Messages
	public void msgPleaseSeatCustomer(CherysCustomer c, int table) //* called from Host.assignCustomer
	{
		Do("recieved msgPleaseSeatCustomer");
		customers.add(new MyCustomer(c, table));
		stateChanged();
	}
	public void msgReadyToOrder(CherysCustomer c) //* called from Customer.flagWaiter
	{
		Do("recieved msgReadyToOrder");
		do
		{
			try
			{
				for(MyCustomer customer : customers)
				{
					if(customer.c == c)
					{
						customer.state = CustomerState.askedToOrder;
						stateChanged();
						break;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
	}
	public void msgHereIsMyOrder(CherysCustomer c, String choice) //* called from Customer.placeOrder
	{
		Do("recieved msgHereIsMyOrder");
		do
		{
			try
			{
				for(MyCustomer customer : customers)
				{
					if(customer.c == c)
					{
						customer.choice = choice;
						customer.state = CustomerState.ordering;
						stateChanged();
						break;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
	}
	public void msgOutOfFood(String choice, int table, List<String> foo)
	{
		Do("recieved msgOutOfFood");
		Order temp = new Order(choice, table);
		do
		{
			try
			{
				for(int i = 0; i < menu.size(); i++)
				{
					if(menu.get(i).name == choice)
					{
						temp.menuNumber = i;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		temp.invalid = true;
		orders.add(temp);
		foodsOutOf = foo;
		stateChanged();
	}
	public void msgOrderReady(String choice, int table, List<String> foo) //* called from Cook.alertWaiter
	{
		Do("recieved msgOrderReady");
		Order temp = new Order(choice, table);
		do
		{
			try
			{
				for(int i = 0; i < menu.size(); i++)
				{
					if(menu.get(i).name == choice)
					{
						temp.menuNumber = i;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		orders.add(temp);
		foodsOutOf = foo;
		stateChanged();
	}
	public void msgDoneEating(CherysCustomer c) //* called from Customer.done
	{
		Do("recieved msgDoneEating");
		do
		{
			try
			{
				for(MyCustomer customer : customers)
				{
					if(customer.c == c)
					{
						customer.state = CustomerState.waitingForCheck;
						break;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		stateChanged();
	}
	public void msgHereIsCheck(CherysCashierCheck ch)
	{
		Do("recieved msgHereIsCheck");
		checks.add(ch);
		stateChanged();
	}
	public void msgLeavingRestaurant(CherysCustomer c)
	{
		Do("recieved msgLeavingRestaurant");
		do
		{
			try
			{
				for(MyCustomer customer : customers)
				{
					if(customer.c == c)
					{
						customer.state = CustomerState.done;
						break;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		stateChanged();
	}
 	public void msgGoOnBreak(boolean tf)
	{
		if(tf)
		{
			Do("Woo! I get a break!");
			permissionToBreak = true;
			stateChanged();
		}
		else
		{
			Do("Aww, no break");
			denied = true;
		}
		stateChanged();
	}

	public void msgGotTired() //* called from animation
	{
		Do("I'm tired");
		tired = true;
		stateChanged();
	}
	public void msgAtLobby() //* called from animation
	{
		atLobby.release();
	}
	public void msgAtTable() //* called from animation
	{
		atTable.release();
	}
	public void msgAtKitchen() //* called from animation
	{
		atKitchen.release();
	}
	public void msgOffBreak() //* called from animation
	{
		permissionToBreak = false;
		onBreak.release();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction()
	{
		if(tired)
		{
			askForBreak();
		}
		if(denied)
		{
			Do("DENIED");
			doWorkThroughThePain();
			return true;
		}
		if(state == AgentState.onBreak)
		{
			offBreak();
			return true;
		}
		if(state == AgentState.toLobby)
		{
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.waiting && command == Command.getCustomer)
						{
							state = null;
							command = Command.noCommand;
							seatCustomer(customer);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
		}
		else if(state == AgentState.toTable)
		{
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.ordered && command == Command.askForOrder)
						{
							state = null;
							command = Command.noCommand;
							askForOrder(customer);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.beingServed && command == Command.serveCustomer)
						{
							state = null;
							command = Command.noCommand;
							serveCustomer(customer);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.askedToOrder && command == Command.askForOrder)
						{
							state = null;
							command = Command.noCommand;
							askForOrder(customer);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.waitingForCheck && command == Command.deliverCheck)
						{
							state = null;
							command = Command.noCommand;
							do
							{
								try
								{
									for(CherysCashierCheck check : checks)
									{
										if(check.table == customer.table)
										{
											giveCustomerCheck(customer, check);
											return true;
										}
									}
								}
								catch(ConcurrentModificationException cme)
								{
									continue;
								}
							}
							while(false);
							Do("Oh no! What happened to the check?!");
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.done && (command == Command.askForOrder || command == Command.clearTable))
						{
							state = null;
							command = Command.noCommand;
							tableAvailible(customer, customer.table);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
		}
		else if(state == AgentState.toKitchen)
		{
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.ordering && command == Command.giveOrderToCook)
						{
							state = null;
							command = Command.noCommand;
							giveOrderToCook(customer);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.ordered && command == Command.pickUpOrder)
						{
							do
							{
								try
								{
									for(Order order : orders)
									{
										if(order.choice == customer.choice && order.table == customer.table)
										{
											if(order.invalid)
											{
												orders.remove(order);
												state = AgentState.toTable;
												command = Command.askForOrder;
												doGoToTable(customer.table);
												return true;
											}
											else
											{
												state = AgentState.toTable;
												command = Command.serveCustomer;
												doServeOrder(customer, order, customer.table);
												return true;
											}
										}
									}
								}
								catch(ConcurrentModificationException cme)
								{
									continue;
								}
							}
							while(false);
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
		}
		else
		{
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.waiting && command == Command.noCommand)
						{
							command = Command.getCustomer;
							state = AgentState.toLobby;
							doGoToLobby(customer.c);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.askedToOrder && command == Command.noCommand)
						{
							command = Command.askForOrder;
							state = AgentState.toTable;
							doGoToTable(customer.table);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.ordering && command == Command.noCommand)
						{
							command = Command.giveOrderToCook;
							state = AgentState.toKitchen;
							doGoToKitchen(false);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.ordered && command == Command.noCommand)
						{
							do
							{
								try
								{
									for(Order order : orders)
									{
										if(order.choice == customer.choice && order.table == customer.table)
										{
											command = Command.pickUpOrder;
											state = AgentState.toKitchen;
											doGoToKitchen(true);
											return true;
										}
									}
								}
								catch(ConcurrentModificationException cme)
								{
									continue;
								}
							}
							while(false);
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.waitingForCheck && command == Command.noCommand)
						{
							command = Command.deliverCheck;
							state = AgentState.toTable;
							askForCheck(customer.table);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state == CustomerState.done && command == Command.noCommand)
						{
							command = Command.clearTable;
							state = AgentState.toTable;
							doGoToTable(customer.table);
							return true;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
		}
		if(permissionToBreak)
		{
			Do("Going on a break?");
			boolean doneServing = true;
			do
			{
				try
				{
					for(MyCustomer customer : customers)
					{
						if(customer.state != CustomerState.gone)
						{
							doneServing = false;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			if(doneServing)
			{
				Do("Going on a break!");
				command = Command.goOnBreak;
				state = AgentState.onBreak;
				doGoOnBreak();
				return true;
			}
		}
		return false;
	}

	// Actions
	/**
	 * Escorts customer to table
	 * @param mc customer being escorted
	 */
	private void seatCustomer(MyCustomer mc)
	{
		Do("Seating customer");
		mc.state = CustomerState.seated;
		mc.c.msgFollowMe(this, menu, foodsOutOf);
		doSeatCustomer(mc.c, mc.table);
	}
	/**
	 * Takes customer's order to cook
	 * @param mc customer that ordered
	 */
	private void giveOrderToCook(MyCustomer mc)
	{
		Do("Giving order to cook");
		mc.state = CustomerState.ordered;
		cook.msgCookThis(this, mc.choice, mc.table);
		stateChanged();
	}
	/**
	 * Bring customer the prepared meal that they ordered
	 * @param o  order that was prepared
	 * @param mc customer that ordered it
	 */
	private void serveCustomer(MyCustomer mc)
	{
		Do("Serving order to customer (and requesting check be made)");
		orders.remove(currentOrder);
		mc.state = CustomerState.eating;
		mc.c.msgOrderServed(currentOrder.choice);
		cashier.msgProduceCheck(this, currentOrder.choice, currentOrder.table);
		currentOrder = null;
		stateChanged();
	}
	private void askForCheck(int table)
	{
		Do("Asking for customer's check at table " + table);
		cashier.msgGiveCheck(this, table);
		doGoToTable(table);
	}
	private void giveCustomerCheck(MyCustomer mc, CherysCashierCheck ch)
	{
		Do("Here is your check. Have a good night!");
		mc.state = null;
		mc.c.msgHereIsCheck(ch);
		checks.remove(ch);
		stateChanged();
	}
	/**
	 * Alerts the host that the customer has left and the table is free
	 * @param mc    customer that left
	 * @param table table that is now free
	 */
	private void tableAvailible(MyCustomer mc, int table)
	{
		Do("Clearing empty table");
		mc.state = CustomerState.gone;
		customers.remove(mc);
		host.msgTableFree(table, this, mc.c);
		stateChanged();
	}
	/**
	 * Ask the customer what their order is
	 * @param mc customer being asked to order
	 */
	private void askForOrder(MyCustomer mc)
	{
		Do("What would you like to eat?");
		mc.state = CustomerState.preparingToOrder;
		Do("We're out of " + foodsOutOf.size() + " foods");
		mc.c.msgWhatIsYourOrder(foodsOutOf);
	}
	private void askForBreak()
	{
		Do("May I go on break?");
		tired = false;
		host.msgMayIGoOnBreak(this);
		stateChanged();
	}
	private void offBreak()
	{
		state = null;
		command = Command.noCommand;
		host.msgBackFromBreak(this);
		stateChanged();
	}
	
	//Animation
	private void doGoToLobby(CherysCustomer c)
	{
		waiterGui.doGoToCustomer(c.getGui());
		try
		{
			atLobby.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	private void doGoToTable(int t)
	{
		waiterGui.doGoToTable(t);
		try
		{
			atTable.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	private void doSeatCustomer(CherysCustomer c, int t)
	{
		waiterGui.doSeatCustomer(((CherysCustomerRole)c).getGui(), t);
		try
		{
			atTable.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	private void doServeOrder(MyCustomer c, Order o, int t)
	{
		c.state = CustomerState.beingServed;
		currentOrder = o;
		orders.remove(o);
		waiterGui.doServeOrder(menu.get(currentOrder.menuNumber).type, t);
		try
		{
			atTable.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	private void doGoToKitchen(boolean pickup)
	{
		waiterGui.doGoToKitchen();
		try
		{
			atKitchen.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	private void doWorkThroughThePain()
	{
		denied = false;
		waiterGui.doWorkThroughThePain();
		stateChanged();
	}
	private void doGoOnBreak()
	{
		waiterGui.doGoOnBreak();
		try
		{
			onBreak.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	//utilities
	public void setGui(CherysWaiterGui gui)
	{
		waiterGui = gui;
	}
	public CherysWaiterGui getGui()
	{
		return waiterGui;
	}
	public CherysCashierCheck getCheck()
	{
		return checks.get(0);
	}
	public EventLog getLog()
	{
		return log;
	}
}