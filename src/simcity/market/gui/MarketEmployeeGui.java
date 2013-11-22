package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import simcity.ItemOrder;
import simcity.gui.Gui;
import simcity.market.MarketEmployeeRole;

public class MarketEmployeeGui implements Gui {
	private MarketEmployeeRole role = null;

	private int xPos = -20, yPos = -20;//default Employee position
	private int xDestination = -20, yDestination = -20;//default Employee destination
	private int xHome = -20, yHome = -20; // Employee home position

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
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
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
		
	}
	public void GoToCashier() {
		
	}
}