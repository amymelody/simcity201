package simcity.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.role.JobRole;
import simcity.role.Role;
import simcity.ItemOrder;
import simcity.PersonAgent;
import simcity.interfaces.RestCashier;
import simcity.interfaces.RestCook;
import simcity.market.Order.OrderState;
import simcity.market.gui.MarketCashierGui;
import simcity.market.gui.MarketCustomerGui;
import simcity.market.interfaces.MarketCashier;
import simcity.market.interfaces.MarketCustomer;
import simcity.market.interfaces.MarketDeliverer;
import simcity.market.interfaces.MarketEmployee;

public class MarketCashierRole extends JobRole implements MarketCashier {

	/* Constructor */
	String name;

	public MarketCashierRole() {
		super();
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


	/* Hacks */
	public void setEmployees(MarketEmployee e, int s) {
		employees.add(new myEmployee(e, s));
	}
	public void setEmployees(MarketEmployee e, int s, boolean u, boolean w) {
		employees.add(new myEmployee(e, s, u, w));
	}
	public void setDeliverers(MarketDeliverer d, int s) {
		deliverers.add(new myDeliverer(d, s));
	}
	public void setDeliverers(MarketDeliverer d, int s, boolean u, boolean w) {
		deliverers.add(new myDeliverer(d, s, u, w));
	}
	public void setOrder(MarketCustomer c, List<ItemOrder> i) {
		orders.add(new Order(c, i));
	}
	public void setSalary(int s) {
		salary = s;
	}
	public void setMarketState(boolean open) {
		if(open) {
			mS = MarketState.open;
			working = true;
		}
		else {
			mS = MarketState.closing;
		}
	}
	public void setMarketMoney(int mM) {
		marketMoney = mM;
	}
	public int viewMarketMoney() {
		return marketMoney;
	}


	/* Animation */
	private Semaphore animation = new Semaphore(0, true);
	MarketCashierGui gui;
	
	
	/* Data */

	// A list of orders
	public List<Order> orders =  Collections.synchronizedList(new ArrayList<Order>());

	// A list of prices
	public InventoryList inventory = new InventoryList();

	// Lists of workers
	public List<myEmployee> employees = Collections.synchronizedList(new ArrayList<myEmployee>());
	public List<myDeliverer> deliverers = Collections.synchronizedList(new ArrayList<myDeliverer>());

	// Market Status Data
	int marketMoney;
	enum MarketState {open, closing, closed};
	MarketState mS;

	// Cashier Status Data
	int salary;
	boolean working = false;


	/* Messages */
	
	// Start/End Shift
	public void msgStartShift() {
		working = true;
		//stateChanged();
	}
	public void msgEndShift() {
		working = false;
		//stateChanged();
	}

	// Worker interactions (hiring, enter/exit shift, etc.)
	public void msgHired(MarketEmployee e, int salary) {
		employees.add(new myEmployee(e, salary));
	}
	public void msgHired(MarketDeliverer d, int salary) {
		deliverers.add(new myDeliverer(d, salary));
	}
	public void msgOntheClock(MarketEmployee e) {
		synchronized(employees){
			for(myEmployee me: employees) {
				if(me.employee == e) {
					me.working = true;
				}
			}
		}
	}
	public void msgOntheClock(MarketDeliverer d) {
		synchronized(deliverers){
			for(myDeliverer md: deliverers) {
				if(md.deliverer == d) {
					md.working = true;
				}
			}
		}
	}
	public void msgOfftheClock(MarketEmployee e) {
		synchronized(employees){
			for(myEmployee me: employees) {
				if(me.employee == e) {
					me.working = true;
				}
			}
		}
	}
	public void msgOfftheClock(MarketDeliverer d) {
		synchronized(deliverers){
			for(myDeliverer md: deliverers) {
				if(md.deliverer == d) {
					md.working = false;
				}
			}
		}
	}
	public void msgDoneForTheDay() {
		mS = MarketState.closing;
		//stateChanged();
	}
	// Inventory updated +10 every time market opens
	public void msgWereOpen() {
		mS = MarketState.open;
		inventory.opening();
	}

	// Normative Scenario #1
	public void msgIWantItems(MarketCustomer c, List<ItemOrder> items) {
		orders.add(new Order(c, items));
		//stateChanged();
	}
	
	public void msgHereAreItems(Order order, MarketEmployee e) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.equals(order)) {
					o.oS = OrderState.ready;
					calculatePrice(o);
				}
			}
		}
		synchronized(employees) {
			for(myEmployee me: employees) {
				if(me.employee.equals(e)) {
					me.unoccupied = true;
				}
			}
		}
		//stateChanged();
	}
	public void msgPayment(MarketCustomer c, int money) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.customer.equals(c)) {
					o.oS = OrderState.done;
					o.amountPaid = money;
					transaction(o);
				}
			}
		}
	}

	// Normative Scenario #2
	public void msgIWantDelivery(RestCook rCk, RestCashier rCh, List<ItemOrder> i, String location) {
		orders.add(new Order(rCk, rCh, i, location));
		//stateChanged();
	}
	public void msgDelivered(Order order, MarketDeliverer d) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.equals(order)) {
					o.oS = OrderState.delivered;
				}
			}
		}
		synchronized(deliverers) {
			for(myDeliverer md: deliverers) {
				if(md.deliverer.equals(d)) {
					md.unoccupied = true;
				}
			}
		}
		//stateChanged();
	}

	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		if(!working) {
			leaveMarket();
			return true;
		}
		if(mS != MarketState.closed && working) {
			synchronized(orders) {
				for(Order o: orders) {
					if(o.oS == OrderState.none) {
						HandToEmployee(o);
						return true;
					}
				}
			}
			synchronized(orders) {
				for(Order o: orders) {
					if(o.oS == OrderState.newDelivery) {
						HandToDeliverer(o);
						return true;
					}
				}
			}
			synchronized(orders) {
				for(Order o: orders) {
					if(o.oS == OrderState.ready) {
						HandToCustomer(o);
						return true;
					}
				}
			}
			synchronized(orders) {
				for(Order o: orders) {
					if(o.oS == OrderState.done) {
						FinishOrder(o);
						return true;
					}
				}
			}
			synchronized(orders) {
				for(Order o: orders) {
					if(o.oS == OrderState.delivered) {
						FinishDelivery(o);
						return true;
					}
				}
			}
		}
		else if(mS == MarketState.closing) {
			closeUp();
			return true;
		}
		return false;
	}


	/* Actions */
	private void leaveMarket() {
		gui.leave();
	}
	private void closeUp() {
		synchronized(employees) {
			for(myEmployee me: employees) {
				me.employee.msgPay();
				marketMoney -= me.salary;
			}
		}
		synchronized(deliverers) {
			for(myDeliverer md: deliverers) {
				md.deliverer.msgPay();
				marketMoney -= md.salary;
			}
		}
		person.msgEndShift();
		working = false;
		marketMoney -= salary;
		// Send a message to the bank to store money surplus
		marketMoney = 100;
	}

	private void HandToEmployee(Order o) {
		o.oS = OrderState.handing;
		synchronized(employees) {
			for(myEmployee me: employees) {
				if(me.unoccupied && me.working) {
					me.employee.msgGetItems(o);
					me.unoccupied = false;
					for(ItemOrder iO: o.items) {
						inventory.updateAmount(iO.getFoodItem(), iO.getAmount(), false);
					}
				}
			}
		}
	}

	private void HandToDeliverer(Order o) {
		o.oS = OrderState.handing;
		calculatePrice(o);
		synchronized(deliverers) {
			for(myDeliverer md: deliverers) {
				if(md.unoccupied && md.working) {
					md.deliverer.msgDeliverItems(o);
					md.unoccupied = false;
					o.cook.msgHereIsWhatICanFulfill(o.items, HaveSomeItems(o.items)); // figure out how much he can fulfill
					for(ItemOrder iO: o.items) {
						inventory.updateAmount(iO.getFoodItem(), iO.getAmount(), false);
					}
				}
			}
		}
	}

	private void HandToCustomer(Order o) {
		o.oS = OrderState.paying;
		o.customer.msgHereAreItemsandPrice(o.items, o.price);
	}

	private void FinishOrder(Order o) {
		o.customer.msgThankYou(o.change);
		updateMarketMoney(o);
		orders.remove(o);
	}

	private void FinishDelivery(Order o) {
		updateMarketMoney(o);
		orders.remove(o);
	}


	/* Classes used by Cashier Role */

	// Employee class (Cashier's view of employees)
	public class myEmployee {
		MarketEmployee employee;
		public boolean unoccupied;
		boolean working;
		int salary;

		myEmployee(MarketEmployee e, int s) {
			employee = e;
			unoccupied = true;
			working = false;
			salary = s;
		}
		
		myEmployee(MarketEmployee e, int s, boolean u, boolean w) {
			employee = e;
			unoccupied = u;
			working = w;
			salary = s;
		}

	}

	// Deliverer class (Cashier's view of deliverers)
	public class myDeliverer {
		MarketDeliverer deliverer;
		public boolean unoccupied;
		boolean working;
		int salary;

		myDeliverer(MarketDeliverer d, int s) {
			deliverer = d;
			unoccupied = true;
			working = false;
			salary = s;
		}
		
		myDeliverer(MarketDeliverer d, int s, boolean u, boolean w) {
			deliverer = d;
			unoccupied = u;
			working = w;
			salary = s;
		}
	}

	/* Calculation functions by Cashier */
	private void calculatePrice(Order o) {
		for(ItemOrder iO: o.items) {
			o.price += inventory.getPrice(iO.getFoodItem())*iO.getAmount();
		}
	}
	private void transaction(Order o) {
		o.change = o.amountPaid - o.price;
	}
	private void updateMarketMoney(Order o) {
		marketMoney += o.price;
	}
	private boolean HaveSomeItems(List<ItemOrder> items) {
		for(ItemOrder iO: items) {
			if(inventory.getAmount(iO.getFoodItem()) == 0) {
				return false;
			}
		}
		return true;
	}
	
}