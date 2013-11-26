package simcity.housing;

import simcity.housing.interfaces.Landlord;
import simcity.housing.test.mock.MockPerson;
import simcity.interfaces.Resident;
import simcity.mock.EventLog;
import simcity.mock.LoggedEvent;
import simcity.role.Role;
import simcity.ItemOrder;
import java.awt.Point;
import java.util.*;
import java.util.concurrent.Semaphore;

public class ResidentRole extends Role implements Resident
{

//Data	
	MockPerson mockPerson;
	Landlord landlord = null;

	public enum ResidentState
	{
		atHome,
		atLandlord,
		away;
	}
	public ResidentState state = ResidentState.away;
	public enum Command
	{
		talkToLandlord,
		payLandlord,
		eat,
		getGroceries,
		putAwayGroceries,
		leave;
	}
	public List<Command> commands = Collections.synchronizedList(new ArrayList<Command>());
	public List<ItemOrder> foodInFridge = Collections.synchronizedList(new ArrayList<ItemOrder>());
	public List<ItemOrder> groceries = Collections.synchronizedList(new ArrayList<ItemOrder>());
	public int rent;
	private Map<String, Point> locations = new HashMap<String, Point>();
	private Random random = new Random();
	public int maintenanceSchedule = 3;
	private Semaphore atLocation = new Semaphore(0, true);
	private Timer timer = new Timer();
	public EventLog log = new EventLog();
	public boolean unitTesting = false;
//	private ResidentGui gui;

	public ResidentRole()
	{
		super();
		foodInFridge.add(new ItemOrder("Salad", 5));
		foodInFridge.add(new ItemOrder("Pizza", 5));
		foodInFridge.add(new ItemOrder("Chicken", 5));
		foodInFridge.add(new ItemOrder("Steak", 5));
	}
	
	public void setLandlord(Landlord l)
	{
		landlord = l;
	}
	public void setMockPerson(MockPerson p)
	{
		mockPerson = p;
	}
	
//Messages
	public void msgRentDue() //from Landlord
	{
		log.add(new LoggedEvent("Received msgRentDue from Landlord. Setting person.rentDue and maintenanceSchedule--"));
		maintenanceSchedule--;
		//mockPerson.rentDue = true;
		person.setRentDue(true);
	}
	public void msgAtLandlord() //from Person
	{
		log.add(new LoggedEvent("Received msgAtLandlord from Person. State.atLandlord, Command.talkToLandlord"));
		state = ResidentState.atLandlord;
		commands.add(Command.talkToLandlord);
		stateChanged();
	}
	public void msgAmountOwed(int r) //from Landlord
	{
		log.add(new LoggedEvent("Received msgAmountOwed from Landlord. Rent = $" + r + ", Command.payLandlord"));
		commands.add(Command.payLandlord);
		rent = r;
		stateChanged();
	}
	public void msgEat() //from Person
	{
		log.add(new LoggedEvent("Received msgEat from Person. Command.eat"));
		commands.add(Command.eat);
		stateChanged();
	}
	public void msgGroceries(List<ItemOrder> g) //from Person
	{
		log.add(new LoggedEvent("Received msgGroceries from Person. Number of grocery items = " + g.size() + ", Command.putAwayGroceries"));
		commands.add(Command.putAwayGroceries);
		groceries = g;
		stateChanged();
	}
	public void msgLeave() //from Person
	{
		log.add(new LoggedEvent("Received msgLeave from Person. Command.leave"));
		commands.add(Command.leave);
		stateChanged();
	}
	public void msgImHome() //from Person
	{
		log.add(new LoggedEvent("Received msgImHome from Person. State.atHome"));
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
    				prepareFood(c);
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
		landlord.msgDingDong(this);
		stateChanged();
	}
	private void sendPayRent(Command c)
	{
		commands.remove(c);
		landlord.msgPayRent(this, rent);
		//mockPerson.msgExpense(rent);
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
	private void prepareFood(Command c)
	{
		commands.remove(c);
		goToLocation(locations.get("Fridge"));
		ItemOrder food = foodInFridge.get(random.nextInt(foodInFridge.size()));
		final List<ItemOrder> groceryList = new ArrayList<ItemOrder>();
		for(ItemOrder i : foodInFridge)
		{
			if(i == food)
			{
				i.setAmount(i.getAmount() - 1);
				if(i.getAmount() == 0)
				{
					for(ItemOrder j : foodInFridge)
					{
						if(j.getAmount() <= 2)
						{
							ItemOrder temp = new ItemOrder(j.getFoodItem(), 5 - j.getAmount());
							groceryList.add(temp);
						}
					}
				}
			}
		}
		goToLocation(locations.get("Stove"));
		if(unitTesting)
		{
			eat(groceryList);
		}
		else
		{
			timer.schedule(new TimerTask() 
				{
					public void run()
					{
						eat(groceryList);
					}
				}, 10000);
		}
	}
	public void eat(final List<ItemOrder> gList)
	{
		goToLocation(locations.get("Table"));
		if(unitTesting)
		{
			groceryCheck(gList);
		}
		else
		{
			timer.schedule(new TimerTask() 
				{
					public void run()
					{
						groceryCheck(gList);
					}
				}, 5000);
		}
	}
	public void groceryCheck(List<ItemOrder> gList)
	{
		//mockPerson.msgDoneEating();
		person.msgDoneEating();
		if(gList.size() > 0)
		{
			//mockPerson.msgFoodLow(gList);
			person.msgFoodLow(gList);
		}
		stateChanged();
	}
	private void clean()
	{
		goToLocation(locations.get("Fridge"));
		Do("Cleaning Fridge");
		if(unitTesting)
		{
			goToLocation(locations.get("Stove"));
		}
		else
		{
			timer.schedule(new TimerTask() 
				{
					public void run()
					{
						goToLocation(locations.get("Stove"));
					}
				}, 1000);
		}
		Do("Cleaning Stove");
		if(unitTesting)
		{
			goToLocation(locations.get("Table"));
		}
		else
		{
			timer.schedule(new TimerTask() 
				{
					public void run()
					{
						goToLocation(locations.get("Table"));
					}
				}, 1000);
		}
		Do("Cleaning Table");
		if(unitTesting)
		{
			goToLocation(locations.get("Sofa"));
		}
		else
		{
			timer.schedule(new TimerTask() 
				{
					public void run()
					{
						goToLocation(locations.get("Sofa"));
					}
				}, 1000);
		}
		Do("Cleaning Sofa");
		if(unitTesting)
		{
			goToLocation(locations.get("Doorway"));
		}
		else
		{
			timer.schedule(new TimerTask() 
				{
					public void run()
					{
						goToLocation(locations.get("Doorway"));
					}
				}, 1000);
		}
		Do("Cleaning Door");
		maintenanceSchedule = 3;
		stateChanged();
	}
	private void leaveHousing(Command c)
	{
		commands.remove(c);
		goToLocation(locations.get("Outside"));
		state = ResidentState.away;
		//mockPerson.msgLeftDestination(this);
		person.msgLeftDestination(this);
		stateChanged();
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
}
