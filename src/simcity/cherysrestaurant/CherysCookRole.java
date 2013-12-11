package simcity.cherysrestaurant; 

import simcity.ItemOrder;
import simcity.RestCookRole;
import simcity.agent.Agent;
import simcity.cherysrestaurant.CherysCashierRole.CheckState;
import simcity.cherysrestaurant.interfaces.*;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.RestCashier;
import simcity.interfaces.RestCook;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cook Agent
 */
public class CherysCookRole extends RestCookRole implements CherysCook
{
	private List<Order> orders = new ArrayList<Order>();
	private class Order
	{
		CherysWaiter w;
		String choice;
		int table;
		OrderState state;
		Order(CherysWaiter w, String c, int t)
		{
			this.w = w;
			choice = c;
			table = t;
			state = OrderState.pending;
		}
	}
	enum OrderState
	{
		pending,
		cooking,
		done,
		plating;
	}
	Timer timer = new Timer();
	List<Food> menu = new ArrayList<Food>();
	List<String> outOfStock = new ArrayList<String>();
	private class Food
	{
		String name;
		int amount;
		int low = 8;
		int capacity;
		boolean waitingOnDelivery = false;
		boolean closed = false;
		int cookingTime;
		List<UnderstockedMarket> understockedMarkets = new ArrayList<UnderstockedMarket>();
		Food(String n, int ct, int a)
		{
			name = n;
			amount = a;
			capacity = 10;
			cookingTime = ct;
		}
	}
	private class UnderstockedMarket
	{
		MarketCashier m;
		UnderstockedMarket(MarketCashier m)
		{
			this.m = m;
		}
	}
	private int platingTime = 2000;
	
	private List<MyMarket> markets = new ArrayList<MyMarket>();
	private class MyMarket
	{
		MarketCashier m;
		int amountOrderedFrom = 0;
		MyMarket(MarketCashier m)
		{
			this.m = m;
		}
	}
	
	private int cookTimeSteak = 15000;
	private int cookTimeChicken = 10000;
	private int cookTimeSalad = 5000;
	private int cookTimePizza = 8000;
	
	private boolean working;
	private boolean goingHome;
	
	private CherysCashier cashier;

