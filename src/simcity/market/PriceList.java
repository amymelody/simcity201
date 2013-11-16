package simcity.market;

import java.util.HashMap;
import java.util.Map;

public class PriceList {
	public static final Map<String, Integer> prices = new HashMap<String, Integer>();
	
	public PriceList() {
		prices.put("Car", 1000);
		prices.put("Salad", 3);
		prices.put("Steak", 10);
		prices.put("Pizza", 5);
		prices.put("Chicken", 10);
		prices.put("Spaghetti", 8);
		prices.put("Lasagna", 10);
		prices.put("Garlic Bread", 5);
		prices.put("Ribs", 10);
		prices.put("Burgers", 8);
		prices.put("Enchiladas", 10);
		prices.put("Tacos", 5);
		prices.put("Pozole", 8);
		prices.put("Horchata", 3);
	}
	
	public Integer getPrice(String foodItem) {
		return prices.get(foodItem);
	}
}
