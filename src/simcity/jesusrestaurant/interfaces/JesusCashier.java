package simcity.jesusrestaurant.interfaces;

public interface JesusCashier {
	
	public abstract void msgComputeCheck (JesusWaiter w, JesusCustomer c, String foodItem, String name);

	public abstract void msgCustomerPayment (JesusCustomer c, int money, String name);

}