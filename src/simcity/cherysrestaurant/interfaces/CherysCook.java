package simcity.cherysrestaurant.interfaces; 

import simcity.mock.EventLog;

public interface CherysCook
{
	public EventLog log = new EventLog();
	
	public abstract void msgCookThis(CherysWaiter w, String choice, int table); //* called from WaiterAgent.takeOrder
	public abstract void msgDelivery(CherysMarket m, String f, int numberDelivered); //* called from MarketAgent
	public abstract void msgStockTooLow(CherysMarket m, String f, int numberAvailible); //* called from MarketAgent
	public abstract void msgGoHome();
}