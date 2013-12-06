package simcity.joshrestaurant;

import java.util.*;

import simcity.mock.LoggedEvent;
import simcity.ItemOrder;
import simcity.RestCookRole;
import simcity.joshrestaurant.gui.JoshCookGui;
import simcity.role.JobRole;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;
import simcity.interfaces.Person;
import simcity.interfaces.MarketCashier;


/**
 * Restaurant Cook Agent
 */

public class JoshCookRole extends RestCookRole {
	public List<Order> orders
	= new ArrayList<Order>();
	public List<MyMarket> markets = new ArrayList<MyMarket>();
	public List<ItemOrder> itemOrders = new ArrayList<ItemOrder>();

	private JoshHostRole host;
	private JoshCashierRole cashier;
	private String name = null;
	private Timer timer = new Timer();
	private boolean orderedItems;
	private JoshCookGui cookGui;
	private RevolvingStandMonitor stand;
	
	public boolean unitTesting = false;
	private boolean working;
	private String location = "joshRestaurant";
	
	Food steak = new Food("Steak", 15, 3, 1, 1);
	Food chicken = new Food("Chicken", 20, 3, 1, 1);
	Food salad = new Food("Salad", 5, 3, 2, 1);
	Food pizza = new Food("Pizza", 10, 3, 3, 1);
	
	public Map<String, Food> foods = new HashMap<String, Food>();

	public enum FoodState
	{Enough, MustBeOrdered, Ordered, WaitingForOrder, ReceivedOrder};

	public JoshCookRole() {
		super();
		working = false;
		orderedItems = false;
		
		foods.put("Steak", steak);
		foods.put("Chicken", chicken);
		foods.put("Salad", salad);
		foods.put("Pizza", pizza);
	}
	
