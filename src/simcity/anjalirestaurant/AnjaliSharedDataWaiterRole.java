package simcity.anjalirestaurant;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simcity.anjalirestaurant.AnjaliHostRole.Table;
import simcity.anjalirestaurant.gui.AnjaliWaiterGui;
import simcity.anjalirestaurant.interfaces.AnjaliCustomer;
import simcity.anjalirestaurant.interfaces.AnjaliWaiter;
//import restaurant.Customer.CustomerEvent;
//import restaurant.Customer.CustomerState;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios

public class AnjaliSharedDataWaiterRole extends Agent implements AnjaliWaiter{
	static final int NTABLES = 3;//a global for the number of tables.
	
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	//public List<Customer> waitingCustomers
	//= new ArrayList<Customer>();
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	//boolean hostIsBack;
	
	private String name;
	
	private Semaphore atTable = new Semaphore(0,true);
	private int yPos;
	boolean wantsBreak = false;
	private class myCustomer{
		 AnjaliCustomer c;
		private  Table t;
		 CustomerState s;
		 String name;
		
		myCustomer(AnjaliCustomer c, Table t, CustomerState s){
			this.c = c;
			this.t = t;
			this.s = s;
			this.name = c.getName();
			
		}
		public CustomerState getCustomerState() {
			return s;
		}
		public void setCustomerState(CustomerState s) {
			this.s = s;
		}
		
		String choice; 	
		
	
		
		double checkAmount = 0.00;
	}
	
	public enum CustomerState {doingNothing, waitingToSit, seated, readyToOrder, ordering, ordered, orderAgain, orderedAgain, orderingAgain, waitingForFood, OrderIsReady, served, needsCheck, paying, paid, receivedCheck, leaving};
	public enum CustomerEvent {none, seated, readyToOrder, ordering, ordered, orderAgain, orderedAgain, waitingForFood, OrderIsReady, served, readingCheck, leaving}
	private CustomerState state = CustomerState.doingNothing;
	private CustomerEvent event = CustomerEvent.none;
	public AnjaliWaiterGui waiterGui;
	public enum WaiterState{normal, wantsBreak, pendingBreak, breakAccepted, onBreak, breakDenied, offBreak};
	private WaiterState waiterState = WaiterState.normal;
	Timer timer = new Timer();
	
	public void setGui(AnjaliWaiterGui gui) {
		waiterGui = gui;
	}

	public AnjaliWaiterGui getGui() {
		return waiterGui;
	}

	private AnjaliHostRole host;
	
	private AnjaliCookRole cook;
	
	private CashierAgent cashier;
	
	//private Semaphore x = new Semaphore(0);
	
	List<myCustomer> customers = new ArrayList<myCustomer>();
	
	//public HostGui hostGui = null;

