package simcity.cherysrestaurant.interfaces; 

import simcity.cherysrestaurant.CherysCashierCheck;
import simcity.mock.EventLog;

import java.util.List;

public interface CherysWaiter
{
	public abstract void msgPleaseSeatCustomer(CherysCustomer c, int table); //* called from HostAgent.assignCustomer
	public abstract void msgReadyToOrder(CherysCustomer c); //* called from CustomerAgent.flagWaiter
	public abstract void msgHereIsMyOrder(CherysCustomer c, String choice); //* called from CustomerAgent.placeOrder
	public abstract void msgOutOfFood(String choice, int table, List<String> foo); //* called from CookAgent
	public abstract void msgOrderReady(String choice, int table, List<String> foo); //* called from CookAgent.alertWaiter
	public abstract void msgDoneEating(CherysCustomer c); //* called from CustomerAgent.done
	public abstract void msgHereIsCheck(CherysCashierCheck ch); //*called from CashierAgent
	public abstract void msgLeavingRestaurant(CherysCustomer c); //*called from CustomerAgent
 	public abstract void msgGoOnBreak(boolean tf); //*called from HostAgent
 	
 	public abstract CherysCashierCheck getCheck();
	public abstract EventLog getLog();
}