package simcity.cherysrestaurant.interfaces;

import simcity.cherysrestaurant.CherysCashierCheck;
import simcity.cherysrestaurant.CherysWaiterFood;
import simcity.cherysrestaurant.gui.CherysCustomerGui;
import simcity.mock.EventLog;

import java.util.List;
import java.util.Map;


public interface CherysCustomer
{	
	public abstract void msgFollowMe(CherysWaiter w, Map<Integer, CherysWaiterFood> m, List<String> foo); //int seat) //* called from WaiterAgent
	public abstract void msgWhatIsYourOrder(List<String> foo); //* called from WaiterAgent
	public abstract void msgOrderServed(String ch); //* called from WaiterAgent
	public abstract void msgHereIsCheck(CherysCashierCheck ch); //* called from WaiterAgent
	public abstract void msgChange(double change); //* called from CashierAgent

	public abstract EventLog getLog();
	public abstract CherysCustomerGui getGui();
}