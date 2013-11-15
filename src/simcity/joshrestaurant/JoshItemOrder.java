package simcity.joshrestaurant;

import java.util.*;

public class JoshItemOrder {
	private String food;
	private int amount;
	
	JoshItemOrder(String f, int a) {
		food = f;
		amount = a;
	}
	
	public String getFood() {
		return food;
	}
	
	public int getAmount() {
		return amount;
	}
}
