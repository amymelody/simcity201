package simcity.anjalirestaurant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simcity.RestHostRole;
import simcity.anjalirestaurant.interfaces.AnjaliCustomer;
import simcity.anjalirestaurant.interfaces.AnjaliHost;
import simcity.anjalirestaurant.interfaces.AnjaliWaiter;
//import restaurant.Waiter.CustomerState;
import simcity.interfaces.Person;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios
//All requirements for v2.1 have been met

public class AnjaliHostRole extends RestHostRole implements AnjaliHost{
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	private AnjaliHostRole host;
//	private Waiter waiter;
	private AnjaliCustomer customer;
	boolean wantsBreak = false;
	private int workingWaiters = 0;
	int waiterSelection = 0;
	protected List<AnjaliCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<AnjaliCustomer>());
	
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	boolean hostIsBack;
	private String name = null;
	private Semaphore atTable = new Semaphore(0,true);
	
	private class myWaiter{
		AnjaliWaiterRole w;
		WaiterState s;
		private Table t;
		
		myWaiter(AnjaliWaiterRole w, WaiterState s){
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
	Timer waitTimer = new Timer();
	
	private boolean working;
	
	public AnjaliHostRole() {
		super();
		working = false;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 0; ix <= NTABLES; ix++) {
			tables.add(new Table(ix, (ix%4+1)*80, ((ix/4 + 1)*80) ));//how you add to a collections
		}
		hostIsBack = true;
	}

	public void setPerson(Person p){
		super.setPerson(p);
		name = person.getName();
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

	public void msgStartShift(){
		person.businessIsClosed(getJobLocation(), false);
		working = true;
		stateChanged();
	}
	
	public void msgEndShift(){
		person.businessIsClosed(getJobLocation(), true);
		working = false;
		stateChanged();
	}
	public void msgIWantFood(AnjaliCustomer cust) {
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
			
		
		
	public void msgIWillWait(final AnjaliCustomer c){
		Do("Host received message that customer will wait in restaurant, setting wait time to 30 seconds.");
		
		waitTimer.schedule(new TimerTask(){
			public void run(){
				Do("Customer is done waiting for a table to empty up");
				waitingCustomers.add(c);
				stateChanged();
				
			}
		}, 30000);
	}
	
	
	
	public void msgWantBreak(AnjaliWaiterRole w){
			findWaiter(w).s = state.wantsBreak;
			stateChanged();
			//stateChanged();
		
		
	}
	
	public void msgWaiterBreakDone(AnjaliWaiterRole w){
		if(findWaiter(w).s == state.onBreak){
			findWaiter(w).s = state.working;
			stateChanged();
		}
	}
	public void msgTableIsFree(Table t, AnjaliWaiterRole w){
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
	public boolean pickAndExecuteAnAction() {
		if(!working){
			leaveRestaurant();
			return true;
		}

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
	private void leaveRestaurant(){
		person.msgLeftDestination(this);
	}
	
	
	
	private AnjaliWaiterRole selectWaiter(){
		
	while(!myWaiters.isEmpty()){
		AnjaliWaiterRole w = myWaiters.get(waiterSelection%myWaiters.size()).w;
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
	
	private void sitCustomer(AnjaliCustomer c, Table t, AnjaliWaiterRole w)
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

	

	public void addWaiter(AnjaliWaiterRole w){
		//waiters.add(w);
		myWaiters.add(new myWaiter(w, WaiterState.working));
		workingWaiters++;
	}
	
	
	public myWaiter getWaiter(int w){
		return myWaiters.get(w);
	}
	
	public myWaiter findWaiter(AnjaliWaiterRole waiter){
	synchronized(myWaiters){
		for(myWaiter k : myWaiters){
			if(k.w == waiter)
				return k;
			}	
		return null;
		}
	}
	public class Table {
		AnjaliCustomer occupiedBy;
		int tableNumber = 0;
		int XPosTable;
		int YPosTable;

		Table(int tableNumber, int XPosTable, int YPosTable) {
			this.tableNumber = tableNumber;
			this.XPosTable = XPosTable;
			this.YPosTable = YPosTable;
		}

		
		void setOccupant(AnjaliCustomer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		AnjaliCustomer getOccupant() {
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
	
	




