package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import simcity.gui.Gui;
import simcity.market.MarketCustomerRole;

public class MarketCustomerGui implements Gui {
	private MarketCustomerRole role = null;

	private int xPos = -20, yPos = 20;//default Customer position
	private int xDestination = -20, yDestination = 20;//default Customer destination
	private int xHome = 20, yHome = 20; // Customer home position

	public enum GuiState {nothing, toCashier, toWaiting, pickingUp, exiting};
	GuiState gS = GuiState.nothing;
	
	public MarketCustomerGui(MarketCustomerRole r) {
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
			if(xDestination == 80 && yDestination == 20) {
				if(gS == GuiState.toCashier) {
					role.msgAtCashier();
					gS = GuiState.nothing;
				}
				else if(gS == GuiState.pickingUp) {
					role.msgAtPickUp();
					gS = GuiState.nothing;
				}
			}
			else if(xDestination == -20 && yDestination == 20 && gS == GuiState.exiting) {
				role.msgOut();
				gS = GuiState.nothing;
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
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
	public void GoToCashier() {
		xDestination = 80;
		yDestination = 20;
		gS = GuiState.toCashier;
		
	}
	public void GoToWaitingArea(int wX, int wY) {
		xDestination = wX;
		yDestination = wY;
		xHome = wX;
		yHome = wY;
		gS = GuiState.toWaiting;
	}
	public void PickUpItems() {
		xDestination = 80;
		yDestination = 20;
		gS = GuiState.pickingUp;
	}
	public void ExitMarket() {
		xDestination = -20;
		yDestination = 20;
		gS = GuiState.exiting;
	}
}