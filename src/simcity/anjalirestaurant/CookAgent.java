package simcity.anjalirestaurant;
import restaurant.gui.CookGui;
import agent.Agent;
import restaurant.gui.CustomerGui;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantPanel;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cook Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios

public class CookAgent extends Agent implements Cook{
	static final int NTABLES = 3;//a global for the number of tables.
	
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	Timer cookTimer = new Timer();
	private Waiter waiter;
	private String outOfFood;
	public List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
	private CookGui cookGui;
	public Market market; 
	private int hasFood = 1;
	private int hasInventory = 0;
	private boolean cantPayCashier = false;
	public String orderThis;
	public class Order{
		
		 String choice;
		 Waiter waiter;
		 int tableNumber;
		 CookState state;
		 String customerName;
		
		
		Order(String name, String c, int tn, Waiter w, CookState cs){
			this.choice = c;
			this.tableNumber = tn;
			this.waiter = w;
			this.state = cs;
			this.customerName = name;
		}
		
		public int getTableNumber(){
			return tableNumber;
		}
		
		public CookState getCookState(){
			return state;
		}
		public void setCookState(CookState s){
			this.state = state;
			
		}
	}
private List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
public enum CookState{nothing, received, inventoryLow, checkingInventory, buyingFood, buying, partOrderFulfilled, outOfFood, waitingForOrder, unfulfilledOrder, cooking, cooked, delivered}; 

private int SteakInventory = 1;
private int SaladInventory = 1;
private int ChickenInventory = 1;
private int PizzaInventory = 1;

private CookState state = CookState.nothing;
	


