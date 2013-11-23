package simcity.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.market.interfaces.MarketCashier;
import simcity.market.interfaces.MarketDeliverer;
import simcity.role.Role;
import simcity.ItemOrder;
import simcity.PersonAgent;

public class BankCustomerRole extends Role {
	
	/* Constructors */
	String name;

	public BankCustomerRole(){
		super();
	}
	public BankCustomerRole(String n) {
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
	private Semaphore animation = new Semaphore(0, true);
	//CustomerGui gui;
	
	/* Data */
	int cash = person.getMoney();
	
	// References to other roles
	BankTellerRole teller;
	BankManagerRole manager;
	
	// Customer Status Data
	enum CustomerState {entered, makingDeposit, makingWithdrawal, beingHelped, Leaving};
	CustomerState cS;
	String location;
	
	/* Messages */
	public void msgMakeDeposit(int cash){
		cS = CustomerState.makingDeposit;
		stateChanged();
	}
	
	
	public void msgMakeWithdrawal(int cash){
		cS = CustomerState.makingWithdrawal;
		stateChanged();
	}
	
	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		if(cS == CustomerState.makingDeposit) {
			MakeTransaction(0);
			return true;
		}
		if(cS == CustomerState.makingWithdrawal){
			MakeTransaction(1);
			return true;
		}
			
		
		return false;
	}
	
	
	//Actions
	
	public void MakeTransaction(int transaction){
		manager.msgMakeTransaction(this, transaction);
		
	}
	
}
	/* Actions */
	