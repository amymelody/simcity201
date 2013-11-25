package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.gui.Gui;
import simcity.market.MarketCashierRole;

public class MarketCashierGui implements Gui {
	private MarketCashierRole role = null;

	private int xPos = -20, yPos = -20; // initial Cashier position
	private int xDestination = -20, yDestination = -20;// initial Cashier destination
	private int xHome = -20, yHome = -20; // Cashier home position

	public MarketCashierGui(MarketCashierRole r) {
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
		g.setColor(Color.RED);
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
	public void leave() {
		
	}
}
