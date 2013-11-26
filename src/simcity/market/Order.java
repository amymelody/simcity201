package simcity.market;

import java.util.List;

import simcity.ItemOrder;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.RestCashier;
import simcity.interfaces.RestCook;

public class Order {
	MarketCustomer customer;
	public RestCashier cashier;
	public RestCook cook;
	List<ItemOrder> items;
	List<ItemOrder> fulfilling;
	OrderState oS;
	public int price;
	public int amountPaid;
	public int change;
	public String location;
	
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
	
	public OrderState getOS() {
		return oS;
	}
	
	public enum OrderState {none, newDelivery, handing, getting, ready, paying, paid, done, delivered, here, know};
}