	public AnjaliSharedDataWaiterRole(String name) {
		
		this.name = name;
		
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void setHost(AnjaliHostRole host)
	{
		this.host = host;
	}
	
	public void setCashier(CashierAgent cashier){
		this.cashier = cashier;
	}
	public AnjaliHostRole getHost()
	{
		return host;
	}
	public void setCook(AnjaliCookRole cook)
	{
		this.cook = cook;
	}
	public AnjaliCookRole getCook()
	{
		return cook;
	}
	//public List getWaitingCustomers() {
	//	return waitingCustomers;
	//}

	//public Collection getTables() {
	//	return tables;
	//}
	

	
		private final Map<String, Double> menu;{
		menu = new HashMap<String, Double>();	
			menu.put("Steak", 15.99);
			menu.put("Chicken", 10.99);
			menu.put("Salad", 5.99);
			menu.put("Pizza", 8.99);
			
			}
		
		public Map<String, Double> getMenu(){
			return menu;
		}
		
		
		
		
			
	
	
	//////////// MESSAGES MESSAGES MESSAGES MESSAGES ///////////
		public void msgAtTable(){//from animation
			atTable.release();
			
			//Do("released from table");	
		}
	public void msgSitThisCustomer(AnjaliCustomer c, Table t){
		
		Do("Waiter received message from host to sit" + c);
	
		customers.add(new myCustomer(c, t, CustomerState.waitingToSit));

		stateChanged();
		
			
	}
	
	
	
	
	public void msgReadyToOrder(AnjaliCustomer c){
	
		for(myCustomer j : customers){
			if(j.c == c){
				j.s = CustomerState.readyToOrder;
				Do("Waiter received message ready to order");
				stateChanged();

			}
		}
}
			
		
	
	
	public void msgHereIsMyChoice(AnjaliCustomer c, String choice){
		for (myCustomer h : customers){
			if(h.c == c){
				h.choice = choice;
				h.s = CustomerState.ordered;
				stateChanged();
			}
		}
	}
	
	public void msgNoMoreFood(Order o){
		//Cook notifies waiter when selcted choice is unavailable 
		for(myCustomer x : customers){
			if(x.choice == o.choice){
				x.s = CustomerState.orderAgain;
				Do("customer state is orderAgain");
			}
		}
	}
	
	public void msgRemoveItem(String food){
		Do("Waiter has received message from cook that " + food + "is no longer available in markets.");
		for(Map.Entry<String, Double> entry : menu.entrySet()){
			if(entry.getKey().equals(food)){
				menu.remove(entry.getKey());
				Do("" + food + "has been removed from menu.");
				Do("The new menu is: " + menu);
				
			}
		}
	}
	
	public void msgOrderIsReady(int tableNumber){

		for(myCustomer c : customers){
			if(c.t.getTableNumber() == tableNumber){
				Do("waiter received order for " + c.c.getName() + "for" + c.choice);
				c.s = CustomerState.OrderIsReady;
				stateChanged();
				
			}
		}
		
	}
	
	public void msgReadyForCheck(AnjaliCustomer c){
		for(myCustomer z : customers){
			if(z.c == c){
				z.s = CustomerState.needsCheck;
				Do("Waiter received message from customer to make check.");
				stateChanged();
			}
		}
	}
	
	public void msgHereIsCheck(int tn, double amount){
		for(myCustomer z : customers){
			if(z.t.getTableNumber() == tn){
				z.checkAmount = amount;
				z.s = CustomerState.paid;
				Do("Waiter received check from cashier, will give it to customer.");
				stateChanged();
			}
		}
	}
	public void msgLeavingTable(AnjaliCustomer c){
		for(myCustomer j : customers){
			if (j.c == c){
				j.s = CustomerState.leaving;	
				Do("Customer state is leaving");
			}
			stateChanged();
		}
	}
	
	public void msgWantsBreak(){
		//The waiter can go on break, scenario 3
		waiterState = WaiterState.wantsBreak;
		Do("Waiter wants to go on break");
		stateChanged();
	}
	
	public void msgBreakAllowed(){
		//Break can be either accepted or denied (denied break is done in host only)
		waiterState = WaiterState.breakAccepted;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
	try{
		if(waiterState == WaiterState.wantsBreak){
			wantsBreak();
			waiterState = WaiterState.pendingBreak;
			//return true;
		}
		
		if(waiterState == WaiterState.breakAccepted){
			goOnBreak();
			waiterState = WaiterState.onBreak;
			//return true;
		}
		
		//Deals with waiting to sit
		
		
		
		for(myCustomer c : customers){
			
			if(c.getCustomerState() == CustomerState.waitingToSit){
				Do("calling seatCustomer on customer" + c.c.getName());
				seatCustomer(this, c.c,c.t);
				c.s = CustomerState.seated;
				return true;
			}
			
		for(myCustomer j : customers){	
			if(j.getCustomerState() == CustomerState.readyToOrder){
				Do("taking Order of customer" + j.c.getName());
				takeOrder(j);
				return true;
				
				//c.s = CustomerState.ordering;
				
			}
		}
		
		for(myCustomer k : customers){
			if(k.getCustomerState() == CustomerState.ordered){
				k.s = CustomerState.ordered;
				placeOrder(k);
				return true;
				
			}
		}
		
		for(myCustomer z : customers){
			//Waiter takes customer's order again if the food is unavailable
			if(z.getCustomerState() == CustomerState.orderAgain){
				z.s = CustomerState.orderingAgain;
				takeOrderAgain(z);
				return true;
			}
		}
		
		for(myCustomer y : customers){
			if(y.getCustomerState() == CustomerState.orderedAgain){
				placeOrder(y);
				return true;
				
			}
		}
		
		for(myCustomer x : customers){
			if(x.getCustomerState() == CustomerState.OrderIsReady){
				pickUpOrder(x);
				deliverFood(x);
				return true;
				
			}
		}
		for(myCustomer w : customers){
			//When the customer is done eating, the waiter asks cashier to compute check
			if(w.getCustomerState() == CustomerState.needsCheck){
				makeCheck(w);
				w.s = CustomerState.paying;
				return true;
				
			}
		}
		for(myCustomer r : customers){
			//When waiter receives check from cashier, he deilvers it to the customer
			if(r.getCustomerState() == CustomerState.paid){
				deliverCheck(r);
				r.s = CustomerState.receivedCheck;
				return true;
			}
		}
		for(myCustomer q : customers){
			
			if(q.getCustomerState() == CustomerState.leaving){
				Do("setting free table");
				setFreeTable(q);
				//return true;
				
			}
			
					
					}
				//return true;
		
			
			
		
		
			
		}
		return false;
		
	}
	catch(ConcurrentModificationException e){
		return false;
	}
		
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(AnjaliWaiter w, AnjaliCustomer c, Table t) {
		
		Do("Waiter seating customer" + c);
		
	
		//waiterGui.DoLeaveCustomer();
	
		waiterGui.DoGoToWaitingCustomer();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		waiterGui.DoBringToTable((AnjaliCustomerRole)c, t.getXPosTable(), t.getYPosTable());		

		c.msgFollowMeToTable(t.getXPosTable(), t.getYPosTable(), getMenu(), this);

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Do("leaving customer");
		waiterGui.DoGoHomePosition();
		
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	private void takeOrder(myCustomer c){

		waiterGui.DoGoToCustomer(c.t.getXPosTable(), c.t.getYPosTable());
		//Do("works");
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		c.c.msgWhatWouldYouLike();
		
		waiterGui.DoGoHomePosition();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Do("released1");
		 
		 
		
	}
	
	private void takeOrderAgain(myCustomer c){
		waiterGui.DoGoToCustomer(c.t.getXPosTable(), c.t.getYPosTable());
		//Do("works");
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		c.c.msgNoMoreFood();
		
		waiterGui.DoGoHomePosition();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Do("released1");
		
	}
	
	private void placeOrder(myCustomer c){
		Do("giving order to cook");
		waiterGui.DoGoToCookArea();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		cook.msgHereIsOrder(c.name, c.choice, c.t.getTableNumber(), this);
		
		c.s = CustomerState.waitingForFood;
		
		waiterGui.DoGoHomePosition();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	private void pickUpOrder(myCustomer c){
		waiterGui.DoGoToPlateArea();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Do("picked up order");
	}
	
	
	private void deliverFood(myCustomer c){
		waiterGui.msgDrawFoodChoice(c.choice);
		
		waiterGui.DoGoToCustomer(c.t.getXPosTable(), c.t.getYPosTable());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.s = CustomerState.served;
		c.c.msgHereIsYourFood();
		waiterGui.msgDrawFoodChoice("");
		
		waiterGui.DoGoHomePosition();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void makeCheck(myCustomer c){
		cashier.msgMakeCheck(c.c, c.choice, c.t.getTableNumber(), this);
		
	}
	
	private void deliverCheck(myCustomer c){
		Do("Waiter is delivering check to customer");
		c.c.msgPayCheck(c.checkAmount);
	}
	
	private void wantsBreak(){
		//Waiter asks host if break can be taken
		host.msgWantBreak(this);
	}
	private void goOnBreak(){
		//Waiter goes on break
		waiterGui.DoGoOnBreak();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Do("Waiter is taking break.");
		
		
		
		timer.schedule(new TimerTask(){
		public void run(){
			Do("Waiter is done with break.");
			returnToWork();
			waiterState = WaiterState.normal;
			//Waiter correctly returns to work after 20 seconds
		}
		
	}, 20000);
		
	}
	public void returnToWork(){
		host.msgWaiterBreakDone(this);
		waiterGui.DoGoHomePosition();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void setFreeTable(myCustomer c){
		
				c.t.setUnoccupied();
				host.msgTableIsFree(c.t, this);
				c.s = CustomerState.doingNothing;
			
			
		}

	
	
	//utlities

	
	
public void setYPos(int yPosition){
	this.yPos = yPosition;
}



}
