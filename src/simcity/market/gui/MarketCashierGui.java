package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.gui.Gui;
import simcity.market.MarketCashierRole;

public class MarketCashierGui implements Gui {
	private MarketCashierRole role = null;

	private int xPos = -1, yPos = 1; // initial Cashier position
	private int xDestination = -1, yDestination = 1;// initial Cashier destination
	private int xHome = 6, yHome = -1; // Cashier's Desk
	private int xLeave = -1, yLeave = 1; // Cashier's Leaving

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
	public void work() {
		xDestination = xHome;
		yDestination = yHome;
	}
	public void leave() {
		xDestination = xLeave;
		yDestination = yLeave;
	}
}
