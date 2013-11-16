package simcity.JesusRestaurant.gui;


import restaurant.CookAgent;
import restaurant.MarketAgent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class JesusMarketGui implements JesusGui {

	private MarketAgent agent = null;

	private int xPos = 440, yPos = 440;//default cook position
	private int xDestination = 440, yDestination = 440;//default cook destination

	public static final Map<Integer, Point> tableLocations = new HashMap<Integer, Point>();
	static {
		tableLocations.put(1, new Point(100, 100));
		tableLocations.put(2, new Point(250, 100));
		tableLocations.put(3, new Point(100, 250));
		tableLocations.put(4, new Point(250, 250));
	}

	Image cookImage;
	
	public JesusMarketGui(MarketAgent m) {
		this.agent = m;

		ImageIcon cookIcon = new ImageIcon(this.getClass().getResource("images/luigi.png"));
		cookImage = cookIcon.getImage();
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
		g.drawImage(cookImage, xPos, yPos, null);
	}

	public void updateInventory(Integer stI, Integer sI, Integer pI) {
		agent.updateInventory(stI, sI, pI);
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
