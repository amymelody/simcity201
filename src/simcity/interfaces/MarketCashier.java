package simcity.interfaces;

import java.util.*;
import simcity.ItemOrder;

public interface MarketCashier {
	
	public abstract String getName();
	
	public void msgIWantDelivery(RestCook c, List<ItemOrder> items, String location);
}