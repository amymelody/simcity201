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
import simcity.mock.LoggedEvent;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;
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
	private AnjaliCashierRole cashier;
	public List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
	public List<ItemOrder> itemOrders = new ArrayList<ItemOrder>();
	
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
public enum CookState{nothing, received, inventoryLow, checkingInventory, buyingFood, buying, partOrderFulfilled, orderFulfilled, outOfFood, waitingForOrder, unfulfilledOrder, cooking, cooked, delivered}; 

private int SteakInventory = 1;
private int SaladInventory = 1;
private int ChickenInventory = 1;
private int PizzaInventory = 1;

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
		AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Cook has received order for table Number" + tableNumber + "from waiter " + waiter.getName() + "for food " + choice);

	
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
				itemOrders.add(new ItemOrder("Steak", 3));
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
			itemOrders.add(new ItemOrder("Salad", 3));

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
			itemOrders.add(new ItemOrder("Chicken", 3));
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
			itemOrders.add(new ItemOrder("Pizza", 3));

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
	
	public void msgHereIsWhatICanFulfill(List<ItemOrder> orders, boolean canFulfill) {
		AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Market telling me what he can fulfill");
		stateChanged();
	}
	public void msgDelivery(List<ItemOrder> orders){
		AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Market fulfilling order");

		List<ItemOrder> temp = new ArrayList<ItemOrder>();
		for (ItemOrder o : orders) {
			temp.add(o);
		}
		
		for(ItemOrder o : temp){
			if(o.getFoodItem().equals("Steak")){
				SteakInventory += o.getAmount();
				AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Steak Inventory is now " + SteakInventory);

			}
			if(o.getFoodItem().equals("Chicken")){
				ChickenInventory += o.getAmount();
				AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Chicken Inventory is now " + ChickenInventory);

			}
			if(o.getFoodItem().equals("Salad")){
				SaladInventory += o.getAmount();
				AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Salad Inventory is now " + SaladInventory);

			}
			if(o.getFoodItem().equals("Pizza")){
				PizzaInventory += o.getAmount();
				AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Pizza Inventory is now " + PizzaInventory);

			}
		}
		state = CookState.orderFulfilled;
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
				checkInventory();
				o.state= CookState.waitingForOrder;
				return true;
			}
			
			if(o.getCookState() == CookState.received){
				
				cookOrder(o);
				AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Cook Order called");
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
		//If the inventory of a certain food is low, the cook orders food from the market
		AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Inventory of " + orderThis + " is low. Cook is buying more food from market");

		state = CookState.checkingInventory;
		checkInventory();
		return true;
	}
	
	
	
		
	
	if(state == CookState.partOrderFulfilled){
		Do("Cook is trying to fulfill the rest of the order.");
		market = null;
		hasInventory = 1;
		state = CookState.checkingInventory;
		checkInventory();
		//state = CookState.buyingFood;
		return true;
	}
	
	
	
		return false;
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
				AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Cooking food");
				cookGui.DoGoToCookArea();
				cookGui.drawFoodChoice(o.choice);
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Leaving food on grill");

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
					AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Cook moving food from grill to plate area");

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
					try {
						atTable.acquire();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "" + o.choice + " is done cooking for " + o.getTableNumber());

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
		
		private void checkInventory(){
			AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "inventory low, ordering from market");

			int index = markets.size()-1;
			if (markets.size() > 1) {
				for (int i = markets.size()-2; i>=0; i--) {
					if (markets.get(i).orderedFrom <= markets.get(index).orderedFrom && (unitTesting || person.businessOpen(markets.get(i).marketName))) {
						index = i;
					}
				}
			}
			
			if (person.businessOpen(markets.get(index).marketName)) {
				AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "I am ordering from " + markets.get(index).market.getName());
								
				markets.get(index).market.msgIWantDelivery(this, cashier, itemOrders, location);
				markets.get(index).incrementOrderedFrom();
				markets.get(index).ordered = true;
				itemOrders.clear();
			}
		}
			
		
			
			
//UTILITIES		
		
		public void setGui(AnjaliCookGui g) {
			cookGui = g;
		}

		public AnjaliCookGui getGui() {
			return cookGui;
		}

	
		public void addMarket(MarketCashier m, String n) {
			markets.add(new Market(m,n));
		}

	
		
		private class Market {
			MarketCashier market;
			int orderedFrom;
			String marketName;
			boolean ordered;
			
			Market(MarketCashier m, String n) {
				market = m;
				orderedFrom = 0;
				marketName = n;
				ordered = false;
			}
			
			public void incrementOrderedFrom() {
				orderedFrom++;
			}
		}


	
		public void setCashier(AnjaliCashierRole cashier) {
			this.cashier = cashier;
		}
		@Override
		public void msgHereIsWhatICanFulfill(List<ItemOrder> items,
				boolean canFulfill, MarketCashier mC) {
			// TODO Auto-generated method stub
			
		}
		
 
	
}
	