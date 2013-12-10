package simcity.cherysrestaurant; 

import simcity.agent.Agent;
import simcity.cherysrestaurant.interfaces.*;
import simcity.mock.EventLog;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Market Agent
 */
public class CherysMarketRole extends Agent implements CherysMarket
{
	private String name;
	List<Food> stock = new ArrayList<Food>();
	private class Food
	{
		String name;
		int amount;
		Food(String n, int a)
		{
			name = n;
			amount = a;
		}
	}
	List<Order> orders = new ArrayList<Order>();
	private class Order
	{
		Food f;
		int amount;
		OrderState state;
		Order(Food f, int a)
		{
			this.f = f;
			amount = a;
			state = OrderState.pending;
		}
	}
	enum OrderState
	{
		pending,
		processing,
		done;
	}
	
	private Timer timer = new Timer();
	private int deliveryTime = 10000;
	
	private double moneyEarned = 0.0;

	private CherysCook cook;
	private CherysCashier cashier;
	
	public EventLog log = new EventLog();

	/**
	 * Constructor for Cook
	 * @param name name of the cook
	 */
	public CherysMarketRole(String name) //* called from RestaurantPanel
	{
		super();

		this.name = name;
		stock.add(new Food("Steak", 100));
		stock.add(new Food("Chicken", 100));
		stock.add(new Food("Salad", 100));
		stock.add(new Food("Pizza", 100));
	}
	
	public String getName()
	{
		return name;
	}
	
	//Messages
	public void msgPlaceOrder(String f, int orderNumber)
	{
		AlertLog.getInstance().logMessage(AlertTag.HOUSING, name, "received msgPlaceOrder");
		do
		{
			try
			{
				for(Food food : stock)
				{
					if(food.name == f)
					{
						orders.add(new Order(food, orderNumber));
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
	public void msgPaymentForDelivery(double payment)
	{
		AlertLog.getInstance().logMessage(AlertTag.HOUSING, name, "received msgPaymentForDelivery. Payment = " + payment);
		moneyEarned += payment;
		moneyEarned = Math.round(moneyEarned * 100.0) / 100.0;
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
				for(Order order : orders)
				{
					if(order.state == OrderState.pending)
					{
						processIt(order);
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
	private void processIt(Order o)
	{
		o.state = OrderState.processing;
		do
		{
			try
			{
				for(Food food : stock)
				{
					if(food == o.f)
					{
						if((food.amount - o.amount) >= 0)
						{
							food.amount -= o.amount;
							deliverIt(o);
						}
						else //non normative
						{
							lowStock(o);
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
	
	private void deliverIt(final Order o)
	{
		timer.schedule(new TimerTask() 
			{
				public void run()
				{
					deliveredIt(o);
				}
			}, deliveryTime);
	}
	
	private void deliveredIt(Order o)
	{
		o.state = OrderState.done;
		cook.msgDelivery(this, o.f.name, o.amount);
		double wholesalePercentage = 0.70;
		cashier.msgPayForDelivery(this, o.f.name, o.amount, wholesalePercentage);
	}
	private void lowStock(Order o)
	{
		do
		{
			try
			{
				for(Food food : stock)
				{
					if(food == o.f)
					{
						cook.msgStockTooLow(this, food.name, food.amount);
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
	
	//Utilities
	public void setCook(CherysCook c)
	{
		cook = c;
	}
	public void setCashier(CherysCashier c)
	{
		cashier = c;
	}
	public void setAmount(String type, int a)
	{
		do
		{
			try
			{
				for(Food f : stock)
				{
					if(f.name == type)
					{
						f.amount = a;
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
	public EventLog getLog()
	{
		return log;
	}
}