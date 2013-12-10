package simcity.jesusrestaurant;

import simcity.role.JobRole;
import simcity.interfaces.Person;
import simcity.jesusrestaurant.gui.JesusHostGui;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class JesusHostRole extends JobRole {
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
	boolean working, start = false;
	public JesusHostGui jesusHostGui = null;
	private JesusCashierRole cashier = null;
	public JesusHostRole() {
		super();
		
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	/*hack to set waiters*/
	public void setWaiters(JesusWaiterRole w, int s) {
		waiters.add(new myWaiter(w,s));
	}
	public void setCashier(JesusCashierRole ch) {
		cashier = ch;
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
	
	public void setPerson(Person p) {
		super.setPerson(p);
		name = p.getName();
	}
	// Messages

	
	public void msgStartShift() {
		working = true;
		start = true;
		stateChanged();
	}

	public void msgEndShift() {
		working = false;
		stateChanged();
	}
	public void msgOpen() {
		open = true;
		stateChanged();
	}
	public void msgClosed() {
		open = false;
		stateChanged();
	}
	public void msgGoingOnBreak(JesusWaiterRole wait) {
		synchronized(waiters){
		for(myWaiter w: waiters) {
			if(w.waiter.getName().equals(wait.getName())) {
				w.state = wState.askingBreak;
				stateChanged();
			}
		}
		}
	}
	public void msgReturningToWork(JesusWaiterRole wait) {
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
	public void msgIWantFood(JesusCustomerRole cust) {
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
	
	public void left() {
		person.msgLeftDestination(this);
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if(!working) {
			leaveRestaurant();
			return true;
		}
		if(start) {
			startWork();
			return true;
		}
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

	private void startWork() {
		jesusHostGui.work();
		person.businessIsClosed(getJobLocation(), false);
	}
	private void leaveRestaurant() {
		jesusHostGui.leave();
	}
	private void closed() {
		person.businessIsClosed(getJobLocation(), true);
		print("Sorry, we're closed. Please come again later when we are open.");
		while(!waitingCustomers.isEmpty()) {
			waitingCustomers.get(0).customer.msgWereClosed();
			waitingCustomers.remove(waitingCustomers.get(0));
		}
		cashier.msgPayEmployees(waiters);
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
		jesusHostGui = gui;
	}

	public JesusHostGui getGui() {
		return jesusHostGui;
	}

	public class myWaiter {
		JesusWaiterRole waiter;
		int numOfCust;
		int salary;
		String name;
		wState state;
		
		public myWaiter(JesusWaiterRole w, int s) {
			this.waiter = w;
			salary = s;
			numOfCust = 0;
			name = w.getName();
			state = wState.working;
		}
	}
	
	private class Table {
		JesusCustomerRole occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(JesusCustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		JesusCustomerRole getOccupant() {
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
		JesusCustomerRole customer;
		boolean asked;
		
		myCustomer(JesusCustomerRole c) {
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