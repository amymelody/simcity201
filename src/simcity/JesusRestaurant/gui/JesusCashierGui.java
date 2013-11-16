package simcity.JesusRestaurant.gui;


import simcity.cherysrestaurant.CashierAgent;
import simcity.cherysrestaurant.CustomerAgent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class JesusCashierGui implements JesusGui {

	private CashierAgent agent = null;

	private int xPos = 0, yPos = 80;//default host position
	private int xDestination = 0, yDestination = 80;//default host destination

	public static final Map<Integer, Point> tableLocations = new HashMap<Integer, Point>();
	static {
		tableLocations.put(1, new Point(100, 50));
		tableLocations.put(2, new Point(300, 50));
		tableLocations.put(3, new Point(100, 200));
		tableLocations.put(4, new Point(300, 200));
	}

	Image hostImage;
	public JesusCashierGui(CashierAgent agent) {
		this.agent = agent;

		ImageIcon hostIcon = new ImageIcon(this.getClass().getResource("images/mario.png"));
		hostImage = hostIcon.getImage();
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

		for(Point p : tableLocations.values()) {
		if (xPos == xDestination && yPos == yDestination
				& ((xDestination == p.x + 20 && yDestination == p.y - 20))) {
		}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.drawImage(hostImage, xPos, yPos, null);
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
