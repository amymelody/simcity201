package simcity.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.PersonAgent;
import simcity.bank.gui.BankManagerGui;
import simcity.bank.interfaces.BankDepositor;
import simcity.bank.interfaces.BankManager;
import simcity.bank.interfaces.BankTeller;
import simcity.role.JobRole;

//Bank Manager Role
public class BankManagerRole extends JobRole implements BankManager  {
	
	String name;
	
	public BankManagerRole(){
		super();
	}
	
	public BankManagerRole(String name) {
		this.name = name;
	}
	
	public String getMaitreDName(){
		return name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setPerson(PersonAgent p){
		super.setPerson(p);
		name = p.getName();
	}
	
	public void setTellers(BankTeller t){
		addTeller(t);
	}
	
	public void setBankState(boolean open){
		if(open){
			bS = BankState.open;
			working = true;
		}
		else{
			bS = BankState.closing;
		}
	}
	
	public void setBankMoney(int bM){
		bankMoney = bM;
	}
	
	public int getBankMoney(){
		return bankMoney;
	}
	/* Data */
	
	// A list of orders
	
	// Lists of workers
	
	
	
	public List<myTeller> tellers = Collections.synchronizedList(new ArrayList<myTeller>());
	public List<myCustomer> customers = Collections.synchronizedList(new ArrayList<myCustomer>());
	public List<BankDepositor> waitingCustomers = Collections.synchronizedList(new ArrayList<BankDepositor>());
	// BankState Status Data
	int tellerSelection = 0;
	
	enum BankState {open, closing, closed};
	BankState bS;
	int bankMoney = 100;
	private BankTeller teller;
	private BankDepositor depositor;
	//Teller data
	int salary;
	boolean working;
	private myCustomer cust;
	private Semaphore managerAnimation = new Semaphore(0,true);
	BankManagerGui gui = new BankManagerGui(this);
	public void setGui(BankManagerGui g){
		gui = g;
	}
	public void setTeller(BankTeller t){
		this.teller = teller;
	}
	public void setDepositor(BankDepositor d){
		this.depositor = d;
	}
	/* Messages */
	
	// Worker interactions (hiring, enter/exit shift, etc.)
	
	
	

	public void msgStartShift() {
		working = true;
		synchronized(tellers){
			for(myTeller tr : tellers) {
			
					tr.tS = TellerState.working;
				}
				stateChanged();
			}
		}
	
	
	public void msgEndShift() {
		working = false;
		synchronized(tellers){
			for(myTeller tr: tellers) {
					tr.tS = TellerState.doingNothing;
				}
				stateChanged();
			}
		}
	
	public void msgHired(BankTeller t, int salary) {
		//Do("Manager has hired teller");
		//addTeller(t);
		//Do("Size of tellers is: " + tellers.size());
	}
	
	
	
	public void msgDoneForTheDay() {
		bS = BankState.closing;
	}

	
	
	
	public void msgWereOpen(){
		bS = BankState.open;
	
	}
	// Normative Scenario #1
	public void msgTransaction(BankDepositor c){
		Do("Manager is adding customer to a list of waiting customers");
		if(findCustomer(c) == null){
			Do("Customer does not have an account in bank, creating account");
			customers.add(new myCustomer(c));
		}	
			
			Do("Manager has accessed customer account");
			waitingCustomers.add(c);

			findCustomer(c).cS = CustomerState.arrived;
			stateChanged();

		}
			
		
	

	
	public void msgMarketTransaction(BankDepositor c){
		Do("Manager is adding market to a list of waiting customers");
		//BankManagerRole changes 
		if(findCustomer(c) == null){
			Do("No customer found, creating customer");
			customers.add(new myCustomer(c));
		}
		Do("Finding customer and changing state");
			findCustomer(c).cS = CustomerState.marketArrived;
			waitingCustomers.add(c);
		stateChanged();
	}
	
	public void msgProcessTransaction(BankTeller t, BankDepositor c, int transactionRequest){
		Do("Bank manager is processing transaction");
		teller = t;
		//Making withdrawal
		if(transactionRequest < 0){
			if(findCustomer(c).cashInBank < transactionRequest){
				findCustomer(c).cS = CustomerState.transactionDenied;
			}
			else{
				findCustomer(c).cashInBank = findCustomer(c).cashInBank - transactionRequest;
				bankMoney = bankMoney - transactionRequest;
				findCustomer(c).cS = CustomerState.transactionProcessed;
				stateChanged();
			}
		}
		//Making deposit
		if(transactionRequest > 0){
			findCustomer(c).cashInBank += transactionRequest;
			bankMoney += transactionRequest;
			findCustomer(c).cS = CustomerState.transactionProcessed;
			stateChanged();
		}
		
	}
	public boolean pickAndExecuteAnAction() {
		synchronized(customers){
			for(myCustomer c : customers){
				if(c.cS == CustomerState.arrived && !tellers.isEmpty()){
					
					helpCustomer(c.customer, findTeller());
					return true;
				}
			}
			for(myCustomer k : customers){
				if(k.cS == CustomerState.transactionDenied){
					transactionDenied(k.customer);
				}
			}
			for(myCustomer x : customers){
				if(x.cS == CustomerState.transactionProcessed){
					transactionComplete(x.customer);
				}
			}
		}
		
		for(myCustomer j : customers){
			if(j.cS == CustomerState.marketArrived && !tellers.isEmpty()){
				helpCustomer(waitingCustomers.get(0), findTeller());
				return true;
			}
		}
		if(bS == BankState.closing) {
			closeUp();
			return true;
		}
		
		
		return false;
	}
	
	
	/* Actions */
	
	private void leaveBank(){
		Do("Bank manager is leaving bank");
		gui.ExitBank();
	}
	private void closeUp() {
		synchronized(tellers){
			for(myTeller t : tellers){
				t.t.msgPay();
				
			}
		msgEndShift();
		}
		
	}
	
	private BankTeller findTeller () {
		
		
		for(myTeller t : tellers){
			if(t.tS == TellerState.working){
				Do("Teller  found");
				return t.t;
			}
		}
		
		
	return null;
	}	
	
	private void helpCustomer(BankDepositor c, BankTeller t){
		Do("Manager is finding a teller to help the customer");
		findCustomer(c).cS = CustomerState.beingHelped;
		t.msgHelpCustomer(c);
		
	}
	
	private void transactionDenied(BankDepositor c){
		Do("Manager has denid transaction, not enough funds");
		teller.msgTransactionDenied(c);
	}
	private void transactionComplete(BankDepositor c) {
		Do("Manager has successfully processed transaction");
		findCustomer(c).cS = CustomerState.transactionComplete;
		teller.msgTransactionComplete(c, findCustomer(c).cashInBank);
	}
	
	
	
	
	// Employee class (Cashier's view of employees)
	
	
	//Customer class
	public class myCustomer{
		BankDepositor customer;
		int cashInBank;
		String name;
		CustomerState cS;

		myCustomer(BankDepositor c){
			customer = c;
			cS = CustomerState.doingNothing;
			cashInBank = 0;			
			
		}
		
		public CustomerState getCustomerState(){
			return cS;
		}
		
	}
	public enum CustomerState{doingNothing, marketArrived, arrived, 
		marketHelped, beingHelped, marketLeaving, leaving, 
		marketTransactionComplete, transactionProcessed, transactionDenied, transactionComplete};
	

	private myCustomer findCustomer(BankDepositor c){
	synchronized(customers){
		for(myCustomer mc : customers){
			if(mc.customer.equals(c)){
				return mc;
			}
		}
	}
	return null;

	}
	private myTeller findTeller(BankTeller t){
		for(myTeller mt : tellers){
			if(mt.t == t){
				return mt;
			}
		}
		return null;
	}
	private class myTeller{
		BankTeller t;
		TellerState tS;
		String n;
		myTeller(BankTeller teller, TellerState ts){
			t = teller;
			tS = TellerState.doingNothing;
		}
	
		public TellerState getTellerState(){
			return tS;
		}
	}
	public enum TellerState{doingNothing, working};
	
	
	public void addTeller(BankTeller t){
		tellers.add(new myTeller(t, TellerState.doingNothing));
	}

	

	
	

	

	
}	


	


