package simcity.cherysrestaurant; 

import simcity.RestCashierRole;
import simcity.agent.Agent;
import simcity.cherysrestaurant.interfaces.*;
import simcity.interfaces.MarketDeliverer;
import simcity.joshrestaurant.interfaces.JoshCustomer;
import simcity.joshrestaurant.interfaces.JoshWaiter;
import simcity.mock.EventLog;
import simcity.mock.LoggedEvent;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cashier Agent
 */
public class CherysCashierRole extends RestCashierRole implements CherysCashier
{
	List<Food> menu = new ArrayList<Food>();
	public class Food
	{
		String name;
		int price;
		Food(String n, int p)
		{
			name = n;
			price = p;
		}
	}
	public List<CherysCashierCheck> checks = new ArrayList<CherysCashierCheck>();
	public class MarketBill
	{
		public CherysMarket market;
		public String foodType;
		public int total;
		public CheckState state;
		MarketBill(CherysMarket m, String ft, int t)
		{
			market = m;
			foodType = ft;
			total = t;
			state = CheckState.askedFor;
		}
	}
	public List<MarketBill> bills = new ArrayList<MarketBill>();
	public enum CheckState
	{
		askedFor,
		unpaid,
		paid;
	}
	public int balance = 250;
	
	private int priceSteak = 15;
	private int priceChicken = 10;
	private int priceSalad = 5;
	private int pricePizza = 8;

	private boolean working;
	private boolean goingHome;

	public EventLog log = new EventLog();
	
	/**
	 * Constructor for CashierAgent
	 * @param name name of the cashier
	 */
	public CherysCashierRole() //* called from RestaurantPanel
	{
		super();

		menu.add(new Food("Steak", priceSteak));
		menu.add(new Food("Chicken", priceChicken));
		menu.add(new Food("Salad", priceSalad));
		menu.add(new Food("Pizza", pricePizza));

	}
	
	public String getName()
	{
		return name;
	}
	
	//Messages
	public void msgProduceCheck(CherysWaiter w, String choice, int table)
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgProduceCheck");
		log.add(new LoggedEvent("Received msgProduceCheck from waiter. Choice = " + choice + ". Table = " + table));
		int price = 0;
		do
		{
			try
			{
				for(Food food : menu)
				{
					if(food.name.equals(choice))
					{
						price = food.price;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		checks.add(new CherysCashierCheck(w, table, choice, price));
		stateChanged();
	}
	public void msgGiveCheck(CherysWaiter w, int table)
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgGiveCheck");
		log.add(new LoggedEvent("Received msgGiveCheck from waiter. Table = " + table));
		do
		{
			try
			{
				for(CherysCashierCheck ch : checks)
				{
					if(ch.waiter == w && ch.table == table)
					{
						ch.state = CheckState.askedFor;
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
	public void msgPayment(CherysCustomer cust, CherysCashierCheck c, int cashGiven)
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgPayment. Payment = " + cashGiven);
		log.add(new LoggedEvent("Received msgPayment from customer. Payment = " + cashGiven));
		do
		{
			try
			{
				for(CherysCashierCheck ch : checks)
				{
					if(ch.waiter == c.waiter && ch.table == c.table && ch.order == c.order && ch.total == c.total)
					{
						ch.customer = cust;
						ch.amountPaid = cashGiven;
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
	public void msgPayForDelivery(CherysMarket m, String foodType, int amountDelivered, double wholesalePercentage)
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgPayForDelivery");
		log.add(new LoggedEvent("Received msgPayForDelivery from market. Food = " + foodType + ". Amount = " + amountDelivered + ". Percentage = " + wholesalePercentage*100 + "%"));
		int total = 0;
		do
		{
			try
			{
				for(Food food : menu)
				{
					if(food.name.equals(foodType))
					{
						total = (int)(food.price*amountDelivered*wholesalePercentage);
					}
				}
			}
			catch(ConcurrentModificationException c)
			{
				continue;
			}
		}
		while(false);
		bills.add(new MarketBill(m, foodType, total));
		stateChanged();
	}

	public void msgPaySalary(int salary)
	{
		balance -= salary;
	}
	
	@Override
	public void msgStartShift()
	{
		AlertLog.getInstance().logMessage(AlertTag.CHERYS_RESTAURANT, name, "received msgStartShift");
		working = true;
		goingHome = false;
		msgPaySalary(person.getSalary());
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
				for(CherysCashierCheck ch : checks)
				{
					if(ch.amountPaid > 0 && ch.state != CheckState.paid)
					{
						processPayment(ch);
						return true;
					}
				}
			}
			catch(ConcurrentModificationException c)
			{
				continue;
			}
		}
		while(false);
		do
		{
			try
			{
				for(CherysCashierCheck ch : checks)
				{
					if(ch.state == CheckState.askedFor)
					{
						ch.state = null;
						giveWaiterCheck(ch);
						return true;
					}
				}
			}
			catch(ConcurrentModificationException c)
			{
				continue;
			}
		}
		while(false);
		do
		{
			try
			{
				for(MarketBill b : bills)
				{
					if(b.state == CheckState.askedFor)
					{
						tryToPay(b);
						return true;
					}
				}
			}
			catch(ConcurrentModificationException c)
			{
				continue;
			}
		}
		while(false);
		if(checks.size() == 0 && !working && goingHome)
		{
			leaveRestaurant();
			return true;
		}
		return false;
	}

	// Actions
	private void giveWaiterCheck(CherysCashierCheck ch)
	{
		Do("Waiter, here's your check");
		ch.waiter.msgHereIsCheck(ch);
		stateChanged();
	}
	private void processPayment(CherysCashierCheck ch)
	{
		Do("Processing payment");
		int change  = ch.amountPaid - ch.total;
		balance += ch.total;
		do
		{
			try
			{
				for(MarketBill b : bills)
				{
					if(b.state == CheckState.unpaid)
					{
						b.state = CheckState.askedFor;
					}
				}
			}
			catch(ConcurrentModificationException cme)
			{
				continue;
			}
		}
		while(false);
		ch.state = CheckState.paid;
		ch.customer.msgChange(change);
		checks.remove(ch);
		stateChanged();
	}
	private void tryToPay(MarketBill b)
	{
		Do("Trying to pay " + b.foodType + " bill for $" + b.total + ". I have $" + balance);
		if(b.total <= balance)
		{
			b.market.msgPaymentForDelivery(b.total);
			balance -= b.total;
			bills.remove(b);
		}
		else
		{
			Do("Unable to pay. Need $" + (Math.round((b.total - balance) * 100.0) / 100.0) + " more.");
			b.state = CheckState.unpaid;
		}
		stateChanged();
	}
	
	private void leaveRestaurant()
	{
		person.msgLeftDestination(this);
	}

	@Override
	public void msgDelivery(int bill, MarketDeliverer deliverer)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgThankYou(int change)
	{
		// TODO Auto-generated method stub
		
	}
}