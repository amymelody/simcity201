package simcity.anjalirestaurant;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import simcity.anjalirestaurant.AnjaliCookRole.Order;
import simcity.anjalirestaurant.AnjaliHostRole.Table;
import simcity.anjalirestaurant.gui.AnjaliCookGui;
import simcity.anjalirestaurant.gui.AnjaliRestaurantGui;
import simcity.anjalirestaurant.gui.AnjaliWaiterGui;
import simcity.anjalirestaurant.interfaces.AnjaliCashier;
import simcity.anjalirestaurant.interfaces.AnjaliCook;
import simcity.anjalirestaurant.interfaces.AnjaliCustomer;
import simcity.anjalirestaurant.interfaces.AnjaliHost;
import simcity.anjalirestaurant.interfaces.AnjaliWaiter;
import simcity.joshrestaurant.JoshCashierRole;
import simcity.joshrestaurant.JoshCookRole;
import simcity.joshrestaurant.JoshHostRole;
import simcity.joshrestaurant.gui.JoshCookGui;
import simcity.joshrestaurant.gui.JoshRestaurantGui;
import simcity.joshrestaurant.gui.JoshWaiterGui;
import simcity.RestWaiterRole;

///Restaurant waiter role base class


public abstract class AnjaliWaiterRole extends RestWaiterRole{

	public abstract void setGui(AnjaliWaiter gui);
	public abstract void setGui(AnjaliCookGui gui);
	public abstract void setRestGui(AnjaliRestaurantGui gui);
	public abstract AnjaliWaiterGui getGui();
	public abstract void setHost(AnjaliHost host);
	public abstract void setCook(AnjaliCook cook);
	public abstract void setCashier(AnjaliCashier cashier);
	public abstract String getName();
	public abstract void msgSitThisCustomer(AnjaliCustomer c, Table t);
	public abstract void msgReadyToOrder(AnjaliCustomer c);
	public abstract void msgHereIsMyChoice(AnjaliCustomer c, String choice);
	public abstract void msgStartShift();
	public abstract void msgEndShift();
	public abstract void msgNoMoreFood(Order o);
	public abstract void msgRemoveItem(String food);
	public abstract void msgOrderIsReady(int tableNumber);
	public abstract void msgReadyForCheck(AnjaliCustomer c);
	public abstract void msgHereIsCheck(int tn, double amount);	
	public abstract void msgLeavingTable(AnjaliCustomer c);
	public abstract void msgWantsBreak();
	public abstract void msgBreakAllowed();
		
}