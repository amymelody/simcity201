package simcity.housing;

import simcity.housing.interfaces.Resident;
import simcity.role.JobRole;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.Semaphore;

public class LandlordRole extends JobRole
{

//Data
	String name = "Landlord";
	List<Renter> renters;
	class Renter
	{
		Resident resident;
		RenterState state = RenterState.away;
		Renter(Resident r)
		{
			resident = r;
		}
	}
	enum RenterState
	{
		called,
		arrived,
		askedToPay,
		paid,
		away;
	}
	enum Command
	{
		callRenters,
		collectRent;
	}
	List<Command> commands;
	int rent = 50;
	int moneyEarned = 0;
	Map<String, Point> locations;
	Semaphore atLocation = new Semaphore(0, true);
//	ResidentGui gui;

	public LandlordRole()
	{
		super();
	}
	
//Messages
	public void msgStartShift() //from Person
	{
		commands.add(Command.callRenters);
		stateChanged();
	}
	public void msgEndShift()
	{
		//dummy method unnecessary for landlord 
	}
	public void msgDingDong(Resident r) //from Resident
	{
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
		int temp = moneyEarned;
		moneyEarned = 0;
//		person.endShift(temp);
	}

	private void goToLocation(Point p)
	{
//		gui.doGoToLocation(p);
		try
		{
			atLocation.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void addRenter(Resident r)
	{
		renters.add(new Renter(r));
	}

}
