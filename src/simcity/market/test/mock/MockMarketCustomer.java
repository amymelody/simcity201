package simcity.market.test.mock;

import java.util.List;

import simcity.ItemOrder;
import simcity.market.interfaces.MarketCashier;
import simcity.market.interfaces.MarketCustomer;
import simcity.market.interfaces.MarketDeliverer;
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
	public void msgOrderItems(List<ItemOrder> i, boolean d, String l) {
		
	}

	@Override
	public void msgHereAreItemsandPrice(List<ItemOrder> i, int price) {
		if(items.equals(i)) {
			log.add(new LoggedEvent("Received correct items and payment"));
		}
		else {
			log.add(new LoggedEvent("Did not receive correct items."));
		}
		
	}

	@Override
	public void msgThankYou(int change) {
		log.add(new LoggedEvent("Received change"));
	}
	
}
