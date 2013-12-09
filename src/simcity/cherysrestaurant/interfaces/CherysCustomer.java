package simcity.Anjalirestaurant.interfaces;

import simcity.Anjalirestaurant.AnjaliCashierCheck;
import simcity.Anjalirestaurant.AnjaliWaiterFood;
import simcity.Anjalirestaurant.gui.AnjaliCustomerGui;
import simcity.mock.EventLog;

import java.util.List;
import java.util.Map;


public interface AnjaliCustomer
{	
	public abstract void msgFollowMe(AnjaliWaiter w, Map<Integer, AnjaliWaiterFood> m, List<String> foo); //int seat) //* called from WaiterAgent
	public abstract void msgWhatIsYourOrder(List<String> foo); //* called from WaiterAgent
	public abstract void msgOrderServed(String ch); //* called from WaiterAgent
	public abstract void msgHereIsCheck(AnjaliCashierCheck ch); //* called from WaiterAgent
	public abstract void msgChange(double change); //* called from CashierAgent

	public abstract EventLog getLog();
	public abstract AnjaliCustomerGui getGui();
}