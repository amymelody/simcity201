package simcity.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import simcity.role.JobRole;


public class BankManagerRole extends JobRole   {
	
	/* Data */
	
	// A list of orders
	
	// Lists of workers
	public List<myTeller> tellers = Collections.synchronizedList(new ArrayList<myTeller>());
	public List<myCustomer> customers = Collections.synchronizedList(new ArrayList<myCustomer>());
	public List<myCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<myCustomer>());
	// BankState Status Data
	int tellerSelection = 0;
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

	public void msgStartShift() {
		synchronized(tellers){
			for(myTeller tr : tellers) {
			
					tr.tS = TellerState.working;
				}
				stateChanged();
			}
		}
	
	
	public void msgEndShift() {
		synchronized(tellers){
			for(myTeller tr: tellers) {
					tr.tS = TellerState.doingNothing;
				}
				stateChanged();
			}
		}
	

	public void msgDoneForTheDay() {
		bS = BankState.closing;
	}

	// Normative Scenario #1
	public void msgMakeTransaction(BankDepositorRole c, int transaction){
		
		if(findCustomer(c) == null){
			customers.add(new myCustomer(c, c.getName(), transaction));
		}
			findCustomer(c).cS = CustomerState.arrived;
			waitingCustomers.add(findCustomer(c));
		stateChanged();
	}
	
	public boolean pickAndExecuteAnAction() {
		synchronized(customers){
			for(myCustomer c : customers){
				if(c.cS == CustomerState.arrived && !tellers.isEmpty()){
					
					helpCustomer(waitingCustomers.get(0), findTeller(),);
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
	
	private BankTellerRole findTeller () {
		BankTellerRole t = tellers.get(tellerSelection%tellers.size()).t;
		tellerSelection++;

		if(findTeller(t).tS == TellerState.doingNothing){
			return t;
		}
		
	return null;
	}	
	
	private void helpCustomer(BankDepositorRole c, BankTellerRole t){
		waitingCustomers.remove(c);
		t.msgHelpCustomer(c, findCustomer(c).cashInBank);
	}
	
	private void processTrasaction() {
		
	}
	
	private void transactionComplete() {
		
	}
	
	
	
	
	// Employee class (Cashier's view of employees)
	
	
	//Customer class
	private class myCustomer{
		BankDepositorRole customer;
		double cashInBank;
		String name;
		CustomerState cS;
		int request;
	// 0 means deposit, 1 means withdrawal
		myCustomer(BankDepositorRole c, String n, int r){
			cashInBank = 0.0;
			name = n;
			request = r;
			
		}
		
	}
	public enum CustomerState{arrived, beingHelped, leaving};

	
	private myCustomer findCustomer(BankDepositorRole c){
	
		for(myCustomer mc : customers){
			if(mc.customer == c){
				return mc;
			}
		}
		return null;
	}
	private myTeller findTeller(BankTellerRole t){
		for(myTeller mt : tellers){
			if(mt.t == t){
				return mt;
			}
		}
		return null;
	}
	private class myTeller{
		BankTellerRole t;
		TellerState tS;
		
		myTeller(BankTellerRole teller, TellerState ts){
			t = teller;
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

