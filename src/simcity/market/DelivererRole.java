package simcity.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import role.Role;
import simcity.ItemOrder;
import simcity.market.Order.OrderState;
import simcity.market.gui.DelivererGui;

public class DelivererRole extends Role {

	/* Animation */
	private Semaphore animation = new Semaphore(0, true);
	DelivererGui gui;
	
	/* Data */
	
	// List of Orders
	public List<Order> orders =  Collections.synchronizedList(new ArrayList<Order>());
	Order currentOrder = null;
	
	// References to other roles
	CashierRole cashier;
	
	// Employee Status
	enum DelivererState {nothing, going, arrived, goingBack, arrivedBack};
	DelivererState dS = DelivererState.nothing;
	
	/* Messages */
	public void msgDeliverItems(Order o) {
		orders.add(o);
		stateChanged();
	}
	public void msgPayment(CustomerRole c, int money) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.customer.equals(c)) {
					o.amountPaid = money;
					o.oS = OrderState.paying;
					stateChanged();
				}
			}
		}
	}
	public void msgSignedInvoice(CustomerRole c) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.customer.equals(c)) {
					o.oS = OrderState.done;
					stateChanged();
				}
			}
		}
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
	
	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		synchronized(orders) {
			for(Order o: orders) {
				if(currentOrder == null && o.oS == OrderState.newDelivery) {
					goToCustomer(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for(Order o: orders) {
				if(currentOrder.equals(o) && dS == DelivererState.arrived) {
					deliverOrder(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for(Order o: orders) {
				if(o.equals(currentOrder) && o.oS == OrderState.paying) {
					takePayment(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for(Order o: orders) {
				if(o.equals(currentOrder) && o.oS == OrderState.done) {
					goToMarket(o);
					return true;
				}
			}
		}	
		synchronized(orders) {
			for(Order o: orders) {
				if(currentOrder.equals(o) && dS == DelivererState.arrivedBack) {
					finishDelivery(o);
					return true;
				}
			}
		}
		return false;
	}
	
	
	/* Actions */
	private void goToCustomer(Order o) {
		DoDeliverOrder(o.location); // animation
		currentOrder = o;
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void deliverOrder(Order o) {
		o.oS = OrderState.ready;
		o.customer.msgHereIsOrder(o.items, o.price, this);
	}
	
	private void takePayment(Order o) {
		o.change = o.amountPaid - o.price;
		o.oS = OrderState.paid;
		o.customer.msgThankYou(o.change);
	}
	
	private void goToMarket(Order o) {
		DoGoBack(); // animation
		try {
			animation.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void finishDelivery(Order o) {
		cashier.msgDelivered(currentOrder, this);
		currentOrder = null;
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
