package simcity.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.bank.gui.BankDepositorGui;
import simcity.bank.gui.BankGui;
import simcity.bank.interfaces.BankDepositor;
import simcity.bank.interfaces.BankManager;
import simcity.bank.interfaces.BankTeller;
import simcity.interfaces.Person;
import simcity.bank.test.mock.MockBankManager;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketDeliverer;
import simcity.role.Role;
import simcity.ItemOrder;
import simcity.PersonAgent;

public class BankDepositorRole extends Role implements BankDepositor{
	//
	/* Constructors */
	String name;
	boolean unitTesting = false;
	public BankDepositorRole(){
		super();
	}
	public BankDepositorRole(String n) {
		name = n;
	}

	public String getMaitreDName() {
		return name;
	}
	public String getName() {
		return name;
	}
	
	public void setPerson(Person p){
		super.setPerson(p);
		name = p.getName();
		int cash = person.getMoney();

	}
	public void setManager(BankManager b) {
		manager = b;
	}
	/* Animation */
	private Semaphore customerAnimation = new Semaphore(0, true);
	BankDepositorGui gui = new BankDepositorGui(this);
	
	/* Data */
	
	// References to other roles
	BankTeller teller;
	BankManager manager;
	
	// Customer Status Data
	public enum CustomerState {entered, makingDeposit, makingWithdrawal, makingTransaction, beingHelped, leaving, atManager, atTeller};
	CustomerState cS;
	public CustomerState getCustomerState(){
		return cS;
	}
	enum MarketState{entered, makingDeposit, beingHelped, leaving}
	MarketState mS;
	int marketTransactionAmount = 0;
	String location;
	int transactionAmount = 0;
	public int getTransactionAmount(){
		return transactionAmount;
	}
	boolean market = false;
	
	
	
	/* Messages */
	public void msgMakeDeposit(int cash){
		Do("Person taking on role of bank depositor");
		cS = CustomerState.makingTransaction;
		transactionAmount = cash;
		stateChanged();
	}
	
	public void msgMarketDeposit(int cash){
		market = true;
		Do("Market is depositing surplus into its bank account");
		//mS = MarketState.entered;
		cS = CustomerState.makingTransaction;
		stateChanged();
	}
	
	
	public void msgMakeWithdrawal(int cash){
		Do("Person taking on role of bank withdrawer");
		cS = CustomerState.makingTransaction;
		transactionAmount = 0-cash;
		stateChanged();
	}
	public void msgMakeRequest(BankTeller t){
		Do("Bank customer received message from teller to make a request");
		this.teller = t;
		if(transactionAmount < 0){
			cS = CustomerState.makingWithdrawal;
		}
		if(transactionAmount >= 0){
			cS = CustomerState.makingDeposit;
		}
		stateChanged();
	}
	
	public void msgCannotMakeTransaction(){
		Do("Bank customer does not have enough money to make request. Please come back later.");
		cS = CustomerState.leaving;
		stateChanged();
	}
	public void msgTransactionComplete(){
		Do("Bank customer received confirmation that his transaction is complete");
		cS = CustomerState.leaving;
		while(market = false){
		if(transactionAmount < 0){
			person.msgIncome(transactionAmount);
		 if(transactionAmount > 0){
		 	person.msgExpense(transactionAmount);
			
			}
		}
		}
		stateChanged();
	}
	
	
	/*Animation messages*/
	public void msgAtManager(){
		customerAnimation.release();
	}
	
	public void msgAtTeller(){
		customerAnimation.release();
	}
	public void msgLeft(){
		customerAnimation.release();
	}
	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		if(cS == CustomerState.makingTransaction) {
			MakeTransaction();
			return true;
		}
		if(cS == CustomerState.makingWithdrawal){
			MakeWithdrawal();
			return true;
		}
		if(cS == CustomerState.makingDeposit){
			MakeDeposit();
			return true;
		}
		if(cS == CustomerState.leaving){
			Leaving();
			return true;
		}
		
		
		return false;
	}
	
	
	//Actions

	public void MakeTransaction(){
		Do("Bank customer is going to manager");
		DoGoToManager();
		try {
			customerAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		manager.msgTransaction(this);
		cS = CustomerState.beingHelped; 
	}
	public void MakeWithdrawal(){
		DoGoToTeller();
		try {
			customerAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		teller.msgMakeWithdrawal(this, transactionAmount);
		cS = CustomerState.beingHelped;
	}
	public void MakeDeposit(){
		DoGoToTeller();
		try {
			customerAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		teller.msgMakeDeposit(this, transactionAmount);
		cS = CustomerState.beingHelped;
	}
	public void Leaving(){
		Do("Goodbye.");
		DoLeaveBank();
		try {
			customerAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cS = CustomerState.leaving;
	}

	/* Actions */
	
		private void DoGoToManager(){
			gui.GoToManager();
		}
		
		private void DoGoToTeller(){
			gui.GoToTeller();
		}
		
		private void DoLeaveBank(){
			gui.ExitBank();
		}
		public BankDepositorGui getGui() {
		
			return gui;
		}
		
		public void setBankGui(BankGui g){
			gui.setGui(g);
		}


}

	