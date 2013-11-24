package simcity.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.role.JobRole;
import simcity.ItemOrder;
import simcity.PersonAgent;
import simcity.interfaces.RestCashier;
import simcity.interfaces.RestCook;
import simcity.market.Order.OrderState;
import simcity.market.gui.MarketCashierGui;
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
	public void setEmployees(MarketEmployee e, int s, boolean w) {
		employees.add(new myEmployee(e, s, w));
	}
	public void setDeliverers(MarketDeliverer d, int s) {
		deliverers.add(new myDeliverer(d, s));
	}
	public void setDeliverers(MarketDeliverer d, int s, boolean w) {
		deliverers.add(new myDeliverer(d, s, w));
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
				}
			}
		}
		synchronized(employees) {
			for(myEmployee me: employees) {
				if(me.employee.equals(e)) {
					me.numOfCust--;
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
					md.numOfCust--;
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
		myEmployee assignedEmployee = employees.get(0);
		int min = employees.get(0).numOfCust;
		synchronized(employees){
			for (myEmployee me : employees) {
				if(me.numOfCust < min && me.working) {
					min = me.numOfCust;
					assignedEmployee = me;
				}
			}
		}
		checkingInventorytoConfirmOrder(o);
		if(o.fulfilling.size() != 0) {
			assignedEmployee.employee.msgGetItems(o);
			assignedEmployee.numOfCust++;
			o.oS = OrderState.handing;
			for(ItemOrder iO: o.fulfilling) {
				inventory.updateAmount(iO.getFoodItem(), iO.getAmount(), false);
			}
		}
		else {
			orders.remove(o);
		}
	}

	private void HandToDeliverer(Order o) {
		myDeliverer assignedDeliverer = deliverers.get(0);
		int min = deliverers.get(0).numOfCust;
		synchronized(deliverers){
			for (myDeliverer md : deliverers) {
				if(md.numOfCust < min && md.working) {
					min = md.numOfCust;
					assignedDeliverer = md;
				}
			}
		}
		checkingInventorytoConfirmOrder(o);
		if(o.fulfilling.size() != 0) {
			assignedDeliverer.deliverer.msgDeliverItems(o);
			assignedDeliverer.numOfCust++;
			o.oS = OrderState.handing;
			for(ItemOrder iO: o.fulfilling) {
				inventory.updateAmount(iO.getFoodItem(), iO.getAmount(), false);
			}
		}
		else {
			orders.remove(o);
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
		boolean working;
		int salary;
		int numOfCust;

		myEmployee(MarketEmployee e, int s) {
			employee = e;
			working = false;
			salary = s;
			numOfCust = 0;
		}
		
		myEmployee(MarketEmployee e, int s, boolean w) {
			employee = e;
			working = w;
			salary = s;
			numOfCust = 0;
		}

	}

	// Deliverer class (Cashier's view of deliverers)
	public class myDeliverer {
		MarketDeliverer deliverer;
		boolean working;
		int salary;
		int numOfCust;

		myDeliverer(MarketDeliverer d, int s) {
			deliverer = d;
			working = false;
			salary = s;
			numOfCust = 0;
		}
		
		myDeliverer(MarketDeliverer d, int s, boolean w) {
			deliverer = d;
			working = w;
			salary = s;
			numOfCust = 0;
		}
	}

	/* Calculation functions by Cashier */
	private void calculatePrice(Order o) {
		for(ItemOrder iO: o.fulfilling) {
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
			if(inventory.getAmount(iO.getFoodItem()) != 0) {
				return true;
			}
		}
		return false;
	}
	private void checkingInventorytoConfirmOrder(Order o) {
		o.fulfilling = new ArrayList<ItemOrder>();
		synchronized(o.items) {
			for(ItemOrder iO: o.items) {
				if(iO.getAmount() < inventory.getAmount(iO.getFoodItem())) {
					o.fulfilling.add(iO);
				}
				else if(inventory.getAmount(iO.getFoodItem()) != 0) {
					o.fulfilling.add(new ItemOrder(iO.getFoodItem(), inventory.getAmount(iO.getFoodItem())));
				}
			}
		}
		calculatePrice(o);
		if(o.location != null)
			o.cook.msgHereIsWhatICanFulfill(o.fulfilling, HaveSomeItems(o.fulfilling));
		else
			o.customer.msgHereIsWhatICanFulfill(o.fulfilling, HaveSomeItems(o.fulfilling));
	}
}