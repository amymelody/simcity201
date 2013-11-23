package simcity.jesusrestaurant;

import java.util.ArrayList;

public class JesusMenu {
	private class menuItem {
		String name;
		double price;
		boolean inStock;
		
		menuItem (String n, double p) {
			name = n;
			price = p;
			inStock = true;
		}
		
		public String getName() {
			return name;
		}
		public double getPrice() {
			return price;
		}
	}
	
	ArrayList<menuItem> menu;
	
	JesusMenu() {
		menu = new ArrayList<menuItem>();
		menu.add(new menuItem("Steak", 15));
		menu.add(new menuItem("Pizza", 8));
		menu.add(new menuItem("Salad", 5));
	}
	
	public menuItem getMenuItem(String name) {
		for(menuItem m: menu) {
			if(m.name.equals(name)) {
				return m;
			}
		}
		return null;
	}
	public String getMenuItemName(String name) {
		for(menuItem m: menu) {
			if(m.name.equals(name)) {
				return m.getName();
			}
		}
		return null;
	}
	public String getMenuItemName(int loc) {
		return menu.get(loc).getName();
	}
	public double getMenuItemPrice(String name) {
		for(menuItem m: menu) {
			if(m.name.equals(name)) {
				return m.getPrice();
			}
		}
		return 0;
	}
	public boolean inStock(String name) {
		for(menuItem m: menu) {
			if(m.name.equals(name)) {
				return m.inStock;
			}
		}
		return false;
	}
	public void outOfStock(String name) {
		for(menuItem m: menu) {
			if(m.name.equals(name)) {
				m.inStock = false;
			}
		}
	}
	public void reStock(String name) {
		for(menuItem m: menu) {
			if(m.name.equals(name)) {
				m.inStock = true;
			}
		}
	}
	public boolean tooExpensive(double money) {
		for(menuItem m: menu) {
			if(m.inStock && m.price < money) {
				return false;
			}
		}
		return true;
	}
	public double priceCheapItem() {
		double currentPrice = 1000;
		for(menuItem m: menu) {
			if(getMenuItemPrice(m.name) < currentPrice) {
				currentPrice = getMenuItemPrice(m.name);
			}
		}
		return currentPrice;
	}
}
