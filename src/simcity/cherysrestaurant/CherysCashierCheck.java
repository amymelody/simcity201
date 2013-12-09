package simcity.Anjalirestaurant;

import simcity.Anjalirestaurant.AnjaliCashierRole.CheckState;
import simcity.Anjalirestaurant.interfaces.*;

public class AnjaliCashierCheck
{
	public AnjaliWaiter waiter;
	public AnjaliCustomer customer;
	public int table;
	public String order;
	public double total;
	public double amountPaid = 0.0;
	public CheckState state = null;
	
	public AnjaliCashierCheck(AnjaliWaiter w, int table, String o, double t)
	{
		waiter = w;
		this.table = table;
		order = o;
		total = t;
	}
}
