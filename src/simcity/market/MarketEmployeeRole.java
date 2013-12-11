package simcity.market;

import java.util.ArrayList;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import simcity.role.JobRole;
import simcity.role.Role;
import simcity.ItemOrder;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketEmployee;
import simcity.interfaces.Person;
import simcity.market.MarketDelivererRole.DelivererState;
import simcity.market.Order.OrderState;
import simcity.market.gui.MarketCustomerGui;
import simcity.market.gui.MarketEmployeeGui;

public class MarketEmployeeRole extends JobRole implements MarketEmployee {

	/* Constructor */
	String name;

	public MarketEmployeeRole() {
		super();
	}

	public String getMaitreDName() {
		return name;
	}
	public String getName() {
		return name;
	}
	public void setCashier(MarketCashier ch) {
		cashier = ch;
		ch.msgHired(this, person.getSalary());
	}
	public void setPerson(Person p) {
		super.setPerson(p);
		name = p.getName();
	}


	/* Hacks */
	public void setOrder(MarketCustomer c, List<ItemOrder> i) {
		orders.add(new Order(c, i));
	}
	boolean unitTest = false;
	public void setUnitTest(boolean uT) {
		unitTest = uT;
	}


	/* Accessors */
	public Queue getOrders() {
		return orders;
	}


	/* Animation */
	private Semaphore animation = new Semaphore(0, true);
	MarketEmployeeGui gui;
	public void setGui(MarketEmployeeGui g){
		gui = g;
	}


	/* Data */

	// List of Orders
	public Queue<Order> orders =  new LinkedList<Order>();
	public Order currentOrder = null;

	// References to other roles
	MarketCashier cashier;

	// Employee Status
	public enum EmployeeState {startedWork, nothing, getting, toCashier, walking, handing, done};
	public EmployeeState eS = EmployeeState.nothing;
	public boolean working;


	/* Messages */

	// Start/End Shifts
	public void msgStartShift() {
		working = true;
		eS = EmployeeState.startedWork;
		stateChanged();
	}
	public void msgEndShift() {
		working = false;
		stateChanged();
	}

	// Normative Scenarios
	public void msgGetItems(Order o) {
		orders.add(new Order(o));
		eS = EmployeeState.nothing;
		stateChanged();
	}
	public void msgPay() {
		person.msgEndShift();
	}

	/* Animation Messages */
	public void msgHaveItems() {
		animation.release();
		eS = EmployeeState.toCashier;
		stateChanged();
	}

	public void msgAtCashier() {
		animation.release();
		eS = EmployeeState.handing;
		stateChanged();
	}
	
	public void left() {
		person.msgLeftDestination(this);
	}


	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		if(!working) {
			leaveMarket();
			return true;
		}
		else {
			if(eS == EmployeeState.startedWork) {
				letCashierKnow();
			}
			if(orders.size() != 0 && eS == EmployeeState.nothing && currentOrder == null) {
				eS = EmployeeState.getting;
				GetItems();
				return true;
			}
			if(eS == EmployeeState.toCashier) {
				eS = EmployeeState.walking;
				WalktoCashier();
				return true;
			}
			if(eS == EmployeeState.handing) {
				eS = EmployeeState.nothing;
				HandItems();
				return true;
			}
		}

		return false;
	}


	/* Actions */
	private void letCashierKnow() {
		cashier.msgOntheClock(this);
		gui.work();
	}
	private void leaveMarket() {
		gui.leave();
		cashier.msgOfftheClock(this);
	}
	private void GetItems() {
		currentOrder = orders.poll();
		if(!unitTest) {
			DoGetItems(currentOrder.items); // animation
			try {
				animation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void WalktoCashier() {
		if(!unitTest) {
			DoHandItems(); // animation
			try {
				animation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void HandItems() {
		AlertLog.getInstance().logMessage(AlertTag.MARKET, name, "Here are items");
		currentOrder.oS = OrderState.ready;
		cashier.msgHereAreItems(currentOrder, this);
		currentOrder = null;
		eS = EmployeeState.nothing;
		stateChanged();
	}


	/* Animation Actions used by Customer Role */

	private void DoGetItems(List<ItemOrder> i) {
		gui.GetItems(i);
	}

	private void DoHandItems() {
		gui.GoToCashier();
	}

}
