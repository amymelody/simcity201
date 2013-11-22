package simcity.joshrestaurant;

import java.util.*;

import simcity.ItemOrder;
import simcity.RestCookRole;
import simcity.joshrestaurant.gui.JoshCookGui;
import simcity.PersonAgent;
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
	private String name;
	private Timer timer = new Timer();
	private boolean orderedItems;
	private JoshCookGui cookGui;
	private boolean working;
	private String location = "joshRestaurant";
	
	Food steak = new Food("steak", 15, 3, 1, 1);
	Food chicken = new Food("chicken", 20, 3, 1, 1);
	Food salad = new Food("salad", 5, 3, 2, 1);
	Food pizza = new Food("pizza", 10, 3, 3, 1);
	
	Map<String, Food> foods = new HashMap<String, Food>();
	
	public enum OrderState
	{Pending, Cooking, Done, Finished};
	public enum FoodState
	{Enough, MustBeOrdered, Ordered, WaitingForOrder, ReceivedOrder};

	public JoshCookRole() {
		super();
		name = person.getName();
		working = false;
		orderedItems = false;
		
		foods.put("steak", steak);
		foods.put("chicken", chicken);
		foods.put("salad", salad);
		foods.put("pizza", pizza);
	}
	
	public void setPerson(PersonAgent p) {
		super.setPerson(p);
		name = person.getName();
	}

	public String getName() {
		return name;
	}

	public List getOrders() {
		return orders;
	}
	
	public void setHost(JoshHostRole h) {
		host = h;
	}
	
	public void setGui(JoshCookGui g) {
		cookGui = g;
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
	
	public void addMarket(MarketCashier m) {
		markets.add(new MyMarket(m));
	}
	
	public void msgHereIsOrder(JoshWaiterRole waiter, String choice, int table) {
		orders.add(new Order(waiter, choice, table, OrderState.Pending));
		stateChanged();
	}
	
	public void msgHereIsWhatICanFulfill(List<ItemOrder> orders, boolean canFulfill) {
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
		List<ItemOrder> temp = new ArrayList<ItemOrder>();
		for (ItemOrder o : orders) {
			temp.add(o);
		}
		for (ItemOrder o : temp) {
			foods.get(o.getFoodItem()).amount += o.getAmount();
			print(o.getFoodItem() + " inventory: " + foods.get(o.getFoodItem()).amount);
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

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void leaveRestaurant() {
		person.msgLeftDestination(this);
	}

	private void cookIt(Order o) {
		if (foods.get(o.choice).getAmount() == 0) {
			print("We're out of " + o.choice);
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
		foods.get(o.choice).getCookingTime() * 1000);
		foods.get(o.choice).setAmount(foods.get(o.choice).getAmount()-1);
		print(foods.get(o.choice).type + " inventory: " + foods.get(o.choice).amount);
		if (foods.get(o.choice).amount <= foods.get(o.choice).low && foods.get(o.choice).state == FoodState.Enough) {
			foods.get(o.choice).setState(FoodState.MustBeOrdered);
		}
	}
	
	private void plateIt(Order o) {
		print(o.choice + " is done");
		cookGui.DoPlateFood(o.choice);
		o.getWaiter().msgOrderDone(o.getChoice(), o.getTable());
		o.setState(OrderState.Finished);
	}
	
	private void orderFoodFromMarket() {
		for (Food food : foods.values()) {
			if ((food.getState() == FoodState.MustBeOrdered || food.getState() == FoodState.Enough) && food.amount <= food.low) {
				itemOrders.add(new ItemOrder(food.type, food.capacity - food.amount));
				food.setState(FoodState.Ordered);
			}
		}
		int index = markets.size()-1;
		if (markets.size() > 1) {
			for (int i = markets.size()-2; i>=0; i--) {
				if (markets.get(i).orderedFrom <= markets.get(index).orderedFrom) {
						index = i;
				}
			}
		}
		print("I am ordering from " + markets.get(index).market.getName());
		for (ItemOrder io : itemOrders) {
			print("I need " + io.getAmount() + " " + io.getFoodItem() + "s");
		}
		markets.get(index).market.msgIWantDelivery(this, itemOrders, location);
		markets.get(index).incrementOrderedFrom();
		itemOrders.clear();
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
		
		MyMarket(MarketCashier m) {
			market = m;
			orderedFrom = 0;
		}
		
		public void incrementOrderedFrom() {
			orderedFrom++;
		}
	}

	private class Order {
		JoshWaiterRole waiter;
		int table;
		private OrderState state;
		String choice;

		Order(JoshWaiterRole w, String c, int t, OrderState s) {
			waiter = w;
			choice = c;
			table = t;
			state = s;
		}

		JoshWaiterRole getWaiter() {
			return waiter;
		}
		
		public int getTable() {
			return table;
		}
		
		OrderState getState() {
			return state;
		}
		
		void setState(OrderState s) {
			state = s;
		}
		
		String getChoice() {
			return choice;
		}
		
		void setChoice(String c) {
			choice = c;
		}
	}
	
	private class Food {
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
		
		int getAmount() {
			return amount;
		}
		
		int getLow() {
			return low;
		}
		
		int getCapacity() {
			return capacity;
		}
		
		FoodState getState() {
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

