package simcity.joshrestaurant.test.mock;

import simcity.interfaces.MarketCashier;
import simcity.interfaces.RestCook;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;
import java.util.*;
import simcity.ItemOrder;

public class MockMarketCashier extends Mock implements MarketCashier {

	public MockMarketCashier(String name) {
		super(name);
	}
	
	@Override
	public void msgIWantDelivery(RestCook c, List<ItemOrder> items, String location) {
		log.add(new LoggedEvent("Received msgIWantDelivery from cook. Location = " + location));
	}
	
	public String toString() {
		return getName();
	}
	
}
