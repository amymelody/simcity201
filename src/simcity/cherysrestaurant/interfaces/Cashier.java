package simcity.cherysrestaurant.interfaces;

import simcity.cherysrestaurant.CashierCheck;
import simcity.mock.EventLog;

public interface Cashier
{
	public EventLog log = new EventLog();
	
	public abstract void msgProduceCheck(Waiter w, String choice, int table); //* called from WaiterAgent
	public abstract void msgGiveCheck(Waiter w, int table); //* called from WaiterAgent
	public abstract void msgPayment(Customer cust, CashierCheck c, double cashGiven); //* called from CustomerAgent
	public abstract void msgPayForDelivery(Market m, String foodType, int amountDelivered, double wholesalePercentage); //* called from MarketAgent
}