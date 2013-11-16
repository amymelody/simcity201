package simcity.cherysrestaurant.interfaces;

import simcity.cherysrestaurant.CashierCheck;
import simcity.cherysrestaurant.WaiterFood;
import simcity.cherysrestaurant.gui.CustomerGui;
import simcity.mock.EventLog;

import java.util.List;
import java.util.Map;


public interface Customer
{	
	public abstract void msgFollowMe(Waiter w, Map<Integer, WaiterFood> m, List<String> foo); //int seat) //* called from WaiterAgent
	public abstract void msgWhatIsYourOrder(List<String> foo); //* called from WaiterAgent
	public abstract void msgOrderServed(String ch); //* called from WaiterAgent
	public abstract void msgHereIsCheck(CashierCheck ch); //* called from WaiterAgent
	public abstract void msgChange(double change); //* called from CashierAgent

	public abstract EventLog getLog();
	public abstract CustomerGui getGui();
}