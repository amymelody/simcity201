package simcity.cherysrestaurant.interfaces;

import simcity.mock.EventLog;

public interface Market
{
	public abstract void msgPlaceOrder(String f, int orderNumber); //*called from CookAgent
	public abstract void msgPaymentForDelivery(double payment); //*called from CashierAgent

	public abstract EventLog getLog();
}