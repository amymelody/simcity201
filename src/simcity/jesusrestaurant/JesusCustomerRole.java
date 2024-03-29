package simcity.jesusrestaurant;

import simcity.RestCustomerRole;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;
import simcity.interfaces.Person;
import simcity.jesusrestaurant.gui.JesusCustomerGui;
import simcity.jesusrestaurant.interfaces.JesusCashier;
import simcity.jesusrestaurant.interfaces.JesusCustomer;
import simcity.jesusrestaurant.interfaces.JesusWaiter;
import simcity.role.Role;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class JesusCustomerRole extends RestCustomerRole implements JesusCustomer {
	private String name;
	private int amountDue = 0;
	Timer timer = new Timer();
	private JesusCustomerGui jesusCustomerGui;

	// agent correspondents
	private JesusHostRole host;
	private JesusWaiter waiter;
	private JesusCashier cashier;

	// private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordered, Eating, Paying, Leaving, GoingPay};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, atTable, givingOrder, waitingPlate, outOfFood, receivedPlate, 
		receivedCheck, doneEating, doneLeaving, noFood, noSeats, atCashier};
		AgentEvent event = AgentEvent.none;

		public enum WaiterState
		{none, takingOrder, handedFood};
		private WaiterState wState = WaiterState.none;

		int tableNum;
		String foodChoice;
		public JesusMenu menu;

		/**
		 * Constructor for CustomerAgent class
		 *
		 * @param name name of the customer
		 * @param gui  reference to the customergui so the customer can send it messages
		 */
		public JesusCustomerRole(){
			super();
			foodChoice = "";
			menu = null;
		}

		public void setPerson(Person p) {
			super.setPerson(p);
			name = p.getName();
		}
		
		public void setHost(JesusHostRole host) {
			this.host = host;
		}

		public void setCashier(JesusCashier csh) {
			this.cashier = csh;
		}

		public void setWaiter(JesusWaiter waiter) {
			this.waiter = waiter;
		}
		public String getCustomerName() {
			return name;
		}
		// Messages
		public void gotHungry() { //from animation
			event = AgentEvent.gotHungry;
			stateChanged();
		}

		public void msgNoSeats() {
			event = AgentEvent.noSeats;
			stateChanged();
		}

		public void msgWereClosed() {
			event = AgentEvent.noFood;
			stateChanged();
		}

		public void msgSitAtTable(JesusWaiter w, int tNum, JesusMenu m) {
			waiter = w;
			AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Received msgSitAtTable");
			tableNum = tNum;
			event = AgentEvent.followWaiter;
			menu = m;
			stateChanged();
		}

		public void msgTakeOrder() {
			event = AgentEvent.givingOrder;
			stateChanged();
		}

		public void msgRetakeOrder(JesusMenu m) {
			event = AgentEvent.outOfFood;
			menu = m;
			stateChanged();
		}

		public void msgNoFood() {
			event = AgentEvent.noFood;
			stateChanged();
		}

		public void msgEnjoyOrder(String choice) {
			event = AgentEvent.receivedPlate;
			stateChanged();
		}

		public void msgHereIsCheck(int aDue) {
			amountDue = aDue;
			person.msgExpense(aDue);
			event = AgentEvent.receivedCheck;
			stateChanged();
		}

		public void msgChange(int change) {
			person.msgIncome(change);
			event = AgentEvent.doneEating;
			state = AgentState.Paying;
			stateChanged();
		}

		public void msgPayNextTime(int amount) {
			event = AgentEvent.doneEating;
			stateChanged();
		}

		//Animation messages
		public void msgAnimationFinishedGoToSeat() {
			//from animation
			event = AgentEvent.atTable;
			stateChanged();
		}
		public void msgAnimationFinishedOrdering() {
			//from animation
			event = AgentEvent.waitingPlate;
			stateChanged();
		}
		public void msgAnimationFinishedGoToCashier() {
			//from animation
			event = AgentEvent.atCashier;
			stateChanged();
		}
		public void msgAnimationFinishedLeaveRestaurant() {
			//from animation
			event = AgentEvent.doneLeaving;
			state = AgentState.DoingNothing;
			person.msgLeftDestination(this);
			stateChanged();
		}

		/**
		 * Scheduler.  Determine what action is called for, and do it.
		 */
		public boolean pickAndExecuteAnAction() {
			//	CustomerAgent is a finite state machine

			if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
				state = AgentState.WaitingInRestaurant;
				goToRestaurant();
				return true;
			}
			if (state == AgentState.WaitingInRestaurant && event == AgentEvent.noSeats) {
				decideToWait();
				return true;
			}
			if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter && tableNum != 0){
				state = AgentState.BeingSeated;
				goToTable(tableNum);
				return true;
			}
			if (state == AgentState.BeingSeated && event == AgentEvent.givingOrder){
				state = AgentState.Ordered;
				giveOrder();
				return true;
			}
			if(state == AgentState.Ordered && event == AgentEvent.outOfFood) {
				event = AgentEvent.none;
				regiveOrder();
				return true;
			}
			if(state == AgentState.Ordered && event == AgentEvent.receivedPlate) {
				state = AgentState.Eating;
				eatFood(foodChoice);
				return true;
			}
			if(state == AgentState.Eating && event == AgentEvent.doneEating) {
				state = AgentState.GoingPay;
				goToCashier();
				return true;
			}
			if(state == AgentState.GoingPay && event == AgentEvent.atCashier) {
				state = AgentState.Paying;
				payCashier();
				return true;
			}
 			if ((state == AgentState.Paying && event == AgentEvent.atCashier) || (event == AgentEvent.noFood)){
				event = AgentEvent.none;
				state = AgentState.Leaving;
				leaveTable();
				return true;
			}
			if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
				//no action
				return true;
			}
			return false;
		}

		// Actions

		private void goToRestaurant() {
			host.msgIWantFood(this);//send our instance, so he can respond to us
			jesusCustomerGui.vait();
		}

		private void decideToWait() {
			host.msgWaiting(name);
			event = AgentEvent.none;
		}

		private void goToTable(int tableN) {
			Do("Being seated. Going to table");
			jesusCustomerGui.DoGoToSeat(tableN);
			waiter.msgReadytoOrder(name);
		}

		private void giveOrder() {
			if(menu.tooExpensive(person.getMoney())) {
				AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Everything is too expensive.");
				waiter.msgLeavingTable(name);
				jesusCustomerGui.DoExitRestaurant();
			}
			else {
				Random rnd = new Random();
				foodChoice = menu.getMenuItemName(rnd.nextInt(3));
				jesusCustomerGui.DoOrder(tableNum, foodChoice);
				AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Ordered " + foodChoice);
				waiter.msgMyOrder(name, foodChoice);
			}
			menu = null;
		}

		private void regiveOrder() {
			if(menu.tooExpensive(person.getMoney())) {
				AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Everything is too expensive.");
				waiter.msgLeavingTable(name);
				jesusCustomerGui.DoExitRestaurant();
			}
			else {
				Random rnd = new Random();
				String newOrder = foodChoice;
				while(newOrder.equals(foodChoice) || !(menu.inStock(newOrder))) {
					newOrder = menu.getMenuItemName(rnd.nextInt(3));
				}
				foodChoice = newOrder;
				AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Ordered " + foodChoice);
				waiter.msgMyOrder(name, foodChoice);
				menu = null;
				jesusCustomerGui.DoOrder(tableNum, foodChoice);
			}
		}

		private void eatFood(String choice) {
			jesusCustomerGui.DoReceivedOrder(tableNum, foodChoice);
			Do("Eating Food");
			//This next complicated line creates and starts a timer thread.
			//We schedule a deadline of getHungerLevel()*1000 milliseconds.
			//When that time elapses, it will call back to the run routine
			//located in the anonymous class created right there inline:
			//TimerTask is an interface that we implement right there inline.
			//Since Java does not all us to pass functions, only objects.
			//So, we use Java syntactic mechanism to create an
			//anonymous inner class that has the public method run() in it.
			timer.schedule(new TimerTask() {
				Object food = 1;
				public void run() {
					AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Done eating, " + foodChoice + "=" + food);
					event = AgentEvent.doneEating;
					//isHungry = false;
					stateChanged();
				}
			},
			5000);//how long to wait before running task
		}

		private void goToCashier() {
			jesusCustomerGui.DoGoToCashier();
		}
		private void payCashier() {
			Do("Paying Cashier");
			cashier.msgCustomerPayment(this, amountDue, name);
		}

		private void leaveTable() {
			Do("Leaving.");
			waiter.msgLeavingTable(name);
			jesusCustomerGui.DoExitRestaurant();

		}

		// Accessors, etc.

		public String getName() {
			return name;
		}

		public String toString() {
			return "customer " + getName();
		}

		public void setGui(JesusCustomerGui g) {
			jesusCustomerGui = g;
		}

		public JesusCustomerGui getGui() {
			return jesusCustomerGui;
		}

		public void setCash(int c) {
			
		}

}