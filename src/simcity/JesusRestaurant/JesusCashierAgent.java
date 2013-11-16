package simcity.JesusRestaurant;

import agent.Agent;
import restaurant.gui.CashierGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class JesusCashierAgent extends Agent implements JesusCashier {
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	public double money = 400.00;

	public enum CheckState {none, ready, paying, paid, owe};
	public enum BillState {none, paying, owe, paid};

	private String name;

	public JesusCashierGui cashierGui = null;

	JesusMenu m = new JesusMenu();

	public EventLog log = new EventLog();

	public JesusCashierAgent(String name) {
		super();

		this.name = name;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages
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

	public void msgCustomerPayment(JesusCustomer c, double m, String name) {
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

	public void msgHereIsBill(JesusMarket m, double amount, int id) {
		bills.add(new Bill(id, m, amount));
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
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
	public void computeCheck(Check ch) {
		print("Computing check for " + ch.name);
		log.add(new LoggedEvent ("Check computed"));
		ch.addAmount();
		ch.waiter.msgCheckComputed(ch.customer, ch.amount, ch.name);
		print("Check complete for " + ch.name + ". Total is " + ch.amount);
		ch.cS = CheckState.ready;
	}

	public void payment(Check ch) {
		ch.subtractAmount();
		if(ch.amount <= 0) {
			print("Paid $" + ch.amountPaid);
			log.add(new LoggedEvent ("Check paid"));
			print("Your change is $" + -ch.amount);
			money += ch.amount;
			ch.customer.msgChange(-ch.amount);
			ch.cS = CheckState.paid;
			ch.clearCheck();
		}
		else {
			print("Paid " + ch.amountPaid);
			log.add(new LoggedEvent ("Check not fully paid"));
			print("Need to pay " + ch.amount + ", but don't worry. You can pay next time.");
			ch.cS = CheckState.owe;
			ch.customer.msgPayNextTime(ch.amount);
		}
	}

	public void payBill(Bill b) {
		print("Paying bill...");
		b.subtractAmount();
		if(b.bS == BillState.paid) {
			print("Bill paid fully. $" + money + "left");
			log.add(new LoggedEvent("Bill paid"));
			b.market.msgPayingBill(b.id, b.amountPaid);
		}
		else if(b.bS == BillState.owe) {
			print("Bill not fully paid. Owe " + b.amountDue);
			log.add(new LoggedEvent("Bill not paid completely"));
			b.market.msgPayingBill(b.id, b.amountPaid);
		}
	}

	//utilities

	public void setGui(JesusCashierGui gui) {
		cashierGui = gui;
	}

	public JesusCashierGui getGui() {
		return cashierGui;
	}

	public class Check {
		JesusWaiter waiter;
		JesusCustomer customer;
		public String name;
		public double amount;
		double amountPaid;
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
		int id;
		public JesusMarket market;
		public double amountDue;
		public BillState bS;
		public double amountPaid;

		public Bill(int i, JesusMarket m, double aD) {
			id = i;
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
	public Bill getBill(int i) {
		synchronized(bills){
			for(Bill b: bills) {
				if(b.id == i) {
					return b;
				}
			}
		}
		return null;
	}
}