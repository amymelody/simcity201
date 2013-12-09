package simcity.anjalirestaurant;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.gui.CustomerGui;
import simcity.anjalirestaurant.interfaces.Cashier;
import simcity.anjalirestaurant.interfaces.Customer;
import simcity.anjalirestaurant.interfaces.Waiter;
import agent.Agent;


/**
 * Restaurant customer agent.
 */

//Works for normative and nonnormative scenarios


public class CustomerAgent extends Agent implements Customer{
	private String name;
	String pizzaName = "Pizza";
	String poorCustomer = "poorCustomer";
	String pickyCustomer = "pickyCustomer";
	String brokeCustomer = "brokeCustomer";
	int brokeC = new Random().nextInt(2);
	private String foodSelection;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private AnjaliCustomerGui customerGui;
	private Semaphore atTable = new Semaphore(0,true);

	int tableX;
	int tableY;
	// agent correspondents
	private HostAgent host;
	private Waiter waiter;
	private Cashier cashier;
	//    private boolean isHungry = false; //hack for gui
	public enum CustomerState
	{DoingNothing, WaitingInRestaurant, WaitingForTable, BeingSeated, Seated, ReadingMenu, ReadyToOrder, Ordered, Eating, DoneEating, readyToPay, paid, Leaving};
	
	private CustomerState state = CustomerState.DoingNothing;//The start state

	public enum CustomerEvent
	{none, gotHungry, followWaiter, seated, tooExpensive, restIsFull, waitingForTable, doneReading,ordering, orderingAgain, served, doneEating, paying, donePaying, doneLeaving};
	CustomerEvent event = CustomerEvent.none;
	Map<String, Double> m;
	
