package simcity.anjalirestaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simcity.RestCashierRole;
import simcity.anjalirestaurant.interfaces.AnjaliCashier;
import simcity.anjalirestaurant.interfaces.AnjaliCustomer;
import simcity.anjalirestaurant.interfaces.AnjaliMarket;
import simcity.anjalirestaurant.interfaces.AnjaliWaiter;
import simcity.interfaces.BankManager;
import simcity.interfaces.MarketDeliverer;
import simcity.interfaces.Person;
import simcity.mock.LoggedEvent;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;



/**
 * Restaurant Cashier Agent, Scenario 1
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.

//Works for normative and nonnormative scenarios
public class AnjaliCashierRole extends RestCashierRole implements AnjaliCashier{
	static final int NTABLES = 3;//a global for the number of tables.
	
	private String name = null;
	private AnjaliWaiter waiter;
	private AnjaliCustomer customer;
	private AnjaliMarket market;
	public boolean cantPay = false;
	public String brokeCashier = "brokeCashier";
	private BankManager bankManager;
	private boolean working;
	private int cashierCash;
	
	public AnjaliCashierRole() {
		super();
		working = false;
		
		
	}
	public void setBankManager(BankManager bm){
		this.bankManager = bm;
	}

	public void setPerson(Person p){
		super.setPerson(p);
		name = person.getName();
	}
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	

	public void setWaiter(AnjaliWaiter waiter){
		this.waiter = waiter;
	}
	
	public void setCustomer(AnjaliCustomer customer){
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
		private class marketBill{
			MarketDeliverer deliverer;
			int charge;
			
			marketBill(MarketDeliverer d, int c){
				deliverer = d;
				charge = c;
			}
			
			public MarketDeliverer getDeliverer(){
				return deliverer;
			}
			
			public int getCharge(){
				return charge;
			}
		}
		
		public class bill{
			private AnjaliCustomer c;
			AnjaliWaiter w;
			String choice;
			int tableNumber;
			CashierState s;
			
		public bill(AnjaliCustomer customer, AnjaliWaiter waiter, String c, int tn, CashierState s){
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
		public AnjaliCustomer getC() {
			return c;
		}
		public void setC(AnjaliCustomer c) {
			this.c = c;
		}
		private double price = 0.00;
		double payment = 0.00;
		
		}
		public enum paymentState {nothing, receivedCheck, payingCheck, paidCheck};

		public class marketPayment{
			AnjaliMarket m;
			paymentState s;
			private double price;
			
		marketPayment(AnjaliMarket m, double p, paymentState s){
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
		public List<marketBill> marketBills = Collections.synchronizedList(new ArrayList<marketBill>());
		public List<marketPayment> marketPayments = Collections.synchronizedList(new ArrayList<marketPayment>());
		public double cash = 1000;
		private int surplus = (int)cash/4;
		
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
	public void msgStartShift(){
		working = true;
		stateChanged();
	}
	
	public void msgEndShift(){
		cashierCash -= person.getSalary();
		working = false;
		stateChanged();
	}
		
		public void msgMakeCheck(AnjaliCustomer c, String choice, int tableNumber, AnjaliWaiter w){
		//Receives order from waiter, creates check for that order	
			AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "received message from waiter to make check for " + c.getName());

		
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
	public void msgTakeMyMoney(AnjaliCustomer c, double p){
		
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
	
	
	public void msgDelivery(int bill, MarketDeliverer deliverer){
		marketBills.add(new marketBill(deliverer, bill));
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
		
		if(!working){
			leaveRestaurant();
			return true;
		}
		synchronized(marketBills){
			for(marketBill p : marketBills){
				payBill(p);
				return true;
				
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
		
	private void leaveRestaurant(){
		//person.msgMakeDeposit(surplus);
		person.msgLeftDestination(this);
	}
	
	public void giveMeMoney(bill b){
		AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Cashier is making check");

		
		for(Map.Entry<String, Double> entry : prices.entrySet()){
			if(entry.getKey() == b.choice){
				b.setPrice(entry.getValue());
				AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Cashier is giving check to waiter for amount " + b.getPrice());

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

	
	public void payBill(marketBill bill){

		cash -= bill.charge;
		AlertLog.getInstance().logMessage(AlertTag.ANJALI_RESTAURANT, name, "Cashier is paying market for order, now has $" + cash);

		bill.deliverer.msgPayment(this, bill.charge);
		marketBills.remove(bill);
	}

	@Override
	public void msgIHaveNoMoney(AnjaliCustomer c) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void msgThankYou(int change) {
		// TODO Auto-generated method stub
		
	}

	

	

	
	
	
	
}

	