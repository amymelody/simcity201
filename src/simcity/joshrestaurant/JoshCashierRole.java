package simcity.joshrestaurant;

import simcity.agent.Agent;
import simcity.joshrestaurant.interfaces.JoshCustomer;
import simcity.joshrestaurant.interfaces.JoshWaiter;
import simcity.joshrestaurant.interfaces.JoshCashier;
import simcity.joshrestaurant.interfaces.JoshMarket;
import simcity.mock.LoggedEvent;

import java.util.*;


/**
 * Restaurant Cashier Agent
 */

public class JoshCashierRole extends Agent implements JoshCashier {
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());

	private String name;
	private int cash;
	
	Map<String, Integer> prices = new HashMap<String, Integer>();
	
	public enum CheckState {Created, GivenToWaiter, Paid, Done};

	public JoshCashierRole(String name) {
		super();
		this.name = name;
		cash = 200;
		
		prices.put("steak", 16);
		prices.put("chicken", 11);
		prices.put("salad", 6);
		prices.put("pizza", 9);
	}
	

	public String getName() {
		return name;
	}
	
	public int getCash() {
		return cash;
	}
	
	// Messages
	
	public void msgProduceCheck(JoshWaiter w, JoshCustomer c, String choice) {
		log.add(new LoggedEvent("Received msgProduceCheck"));
		checks.add(new Check(c, w, choice, prices.get(choice)+c.getCharge(), CheckState.Created));
		stateChanged();
	}
	
	public void msgPayment(JoshCustomer c, int cash) {
		log.add(new LoggedEvent("Received msgPayment"));
		synchronized(checks) {
			for (Check check : checks) {
				if (check.cust == c & check.state == CheckState.GivenToWaiter) {
					check.setPayment(cash);
					check.setState(CheckState.Paid);
					stateChanged();
				}
			}
		}
	}
	
	public void msgHereIsBill(int bill, JoshMarket market) {
		log.add(new LoggedEvent("Received msgHereIsBill"));
		bills.add(new Bill(market, bill));
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() { //Changed to public for unit testing
		synchronized(checks) {
			for (Check check : checks) {
				if (check.state == CheckState.Created) {
					giveToWaiter(check);
					return true;
				}
			}
		}
		synchronized(checks) {
			for (Check check : checks) {
				if (check.state == CheckState.Paid) {
					giveCustomerChange(check);
					return true;
				}
			}
		}
		synchronized(bills) {
			for (Bill bill : bills) {
				payBill(bill);
				return true;
			}
		}

		return false;
	}

	// Actions

	private void giveToWaiter(Check c) {
		print(c.waiter + ", here is the check for " + c.cust);
		c.setState(CheckState.GivenToWaiter);
		c.waiter.msgHereIsCheck(c.cust, c.charge);
	}
	
	private void giveCustomerChange(Check c) {
		int change = c.payment - c.charge;
		if (change >= 0) {
			print(c.cust + ", here is your change of $" + change);
			cash += c.charge;
		} else {
			print(c.cust + ", thank you for eating at our restaurant. Please pay $" + -change + " next time you rotten cheapskate.");
			cash += c.payment;
		}
		c.setState(CheckState.Done);
		c.cust.msgChange(change);
	}
	
	private void payBill(Bill bill) {
		cash -= bill.charge;
		print("Paying bill. Cash = $" + cash);
		bill.market.msgPayment(bill.charge);
		bills.remove(bill);
	}

	//utilities
	
	public class Bill {
		JoshMarket market;
		int charge;
		
		Bill(JoshMarket m, int c) {
			market = m;
			charge = c;
		}
		
		public JoshMarket getMarket() {
			return market;
		}
		
		public int getCharge() {
			return charge;
		}
	}

	public class Check {
		JoshCustomer cust;
		JoshWaiter waiter;
		String choice;
		int charge;
		int payment;
		CheckState state;
		
		Check(JoshCustomer c, JoshWaiter w, String choice, int charge, CheckState s) {
			cust = c;
			waiter = w;
			this.choice = choice;
			this.charge = charge;
			payment = 0;
			state = s;
		}
		
		public void setPayment(int payment) {
			this.payment = payment;
		}
		
		public void setState(CheckState state) {
			this.state = state;
		}
		
		public CheckState getState() {
			return state;
		}
		
		public int getCharge() {
			return charge;
		}
		
		public int getPayment() {
			return payment;
		}
		
		public JoshCustomer getCust() {
			return cust;
		}
		
		public JoshWaiter getWaiter() {
			return waiter;
		}
		
		public String getChoice() {
			return choice;
		}
	}
}
