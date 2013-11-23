package simcity.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.market.interfaces.MarketCashier;
import simcity.market.interfaces.MarketDeliverer;
import simcity.role.Role;
import simcity.ItemOrder;

public class BankCustomerRole extends Role {
	
	/* Constructors */
	String name;

	public BankCustomerRole(String n) {
		name = n;
	}

	public String getMaitreDName() {
		return name;
	}
	public String getName() {
		return name;
	}
	
	
	
	/* Animation */
	private Semaphore animation = new Semaphore(0, true);
	//CustomerGui gui;
	
	/* Data */
	
	//Money
	double cash = 100.0;
	double amount = 0.0;
	
	// References to other roles
	BankTellerRole teller;
	BankManagerRole manager;
	
	// Customer Status Data
	enum CustomerState {entered, beingHelped, Leaving};
	CustomerState cS;
	String location;
	
	/* Messages */
	
	
	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		if(cS == CustomerState.arrived && !delivery) {
			GoToCashier();
			return true;
		}
		if(cS == CustomerState.atCashier || (cS == CustomerState.arrived && delivery)) {
			GiveOrder();
		}
		if(cS == CustomerState.getting) {
			GetItems();
			return true;
		}
		if(cS == CustomerState.leaving) {
			GetOut();
			return true;
		}
		return false;
	}
	
	
	/* Actions */
	private void GoToCashier() {
		DoGoToCashier(); // animation
		cS = CustomerState.walking;
	}
	private void GiveOrder() {
		if(delivery) {
			cashier.msgIWantDelivery(this, items, location);
		}
		else {
			DoGiveOrder(); // animation
			try {
				animation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cashier.msgIWantItems(this, items);
		}
		cS = CustomerState.waiting;
	}
	
	private void GetItems() {
		if(delivery) {
			deliverer.msgPayment(cost);
		}
		else {
			DoGetItems(); // animation
			try {
				animation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cashier.msgPayment(this, cost);
		}
		cS = CustomerState.paying;
		cost = 0;
	}
	
	private void GetOut() {
		if(delivery) {
			deliverer.msgSignedInvoice();
		}
		else {
			DoGetOut(); // animation
			try {
				animation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cS = CustomerState.done;
	}
	
	
	/* Animation Actions used by Customer Role */
	
	private void DoGoToCashier() {
		gui.GoToCashier();
	}
	
	private void DoGiveOrder() {
		gui.GoToWaitingArea();
	}
	
	private void DoGetItems() {
		gui.PickUpItems();
	}
	
	private void DoGetOut() {
		gui.ExitMarket();
	}
	
}
