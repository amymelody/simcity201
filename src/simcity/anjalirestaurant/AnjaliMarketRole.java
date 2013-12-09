package simcity.anjalirestaurant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simcity.anjalirestaurant.interfaces.AnjaliCashier;
import simcity.anjalirestaurant.interfaces.AnjaliCook;
import simcity.anjalirestaurant.interfaces.AnjaliMarket;

/**
 * Restaurant Market Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class AnjaliMarketRole extends Agent implements AnjaliMarket {
	static final int NTABLES = 3;//a global for the number of tables.
	
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private String sendThisFood;
	private AnjaliCook cook;
	private AnjaliCashier cashier;
	Timer timer = new Timer(); 
	public boolean partOrderFulfilled = true;
	private int steakInventory = 2;
	private int chickenInventory = 3;
	private	int saladInventory = 3;
	private	int pizzaInventory = 3;
	private boolean brokeCashier = false;
	private double foodPrice = 10.00;
	
	public enum MarketState{nothing, sendingFood, processing, makingPayment, hasInventory, outOfFood};
	
	private MarketState state = MarketState.nothing;
		

	public AnjaliMarketRole(String name) {
		super();

		this.name = name;
	}

	public String getName(){
		return name;
	}
	public void setCashier(CashierAgent cashier){
		this.cashier = cashier;
	}
/////MESSAGES////////
	
		
	public void msgOrderSupply(String food, AnjaliCook c, int reduceInventory, boolean canPay){
	
	this.brokeCashier = canPay;
		Do("Market has received request from cook for more food");
		if(food == "Steak"){
			if(reduceInventory == 1){
				steakInventory = steakInventory -2;
			}
			if(reduceInventory == 0){
			steakInventory--;
			}
			//Do("Steak inventory from " + this.getName() + "is " + steakInventory);
			cook = c;
			sendThisFood = food;
			state = MarketState.sendingFood;
			stateChanged();
		}
		if(food == "Chicken"){
			chickenInventory--;	
			cook = c;
			sendThisFood = food;
			state = MarketState.sendingFood;
			stateChanged();
		}
		if(food == "Salad"){
			saladInventory--;
			cook = c;
			sendThisFood = food;
			state = MarketState.sendingFood;
			stateChanged();
		}
		if(food == "Pizza"){
			if(pizzaInventory != 0){
			pizzaInventory--;
			cook = c;
			sendThisFood = food;
			state = MarketState.sendingFood;
			stateChanged();
			}
			else{
				cook = c;
				sendThisFood = food;
				state = MarketState.outOfFood;
				stateChanged();
			}
		}
		
			
		}
	public void msgCheckInventory(String food, AnjaliCook c){
		//Do("Market " + this.getName() + " received message to check inventory");
		this.cook = c;
		//this.sendThisFood = food;
		if(food == "Steak"){
			if(steakInventory >= 1){
				//Do("Steak Inventory in " + this.getName() + "is at least 1");
				state = MarketState.hasInventory;
				stateChanged();

			}
		}
		if(food == "Chicken"){
			if(chickenInventory >= 1){
				state = MarketState.hasInventory;
				stateChanged();

			}
		}
		if(food == "Salad"){
			if(saladInventory >= 1){
				state = MarketState.hasInventory;
				stateChanged();

			}
		}
		if(food == "Pizza"){
			if(pizzaInventory >= 1){
				state = MarketState.hasInventory;
				stateChanged();

			}
			else{
				cook = c;
				sendThisFood = food;
				state = MarketState.outOfFood;
				stateChanged();

			}
		}
		}
	
	
		
	public void msgHereIsMoney(){
		Do("Market received money from cashier for food");
	}
		
	
	
////////SCHEDULER//////
	protected boolean pickAndExecuteAnAction() {
	
	
	if(state == MarketState.sendingFood){
		sendFood(sendThisFood);
		state = MarketState.processing;
		return true;
	}
	if(state == MarketState.hasInventory){
		orderFromMe();
		state = MarketState.nothing;
		return true;
	}
	if(state == MarketState.makingPayment){
		sendCheck();
		state = MarketState.nothing;
		return true;
	}
	if(state == MarketState.outOfFood){
		outOfFood(sendThisFood);
	}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
		 
		 
	}
	

	///////// ACTIONS/////////////
	
	public void orderFromMe(){
		cook.msgOrderFromMe(this);
	}
	
	private void sendFood(final String food){
		Do("Market is preparing food to send to cook...may take a while");

		if(food == "Steak" && steakInventory == 0){
			Do("This market could only fulfill part of the order. Must order from a different market as well. ");
			cook.msgPartOrderFulfilled(food);
			partOrderFulfilled = false;
			
		}
		
		timer.schedule(new TimerTask(){
			public void run(){
				Do("Market is sending cook food");
				cook.msgHereIsMoreFood(food);
				state = MarketState.makingPayment;
				stateChanged();

			}
		}, 20000);

		
	}
	
	

	
	private void sendCheck(){
		Do("Market sending cashier check for food");
		cashier.msgPayMarket(this, brokeCashier, foodPrice);
	}
	
	private void outOfFood(final String food){
		//Market is unable to fulfill order
		Do("Market does not carry that item anymore.");
		cook.msgNoMarketSupply(food);
		
	}
	
}	
 
 
	

	