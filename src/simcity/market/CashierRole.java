package simcity.market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import role.Role;
import simcity.ItemOrder;
import simcity.market.Order.OrderState;

public class CashierRole extends Role {
	
	/* Data */
	
	// A list of orders
	public List<Order> orders =  Collections.synchronizedList(new ArrayList<Order>());
	
	// Lists of workers
	public List<myEmployee> employees = Collections.synchronizedList(new ArrayList<myEmployee>());
	public List<myDeliverer> deliverers = Collections.synchronizedList(new ArrayList<myDeliverer>());
	
	// Market Status Data
	int marketMoney;
	enum MarketState {open, closing, closed};
	MarketState mS;
	
	// Cashier Status Data
	int salary;
	
	
	/* Messages */
	
	// Worker interactions (hiring, enter/exit shift, etc.)
	public void msgHired(EmployeeRole e, int salary) {
		employees.add(new myEmployee(e, salary));
	}
	public void msgHired(DelivererRole d, int salary) {
		deliverers.add(new myDeliverer(d, salary));
	}
	public void msgOntheClock(EmployeeRole e) {
		synchronized(employees){
			for(myEmployee me: employees) {
				if(me.employee == e) {
					me.working = true;
				}
			}
		}
	}
	public void msgOntheClock(DelivererRole d) {
		synchronized(deliverers){
			for(myDeliverer md: deliverers) {
				if(md.deliverer == d) {
					md.working = true;
				}
			}
		}
	}
	public void msgOfftheClock(EmployeeRole e) {
		synchronized(employees){
			for(myEmployee me: employees) {
				if(me.employee == e) {
					me.working = true;
				}
			}
		}
	}
	public void msgOfftheClock(DelivererRole d) {
		synchronized(deliverers){
			for(myDeliverer md: deliverers) {
				if(md.deliverer == d) {
					md.working = false;
				}
			}
		}
	}
	public void msgDoneForTheDay() {
		mS = MarketState.closing;
	}
	
	// Normative Scenario #1
	public void msgIWantItems(CustomerRole c, List<ItemOrder> items) {
		orders.add(new Order(c, items));
		stateChanged();
	}
	public void msgHereAreItems(Order order, EmployeeRole e) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.equals(order)) {
					o.oS = OrderState.ready;
					calculatePrice(o);
				}
			}
		}
		synchronized(employees) {
			for(myEmployee me: employees) {
				if(me.employee.equals(e)) {
					me.unoccupied = true;
				}
			}
		}
		stateChanged();
	}
	public void msgPayment(CustomerRole c, int money) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.customer.equals(c)) {
					o.oS = OrderState.done;
					o.amountPaid = money;
					transaction(o);
				}
			}
		}
	}
	
	// Normative Scenario #2
	public void msgIWantDelivery(CustomerRole c, List<ItemOrder> i, String location) {
		orders.add(new Order(c, i, location));
		stateChanged();
	}
	public void msgDelivered(Order order, DelivererRole d) {
		synchronized(orders) {
			for(Order o: orders) {
				if(o.equals(order)) {
					o.oS = OrderState.delivered;
					transaction(o);
				}
			}
		}
		synchronized(deliverers) {
			for(myDeliverer md: deliverers) {
				if(md.deliverer.equals(d)) {
					md.unoccupied = true;
				}
			}
		}
		stateChanged();
	}
	
	/* Scheduler */
	public boolean pickAndExecuteAnAction() {
		if(mS == MarketState.closing) {
			closeUp();
			return true;
		}
		synchronized(orders) {
			for(Order o: orders) {
				if(o.oS == OrderState.none) {
					HandToEmployee(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for(Order o: orders) {
				if(o.oS == OrderState.newDelivery) {
					HandToDeliverer(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for(Order o: orders) {
				if(o.oS == OrderState.ready) {
					HandToCustomer(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for(Order o: orders) {
				if(o.oS == OrderState.done) {
					FinishOrder(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for(Order o: orders) {
				if(o.oS == OrderState.delivered) {
					FinishDelivery(o);
					return true;
				}
			}
		}
		return false;
	}
	
	
	/* Actions */
	private void closeUp() {
		
	}
	
	private void HandToEmployee(Order o) {
		
	}
	
	private void HandToDeliverer(Order o) {
		
	}
	
	private void HandToCustomer(Order o) {
		
	}
	
	private void FinishOrder(Order o) {
		
	}
	
	private void FinishDelivery(Order o) {
		
	}
	
	
	/* Classes used by Cashier Role */
	
	// Employee class (Cashier's view of employees)
	class myEmployee {
		EmployeeRole employee;
		boolean unoccupied;
		boolean working;
		int salary;
		
		myEmployee(EmployeeRole e, int s) {
			employee = e;
			unoccupied = true;
			working = false;
			salary = s;
		}
	}
	
	// Deliverer class (Cashier's view of deliverers)
	class myDeliverer {
		DelivererRole deliverer;
		boolean unoccupied;
		boolean working;
		int salary;
		
		myDeliverer(DelivererRole d, int s) {
			deliverer = d;
			unoccupied = true;
			working = false;
			salary = s;
		}
	}
	
	/* Calculation functions by Cashier */
	private void calculatePrice(Order o) {
		for(ItemOrder iO: o.items) {
			
		}
	}
	private void transaction(Order o) {
		o.change = o.price - o.amountPaid;
	}
}
