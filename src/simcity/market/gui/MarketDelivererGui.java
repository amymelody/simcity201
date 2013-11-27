package simcity.market.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import simcity.ItemOrder;
import simcity.gui.DelivererGui;
import simcity.gui.Gui;
import simcity.market.MarketDelivererRole;
import simcity.market.gui.MarketEmployeeGui.GuiState;

public class MarketDelivererGui implements Gui {
	private MarketDelivererRole role = null;
	private String location;

	private int xPos = -20, yPos = 20;//default Deliverer position
	private int xDestination = -20, yDestination = 20;//default Deliverer destination
	private int xHome = 400, yHome = 20; // Deliverer home position
	
	public enum GuiState {nothing, delivering, cashier, leaving}
	public GuiState gS = GuiState.nothing;
	
	DelivererGui dG;
	
	public MarketDelivererGui(MarketDelivererRole r) {
		this.role = r;

	}
	public void setDelivererGui(DelivererGui g) {
		dG = g;
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
				dG.deliver(location);
				gS = GuiState.nothing;
			}
			if(gS == GuiState.cashier) {
				role.msgArrivedBack();
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
		g.setColor(Color.GREEN);
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
	public void Deliver(String l) {
		location = l;
		xDestination = 520;
		yDestination = 20;
		gS = GuiState.delivering;
	}
	public void Outside() {
		role.msgArrived();
	}
	public void GoToCashier() {
		dG.goBack();
	}
	public void Inside() {
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