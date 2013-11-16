package simcity.cherysrestaurant.interfaces;

import simcity.mock.EventLog;

public interface Host
{
	public EventLog log = new EventLog();
	
	public abstract void msgImHungry(Customer c); //* called from CustomerAgent.alertHost
	public abstract void msgTableFree(int t, Waiter w, Customer c); //* called from WaiterAgent.tableAvailible
	public abstract void msgMayIGoOnBreak(Waiter w); //*called from WaiterAgent
	public abstract void msgBackFromBreak(Waiter w); //*called from WaiterAgent
}
