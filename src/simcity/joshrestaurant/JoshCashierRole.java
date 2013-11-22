package simcity.joshrestaurant;

import simcity.RestCashierRole;
import simcity.interfaces.MarketDeliverer;
import simcity.joshrestaurant.interfaces.JoshCustomer;
import simcity.joshrestaurant.interfaces.JoshWaiter;
import simcity.joshrestaurant.interfaces.JoshCashier;
import simcity.mock.LoggedEvent;
import simcity.PersonAgent;

import java.util.*;


/**
 * Restaurant Cashier Agent
 */

public class JoshCashierRole extends RestCashierRole implements JoshCashier {
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());

	private String name;
	private int cash;
	private boolean working;
	
	Map<String, Integer> prices = new HashMap<String, Integer>();
	
	public enum CheckState {Created, GivenToWaiter, Paid, Done};

	public JoshCashierRole() {
		super();
		name = person.getName();
		working = false;
		cash = 200;
		
		prices.put("steak", 16);
		prices.put("chicken", 11);
		prices.put("salad", 6);
		prices.put("pizza", 9);
	}
	
	public void setPerson(PersonAgent p) {
		super.setPerson(p);
		name = person.getName();
	}

	public String getName() {
		return name;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public int getCash() {
		return cash;
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
	
	public void msgDelivery(int bill, MarketDeliverer deliverer) {
		log.add(new LoggedEvent("Received msgDelivery"));
		bills.add(new Bill(deliverer, bill));
		stateChanged();
	}
	
	public void msgChange(int change) {
		log.add(new LoggedEvent("Received msgChange"));
		cash += change;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() { //Changed to public for unit testing
		if (!working) {
			leaveRestaurant();
			return true;
		}
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

	private void leaveRestaurant() {
		person.msgLeftDestination(this);
	}
	
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
		bill.deliverer.msgPayment(bill.charge);
		bills.remove(bill);
	}

	//utilities
	
	public class Bill {
		MarketDeliverer deliverer;
		int charge;
		
		Bill(MarketDeliverer d, int c) {
			deliverer = d;
			charge = c;
		}
		
		public MarketDeliverer getDeliverer() {
			return deliverer;
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

