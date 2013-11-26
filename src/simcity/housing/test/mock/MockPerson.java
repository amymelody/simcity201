package simcity.housing.test.mock;

import simcity.Day;
import simcity.ItemOrder;
import simcity.PersonAgent.LocationState;
import simcity.PersonAgent.PhysicalState;
import simcity.Time;
import simcity.housing.ResidentRole;
import simcity.interfaces.Bus;
import simcity.interfaces.Car;
import simcity.interfaces.Person;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;
import simcity.role.Role;

import java.util.*;

public class MockPerson extends Mock implements Person
{
	public List<ItemOrder> groceries;
	public boolean rentDue = false;
	
	public MockPerson(String name)
	{
		super(name);
	}

	@Override
	public void msgExpense(int r) //from Resident
	{
		log.add(new LoggedEvent("Received msgExpense from Resident. Rent = $" + r));
	}
	@Override
	public void msgDoneEating() //from Resident
	{
		log.add(new LoggedEvent("Received msgDoneEating from Resident"));
	}
	@Override
	public void msgFoodLow(List<ItemOrder> glist) //from Resident
	{
		log.add(new LoggedEvent("Received msgFoodLow from Resident. " + glist.size() + " groceries needed"));
		groceries = glist;
	}
	@Override
	public void msgLeftDestination(Role r) //from Resident or Landlord
	{
		log.add(new LoggedEvent("Received msgLeftDestination"));
	}

	@Override
	public void msgIncome(int money) //from Landlord
	{
		log.add(new LoggedEvent("Received msgIncome from Landlord. Money = $" + money));
	}
	@Override
	public void msgEndShift()
	{
		log.add(new LoggedEvent("Received msgEndShift from Landlord"));
	}
	@Override
	public void setRentDue(boolean tf)
	{
		rentDue = tf;
	}

	@Override
	public void msgAtDestination()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgUpdateWatch(Day d, int h, int m)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImHungry()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYoureHired(String role, int payrate,
			Map<Day, Time> startShifts, Map<Day, Time> endShifts)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYoureHired(String jobLocation, String role, int payrate,
			Map<Day, Time> startShifts, Map<Day, Time> endShifts)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReceivedItems(List<ItemOrder> items)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBoughtCar(Car c)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCreatedAccount()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBusIsHere(Bus b)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDestination(String d)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPState(PhysicalState ps)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLState(LocationState ls)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEState(String es)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPState(String ps)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRole(Role r, String n)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getMoney()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getJob()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDestination()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stateChanged()
	{
		// TODO Auto-generated method stub
		
	}
}
