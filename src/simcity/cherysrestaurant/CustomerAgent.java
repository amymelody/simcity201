package simcity.cherysrestaurant;

import agent.Agent;
import simcity.cherysrestaurant.gui.CustomerGui;
import simcity.cherysrestaurant.gui.RestaurantGui;
import simcity.cherysrestaurant.interfaces.*;
import simcity.cherysrestaurant.test.mock.EventLog;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Customer Agent.
 */
public class CustomerAgent extends Agent implements Customer
{
	private Semaphore atTable = new Semaphore(0, true);
	private Semaphore atCashier = new Semaphore(0, true);
	private Semaphore leftRestaurant = new Semaphore(0, true);
	
	private String name;
	private Random random = new Random();
	private int hungerLevel = 0;
	private int maxHunger = 5000;
	Timer timer = new Timer();
	
	private CustomerGui customerGui = null;

	private Host host;

	private enum AgentState
	{
		entering,
		atTable,
		exiting;
	}
	private AgentState state;
	private enum AgentEvent
	{
		arrived,
		beingSeated,
		satDown,
		askedForOrder,
		foodRecieved,
		gotCheck,
		readyToPay,
		transactionComplete,
		done;
	}
	private List<AgentEvent> events = new ArrayList<AgentEvent>();
	
	Waiter waiter;
	String choice = null;
	int orderTime = 2000;
	Map<Integer, WaiterFood> menu;
	List<String> foodsOutOf = new ArrayList<String>();
	
	private Cashier cashier;
	private double money = 0.0;
	private CashierCheck check;
	
	public EventLog log = new EventLog();
	
