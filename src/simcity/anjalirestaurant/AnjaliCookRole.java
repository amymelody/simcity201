package simcity.anjalirestaurant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simcity.ItemOrder;
import simcity.RestCookRole;
import simcity.anjalirestaurant.gui.AnjaliCookGui;
import simcity.anjalirestaurant.interfaces.AnjaliCook;
import simcity.anjalirestaurant.interfaces.AnjaliMarket;
import simcity.anjalirestaurant.interfaces.AnjaliWaiter;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.Person;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.anjalirestaurant.AnjaliWaiterRole;
import simcity.anjalirestaurant.RevolvingStandMonitor;

/**
 * Restaurant Cook Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios

public class AnjaliCookRole extends RestCookRole implements AnjaliCook{
	static final int NTABLES = 3;//a global for the number of tables.
	
	private boolean unitTesting = false;
	private boolean working;
	private String location = "anjaliRestaurant";
	private boolean orderedItems = false;
	private RevolvingStandMonitor stand;
	public AnjaliCookRole(){
		super();
		working = false;
		orderedItems = false;
	}
	public AnjaliCookRole(String name){
		this.name = name;
	}
	public void setPerson(Person p){
		super.setPerson(p);
		name = person.getName();
	}
	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	Timer cookTimer = new Timer();
	private AnjaliWaiterRole waiter;
	private String outOfFood;
	public List<AnjaliMarket> markets = Collections.synchronizedList(new ArrayList<AnjaliMarket>());
	private AnjaliCookGui cookGui;
	public AnjaliMarket market; 
	private int hasFood = 1;
	private int hasInventory = 0;
	private boolean cantPayCashier = false;
	public String orderThis;
	public class Order{
		
		 String choice;
		 AnjaliWaiter waiter;
		 int tableNumber;
		 CookState state;
		 String customerName;
		
		
		Order(String name, String c, int tn, AnjaliWaiter w, CookState cs){
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

private int SteakInventory = 3;
private int SaladInventory = 3;
private int ChickenInventory = 3;
private int PizzaInventory = 3;

private CookState state = CookState.nothing;
	
public void setStand(RevolvingStandMonitor s) {
	stand = s;
}


	

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	private final Map<String, Integer> cookTime;{
		cookTime = new HashMap<String, Integer>();
		cookTime.put("Steak", 2000);
		cookTime.put("Chicken", 2000);
		cookTime.put("Salad" , 2000);
		cookTime.put("Pizza", 2000);			
	}
	
	public Map<String, Integer> getCookTime(){
		return cookTime;
	}

	public void setWaiter(AnjaliWaiterRole waiter){
		this.waiter = waiter;
	}
/////MESSAGES////////
	
	public void msgStartShift(){
		working = true;
		stateChanged();
	}
	
	public void msgEndShift(){
		working = false;
		stateChanged();
	}
	public void msgAtTable(){//from animation
		atTable.release();
		
		//Do("released from table");	
	}
	public void msgHereIsOrder(String name, String choice, int tableNumber, AnjaliWaiter waiter){
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
	public void msgOrderFromMe(AnjaliMarket m){
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
	public boolean pickAndExecuteAnAction() {
	if(!working){
		leaveRestaurant();
		return true;
	}
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
	private void leaveRestaurant(){
		person.msgLeftDestination(this);
	}
	
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
			
		
			public void buyFood(String food, AnjaliMarket m){
				Do("Ordering " + food + "from " + market.getName());
				
				market.msgOrderSupply(food, this, hasFood, cantPayCashier);	
				hasFood--;
				
			}
			
//UTILITIES		
		public void addMarket(AnjaliMarket m){
			markets.add(m);
			Do("" + m.getName() +" created");
		}
		public void setGui(AnjaliCookGui g) {
			cookGui = g;
		}

		public AnjaliCookGui getGui() {
			return cookGui;
		}

		@Override
		public void addMarket(MarketCashier m, String n) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgHereIsOrder(JoshWaiterRole waiter, String choice,
				int table) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgDelivery(List<ItemOrder> order) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void msgHereIsWhatICanFulfill(List<ItemOrder> items,
				boolean canFulfill) {
			// TODO Auto-generated method stub
			
		}
		
		
 
	
}
	