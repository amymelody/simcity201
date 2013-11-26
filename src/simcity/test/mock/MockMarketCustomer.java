package simcity.test.mock;

import java.util.List;

import simcity.ItemOrder;
import simcity.interfaces.MarketCashier;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.MarketDeliverer;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;

public class MockMarketCustomer extends Mock implements MarketCustomer {

	/**
	 * References for unit testing
	 */
	public MarketCashier cashier;
	public MarketDeliverer deliverer;
	public List<ItemOrder> items;
	public String name;

	public MockMarketCustomer(String n) {
		super(n);

		name = n;
	}
	
	public void setItems(List<ItemOrder> i) {
		items = i;
	}

	@Override
	public void msgOrderItems(List<ItemOrder> i) {
		
	}
	
	@Override
	public void msgHereIsWhatICanFulfill(List<ItemOrder> orders, boolean canFulfill, int waitX, int waitY) {
		// TODO Auto-generated method stub
		if(canFulfill)
			log.add(new LoggedEvent("Confirmation of (at least partial) delivery"));
		else
			log.add(new LoggedEvent("Doesn't have items requested"));
	}

	@Override
	public void msgHereAreItemsandPrice(List<ItemOrder> i, int price) {
		if(items.equals(i)) {
			log.add(new LoggedEvent("Received correct items and payment"));
		}
		else {
			log.add(new LoggedEvent("Did not receive correct items"));
		}
		
	}

	@Override
	public void msgThankYou(int change) {
		log.add(new LoggedEvent("Received change"));
	}

	@Override
	public void msgOrderReady() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("My order is ready"));
	}
	
}
