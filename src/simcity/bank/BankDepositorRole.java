package simcity.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.bank.gui.BankDepositorGui;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketDeliverer;
import simcity.role.Role;
import simcity.ItemOrder;
import simcity.PersonAgent;

public class BankDepositorRole extends Role implements BankDepositor{
	//
	/* Constructors */
	String name;

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
	
	public void setPerson(PersonAgent p){
		super.setPerson(p);
		name = p.getName();
	}
	public void setManager(BankManagerRole b) {
		manager = b;
	}
	/* Animation */
	private Semaphore customerAnimation = new Semaphore(0, true);
	BankDepositorGui gui;
	
	/* Data */
	int cash = person.getMoney();
	
	// References to other roles
	BankTellerRole teller;
	BankManagerRole manager;
	
	// Customer Status Data
	enum CustomerState {entered, makingDeposit, makingWithdrawal, makingTransaction, beingHelped, leaving, atManager, atTeller};
	CustomerState cS;
	String location;
	int transactionAmount = 0;
	
	/* Messages */
	public void msgMakeDeposit(int cash){
		cS = CustomerState.makingTransaction;
		transactionAmount = cash;
		stateChanged();
	}
	
	
	public void msgMakeWithdrawal(int cash){
		cS = CustomerState.makingTransaction;
		transactionAmount = 0-cash;
		stateChanged();
	}
	public void msgMakeRequest(BankTellerRole t){
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
		//Not enough money in account for transaction
	}
	public void msgTransactionComplete(){
		cS = CustomerState.leaving;
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
		
		return false;
	}
	
	
	//Actions
	
	public void MakeTransaction(){
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
}
	