	public CookAgent(String name) {
		super();

		this.name = name;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	private final Map<String, Integer> cookTime;{
		cookTime = new HashMap<String, Integer>();
		cookTime.put("Steak", 10000);
		cookTime.put("Chicken", 10000);
		cookTime.put("Salad" , 10000);
		cookTime.put("Pizza", 10000);			
	}
	
	public Map<String, Integer> getCookTime(){
		return cookTime;
	}

	public void setWaiter(Waiter waiter){
		this.waiter = waiter;
	}
/////MESSAGES////////
	public void msgAtTable(){//from animation
		atTable.release();
		
		//Do("released from table");	
	}
	public void msgHereIsOrder(String name, String choice, int tableNumber, Waiter waiter){
		Do("Cook has received order for table Number" + tableNumber + "from waiter " + waiter.getName() + "for food " + choice);	
		//After every order the cook receives, he checks to see whether the inventory is low or not. 
		//The cook may run out of food, scenario 2
		if (name.equals("brokeCashier")){
			cantPayCashier = true;
		}
		if (choice == "Steak"){
			if(name.equals("pickyCustomer"))
				SteakInventory--;
			
				
			if(SteakInventory == 1){
				state = CookState.inventoryLow;
				orderThis = choice; 
				orders.add(new Order(name, choice, tableNumber, waiter, CookState.received));
				
			}
			
			else if(SteakInventory == 0){
				Do("Steak Inventory is 0");
				orders.add(new Order(name, choice, tableNumber, waiter, CookState.outOfFood));
				
			}
			else
				orders.add(new Order(name, choice, tableNumber, waiter, CookState.received));
			
			if(SteakInventory > 0){
				SteakInventory--;
				}
			
			
		}
	
		
	if(choice == "Salad"){
		
		if(name.equals("poorCustomer"))
			SaladInventory--;
		
		if(SaladInventory == 1){
			state = CookState.inventoryLow;
			orderThis = choice; 
			orders.add(new Order(name, choice, tableNumber, waiter, CookState.received));

		}
		
		else if(SaladInventory == 0){
			orders.add(new Order(name, choice, tableNumber, waiter, CookState.outOfFood));
			
		}
		else
			orders.add(new Order(name, choice, tableNumber, waiter, CookState.received));

		if(SaladInventory > 0){
			SaladInventory--;
	}
		
		
	}
	
	if(choice == "Chicken"){
		
		
		
		if(ChickenInventory == 1){
			state = CookState.inventoryLow;
			orderThis = choice; 
			orders.add(new Order(name, choice, tableNumber, waiter, CookState.received));

		}
		
		else if(ChickenInventory == 0){
			orders.add(new Order(name, choice, tableNumber, waiter, CookState.outOfFood));
		}
		
		else
			orders.add(new Order(name, choice, tableNumber, waiter, CookState.received));

		
		if( ChickenInventory > 0){
			ChickenInventory--;
	}
		
		
	}
	
	
	if(choice == "Pizza"){
		if(name.equals("Pizza"))
			PizzaInventory--;
		
		if(PizzaInventory == 1){
			state = CookState.inventoryLow;
			orderThis = choice; 
			orders.add(new Order(name, choice, tableNumber, waiter, CookState.received));

		}
		
		else if(PizzaInventory == 0){
			orders.add(new Order(name, choice, tableNumber, waiter, CookState.outOfFood));
		
		}
		else
			orders.add(new Order(name, choice, tableNumber, waiter, CookState.received));

		
		if(PizzaInventory > 0){
			PizzaInventory--;
		}
		
		
	}
		
		
		
		
		stateChanged();
		
		
		}
	
	public void msgHereIsMoreFood(String food){
		Do("Cook has received more " +  food + " from market");
		if(food == "Steak"){
			SteakInventory = SteakInventory + 2;	
		}
		if(food == "Chicken"){
			ChickenInventory = ChickenInventory + 2;	
		}
		if(food == "Salad"){
			SaladInventory = SaladInventory + 2;	
		}
		if(food == "Pizza"){
			PizzaInventory = PizzaInventory + 2;
		}
		
	}
	public void msgOrderFromMe(Market m){
		Do("received message from marekt to order from " + m.getName());
		//hasInventory = true;
		this.market = m;
		//Do("Market name is " + market.getName());
		state = CookState.buyingFood;
		stateChanged();
		
	}
	public void msgPartOrderFulfilled(String food){
		
		Do("Cook received message from Market to order from another market as well.");
		//hasFood = false;
		//this.market = null;
		//hasInventory = false;
		this.orderThis = food;
		state = CookState.partOrderFulfilled;
		stateChanged();
		
	}
	public void msgNoMarketSupply(String food){
		this.outOfFood = food;
		for(Order o : orders){
			if(o.state == CookState.waitingForOrder){
				o.state = CookState.unfulfilledOrder;
			}
		}
	//	state = CookState.unfulfilledOrder;
		stateChanged();
	}
	
////////SCHEDULER//////
	protected boolean pickAndExecuteAnAction() {
	
	synchronized(orders){
	
		for(Order o : orders){
			
			if(o.getCookState() == CookState.outOfFood){
				
				Do("cook state is out of food");
				noMoreFood(o);
				checkInventory(o.choice);
				o.state= CookState.waitingForOrder;
				return true;
			}
			
			if(o.getCookState() == CookState.received){
				
				cookOrder(o);
				Do("cook order called");
				o.state = CookState.cooking;
				return true;
			}
			
			if(o.getCookState() == CookState.unfulfilledOrder){
				removeItem(o, outOfFood);
				o.state = CookState.nothing;
				return true;
			}
			
		}
	}
	if(state == CookState.inventoryLow){
		//If the inventory of a certain food is low, the cook orders food from the market.
		Do("Inventory of " + orderThis + " is low. Cook is buying more food from market");
		state = CookState.checkingInventory;
		checkInventory(orderThis);
		return true;
	}
	
	
	
	if(state == CookState.buyingFood){
		buyFood(orderThis, market);
		state = CookState.buying;
		return true;
	}
	
	if(state == CookState.partOrderFulfilled){
		Do("Cook is trying to fulfill the rest of the order.");
		market = null;
		hasInventory = 1;
		state = CookState.checkingInventory;
		checkInventory(orderThis);
		//state = CookState.buyingFood;
		return true;
	}
	
	/*
	if(state == CookState.unfulfilledOrder){
		Do("Market was unable to fulfill order for "+ outOfFood + ".");
		state = CookState.nothing;
		removeItem(outOfFood);
		return true;
	}
	*/	return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
		 
		 
	}
	

	///////// ACTIONS/////////////
		private void noMoreFood(Order o){
			//If the cook is out of a certain food, he tells the waiter the customer must reorder. 
			o.waiter.msgNoMoreFood(o);
			Do("Sorry, but I am out of " + o.choice + ". Please ask customer to reorder.");
		}
	
		private void removeItem(Order o, String food){
			Do("Market was unable to fulfill my order for " + food + ". Messaging waiter to tell him.");
			o.waiter.msgRemoveItem(food);
			
		}
		private void cookOrder(final Order o){
			Do("Cook is going to fridge to get ingredients");
			cookGui.DoGoToFridge();
			cookGui.drawFoodChoice(o.choice);
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			int time = 0;
				for(Map.Entry<String, Integer> entry : cookTime.entrySet()){
					if(entry.getKey() == o.choice){
						time = entry.getValue();
						
					}
				}
				Do("cooking food ");
				cookGui.DoGoToCookArea();
				cookGui.drawFoodChoice(o.choice);
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Do("Leaving food on grill");
				cookGui.drawFoodChoice(" ");
				cookGui.DoGoToHome();
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				cookTimer.schedule(new TimerTask(){
				public void run(){
					Do("Cook moving food from grill to plate area");
					cookGui.DoGoToCookArea();
					cookGui.drawFoodChoice(orderThis);
					try {
						atTable.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cookGui.DoGoToPlateArea();
					try {
						atTable.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cookGui.drawFoodChoice(" ");
					Do("" + o.choice + " is done cooking for " + o.getTableNumber());
					o.waiter.msgOrderIsReady(o.getTableNumber());
					orders.remove(o);
					o.state = CookState.cooked;
				cookGui.DoGoToHome();
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				}
			}, time);
			
		}
		
		private void checkInventory(String food){
			//Cook has 3 markets to order from. If a market has at least 1 of the requested items, he orders from that market. 
			
			//Cook checking inventory to decide which market to order from
			synchronized(markets){
			
				if(hasInventory == 0){
					markets.get(0).msgCheckInventory(food, this);
					hasInventory = -1;
				}
				else if(hasInventory == 1){
					
						markets.get(1).msgCheckInventory(food, this);	
					
				}
				
					
			}
			
		}
			
		
			public void buyFood(String food, Market m){
				Do("Ordering " + food + "from " + market.getName());
				
				market.msgOrderSupply(food, this, hasFood, cantPayCashier);	
				hasFood--;
				
			}
			
//UTILITIES		
		public void addMarket(Market m){
			markets.add(m);
			Do("" + m.getName() +" created");
		}
		public void setGui(CookGui g) {
			cookGui = g;
		}

		public CookGui getGui() {
			return cookGui;
		}
 
	
}
	