package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import simcity.ItemOrder;
import simcity.gui.Gui;
import simcity.market.MarketDelivererRole;

public class MarketDelivererGui implements Gui {
	private MarketDelivererRole role = null;

	private int xPos = -10, yPos = 10;//default Deliverer position
	private int xDestination = 200, yDestination = 20;//default Deliverer destination
	private int xHome = 200, yHome = 20; // Deliverer home position
	
	public enum GuiState {nothing, delivering, cashier, leaving}
	public GuiState gS = GuiState.nothing;
	
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
		xDestination = 510;
		yDestination = 20;
		gS = GuiState.delivering;
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