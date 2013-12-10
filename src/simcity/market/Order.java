package simcity.market;

import java.util.List;

import simcity.ItemOrder;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.RestCashier;
import simcity.interfaces.RestCook;

public class Order {
	public MarketCustomer customer;
	public RestCashier cashier;
	public RestCook cook;
	public List<ItemOrder> items;
	public List<ItemOrder> fulfilling;
	OrderState oS;
	public int price;
	public int amountPaid;
	public int change;
	public String location;
	public boolean complete = false;
	
	public Order(MarketCustomer c, List<ItemOrder> i) {
		customer = c;
		items = i;
		oS = OrderState.none;
		price = 0;
		amountPaid = 0;
		change = 0;
		location = null;
	}
	
	public Order(RestCook ck, RestCashier ch, List<ItemOrder> i, String l) {
		customer = null;
		cashier = ch;
		cook = ck;
		items = i;
		oS = OrderState.newDelivery;
		price = 0;
		amountPaid = 0;
		change = 0;
		location = l;
	}
	
	public Order(Order o) {
		customer = o.customer;
		cashier = o.cashier;
		cook = o.cook;
		items = o.items;
		oS = o.oS;
		price = o.price;
		amountPaid = o.amountPaid;
		change = o.change;
		location = o.location;
	}
	
	public OrderState getOS() {
		return oS;
	}
	
	public enum OrderState {none, newDelivery, handing, getting, ready, paying, paid, done, delivered, here, know, needToComplete};
}
