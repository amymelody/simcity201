package simcity.housing;

import simcity.housing.interfaces.Resident;
import simcity.housing.test.mock.MockPerson;
import simcity.mock.EventLog;
import simcity.mock.LoggedEvent;
import simcity.role.JobRole;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.Semaphore;

public class LandlordRole extends JobRole
{

//Data
	MockPerson mockPerson;
	public boolean unitTesting = false;
	public List<Renter> renters = Collections.synchronizedList(new ArrayList<Renter>());
	public class Renter
	{
		Resident resident;
		public RenterState state = RenterState.away;
		Renter(Resident r)
		{
			resident = r;
		}
	}
	public enum RenterState
	{
		called,
		arrived,
		askedToPay,
		paid,
		away;
	}
	public enum Command
	{
		callRenters,
		collectRent;
	}
	public List<Command> commands = Collections.synchronizedList(new ArrayList<Command>());
	int rent = 50;
	public int moneyEarned = 0;
	Map<String, Point> locations = new HashMap<String, Point>();
	Semaphore atLocation = new Semaphore(0, true);
	public EventLog log = new EventLog();
//	ResidentGui gui;

	public LandlordRole()
	{
		super();
	}
	
	public void setMockPerson(MockPerson p)
	{
		mockPerson = p;
	}
	
//Messages
	public void msgStartShift() //from Person
	{
		log.add(new LoggedEvent("Received msgStartShift from Person. Command.callRenters"));
		commands.add(Command.callRenters);
		stateChanged();
	}
	public void msgEndShift()
	{
		//dummy method unnecessary for landlord 
	}
	public void msgDingDong(Resident r) //from Resident
	{
		log.add(new LoggedEvent("Received msgDingDong from Resident. State.arrived, Command.collectRent"));
		commands.add(Command.collectRent);
		for(Renter renter : renters)
		{
			if(renter.resident == r)
			{
				renter.state = RenterState.arrived;
			}
		}
		stateChanged();
	}
	public void msgPayRent(Resident r, int money) //from Resident
	{
		log.add(new LoggedEvent("Received msgPayRent from Resident. State.paid. Payment = $" + money));
		moneyEarned += money;
		for(Renter renter : renters)
		{
		
			if(renter.resident == r)
			{
				renter.state = RenterState.paid;
			}
		}
		stateChanged();
	}

	public void msgAtLocation()
	{
		atLocation.release();
	}

//Scheduler
    public boolean pickAndExecuteAnAction()
    {
    	for(Command c : commands)
    	{
    		if(c == Command.collectRent)
    		{
    			sendAmountOwed(c);
    			return true;
    		}
    	}
    	for(Command c : commands)
    	{
    		if(c == Command.callRenters)
    		{
    			sendRentDue(c);
    			return true;
    		}
    	}
    	boolean allRentCollected = true;
    	for(Renter r : renters)
    	{
    		if(r.state != RenterState.paid)
    		{
    			allRentCollected = false;
    		}
    	}
    	if(allRentCollected)
    	{
    		sendEndShift();
    		return true;
    	}
    	goToLocation(locations.get("Sofa"));
    	return false;
    }

//Actions
	private void sendRentDue(Command c)
	{
		commands.remove(c);
		for(Renter r : renters)
		{
			if(r.state == RenterState.away)
			{
				r.state = RenterState.called;
				r.resident.msgRentDue();
			}
		}
		stateChanged();
	}
	private void sendAmountOwed(Command c)
	{
		commands.remove(c);
		goToLocation(locations.get("Doorway"));
		for(Renter r : renters)
		{
			if(r.state == RenterState.arrived)
			{
				r.state = RenterState.askedToPay;
				r.resident.msgAmountOwed(rent);
			}
		}
		stateChanged();
	}
	private void sendEndShift()
	{
		for(Renter r : renters)
		{
			r.state = RenterState.away;
		}
		mockPerson.msgIncome(moneyEarned);
		person.msgIncome(moneyEarned);
		moneyEarned = 0;
		mockPerson.msgEndShift();
		if(!unitTesting)
		{
			person.msgEndShift();
		}
	}

	private void goToLocation(Point p)
	{
//		gui.doGoToLocation(p);
//		try
//		{
//			atLocation.acquire();
//		}
//		catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}
	}

	public void addRenter(Resident r)
	{
		renters.add(new Renter(r));
	}

}
