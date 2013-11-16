package simcity.market;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class MarketItem {
	String name;
	int price;
	int amount;
	
	public MarketItem(String n, int p, int a) {
		name = n;
		price = p;
		amount = a;
	}
	public int getPrice() {
		return price;
	}
	public int getAmount() {
		return amount;
	}
	public void subtractAmount(int subtracted) {
		amount -= subtracted;
	}
	public void addAmount(int added) {
		amount += added;
	}
	public void setAmount(int a) {
		amount = a;
	}
}

public class InventoryList {
	public static final Map<String, MarketItem> inventory = new HashMap<String, MarketItem>();
	
	public InventoryList() {
		inventory.put("Car", new MarketItem("Car", 1000, 0));
		inventory.put("Salad", new MarketItem("Salad", 3, 0));
		inventory.put("Steak", new MarketItem("Steak", 10, 0));
		inventory.put("Pizza", new MarketItem("Pizza", 5, 0));
		inventory.put("Chicken", new MarketItem("Chicken", 10, 0));
		inventory.put("Spaghetti", new MarketItem("Spaghetti", 8, 0));
		inventory.put("Lasagna", new MarketItem("Lasagna", 10, 0));
		inventory.put("Garlic Bread", new MarketItem("Garlic Bread", 5, 0));
		inventory.put("Ribs", new MarketItem("Ribs", 10, 0));
		inventory.put("Burgers", new MarketItem("Burgers", 8, 0));
		inventory.put("Enchiladas", new MarketItem("Enchiladas", 10, 0));
		inventory.put("Tacos", new MarketItem("Tacos", 5, 0));
		inventory.put("Pozole", new MarketItem("Pozole", 8, 0));
		inventory.put("Horchata", new MarketItem("Horchata", 3, 0));
	}
	
	public Integer getPrice(String foodItem) {
		return inventory.get(foodItem).getPrice();
	}
	public Integer getAmount(String foodItem) {
		return inventory.get(foodItem).getAmount();
	}
	public void updateAmount(String foodItem, int a, boolean add) {
		if(add) {
			inventory.get(foodItem).addAmount(a);
		}
		else {
			inventory.get(foodItem).subtractAmount(a);
		}
	}
	public void setAmount(String foodItem, int a) {
		inventory.get(foodItem).setAmount(a);
	}
	public void opening() {
		for(MarketItem mI: inventory.values()) {
			mI.addAmount(10);
		}
	}
}
