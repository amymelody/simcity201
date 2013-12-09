package simcity.Anjalirestaurant.interfaces;

import simcity.Anjalirestaurant.AnjaliCashierCheck;
import simcity.mock.EventLog;

import java.util.List;

public interface AnjaliWaiter
{
	public abstract void msgPleaseSeatCustomer(AnjaliCustomer c, int table); //* called from HostAgent.assignCustomer
	public abstract void msgReadyToOrder(AnjaliCustomer c); //* called from CustomerAgent.flagWaiter
	public abstract void msgHereIsMyOrder(AnjaliCustomer c, String choice); //* called from CustomerAgent.placeOrder
	public abstract void msgOutOfFood(String choice, int table, List<String> foo); //* called from CookAgent
	public abstract void msgOrderReady(String choice, int table, List<String> foo); //* called from CookAgent.alertWaiter
	public abstract void msgDoneEating(AnjaliCustomer c); //* called from CustomerAgent.done
	public abstract void msgHereIsCheck(AnjaliCashierCheck ch); //*called from CashierAgent
	public abstract void msgLeavingRestaurant(AnjaliCustomer c); //*called from CustomerAgent
 	public abstract void msgGoOnBreak(boolean tf); //*called from HostAgent
 	
 	public abstract AnjaliCashierCheck getCheck();
	public abstract EventLog getLog();
}