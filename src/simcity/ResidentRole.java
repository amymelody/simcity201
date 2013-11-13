package simcity;

import role.Role;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.Semaphore;

public class ResidentRole extends Role
{

//Data
	LandlordRole landlord;

	private enum ResidentState
	{
		atHome,
		atLandlord,
		away;
	}
	private ResidentState state = ResidentState.away;
	private enum Command
	{
		rentDue,
		talkToLandlord,
		payLandlord,
		eat,
		getGroceries,
		putAwayGroceries,
		leave;
	}
	private List<Command> commands;
	private List<ItemOrder> foodInFridge;
	private List<ItemOrder> groceries;
	private int rent;
	private Map<String, Point> locations;
	private Random random = new Random();
	private int maintenanceSchedule = 3;
	private Semaphore atLocation = new Semaphore(0, true);
	private Timer timer = new Timer();
//	private ResidentGui gui;

//Messages
	public void msgRentDue() //from Landlord
	{
		commands.add(Command.rentDue);
		maintenanceSchedule--;
		stateChanged();
	}
	public void msgAtLandlord() //from Person
	{
		state = ResidentState.atLandlord;
		commands.add(Command.talkToLandlord);
		stateChanged();
	}
	public void msgAmountOwed(int r) //from Landlord
	{
		commands.add(Command.payLandlord);
		rent = r;
		stateChanged();
	}
	public void msgEat() //from Person
	{
		commands.add(Command.eat);
		stateChanged();
	}
	public void msgGroceries(List<ItemOrder> g) //from Person
	{
		commands.add(Command.putAwayGroceries);
		groceries = g;
		stateChanged();
	}
	public void msgLeave() //from Person
	{
		commands.add(Command.leave);
		stateChanged();
	}
	public void msgImHome() //from Person
	{
		state = ResidentState.atHome;
		stateChanged();
	}

	public void msgAtLocation() //from GUI
	{
		atLocation.release();
	}

//Scheduler
    public boolean pickAndExecuteAnAction()
    {
//	  + If state == ResidentState.atHome, then
//	    {
//	      + If (there exists) Command c in commands (such that) c == Command.rentDue,
//		    then sendRentDue(c);
//	      + If (there exists) Command c in commands (such that) c == Command.putAwayGroceries,
//		    then putGroceriesInFridge(c);
//	      + If (there exists) Command c in commands (such that) c == Command.eat,
//		    then eat(c);
//	      + If (there exists) Command c in commands (such that) c == Command.leave,
//		    then leaveHousing(c);
//		  + If maintenanceSchedule =< 0,
//		    then clean();
//		  + Else goToLocation("TV");
//		}
//	  + If state == ResidentState.atLandlord, then
//		{
//	      + If (there exists) Command c in commands (such that) c == Command.talkToLandlord,
//		    then sendDingDong(c);
//	      + If (there exists) Command c in commands (such that) c == Command.payLandlord,
//		    then sendPayRent(c);
//	      + If (there exists) Command c in commands (such that) c == Command.leave,
//		    then leaveHousing(c);
//		}
//	  + If state == ResidentState.away, then
//		{
//	      + If (there exists) Command c in commands (such that) c == Command.rentDue,
//		    then sendRentDue();
//		}
    	return false;
    }

//Actions
	private void sendRentDue(Command c)
	{
		commands.remove(c);
		person.msgRentDue();
		stateChanged();
	}
	private void sendDingDong(Command c)
	{
		commands.remove(c);
		landlord.msgDingDong(this);
		stateChanged();
	}
	private void sendPayRent(Command c)
	{
		commands.remove(c);
		landlord.msgPayRent(this, rent);
		person.msgExpense(rent);
		rent = -1;
		stateChanged();
	}
	private void putGroceriesInFridge(Command c)
	{
		commands.remove(c);
		goToLocation(locations.get("Fridge"));
		for(ItemOrder food : foodInFridge)
		{
			for(ItemOrder grocery : groceries)	
			{
				if(food.foodItem.equals(grocery.foodItem))
				{
					food.amount += grocery.amount;
					break;
				}
			}
		}
		foodInFridge.add(groceries); //*
		groceries.clear();
		stateChanged();
	}
	private void eat(Command c)
	{
		commands.remove(c);
		goToLocation(locations.get("Fridge"));
		ItemOrder food = foodInFridge.get(random.nextInt(4)).foodItem; //assuming that the max number of types of food is 4
		foodInFridge.remove(food); //*
		goToLocation(locations.get("Stove"));
		timer.schedule(new TimerTask() 
		{
			public void run()
			{
				goToLocation(locations.get("Table"));
			}
		}, 2000);
		timer.schedule(new TimerTask() 
		{
			public void run()
			{
				person.msgDoneEating();
			}
		}, 10000);
		stateChanged();
	}
	private void clean()
	{
		goToLocation(locations.get("Fridge"));
		Do("Cleaning Fridge");
		timer.schedule(new TimerTask() 
		{
			public void run()
			{
				goToLocation(locations.get("Stove"));
			}
		}, 1000);
		Do("Cleaning Stove");
		timer.schedule(new TimerTask() 
		{
			public void run()
			{
				goToLocation(locations.get("Table"));
			}
		}, 1000);
		Do("Cleaning Table");
		timer.schedule(new TimerTask() 
		{
			public void run()
			{
				goToLocation(locations.get("TV"));
			}
		}, 1000);
		Do("Cleaning Sofa");
		timer.schedule(new TimerTask() 
		{
			public void run()
			{
				goToLocation(locations.get("Door"));
			}
		}, 1000);
		Do("Cleaning Door");
		maintenanceSchedule = 3;
		stateChanged();
	}
	private void leaveHousing(Command c)
	{
		commands.remove(c);
		goToLocation(locations.get("Outside"));
		state = ResidentState.away;
		person.msgLeftDestination();
		stateChanged();
	}

	private void goToLocation(Point p)
	{
		//gui.doGoToLocation(p);
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
