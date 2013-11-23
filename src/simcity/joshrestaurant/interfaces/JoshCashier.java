package simcity.joshrestaurant.interfaces;

import simcity.interfaces.MarketDeliverer;

public interface JoshCashier {
	
	public abstract void msgProduceCheck(JoshWaiter w, JoshCustomer c, String choice);
	
	public abstract void msgPayment(JoshCustomer c, int cash);
	
	public abstract void msgDelivery(int bill, MarketDeliverer deliverer);

}