package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import simcity.ItemOrder;
import simcity.gui.Gui;
import simcity.market.MarketEmployeeRole;

public class MarketEmployeeGui implements Gui {
	private MarketEmployeeRole role = null;

	private int xPos = -10, yPos = 10;//default Employee position
	private int xDestination = -10, yDestination = 10;//default Employee destination
	private int xHome = 190, yHome = -20; // Employee home position
	private List<ItemOrder> items;
	private Queue<Point> destinations = new LinkedList<Point>();
	private static Map<String, Point> foodLocations = new HashMap<String, Point>();
	static {
		foodLocations.put("Car", new Point(170, 10));
		foodLocations.put("Salad", new Point(170, 20));
		foodLocations.put("Steak", new Point(170, 40));
		foodLocations.put("Pizza", new Point(170, 50));
		foodLocations.put("Chicken", new Point(170, 70));
		foodLocations.put("Spaghetti", new Point(170, 80));
		foodLocations.put("Lasagna", new Point(170, 100));
		foodLocations.put("Garlic Bread", new Point(170, 110));
		foodLocations.put("Ribs", new Point(170, 130));
		foodLocations.put("Burger", new Point(170, 140));
		foodLocations.put("Enchiladas", new Point(170, 160));
		foodLocations.put("Tacos", new Point(170, 170));
		foodLocations.put("Pozole", new Point(170, 190));
		foodLocations.put("Horchata", new Point(170, 200));
	}
	public enum GuiState {nothing, gathering, cashier, leaving};
	GuiState gS = GuiState.nothing;
	
	public MarketEmployeeGui(MarketEmployeeRole r) {
		this.role = r;

	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		if(xPos == xDestination && yPos == yDestination) {
			if(gS == GuiState.gathering) {
				destinations.poll();
				if(destinations.size() != 0) {
					xDestination = destinations.peek().x;
					yDestination = destinations.peek().y;
				}
				else {
					GoToCashier();
				}
			}
			if(gS == GuiState.cashier) {
				role.msgAtCashier();
				xDestination = xHome;
				yDestination = yHome;
				gS = GuiState.nothing;
			}
			if(gS == GuiState.leaving) {
				role.left();
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillRect(20, 20, xPos, yPos);
	}

	public boolean isPresent() {
		return true;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
	
	
	/* Role Functions */
	public void GetItems(List<ItemOrder> i) {
		items = i;
		for(ItemOrder iO: i) {
			destinations.add(foodLocations.get(iO.getFoodItem()));
		}
		gS = GuiState.gathering;
		xDestination = destinations.peek().x;
		yDestination = destinations.peek().y;
	}
	public void GoToCashier() {
		xDestination = 70;
		yDestination = 10;
		gS = GuiState.cashier;
	}
	public void leave() {
		xDestination = -10;
		yDestination = 10;
		gS = GuiState.leaving;
	}
}