	/**
	 * Constructor for CustomerAgent class
	 * @param name name of the customer
	 */
	public CustomerAgent(String name) //* called in RestaurantPanel
	{
		super();
		this.name = name;
		stateChanged();
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(Host host) //* called in RestaurantPanel.addPerson
	{
		this.host = host;
	}

	public String getCustomerName()
	{
		return name;
	}
	
	// Messages
	public void gotHungry() //* called from animation
	{
		Do("I'm hungry");
		money += (double)random.nextInt(15) + 5.0;
		money = Math.round(money * 100.0) / 100.0;
		events.add(AgentEvent.arrived);
		state = null;
		hungerLevel = maxHunger;
		stateChanged();
	}
	public void msgFollowMe(Waiter w, Map<Integer, WaiterFood> m, List<String> foo)//int seat) //* called from Waiter
	{
		Do("recieved msgFollowMe");
		waiter = w;
		menu = m;
		foodsOutOf = foo;
		events.add(AgentEvent.beingSeated);
		stateChanged();
	}
	public void msgWhatIsYourOrder(List<String> foo) //* called from Waiter
	{
		Do("recieved msgWhatIsYourOrder");
		foodsOutOf = foo;
		events.add(AgentEvent.askedForOrder);
		stateChanged();
	}
	public void msgOrderServed(String ch) //* called from Waiter
	{
		Do("recieved msgOrderServed");
		doGetFood(ch);
		events.add(AgentEvent.foodRecieved);
		stateChanged();
	}
	public void msgHereIsCheck(CashierCheck ch)
	{
		Do("recieved msgHereIsCheck");
		check = ch;
		events.add(AgentEvent.gotCheck);
		stateChanged();
	}
	public void msgChange(double change)
	{
		Do("recieved msgChange");
		money += change;
		money = Math.round(money * 100.0) / 100.0;
		Do("I now have $" + money);
		events.add(AgentEvent.transactionComplete);
		stateChanged();
	}
	
	public void msgAtTable() //* called from animation
	{
		Do("AT TABLE RELEASE");
		events.add(AgentEvent.satDown);
		atTable.release();
	}
	public void msgAtCashier() //* called from animation
	{
		Do("AT CASHIER RELEASE");
		events.add(AgentEvent.readyToPay);
		atCashier.release();
	}
	public void msgLeftRestaurant() //* called from animation
	{
		Do("LEFT RESTAURANT RELEASE");
		leftRestaurant.release();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction()
	{
		if(events.size() > 0)
		{
			if(state == null)
			{
				if(events.get(0) == AgentEvent.arrived && hungerLevel > 0)
				{
					events.remove(0);
					state = AgentState.entering;
					alertHost();
					return true;
				}
			}
			else if(state == AgentState.entering)
			{
				if(events.get(0) == AgentEvent.beingSeated)
				{
					events.remove(0);
					state = AgentState.atTable;
					followWaiter();
					return true;
				}
			}
			else if(state == AgentState.atTable)
			{
				if(events.get(0) == AgentEvent.satDown)
				{
					events.remove(0);
					chooseOrder();
					return true;
				}
				if(events.get(0) == AgentEvent.askedForOrder)
				{
					events.remove(0);
					placeOrder();
					return true;
				}
				if(events.get(0) == AgentEvent.foodRecieved)
				{
					events.remove(0);
					eatFood();
					return true;
				}
				if(events.get(0) == AgentEvent.gotCheck)
				{
					events.remove(0);
					state = AgentState.exiting;
					doGoToCashier();
					return true;
				}
			}
			else if(state == AgentState.exiting)
			{
				if(events.get(0) == AgentEvent.readyToPay)
				{
					events.remove(0);
					payCashier();
					return true;
				}
				if(events.get(0) == AgentEvent.transactionComplete)
				{
					events.remove(0);
					done();
					return true;
				}
			}
		}
		return false;
	}

	// Actions
	/**
	 * Customer is being seated
	 */
	private void followWaiter()
	{
		Do("Being seated. Going to table");
		doGoToTable();
	}
	/**
	 * Customer takes time to choose their order, then flags down the waiter
	 */
	private void chooseOrder()
	{
		timer.schedule(new TimerTask() 
			{
				public void run()
				{
					flagWaiter();
				}
			}, orderTime);
	}
	/**
	 * Determines choice of order and messages waiter
	 */
	private void flagWaiter()
	{
		Do("Oh waiter!");
		waiter.msgReadyToOrder(this);
	}
	/**
	 * Gives the waiter the order once the waiter asks
	 */
	private void placeOrder()
	{
		Map<Integer, WaiterFood> choices = new HashMap<Integer, WaiterFood>();;
		int index = 0;
		do
		{
			try
			{
				for(int i = 0; i < menu.size(); i++)
				{
					boolean availible = true;
					do
					{
						try
						{
							for(String outOf : foodsOutOf)
							{
								if(menu.get(i).name == outOf)
								{
									availible = false;
									break;
								}
							}
						}
						catch(ConcurrentModificationException cme)
						{
							continue;
						}
					}
					while(false);
					if(availible)
					{
						if(menu.get(i).price <= money)
						{
							choices.put(index, menu.get(i));
							index++;
						}
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		if(choices.size() > 0)
		{
			choice = choices.get(random.nextInt(choices.size())).name;
			Do("I have $" + money + " and would like to order a " + choice);
			doWaitForFood(choice);
			waiter.msgHereIsMyOrder(this, choice);
			stateChanged();
		}
		else
		{
			Do("*angry muttering*");
			waiter.msgLeavingRestaurant(this);
			done();
		}
	}
	/**
	 * Customer takes time to eat their food
	 */
	private void eatFood()
	{
		Do("Om nom nom");
		timer.schedule(new TimerTask() 
		{
			public void run()
			{
				finishedEating();
			}
		}, hungerLevel);
	}
	/**
	 * Has finished eating
	 */
	private void finishedEating()
	{
		Do("I'm done eating");
		waiter.msgDoneEating(this);
	}
	private void payCashier()
	{
		double amountPaying = (double)Math.ceil(check.total);
		amountPaying = Math.round(amountPaying * 100.0) / 100.0;
		if(money >= amountPaying)
		{
			Do("Here is my bill");
			money -= amountPaying;
			money = Math.round(money * 100.0) / 100.0;
			cashier.msgPayment(this, check, amountPaying);
			check = null;
		}
	}
	private void done()
	{
		Do("I'm leaving");
		hungerLevel = 0;
		events.clear();
		doExitRestaurant();
	}
	/**
	 * Customer tells the host that they want a table
	 */
	private void alertHost()
	{
		Do("Table for one");
		host.msgImHungry(this);
		stateChanged();
	}
	
	//Animation
	private void doGoToTable()
	{
		customerGui.doGoToTable();
		try
		{
			atTable.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	private void doWaitForFood(String ch)
	{
		do
		{
			try
			{
				for(int i = 0; i < menu.size(); i++)
				{
					if(menu.get(i).name == choice)
					{
						customerGui.doWaitForFood(menu.get(i).type);
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
	}
	private void doGetFood(String ch)
	{
		do
		{
			try
			{
				for(int i = 0; i < menu.size(); i++)
				{
					if(menu.get(i).name == choice)
					{
						customerGui.doGetFood(menu.get(i).type);
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
	}
	private void doGoToCashier()
	{
		waiter.msgLeavingRestaurant(this);
		customerGui.doGoToCashier();
		try
		{
			atCashier.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	private void doExitRestaurant()
	{
		customerGui.doExitRestaurant();
		try
		{
			leftRestaurant.acquire();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	// Accessors, etc.
	public String getName()
	{
		return name;
	}
	public int getHungerLevel()
	{
		return hungerLevel;
	}
	public void setHungerLevel(int hungerLevel)
	{
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}
	public String toString()
	{
		return "customer " + getName();
	}
	public void setGui(CustomerGui g)
	{
		customerGui = g;
	}
	public CustomerGui getGui()
	{
		return customerGui;
	}
	public void setCashier(Cashier cash)
	{
		cashier = cash;
	}
	public EventLog getLog()
	{
		return log;
	}
}

