package simcity.cherysrestaurant; 

import simcity.RestHostRole;
import simcity.agent.Agent;
import simcity.cherysrestaurant.gui.CherysRestaurantGui;
import simcity.cherysrestaurant.interfaces.*;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
public class CherysHostRole extends RestHostRole implements CherysHost
{
	public List<MyCustomer> customers = new ArrayList<MyCustomer>();
	private class MyCustomer
	{
		CherysCustomer c;
		boolean served;
		MyCustomer(CherysCustomer c)
		{
			this.c = c;
			served = false;
		}
	}
	private List<Table> tables = new ArrayList<Table>();
	private class Table
	{
		int tableNumber;
		boolean occupied;
		Table(int t)
		{
			tableNumber = t;
			occupied = false;
		}
	}
	private List<MyWaiter> waiters = new ArrayList<MyWaiter>();
	private class MyWaiter
	{
		CherysWaiter w;
		int customersAssigned;
		boolean wantsBreak;
		boolean onBreak;
		boolean onDuty;
		MyWaiter(CherysWaiter w)
		{
			this.w = w;
			customersAssigned = 0;
			wantsBreak = false;
		}
		void setOnDuty(boolean tf)
		{
			onDuty = tf;
		}
	}
	int numWaiters;
	int numTables = 5;
	
	private boolean working;

	private CherysCashier cashier;
	private CherysCook cook;
	CherysRestaurantGui gui;

