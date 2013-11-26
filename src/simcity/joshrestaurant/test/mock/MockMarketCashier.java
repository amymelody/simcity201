package simcity.joshrestaurant.test.mock;

import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketDeliverer;
import simcity.interfaces.MarketEmployee;
import simcity.interfaces.RestCook;
import simcity.interfaces.RestCashier;
import simcity.market.Order;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

import java.util.*;

import simcity.ItemOrder;

public class MockMarketCashier extends Mock implements MarketCashier {

	public MockMarketCashier(String name) {
		super(name);
	}
	
	public void msgIWantDelivery(RestCook c, RestCashier ca, List<ItemOrder> items, String location) {
		log.add(new LoggedEvent("Received msgIWantDelivery from cook. Location = " + location));
	}
	
	public String toString() {
		return getName();
	}
	
	public void msgHired(MarketEmployee e, int salary) {};
	
	public void msgHired(MarketDeliverer d, int salary) {};
	
	public void msgOntheClock(MarketEmployee e) {};
	
	public void msgOntheClock(MarketDeliverer d) {};
	
	public void msgOfftheClock(MarketEmployee e) {};
	
	public void msgOfftheClock(MarketDeliverer d) {};
	
	public void msgDoneForTheDay() {};
	
	public void msgWereOpen() {};
	
	public void msgIWantItems(MarketCustomer c, List<ItemOrder> i) {};
	
	public void msgImHere(MarketCustomer c) {};
	
	public void msgHereAreItems(Order order, MarketEmployee e) {};
	
	public void msgPayment(MarketCustomer c, int money) {};
	
	public void msgDelivered(Order order, MarketDeliverer d) {};
	
}
