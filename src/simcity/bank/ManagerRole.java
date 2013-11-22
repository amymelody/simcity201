package simcity.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import role.Role;
import simcity.ItemOrder;
import simcity.market.Order.OrderState;

public class ManagerRole extends Role {
	
	/* Data */
	
	// A list of orders
	
	// Lists of workers
	public List<myTeller> tellers = Collections.synchronizedList(new ArrayList<myTeller>());
	public List<myCustomer> customers = Collections.synchronizedList(new ArrayList<myCustomer>());
	
	// BankState Status Data
	
	enum BankState {open, closing, closed};
	BankState bS;
	
	// Cashier Status Data
	int salary;
	
	private myCustomer cust;
	/* Messages */
	
	// Worker interactions (hiring, enter/exit shift, etc.)
	public void msgHired(TellerRole t, int salary) {
		tellers.add(new myTeller(t, salary));
	}

	public void msgOntheClock(TellerRole t) {
		synchronized(tellers){
			for(myTeller tr : tellers) {
				if(tr.teller == t) {
					tr.working = true;
				}
			}
		}
	}
	
	public void msgOfftheClock(TellerRole t) {
		synchronized(tellers){
			for(myTeller tr: tellers) {
				if(tr.teller == t) {
					tr.working = false;
				}
			}
		}
	}

	public void msgDoneForTheDay() {
		bS = BankState.closing;
	}
	
	// Normative Scenario #1
	public void msgMakeTransaction(CustomerRole c){
		if(findCustomer(c) == null){
			customers.add(new myCustomer(c));
			
			
		}
		else {
			//figuring out how to add transaction
		}
	}
	
	public boolean pickAndExecuteAnAction() {
		if(bS == BankState.closing) {
			closeUp();
			return true;
		}
		
		
		return false;
	}
	
	
	/* Actions */
	private void closeUp() {
		
	}
	
	private void workWithCustomer (myCustomer c) {
		
	}
	
	private void processTrasaction() {
		
	}
	
	private void transactionComplete() {
		
	}
	
	
	
	
	// Employee class (Cashier's view of employees)
	class myTeller {
		TellerRole teller;
		boolean working;
		int salary;
		
		myTeller(TellerRole t, int s) {
			teller = t;
			working = false;
			salary = s;
		}
	}
	
	//Transaction class
	class myCustomer{
		CustomerRole customer;
		boolean beingHelped;
		int bankID;
		myCustomer(CustomerRole c){
			beingHelped = false;
			bankID = 0;
		}
	}
	private myCustomer findCustomer(CustomerRole c){
	
		for(myCustomer mc : customers){
			if(mc.customer == c){
				return mc;
			}
		}
		return null;
	}
	
	
	
}
