package simcity.anjalirestaurant;

import java.util.*;

import simcity.anjalirestaurant.AnjaliCookRole.Order;

public class RevolvingStandMonitor extends Object {
	
	private final int MAX = 3;
	private int count = 0;
	private Vector<Order> orders;
	
	public RevolvingStandMonitor() {
		orders = new Vector<Order>();
	}
	
	synchronized public void add(Order o) {
		while (count == MAX) {
			try {
				//System.out.println("\tFull, waiting");
				wait(5000);
			} catch (InterruptedException e) {};
		}
		
		addOrder(o);
		count++;
		if (count == 1) {
			notify();
		}
	}
	
	synchronized public Order remove() {
		Order order;
		/*while (count == 0) {
			try {
				//System.out.println("\tEmpty, waiting");
				wait(5000);
			} catch (InterruptedException e) {};
		}*/
		if (count == 0) {
			return null;
		}
		
		order = removeOrder();
		count--;
		if (count == MAX-1) {
			notify();
		}
		return order;
	}
	
	private void addOrder(Order o) {
		orders.addElement(o);
	}
	
	private Order removeOrder() {
		Order order = orders.firstElement();
		orders.removeElementAt(0);
		return order;
	}
}
