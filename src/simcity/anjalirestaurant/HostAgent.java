package simcity.anjalirestaurant;

import agent.Agent;
//import restaurant.Waiter.CustomerState;
import restaurant.gui.HostGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios
//All requirements for v2.1 have been met

public class HostAgent extends Agent implements Host{
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private HostAgent host;
//	private Waiter waiter;
	private Customer customer;
	boolean wantsBreak = false;
	private int workingWaiters = 0;
	int waiterSelection = 0;
	protected List<Customer> waitingCustomers = Collections.synchronizedList(new ArrayList<Customer>());
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	boolean hostIsBack;
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	
	private class myWaiter{
		Waiter w;
		WaiterState s;
		private Table t;
		
		myWaiter(Waiter w, WaiterState s){
			this.w = w;
			this.s = s;
		}
		
		public WaiterState getWaiterState(){
			return s;
		}
		int waitersCustomers = 0;
	}
	public enum RestaurantState{notFull, full};
	private RestaurantState rs = RestaurantState.notFull;
	public enum WaiterState{doingNothing, working, wantsBreak, goingOnBreak, onBreak};
	private WaiterState state = WaiterState.doingNothing;
	
	int numCust = 0;
	private List<myWaiter> myWaiters = Collections.synchronizedList(new ArrayList<myWaiter>());
	public HostGui hostGui = null; 
	Timer waitTimer = new Timer();
	public HostAgent(String name) {
		super();
		//waiters.add(restaurant.gui.RestaurantPanel.getWaiter());
		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 0; ix <= NTABLES; ix++) {
			tables.add(new Table(ix, (ix%4+1)*80, ((ix/4 + 1)*80) ));//how you add to a collections
		}
		hostIsBack = true;
	}

	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public Collection getTables() {
		return tables;
	}
	
	
	
	
	//////// MESSAGES //////////

	public void msgIWantFood(Customer cust) {
				numCust++;
			if(numCust <= 3){
				waitingCustomers.add(cust);
				Do("host received message that customer wants food");
				stateChanged();
			}
			if(numCust > 3){
				rs = RestaurantState.full;
				this.customer = cust;
				stateChanged();
			}
					
	}
			
		
		
	public void msgIWillWait(final Customer c){
		Do("Host received message that customer will wait in restaurant, setting wait time to 30 seconds.");
		
		waitTimer.schedule(new TimerTask(){
			public void run(){
				Do("Customer is done waiting for a table to empty up");
				waitingCustomers.add(c);
				stateChanged();
				
			}
		}, 30000);
	}
	
	
	
	public void msgWantBreak(Waiter w){
			findWaiter(w).s = state.wantsBreak;
			stateChanged();
			//stateChanged();
		
		
	}
	
	public void msgWaiterBreakDone(Waiter w){
		if(findWaiter(w).s == state.onBreak){
			findWaiter(w).s = state.working;
			stateChanged();
		}
	}
	public void msgTableIsFree(Table t, Waiter w){
		t.setOccupant(null);
		numCust--;
		print("table " + t.getTableNumber() + " is free");
		findWaiter(w).waitersCustomers--;
		
		if(findWaiter(w).waitersCustomers == 0 && findWaiter(w).s == state.wantsBreak){
			findWaiter(w).s = state.goingOnBreak;
			stateChanged();
		}
		//Do("after table is free, waiters waitingCustomers size is: " + findWaiter(w).waitersCustomers);
 
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		//RULES FOR V2
		 * if there exists a customer c in waitingCustomers and there exists a t in Tables such that !t.isOccupied() and findLeastBusyWaiter()
		 * {sitCustomer(customer c, Table t, Waiter w}
		
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty() && hostIsBack) {
					seatCustomer(waitingCustomers.get(0), table, findLeastBusyWaiter());//the action
					return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		}
*/
		if(rs == RestaurantState.full){
			Do("Restaurant is full.");
			restFull();
			
		}
		
		synchronized(myWaiters){
		for(myWaiter mw : myWaiters){
			if(mw.s == state.goingOnBreak){
				decideBreak(mw);
				}
				//return true;
			}
		
		}
		
		synchronized(tables){
	for(Table table : tables){
		if(!table.isOccupied()){
			if(!waitingCustomers.isEmpty() && !myWaiters.isEmpty()){			
				sitCustomer(waitingCustomers.get(0), table, selectWaiter());
				
			}
		}
	}
		}
		return false;
		
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	/////////// ACTIONS ACTIONS ACTIONS ACTIONS ACTIONS ////////////////////
	
	private Waiter selectWaiter(){
		
	while(!myWaiters.isEmpty()){
		Waiter w = myWaiters.get(waiterSelection%myWaiters.size()).w;
		myWaiters.get(waiterSelection%myWaiters.size()).waitersCustomers++;
		Do("size of myWaiters waitingCustomers list: " + myWaiters.get(waiterSelection%myWaiters.size()).waitersCustomers);
	waiterSelection++;

		if(findWaiter(w).s == WaiterState.working){
			return w;
		}
		
		}
	return null;
	}	
	
	private void restFull(){
		customer.msgRestaurantIsFull();
		
			
		
	}
	
	private void sitCustomer(Customer c, Table t, Waiter w)
	{
		waitingCustomers.remove(c);
		t.setOccupant(c);
		print("customer removed from list");	
		print("host is giving customer to waiter to seat");
		//print("customer set at table" + t.tableNumber);
		w.msgSitThisCustomer(c,t);
		
	}

	private  void decideBreak(myWaiter mw){
		//Host correctly gives or denies breaks.
		if(myWaiters.size() > 1 && workingWaiters > 1){
			workingWaiters--;
			mw.w.msgBreakAllowed();
			mw.s = state.onBreak;
			stateChanged();
			
		}
		if(myWaiters.size() <= 1){
			Do("Sorry, you are the only waiter. You cannot have a break.");
			mw.s = state.working;
			stateChanged();
		}
		
		
	}
	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	public void addWaiter(Waiter w){
		//waiters.add(w);
		myWaiters.add(new myWaiter(w, WaiterState.working));
		workingWaiters++;
		stateChanged();
	}
	
	
	public myWaiter getWaiter(int w){
		return myWaiters.get(w);
	}
	
	public myWaiter findWaiter(Waiter waiter){
	synchronized(myWaiters){
		for(myWaiter k : myWaiters){
			if(k.w == waiter)
				return k;
			}	
		return null;
		}
	}
	public class Table {
		Customer occupiedBy;
		int tableNumber = 0;
		int XPosTable;
		int YPosTable;

		Table(int tableNumber, int XPosTable, int YPosTable) {
			this.tableNumber = tableNumber;
			this.XPosTable = XPosTable;
			this.YPosTable = YPosTable;
		}

		
		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}
		void setTableNumber()
		{
			tableNumber = -1;
		}
		int getTableNumber()
		{
			return tableNumber;
		}
		void setXPosTable()
		{
			XPosTable = 0;
		}
		int getXPosTable()
		{
			return XPosTable;
		}
		void setYPosTable()
		{
			YPosTable = 0;
		}
		int getYPosTable()
		{
			return YPosTable;
		}
		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
}
	
	




