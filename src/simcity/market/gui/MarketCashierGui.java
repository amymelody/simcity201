package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import simcity.gui.Gui;
import simcity.market.MarketCashierRole;

public class MarketCashierGui implements Gui {
	private MarketCashierRole role = null;

	private int xPos = -10, yPos = 10; // initial Cashier position
	private int xDestination = -10, yDestination = 10;// initial Cashier destination
	private int xHome = 60, yHome = 10; // Cashier's Desk
	private int xLeave = -10, yLeave = 10; // Cashier's Leaving
	boolean leaving = false;

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
		if(xPos == -10 && yPos == 10 && leaving) {
			role.left();
			leaving = false;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
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
	public void work() {
		xDestination = xHome;
		yDestination = yHome;
	}
	public void leave() {
		xDestination = xLeave;
		yDestination = yLeave;
		leaving = true;
	}
}
