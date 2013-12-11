package simcity.market;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import simcity.role.JobRole;
import simcity.ItemOrder;
import simcity.bank.BankManagerRole;
import simcity.interfaces.BankDepositor;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketDeliverer;
import simcity.interfaces.MarketEmployee;
import simcity.interfaces.Person;
import simcity.interfaces.RestCashier;
import simcity.interfaces.RestCook;
import simcity.market.Order.OrderState;
import simcity.market.gui.MarketCashierGui;

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
	public void setPerson(Person p) {
		super.setPerson(p);
		name = p.getName();
	}


	/* Hacks */
	public void setBankDepositor(BankDepositor bD) {
		bank = bD;
	}
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


	/* Accessors */
	public int viewMarketMoney() {
		return marketMoney;
	}
	public int viewMarketMoneySurplus() {
		return marketMoneySurplus;
	}
	public myEmployee getEmployee(MarketEmployee e) {
		synchronized(employees) {
			for(myEmployee me: employees) {
				if(me.employee.equals(e)) {
					return me;
				}
			}
		}
		return null;
	}
	public myDeliverer getDeliverer(MarketDeliverer d) {
		synchronized(deliverers) {
			for(myDeliverer md: deliverers) {
				if(md.deliverer.equals(d)) {
					return md;
				}
			}
		}
		return null;
	}


	/* Animation */
	private Semaphore animation = new Semaphore(0, true);
	MarketCashierGui gui;
	public void setGui(MarketCashierGui g){
		gui = g;
	}
	private static Map<Integer, Point> chairLocations = new HashMap<Integer, Point>();
	static {
		chairLocations.put(1, new Point(80, 320));
		chairLocations.put(2, new Point(20, 320));
		chairLocations.put(3, new Point(80, 340));
		chairLocations.put(4, new Point(20, 340));
		chairLocations.put(5, new Point(80, 360));
		chairLocations.put(6, new Point(20, 360));
		chairLocations.put(7, new Point(80, 380));
		chairLocations.put(8, new Point(20, 380));
		chairLocations.put(9, new Point(80, 400));
		chairLocations.put(20, new Point(20, 400));
		chairLocations.put(11, new Point(80, 420));
		chairLocations.put(12, new Point(20, 420));
		chairLocations.put(13, new Point(80, 440));
		chairLocations.put(14, new Point(20, 440));
		chairLocations.put(15, new Point(80, 460));
		chairLocations.put(16, new Point(20, 460));
		chairLocations.put(17, new Point(60, 480));
		chairLocations.put(18, new Point(40, 480));
		chairLocations.put(19, new Point(20, 480));
	}
	Integer chairCnt = 1;


	/* Data */

	// A list of orders
	public List<Order> orders =  Collections.synchronizedList(new ArrayList<Order>());

	// A list of prices
	public InventoryList inventory = new InventoryList();

	// Lists of workers
	public List<myEmployee> employees = Collections.synchronizedList(new ArrayList<myEmployee>());
	public List<myDeliverer> deliverers = Collections.synchronizedList(new ArrayList<myDeliverer>());

	// Market Status Data
	int marketMoney, marketMoneySurplus;
	public enum MarketState {open, closing, closed};
	public MarketState mS;
	public BankManagerRole manager;

	// Reference to bank
	BankDepositor bank;

	// Cashier Status Data
	int salary;
	boolean working, start = false;


	/* Messages */

	// Start/End Shift
	public void msgStartShift() {
		working = true;
		start = true;
		stateChanged();
	}
	public void msgEndShift() {
		working = false;
		stateChanged();
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
					me.working = false;
					marketMoney -= me.salary;
				}
			}
		}
	}
	public void msgOfftheClock(MarketDeliverer d) {
		synchronized(deliverers){
			for(myDeliverer md: deliverers) {
				if(md.deliverer == d) {
					md.working = false;
					marketMoney -= md.salary;
				}
			}
		}
	}
	public void msgDoneForTheDay() {
		mS = MarketState.closing;
		stateChanged();
	}
	// Inventory updated +10 every time market opens
	public void msgWereOpen() {
		mS = MarketState.open;
		marketMoneySurplus = 0;
		inventory.opening();
		stateChanged();
	}

	// Normative Scenario #1
	public void msgIWantItems(MarketCustomer c, List<ItemOrder> items) {
		List<ItemOrder> temp = new ArrayList<ItemOrder>();
		for(ItemOrder iO: items) {
			temp.add(new ItemOrder(iO.getFoodItem(), iO.getAmount()));
		}
		orders.add(new Order(c, temp));
		stateChanged();
	}

	public void msgImHere(MarketCustomer c) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.customer != null && o.customer.equals(c)) {
					o.oS = OrderState.here;
				}
			}
		}
		stateChanged();
	}

	public void msgHereAreItems(Order order, MarketEmployee e) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.customer != null && o.customer.equals(order.customer)) {
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
		stateChanged();
	}
	public void msgPayment(MarketCustomer c, int money) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.customer != null && o.customer.equals(c)) {
					o.oS = OrderState.done;
					o.amountPaid = money;
					transaction(o);
				}
			}
		}
		stateChanged();
	}

	// Normative Scenario #2
	public void msgIWantDelivery(RestCook rCk, RestCashier rCh, List<ItemOrder> i, String location) {
		List<ItemOrder> temp = new ArrayList<ItemOrder>();
		for(ItemOrder iO: i) {
			temp.add(new ItemOrder(iO.getFoodItem(), iO.getAmount()));
		}
		orders.add(new Order(rCk, rCh, temp, location));
		stateChanged();
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
		stateChanged();
	}
	public void msgNotDelivererd(Order order, MarketDeliverer d) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.equals(order)) {
					o.oS = OrderState.needToComplete;
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
	}


	/* Animation Messages */
	public void left() {
		person.msgLeftDestination(this);
	}

	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		if(!working) {
			leaveMarket();
			return true;
		}
		if(start) {
			startWork();
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
						LetCustomerKnow(o);
						return true;
					}
					if(o.oS == OrderState.here) {
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
			if(mS == MarketState.closing && orders.size() == 0) {
				closeUp();
				return true;
			}
		}
		return false;
	}


	/* Actions */
	private void startWork() {
		start = false;
		gui.work();
		person.businessIsClosed(getJobLocation(), false);
		for(Order o: orders) {
			if(o.oS == OrderState.needToComplete) {
				o.oS = OrderState.newDelivery;
				HandToDeliverer(o);
			}
		}
		stateChanged();
	}
	private void leaveMarket() {
		gui.leave();
	}
	private void closeUp() {
		synchronized(employees) {
			for(myEmployee me: employees) {
				marketMoney -= me.salary;
			}
		}
		synchronized(deliverers) {
			for(myDeliverer md: deliverers) {
				marketMoney -= md.salary;
			}
		}
		marketMoney -= salary;
		marketMoneySurplus = marketMoney - 100;
		//bank.msgMarketDeposit(marketMoneySurplus);
		person.businessIsClosed(getJobLocation(), true);
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
		if(o.fulfilling.size() > 0) {
			assignedEmployee.employee.msgGetItems(o);
			assignedEmployee.numOfCust++;
			o.oS = OrderState.handing;
			for(ItemOrder iO: o.fulfilling) {
				inventory.updateAmount(iO.getFoodItem(), iO.getAmount(), false);
			}
		}
		else {
			removeOrder(o);
		}
		stateChanged();
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
		if(o.fulfilling.size() > 0) {
			assignedDeliverer.deliverer.msgDeliverItems(o);
			assignedDeliverer.numOfCust++;
			o.oS = OrderState.handing;
			for(ItemOrder iO: o.fulfilling) {
				inventory.updateAmount(iO.getFoodItem(), iO.getAmount(), false);
			}
		}
		else {
			removeOrder(o);
		}
		stateChanged();
	}

	private void LetCustomerKnow(Order o) {
		o.oS = OrderState.know;
		o.customer.msgOrderReady();
	}

	private void HandToCustomer(Order o) {
		o.oS = OrderState.paying;
		o.customer.msgHereAreItemsandPrice(o.items, o.price);
	}

	private void FinishOrder(Order o) {
		o.customer.msgThankYou(o.change);
		updateMarketMoney(o);
		removeOrder(o);
	}

	private void FinishDelivery(Order o) {
		updateMarketMoney(o);
		removeOrder(o);
		stateChanged();
	}


	/* Classes used by Cashier Role */

	// Employee class (Cashier's view of employees)
	public class myEmployee {
		MarketEmployee employee;
		public boolean working;
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
		public boolean working;
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
			o.cook.msgHereIsWhatICanFulfill(o.fulfilling, HaveSomeItems(o.fulfilling), this);
		else {
			o.customer.msgHereIsWhatICanFulfill(o.fulfilling, HaveSomeItems(o.fulfilling), (int)chairLocations.get(chairCnt).getX(), (int)chairLocations.get(chairCnt).getY());
			if(chairCnt == 19)
				chairCnt = 1;
			else
				chairCnt++;
		}
	}
	//stateChanged() in case MarketState == closing
	private void removeOrder(Order o) {
		orders.remove(o);
		stateChanged();
	}
}