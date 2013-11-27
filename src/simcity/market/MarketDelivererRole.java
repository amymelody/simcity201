package simcity.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import simcity.ItemOrder;
import simcity.PersonAgent;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketDeliverer;
import simcity.interfaces.RestCashier;
import simcity.market.Order.OrderState;
import simcity.market.gui.MarketDelivererGui;
import simcity.role.JobRole;

public class MarketDelivererRole extends JobRole implements MarketDeliverer {

	/* Constructor */
	String name;

	public MarketDelivererRole() {
		super();
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
	public void setCashier(MarketCashier ch) {
		cashier = ch;
		ch.msgHired(this, person.getSalary());
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
	public List getOrders() {
		return orders;
	}


	/* Animation */
	private Semaphore animation = new Semaphore(0, true);
	MarketDelivererGui gui;
	public void setGui(MarketDelivererGui g){
		gui = g;
	}


	/* Data */

	// List of Orders
	public List<Order> orders =  Collections.synchronizedList(new ArrayList<Order>());
	public Order currentOrder = null;

	// References to other roles
	MarketCashier cashier;

	// Deliverer Status
	public enum DelivererState {startedWork, nothing, going, arrived, goingBack, arrivedBack};
	public DelivererState dS = DelivererState.nothing;
	public boolean working;


	/* Messages */

	// Start/End Shifts
	public void msgStartShift() {
		working = true;
		dS = DelivererState.startedWork;
		stateChanged();
	}
	public void msgEndShift() {
		working = false;
		stateChanged();
	}

	// Normative Scenarios
	public void msgDeliverItems(Order o) {
		o.oS = OrderState.newDelivery;
		orders.add(o);
		stateChanged();
	}
	public void msgPayment(RestCashier c, int money) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.cashier.equals(c)) {
					o.amountPaid = money;
					o.oS = OrderState.paying;
				}
			}
		}
		stateChanged();
	}
	/* Animation Messages */
	public void msgArrived() {
		animation.release();
		dS = DelivererState.arrived;
		stateChanged();
	}
	public void msgArrivedBack() {
		animation.release();
		dS = DelivererState.arrivedBack;
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
			if(dS == DelivererState.startedWork) {
				letCashierKnowStart();
			}
			synchronized(orders) {
				for(Order o: orders) {
					if(currentOrder == null && o.oS == OrderState.newDelivery) {
						goToCustomer(o);
						return true;
					}
					if(currentOrder.equals(o) && dS == DelivererState.arrived && o.oS == OrderState.newDelivery) {
						deliverOrder(o);
						return true;
					}
					if(o.equals(currentOrder) && o.oS == OrderState.paying) {
						takePayment(o);
						return true;
					}
					if(currentOrder.equals(o) && dS == DelivererState.arrivedBack) {
						finishDelivery(o);
						return true;
					}
				}
			}
		}
		return false;
	}


	/* Actions */
	private void letCashierKnowStart() {
		cashier.msgOntheClock(this);
	}
	private void leaveMarket() {
		cashier.msgOfftheClock(this);
		gui.leave();
	}
	private void goToCustomer(Order o) {
		currentOrder = o;
		if(!unitTest) {
			DoDeliverOrder(o.location); //animation
			try {
				animation.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void deliverOrder(Order o) {
		o.oS = OrderState.ready;
		o.cook.msgDelivery(o.items);
		o.cashier.msgDelivery(o.price, (simcity.interfaces.MarketDeliverer) this);
	}

	private void takePayment(Order o) {
		o.change = o.amountPaid - o.price;
		o.oS = OrderState.paid;
		o.cashier.msgThankYou(o.change);
		if(!unitTest)
			DoGoBack();
	}

	private void finishDelivery(Order o) {
		cashier.msgDelivered(currentOrder, this);
		currentOrder = null;
		orders.remove(o);
		stateChanged();
	}


	/* Animation Actions used by Customer Role */

	private void DoDeliverOrder(String location) {
		gui.Deliver(location);
		dS = DelivererState.going;
	}

	private void DoGoBack() {
		gui.GoToCashier();
		dS = DelivererState.goingBack;
	}

}
