package simcity.cherysrestaurant.interfaces;

import simcity.cherysrestaurant.CherysCashierCheck;
import simcity.mock.EventLog;

public interface CherysCashier
{
	public EventLog log = new EventLog();
	
	public abstract void msgProduceCheck(CherysWaiter w, String choice, int table); //* called from WaiterAgent
	public abstract void msgGiveCheck(CherysWaiter w, int table); //* called from WaiterAgent
	public abstract void msgPayment(CherysCustomer cust, CherysCashierCheck c, int cashGiven); //* called from CustomerAgent
	public abstract void msgPayForDelivery(CherysMarket m, String foodType, int amountDelivered, double wholesalePercentage); //* called from MarketAgent
	public abstract void msgPaySalary(int salary);
}