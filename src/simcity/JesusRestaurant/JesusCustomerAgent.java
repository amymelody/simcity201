package simcity.JesusRestaurant;

import simcity.cherysrestaurant.WaiterAgent.customerState;
import simcity.cherysrestaurant.gui.CustomerGui;
import simcity.cherysrestaurant.gui.RestaurantGui;
import simcity.cherysrestaurant.interfaces.Cashier;
import simcity.cherysrestaurant.interfaces.Customer;
import simcity.cherysrestaurant.interfaces.Waiter;
import agent.Agent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class JesusCustomerAgent extends Agent implements JesusCustomer {
	private String name;
	private int hungerLevel = 5; // determines length of meal
	private double money = 20.00;
	private double amountDue = 0.00;
	Timer timer = new Timer();
	private JesusCustomerGui customerGui;

	// agent correspondents
	private JesusHostAgent host;
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
		public JesusCustomerAgent(String name){
			super();
			this.name = name;
			foodChoice = "";
			menu = null;
		}

		/**
		 * hack to establish connection to Host agent.
		 */
		public void setHost(JesusHostAgent host) {
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
		public double getMoney() {
			return money;
		}
		public void updateMoney(Double m) {
			money = m;
		}
		// Messages
		public void gotHungry() { //from animation
			print("I'm hungry");
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
			print("Received msgSitAtTable");
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

		public void msgHereIsCheck(double aDue) {
			amountDue = aDue;
			event = AgentEvent.receivedCheck;
			stateChanged();
		}

		public void msgChange(double change) {
			money = change;
			event = AgentEvent.doneEating;
			state = AgentState.Paying;
			stateChanged();
		}

		public void msgPayNextTime(double amount) {
			money = 0;
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
			stateChanged();
		}

		/**
		 * Scheduler.  Determine what action is called for, and do it.
		 */
		protected boolean pickAndExecuteAnAction() {
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
			Do("Going to restaurant");
			host.msgIWantFood(this);//send our instance, so he can respond to us
			customerGui.vait();
		}

		private void decideToWait() {
			if(hungerLevel > 5) {
				host.msgLeaving(name);
				print("No thanks. I'll come back later.");
				state = AgentState.DoingNothing;
				event = AgentEvent.none;
				customerGui.DoExitRestaurant();
			}
			else {
				host.msgWaiting(name);
				print("Sure. I'll wait.");
				event = AgentEvent.none;
			}
		}

		private void goToTable(int tableN) {
			Do("Being seated. Going to table");
			customerGui.DoGoToSeat(tableN);
			waiter.msgReadytoOrder(name);
		}

		private void giveOrder() {
			if(menu.tooExpensive(money)) {
				print("Everything is too expensive.");
				waiter.msgLeavingTable(name);
				customerGui.DoExitRestaurant();
			}
			else {
				if(name.equals("Salad") || name.equals("Steak") || name.equals("Pizza")) {
					foodChoice = name;
				}
				else {
					Random rnd = new Random();
					foodChoice = menu.getMenuItemName(rnd.nextInt(3));
				}
				customerGui.DoOrder(tableNum, foodChoice);
				print("Ordered " + foodChoice);
				waiter.msgMyOrder(name, foodChoice);
			}
			menu = null;
		}

		private void regiveOrder() {
			if(menu.tooExpensive(money)) {
				print("Everything is too expensive.");
				waiter.msgLeavingTable(name);
				customerGui.DoExitRestaurant();
			}
			else {
				Random rnd = new Random();
				String newOrder = foodChoice;
				while(newOrder.equals(foodChoice) || !(menu.inStock(newOrder))) {
					newOrder = menu.getMenuItemName(rnd.nextInt(3));
				}
				foodChoice = newOrder;
				print("Ordered " + foodChoice);
				waiter.msgMyOrder(name, foodChoice);
				menu = null;
				customerGui.DoOrder(tableNum, foodChoice);
			}
		}

		private void eatFood(String choice) {
			customerGui.DoReceivedOrder(tableNum, foodChoice);
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
					print("Done eating, " + foodChoice + "=" + food);
					event = AgentEvent.doneEating;
					//isHungry = false;
					stateChanged();
				}
			},
			getHungerLevel() * 1000);//how long to wait before running task
		}

		private void goToCashier() {
			customerGui.DoGoToCashier();
		}
		private void payCashier() {
			Do("Paying Cashier");
			cashier.msgCustomerPayment(this, money, name);
		}

		private void leaveTable() {
			Do("Leaving.");
			waiter.msgLeavingTable(name);
			customerGui.DoExitRestaurant();

		}

		// Accessors, etc.

		public String getName() {
			return name;
		}

		public int getHungerLevel() {
			return hungerLevel;
		}

		public void setHungerLevel(int hungerLevel) {
			this.hungerLevel = hungerLevel;
			//could be a state change. Maybe you don't
			//need to eat until hunger lever is > 5?
		}

		public String toString() {
			return "customer " + getName();
		}

		public void setGui(JesusCustomerGui g) {
			customerGui = g;
		}

		public JesusCustomerGui getGui() {
			return customerGui;
		}
}