	double price = 0.00;
	/**
	 * Constructor for Customer class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	
	public CustomerAgent (String name){
		super();
		this.name = name;
		
	}
	
	public String getCustomerChoice(){
		return foodSelection;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostAgent host) {
		this.host = host;
	}
	
	public void setWaiter(Waiter waiter){
		this.waiter = waiter;
	}
	
	public void setCashier(CashierAgent cashier){
		this.cashier = cashier;
	}
	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		
		
		{
		
		event = CustomerEvent.gotHungry;
		stateChanged();
		}
	}
	
	public void msgAtTable(){//from animation
		atTable.release();
		
		//Do("released from table");	
	}
	public void msgRestaurantIsFull(){
		String willWait = "willWait";
		String wontWait = "wontWait";
		
		if(wontWait.equals(getName())){
			Do("Sorry, I don't want to wait. I'm leaving.");
			
		}
		if(willWait.equals(getName())){
			Do("Thats ok, I'll wait");
			event = CustomerEvent.waitingForTable;
			stateChanged();
		}
		}

	
	public void msgFollowMeToTable(int tableX, int tableY, Map<String, Double> m, Waiter w) {
	
		print("Received msgFollowMeToTable");
		this.tableX = tableX;
		this.tableY = tableY;
		this.m = m;
		this.waiter = w;
		
		print("Customer has received menu");
		print("Menu for today is ");
		System.out.println(m);
		event = CustomerEvent.followWaiter;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = CustomerEvent.seated;
		Do("Customer has finished going to seat");
		stateChanged();
	}
	
	public void msgWhatWouldYouLike()
	{
		print("Waiter asking customer what he would like");
		//event = CustomerEvent.ordering;
		stateChanged();
	}
	
	public void msgNoMoreFood(){
		print("Food is unavailable. Making another choice");
		event = CustomerEvent.orderingAgain;
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = CustomerEvent.doneLeaving;
		stateChanged();
	}
	public void msgHereIsYourFood(){
		print("Customer received food");
		event = CustomerEvent.served;
		stateChanged();
	}

	
	public void msgPayCheck(double price){
		Do("Customer received check from waiter");
		this.price = price;
		event = CustomerEvent.paying;
		stateChanged();
	}
	
	public void msgGoodToGo(){
		Do("Cashier has cleared customer to go because payment was correct");
		event = CustomerEvent.donePaying;
		stateChanged();
	}

	public void msgPayMeLater(){
		//Customer still ordered even though they wre broke
		Do("Cashier has told customer to pay him later because he has no money.");
		event = CustomerEvent.donePaying;
		stateChanged();
	}
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	Customer is a finite state machine

		
		//V2 SCHEDULER
		//if there exists a customerState = one and customerEvent = gotHungry, then goToRestaraunt()
		//if there exists a customerState=waitingInRestaraunt and event = followWaiter, then sitDown(), state = beingSeated();
		if (state == CustomerState.DoingNothing && event == CustomerEvent.gotHungry ){
			state = CustomerState.WaitingInRestaurant;
			Do("customer state changed to waiting in restaurant");
			goToRestaurant();
			Do("goToRestaurant() called");
			return true;
		}
		if(state == CustomerState.WaitingInRestaurant && event == CustomerEvent.restIsFull){
			Do("Restaurant is full and customer doesn't want to wait, so he is leaving.");
			leaveTable();
			return true;
		}
		if(state == CustomerState.WaitingInRestaurant && event == CustomerEvent.waitingForTable){
			willWait();
			state = CustomerState.WaitingForTable;
			return true;
		}
		if (state == CustomerState.WaitingInRestaurant && event == CustomerEvent.followWaiter ){
			state = CustomerState.BeingSeated;
			Do("SitDown() called");
			SitDown();
			return true;
		}
		if (state == CustomerState.BeingSeated && event == CustomerEvent.seated){
			state = CustomerState.ReadingMenu;
			Do("read Menu()");
			ReadMenu();
			return true;
		}
		
		if(state == CustomerState.ReadingMenu && event == CustomerEvent.tooExpensive){
			state = CustomerState.Leaving;
			//Do("Customer cannot afford anything in the restaurant. He is leaving.");
			leaveTable();
			return true;
			
			}
		
		if (state == CustomerState.ReadingMenu && event == CustomerEvent.doneReading){
			state = CustomerState.ReadyToOrder;
			Do("Customer state is reading menu and event is done reading, customer is " + this.getName());
			Do("waiter pointer" + waiter);
			waiter.msgReadyToOrder(this);
			Do("ordering");
			Order();
			return true;
		}
		
		if(state == CustomerState.ReadyToOrder && event == CustomerEvent.orderingAgain){
			Do("Ordering again");
			OrderAgain();
			state = CustomerState.Ordered;
			return true;
		}
		
		if(state == CustomerState.Ordered && event == CustomerEvent.tooExpensive){
			state = CustomerState.Leaving;
			Do("Customer cannot afford anything in the restaurant. He is leaving.");
			leaveTable();
			return true;
		}
		if(state == CustomerState.ReadyToOrder && event == CustomerEvent.served){
			state = CustomerState.Eating;
			Do("customer is eating");
			EatFood();
			return true;
			
		}
		if(state == CustomerState.Ordered && event == CustomerEvent.served){
			state = CustomerState.Eating;
			Do("customer is eating");
			EatFood();
			return true;
			
		}
		if (state == CustomerState.Eating && event == CustomerEvent.doneEating){
			state = CustomerState.readyToPay;
			getCheck();
			return true;
		}
		if (state == CustomerState.readyToPay && event == CustomerEvent.paying){
			state = CustomerState.paid;
			pay();
			return true;
		}
		
		if (state == CustomerState.paid && event == CustomerEvent.donePaying){
			state = CustomerState.Leaving;
			leaveTable();
			return true;
		}
		
		if (state == CustomerState.Leaving && event == CustomerEvent.doneLeaving){
			state = CustomerState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
		//customerGui.DoGoToWaitingArea(10,200);
		Do("Customer wants food");
	}

	private void willWait(){
		Do("Customer has decided to wait, telling waiter.");
		
		host.msgIWillWait(this);
	}
	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToWaitingArea();
		
		customerGui.DoGoToSeat(tableX, tableY);//hack; only one table
		
		
		
	}

	private void ReadMenu(){
		
		System.out.println(m);
		timer.schedule(new TimerTask() {
			
			public void run() {
				Do("customer is done reading menu");
				
				//If a customer has no money, he will sometimes choose to leave, sometimes order anyway
				if(brokeCustomer.equals(getName()) && brokeC == 1){
					Do("Customer doesn't have enough money to eat here.");
					event = CustomerEvent.tooExpensive;
					stateChanged();	
				}
				else
				event = CustomerEvent.doneReading;
				stateChanged();
			}
		},
		5000);		
		
		
	}	


	private void Order(){
		Do(this.getCustomerName() + "is ordering");
		
		ArrayList<String> choices = new ArrayList<String> (m.keySet());
		System.out.println(choices);
		
		//Test scenario where market was unable to fulfill an order by using customer name Pizza
		if(this.getCustomerName().equals(pizzaName)){
			foodSelection = pizzaName;
			Do("Customer wants to order " + foodSelection);
			waiter.msgHereIsMyChoice(this, foodSelection);
			customerGui.msgDrawFoodChoice(foodSelection + "?");
		}
		else if(this.getCustomerName().equals(pickyCustomer)){
			foodSelection = "Steak";
			Do("Customer wants to order " + foodSelection);
			waiter.msgHereIsMyChoice(this, foodSelection);
			customerGui.msgDrawFoodChoice(foodSelection + "?");
		}
		else if(this.getCustomerName().equals(poorCustomer)){
			//A poor customer will only order the cheapest item, then will leave if that item is not available. 
			foodSelection = "Salad";
			Do("Customer wants to order " + foodSelection);
			waiter.msgHereIsMyChoice(this, foodSelection);
			customerGui.msgDrawFoodChoice(foodSelection + "?");
		}
		else if(this.getCustomerName().equals("steak")){
			//A poor customer will only order the cheapest item, then will leave if that item is not available. 
			foodSelection = "Steak";
			Do("Customer wants to order " + foodSelection);
			waiter.msgHereIsMyChoice(this, foodSelection);
			customerGui.msgDrawFoodChoice(foodSelection + "?");
		}
		else {
			int food = new Random().nextInt(4);
			foodSelection = choices.get(food);
		Do("Customer wants to order " + foodSelection);
		waiter.msgHereIsMyChoice(this, foodSelection);
		customerGui.msgDrawFoodChoice(foodSelection + "?");
		}
	}
	private void OrderAgain(){
		ArrayList<String> choices = new ArrayList<String> (m.keySet());
		System.out.println(choices);
		
		//Names for different scenarios to test
		if(this.getName().equals(poorCustomer)){
			event = CustomerEvent.tooExpensive;
			Do("Customer can't afford any other items.");
			
			stateChanged();
		}
		
		else if(this.getName().equals(pickyCustomer)){
			event = CustomerEvent.tooExpensive;
			Do("Customer doesn't want to eat anything else at this restaurant. He is leaving.");
			stateChanged();
		
		}
		
		else{
		int food = new Random().nextInt(2);
		foodSelection = choices.get(food);
		System.out.println("choice is " + foodSelection);
		waiter.msgHereIsMyChoice(this, foodSelection);
		customerGui.msgDrawFoodChoice(foodSelection + "?");
		}
	}
	
	private void EatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		customerGui.msgDrawFoodChoice(foodSelection);
		timer.schedule(new TimerTask() {
			
			public void run() {
				print("Done eating");
				event = CustomerEvent.doneEating;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void getCheck() {
		Do("Customer is asking waiter for check");
		waiter.msgReadyForCheck(this);
	}
	
	private void pay(){
		
	
		
	if(this.getName().equals(brokeCustomer)){
			Do("Sorry, I'm broke");
				cashier.msgTakeMyMoney(this, 0.00);
			}
			else{
				Do("Customer is paying cashier:" + price);
		cashier.msgTakeMyMoney(this, price);
			}
		}
	
	private void leaveTable() {
		//Animation for customer leaving restaurant if it runs out of choice, if it can't afford anything, if it is done eating.
		Do("Leaving.");
		waiter.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
	}

	public void drawString(){
		
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

	public void setGui(AnjaliCustomerGui g) {
		customerGui = g;
	}

	public AnjaliCustomerGui getGui() {
		return customerGui;
	}
}

