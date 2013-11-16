package simcity.cherysrestaurant.interfaces;

import simcity.cherysrestaurant.CashierCheck;
import simcity.mock.EventLog;

import java.util.List;

public interface Waiter
{
	public abstract void msgPleaseSeatCustomer(Customer c, int table); //* called from HostAgent.assignCustomer
	public abstract void msgReadyToOrder(Customer c); //* called from CustomerAgent.flagWaiter
	public abstract void msgHereIsMyOrder(Customer c, String choice); //* called from CustomerAgent.placeOrder
	public abstract void msgOutOfFood(String choice, int table, List<String> foo); //* called from CookAgent
	public abstract void msgOrderReady(String choice, int table, List<String> foo); //* called from CookAgent.alertWaiter
	public abstract void msgDoneEating(Customer c); //* called from CustomerAgent.done
	public abstract void msgHereIsCheck(CashierCheck ch); //*called from CashierAgent
	public abstract void msgLeavingRestaurant(Customer c); //*called from CustomerAgent
 	public abstract void msgGoOnBreak(boolean tf); //*called from HostAgent
 	
 	public abstract CashierCheck getCheck();
	public abstract EventLog getLog();
}