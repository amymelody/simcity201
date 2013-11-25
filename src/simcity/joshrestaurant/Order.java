package simcity.joshrestaurant;

public class Order {
	JoshWaiterRole waiter;
	int table;
	private OrderState state;
	String choice;

	Order(JoshWaiterRole w, String c, int t, OrderState s) {
		waiter = w;
		choice = c;
		table = t;
		state = s;
	}

	JoshWaiterRole getWaiter() {
		return waiter;
	}
	
	public int getTable() {
		return table;
	}
	
	OrderState getState() {
		return state;
	}
	
	void setState(OrderState s) {
		state = s;
	}
	
	String getChoice() {
		return choice;
	}
	
	void setChoice(String c) {
		choice = c;
	}
}