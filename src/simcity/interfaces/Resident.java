package simcity.interfaces;

import simcity.ItemOrder;

import java.util.*;

public interface Resident {

	public abstract void msgRentDue(); //from Landlord
	public abstract void msgAtLandlord(); //from Person
	public abstract void msgAmountOwed(int r); //from Landlord
	public abstract void msgEat(); //from Person
	public abstract void msgGroceries(List<ItemOrder> g); //from Person
	public abstract void msgLeave(); //from Person
	public abstract void msgImHome(); //from Person
}