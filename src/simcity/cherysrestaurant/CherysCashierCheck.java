package simcity.cherysrestaurant;

import simcity.cherysrestaurant.CherysCashierRole.CheckState;
import simcity.cherysrestaurant.interfaces.*;

public class CherysCashierCheck
{
	public CherysWaiter waiter;
	public CherysCustomer customer;
	public int table;
	public String order;
	public int total;
	public int amountPaid = 0;
	public CheckState state = null;
	
	public CherysCashierCheck(CherysWaiter w, int table, String o, int t)
	{
		waiter = w;
		this.table = table;
		order = o;
		total = t;
	}
}
