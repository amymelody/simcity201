package simcity.market;

import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.role.Role;
import simcity.ItemOrder;
import simcity.PersonAgent;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.Person;
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
	public void setPerson(Person p) {
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
	boolean unitTest = false;
	public void setUnitTest(boolean uT) {
		unitTest = uT;
	}


	/* Animation */
	private Semaphore animation = new Semaphore(0, true);
	MarketCustomerGui gui;
	public void setGui(MarketCustomerGui g){
		gui = g;
	}
	int waitingChairX, waitingChairY;


	/* Data */

	// Order items information
	public List<ItemOrder> items;
	public List<ItemOrder> itemsReceived;
	public int cost;
	public int change;
	boolean delivery;

	// References to other roles
	MarketCashier cashier;

	// Customer Status Data
	public enum CustomerState {nothing, arrived, atCashier, confirming, waiting, getting, paying, leaving, done, out, walking, ready, atPickUp};
	public CustomerState cS = CustomerState.nothing;
	String location;


	/* Messages */
	public void msgOrderItems(List<ItemOrder> i) {
		items = i;
		cS = CustomerState.arrived;
		stateChanged();
	}

	public void msgHereIsWhatICanFulfill(List<ItemOrder> orders, boolean canFulfill, int waitX, int waitY) {
		if(canFulfill)
			cS = CustomerState.waiting;
		else
			cS = CustomerState.leaving;
		waitingChairX = waitX;
		waitingChairY = waitY;
	}

	public void msgOrderReady() {
		cS = CustomerState.ready;
	}

	public void msgHereAreItemsandPrice(List<ItemOrder> i, int price) {
		cost = price;
		cS = CustomerState.getting;
		stateChanged();
	}

	public void msgThankYou(int chnge) {
		change = chnge;
		cS = CustomerState.leaving;
		stateChanged();
	}


	/* Animation Messages */
	public void msgAtCashier() {
		animation.release();
		cS = CustomerState.atCashier;
		stateChanged();
	}

	public void msgAtPickUp() {
		animation.release();
		cS = CustomerState.atPickUp;
		stateChanged();
	}

	public void msgOut() {
		cS = CustomerState.out;
		person.msgLeftDestination(this);
		stateChanged();
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
		if(cS == CustomerState.ready) {
			GoingToPickUp();
			return true;
		}
		if(cS == CustomerState.atPickUp) {
			AtDesk();
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
		if(cS == CustomerState.out) {
			updateInfo();
			return true;
		}
		return false;
	}


	/* Actions */
	private void GoToCashier() {
		cS = CustomerState.walking;
		if(!unitTest) {
			DoGoToCashier(); // animation
			try {
				animation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void GiveOrder() {
		cashier.msgIWantItems(this, items);
		cS = CustomerState.confirming;
	}
	private void WillWait() {
		cS = CustomerState.walking;
		if(!unitTest) {
			DoWillWait(); // animation
		}
	}

	private void GoingToPickUp() {
		cS = CustomerState.walking;
		if(!unitTest) {
			DoGetItems(); // animation
			try {
				animation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void AtDesk() {
		cS = CustomerState.nothing;
		cashier.msgImHere(this);
	}

	private void GetItems() {
		cashier.msgPayment(this, cost);
		cS = CustomerState.paying;
		cost = 0;
	}

	private void GetOut() {
		cS = CustomerState.done;
		DoGetOut(); // animation
		waitingChairX = 0;
		waitingChairY = 0;
		person.msgReceivedItems(items);
		items = null;
	}

	private void updateInfo() {
		person.msgExpense(cost);
		person.msgIncome(change);
		person.msgReceivedItems(itemsReceived);
	}


	/* Animation Actions used by Customer Role */

	private void DoGoToCashier() {
		gui.GoToCashier();
	}

	private void DoWillWait() {
		gui.GoToWaitingArea(waitingChairX, waitingChairY);
	}

	private void DoGetItems() {
		gui.PickUpItems();
	}

	private void DoGetOut() {
		gui.ExitMarket();
	}

}
