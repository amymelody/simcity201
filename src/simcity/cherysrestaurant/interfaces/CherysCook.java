package simcity.cherysrestaurant.interfaces; 

import simcity.mock.EventLog;

public interface CherysCook
{
	public EventLog log = new EventLog();
	
	public abstract void msgCookThis(CherysWaiter w, String choice, int table); //* called from WaiterAgent.takeOrder
	public abstract void msgGoHome();
}