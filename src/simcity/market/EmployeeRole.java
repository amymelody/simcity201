package simcity.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import role.Role;
import simcity.ItemOrder;
import simcity.market.Order.OrderState;
import simcity.market.gui.EmployeeGui;

public class EmployeeRole extends Role {

	/* Animation */
	private Semaphore animation = new Semaphore(0, true);
	EmployeeGui gui;
	
	/* Data */
	
	// List of Orders
	public List<Order> orders =  Collections.synchronizedList(new ArrayList<Order>());
	Order currentOrder = null;
	
	// References to other roles
	CashierRole cashier;
	
	// Employee Status
	enum EmployeeState {nothing, getting, toCashier, walking, handing, done};
	EmployeeState eS = EmployeeState.nothing;
	
	/* Messages */
	public void msgGetItems(Order o) {
		orders.add(o);
		//stateChanged();
	}
	
	/* Animation Messages */
	public void msgHaveItems() {
		animation.release();
		eS = EmployeeState.toCashier;
		//stateChanged();
	}
	
	public void msgAtCashier() {
		animation.release();
		eS = EmployeeState.handing;
		//stateChanged();
	}
	
	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.oS == OrderState.handing && eS == EmployeeState.nothing && currentOrder == null) {
					eS = EmployeeState.getting;
					GetItems(o);
					return true;
				}
			}
		}
		if(eS == EmployeeState.toCashier) {
			eS = EmployeeState.walking;
			WalktoCashier();
		}
		if(eS == EmployeeState.handing) {
			eS = EmployeeState.done;
			HandItems();
		}
		return false;
	}
	
	
	/* Actions */
	private void GetItems(Order o) {
		DoGetItems(o.items); // animation
		currentOrder = o;
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void WalktoCashier() {
		DoHandItems(); // animation
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void HandItems() {
		currentOrder.oS = OrderState.ready;
		cashier.msgHereAreItems(currentOrder, this);
		currentOrder = null;
	}
	
	
	/* Animation Actions used by Customer Role */
	
	private void DoGetItems(List<ItemOrder> i) {
		gui.GetItems(i);
	}
	
	private void DoHandItems() {
		gui.GoToCashier();
	}
	
}
