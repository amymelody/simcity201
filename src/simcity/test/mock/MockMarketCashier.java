package simcity.test.mock;

import java.util.List;

import simcity.ItemOrder;
import simcity.interfaces.RestCashier;
import simcity.interfaces.RestCook;
import simcity.market.Order;
import simcity.market.interfaces.MarketCashier;
import simcity.market.interfaces.MarketCustomer;
import simcity.market.interfaces.MarketDeliverer;
import simcity.market.interfaces.MarketEmployee;
import simcity.RestCashierRole;
import simcity.RestCookRole;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHired(MarketDeliverer d, int salary) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOntheClock(MarketEmployee e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOntheClock(MarketDeliverer d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOfftheClock(MarketEmployee e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOfftheClock(MarketDeliverer d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneForTheDay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWereOpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantItems(MarketCustomer c, List<ItemOrder> i) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received order"));
	}

	@Override
	public void msgHereAreItems(Order order, MarketEmployee e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayment(MarketCustomer c, int money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantDelivery(RestCook rCk, RestCashier rCh,
			List<ItemOrder> i, String location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDelivered(Order order, MarketDeliverer d) {
		// TODO Auto-generated method stub

	}

}