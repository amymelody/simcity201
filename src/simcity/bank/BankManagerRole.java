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
	// Cashier Status Data
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
	public void msgHired(BankTeller t) {
		Do("Manager has hired teller");
		addTeller(t);
	}

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
	

	public void msgDoneForTheDay() {
		bS = BankState.closing;
	}

	/*
	public void msgHired(BankTellerRole t, int salary){
		addTeller(t);
	}
	*/
	public void msgWereOpen(){
		bS = BankState.open;
	
	}
	// Normative Scenario #1
	public void msgTransaction(BankDepositor c){
		Do("Manager is adding customer to a list of waiting customers");
		if(findCustomer(c) == null){
			Do("no customer found");
			customers.add(new myCustomer(c, c.getName()));
			customers.get(0).cS = CustomerState.arrived;
			waitingCustomers.add(c);
		}
		while(findCustomer(c) != null){
			Do("Customer found");
			findCustomer(c).cS = CustomerState.arrived;
			waitingCustomers.add(c);
		}
			
		
		stateChanged();
	}

	
	public void msgMarketTransaction(BankDepositor c){
		Do("Manager is adding market to a list of waiting customers");
		if(findCustomer(c) == null){
			
			customers.add(new myCustomer(c, c.getName()));
		}
		
			findCustomer(c).cS = CustomerState.marketArrived;
			waitingCustomers.add(c);
		stateChanged();
	}
	
	public void msgProcessTransaction(BankTeller t, BankDepositor c, int money){
		Do("Bank manager is processing transaction");
		teller = t;
		findCustomer(c).cashInBank += money;
		bankMoney += money;
		findCustomer(c).cS = CustomerState.transactionProcessed;
		stateChanged();
	}
	public boolean pickAndExecuteAnAction() {
		synchronized(customers){
			for(myCustomer c : customers){
				if(c.cS == CustomerState.arrived && !tellers.isEmpty()){
					
					helpCustomer(waitingCustomers.get(0), findTeller());
					return true;
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
		BankTeller t = tellers.get(tellerSelection%tellers.size()).t;
		tellerSelection++;

		if(findTeller(t).tS == TellerState.doingNothing){
			return t;
		}
		
	return null;
	}	
	
	private void helpCustomer(BankDepositor c, BankTeller t){
		Do("Manager is finding a teller to help the customer");
		waitingCustomers.remove(c);
		t.msgHelpCustomer(c, findCustomer(c).cashInBank);
	}
	
	
	private void transactionComplete(BankDepositor c) {
		Do("Manager has successfully processed transaction");
		teller.msgTransactionComplete(c);
	}
	
	
	
	
	// Employee class (Cashier's view of employees)
	
	
	//Customer class
	public class myCustomer{
		BankDepositor customer;
		int cashInBank;
		String name;
		CustomerState cS;

	// 0 means deposit, 1 means withdrawal
		myCustomer(BankDepositor c, String n){
			cashInBank = 0;
			name = n;
			
			
		}
		public CustomerState getCustomerState(){
			return cS;
		}
		
	}
	public enum CustomerState{marketArrived, arrived, marketHelped, beingHelped, marketLeaving, leaving, marketTransactionComplete, transactionProcessed};
	

	private myCustomer findCustomer(BankDepositor c){
	
		for(myCustomer mc : customers){
			if(mc.customer == c){
				return mc;
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
			tS = ts;
		}
	
		public TellerState getTellerState(){
			return tS;
		}
	}
	public enum TellerState{doingNothing, working};
	
	
	public void addTeller(BankTeller t){
		tellers.add(new myTeller(t, TellerState.doingNothing));
	}

	@Override
	public void msgHired(BankTeller t, int salary) {
		// TODO Auto-generated method stub
		
	}

	
}	


	


