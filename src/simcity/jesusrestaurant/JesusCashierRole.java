package simcity.jesusrestaurant;

import simcity.interfaces.Person;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;
import simcity.RestCashierRole;
import simcity.role.JobRole;
import simcity.interfaces.MarketDeliverer;
import simcity.jesusrestaurant.JesusHostRole.myWaiter;
import simcity.jesusrestaurant.gui.JesusCashierGui;
import simcity.jesusrestaurant.interfaces.JesusCashier;
import simcity.jesusrestaurant.interfaces.JesusCustomer;
import simcity.jesusrestaurant.interfaces.JesusMarket;
import simcity.jesusrestaurant.interfaces.JesusWaiter;
import simcity.joshrestaurant.interfaces.JoshCustomer;
import simcity.joshrestaurant.interfaces.JoshWaiter;
import simcity.market.gui.MarketCustomerGui.GuiState;
import simcity.mock.EventLog;
import simcity.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * simcity.jesusrestaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a simcity.jesusrestaurant who sees that all
//is proceeded as he wishes.
public class JesusCashierRole extends RestCashierRole implements JesusCashier {
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	public int money;

	public enum CheckState {none, ready, paying, paid, owe};
	public enum BillState {none, paying, owe, paid};

	private String name;

	public JesusCashierGui jesusCashierGui = null;

	JesusMenu m = new JesusMenu();

	public EventLog log = new EventLog();
	JesusHostRole host;
	JesusCookRole cook;
	boolean working, start = false;
	
	public JesusCashierRole() {
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
	public void setHost(JesusHostRole h) {
		host = h;
	}
	public void setCook(JesusCookRole c) {
		cook = c;
	}
	public void setMoney(int m) {
		money = m;
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
	public void msgPayEmployees(List<myWaiter> waiters) {
		for(myWaiter mW: waiters) {
			money -= mW.salary;
		}
		money -= getPersonAgent().getSalary();
		money -= host.getPersonAgent().getSalary();
		money -= cook.getPersonAgent().getSalary();
	}
	public void msgComputeCheck(JesusWaiter w, JesusCustomer c, String foodItem, String name) {
		synchronized(checks) {
			for(Check ch: checks) {
				if(ch.name.equals(name)) {
					ch.addItem(foodItem);
					ch.cS = CheckState.none;
					stateChanged();
					return;
				}
			}
		}
		Check check = new Check(w, c, name);
		check.addItem(foodItem);
		checks.add(check);
		stateChanged();
	}

	public void msgCustomerPayment(JesusCustomer c, int m, String name) {
		synchronized(checks) {
			for(Check ch: checks) {
				if(ch.name.equals(name)) {
					ch.cS = CheckState.paying;
					ch.amountPaid = m;
					money += m;
					stateChanged();
				}
			}
		}
	}

	/*public void msgHereIsBill(JesusMarket m, int amount, int id) {
		bills.add(new Bill(id, m, amount));
		stateChanged();
	}*/
	
	public void msgDelivery(int bill, MarketDeliverer deliverer) {
		bills.add(new Bill(deliverer, bill));
		stateChanged();
	}

	public void msgThankYou(int change) {
		money += change;
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
		if(!(checks.isEmpty())) {
			synchronized(checks) {
				for(Check ch: checks) {
					if(ch.cS == CheckState.none) {
						computeCheck(ch);
						return true;
					}
				}
			}
		}

		synchronized(checks) {
			for(Check ch: checks) {
				if(ch.cS == CheckState.paying) {
					payment(ch);
					return true;
				}
			}
		}

		if(!(bills.isEmpty())) {
			synchronized(bills){
				for(Bill b: bills) {
					if(b.bS == BillState.none) {
						payBill(b);
						return true;
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
	private void startWork() {
		jesusCashierGui.work();
	}
	private void leaveRestaurant() {
		jesusCashierGui.leave();
	}
	public void computeCheck(Check ch) {
		AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Computing check for " + ch.name);
		log.add(new LoggedEvent ("Check computed"));
		ch.addAmount();
		ch.waiter.msgCheckComputed(ch.customer, ch.amount, ch.name);
		AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Check complete for " + ch.name + ". Total is " + ch.amount);
		ch.cS = CheckState.ready;
	}

	public void payment(Check ch) {
		ch.subtractAmount();
		if(ch.amount <= 0) {
			AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Paid $" + ch.amountPaid);
			log.add(new LoggedEvent ("Check paid"));
			AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Your change is $" + -ch.amount);
			money += ch.amount;
			ch.customer.msgChange(-ch.amount);
			ch.cS = CheckState.paid;
			ch.clearCheck();
		}
		else {
			AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Paid " + ch.amountPaid);
			log.add(new LoggedEvent ("Check not fully paid"));
			AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Need to pay " + ch.amount + ", but don't worry. You can pay next time.");
			ch.cS = CheckState.owe;
			ch.customer.msgPayNextTime(ch.amount);
		}
	}

	public void payBill(Bill b) {
		b.subtractAmount();
		if(b.bS == BillState.paid) {
			AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Bill paid fully. $" + money + "left");
			log.add(new LoggedEvent("Bill paid"));
			b.market.msgPayment(this, b.amountDue);
		}
		else if(b.bS == BillState.owe) {
			AlertLog.getInstance().logMessage(AlertTag.JESUS_RESTAURANT, name, "Bill not fully paid. Owe " + b.amountDue);
			log.add(new LoggedEvent("Bill not paid completely"));
			b.market.msgPayment(this, money);
			money = 0;
		}
	}

	//utilities

	public void setGui(JesusCashierGui gui) {
		jesusCashierGui = gui;
	}

	public JesusCashierGui getGui() {
		return jesusCashierGui;
	}

	public class Check {
		JesusWaiter waiter;
		JesusCustomer customer;
		public String name;
		public int amount;
		int amountPaid;
		String foodItem;
		public CheckState cS;

		public Check(JesusWaiter w, JesusCustomer c, String n) {
			waiter = w;
			customer = c;
			name = n;
			amount = 0;
			cS = CheckState.none;
			foodItem = "";
		}
		public void clearCheck() {
			amount = 0;
			amountPaid = 0;
			foodItem = "";
		}
		public void addItem(String fI) {
			foodItem = fI;
		}
		public void addAmount() {
			amount += m.getMenuItemPrice(foodItem);
		}
		public void subtractAmount() {
			amount -= amountPaid;
		}
	}
	public class Bill {
		public MarketDeliverer market;
		public int amountDue;
		public BillState bS;
		public int amountPaid;

		public Bill(MarketDeliverer m, int aD) {
			market = m;
			amountDue = aD;
			bS = BillState.none;
		}
		public void subtractAmount() {
			if(money < amountDue) {
				amountPaid = money;
				amountDue -= amountPaid;
				money = 0;
				bS = BillState.owe;
			}
			else {
				amountPaid = amountDue;
				money -= amountPaid;
				amountDue = 0;
				bS = BillState.paid;
			}
		}
		public void addAmount(double num) {
			amountDue += num;
		}
	}
	
}