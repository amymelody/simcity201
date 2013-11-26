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
	BankTeller teller;
	BankManager manager;
	
	// Customer Status Data
	enum CustomerState {entered, makingDeposit, makingWithdrawal, makingTransaction, beingHelped, leaving, atManager, atTeller};
	CustomerState cS;
	String location;
	int transactionAmount = 0;
	
	/* Messages */
	public void msgMakeDeposit(int cash){
		Do("Person taking on role of bank depositor");
		cS = CustomerState.makingTransaction;
		transactionAmount = cash;
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
	