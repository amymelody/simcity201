package simcity.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simcity.role.Role;


public class BankManagerRole extends Role   {
	
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
	public void msgHired(BankTellerRole t) {
		addTeller(t);
	}

	public void msgStartShift(BankTellerRole t) {
		synchronized(tellers){
			for(myTeller tr : tellers) {
				if(tr.teller == t) {
					tr.tS = TellerState.working;
				}
				stateChanged();
			}
		}
	}
	
	public void msgEndShift(BankTellerRole t) {
		synchronized(tellers){
			for(myTeller tr: tellers) {
				if(tr.teller == t) {
					tr.tS = TellerState.doingNothing;
				}
				stateChanged();
			}
		}
	}

	public void msgDoneForTheDay() {
		bS = BankState.closing;
	}

	// Normative Scenario #1
	public void msgMakeTransaction(BankCustomerRole c){
		if(findCustomer(c) == null){
			customers.add(new myCustomer(c, c.getName()));
		}
			findCustomer(c).cS = CustomerState.arrived;
			
		stateChanged();
	}
	
	public boolean pickAndExecuteAnAction() {
		synchronized(customers){
			for(myCustomer c : customers){
				if(c.cS == CustomerState.arrived){
					FindTeller(c);
					return true;
				}
			}
		}
		
		if(bS == BankState.closing) {
			closeUp();
			return true;
		}
		
		
		return false;
	}
	
	*/
	/* Actions */
	
	private void closeUp() {
		
	}
	
	private void findTeller (myCustomer c) {
		
	}
	
	private void processTrasaction() {
		
	}
	
	private void transactionComplete() {
		
	}
	
	
	
	
	// Employee class (Cashier's view of employees)
	
	
	//Customer class
	private class myCustomer{
		BankCustomerRole customer;
		double cashInBank;
		String name;
		CustomerState cS;
		
		myCustomer(BankCustomerRole c, String n){
			cashInBank = 0.0;
			name = n;
			
		}
		
	}
	public enum CustomerState{arrived, beingHelped, leaving};

	
	private myCustomer findCustomer(BankCustomerRole c){
	
		for(myCustomer mc : customers){
			if(mc.customer == c){
				return mc;
			}
		}
		return null;
	}
	private class myTeller{
		BankTellerRole teller;
		TellerState tS;
		
		myTeller(BankTellerRole t, TellerState ts){
			teller = t;
			tS = ts;
		}
	
		public TellerState getTellerState(){
			return tS;
		}
	}
	public enum TellerState{doingNothing, working};
	
	
	public void addTeller(BankTellerRole t){
		tellers.add(new myTeller(t, TellerState.doingNothing));
	}


	
}

