package simcity.joshrestaurant.interfaces;

public interface JoshCashier {
	
	public abstract void msgProduceCheck(JoshWaiter w, JoshCustomer c, String choice);
	
	public abstract void msgPayment(JoshCustomer c, int cash);
	
	public abstract void msgHereIsBill(int bill, JoshMarket m);

}