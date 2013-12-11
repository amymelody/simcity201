package simcity.jesusrestaurant;

import simcity.interfaces.Person;
import simcity.RestWaiterRole;
import simcity.role.JobRole;
import simcity.jesusrestaurant.gui.JesusWaiterGui;
import simcity.jesusrestaurant.interfaces.JesusCustomer;
import simcity.jesusrestaurant.interfaces.JesusWaiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class JesusNormalWaiterRole extends RestWaiterRole implements JesusWaiter {
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<myCustomer> myCustomers = Collections.synchronizedList(new ArrayList<myCustomer>());

	public JesusMenu menu = new JesusMenu();

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public JesusWaiterGui jesusWaiterGui = null;

	// agent correspondents
	private JesusHostRole host;
	private JesusCookRole cook;
	private JesusCashierRole cashier;

	boolean breakTime = false;
	boolean working, start;

	public enum customerState {waitingSeat, seated, readyToOrder, 
		ordering, waitingOrder, orderReady, eating, doneEating, leaving, takingPlate, orderGiven, check};

	public enum AgentState {DoingNothing, Seating, WalkingOrder, 
		TakingOrder, WalkingCook, RetakingOrder, HandingOrder, 
		WalkingPlate, HandingPlate, GettingCheck, HandingCheck, 
		WalkingClear, ClearingTable, Break, Closed, GettingPlate};

	public enum AgentEvent {none, assigned, readyTakeOrder, gotOrder, atCook, 
		noFoodChoice, noFood, orderReady, pickUp, handPlate, askForCheck, 
		receivedCheck, clean, goOnBreak, noBreak, returnToWork, BreakAccepted, atVait};

		AgentState state = AgentState.DoingNothing;
		AgentEvent event = AgentEvent.none;

		public JesusNormalWaiterRole() {
			super();
		}
		
		public void setPerson(Person p) {
			super.setPerson(p);
			name = p.getName();
		}

		public String getMaitreDName() {
			return name;
		}

		public String getName() {
			return name;
		}

		public void setHost(JesusHostRole h) {
			host = h;
		}

		public void setCook(JesusCookRole c) {
			cook = c;
		}

		public void setCashier(JesusCashierRole csh) {
			cashier = csh;
		}

		public List getMyCustomers() {
			return myCustomers;
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
		public void msgGoOnBreak() {
			breakTime = true;
			event = AgentEvent.goOnBreak;
			stateChanged();
		}
		public void msgAnswer(boolean b) {
			if(b) {
				event = AgentEvent.BreakAccepted;
			}
			else {
				event = AgentEvent.noBreak;
			}
			stateChanged();
		}
		public void msgReturnToWork() {
			event = AgentEvent.returnToWork;
			stateChanged();
		}
		public void msgSeatCustomer(JesusCustomerRole cust, int tableNum, String name) {
			System.out.println("poop");
			myCustomers.add(new myCustomer(cust, tableNum, name));
			event = AgentEvent.assigned;
			stateChanged();
		}

		public void msgReadytoOrder(String name) {
			synchronized(myCustomers){
				for(myCustomer c: myCustomers) {
					if(c.name.equals(name)) {
						c.state = customerState.readyToOrder;
					}
				}
			}
			stateChanged();
		}

		public void msgMyOrder(String name, String order) {
			synchronized(myCustomers){
				for(myCustomer c: myCustomers) {
					if(c.name.equals(name)) {
						c.foodChoice = order;
						c.state = customerState.waitingOrder;
					}
				}
			}
			event = AgentEvent.gotOrder;
			stateChanged();
		}

		public void msgOrderReady(String choice, String custName) {
			synchronized(myCustomers){
				for(myCustomer c: myCustomers) {
					if(c.name.equals(custName)) {
						c.state = customerState.orderReady;
					}
				}
			}
			stateChanged();
		}

		public void msgOutOfFood(String choice) {
			event = AgentEvent.noFoodChoice;
			menu.outOfStock(choice);
			stateChanged();
		}

		public void msgNoFood() {
			event = AgentEvent.noFood;
			stateChanged();
		}

		public void msgCheckComputed(JesusCustomer cust, int amount, String name) {
			synchronized(myCustomers){
				for(myCustomer mc: myCustomers) {
					if(mc.name.equals(name)) {
						mc.amountDue = amount;
						event = AgentEvent.receivedCheck;
						stateChanged();
					}
				}
			}
		}

		public void msgLeavingTable(String n) {
			synchronized(myCustomers){
				for(myCustomer c: myCustomers) {
					if(c.name.equals(n)) {
						c.state = customerState.leaving;
						stateChanged();
					}
				}
			}
		}

		//Animation Messages
		public void msgAtVait() {
			event = AgentEvent.atVait;
			atTable.release();
			stateChanged();
		}
		public void msgAtTable() {
			atTable.release();// = true;
			stateChanged();
		}
		public void msgFinishedGoForOrder() {
			atTable.release();
			event = AgentEvent.readyTakeOrder;
			stateChanged();
		}
		public void msgFinishedGoToCook() {
			atTable.release();
			event = AgentEvent.atCook;
			stateChanged();
		}
		public void msgFinishedPickUp() {
			atTable.release();
			event = AgentEvent.pickUp;
			stateChanged();
		}
		public void msgFinishedGoWithPlate() {
			atTable.release();
			event = AgentEvent.handPlate;
			stateChanged();
		}
		public void msgFinishedClearing() {
			atTable.release();
			event = AgentEvent.clean;
			stateChanged();
		}
		public void left() {
			person.msgLeftDestination(this);
		}

		/**
		 * Scheduler.  Determine what action is called for, and do it.
		 */
		public boolean pickAndExecuteAnAction() {
			if(!working) {
				leaveRestaurant();
				return true;
			}
			if(start) {
				startWork();
				return true;
			}
			try{
				for (myCustomer c : myCustomers) {
					if (c.state == customerState.waitingSeat && state != AgentState.Seating) {
						state = AgentState.Seating;
						goToVait();//the action
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}
			
			try{
				for (myCustomer c : myCustomers) {
					if (c.state == customerState.waitingSeat && state == AgentState.Seating) {
						state = AgentState.DoingNothing;
						seatCustomer(c);//the action
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(c.state == customerState.readyToOrder && state != AgentState.WalkingOrder) {
						state = AgentState.WalkingOrder;
						walkForOrder(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if (c.state == customerState.readyToOrder && state != AgentState.WalkingCook && state != AgentState.TakingOrder) {
						state = AgentState.TakingOrder;
						takeOrder(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(c.state == customerState.waitingOrder && state != AgentState.WalkingCook) {
						state = AgentState.WalkingCook;
						gotoCook(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			if(event == AgentEvent.atCook && state == AgentState.WalkingCook) {
				try{
					for (myCustomer c: myCustomers) {
						if(c.state == customerState.waitingOrder) {
							c.state = customerState.orderGiven;
							state = AgentState.HandingOrder;
							giveOrder(c);
						}
					}
				} catch(ConcurrentModificationException e) {
					return true;
				}
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(event == AgentEvent.noFoodChoice && c.state == customerState.orderGiven && state != AgentState.RetakingOrder) {
						state = AgentState.RetakingOrder;
						walkForOrder(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(event == AgentEvent.readyTakeOrder && state == AgentState.RetakingOrder) {
						state = AgentState.TakingOrder;
						retakeOrder(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(c.state == customerState.orderGiven && event == AgentEvent.noFood) {
						state = AgentState.Closed;
						noFood(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(c.state == customerState.orderGiven && event == AgentEvent.readyTakeOrder && state == AgentState.Closed) {
						wereClosed(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(c.state == customerState.orderReady) {
						state = AgentState.GettingPlate;
						c.state = customerState.takingPlate;
						getPlate(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(c.state == customerState.takingPlate && event == AgentEvent.pickUp && state != AgentState.WalkingPlate) {
						state = AgentState.WalkingPlate;
						walkWithPlate(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(c.state == customerState.takingPlate && event == AgentEvent.handPlate) {
						state = AgentState.HandingPlate;
						handPlate(c);
						return true;
					}
				}		
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(c.state == customerState.eating && event == AgentEvent.askForCheck && state != AgentState.GettingCheck) {
						state = AgentState.GettingCheck;
						getCheck(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if((c.state == customerState.eating || c.state == customerState.doneEating) && state == AgentState.GettingCheck && event == AgentEvent.receivedCheck) {
						state = AgentState.HandingCheck;
						handCheck(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(c.state == customerState.leaving && state != AgentState.WalkingClear) {
						state = AgentState.WalkingClear;
						walkToClear(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(c.state == customerState.leaving && state == AgentState.WalkingClear && event == AgentEvent.clean) {
						state = AgentState.ClearingTable;
						clearTable(c);
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			try{
				for (myCustomer c: myCustomers) {
					if(state == AgentState.ClearingTable) {
						state = AgentState.DoingNothing;
						event = AgentEvent.none;
						return true;
					}
				}
			} catch(ConcurrentModificationException e) {
				return true;
			}

			return false;
			//we have tried all our rules and found
			//nothing to do. So return false to main loop of abstract agent
			//and wait.
		}

		// Actions
		private void leaveRestaurant() {
			jesusWaiterGui.leave();
		}
		private void startWork() {
			jesusWaiterGui.work();
			start = false;
		}
		private void goToVait() {
			jesusWaiterGui.DoGoToVait();
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private void seatCustomer(myCustomer cust) {
			jesusWaiterGui.DoLeaveCustomer();
			//Do("Seating customer " + cust.customer);
			cust.customer.msgSitAtTable(this, cust.tableNumber, menu);
			DoSeatCustomer(cust.customer, cust.tableNumber);
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void walkForOrder (myCustomer mc) {
			DoTakeOrder(mc.customer, mc.tableNumber); //animation
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void takeOrder(myCustomer mc) {
			//Do("Taking order from " + mc.customer);
			mc.state = customerState.ordering;
			mc.customer.msgTakeOrder();
		}

		private void gotoCook(myCustomer mc) {
			//Do("Going to cook " + cook);
			DoGoToCook(mc); //animation
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mc.state = customerState.waitingOrder;
		}

		private void giveOrder(myCustomer mc) {
			cook.msgCookOrder(this, mc.foodChoice, mc.name);
		}

		private void retakeOrder(myCustomer mc) {
			//print("Out of " + mc.foodChoice);
			mc.customer.msgRetakeOrder(menu);
		}

		private void noFood(myCustomer mc) {
			DoTakeOrder(mc.customer, mc.tableNumber);
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void wereClosed(myCustomer mc) {
			//print("Sorry, but we are out of everything...");
			mc.customer.msgNoFood();
			myCustomers.remove(mc);
		}

		private void getPlate(myCustomer mc) {
			//Do("Picking up " + mc.foodChoice);
			DoGetPlate(mc);//animation
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void walkWithPlate(myCustomer mc) {
			cook.msgGotPlate(mc.foodChoice);
			//Do("Handing " + mc.foodChoice + " to " + mc.customer);
			DoHandPlate(mc);//animation
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void handPlate(myCustomer mc) {
			//Do("Enjoy your meal, " + mc.name);
			mc.customer.msgEnjoyOrder(mc.foodChoice);
			mc.state = customerState.eating;
			event = AgentEvent.askForCheck;
		}

		private void getCheck(myCustomer mc) {
			cashier.msgComputeCheck(this, mc.customer, mc.foodChoice, mc.name);
			state = AgentState.GettingCheck;
			mc.state = customerState.check;
		}

		private void handCheck(myCustomer mc) {
			//Do("Here is your check of $" + mc.amountDue + ", " + mc.name);
			mc.customer.msgHereIsCheck(mc.amountDue);
		}

		private void walkToClear(myCustomer mc) {
			DoWalkToClear(mc.tableNumber);
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void clearTable(myCustomer cust) {
			//Do("Clearing table " + cust.tableNumber);
			host.msgTableFree(cust.tableNumber, name);
			myCustomers.remove(cust);
		}

		// The animation DoXYZ() routines
		private void DoSeatCustomer(JesusCustomer customer, int tableNumber) {
			//Notice how we print "customer" directly. It's toString method will do it.
			//Same with "table"
			//print("Seating " + customer + " at Table " + tableNumber);
			jesusWaiterGui.DoBringToTable(customer, tableNumber); 

		}

		private void DoTakeOrder(JesusCustomer cust, int tableN) {
			jesusWaiterGui.DoTakeOrder(cust, tableN);
		}

		private void DoGoToCook(myCustomer mc) {
			//print("Giving order to " + cook.getName() + " (" + mc.foodChoice + ")");
			jesusWaiterGui.DoGoToCook();
		}

		private void DoGetPlate(myCustomer mc) {
			jesusWaiterGui.DoGetPlate(mc.foodChoice);
		}

		private void DoHandPlate(myCustomer cust) {
			jesusWaiterGui.DoHandPlate(cust.tableNumber, cust.foodChoice);
			//waiterGui.DoLeaveCustomer();
		}

		private void DoWalkToClear(int tableNum) {
			jesusWaiterGui.DoWalkToClear(tableNum);
		}
		//utilities

		public void setGui(JesusWaiterGui gui) {
			jesusWaiterGui = gui;
		}

		public JesusWaiterGui getGui() {
			return jesusWaiterGui;
		}

		private class myCustomer {
			JesusCustomerRole customer;
			String name;
			int tableNumber;
			customerState state;
			String foodChoice;
			int amountDue;

			myCustomer(JesusCustomerRole c, int tN, String n) {
				customer = c;
				name = n;
				tableNumber = tN;
				state = customerState.waitingSeat;
				foodChoice = "";
				amountDue = 0;
			}
		}

}