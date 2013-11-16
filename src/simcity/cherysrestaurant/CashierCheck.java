package simcity.cherysrestaurant;

import simcity.cherysrestaurant.CashierAgent.CheckState;
import simcity.cherysrestaurant.interfaces.*;

public class CashierCheck
{
	public Waiter waiter;
	public Customer customer;
	public int table;
	public String order;
	public double total;
	public double amountPaid = 0.0;
	public CheckState state = null;
	
	public CashierCheck(Waiter w, int table, String o, double t)
	{
		waiter = w;
		this.table = table;
		order = o;
		total = t;
	}
}
