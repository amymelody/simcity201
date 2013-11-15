package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.market.CustomerRole;

public class CustomerGui {
	private CustomerRole role = null;

	private int xPos = -20, yPos = -20;//default Customer position
	private int xDestination = -20, yDestination = -20;//default Customer destination

	public CustomerGui(CustomerRole r) {
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
		g.setColor(Color.BLUE);
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
}