package simcity.housing.interfaces;

import java.util.List;

import simcity.ItemOrder;
import simcity.mock.EventLog;

public interface Resident
{
	public EventLog log = new EventLog();

	public abstract void msgRentDue(); //from Landlord
	public abstract void msgAtLandlord(); //from Person
	public abstract void msgAmountOwed(int r); //from Landlord
	public abstract void msgEat(); //from Person
	public abstract void msgGroceries(List<ItemOrder> g); //from Person
	public abstract void msgLeave(); //from Person
	public abstract void msgImHome(); //from Person
}