	public void setPerson(Person p) {
		super.setPerson(p);
		name = person.getName();
	}

	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		name = n;
	}

	public List getOrders() {
		return orders;
	}
	
	public void setHost(JoshHostRole h) {
		host = h;
	}
	
	public void setStand(RevolvingStandMonitor s) {
		stand = s;
	}
	
	public void setCashier(JoshCashierRole c) {
		cashier = c;
	}
	
	public void setGui(JoshCookGui g) {
		cookGui = g;
	}
	
	public void addMarket(MarketCashier m, String n) {
		markets.add(new MyMarket(m, n));
	}
	
	// Messages

	public void msgStartShift() {
		working = true;
		stateChanged();
	}
	
	public void msgEndShift() {
		working = false;
		stateChanged();
	}
	
	public void msgHereIsOrder(JoshWaiterRole waiter, String choice, int table) {
		orders.add(new Order(waiter, choice, table, OrderState.Pending));
		stateChanged();
	}
	
	public void msgHereIsWhatICanFulfill(List<ItemOrder> orders, boolean canFulfill) {
		log.add(new LoggedEvent("Received msgHereIsWhatICanFulfill"));
		if (canFulfill == false) {
			AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Oh you can't fulfill this order");
		}
		for (Food f : foods.values()) {
			if (f.state == FoodState.Ordered) {
				f.state = FoodState.MustBeOrdered;
			}
		}
		for (ItemOrder o : orders) {
			foods.get(o.getFoodItem()).setState(FoodState.WaitingForOrder);
		}
		stateChanged();
	}
	
	public void msgDelivery(List<ItemOrder> orders) {
		log.add(new LoggedEvent("Received msgDelivery"));
		List<ItemOrder> temp = new ArrayList<ItemOrder>();
		for (ItemOrder o : orders) {
			temp.add(o);
		}
		for (ItemOrder o : temp) {
			foods.get(o.getFoodItem()).amount += o.getAmount();
			AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, o.getFoodItem() + " inventory: " + foods.get(o.getFoodItem()).amount);
			foods.get(o.getFoodItem()).state = FoodState.ReceivedOrder;
		}
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try {
			if (!working) {
				leaveRestaurant();
				return true;
			}
	//		if (unitTesting == true && orderedItems == false) {
			if (orderedItems == false) {
				orderedItems = true;
				orderFoodFromMarket();
				return true;
			}
			for (Food food : foods.values()) {
				if (food.getState() == FoodState.ReceivedOrder) {
					addFood(food);
					return true;
				}
			}
			for (Food food : foods.values()) {
				if (food.getState() == FoodState.MustBeOrdered) {
					orderFoodFromMarket();
					return true;
				}
			}
			for (Order order : orders) {
				if (order.getState() == OrderState.Done) {
					plateIt(order);
					return true;
				}
			}
			for (Order order : orders) {
				if (order.getState() == OrderState.Pending) {
					cookIt(order);
					return true;
				}
			}
		} catch (ConcurrentModificationException e) {
			return false;
		}

		retrieveOrderFromStand();
		return false;
	}

	// Actions
	
	private void leaveRestaurant() {
		person.msgLeftDestination(this);
	}
	
	private void retrieveOrderFromStand() {
		Order order = stand.remove();
		if (order != null) {
			AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "Picking up order from stand");
			orders.add(order);
		}
	}

	private void cookIt(Order o) {
		if (foods.get(o.choice).getAmount() == 0) {
			AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "We're out of " + o.choice);
			o.waiter.msgOutOfFood(o.choice, o.table);
			o.setState(OrderState.Finished);
			return;
		}
		o.setState(OrderState.Cooking);
		cookGui.DoCookFood(o.choice);
		timer.schedule(new CookingTimerTask(o) {
			@Override
			public void run() {
				order.setState(OrderState.Done);
				stateChanged();
			}
		},
		foods.get(o.choice).getCookingTime() * 400);
		
		foods.get(o.choice).setAmount(foods.get(o.choice).getAmount()-1);
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, foods.get(o.choice).type + " inventory: " + foods.get(o.choice).amount);
//		if (unitTesting && foods.get(o.choice).amount <= foods.get(o.choice).low && foods.get(o.choice).state == FoodState.Enough) {
		if (foods.get(o.choice).amount <= foods.get(o.choice).low && foods.get(o.choice).state == FoodState.Enough) {
			foods.get(o.choice).setState(FoodState.MustBeOrdered);
		}
	}
	
	private void plateIt(Order o) {
		AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, o.choice + " is done");
		cookGui.DoPlateFood(o.choice);
		o.getWaiter().msgOrderDone(o.getChoice(), o.getTable());
		o.setState(OrderState.Finished);
	}
	
	private void orderFoodFromMarket() {
//		if (unitTesting) {
			for (Food food : foods.values()) {
				if ((food.getState() == FoodState.MustBeOrdered || food.getState() == FoodState.Enough) && food.amount <= food.low) {
					itemOrders.add(new ItemOrder(food.type, food.capacity - food.amount));
					food.setState(FoodState.Ordered);
				}
			}
			int index = markets.size()-1;
			if (markets.size() > 1) {
				for (int i = markets.size()-2; i>=0; i--) {
					if (markets.get(i).orderedFrom <= markets.get(index).orderedFrom && (unitTesting || person.businessOpen(markets.get(i).marketName))) {
						index = i;
					}
				}
			}
			
			if (person.businessOpen(markets.get(index).marketName)) {
				AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "I am ordering from " + markets.get(index).market.getName());
				for (ItemOrder io : itemOrders) {
					AlertLog.getInstance().logMessage(AlertTag.JOSH_RESTAURANT, name, "I need " + io.getAmount() + " " + io.getFoodItem() + "s");
				}
				markets.get(index).market.msgIWantDelivery(this, cashier, itemOrders, location);
				markets.get(index).incrementOrderedFrom();
				itemOrders.clear();
			}
//		}
	}
	
	private void addFood(Food f) {
		for (Food food : foods.values()) {
			if (food == f) {
				food.setState(FoodState.Enough);
				host.msgReceivedOrder(food.type);
			}
		}
	}
	

	//utilities
	
	private class MyMarket {
		MarketCashier market;
		int orderedFrom;
		String marketName;
		
		MyMarket(MarketCashier m, String n) {
			market = m;
			orderedFrom = 0;
			marketName = n;
		}
		
		public void incrementOrderedFrom() {
			orderedFrom++;
		}
	}
	
	public class Food {
		String type;
		int cookingTime;
		int amount;
		int low;
		int capacity;
		FoodState state;
		
		Food(String t, int c, int cap, int a, int l) {
			type = t;
			cookingTime = c;
			amount = a;
			capacity = cap;
			low = l;
			state = FoodState.Enough;
		}
		
		String getType() {
			return type;
		}
		
		int getCookingTime() {
			return cookingTime;
		}
		
		void setAmount(int a) {
			amount = a;
		}
		
		public int getAmount() {
			return amount;
		}
		
		int getLow() {
			return low;
		}
		
		int getCapacity() {
			return capacity;
		}
		
		public FoodState getState() {
			return state;
		}
		
		void setState(FoodState s) {
			state = s;
		}
	}
	
	private class CookingTimerTask extends TimerTask {
		Order order;
		
		CookingTimerTask(Order o) {
			order = o;
		}
		
		public void run() {
			order = order;
		}
	}
}

