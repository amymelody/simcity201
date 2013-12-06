package simcity.test.mock;

import java.util.List;

import simcity.ItemOrder;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketDeliverer;
import simcity.interfaces.RestCook;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockRestCook extends Mock implements RestCook {

	/**
	 * References for unit testing
	 */
	public MarketCashier cashier;
	public MarketDeliverer deliverer;
	public List<ItemOrder> items;
	public String name;

	public MockRestCook(String n) {
		super(n);

		name = n;
	}

	public void setItems(List<ItemOrder> i) {
		items = i;
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
	public void addMarket(simcity.interfaces.MarketCashier m, String n) {
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
		if(canFulfill)
			log.add(new LoggedEvent("Confirmation of (at least partial) delivery"));
		else
			log.add(new LoggedEvent("Doesn't have items requested"));
	}

	@Override
	public void msgDelivery(List<ItemOrder> orders) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received delivery"));
	}

}