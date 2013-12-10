package simcity.cherysrestaurant.interfaces;
 
import simcity.mock.EventLog;

public interface CherysHost
{
	public EventLog log = new EventLog();
	
	public abstract void msgImHungry(CherysCustomer c); //* called from CustomerAgent.alertHost
	public abstract void msgTableFree(int t, CherysWaiter w, CherysCustomer c); //* called from WaiterAgent.tableAvailible
	public abstract void msgMayIGoOnBreak(CherysWaiter w); //*called from WaiterAgent
	public abstract void msgBackFromBreak(CherysWaiter w); //*called from WaiterAgent
}
