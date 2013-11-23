package simcity.housing.test.mock;

import simcity.housing.interfaces.Landlord;
import simcity.housing.interfaces.Resident;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockLandlord extends Mock implements Landlord
{

	public MockLandlord(String name)
	{
		super(name);
	}
	
	@Override
	public void msgStartShift() //from Person
	{
		log.add(new LoggedEvent("Received msgStartShift from Person. Command.callRenters"));
	}
	@Override
	public void msgDingDong(Resident r) //from Resident
	{
		log.add(new LoggedEvent("Received msgDingDong from Resident. State.arrived, Command.collectRent"));
	}
	@Override
	public void msgPayRent(Resident r, int money) //from Resident
	{
		log.add(new LoggedEvent("Received msgPayRent from Resident. State.paid. Payment = $" + money));
	}
}
