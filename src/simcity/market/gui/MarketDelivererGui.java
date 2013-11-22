package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import simcity.ItemOrder;
import simcity.gui.Gui;
import simcity.market.MarketDelivererRole;

public class MarketDelivererGui implements Gui {
	private MarketDelivererRole role = null;

	private int xPos = -20, yPos = -20;//default Deliverer position
	private int xDestination = -20, yDestination = -20;//default Deliverer destination
	private int xHome = -20, yHome = -20; // Deliverer home position
	
	public MarketDelivererGui(MarketDelivererRole r) {
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
		g.setColor(Color.GREEN);
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
	public void Deliver(String l) {
		
	}
	public void GoToCashier() {
		
	}
}