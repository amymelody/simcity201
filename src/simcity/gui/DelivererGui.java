package simcity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simcity.ItemOrder;
import simcity.gui.Gui;
import simcity.market.MarketDelivererRole;
import simcity.market.gui.MarketDelivererGui;
import simcity.market.gui.MarketGui;

public class DelivererGui implements Gui {
	private MarketDelivererGui gui;
	private MarketGui mG;
	Point marketPnt, destPnt;
	
	private int xPos, yPos;//default Deliverer position
	private int xDestination, yDestination;//default Deliverer destination
	private int xHome, yHome; // Deliverer home poMap<K, V>ion
	private static Map<String, Point> locations = new HashMap<String, Point>();
	static {
		locations.put("market1", new Point(370, 280));
		locations.put("market2", new Point(370, 420));
		locations.put("joshRestaurant", new Point(280, 280));
		locations.put("AnjaliRestaurant", new Point(420, 280));
		locations.put("anjaliRestaurant", new Point(420, 420));
		locations.put("jesusRestaurant", new Point(280, 420));
	}
	
	public enum GuiState {nothing, delivering, cashier}
	public GuiState gS = GuiState.nothing;
	
	public DelivererGui(MarketDelivererGui g, String nameOfMarket) {
		this.gui = g;
	
		marketPnt = locations.get(nameOfMarket);
		xPos = 520;
		yPos = marketPnt.y;
		xDestination = xPos;
		yDestination = yPos;
		xHome = marketPnt.x;
		yHome = marketPnt.y;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos+=10;
		else if (xPos > xDestination)
			xPos-=10;

		if (yPos < yDestination)
			yPos+=10;
		else if (yPos > yDestination)
			yPos-=10;
		if(xPos == xDestination && yPos == yDestination) {
			if(gS == GuiState.delivering) {
				gui.Outside();
				xPos = 520;
				xDestination=520;
				gS = GuiState.nothing;
			}
			if(gS == GuiState.cashier) {
				gui.Inside();
				xPos = 520;
				xDestination = 520;
				gS = GuiState.nothing;
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 10, 10);
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
	public void deliver(String l) {
		xPos = marketPnt.x;
		yPos = marketPnt.y;
		destPnt = locations.get(l);
		xDestination = destPnt.x;
		yDestination = destPnt.y;
		gS = GuiState.delivering;
	}
	public void goBack() {
		xPos = destPnt.x;
		yPos = destPnt.y;
		xDestination = xHome;
		yDestination = yHome;
		gS = GuiState.cashier;
	}
}