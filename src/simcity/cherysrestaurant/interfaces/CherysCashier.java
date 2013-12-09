package simcity.Anjalirestaurant.interfaces;

import simcity.Anjalirestaurant.AnjaliCashierCheck;
import simcity.mock.EventLog;

public interface AnjaliCashier
{
	public EventLog log = new EventLog();
	
	public abstract void msgProduceCheck(AnjaliWaiter w, String choice, int table); //* called from WaiterAgent
	public abstract void msgGiveCheck(AnjaliWaiter w, int table); //* called from WaiterAgent
	public abstract void msgPayment(AnjaliCustomer cust, AnjaliCashierCheck c, double cashGiven); //* called from CustomerAgent
	public abstract void msgPayForDelivery(AnjaliMarket m, String foodType, int amountDelivered, double wholesalePercentage); //* called from MarketAgent
}