package simcity.JesusRestaurant;

import agent.Agent;
import restaurant.MarketAgent;
import restaurant.gui.CookGui;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class JesusCookAgent extends Agent {
	private static final int steakTime = 7000;
	private static final int saladTime = 4000;
	private static final int pizzaTime = 2000;
	private static final int init_inv = 5;
	private static final int restockAmount = 10;

	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<Food> foods = Collections.synchronizedList(new ArrayList<Food>());
	public List<myMarket> markets = Collections.synchronizedList(new ArrayList<myMarket>());
	public JesusHostAgent host;
	public JesusCashierAgent cashier;

	public enum orderState {waiting, preparing, ready};
	public enum stockState {none, checkFood, outOfFood, ordered, stockReplenished};
	stockState sState = stockState.none;
	boolean open = false;

	Timer timer = new Timer();

	private String name;

	public JesusCookGui cookGui = null;

	public JesusCookAgent(String name) {
		super();

		this.name = name;

		foods.add(new Food("Pizza", init_inv, pizzaTime));
		foods.add(new Food("Salad", init_inv, saladTime));
		foods.add(new Food("Steak", init_inv, steakTime));
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setHost(JesusHostAgent h) {
		host = h;
	}

	public void setCashier(JesusCashierAgent ch) {
		cashier = ch;
	}
	
	public void addMarket(JesusMarketAgent m) {
		markets.add(new myMarket(m));
	}

	public boolean noInventory() {
		synchronized(foods){
			for(Food f: foods) {
				if(f.inventory > 0) {
					return false;
				}
			}
		}
		return true;
	}

	public int getInventory(String n) {
		synchronized(foods){
			for(Food f: foods) {
				if(f.name.equals(n)) {
					return f.inventory;
				}
			}
		}
		return 0;
	}
	public void addInventory(String n, int num) {
		synchronized(foods){
			for(Food f: foods) {
				if(f.name.equals(n)) {
					f.inventory += num;
				}
			}
		}
	}
	public void subtractInventory(String n, int num) {
		synchronized(foods){
			for(Food f: foods) {
				if(f.name.equals(n)) {
					f.inventory -= num;
				}
			}
		}
	}
	public void updateInventory(String n, int num) {
		synchronized(foods){
			for(Food f: foods) {
				if(f.name.equals(n)) {
					f.inventory = num;
				}
			}
		}
		checkInventory();
	}
	public int getCookTime(String n) {
		synchronized(foods){
			for(Food f: foods) {
				if(f.name.equals(n)) {
					return f.cookTime;
				}
			}
		}
		return 0;
	}
	public boolean allMarketsOut() {
		synchronized(markets){
			for(myMarket m: markets) {
				for(Map.Entry<String, Boolean> entry: m.outStock.entrySet()) {
					if(!entry.getValue())
						return false;
				}
			}
		}
		return true;
	}

	// Messages

	public void msgGotPlate(String foodChoice) {
		cookGui.GotFood(foodChoice);
	}
	
	public void msgCheckInventory() {
		sState = stockState.checkFood;
		stateChanged();
	}

	public void msgCookOrder(JesusWaiterAgent wait, String choice, String customerName) {
		orders.add(new Order(choice, wait, customerName));
		stateChanged();
	}

	public void msgNoStock(String mName, String foodName, boolean everything) {
		synchronized(markets){
			for(myMarket m: markets) {
				if(m.market.getName().equals(mName)) {
					for(Map.Entry<String, Boolean> entry: m.outStock.entrySet()) {
						m.outStock.put(entry.getKey(), true);
					}
					for(Food f: foods) {
						if(f.name.equals(foodName)) {
							f.needsRestock = true;
						}
					}
					sState = stockState.outOfFood;
					stateChanged();
				}
			}
		}
	}
	public void msgNoStock(String mName, String foodName) {
		synchronized(markets){
			for(myMarket m: markets) {
				if(m.market.getName().equals(mName)) {
					m.outStock.put(foodName, true);
					for(Food f: foods) {
						if(f.name.equals(foodName)) {
							f.needsRestock = true;
						}
					}
					sState = stockState.outOfFood;
					stateChanged();
				}
			}
		}
	}
	public void msgOrderSent(String foodName, int amt, int amtLeft, String mName) {
		if(!open)
			open = true;
		synchronized(foods){
			for(Food f: foods) {
				if(f.name.equals(foodName)) {
					addInventory(foodName, amt);
					if(amtLeft == 0) {
						sState = stockState.stockReplenished;
						f.amtLeft = 0;
						f.needsRestock = false;
					}
					else {
						synchronized(markets) {
							for(myMarket m: markets) {
								if(m.market.getName().equals(mName)){
									m.outStock.put(foodName, true);
									f.needsRestock = true;
									f.amtLeft = amtLeft;
									sState = stockState.outOfFood;
								}
							}
						}
					}
					stateChanged();
				}
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if(sState == stockState.checkFood) {
			sState = stockState.none;
			checkInventory();
		}

		try{
			for(Order o: orders) {
				if(o.oState == orderState.ready) {
					orderReady(o);
					return true;
				}
			}
		} catch(ConcurrentModificationException e) {
			return true;
		}
		try{
			for(Order o: orders) {
				if(o.oState == orderState.waiting) {
					prepareFood(o);
					return true;
				}
			}
		} catch(ConcurrentModificationException e) {
			return true;
		}
		synchronized(foods){
			for(Food f: foods) {
				if(f.needsRestock) {
					for(myMarket m: markets) {
						if(!m.outStock.get(f.name)) {
							restock(f, m);
							return true;
						}
					}
				}
			}
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	public void checkInventory() {
		if(noInventory() && !markets.isEmpty()) {
			host.msgClosed();
			for(Food f: foods) {
				restock(f, markets.get(0));
			}
			host.msgOpen();
			open = true;
		}
		else if(noInventory()) {
			host.msgClosed();
			open = false;
		}
		else {
			host.msgOpen();
			open = true;
		}
	}

	private void prepareFood(final Order o) {
		if(noInventory()) {
			print("Out of everything!");
			o.waiter.msgNoFood();
			sState = stockState.outOfFood;
			synchronized(foods){
				for(Food f: foods) {
					if(f.name.equals(o.name)) {
						f.needsRestock = true;
					}
				}
			}
			orders.remove(o);
			host.msgClosed();
			open = false;
		}
		else if(getInventory(o.name) <= 0) {
			print("Out of " + o.name);
			o.waiter.msgOutOfFood(o.name);
			sState = stockState.outOfFood;
			synchronized(foods){
				for(Food f: foods) {
					if(f.name.equals(o.name)) {
						f.needsRestock = true;
					}
				}
			}
			orders.remove(o);
		}
		else {
			if(getInventory(o.name) <= 5) {
				synchronized(foods){
					for(Food f: foods) {
						if(f.name.equals(o.name))
							f.needsRestock = true;
					}
				}
			}
			o.oState = orderState.preparing;
			subtractInventory(o.name, 1);
			Do("Preparing " + o.custName + "'s " + o.name);
			cookGui.DoCookFood(o.name);
			print(getInventory(o.name) + " " + o.name + "(s) left");
			timer.schedule(new TimerTask() {
				public void run() {
					print("Order ready");
					o.oState = orderState.ready;
					stateChanged();
					cookGui.DoPlateFood(o.name);
				}
			},
			getCookTime(o.name));
		}
	}

	private void orderReady(Order o) {
		o.oState = orderState.waiting;
		print("Waiter: " + o.waiter.getName() + ", Food: " + o.name + ", Customer: " + o.custName);
		o.waiter.msgOrderReady(o.name, o.custName);
		orders.remove(o);
	}

	public void restock(Food f, myMarket m) {
		if(f.amtLeft == 0)
			m.market.msgNeedRestock(this, f.name, restockAmount);
		else
			m.market.msgNeedRestock(this, f.name, f.amtLeft);
		f.needsRestock = false;
		sState = stockState.ordered;
	}

	// The animation DoXYZ() routines


	//utilities

	public void setGui(JesusCookGui gui) {
		cookGui = gui;
	}

	public JesusCookGui getGui() {
		return cookGui;
	}

	private class Order {
		String name;
		JesusWaiterAgent waiter;
		String custName;
		orderState oState;

		Order(String n, JesusWaiterAgent w, String cN) {
			name = n;
			waiter = w;
			custName = cN;
			oState = orderState.waiting;
		}
	}

	private class Food {
		String name;
		int inventory;
		int cookTime;
		int amtLeft;
		boolean needsRestock;

		Food (String n, int inv, int cT) {
			name = n;
			inventory = inv;
			cookTime = cT;
			amtLeft = 0;
			if(inventory > 0)
				needsRestock = false;
			else
				needsRestock = true;
		}
	}

	private class myMarket {
		JesusMarketAgent market;
		Map<String, Boolean> outStock;

		myMarket(JesusMarketAgent m) {
			market = m;
			outStock = new HashMap<String, Boolean>();
			outStock.put("Steak", false);
			outStock.put("Pizza", false);
			outStock.put("Salad", false);
		}

		public void outOfFood(String name) {
			outStock.put(name, true);
		}
	}

	public void updateInventory(Integer stI, Integer sI, Integer pI) {
		updateInventory("Steak", stI);
		updateInventory("Salad", sI);
		updateInventory("Pizza", pI);
	}
}