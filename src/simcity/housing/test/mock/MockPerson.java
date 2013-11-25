package simcity.housing.test.mock;

import simcity.ItemOrder;
import simcity.housing.ResidentRole;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;
import java.util.*;

public class MockPerson extends Mock
{
	public List<ItemOrder> groceries;
	public boolean rentDue = false;
	
	public MockPerson(String name)
	{
		super(name);
	}
	
	public void msgExpense(int r) //from Resident
	{
		log.add(new LoggedEvent("Received msgExpense from Resident. Rent = $" + r));
	}
	public void msgDoneEating() //from Resident
	{
		log.add(new LoggedEvent("Received msgDoneEating from Resident"));
	}
	public void msgFoodLow(List<ItemOrder> glist) //from Resident
	{
		log.add(new LoggedEvent("Received msgFoodLow from Resident. " + glist.size() + " groceries needed"));
		groceries = glist;
	}
	public void msgLeftDestination(ResidentRole r) //from Resident
	{
		log.add(new LoggedEvent("Received msgLeftDestination from Resident"));
	}
	
	public void msgIncome(int money) //from Landlord
	{
		log.add(new LoggedEvent("Received msgIncome from Landlord. Money = $" + money));
	}
	public void msgEndShift()
	{
		log.add(new LoggedEvent("Received msgEndShift from Landlord"));
	}
}
