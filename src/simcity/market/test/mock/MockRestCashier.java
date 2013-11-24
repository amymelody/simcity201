package simcity.market.test.mock;

import simcity.interfaces.RestCashier;
import simcity.joshrestaurant.interfaces.JoshCustomer;
import simcity.joshrestaurant.interfaces.JoshWaiter;
import simcity.market.interfaces.MarketCustomer;
import simcity.market.interfaces.MarketDeliverer;
import simcity.market.interfaces.MarketEmployee;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockRestCashier extends Mock implements RestCashier {

	/**
	 * References for unit testing
	 */
	public MarketCustomer customer;
	public MarketDeliverer deliverer;
	public MarketEmployee employee;
	public String name;

	public MockRestCashier(String n) {
		super(n);

		name = n;
	}

	@Override
	public void msgStartShift() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgEndShift() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgProduceCheck(JoshWaiter w, JoshCustomer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPayment(JoshCustomer c, int cash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDelivery(int bill,
			simcity.interfaces.MarketDeliverer deliverer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgThankYou(int change) {
		// TODO Auto-generated method stub
		
	}

}