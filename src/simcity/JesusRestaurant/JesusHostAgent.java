package simcity.JesusRestaurant;

import agent.Agent;
import restaurant.gui.HostGui;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class JesusHostAgent extends Agent {
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<myCustomer> waitingCustomers
	= Collections.synchronizedList(new ArrayList<myCustomer>());
	public List<myWaiter> waiters = Collections.synchronizedList(new ArrayList<myWaiter>());
	public enum wState {working, askingBreak, waitingResponse, onBreak};
	public enum AgentState {none, checking};
	boolean open = false;
	AgentState state = AgentState.none;
	int waitersOnBreak = 0;
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	
	public JesusHostGui hostGui = null;

	public JesusHostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	/*hack to set waiters*/
	public void setWaiters(JesusWaiterAgent w) {
		waiters.add(new myWaiter(w));
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	// Messages

	public void msgOpen() {
		open = true;
		stateChanged();
	}
	public void msgClosed() {
		open = false;
		stateChanged();
	}
	public void msgGoingOnBreak(JesusWaiterAgent wait) {
		synchronized(waiters){
		for(myWaiter w: waiters) {
			if(w.waiter.getName().equals(wait.getName())) {
				w.state = wState.askingBreak;
				stateChanged();
			}
		}
		}
	}
	public void msgReturningToWork(JesusWaiterAgent wait) {
		synchronized(waiters){
		for(myWaiter w: waiters) {
			if(w.waiter.getName().equals(wait.getName())) {
				w.state = wState.working;
				stateChanged();
			}
		}
		}
		waitersOnBreak--;
	}
	
	public void msgLeaving(String cName) {
		myCustomer cRemove = null;
		synchronized(waitingCustomers){
		for(myCustomer c: waitingCustomers) {
			if(c.customer.getName().equals(cName)) {
				cRemove = c;
			}
		}
		}
		waitingCustomers.remove(cRemove);
	}
	public void msgWaiting(String cName) {
		synchronized(waitingCustomers){
		for(myCustomer c: waitingCustomers) {
			if(c.customer.getName().equals(cName)) {
			}
		}
		}
	}
	public void msgIWantFood(JesusCustomerAgent cust) {
		waitingCustomers.add(new myCustomer(cust));
		state = AgentState.checking;
		stateChanged();
	}

	public void msgTableFree(int tableNum, String waiterName) {
		for (Table table : tables) {
			if (table.tableNumber == tableNum) {
				print("Table " + table.tableNumber + " cleared");
				table.setUnoccupied();
				stateChanged();
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(!waitingCustomers.isEmpty()) {
			if(open == false) {
				closed();
				return true;
			}
		}
		try{
		for (myWaiter w: waiters) {
			if(w.state == wState.askingBreak) {
				w.state = wState.waitingResponse;
				confirmBreak(w);
				return true;
			}
		}
		}catch(ConcurrentModificationException e){
			return true;
		}
		try{
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty()) {
					assignWaiter(waitingCustomers.get(0), table);//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
		}catch(ConcurrentModificationException e){
			return true;
		}
		if(tablesFull()){
			if(!waitingCustomers.isEmpty() && state == AgentState.checking) {
				try{
				for(myCustomer c: waitingCustomers) {
					if(!c.asked) {
						noSeats(c);
						return true;
					}
				}
				} catch(ConcurrentModificationException e) {
					return true;
				}
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void closed() {
		print("Sorry, we're closed. Please come again later when we are open.");
		while(!waitingCustomers.isEmpty()) {
			waitingCustomers.get(0).customer.msgWereClosed();
			waitingCustomers.remove(waitingCustomers.get(0));
		}
	}
	private void confirmBreak(myWaiter w) {
		if(waiters.size() - waitersOnBreak <= 1) {
			print(w.waiter.getName() + "'s break DENIED!");
			w.state = wState.working;
			w.waiter.msgAnswer(false);
		}
		else {
			waitersOnBreak++;
			w.state = wState.onBreak;
			w.waiter.msgAnswer(true);
		}
	}
	private void assignWaiter(myCustomer c, Table table) {
		myWaiter assignedWaiter = waiters.get(0);
		int min = waiters.get(0).numOfCust;
		synchronized(waiters){
		for (myWaiter waiter : waiters) {
			if(waiter.numOfCust < min && waiter.state != wState.onBreak) {
				min = waiter.numOfCust;
				assignedWaiter = waiter;
			}
		}
		}
		Do("Assigning waiter " + assignedWaiter.waiter.getName());
		assignedWaiter.numOfCust++;
		c.asked = true;
		table.setOccupant(c.customer);
		waitingCustomers.remove(c);
		assignedWaiter.waiter.msgSeatCustomer(c.customer, table.tableNumber, c.customer.getName());
	}

	private void noSeats(myCustomer c) {
		c.customer.msgNoSeats();
		c.asked = true;
		print("Sorry " + c.customer.getName() + ", but tables are full. Would you like to wait?");
		state = AgentState.none;
	}
	
	//utilities

	public void setGui(JesusHostGui gui) {
		hostGui = gui;
	}

	public JesusHostGui getGui() {
		return hostGui;
	}

	private class myWaiter {
		JesusWaiterAgent waiter;
		int numOfCust;
		String name;
		wState state;
		
		myWaiter(JesusWaiterAgent w) {
			this.waiter = w;
			numOfCust = 0;
			name = w.getName();
			state = wState.working;
		}
	}
	
	private class Table {
		JesusCustomerAgent occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(JesusCustomerAgent cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		JesusCustomerAgent getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
	public static int getNTables() {
		return NTABLES;
	}
	
	public boolean tablesFull() {
		for(Table t: tables) {
			if(!t.isOccupied()) {
				return false;
			}
		}
		return true;
	}
	
	public class myCustomer {
		JesusCustomerAgent customer;
		boolean asked;
		
		myCustomer(JesusCustomerAgent c) {
			customer = c;
			asked = false;
		}
	}
	
	public String openPanel() {
		if(open) {
			return "We're Open!";
		}
		else
			return "Sorry, We're Closed.";
	}
}