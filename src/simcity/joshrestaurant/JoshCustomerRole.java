package simcity.joshrestaurant;

import simcity.agent.Agent;
import simcity.joshrestaurant.gui.JoshCustomerGui;
import simcity.joshrestaurant.interfaces.JoshCustomer;
import simcity.RestCustomerRole;
import simcity.interfaces.Person;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class JoshCustomerRole extends RestCustomerRole implements JoshCustomer {
	private String name = null;
	private int hungerLevel = 1;
	Timer timer = new Timer();
	private JoshCustomerGui customerGui;
	private JoshMenu menu;
	private String choice;
	private Semaphore doneOrdering = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0,true);
	
	public int tableNumber;
	private int cash;
	private int charge;
	
	// agent correspondents
	private JoshHostRole host;
	private JoshWaiterRole waiter;
	private JoshCashierRole cashier;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, GoingToRestaurant, WaitingInRestaurant, BeingSeated, Seated, WantToLeave, ReadyToOrder, Ordered, Eating, WaitingForCheck, Paying, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, arrivedAtRestaurant, gotImpatient, followWaiter, seated, looksAtMenuAndCries, toldWaiter, madeChoice, order, receivedFood, doneEating, receivedCheck, receivedChange, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public JoshCustomerRole(){
		super();
		
		cash = 30;
		/*if (name.equals("cheapskate") || name.equals("poor")) {
			cash = 5;
		}
		if (name.equals("enoughforsalad")) {
			cash = 7;
		}*/
		charge = 0;
	}

	public void setPerson(Person p) {
		super.setPerson(p);
		name = person.getName();
		cash = person.getMoney();
	}
	
	public void setHost(JoshHostRole host) {
		this.host = host;
	}
	
	public void setCashier(JoshCashierRole cashier) {
		this.cashier = cashier;
	}
	
	public void setCash(int c) {
		cash = c;
	}

	public String getCustomerName() {
		return name;
	}
	
	public String getChoice() {
		return choice;
	}
	
	public int getCharge() {
		return charge;
	}
	
	public boolean isEating() {
		if (state == AgentState.Eating) {
			return true;
		}
		return false;
	}
	
	public JoshWaiterRole getWaiter() {
		return waiter;
	}
	
	// Messages

	public void gotHungry() {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "I'm hungry");
		event = AgentEvent.gotHungry;
		cash = person.getMoney();
		stateChanged();
	}
	
	public void msgRestaurantIsFull() {
		if (name.equals("impatient")) {
			event = AgentEvent.gotImpatient;
		} else {
			AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "I'll wait for a table to open");
		}
		stateChanged();
	}

	public void msgFollowMe(JoshWaiterRole w, JoshMenu m, int tableNumber) { //Menu will be added later
		waiter = w;
		menu = m;
		this.tableNumber = tableNumber;
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Received msgFollowMe");
		event = AgentEvent.followWaiter;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		if (!name.equals("cheapskate") && cash < menu.lowestPrice()) {
			event = AgentEvent.looksAtMenuAndCries;
		}
		else {
			event = AgentEvent.seated;
		}
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() {
		event = AgentEvent.order;
		stateChanged();
	}
	
	public void msgWantSomethingElse(JoshMenu menu) {
		this.menu = menu;
		if (name != "cheapskate" && cash < menu.lowestPrice()) {
			state = AgentState.BeingSeated;
			event = AgentEvent.looksAtMenuAndCries;
		} else {
			state = AgentState.ReadyToOrder;
			event = AgentEvent.order;
		}
		stateChanged();
	}
	
	public void msgHereIsFood(String choice) {
		if (this.choice == choice) {
			event = AgentEvent.receivedFood;
			stateChanged();
		}
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	public void msgAnimationFinishedEnterRestaurant() {
		//from animation
		event = AgentEvent.arrivedAtRestaurant;
		stateChanged();
	}
	
	public void  msgHereIsCheck(int c) {
		charge += c;
		event = AgentEvent.receivedCheck;
		stateChanged();
	}
	
	public void msgChange(int change) {
		if (change < 0) {
			charge = -change;
			cash += 30;
		}
		else {
			cash += change;
			person.msgIncome(change);
			charge = 0;
		}
		event = AgentEvent.receivedChange;
		stateChanged();
	}
	
	public void msgDoneOrdering() {//from animation
		doneOrdering.release();// = true;
		stateChanged();
	}
	
	public void msgAtCashier() {//from animation
		atCashier.release();// = true;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {

		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry){
			state = AgentState.GoingToRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.GoingToRestaurant && event == AgentEvent.arrivedAtRestaurant) {
			state = AgentState.WaitingInRestaurant;
			requestSeat();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.gotImpatient){
			state = AgentState.Leaving;
			leaveAndNotifyHost();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.looksAtMenuAndCries){
			state = AgentState.WantToLeave;
			tellWaiter();
			return true;
		}
		if (state == AgentState.WantToLeave && event == AgentEvent.toldWaiter){
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Seated;
			timer.schedule(new TimerTask() {
				public void run() {
					event = AgentEvent.madeChoice;
					stateChanged();
				}
			},
			getHungerLevel() * 500);//how long to wait before running task
			return true;
		}
		if (state == AgentState.Seated && event == AgentEvent.madeChoice){
			state = AgentState.ReadyToOrder;
			callWaiter();
			return true;
		}
		if (state == AgentState.ReadyToOrder && event == AgentEvent.order){
			state = AgentState.Ordered;
			giveOrder();
			return true;
		}
		if (state == AgentState.Ordered && event == AgentEvent.receivedFood){
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.WaitingForCheck;
			askForCheck();
			return true;
		}
		if (state == AgentState.WaitingForCheck && event == AgentEvent.receivedCheck){
			state = AgentState.Paying;
			leaveTable();
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.receivedChange){
			state = AgentState.Leaving;
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			goOutside();
			return true;
		}
		return false;
	}

	// Actions
	
	private void goToRestaurant() {
		customerGui.setHungry();
	}

	private void requestSeat() {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Table for 1");
		host.msgIWantFood(this);
	}
	
	private void leaveAndNotifyHost() {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "I don't want to wait. Leaving restaurant");
		customerGui.DoExitRestaurant();
		host.msgImLeaving(this);
	}

	private void SitDown() {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Being seated. Going to table");
		customerGui.DoGoToSeat(tableNumber);
	}
	
	private void tellWaiter() {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "This food is too expensive. I'm leaving.");
		waiter.msgIWantToLeave(this);
		event = AgentEvent.toldWaiter;
	}
	
	private void leaveRestaurant() {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Leaving restaurant");
		customerGui.DoExitRestaurant();
	}
	
	private void callWaiter() {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "I'm ready to order.");
		waiter.msgReadyToOrder(this);
	}
	
	private void giveOrder() {
		if (menu.checkItem(name)) {
			choice = name;
		} else {
			do {
				choice = menu.randomItem();
			} while (menu.getPrice(choice) > cash && !name.equals("cheapskate"));
		}
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "I would like to order " + choice);
		customerGui.order();
		try {
			doneOrdering.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiter.msgHereIsChoice(this, choice);
	}

	private void EatFood() {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Eating Food");
		timer.schedule(new TimerTask() {
			public void run() {
				AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Done eating " + choice);
				person.msgDoneEating();
				event = AgentEvent.doneEating;
				stateChanged();
			}
		},
		getHungerLevel() * 1000);//how long to wait before running task
	}
	
	private void askForCheck() {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Check please.");
		waiter.msgDoneEating(this);
	}

	private void leaveTable() {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Going to cashier");
		customerGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int payment = charge + 10 - charge % 10;
		if (cash < payment) {
			payment = cash;
		}
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Paying $" + payment);
		cashier.msgPayment(this, payment);
		cash -= payment;
		person.msgExpense(payment);
	}
	
	private void goOutside() {
		person.msgLeftDestination(this);
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

	public void setGui(JoshCustomerGui g) {
		customerGui = g;
	}

	public JoshCustomerGui getGui() {
		return customerGui;
	}
}

