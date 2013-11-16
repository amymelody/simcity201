package simcity.cherysrestaurant.interfaces;

import simcity.mock.EventLog;

public interface Cook
{
	public EventLog log = new EventLog();
	
	public abstract void msgCookThis(Waiter w, String choice, int table); //* called from WaiterAgent.takeOrder
	public abstract void msgDelivery(Market m, String f, int numberDelivered); //* called from MarketAgent
	public abstract void msgStockTooLow(Market m, String f, int numberAvailible); //* called from MarketAgent
}