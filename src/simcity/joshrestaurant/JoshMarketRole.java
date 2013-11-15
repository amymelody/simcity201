package simcity.joshrestaurant;

import agent.Agent;
import simcity.joshrestaurant.interfaces.JoshMarket;

import java.util.*;


/**
 * Restaurant Market Agent
 */

public class JoshMarketRole extends Agent implements JoshMarket {
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());

	private String name;
	private JoshCookRole cook;
	private JoshCashierRole cashier;
	private Timer timer = new Timer();
	private int cash;
	
	Food steak;
	Food chicken;
	Food salad;
	Food pizza;
	
	Map<String, Food> foods = new HashMap<String, Food>();
	Map<String, Integer> foodPrices = new HashMap<String, Integer>();
	
	public enum OrderState
	{Received, ProducingOrder, Ready, Finished};
	
	public JoshMarketRole(String name, JoshCookRole c, JoshCashierRole ca, int stAmt, int cAmt, int saAmt, int pAmt) {
		super();
		this.name = name;
		cook = c;
		cashier = ca;
		cash = 1000;
		
		steak = new Food("steak", 10, stAmt);
		chicken = new Food("chicken", 10, cAmt);
		salad = new Food("salad", 10, saAmt);
		pizza = new Food("pizza", 10, pAmt);
		
		foods.put("steak", steak);
		foods.put("chicken", chicken);
		foods.put("salad", salad);
		foods.put("pizza", pizza);
		
		foodPrices.put("steak", 12);
		foodPrices.put("chicken", 9);
		foodPrices.put("salad", 4);
		foodPrices.put("pizza", 6);
	}
	

	public String getName() {
		return name;
	}

	public List getOrders() {
		return orders;
	}
	
	public int timeToProduceOrder(Order o) {
		int time = 0;
		for (JoshItemOrder io : o.items) {
			time += foods.get(io.getFood()).timeToProduce*io.getAmount();
		}
		return time*400;
	}
	
	// Messages
	
	public void msgHereIsOrder(List<JoshItemOrder> io) {
		List<JoshItemOrder> temp = new ArrayList<JoshItemOrder>();
		for (JoshItemOrder o : io) {
			if (foods.get(o.getFood()).amount >= o.getAmount()) {
				temp.add(o);
			}
		}
		orders.add(new Order(temp, OrderState.Received));
		stateChanged();
	}
	
	public void msgPayment(int cash) {
		print("Received payment");
		this.cash += cash;
		stateChanged();
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(orders) {
			for (Order order : orders) {
				if (order.getState() == OrderState.Ready) {
					deliverOrder(order);
					return true;
				}
			}
			for (Order order : orders) {
				if (order.getState() == OrderState.Received) {
					produceOrder(order);
					return true;
				}
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void produceOrder(Order o) {
		print("Here is what I can fulfill: ");
		for (JoshItemOrder io : o.items) {
			print(io.getAmount() + " " + io.getFood() + "s");
		}
		cook.msgHereIsWhatICanFulfill(o.items);
		if (!o.items.isEmpty()) {
			o.setState(OrderState.ProducingOrder);
			timer.schedule(new ProducingTimerTask(o) {
				@Override
				public void run() {
					order.setState(OrderState.Ready);
					stateChanged();
				}
			},
			timeToProduceOrder(o));
			for (JoshItemOrder io : o.items) {
				foods.get(io.getFood()).amount -= io.getAmount();
			}
		} else {
			print("I can't fulfill this order");
			o.setState(OrderState.Finished);
		}
	}
	
	private void deliverOrder(Order o) {
		int bill = 0;
		print("Here is your order: ");
		for (JoshItemOrder io : o.items) {
			print(io.getAmount() + " " + io.getFood() + "s");
			bill += foodPrices.get(io.getFood())*io.getAmount();
		}
		cook.msgOrderDelivered(o.items);
		print("Here is the bill: $" + bill);
		cashier.msgHereIsBill(bill, this);
		o.setState(OrderState.Finished);
	}
	

	//utilities

	private class Order {
		List<JoshItemOrder> items;
		OrderState state;

		Order(List<JoshItemOrder> io, OrderState s) {
			items = io;
			state = s;
		}
		
		OrderState getState() {
			return state;
		}
		
		void setState(OrderState s) {
			state = s;
		}
	}
	
	private class Food {
		String type;
		int timeToProduce;
		int amount;
		
		Food(String t, int p, int a) {
			type = t;
			timeToProduce = p;
			amount = a;
		}
		
		String getType() {
			return type;
		}
		
		int getTimeToProduce() {
			return timeToProduce;
		}
		
		void setAmount(int a) {
			amount = a;
		}
		
		int getAmount() {
			return amount;
		}
	}
	
	private class ProducingTimerTask extends TimerTask {
		Order order;
		
		ProducingTimerTask(Order o) {
			order = o;
		}
		
		public void run() {
			order = order;
		}
	}
}