	/**
	 * Constructor for CookAgent
	 * @param name name of the cook
	 */
	public CherysCookRole() //* called from RestaurantPanel
	{
		super();
		menu.add(new Food("Steak", cookTimeSteak, 10000));
		menu.add(new Food("Chicken", cookTimeChicken, 10000));
		menu.add(new Food("Salad", cookTimeSalad, 10000));
		menu.add(new Food("Pizza", cookTimePizza, 8));

		do
		{
			try
			{
				for(Food food : menu)
				{
					if(food.amount <= 0)
					{
						outOfStock.add(food.name);
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
	
	public String getName()
	{
		return name;
	}
	public void setCashier(CherysCashier c)
	{
		cashier = c;
	}
	
	//Messages
	public void msgCookThis(CherysWaiter w, String choice, int table) //* called from Waiter.takeOrder
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgCookThis");
		orders.add(new Order(w, choice, table));
		
		stateChanged();
	}
	@Override
	public void msgDelivery(List<ItemOrder> order)
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgDelivery");
		do
		{
			try
			{
				for(Food food : menu)
				{
					if(food.name.equals(order.get(0).getFoodItem()))
					{
						food.waitingOnDelivery = false;
						food.amount += order.get(0).getAmount();
						outOfStock.remove(food.name);
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		stateChanged();
	}

	@Override
	public void msgHereIsWhatICanFulfill(List<ItemOrder> items, boolean canFulfill, MarketCashier m)
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgHereIsWhatICanFulfill");
		if(canFulfill)
		{
			do
			{
				try
				{
					for(Food food : menu)
					{
						if(food.name.equals(items.get(0).getFoodItem()))
						{
							if((food.amount + items.get(0).getAmount()) < food.capacity)
							{
								food.understockedMarkets.add(new UnderstockedMarket(m));
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
		}
		else
		{
			for(Food food : menu)
			{
				if(food.name.equals(items.get(0).getFoodItem()))
				{
					food.waitingOnDelivery = false;
				}
			}
		}
		stateChanged();
	}

	@Override
	public void msgStartShift()
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgStartShift");
		working = true;
		goingHome = false;
		cashier.msgPaySalary(person.getSalary());
		stateChanged();
	}

	@Override
	public void msgEndShift()
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgEndShift");
		working = false;
		stateChanged();
	}
	public void msgGoHome()
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgGoHome");
		goingHome = true;
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction()
	{
		do
		{
			try
			{
				for(Food food : menu)
				{
					if(food.amount <= food.low && !(food.waitingOnDelivery) && !(food.closed))
					{
						placeOrder(food);
						return true;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		do
		{
			try
			{
				for(Order order : orders)
				{
					do
					{
						try
						{
							for(String outOf : outOfStock)
							{
								if(order.choice == outOf)
								{
									outOfFood(order);
									return true;
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
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		do
		{
			try
			{
				for(Order order : orders)
				{
					if(order.state == OrderState.done)
					{
						plateIt(order);
						return true;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		do
		{
			try
			{
				for(Order order : orders)
				{
					if(order.state == OrderState.pending)
					{
						cookIt(order);
						return true;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		if(orders.size() == 0 && !working && goingHome)
		{
			leaveRestaurant();
			return true;
		}
		return false;
	}

// Actions
	/**
	 * "Cooks" the given order for the cook time of that food
	 * @param o the order to be cooked
	 */
	private void cookIt(final Order o)
	{
		int timerLength = 0;
		do
		{
			try
			{
				for(Food food : menu)
				{
					if(food.name == o.choice)
					{
						timerLength = food.cookingTime;
						food.amount--;
						if(food.amount <= 0)
						{
							outOfStock.add(food.name);
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
		timer.schedule(new TimerTask() 
			{
				public void run()
				{
					foodDone(o);
				}
			}, (int)(timerLength*0.25));
		o.state = OrderState.cooking;
//		DoCookIt(o);
	}
	/**
	 * Changes the state of the order to 'done'
	 * @param o the order being cooked
	 */
	private void foodDone(Order o)
	{
		o.state = OrderState.done;
		stateChanged();
	}
	/**
	 * Preps the finished order for being served
	 * @param o the finished order
	 */
	private void plateIt(final Order o)
	{
		timer.schedule(new TimerTask() 
			{
				public void run()
				{
					alertWaiter(o);
				}
			}, platingTime);
		o.state = OrderState.plating;
	}
	/**
	 * alerts the waiter that left the order 
	 * @param o the order ready to be served
	 */
	private void alertWaiter(Order o)
	{
		o.w.msgOrderReady(o.choice, o.table, outOfStock);
		stateChanged();
	}
	private void outOfFood(Order o)
	{
		o.w.msgOutOfFood(o.choice, o.table, outOfStock);
		orders.remove(o);
	}

	private void placeOrder(Food f)
	{
		boolean[] fullyStocked = new boolean[markets.size()]; //each market is assumed fully stocked
		do
		{
			try
			{
				for(int i = 0; i < markets.size(); i++)
				{
					fullyStocked[i] = true;
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		int leastOrderedFrom = -1;
		do
		{
			try
			{
				for(int i = 0; i < markets.size(); i++) //check markets against the list of understocked markets
				{
					do
					{
						try
						{
							for(UnderstockedMarket market : f.understockedMarkets)
							{
								if(markets.get(i).m == market.m)
								{
									fullyStocked[i] = false; //set fullyStocked to false if the market is understocked
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
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		do
		{
			try
			{
				for(int i = 0; i < markets.size(); i++) //if the market is fully stocked and has been ordered from least...
				{
					if(leastOrderedFrom == -1)
					{
						if(fullyStocked[i])
						{
							leastOrderedFrom = i;
						}
					}
					else if(markets.get(i).amountOrderedFrom < markets.get(leastOrderedFrom).amountOrderedFrom && fullyStocked[i])
					{
						leastOrderedFrom = i;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		
		if(leastOrderedFrom > -1) //if a market is found, order
		{
			f.waitingOnDelivery = true;
			List<ItemOrder> temp = new ArrayList<ItemOrder>();
			temp.add(new ItemOrder(f.name, (f.capacity - f.amount)));
			markets.get(leastOrderedFrom).m.msgIWantDelivery((RestCook)this, (RestCashier)cashier, temp, getJobLocation());
			markets.get(leastOrderedFrom).amountOrderedFrom++;
			stateChanged();
		}
		else
		{
			f.closed = true;
			stateChanged();
		}
	}
	
	private void leaveRestaurant()
	{
		person.msgLeftDestination(this);
	}
	
	//Utilities
	@Override
	public void addMarket(MarketCashier m, String n)
	{
		markets.add(new MyMarket(m));
	}


}