package simcity.housing.test.mock;

import java.util.List;

import simcity.ItemOrder;
import simcity.interfaces.Landlord;
import simcity.interfaces.Resident;
import simcity.mock.Mock;
import simcity.mock.LoggedEvent;

public class MockResident extends Mock implements Resident
{
	public int rent;

	public MockResident(String name)
	{
		super(name);
	}

	@Override
	public void msgRentDue() //from Landlord
	{
		log.add(new LoggedEvent("Received msgRentDue from Landlord. Setting person.rentDue"));
	}
	@Override
	public void msgAtLandlord() //from Person
	{
		log.add(new LoggedEvent("Received msgAtLandlord from Person. State.atLandlord, Command.talkToLandlord"));
	}
	@Override
	public void msgAmountOwed(int r) //from Landlord
	{
		log.add(new LoggedEvent("Received msgAmountOwed from Landlord. Rent = $" + r + ", Command.payLandlord"));
		rent = r;
	}
	@Override
	public void msgEat() //from Person
	{
		log.add(new LoggedEvent("Received msgEat from Person. Command.eat and maintenanceSchedule--"));
	}
	@Override
	public void msgGroceries(List<ItemOrder> g) //from Person
	{
		log.add(new LoggedEvent("Received msgGroceries from Person. Number of grocery items = " + g.size() + ", Command.putAwayGroceries"));
	}
	@Override
	public void msgLeave() //from Person
	{
		log.add(new LoggedEvent("Received msgLeave from Person. Command.leave"));
	}
	@Override
	public void msgImHome() //from Person
	{
		log.add(new LoggedEvent("Received msgImHome from Person. State.atHome"));
	}

	@Override
	public void msgAtLocation()
	{
		log.add(new LoggedEvent("Received msgAtLocation"));
	}

	@Override
	public void setLandlord(Landlord l) {
		// TODO Auto-generated method stub
		
	}
}
