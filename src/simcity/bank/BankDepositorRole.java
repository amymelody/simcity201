package simcity.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.bank.gui.BankDepositorGui;
import simcity.bank.gui.BankGui;
import simcity.interfaces.BankDepositor;

import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;
import simcity.interfaces.Person;


import simcity.interfaces.Person;
import simcity.bank.test.mock.MockBankManager;
import simcity.interfaces.BankManager;
import simcity.interfaces.BankTeller;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketDeliverer;

import simcity.role.Role;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;
import simcity.ItemOrder;
import simcity.PersonAgent;

public class BankDepositorRole extends Role implements BankDepositor{
	//
	/* Constructors */
	String name;
	public boolean unitTesting = false;
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
	public void setGui(BankDepositorGui g) {
		gui = g;
	}
	
	public boolean robber = false;
			
	public boolean getRobberStatus(){
		return robber;
	}
	/* Animation */
	private Semaphore customerAnimation = new Semaphore(0, true);
	BankDepositorGui gui;
	
	
	/* Data */
	
	// References to other roles
	BankTeller teller;
	BankManager manager;
	
	// Customer Status Data
	public enum CustomerState {entered, makingRequest, makingTransaction, beingHelped, 
		leaving, atManager, atTeller, out, waiting, makingLoan,
		robberEntered, robAttempt, robberKilled, robberLeft};
	CustomerState cS = CustomerState.entered;
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
	
	
	/*Animation messages*/

	
	/* Messages */
	public void msgMakeDeposit(int cash){
		//Do("Person taking on role of bank depositor");
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "Bank depositor wants to make a deposit");
		cS = CustomerState.makingTransaction;
		transactionAmount = cash;
		stateChanged();
	}
	
	public void msgBusinessDeposit(int cash){
		market = true;
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I'm a business, I want to deposit surplus");
		//mS = MarketState.entered;
		cS = CustomerState.makingTransaction;
		stateChanged();
	}
	
	
	public void msgMakeWithdrawal(int cash){
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I want to make a withdrawal. I have " + person.getMoney() + "right now.");
		cS = CustomerState.makingTransaction;
		transactionAmount = -cash;
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I want to make a withdrawal of " + transactionAmount);
		stateChanged();
	}
	public void msgGoToTellerDesk(){
		gui.GoToTeller();
	}
	public void msgMakeRequest(BankTeller t){
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I'm making a request to teller");
		this.teller = t;
		cS = CustomerState.makingRequest;
		stateChanged();
	}
	
	public void msgCannotMakeTransaction(){
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "Ok, I guess I'll make a loan");
		
		cS = CustomerState.makingLoan;
		stateChanged();
	}
	
	public void msgLoanDenied(){
		cS = CustomerState.leaving;
		stateChanged();
	}
	
	public void msgTransactionComplete(){
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I've received confirmation that my transaction was successful");

		cS = CustomerState.leaving;
		
		if(market == false){
			if(transactionAmount < 0){
				person.msgIncome(-transactionAmount);
			}
			else if(transactionAmount > 0){
		 	person.msgExpense(transactionAmount);
			}
			}
		
		
		stateChanged();
	}
	
	
	////Bank Robber scenario
	
	public void msgImARobber(){
		robber = true;
		cS = CustomerState.robberEntered;
		stateChanged();
	}
	
	public void msgYoureDead(){
		cS = CustomerState.robberKilled;
		stateChanged();
	}
	
	public void msgLeaveMyBank(){
		cS = CustomerState.leaving;
		stateChanged();
	}
	/*Animation messages*/
	public void msgAtDestination(){
		customerAnimation.release();
		
	}
	
	
	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		if(cS == CustomerState.makingTransaction) {
			MakeTransaction();
			return true;
		}
		if(cS == CustomerState.makingRequest){
			cS = CustomerState.waiting;

			MakeTellerRequest();
			return true;
		}
		if(cS == CustomerState.makingLoan){
			cS = CustomerState.waiting;
			MakeLoan();
			return true;
		}
		
		if(cS == CustomerState.leaving){
			cS = CustomerState.out;
			Leaving();
			return true;
		}
		//For robber scenario
		if(cS == CustomerState.robberEntered){
			cS = CustomerState.waiting;
			RobBank();
			return true;
		}
		if(cS == CustomerState.robberKilled){
			cS = CustomerState.waiting;
			ReturnMoney();
			return true;
		}
		
		return false;
	}
	
	
	//Actions

	public void MakeTransaction(){
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I'm going to the managers desk");
		if(!unitTesting){
		DoGoToManager();
		
		try {
			customerAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		manager.msgTransaction(this);
		cS = CustomerState.beingHelped; 
	}
	public void MakeTellerRequest(){
		
		teller.msgMakeRequest(this, transactionAmount);
		cS = CustomerState.beingHelped;
	}
	public void MakeLoan(){
		teller.msgMakeLoanRequest(this);
	}
	public void Leaving(){
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I'm leaving the bank");
		if(!unitTesting){
		DoLeaveBank();
		try {
			customerAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		}

		if(this.robber){
			this.robber = false;
			person.msgGoodGuyAgain();
		}
		person.msgLeftDestination(this);
		
	}
	
	/////Rob bank actions
	public void RobBank(){
		if(!unitTesting){
		gui.RobBank();
		try {
			customerAnimation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "I'm robbing the bank biotch");

		manager.msgImRobbingYourBank(this, 300);
		
	}
	
	public void ReturnMoney(){
		manager.msgHeresYourMoneyBack(this, 300);
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "Fine take your money back");

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
		
		public void DoGoRobBank(){
			gui.RobBank();
		}
		
		public void setBankGui(BankGui g){
			gui.setGui(g);
		}


}

	