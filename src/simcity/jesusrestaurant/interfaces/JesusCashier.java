package simcity.jesusrestaurant.interfaces;

public interface JesusCashier {
	
	public abstract void msgComputeCheck (JesusWaiter w, JesusCustomer c, String foodItem, String name);

	public abstract void msgCustomerPayment (JesusCustomer c, double money, String name);
	
	public abstract void msgHereIsBill(JesusMarket m, double amount, int id);

}