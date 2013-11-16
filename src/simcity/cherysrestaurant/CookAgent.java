package simcity.cherysrestaurant;

import agent.Agent;
import simcity.cherysrestaurant.CashierAgent.CheckState;
import simcity.cherysrestaurant.interfaces.*;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cook Agent
 */
public class CookAgent extends Agent implements Cook
{
	private String name;
	private List<Order> orders = new ArrayList<Order>();
	private class Order
	{
		Waiter w;
		String choice;
		int table;
		OrderState state;
		Order(Waiter w, String c, int t)
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
		int low = 1;
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
		Market m;
		int amountInStock;
		UnderstockedMarket(Market m, int a)
		{
			this.m = m;
			amountInStock = a;
		}
	}
	private int platingTime = 2000;
	
	private List<MyMarket> markets = new ArrayList<MyMarket>();
	private class MyMarket
	{
		Market m;
		int amountOrderedFrom = 0;
		MyMarket(Market m)
		{
			this.m = m;
		}
	}
	
	private int cookTimeSteak = 15000;
	private int cookTimeChicken = 10000;
	private int cookTimeSalad = 5000;
	private int cookTimePizza = 8000;

	/**
	 * Constructor for CookAgent
	 * @param name name of the cook
	 */
	public CookAgent(String name) //* called from RestaurantPanel
	{
		super();

		this.name = name;
		menu.add(new Food("Steak", cookTimeSteak, 1));
		menu.add(new Food("Chicken", cookTimeChicken, 10));
		menu.add(new Food("Salad", cookTimeSalad, 1));
		menu.add(new Food("Pizza", cookTimePizza, 1));

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
	
	//Messages
	public void msgCookThis(Waiter w, String choice, int table) //* called from Waiter.takeOrder
	{
		Do("recieved msgCookThis");
		orders.add(new Order(w, choice, table));
		
		stateChanged();
	}
	public void msgDelivery(Market m, String f, int numberDelivered)
	{
		Do("recieved msgDelivery. Food = " + f + " Amount = " + numberDelivered);
		do
		{
			try
			{
				for(Food food : menu)
				{
					if(food.name == f)
					{
						food.waitingOnDelivery = false;
						food.amount += numberDelivered;
						outOfStock.remove(f);
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
	public void msgStockTooLow(Market m, String f, int numberAvailible)
	{
		Do("recieved msgStockTooLow");
		do
		{
			try
			{
				for(Food food : menu)
				{
					if(food.name == f)
					{
						food.waitingOnDelivery = false;
						food.understockedMarkets.add(new UnderstockedMarket(m, numberAvailible));
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
		stateChanged();
		
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction()
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
		return false;
	}

	// Actions
	/**
	 * "Cooks" the given order for the cook time of that food
	 * @param o the order to be cooked
	 */
	private void cookIt(final Order o)
	{
		Do("Cooking order");
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
			}, timerLength);
		o.state = OrderState.cooking;
//		DoCookIt(o);
	}
	/**
	 * Changes the state of the order to 'done'
	 * @param o the order being cooked
	 */
	private void foodDone(Order o)
	{
		Do("Done cooking");
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
		Do("Order up!");
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
		Do("I need " + f.name);
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
			markets.get(leastOrderedFrom).m.msgPlaceOrder(f.name, (f.capacity - f.amount));
			markets.get(leastOrderedFrom).amountOrderedFrom++;
			stateChanged();
		}
		else //non-normative
		{
			int[] mostStockToLeast = new int[f.understockedMarkets.size()];
			do
			{
				try
				{
					for(int j = 0; j < f.understockedMarkets.size(); j++) //Should arrange the markets in order from the one with the most stock to the least
					{
						mostStockToLeast[j] = -1;
						do
						{
							try
							{
								for(int i = 0; i < f.understockedMarkets.size(); i++)
								{
									if(j == 0)
									{
										if(f.understockedMarkets.get(i).amountInStock > 0)
										{
											if(mostStockToLeast[j] == -1)
											{
												mostStockToLeast[j] = i;
											}
											else if(f.understockedMarkets.get(i).amountInStock > f.understockedMarkets.get(mostStockToLeast[j]).amountInStock)
											{
												mostStockToLeast[j] = i;
											}
										}
									}
									else if(j == 1)
									{
										if(f.understockedMarkets.get(i).amountInStock > 0 && mostStockToLeast[0] != i)
										{
											if(mostStockToLeast[j] == -1)
											{
												mostStockToLeast[j] = i;
											}
											else if(f.understockedMarkets.get(i).amountInStock > f.understockedMarkets.get(mostStockToLeast[j]).amountInStock)
											{
												mostStockToLeast[j] = i;
											}
										}
									}
									else if(j == 2)
									{
										if(f.understockedMarkets.get(i).amountInStock > 0 && mostStockToLeast[0] != i && mostStockToLeast[1] != i)
										{
											if(mostStockToLeast[j] == -1)
											{
												mostStockToLeast[j] = i;
											}
											else if(f.understockedMarkets.get(i).amountInStock > f.understockedMarkets.get(mostStockToLeast[j]).amountInStock)
											{
												mostStockToLeast[j] = i;
											}
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
				}
				catch(ConcurrentModificationException cme)
				{
					continue;
				}
			}
			while(false);
			if(mostStockToLeast[0] > -1)
			{
				int amountNeeded = f.capacity - f.amount;
				do
				{
					try
					{
						for(int i = 0; i < f.understockedMarkets.size(); i++)
						{
							if(amountNeeded > 0 && mostStockToLeast[i] > -1)
							{
								if(f.understockedMarkets.get(mostStockToLeast[i]).amountInStock <= amountNeeded && f.understockedMarkets.get(mostStockToLeast[i]).amountInStock > 0)
								{
									amountNeeded -= f.understockedMarkets.get(mostStockToLeast[i]).amountInStock;
									f.understockedMarkets.get(mostStockToLeast[i]).m.msgPlaceOrder(f.name, f.understockedMarkets.get(mostStockToLeast[i]).amountInStock);
									do
									{
										try
										{
											for(MyMarket market : markets)
											{
												if(f.understockedMarkets.get(mostStockToLeast[i]).m == market.m)
												{
													market.amountOrderedFrom++;
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
								else if(f.understockedMarkets.get(mostStockToLeast[i]).amountInStock > amountNeeded)
								{
									f.understockedMarkets.get(mostStockToLeast[i]).amountInStock -= amountNeeded;
									f.understockedMarkets.get(mostStockToLeast[i]).m.msgPlaceOrder(f.name, amountNeeded);
									amountNeeded = 0;
									do
									{
										try
										{
											for(MyMarket market : markets)
											{
												if(f.understockedMarkets.get(mostStockToLeast[i]).m == market.m)
												{
													market.amountOrderedFrom++;
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
						}
					}
					catch(ConcurrentModificationException cme)
					{
						continue;
					}
				}
				while(false);
				if(amountNeeded > 0)
				{
					Do("Completely out of " + f.name + ". Ordered some, but still " + amountNeeded + " short.");
					f.closed = true;
				}
				f.waitingOnDelivery = true;
				stateChanged();
			}
			else
			{
				Do("Completely out of " + f.name);
				f.closed = true;
				stateChanged();
			}
		}
	}
	
	//Utilities
	public void setMarket(Market m)
	{
		markets.add(new MyMarket(m));
	}
}