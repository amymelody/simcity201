package simcity.housing;

import simcity.role.JobRole;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.Semaphore;

public class LandlordRole extends JobRole
{

//Data
	List<Renter> renters;
	class Renter
	{
		ResidentRole resident;
		RenterState state = RenterState.away;
		Renter(ResidentRole r)
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

//Messages
	public void msgStartShift() //from Person
	{
		commands.add(Command.callRenters);
		stateChanged();
	}
	public void msgDingDong(ResidentRole r) //from Resident
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
	public void msgPayRent(ResidentRole r, int money) //from Resident
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
//	+ If (there exists) Command c in commands (such that) c == Command.callRenters,
//	then sendRentDue(c);
//	+ If (there exists) Command c in commands (such that) c == Command.collectRent,
//			then sendAmountOwed(c);
//	+ If !(there exists) Renter r in renters (such that) r.state != RenterState.Paid,
//			then sendEndShift();
//	+ Else goToLocation(locations.get("TV"));
    	return false;
    }

//Actions
	public void sendRentDue(Command c)
	{
		commands.remove(c);
		for(Renter r : renters)
		{
			if(r.state == RenterState.away)
			{
				r.resident.msgRentDue();
				r.state = RenterState.called;
			}
		}
		stateChanged();
	}
	public void sendAmountOwed(Command c)
	{
		commands.remove(c);
		goToLocation(locations.get("Door"));
		for(Renter r : renters)
		{
			if(r.state == RenterState.arrived)
			{
				r.resident.msgAmountOwed(rent);
				r.state = RenterState.askedToPay;
			}
		}
		stateChanged();
	}
	public void sendEndShift()
	{
		for(Renter r : renters)
		{
			r.state = RenterState.away;
		}
		int temp = moneyEarned;
		moneyEarned = 0;
//		person.endShift(temp);
	}

	public void goToLocation(Point p)
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
}
