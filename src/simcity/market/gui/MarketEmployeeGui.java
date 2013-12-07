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

	private int xPos = -20, yPos = 20;//default Employee position
	private int xDestination = -20, yDestination = 20;//default Employee destination
	private int xHome = 380, yHome = 20; // Employee home position
	private List<ItemOrder> items;
	private Queue<Point> destinations = new LinkedList<Point>();
	private static Map<String, Point> foodLocations = new HashMap<String, Point>();
	static {
		foodLocations.put("Car", new Point(340, 20));
		foodLocations.put("Salad", new Point(340, 40));
		foodLocations.put("Steak", new Point(340, 80));
		foodLocations.put("Pizza", new Point(340, 100));
		foodLocations.put("Chicken", new Point(340, 140));
		foodLocations.put("Spaghetti", new Point(340, 160));
		foodLocations.put("Lasagna", new Point(340, 200));
		foodLocations.put("Garlic Bread", new Point(340, 220));
		foodLocations.put("Ribs", new Point(340, 260));
		foodLocations.put("Burger", new Point(340, 280));
		foodLocations.put("Enchiladas", new Point(340, 320));
		foodLocations.put("Tacos", new Point(340, 340));
		foodLocations.put("Pozole", new Point(340, 380));
		foodLocations.put("Horchata", new Point(340, 400));
	}
	public enum GuiState {nothing, gathering, cashier, leaving};
	GuiState gS = GuiState.nothing;
	
	public MarketEmployeeGui(MarketEmployeeRole r) {
		this.role = r;

	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos+=20;
		else if (xPos > xDestination)
			xPos-=20;

		if (yPos < yDestination)
			yPos+=20;
		else if (yPos > yDestination)
			yPos-=20;
		if(xPos == xDestination && yPos == yDestination) {
			if(gS == GuiState.gathering) {
				destinations.poll();
				if(destinations.size() != 0) {
					xDestination = destinations.peek().x;
					yDestination = destinations.peek().y;
				}
				else {
					role.msgHaveItems();
					gS = GuiState.nothing;
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
		g.fillRect(xPos, yPos, 20, 20);
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
	public void work() {
		xDestination = xHome;
		yDestination = yHome;
	}
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
		xDestination = 140;
		yDestination = 20;
		gS = GuiState.cashier;
	}
	public void leave() {
		xDestination = -20;
		yDestination = 20;
		gS = GuiState.leaving;
	}
}