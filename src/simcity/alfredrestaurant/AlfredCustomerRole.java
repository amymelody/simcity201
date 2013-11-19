package simcity.alfredrestaurant;

import restaurant.WaiterAgent.AgentEvent;
import restaurant.WaiterAgent.AgentState;
import restaurant.gui.CustomerGui;
import restaurant.interfaces.Customer;
import agent.Agent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class AlfredCustomerRole extends Agent implements CherysCustomer{

	private static int ID = 1;
	
	private String name;
	private int hungerLevel = 5; // determines length of meal
	Timer timer = new Timer();
	private CherysCustomerGui customerGui;
	private Table table;
	// agent correspondents
	private CherysHostRole host;
	
	private int id;

	private CherysWaiterRole waiter;
	
	int foodIndex = -1;

	public Table getTable() {
		return table;
	}

	public enum AgentState {

		DoingNothing, WaitingInRestaurant, BeingSeated, Seated, BeingOrdered, Eating, DoneEating, 
		WaitingForBill, Leaving, WaitForFullfillPaymentRequest, PayingTheBill
	};

	private AgentState state = AgentState.DoingNothing;// The start state

	public enum AgentEvent {

		none, gotHungry, followHost, followWaiter, seated, ordered, leaveDuetoExpensive, 
		doneEating, doneLeaving, HadBill, donePaymentRequest, paidBill
		
	};

	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 * 
	 * @param name
	 *            name of the customer
	 * @param gui
	 *            reference to the customergui so the customer can send it
	 *            messages
	 */
	public CustomerAgent(String name) {
		super();
		this.name = name;
		id = ID++;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(CherysHostRole host) {
		this.host = host;
	}

	public void setWaiter(CherysWaiterRole waiter) {
		this.waiter = waiter;
	}

	/**
	 * hack to establish connection to table
	 */
	public void setTable(Table table) {
		this.table = table;
	}

	public String getCustomerName() {
		return name;
	}

	// Messages

	public void gotHungry() {// from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgSitAtTable() {
		print("Received msgSitAtTable");
		event = AgentEvent.followHost;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		// from animation
		event = AgentEvent.seated;
		stateChanged();
	}

	public void msgAnimationFinishedLeaveRestaurant() {
		// from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		// CustomerAgent is a finite state machine
		host.restaurantPanel.restauranGUI.customerWaitingArea.repaint();
		
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry) {
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}

		if (state == AgentState.WaitingInRestaurant
				&& event == AgentEvent.followWaiter) {
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated) {
			// state = AgentState.Eating;
			// EatFood();
			// Must order before eating
			state = AgentState.BeingOrdered;
			waiter.msgSeated();
			// wait some second before ready to order
			doSomeThingBeforeReadyToOrder();

			return true;
		}
		if (state == AgentState.BeingOrdered && event == AgentEvent.leaveDuetoExpensive) {
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.BeingOrdered && event == AgentEvent.ordered) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}

		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
//			waiter.msgDoneEating();
			//state = AgentState.WaitingForBill;
			
			host.cashierAgent.msgPaymentRequest(this, new Bill(Menu.FOODS[foodIndex], Menu.PRICES[foodIndex]));
			state = AgentState.WaitForFullfillPaymentRequest;
			return true;
		}
//		if (state == AgentState.WaitingForBill && event == AgentEvent.HadBill) {
//			waiter.msgSendingBillDone();
//			host.cashierAgent.msgCheckRequest(this);
//			state = AgentState.WaitForFullfillPaymentRequest;
//			return true;
//		}
		if (state == AgentState.WaitForFullfillPaymentRequest && event == AgentEvent.donePaymentRequest) {
			//host.cashierAgent.msgCheckRequest(this);
			state = AgentState.PayingTheBill;
			payBill();
			return true;
		}
		if (state == AgentState.PayingTheBill && event == AgentEvent.paidBill) {
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
//			waiter.msgDoneLeaving();
			
			host.msgTableIsFree(getTable());
			return true;
		}
		
		return false;
	}

	private void payBill() {
		System.out.println("paying bill..");
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.paidBill;
				stateChanged();
			}
		}, 2000);		
	}

	private void doSomeThingBeforeReadyToOrder() {
		final CherysCustomerRole thisAgent = this;
		timer.schedule(new TimerTask() {
			public void run() {
				//want to leave, expensive food
				if (Math.random() < 0.3){
					event = AgentEvent.leaveDuetoExpensive;
					stateChanged();
					waiter.messeageleaveDuetoExpensive(thisAgent);
				}else{
					waiter.messeageReadyToOrder(thisAgent);
				}
			}
		}, 4000);

	}

	// Actions
	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);// send our instance, so he can respond to us
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat();
	}

	private void EatFood() {
		Do("Eating Food");
		// This next complicated line creates and starts a timer thread.
		// We schedule a deadline of getHungerLevel()*1000 milliseconds.
		// When that time elapses, it will call back to the run routine
		// located in the anonymous class created right there inline:
		// TimerTask is an interface that we implement right there inline.
		// Since Java does not all us to pass functions, only objects.
		// So, we use Java syntactic mechanism to create an
		// anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating");
				event = AgentEvent.doneEating;
				// isHungry = false;
				customerGui.clearFood();
				stateChanged();
			}
		}, 10000);// getHungerLevel() * 1000);//how long to wait before running
					// task
	}

	private void leaveTable() {
		Do("Leaving.");
//		host.msgLeavingTable(this);
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
		// could be a state change. Maybe you don't
		// need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CherysCustomerGui g) {
		customerGui = g;
	}

	public CherysCustomerGui getGui() {
		return customerGui;
	}

	public void becomeHungry() {
		Do("I want to eat");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgFollowMeToTable(Menu menu) {
		print("Received msgFollowMeToTable");
		event = AgentEvent.followWaiter;
		stateChanged();
	}

	private String lastFood  = "";
	public void msgWhatWouldYouLike(Menu menu) {
		foodIndex = (int)(Math.random() * Menu.MAX_FOODS);
		String food = menu.chooseFood(foodIndex);
		
		//try to prevent duplicate not available food
		while (food.equals(lastFood)){
			foodIndex = (int)(Math.random() * Menu.MAX_FOODS);
			food = menu.chooseFood(foodIndex);
		}
		lastFood = food;
		waiter.msgMyChoice(food);
		customerGui.displayAskingFood(food);
	}

	public void msgHereIsYourFood() {
		print("Received msgHereIsYourFood");
		customerGui.displayFood();
		event = AgentEvent.ordered;
		stateChanged();
	}
	
	public void msgHereIsYourNoFood() {
		print("Received msgHereIsYourNoFood");
		state = AgentState.BeingOrdered;
		waiter.msgSeated();
		// wait some second before ready to order
		doSomeThingBeforeReadyToOrder();

	}

	public void msgHereYourBill() {
		System.out.println("msgHereYourBill");
		event = AgentEvent.HadBill;
		stateChanged();
	}

	//call from cashier
	public void msgCheckRequestDone(){
		System.out.println("msgHereYourBill");
		event = AgentEvent.donePaymentRequest;
		stateChanged();
	}
	
	public boolean isWaiting(){
		return state == AgentState.WaitingInRestaurant;
	}
}
