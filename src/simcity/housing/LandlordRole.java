package simcity.housing;

import simcity.interfaces.Resident;
import simcity.housing.gui.HousingGui;
import simcity.housing.gui.LandlordGui;
import simcity.mock.EventLog;
import simcity.mock.LoggedEvent;
import simcity.role.JobRole;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.Semaphore;

public class LandlordRole extends JobRole
{

//Data
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
		sit,
		callRenters,
		collectRent;
	}
	public List<Command> commands = Collections.synchronizedList(new ArrayList<Command>());
	int rent = 50;
	public int moneyEarned = 0;
	Map<String, Point> locations = new HashMap<String, Point>();
	Semaphore atLocation = new Semaphore(0, true);
	public EventLog log = new EventLog();
	public boolean unitTesing = false;
	LandlordGui gui;

	public LandlordRole()
	{
		super();
		locations.put("Fridge", new Point(200, 140));
		locations.put("Stove", new Point(60, 140));
		locations.put("Table", new Point(80, 340));
		locations.put("Sofa", new Point(360, 240));
		locations.put("Doorway", new Point(480, 240));
		locations.put("Exit", new Point(480, 260));
		gui = new LandlordGui(this);
		commands.add(Command.sit);
	}
	
	public void setGui(HousingGui g)
	{
		gui.setGui(g);
	}
	
	public LandlordGui getGui()
	{
		return gui;
	}
	
//Messages
	public void msgStartShift() //from Person
	{
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "received msgStartShift");
		log.add(new LoggedEvent("Received msgStartShift from Person. Command.callRenters"));
		commands.add(Command.callRenters);
		stateChanged();
	}
	public void msgEndShift()
	{
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "WOAH THERE");
		//dummy method unnecessary for landlord 
	}
	public void msgDingDong(Resident r) //from Resident
	{
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "received msgDingDong");
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
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "received msgPayRent. Payment = $" + money);
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
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "received msgAtLocation----------");
		atLocation.release();
	}

//Scheduler
    public boolean pickAndExecuteAnAction()
    {
    	for(Command c : commands)
    	{
    		if(c == Command.sit)
    		{
    	    	goToLocation(locations.get("Sofa"), "");
    			commands.remove(Command.sit);
    			return true;
    		}
    	}
    	for(Command c : commands)
    	{
    		if(c == Command.collectRent)
    		{
    			AlertLog.getInstance().logMessage(AlertTag.BANK, name, "sendAmountOwed----------");
    			sendAmountOwed(c);
    			return true;
    		}
    	}
    	
    	for(Command c : commands)
    	{
    		if(c == Command.callRenters)
    		{
    			AlertLog.getInstance().logMessage(AlertTag.BANK, name, "sendRentDue----------");
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
			AlertLog.getInstance().logMessage(AlertTag.BANK, name, "sendEndShift----------");
    		sendEndShift();
    		return true;
    	}
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "goToSofa----------");
    	goToLocation(locations.get("Sofa"), "");
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
		goToLocation(locations.get("Doorway"), "");
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
		person.msgIncome(moneyEarned);
		moneyEarned = 0;
		person.msgEndShift();
		goToLocation(locations.get("Exit"), "Exit");
		person.msgLeftDestination(this);
	}

	private void goToLocation(Point p, String s)
	{
		if(!unitTesing)
		{
			gui.doGoToLocation(p, s);
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

	public void addRenter(Resident r)
	{
		AlertLog.getInstance().logMessage(AlertTag.BANK, name, "Sofa surfing----------");
		renters.add(new Renter(r));
		(ResidentRole)r.setLandlord(this);
	}

}