	/**
	 * Constructor for HostAgent
	 * @param name agent name
	 * @param gui  reference to the main gui
	 */
	public CherysHostRole() //* called from RestaurantGui
	{
		super();

		// make some tables
		tables = new ArrayList<Table>(numTables);
		for (int ix = 1; ix <= numTables; ix++)
		{
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	public String getName()
	{
		return name;
	}
	public void setCashier(CherysCashier c)
	{
		cashier = c;
	}
	public void setCook(CherysCook c)
	{
		cook = c;
	}
	public void setWaiter(CherysWaiter w) //* called from RestaurantPanel.addPerson
	{
		waiters.add(new MyWaiter(w));
	}
	public void setGui(CherysRestaurantGui g)
	{
		gui = g;
	}
	
	// Messages
	public void msgImHungry(CherysCustomer c) //* called from Customer.alertHost
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgImHungry");
		customers.add(new MyCustomer(c));
		stateChanged();
	}
	public void msgTableFree(int t, CherysWaiter w, CherysCustomer c) //* called from Waiter.tableAvailible
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgTableFree");
		do
		{
			try
			{
				for(Table table : tables)
				{
					if(table.tableNumber == t)
					{
						table.occupied = false;
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
		do
		{
			try
			{
				for(MyWaiter waiter : waiters)
				{
					if(waiter.w == w)
					{
						waiter.customersAssigned--;
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
	public void msgMayIGoOnBreak(CherysWaiter w)
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgMayIGoOnBreak");
		do
		{
			try
			{
				for(MyWaiter waiter : waiters)
				{
					if(waiter.w == w)
					{
						waiter.wantsBreak = true;
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
	public void msgBackFromBreak(CherysWaiter w)
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgBackFromBreak");
		do
		{
			try
			{
				for(MyWaiter waiter : waiters)
				{
					if(waiter.w == w)
					{
						waiter.onBreak = false;
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
	public void msgOnDuty(CherysWaiter w, boolean tf)
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgOnDuty");
		do
		{
			try
			{
				for(MyWaiter waiter : waiters)
				{
					if(waiter.w == w)
					{
						waiter.onDuty = tf;
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
	
	@Override
	public void msgStartShift()
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgStartShift");
		person.businessIsClosed(getJobLocation(), false);
		working = true;
		cashier.msgPaySalary(person.getSalary());
		stateChanged();
	}

	@Override
	public void msgEndShift()
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgEndShift");
		person.businessIsClosed(getJobLocation(), true);
		working = false;
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction()
	{
		MyCustomer mc = null;
		Table t = null;
		do
		{
			try
			{
				for(MyCustomer customer : customers)
				{
					if(customer.served == false)
					{
						mc = customer;
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
		do
		{
			try
			{
				for(Table table : tables)
				{
					if(table.occupied == false)
					{
						t = table;
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
		if(mc != null && t != null && waiters.size() > 0)
		{
			int waiterLoad[] = new int[waiters.size()];
			int unavailible = 100; //hack for waiters on break
			do
			{
				try
				{
					for(int i = 0; i < waiters.size(); i++)
					{
						if(!(waiters.get(i).onBreak))
						{
							waiterLoad[i] = waiters.get(i).customersAssigned;
						}
						else
						{
							waiterLoad[i] = unavailible;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			int lowestLoad = 0;
			do
			{
				try
				{
					for(int i = 0; i < waiters.size(); i++)
					{
						if(waiterLoad[i] < waiterLoad[lowestLoad])
						{
							lowestLoad = i;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			assignCustomer(mc, waiters.get(lowestLoad), t);
			return true;
		}
		do
		{
			try
			{
				for(MyWaiter waiter : waiters)
				{
					if(waiter.wantsBreak)
					{
						putWaiterOnBreak(waiter.w);
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
		if(!working)
		{
			boolean allGone = true;
			do
			{
				try
				{
					for(MyWaiter waiter : waiters)
					{
						if(waiter.onDuty)
						{
							allGone = false;
						}
					}
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			if(allGone)
			{
				leaveRestaurant();
				return true;
			}
		}
		return false;
	}

	// Actions
	/**
	 * Gives a waiting customer to a free waiter and assigns them a table.
	 * Updates the customer's waiting status, the number of customers the
	 * waiter is in charge of, the waiter's busy status, and the table's
	 * occupied status.
	 * @param mc reference to the customer being seated
	 * @param mw reference to the waiter being assigned the customer
	 * @param t  the table where the customer is being seated
	 */
	private void assignCustomer(MyCustomer mc, MyWaiter mw, Table t)
	{
		mc.served = true;
		mw.customersAssigned++;
		t.occupied = true;
		mw.w.msgPleaseSeatCustomer(mc.c, t.tableNumber);
		stateChanged();
	}
	private void putWaiterOnBreak(CherysWaiter w)
	{
		int availibleWaiters = waiters.size() - 1;
		do
		{
			try
			{
				for(MyWaiter waiter : waiters)
				{
					if(waiter.onBreak)
					{
						availibleWaiters--;
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
				for(MyWaiter waiter : waiters)
				{
					if(waiter.w == w)
					{
						waiter.wantsBreak = false;
						if(availibleWaiters > 0)
						{
							waiter.onBreak = true;
							w.msgGoOnBreak(true);
							break;
						}
						else
						{
							w.msgGoOnBreak(false);
							break;
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
		stateChanged();
	}
	
	private void leaveRestaurant()
	{
		cashier.msgGoHome();
		cook.msgGoHome();
		person.msgLeftDestination(this);
	}

	//utilities


//	private class Table
//	{
//		Customer occupiedBy;
//		int tableNumber;
//
//		Table(int tableNumber)
//		{
//			this.tableNumber = tableNumber;
//		}
//
//		void setOccupant(Customer cust)
//		{
//			occupiedBy = cust;
//		}
//
//		void setUnoccupied()
//		{
//			occupiedBy = null;
//		}
//
//		Customer getOccupant()
//		{
//			return occupiedBy;
//		}
//
//		boolean isOccupied()
//		{
//			return occupiedBy != null;
//		}
//
//		public String toString()
//		{
//			return "table " + tableNumber;
//		}
//	}
}




//package restaurant;
//
//import agent.Agent;
//import restaurant.gui.HostGui;
//
//import java.util.*;
//import java.util.concurrent.Semaphore;
//
///**
// * Restaurant Host Agent
// */
////We only have 2 types of agents in this prototype. A customer and an agent that
////does all the rest. Rather than calling the other agent a waiter, we called him
////the HostAgent. A Host is the manager of a restaurant who sees that all
////is proceeded as he wishes.
//public class HostAgent extends Agent {
//	static final int NTABLES = 3;//a global for the number of tables.
//	//Notice that we implement waitingCustomers using ArrayList, but type it
//	//with List semantics.
//	public List<Customer> waitingCustomers = new ArrayList<Customer>();
//	//public Collection<Table> tables;
//	//note that tables is typed with Collection semantics.
//	//Later we will see how it is implemented
//	public List<Table> tables;
//	private boolean ready = true;
//	
//	
//	private String name;
//	private Semaphore atTable = new Semaphore(0, true);
//
//	public HostGui hostGui = null;
//
//	public HostAgent(String name)
//	{
//		super();
//
//		this.name = name;
//		// make some tables
//		tables = new ArrayList<Table>(NTABLES);
//		for (int ix = 1; ix <= NTABLES; ix++)
//		{
//			tables.add(new Table(ix));//how you add to a collections
//		}
//	}
//
//	public String getMaitreDName()
//	{
//		return name;
//	}
//
//	public String getName()
//	{
//		return name;
//	}
//
//	public List getWaitingCustomers()
//	{
//		return waitingCustomers;
//	}
//
//	public Collection getTables()
//	{
//		return tables;
//	}
//	// Messages
//
//	public void msgIWantFood(Customer cust)
//	{
//		waitingCustomers.add(cust);
//		stateChanged();
//	}
//
//	public void msgLeavingTable(Customer cust)
//	{
//		for (Table table : tables)
//		{
//			if (table.getOccupant() == cust)
//			{
//				print(cust + " leaving " + table);
//				table.setUnoccupied();
//				stateChanged();
//			}
//		}
//	}
//
//	public void msgAtTable() //from animation
//	{
//		//print("msgAtTable() called");
//		atTable.release();// = true;
//		stateChanged();
//	}
//	
//	public void msgAtDesk() //from animation
//	{
//		ready = true;
//		stateChanged();
//	}
//
//	/**
//	 * Scheduler.  Determine what action is called for, and do it.
//	 */
//	protected boolean pickAndExecuteAnAction()
//	{
//		/* Think of this next rule as:
//            Does there exist a table and customer,
//            so that table is unoccupied and customer is waiting.
//            If so seat him at the table.
//		 */
//		if(ready)
//		{
//			for (Table table : tables)
//			{
//				if (!table.isOccupied())
//				{
//					if (!waitingCustomers.isEmpty())
//					{
//						ready = false;
//						seatCustomer(waitingCustomers.get(0), table);//the action
//						return true;//return true to the abstract agent to reinvoke the scheduler.
//					}
//				}
//			}
//		}
//
//		return false;
//		//we have tried all our rules and found
//		//nothing to do. So return false to main loop of abstract agent
//		//and wait.
//	}
//
//	// Actions
//
//	private void seatCustomer(Customer customer, Table table)
//	{
//		customer.msgSitAtTable(table.tableNumber);
//		DoSeatCustomer(customer, table);
//		try
//		{
//			atTable.acquire();
//		}
//		catch (InterruptedException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		table.setOccupant(customer);
//		waitingCustomers.remove(customer);
//		hostGui.DoLeaveCustomer();
//	}
//
//	// The animation DoXYZ() routines
//	private void DoSeatCustomer(Customer customer, Table table)
//	{
//		//Notice how we print "customer" directly. It's toString method will do it.
//		//Same with "table"
//		print("Seating " + customer + " at " + table);
//		hostGui.DoBringToTable(customer, table.tableNumber); 
//
//	}
//
//	//utilities
//
//	public void setGui(HostGui gui)
//	{
//		hostGui = gui;
//	}
//
//	public HostGui getGui()
//	{
//		return hostGui;
//	}
//
//	private class Table
//	{
//		Customer occupiedBy;
//		int tableNumber;
//
//		Table(int tableNumber)
//		{
//			this.tableNumber = tableNumber;
//		}
//
//		void setOccupant(Customer cust)
//		{
//			occupiedBy = cust;
//		}
//
//		void setUnoccupied()
//		{
//			occupiedBy = null;
//		}
//
//		Customer getOccupant()
//		{
//			return occupiedBy;
//		}
//
//		boolean isOccupied()
//		{
//			return occupiedBy != null;
//		}
//
//		public String toString()
//		{
//			return "table " + tableNumber;
//		}
//	}
//}
//
