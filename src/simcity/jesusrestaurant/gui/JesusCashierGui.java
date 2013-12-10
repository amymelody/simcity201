package simcity.jesusrestaurant.gui;


import simcity.gui.Gui;
import simcity.jesusrestaurant.JesusCustomerRole;
import simcity.jesusrestaurant.JesusCashierRole;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class JesusCashierGui implements Gui {

	private JesusCashierRole role = null;

	private int xPos = -20, yPos = 20; // default cashier position
	private int xDestination = -20, yDestination = 20; // default cashier destination
	private int xHome = 0, yHome = 80;
	private boolean leave = false;
	
	public static final Map<Integer, Point> tableLocations = new HashMap<Integer, Point>();
	static {
		tableLocations.put(1, new Point(100, 50));
		tableLocations.put(2, new Point(300, 50));
		tableLocations.put(3, new Point(100, 200));
		tableLocations.put(4, new Point(300, 200));
	}

	Image hostImage;
	public JesusCashierGui(JesusCashierRole role) {
		this.role = role;

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

		if (xPos == xDestination && yPos == yDestination)
			if(leave) {
				role.left();
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
	
	public void work() {
		xDestination = xHome;
		yDestination = yHome;	
	}
	public void leave() {
		xDestination = -20;
		yDestination = 20;
		leave = true;
	}
}
