package simcity.market.test.mock;

import java.util.List;

import simcity.ItemOrder;
import simcity.market.Order;
import simcity.market.interfaces.MarketCashier;
import simcity.market.interfaces.MarketCustomer;
import simcity.market.interfaces.MarketDeliverer;
import simcity.market.interfaces.MarketEmployee;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockMarketCashier extends Mock implements MarketCashier {

	/**
	 * References for unit testing
	 */
	public MarketCustomer customer;
	public MarketDeliverer deliverer;
	public MarketEmployee employee;
	public String name;

	public MockMarketCashier(String n) {
		super(n);

		name = n;
	}

	@Override
	public void msgHired(MarketEmployee e, int salary) {
		log.add(new LoggedEvent("Hiring employee."));
		
	}

	@Override
	public void msgHired(MarketDeliverer d, int salary) {
		log.add(new LoggedEvent("Hiring deliverer."));
		
	}

	@Override
	public void msgOntheClock(MarketEmployee e) {
		log.add(new LoggedEvent("Employee on the clock."));
		
	}

	@Override
	public void msgOntheClock(MarketDeliverer d) {
		log.add(new LoggedEvent("Deliverer on the clock."));
		
	}

	@Override
	public void msgOfftheClock(MarketEmployee e) {
		log.add(new LoggedEvent("Employee off the clock."));
		
	}

	@Override
	public void msgOfftheClock(MarketDeliverer d) {
		log.add(new LoggedEvent("Deliverer off the clock."));
		
	}

	@Override
	public void msgDoneForTheDay() {
		log.add(new LoggedEvent("Closing"));
		
	}

	@Override
	public void msgWereOpen() {
		log.add(new LoggedEvent("We're open."));
		
	}
	
	
	@Override
	public void msgIWantItems(MarketCustomer c, List<ItemOrder> items) {
		log.add(new LoggedEvent("Received customer message for order."));
		
	}

	@Override
	public void msgHereAreItems(Order order, MarketEmployee e) {
		log.add(new LoggedEvent("Handed order to employee."));
		
	}

	@Override
	public void msgPayment(MarketCustomer c, int money) {
		log.add(new LoggedEvent("Customer paid."));
		
	}

	@Override
	public void msgIWantDelivery(MarketCustomer c, List<ItemOrder> i, String location) {
		log.add(new LoggedEvent("Received customer message for delivery."));
		
	}

	@Override
	public void msgDelivered(Order order, MarketDeliverer d) {
		log.add(new LoggedEvent("Customer paid for delivery."));
		
	}

}
