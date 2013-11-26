package simcity..gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import simcity.ItemOrder;
import simcity.gui.Gui;
import simcity.market.MarketDelivererRole;
import simcity.market.gui.MarketEmployeeGui.GuiState;

public class DelivererGui implements Gui {
	private DelivererGui gui = null;

	private int xPos = -10, yPos = 10;//default Deliverer position
	private int xDestination = 200, yDestination = 20;//default Deliverer destination
	private int xHome = 200, yHome = 20; // Deliverer home position
	
	public enum GuiState {nothing, delivering, cashier, leaving}
	public GuiState gS = GuiState.nothing;
	
	public DelivererGui(DelivererGui g) {
		this.gui = g;

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
			if(gS == GuiState.delivering) {
				
				gS = GuiState.nothing;
			}
			if(gS == GuiState.cashier) {
				role.msgArrivedBack();
				xDestination = xHome;
				yDestination = yHome;
				gS = GuiState.nothing;
			}
			if(gS == GuiState.leaving) {
			}
		}
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