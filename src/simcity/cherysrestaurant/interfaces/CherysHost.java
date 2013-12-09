package simcity.Anjalirestaurant.interfaces;

import simcity.mock.EventLog;

public interface AnjaliHost
{
	public EventLog log = new EventLog();
	
	public abstract void msgImHungry(AnjaliCustomer c); //* called from CustomerAgent.alertHost
	public abstract void msgTableFree(int t, AnjaliWaiter w, AnjaliCustomer c); //* called from WaiterAgent.tableAvailible
	public abstract void msgMayIGoOnBreak(AnjaliWaiter w); //*called from WaiterAgent
	public abstract void msgBackFromBreak(AnjaliWaiter w); //*called from WaiterAgent
}
