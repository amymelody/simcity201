package simcity.anjalirestaurant;

import simcity.RestCashierRole;
import simcity.anjalirestaurant.interfaces.Cashier;
import simcity.anjalirestaurant.interfaces.Customer;
import simcity.anjalirestaurant.interfaces.Market;
import simcity.anjalirestaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Cashier Agent, Scenario 1
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios
public class CashierAgent extends Agent implements Cashier{
	static final int NTABLES = 3;//a global for the number of tables.
	
	private String name;
	private Waiter waiter;
	private Customer customer;
	private Market market;
	public boolean cantPay = false;
	public String brokeCashier = "brokeCashier";
	public CashierAgent(String name) {
		super();

		this.name = name;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	

	public void setWaiter(Waiter waiter){
		this.waiter = waiter;
	}
	
	public void setCustomer(Customer customer){
		this.customer = customer;
	}
	
	private final Map<String, Double> prices;{
		prices = new HashMap<String, Double>();	
			prices.put("Steak", 15.99);
			prices.put("Chicken", 10.99);
			prices.put("Salad", 5.99);
			prices.put("Pizza", 8.99);
			
			}
		
		public Map<String, Double> getPrices(){
			return prices;
		}
		
		public class bill{
			private Customer c;
			Waiter w;
			String choice;
			int tableNumber;
			CashierState s;
			
		public bill(Customer customer, Waiter waiter, String c, int tn, CashierState s){
			this.setC(customer);
			this.choice = c;
			this.tableNumber = tn;
			this.w = waiter;
			this.s = s;
		}
		public CashierState getCashierState(){
			return s;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public Customer getC() {
			return c;
		}
		public void setC(Customer c) {
			this.c = c;
		}
		private double price = 0.00;
		double payment = 0.00;
		
		}
		public enum paymentState {nothing, receivedCheck, payingCheck, paidCheck};

		public class marketPayment{
			Market m;
			paymentState s;
			private double price;
			
		marketPayment(Market m, double p, paymentState s){
			this.m = m;
			this.s = s;
			this.setPrice(p);
		}
		public paymentState getPaymentState(){
			return s;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		}
		
		double billAmount = 0.00;
		
		private paymentState pstate = paymentState.nothing;
		
		
		public List<bill> bills = Collections.synchronizedList(new ArrayList<bill>());
		
		public List<marketPayment> marketPayments = Collections.synchronizedList(new ArrayList<marketPayment>());
		private double cash = 1000.00;
		
		public double getCash(){
			return cash;
		}
		
		
		public enum CashierState{doingNothing, checkMade, checkDelivered, processingPayment, paymentComplete, payMarket, marketPaid};
		//public enum payMarketState{nothing, payMarket, marketPaid, unableToPay}
		/*
		payMarketState marketState = payMarketState.nothing;
		public payMarketState getMarketState(){
			return marketState;
			*/
		
		public CashierState state = CashierState.doingNothing;
		private String choice;
		private int tableNumber;
		public Object log;
		
/////MESSAGES////////
	public void msgMakeCheck(Customer c, String choice, int tableNumber, Waiter w){
		//Receives order from waiter, creates check for that order	
		Do("Cashier received message from waiter to make check for " + c.getName());
		
		if(c.getName() == "brokeCashier"){
			cantPay = true;
		}
		
		bills.add(new bill(c, w, choice, tableNumber, CashierState.checkMade));	
		
			stateChanged();
		}
	/*
	public void msgIHaveNoMoney(Customer c){
		Do("Customer has no money to pay me.");
		for(bill j : bills){
			if(j.c == c){
				j.payment = 0.0;
				j.s = CashierState.processingPayment;
				stateChanged();
			}
		}
		
	}
	*/
	public void msgTakeMyMoney(Customer c, double p){
		
	synchronized(bills){
		for(bill j : bills){
			if(j.getC() == c){
				j.payment = p;
				j.s = CashierState.processingPayment;
				stateChanged();
			}
		}
	}
	}
	
	public void msgPayMarket(Market m, boolean broke, double price){
		Do("Cashier received bill from " + m.getName());
		marketPayments.add(new marketPayment(m, price, paymentState.receivedCheck));
		//this.market = m;
		this.cantPay = broke;
		//cash = cash - price;
		//marketState = payMarketState.payMarket;
		stateChanged();
	}
	
////////SCHEDULER//////
	public boolean pickAndExecuteAnAction() {
	
		/*
		if(marketState == payMarketState.payMarket){
			payMarket();
			marketState = payMarketState.marketPaid;
			
		}
		*/
		synchronized(marketPayments){
			for(marketPayment p : marketPayments){
				if(p.s == paymentState.receivedCheck){
					payMarket(p.m, p.getPrice());
					p.s = paymentState.payingCheck;
					return true;
				}
			}
			
		}
		
		synchronized(bills){
		for(bill b : bills){
		
		
		
		if(b.s == CashierState.checkMade){
			giveMeMoney(b);
			b.s = CashierState.checkDelivered;
			return true;
		}
	
		if(b.s == CashierState.processingPayment){
			processPayment(b);
			b.s = CashierState.paymentComplete;
			return true;
		}
		
		}
		
		
		
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
		}
		
		
		
		return false;
		 
	}
	

	///////// ACTIONS/////////////
		
 
	public void giveMeMoney(bill b){
		Do("Cashier is making check.");
		for(Map.Entry<String, Double> entry : prices.entrySet()){
			if(entry.getKey() == b.choice){
				b.setPrice(entry.getValue());
				Do("Cashier is giving check to waiter for amount " + b.getPrice());
				//Gives waiter check upon request
				b.w.msgHereIsCheck(b.tableNumber, b.getPrice());
				break;
			}
			
	}
	}
	
	public void processPayment(bill b){
		//Receives money from customer 
		if(b.getPrice() == b.payment){
			
			cash = cash + b.payment;
			b.getC().msgGoodToGo();
			b.setPrice(0);
			b.payment = 1;
			
			
		}
		//If customer cannot pay the amount, the cashier lets him pay later
		else{
			b.getC().msgPayMeLater();
			
		}
		bills.remove(b);
	}

	public void payMarket(Market m, double price){
		
		if(cantPay == false){
		cash = cash - price;
		Do("Cashier is paying market for order");
		m.msgHereIsMoney();
		}
		if(cantPay == true){
			Do("Cashier does not have enough money to pay marekt. Will pay more for the item next time.");
		
		}
		
		
	}

	@Override
	public void msgIHaveNoMoney(Customer c) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
}

	