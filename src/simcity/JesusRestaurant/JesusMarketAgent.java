package simcity.JesusRestaurant;

import agent.Agent;
import restaurant.CookAgent;
import restaurant.CashierAgent.BillState;
import restaurant.gui.MarketGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Market;

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
public class JesusMarketAgent extends Agent implements JesusMarket{
	private static final int prepareTime = 5000;
	private static final int init_inv = 15;
	private int id_cnt = 0;

	public List<stockOrder> stockOrders = Collections.synchronizedList(new ArrayList<stockOrder>());
	public List<Food> foods = Collections.synchronizedList(new ArrayList<Food>());
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());

	public enum BillState {none, sending, sent, paying, owe};

	JesusCookAgent cook;
	JesusCashier cashier;

	public enum orderState {nothing, preparing, done, sending};

	Timer timer = new Timer();	

	private String name;

	public JesusMarketGui marketGui = null;

	public JesusMarketAgent(String name) {
		super();

		this.name = name;

		foods.add(new Food("Pizza", init_inv, prepareTime, 5));
		foods.add(new Food("Salad", init_inv, prepareTime, 3));
		foods.add(new Food("Steak", init_inv, prepareTime, 10));
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setCook(JesusCookAgent c) {
		cook = c;
		cook.msgCheckInventory();
	}

	public void setCashier(JesusCashierAgent ch) {
		cashier = ch;
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
					if(f.inventory < num) {
						f.inventory = 0;
					}
					else {
						f.inventory -= num;
					}
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
	}
	public int getPrepareTime(String n) {
		synchronized(foods){
			for(Food f: foods) {
				if(f.name.equals(n)) {
					return f.prepareTime;
				}
			}
		}
		return 0;
	}

	// Messages

	public void msgNeedRestock (JesusCookAgent cook, String choice, int amount) {
		stockOrders.add(new stockOrder(choice, cook, amount));
		stateChanged();
	}

	public void msgPayingBill (int id, double amount) {
		synchronized(bills){
			for(Bill b: bills){
				if(b.id == id) {
					b.amountPaid = amount;
					b.bS = BillState.paying;
					stateChanged();
				}
			}
		}
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		synchronized(bills){
			for(Bill b: bills){
				if(b.bS == BillState.paying) {
					confirmPayment(b);
					return true;
				}
			}
		}
		synchronized(stockOrders){
			for(stockOrder o: stockOrders) {
				if(o.oState == orderState.nothing) {
					Collections.rotate(stockOrders, -1);
					o.oState = orderState.preparing;
					completeOrder(stockOrders.get(0));
					return true;
				}
			}
		}
		synchronized(stockOrders){
			for(stockOrder o: stockOrders) {
				if(o.oState == orderState.done) {
					o.oState = orderState.sending;
					sendOrder(o);
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

	private void confirmPayment(Bill b) {
		b.subtractAmount();
		if(b.amountDue > 0) {
			b.bS = BillState.owe;
			print("You still owe $" + b.amountDue + ", but you can pay next time.");
		}
		else {
			b.amountDue = 0;
			b.amountPaid = 0;
			b.bS = BillState.none;
			print("Thank you for the payment.");
		}
	}

	private void completeOrder(final stockOrder o) {
		if(noInventory()) {
			print("Sorry, out of everything!");
			o.cook.msgNoStock(name, o.name, true);
			stockOrders.remove(o);
		}
		else if(getInventory(o.name) <= 0) {
			print("Out of " + o.name);
			o.cook.msgNoStock(name, o.name);
			stockOrders.remove(o);
		}
		else if(getInventory(o.name) < o.amount) {
			print("Not enough in stock to complete order. Preparing what's left.");
			Do("Preparing " + o.cook.getName() + "'s stock order of " + getInventory(o.name) + " " + o.name + "s");
			o.amountLeft = o.amount - getInventory(o.name);
			synchronized(foods){
				for(Food f: foods) {
					if(f.name.equals(o.name)) {
						Bill b = new Bill(id_cnt, f.cost*getInventory(o.name));
						bills.add(b);
						cashier.msgHereIsBill(this, b.amountDue, b.id);
						b.bS = BillState.sent;
						id_cnt++;
					}
				}
			}
			subtractInventory(o.name, getInventory(o.name));
			print(getInventory(o.name) + " " + o.name + "(s) left");
			timer.schedule(new TimerTask() {
				public void run() {
					print("Order ready");
					o.oState = orderState.done;
					stateChanged();
				}
			},
			getPrepareTime(o.name));
		}
		else {
			Do("Preparing " + o.cook.getName() + "'s stock order of " + o.amount + " " + o.name + "s");
			o.amountLeft = 0;
			synchronized(foods){
				for(Food f: foods) {
					if(f.name.equals(o.name)) {
						Bill b = new Bill(id_cnt, f.cost*o.amount);
						bills.add(b);
						cashier.msgHereIsBill(this, b.amountDue, b.id);
						b.bS = BillState.sent;
						id_cnt++;
					}
				}
			}
			subtractInventory(o.name, o.amount);
			print(getInventory(o.name) + " " + o.name + "(s) left");
			timer.schedule(new TimerTask() {
				public void run() {
					print("Order ready");
					o.oState = orderState.done;
					stateChanged();
				}
			},
			getPrepareTime(o.name));
		}
	}

	private void sendOrder(stockOrder o) {
		Do("Sending " + o.cook.getName() + "'s stock order");
		o.cook.msgOrderSent(o.name, o.amount, o.amountLeft, name);
		stockOrders.remove(o);
	}

	// The animation DoXYZ() routines


	//utilities

	public void setGui(JesusMarketGui gui) {
		marketGui = gui;
	}

	public JesusMarketGui getGui() {
		return marketGui;
	}

	private class stockOrder {
		String name;
		JesusCookAgent cook;
		int amount;
		int amountLeft;
		orderState oState;

		stockOrder(String n, JesusCookAgent c, int a) {
			name = n;
			cook = c;
			amount = a;
			amountLeft = 0;
			oState = orderState.nothing;
		}
	}

	private class Food {
		String name;
		int inventory;
		int prepareTime;
		int cost;

		Food (String n, int inv, int cT, int c) {
			name = n;
			inventory = inv;
			prepareTime = cT;
			cost = c;
		}
	}

	class Bill {
		double amountDue;
		double amountPaid;
		int id;
		BillState bS;

		public Bill(int i, double aD) {
			id = i;
			amountDue = aD;
			amountPaid = 0;
			bS = BillState.sending;
		}
		public void subtractAmount() {
			amountDue -= amountPaid;
		}
		public void addAmount(double amount) {
			amountDue += amount;
		}
		public void addAmount(double cost, int aF) {
			amountDue += (cost*aF);
		}
	}

	public void updateInventory(Integer stI, Integer sI, Integer pI) {
		updateInventory("Steak", stI);
		updateInventory("Salad", sI);
		updateInventory("Pizza", pI);
	}
}