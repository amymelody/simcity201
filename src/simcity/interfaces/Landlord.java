package simcity.interfaces;

import simcity.interfaces.Resident;

public interface Landlord
{
	public abstract void msgStartShift(); //from Person
	public abstract void msgDingDong(Resident r); //from Resident
	public abstract void msgPayRent(Resident r, int money); //from Resident
}
