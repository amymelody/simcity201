package simcity.cherysrestaurant;

import simcity.agent.Agent;
import simcity.cherysrestaurant.interfaces.*;
import simcity.mock.EventLog;
import simcity.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cashier Agent
 */
public class CashierAgent extends Agent implements Cashier
{
	private String name;
	List<Food> menu = new ArrayList<Food>();
	public class Food
	{
		String name;
		double price;
		Food(String n, double p)
		{
			name = n;
			price = p;
		}
	}
	public List<CashierCheck> checks = new ArrayList<CashierCheck>();
	public class MarketBill
	{
		public Market market;
		public String foodType;
		public double total;
		public CheckState state;
		MarketBill(Market m, String ft, double t)
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
	public double balance = 100;
	
	private double priceSteak = 15.99;
	private double priceChicken = 10.99;
	private double priceSalad = 5.99;
	private double pricePizza = 8.99;
	
	/**
	 * Constructor for CashierAgent
	 * @param name name of the cashier
	 */
	public CashierAgent(String name) //* called from RestaurantPanel
	{
		super();

		this.name = name;
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
	public void msgProduceCheck(Waiter w, String choice, int table)
	{
		log.add(new LoggedEvent("Received msgProduceCheck from waiter. Choice = " + choice + ". Table = " + table));
		Do("received msgProduceCheck");
		double price = 0;
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
		checks.add(new CashierCheck(w, table, choice, price));
		stateChanged();
	}
	public void msgGiveCheck(Waiter w, int table)
	{
		log.add(new LoggedEvent("Received msgGiveCheck from waiter. Table = " + table));
		Do("received msgGiveCheck");
		do
		{
			try
			{
				for(CashierCheck ch : checks)
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
	public void msgPayment(Customer cust, CashierCheck c, double cashGiven)
	{
		log.add(new LoggedEvent("Received msgPayment from customer. Payment = " + cashGiven));
		Do("received msgPayment. Payment = " + cashGiven);
		do
		{
			try
			{
				for(CashierCheck ch : checks)
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
	public void msgPayForDelivery(Market m, String foodType, int amountDelivered, double wholesalePercentage)
	{
		log.add(new LoggedEvent("Received msgPayForDelivery from market. Food = " + foodType + ". Amount = " + amountDelivered + ". Percentage = " + wholesalePercentage*100 + "%"));
		Do("received msgPayForDelivery");
		double total = 0;
		do
		{
			try
			{
				for(Food food : menu)
				{
					if(food.name.equals(foodType))
					{
						total = food.price*amountDelivered*wholesalePercentage;
						total = Math.round(total * 100.0) / 100.0;
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
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction()
	{
		do
		{
			try
			{
				for(CashierCheck ch : checks)
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
				for(CashierCheck ch : checks)
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
		return false;
	}

	// Actions
	private void giveWaiterCheck(CashierCheck ch)
	{
		Do("Waiter, here's your check");
		ch.waiter.msgHereIsCheck(ch);
		stateChanged();
	}
	private void processPayment(CashierCheck ch)
	{
		Do("Processing payment");
		double change  = ch.amountPaid - ch.total;
		change = Math.round(change * 100.0) / 100.0;
		balance += ch.total;
		balance = Math.round(balance * 100.0) / 100.0;
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
		b.total = Math.round(b.total * 100.0) / 100.0;
		Do("Trying to pay " + b.foodType + " bill for $" + b.total + ". I have $" + balance);
		if(b.total <= balance)
		{
			b.market.msgPaymentForDelivery(b.total);
			balance -= b.total;
			balance = Math.round(balance * 100.0) / 100.0;
			bills.remove(b);
		}
		else
		{
			Do("Unable to pay. Need $" + (Math.round((b.total - balance) * 100.0) / 100.0) + " more.");
			b.state = CheckState.unpaid;
		}
		stateChanged();
	}
}