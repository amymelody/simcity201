package simcity.market;

import java.util.List;

import simcity.ItemOrder;
import simcity.market.interfaces.MarketCustomer;

public class Order {
	MarketCustomer customer;
	List<ItemOrder> items;
	OrderState oS;
	int price;
	int amountPaid;
	int change;
	String location;
	
	Order(MarketCustomer c, List<ItemOrder> i) {
		customer = c;
		items = i;
		oS = OrderState.none;
		price = 0;
		amountPaid = 0;
		change = 0;
		location = null;
	}
	
	Order(MarketCustomer c, List<ItemOrder> i, String l) {
		customer = c;
		items = i;
		oS = OrderState.none;
		price = 0;
		amountPaid = 0;
		change = 0;
		location = l;
	}
	
	public OrderState getOS() {
		return oS;
	}
	
	public enum OrderState {none, newDelivery, handing, getting, ready, paying, paid, done, delivered};
}
