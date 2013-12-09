package simcity.Anjalirestaurant.interfaces;

import simcity.mock.EventLog;

public interface AnjaliCook
{
	public EventLog log = new EventLog();
	
	public abstract void msgCookThis(AnjaliWaiter w, String choice, int table); //* called from WaiterAgent.takeOrder
	public abstract void msgDelivery(AnjaliMarket m, String f, int numberDelivered); //* called from MarketAgent
	public abstract void msgStockTooLow(AnjaliMarket m, String f, int numberAvailible); //* called from MarketAgent
}