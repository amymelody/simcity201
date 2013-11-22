package simcity.JesusRestaurant;

import simcity.JesusRestaurant.gui.JesusWaiterGui;
import simcity.JesusRestaurant.interfaces.JesusCustomer;
import simcity.JesusRestaurant.interfaces.JesusWaiter;
import simcity.JesusRestaurant.JesusMenu;
import simcity.role.Role;

import java.util.*;
import java.util.concurrent.Semaphore;

public class JesusWaiterRole extends Role implements JesusWaiter {
	
	public List<myCustomer> myCustomers = Collections.synchronizedList(new ArrayList<myCustomer>());

	public JesusMenu menu = new JesusMenu();

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public JesusWaiterGui waiterGui = null;

	// agent correspondents
	private JesusHostRole host;
	private JesusCookRole cook;
	private JesusCashierRole cashier;

	private String currentPlate = "";
	boolean breakTime = false;

	public enum customerState {waitingSeat, seated, readyToOrder, 
		ordering, waitingOrder, orderReady, eating, doneEating, leaving, takingPlate, orderGiven};

	public enum cookState {waitingOrder, preparingOrder, readyPickUp};

	public enum AgentState {DoingNothing, Seating, WalkingOrder, 
		TakingOrder, WalkingCook, RetakingOrder, HandingOrder, 
		WalkingPlate, HandingPlate, GettingCheck, HandingCheck, 
		WalkingClear, ClearingTable, Break, Closed, GettingPlate};

	public enum AgentEvent {none, assigned, readyTakeOrder, gotOrder, atCook, 
		noFoodChoice, noFood, orderReady, pickUp, handPlate, askForCheck, 
		receivedCheck, clean, goOnBreak, noBreak, returnToWork, BreakAccepted, atVait};

		AgentState state = AgentState.DoingNothing;
		AgentEvent event = AgentEvent.none;
		cookState cState = cookState.waitingOrder;
		
		int ordersToGet = 0;

		public JesusWaiterRole(String name) {
			super();

			this.name = name;
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
						ordersToGet++;
					}
				}
			}
			currentPlate = choice;
			event = AgentEvent.orderReady;
			cState = cookState.readyPickUp;
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

		public void msgCheckComputed(JesusCustomer cust, double amount, String name) {
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
			ordersToGet--;
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

		/**
		 * Scheduler.  Determine what action is called for, and do it.
		 */
		protected boolean pickAndExecuteAnAction() {
			if(event == AgentEvent.noBreak) {
				state = AgentState.DoingNothing;
				event = AgentEvent.none;
				noBreak();
				return true;
			}
			if(event == AgentEvent.BreakAccepted) {
				state = AgentState.Break;
				event = AgentEvent.none;
				yesBreak();
				return true;
			}
			if(event == AgentEvent.returnToWork && state == AgentState.Break) {
				state = AgentState.DoingNothing;
				returnToWork();
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
					if(c.state == customerState.readyToOrder && event != AgentEvent.readyTakeOrder && state != AgentState.WalkingOrder) {
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
					if (c.state == customerState.readyToOrder && (state != AgentState.WalkingCook || state != AgentState.TakingOrder)) {
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
					if(c.state == customerState.orderReady && ordersToGet != 0) {
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

			if(breakTime && myCustomers.isEmpty()) {
				state = AgentState.Break;
				breakTime = false;
				goOnBreak();
				return true;
			}

			return false;
			//we have tried all our rules and found
			//nothing to do. So return false to main loop of abstract agent
			//and wait.
		}

		// Actions

		private void goOnBreak() {
			print(name + " going on break...");
			host.msgGoingOnBreak(this);
		}

		private void noBreak() {
			waiterGui.breakDecision(false);
		}

		private void yesBreak() {
			waiterGui.breakDecision(true);
		}

		private void returnToWork() {
			print("Returning to work.");
			host.msgReturningToWork(this);
			waiterGui.breakDecision(false);
		}
		private void goToVait() {
			waiterGui.DoGoToVait();
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		private void seatCustomer(myCustomer cust) {
			waiterGui.DoLeaveCustomer();
			Do("Seating customer " + cust.customer);
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
			Do("Taking order from " + mc.customer);
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
			cState = cookState.preparingOrder;
		}

		private void retakeOrder(myCustomer mc) {
			print("Out of " + mc.foodChoice);
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
			print("Sorry, but we are out of everything...");
			mc.customer.msgNoFood();
			myCustomers.remove(mc);
		}

		private void getPlate(myCustomer mc) {
			Do("Picking up " + mc.foodChoice);
			DoGetPlate(mc);//animation
			cState = cookState.waitingOrder;
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void walkWithPlate(myCustomer mc) {
			cook.msgGotPlate(mc.foodChoice);
			Do("Handing " + mc.foodChoice + " to " + mc.customer);
			DoHandPlate(mc);//animation
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void handPlate(myCustomer mc) {
			Do("Enjoy your meal, " + mc.name);
			mc.customer.msgEnjoyOrder(mc.foodChoice);
			mc.state = customerState.eating;
			event = AgentEvent.askForCheck;
		}

		private void getCheck(myCustomer mc) {
			cashier.msgComputeCheck(this, mc.customer, mc.foodChoice, mc.name);
			state = AgentState.GettingCheck;
		}

		private void handCheck(myCustomer mc) {
			Do("Here is your check of $" + mc.amountDue + ", " + mc.name);
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
			Do("Clearing table " + cust.tableNumber);
			host.msgTableFree(cust.tableNumber, name);
			myCustomers.remove(cust);
			stateChanged();
		}

		// The animation DoXYZ() routines
		private void DoSeatCustomer(JesusCustomer customer, int tableNumber) {
			//Notice how we print "customer" directly. It's toString method will do it.
			//Same with "table"
			print("Seating " + customer + " at Table " + tableNumber);
			waiterGui.DoBringToTable(customer, tableNumber); 

		}

		private void DoTakeOrder(JesusCustomer cust, int tableN) {
			waiterGui.DoTakeOrder(cust, tableN);
		}

		private void DoGoToCook(myCustomer mc) {
			print("Giving order to " + cook.getName() + " (" + mc.foodChoice + ")");
			waiterGui.DoGoToCook();
		}

		private void DoGetPlate(myCustomer mc) {
			waiterGui.DoGetPlate(mc.foodChoice);
		}

		private void DoHandPlate(myCustomer cust) {
			waiterGui.DoHandPlate(cust.tableNumber, cust.foodChoice);
			//waiterGui.DoLeaveCustomer();
		}

		private void DoWalkToClear(int tableNum) {
			waiterGui.DoWalkToClear(tableNum);
		}
		//utilities

		public void setGui(JesusWaiterGui gui) {
			waiterGui = gui;
		}

		public JesusWaiterGui getGui() {
			return waiterGui;
		}

		private class myCustomer {
			JesusCustomer customer;
			String name;
			int tableNumber;
			customerState state;
			String foodChoice;
			double amountDue;

			myCustomer(JesusCustomer c, int tN, String n) {
				customer = c;
				name = n;
				tableNumber = tN;
				state = customerState.waitingSeat;
				foodChoice = "";
				amountDue = 0;
			}
		}
}