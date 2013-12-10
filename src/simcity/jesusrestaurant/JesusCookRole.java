package simcity.jesusrestaurant;

import simcity.ItemOrder;
import simcity.PersonAgent;
import simcity.role.JobRole;
import simcity.jesusrestaurant.JesusCookRole;
import simcity.jesusrestaurant.gui.JesusCookGui;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.market.MarketCashierRole;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.RestCook;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class JesusCookRole extends JobRole implements RestCook {
	private static final int steakTime = 7000;
	private static final int saladTime = 4000;
	private static final int pizzaTime = 2000;
	private static final int init_inv = 5;
	private static final int restockAmount = 10;

	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public List<ItemOrder> needToRestock = Collections.synchronizedList(new ArrayList<ItemOrder>());
	public List<Food> foods = Collections.synchronizedList(new ArrayList<Food>());
	public List<myMarket> markets = Collections.synchronizedList(new ArrayList<myMarket>());
	public JesusHostRole host;
	public JesusCashierRole cashier;
	boolean start, working;
	public enum orderState {waiting, preparing, ready};
	public enum stockState {none, checkFood, outOfFood, ordered, stockReplenished};
	stockState sState = stockState.none;
	boolean open = false;
	myMarket currentMarket;

	Timer timer = new Timer();

	private String name;

	public JesusCookGui jesusCookGui = null;

	public JesusCookRole() {
		super();

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
	
	public void setPerson(PersonAgent p) {
		super.setPerson(p);
		name = p.getName();
	}

	public void setHost(JesusHostRole h) {
		host = h;
	}

	public void setCashier(JesusCashierRole ch) {
		cashier = ch;
	}
	
	public void addMarket(MarketCashier c, String mName) {
		markets.add(new myMarket(c, mName));
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
					f.amtLeft -= num;
					if(f.amtLeft > 0) {
						f.needsRestock = true;
					}
					else {
						f.amtLeft = 0;
						f.needsRestock = false;
					}
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
	public boolean marketOut(myMarket m) {
		for(Map.Entry<String, Boolean> entry: m.outStock.entrySet()) {
			if(!entry.getValue())
				return false;
		}
		return true;
	}

	// Messages
	public void msgStartShift() {
		working = true;
		start = true;
		stateChanged();
	}

	public void msgEndShift() {
		working = false;
		stateChanged();
	}
	public void msgGotPlate(String foodChoice) {
		jesusCookGui.GotFood(foodChoice);
	}
	
	public void msgCheckInventory() {
		sState = stockState.checkFood;
		stateChanged();
	}

	public void msgCookOrder(JesusWaiterRole wait, String choice, String customerName) {
		orders.add(new Order(choice, wait, customerName));
		stateChanged();
	}
	
	public void msgHereIsWhatICanFulfill(List<ItemOrder> orders, boolean canFulfill) {
		if(canFulfill) {
			
		}
		else {
			for(ItemOrder iO: orders) {
				currentMarket.outStock.put(iO.getFoodItem(), true);
				for(Food f: foods) {
					if(f.name.equals(iO.getFoodItem())) {
						f.needsRestock = true;
					}
				}
			}
			sState = stockState.outOfFood;
		}
		stateChanged();
	}

	public void msgDelivery(List<ItemOrder> orders) {
		boolean notFulfilled = false;
		for(ItemOrder iO: orders) {
			addInventory(iO.getFoodItem(), iO.getAmount());
			if(iO.getAmount() == 0) {
				notFulfilled = true;
				for(Food f: foods) {
					if(f.name.equals(iO.getFoodItem())) {
						f.needsRestock = true;
					}
				}
			}
		}
		if(notFulfilled) {
			sState = stockState.outOfFood;
		}
		else {
			sState = stockState.stockReplenished;
		}
	}
	
	public void left() {
		person.msgLeftDestination(this);
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(!working) {
			leaveRestaurant();
			return true;
		}
		if(start) {
			startWork();
			return true;
		}
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
			for(myMarket m: markets) {
				if(!marketOut(m)) {
					for(Food f: foods) {
						if(!m.outStock.get(f.name)) {
							needToRestock.add(new ItemOrder(f.name, f.amtLeft));
						}
					}
					restock(needToRestock, m);
					return true;
				}
			}
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void startWork() {
		start = false;
		jesusCookGui.work();
	}
	private void leaveRestaurant() {
		jesusCookGui.leave();
	}
	private void checkInventory() {
		if(noInventory() && !markets.isEmpty()) {
			host.msgClosed();
			for(Food f: foods) {
				f.amtLeft = restockAmount;
				needToRestock.add(new ItemOrder(f.name, f.amtLeft));
			}
			markets.get(0).market.msgIWantDelivery(this, cashier, needToRestock, getJobLocation());
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
			jesusCookGui.DoCookFood(o.name);
			print(getInventory(o.name) + " " + o.name + "(s) left");
			timer.schedule(new TimerTask() {
				public void run() {
					print("Order ready");
					o.oState = orderState.ready;
					stateChanged();
					jesusCookGui.DoPlateFood(o.name);
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

	public void restock(List<ItemOrder> list, myMarket m) {
		synchronized(foods) {
			for(Food f: foods) {
				if(f.amtLeft == 0)
					f.amtLeft = restockAmount;
				f.needsRestock = false;
			}
		}
		currentMarket = m;
		m.market.msgIWantDelivery(this, cashier, list, getJobLocation());
		sState = stockState.ordered;
	}

	// The animation DoXYZ() routines


	//utilities

	public void setGui(JesusCookGui gui) {
		jesusCookGui = gui;
	}

	public JesusCookGui getGui() {
		return jesusCookGui;
	}

	private class Order {
		String name;
		JesusWaiterRole waiter;
		String custName;
		orderState oState;

		Order(String n, JesusWaiterRole w, String cN) {
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
		MarketCashier market;
		String name;
		Map<String, Boolean> outStock;

		myMarket(MarketCashier m, String mName) {
			market = m;
			name = mName;
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