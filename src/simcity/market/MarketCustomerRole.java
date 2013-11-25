package simcity.market;

import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.role.Role;
import simcity.ItemOrder;
import simcity.PersonAgent;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.market.gui.MarketCustomerGui;

public class MarketCustomerRole extends Role implements MarketCustomer {

	/* Constructors */
	String name;

	public MarketCustomerRole() {
		super();
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
	public void setPerson(PersonAgent p) {
		super.setPerson(p);
		name = p.getName();
	}


	/* Hacks */
	public void setCashier(MarketCashier ch) {
		cashier = ch;
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
	public void setGui(MarketCustomerGui g){
		gui = g;
	}
	

	/* Data */

	// Order items information
	public List<ItemOrder> items;
	public int cost;
	boolean delivery;

	// References to other roles
	MarketCashier cashier;

	// Customer Status Data
	public enum CustomerState {nothing, arrived, atCashier, confirming, waiting, getting, paying, leaving, done, walking};
	public CustomerState cS = CustomerState.nothing;
	String location;


	/* Messages */
	public void msgOrderItems(List<ItemOrder> i) {
		items = i;
		cS = CustomerState.arrived;
		//stateChanged();
	}

	public void msgHereIsWhatICanFulfill(List<ItemOrder> orders, boolean canFulfill) {
		if(canFulfill)
			cS = CustomerState.waiting;
		else
			cS = CustomerState.leaving;
	}
	
	public void msgHereAreItemsandPrice(List<ItemOrder> i, int price) {
		person.msgExpense(price);
		person.msgReceivedItems(i);
		cost = price;
		cS = CustomerState.getting;
		//stateChanged();
	}

	public void msgThankYou(int change) {
		person.msgIncome(change);
		cS = CustomerState.leaving;
		//stateChanged();
	}


	/* Animation Messages */
	public void msgAtCashier() {
		animation.release();
		cS = CustomerState.atCashier;
		//stateChanged();
	}


	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		if(cS == CustomerState.arrived) {
			GoToCashier();
			return true;
		}
		if(cS == CustomerState.atCashier) {
			GiveOrder();
			return true;
		}
		if(cS == CustomerState.waiting) {
			WillWait();
			return true;
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
		cS = CustomerState.walking;
		DoGoToCashier(); // animation
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void GiveOrder() {
		cashier.msgIWantItems(this, items);
		cS = CustomerState.confirming;
	}
	private void WillWait() {
		cS = CustomerState.walking;
		DoWillWait(); // animation
	}

	private void GetItems() {
		cashier.msgPayment(this, cost);
		cS = CustomerState.paying;
		cost = 0;
		DoGetItems(); // animation
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void GetOut() {
		cS = CustomerState.done;
		DoGetOut(); // animation
	}


	/* Animation Actions used by Customer Role */

	private void DoGoToCashier() {
		gui.GoToCashier();
	}

	private void DoWillWait() {
		gui.GoToWaitingArea();
	}

	private void DoGetItems() {
		gui.PickUpItems();
	}

	private void DoGetOut() {
		gui.ExitMarket();
	}

}
