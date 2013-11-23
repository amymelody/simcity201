package simcity.housing;

import simcity.housing.interfaces.Landlord;
import simcity.interfaces.Resident;
import simcity.role.Role;
import simcity.ItemOrder;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.Semaphore;

public class ResidentRole extends Role implements Resident
{

//Data
	String name = "Resident";
	
	Landlord landlord = null;

	private enum ResidentState
	{
		atHome,
		atLandlord,
		away;
	}
	private ResidentState state = ResidentState.away;
	private enum Command
	{
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

	public ResidentRole()
	{
		super();
	}
	
	public void setLandlord(Landlord l)
	{
		landlord = l;
	}
	
//Messages
	public void msgRentDue() //from Landlord
	{
		person.setRentDue(true);
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
    	if(state == ResidentState.atHome)
	    {
    		for(Command c : commands)
    		{
    			if(c == Command.putAwayGroceries)
    			{
    				putGroceriesInFridge(c);
        	    	return true;
    			}
    		}
    		for(Command c : commands)
    		{
    			if(c == Command.eat)
    			{
    				eat(c);
        	    	return true;
    			}
    		}
    		for(Command c : commands)
    		{
    			if(c == Command.leave)
    			{
    				leaveHousing(c);
        	    	return true;
    			}
    		}
    		if(maintenanceSchedule <= 0)
    		{
    			clean();
    	    	return true;
    		}
    		goToLocation(locations.get("Sofa"));
        	return false;
		}
    	if(state == ResidentState.atLandlord)
		{
    		for(Command c : commands)
    		{
    			if(c == Command.talkToLandlord)
    			{
    				sendDingDong(c);
        	    	return true;
    			}
    		}
    		for(Command c : commands)
    		{
    			if(c == Command.payLandlord)
    			{
    				sendPayRent(c);
        	    	return true;
    			}
    		}
    		for(Command c : commands)
    		{
    			if(c == Command.leave)
    			{
    				leaveHousing(c);
        	    	return true;
    			}
    		}
		}
    	return false;
    }

//Actions
	private void sendDingDong(Command c)
	{
		commands.remove(c);
		landlord.msgDingDong((simcity.housing.interfaces.Resident)this);
		stateChanged();
	}
	private void sendPayRent(Command c)
	{
		commands.remove(c);
		landlord.msgPayRent((simcity.housing.interfaces.Resident) this, rent);
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
				if(food.getFoodItem().equals(grocery.getFoodItem()))
				{
					food.setAmount(food.getAmount() + grocery.getAmount());
					break;
				}
			}
		}
		groceries.clear();
		stateChanged();
	}
	private void eat(Command c)
	{
		commands.remove(c);
		goToLocation(locations.get("Fridge"));
		String food = foodInFridge.get(random.nextInt(4)).getFoodItem(); //assuming that the max number of types of food is 4
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
				goToLocation(locations.get("Sofa"));
			}
		}, 1000);
		Do("Cleaning Sofa");
		timer.schedule(new TimerTask() 
		{
			public void run()
			{
				goToLocation(locations.get("Doorway"));
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
		person.msgLeftDestination(this);
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
