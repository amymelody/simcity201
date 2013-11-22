package simcity.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.role.Role;
import simcity.ItemOrder;
import simcity.market.gui.MarketCustomerGui;
import simcity.market.interfaces.MarketCashier;
import simcity.market.interfaces.MarketCustomer;
import simcity.market.interfaces.MarketDeliverer;

public class MarketCustomerRole extends Role implements MarketCustomer {
	
	/* Constructors */
	String name;

	public MarketCustomerRole(String n) {
		name = n;
	}
	public MarketCustomerRole(String n, List<ItemOrder> i) {
		name = n;
		items = i;
	}
	public MarketCustomerRole(String n, List<ItemOrder> i, String l) {
		name = n;
		items = i;
		location = l;
	}
	
	public String getMaitreDName() {
		return name;
	}
	public String getName() {
		return name;
	}
	
	
	/* Hacks */
	public void setCashier(MarketCashier ch) {
		cashier = ch;
	}
	public void setDeliverer(MarketDeliverer d) {
		deliverer = d;
	}
	public void setItems(List<ItemOrder> i) {
		items = i;
	}
	public void setLocation(String l) {
		location = l;
	}
	
	
	/* Animation */
	private Semaphore animation = new Semaphore(0, true);
	MarketCustomerGui gui;
	
	
	/* Data */
	
	// Order items information
	public List<ItemOrder> items;
	int cost;
	boolean delivery;
	
	// References to other roles
	MarketCashier cashier;
	MarketDeliverer deliverer;
	
	// Customer Status Data
	enum CustomerState {arrived, atCashier, waiting, getting, paying, leaving, done, walking};
	CustomerState cS;
	String location;
	
	
	/* Messages */
	public void msgOrderItems(List<ItemOrder> i, boolean d, String l) {
		items = i;
		delivery = d;
		location = l;
		cS = CustomerState.arrived;
		stateChanged();
	}
	
	public void msgHereAreItemsandPrice(List<ItemOrder> i, int price) {
		person.msgExpense(price);
		person.msgReceivedItems(i);
		cost = price;
		cS = CustomerState.getting;
		stateChanged();
	}
	
	public void msgHereIsOrder(List<ItemOrder> i, int price, MarketDeliverer del) {
		person.msgExpense(price);
		person.msgReceivedItems(i);
		cost = price;
		cS = CustomerState.getting;
		deliverer = del;
		stateChanged();
	}
	
	public void msgThankYou(int change) {
		person.msgIncome(change);
		cS = CustomerState.leaving;
		stateChanged();
	}
	
	
	/* Animation Messages */
	public void msgAtCashier() {
		animation.release();
		cS = CustomerState.atCashier;
		stateChanged();
	}
	
	public void msgAtWaitingArea() {
		animation.release();
		stateChanged();
	}
	
	public void msgLeft() {
		animation.release();
		stateChanged();
	}
	
	
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
			deliverer.msgPayment(this, cost);
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
			deliverer.msgSignedInvoice(this);
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
