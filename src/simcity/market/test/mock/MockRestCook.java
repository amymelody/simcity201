package simcity.market.test.mock;

import java.util.List;

import simcity.ItemOrder;
import simcity.interfaces.RestCook;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.market.interfaces.MarketCustomer;
import simcity.market.interfaces.MarketDeliverer;
import simcity.market.interfaces.MarketEmployee;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockRestCook extends Mock implements RestCook {

	/**
	 * References for unit testing
	 */
	public MarketCustomer customer;
	public MarketDeliverer deliverer;
	public MarketEmployee employee;
	public String name;

	public MockRestCook(String n) {
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
	public void addMarket(simcity.interfaces.MarketCashier m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsOrder(JoshWaiterRole waiter, String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsWhatICanFulfill(List<ItemOrder> orders,
			boolean canFulfill) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDelivery(List<ItemOrder> orders) {
		// TODO Auto-generated method stub
		
	}

}