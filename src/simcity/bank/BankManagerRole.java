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


public class BankManagerRole extends JobRole implements BankManager  {
	
	String name;
	
	public BankManagerRole(){
		super();
	}
	
	public BankManagerRole(String name) {
		// TODO Auto-generated constructor stub
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
	int bankMoney;
	private BankTeller teller;
	// Cashier Status Data
	int salary;
	boolean working;
	private myCustomer cust;
	private Semaphore managerAnimation = new Semaphore(0,true);
	BankManagerGui gui;
	public void setGui(BankManagerGui g){
		gui = g;
	}
	
	/* Messages */
	
	// Worker interactions (hiring, enter/exit shift, etc.)
	public void msgHired(BankTeller t) {
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
		
		if(findCustomer(c) == null){
			customers.add(new myCustomer(c, c.getName()));
		}
			findCustomer(c).cS = CustomerState.arrived;
			waitingCustomers.add(c);
		stateChanged();
	}
	
	
	public void msgProcessTransaction(BankTeller t, BankDepositor c, int money){
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
		
		if(bS == BankState.closing) {
			closeUp();
			return true;
		}
		
		
		return false;
	}
	
	
	/* Actions */
	
	private void leaveBank(){
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
		waitingCustomers.remove(c);
		t.msgHelpCustomer(c, findCustomer(c).cashInBank);
	}
	
	
	private void transactionComplete(BankDepositor c) {
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
		
	}
	public enum CustomerState{arrived, beingHelped, leaving, transactionProcessed};

	
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


	


