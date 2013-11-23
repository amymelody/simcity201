package simcity.jesusrestaurant.interfaces;

public interface JesusWaiter {
	
	public abstract void msgReadytoOrder (String name);
	
	public abstract void msgMyOrder (String name, String order);

	public abstract void msgCheckComputed (JesusCustomer cust, double amount, String name);

	public abstract void msgLeavingTable (String n);
	
}