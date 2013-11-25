package simcity.joshrestaurant;

import simcity.RestWaiterRole;
import simcity.joshrestaurant.gui.JoshCookGui;
import simcity.joshrestaurant.gui.JoshWaiterGui;
import simcity.joshrestaurant.interfaces.JoshCustomer;

/**
 * Restaurant Waiter Role
 */
public abstract class JoshWaiterRole extends RestWaiterRole {
	public abstract void setGui(JoshWaiterGui gui);
	public abstract void setGui(JoshCookGui gui);
	public abstract JoshWaiterGui getGui();
	public abstract void setHost(JoshHostRole host);
	public abstract void setCook(JoshCookRole cook);
	public abstract void setCashier(JoshCashierRole cashier);
	public abstract String getName();
	public abstract boolean isOnBreak();
	public abstract boolean isAboutToGoOnBreak();
	public abstract void msgStartShift();
	public abstract void msgEndShift();
	public abstract void msgWantToGoOnBreak();
	public abstract void msgCanGoOnBreak();
	public abstract void msgCantGoOnBreak();
	public abstract void msgGoOffBreak();
	public abstract void msgPleaseSeatCustomer(JoshCustomerRole cust, int tableNumber);
	public abstract void msgIWantToLeave(JoshCustomerRole cust);
	public abstract void msgReadyToOrder(JoshCustomerRole cust);
	public abstract void msgHereIsChoice(JoshCustomerRole cust, String choice);
	public abstract void msgOutOfFood(String choice, int table);
	public abstract void msgOrderDone(String choice, int tableNum);
	public abstract void msgDoneEating(JoshCustomerRole cust);
	public abstract void msgFoodArrived(String food);
	public abstract void msgHereIsCheck(JoshCustomer c, int charge);
	public abstract void msgAtHome();
	public abstract void msgAtCustomer();
	public abstract void msgAtTable();
	public abstract void msgAtCook();
	public abstract void msgLeftRestaurant();
}

