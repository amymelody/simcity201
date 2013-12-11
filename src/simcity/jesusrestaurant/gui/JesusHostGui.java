package simcity.jesusrestaurant.gui;


import simcity.gui.Gui;
import simcity.jesusrestaurant.JesusHostRole;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class JesusHostGui implements Gui {

	private JesusHostRole role = null;

	private int xPos = -20, yPos = 20;//default host position
	private int xDestination = -20, yDestination = 20;//default host destination
	private int xHome = 80, yHome = 80;
	private boolean leaving = false;
	
	public static final Map<Integer, Point> tableLocations = new HashMap<Integer, Point>();
	static {
		tableLocations.put(1, new Point(40, 160));
		tableLocations.put(2, new Point(200, 160));
		tableLocations.put(3, new Point(40, 320));
		tableLocations.put(4, new Point(200, 320));
	}

	Image hostImage;
	public JesusHostGui(JesusHostRole r) {
		this.role = r;

		ImageIcon hostIcon = new ImageIcon(this.getClass().getResource("images/mario.png"));
		hostImage = hostIcon.getImage();
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos+=20;
		else if (xPos > xDestination)
			xPos-=20;

		else if (yPos < yDestination)
			yPos+=20;
		else if (yPos > yDestination)
			yPos-=20;

		if (xPos == xDestination && yPos == yDestination) {
			if(xDestination == xHome && yDestination == yHome) {
				
			}
			else if(xDestination == -20 && yDestination == 20 && leaving) {
				role.left();
				leaving = false;
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

	public void work() {
		xDestination = xHome;
		yDestination = yHome;
	}
	public void  leave() {
		xDestination = -20;
		yDestination = 20;
		leaving = true;
	}
	public static int getNTab() {
		return JesusHostRole.getNTables();
	}
}
