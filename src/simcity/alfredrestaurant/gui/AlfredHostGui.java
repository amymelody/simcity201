package simcity.alfredrestaurant.gui;

import simcity.alfredrestaurant.AlfredCustomerRole;
import simcity.alfredrestaurant.AlfredHostRole;
import simcity.alfredrestaurant.AlfredTable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AlfredHostGui implements AlfredGui{

	private AlfredHostRole agent = null;
	private int xPos = -50, yPos = -50;// default waiter position
	private int xDestination = -50, yDestination = -50;// default start position
			// public static int xTable = 200;
			// public static int yTable = 250;
	public static final int sizeHost = 50;
	public static final int sizeTable = 50;
	private static final int defaultPosition = -50;
	private final String IMAGE_FILE = "C:/Users/AlfredKoshan/Documents/GitHub/restaurant_akoshan/host.jpg";

	public AlfredHostGui(AlfredHostRole agent) {
		this.agent = agent;
		try {
			image = ImageIO.read(new File(IMAGE_FILE));
		} catch (IOException ex) {
			// handle exception...
		}
	}

	public void updatePosition() {
//		CustomerAgent cust = null;
//		Point tablePos = null;
//		Table table = null;
//		if (agent.getWaitingCustomersSize() > 0) {
//			cust = (CustomerAgent) agent.getWaitingCustomers().get(0);
//			table = cust.getTable();
//			if (cust.getTable() != null)
//				tablePos = cust.getTable().getPosition();
//		} else {
//			xDestination = defaultPosition;
//			yDestination = defaultPosition;
//		}
//
//		if (xPos < xDestination) {
//			xPos++;
//		} else if (xPos > xDestination) {
//			xPos--;
//		}
//
//		if (yPos < yDestination) {
//			yPos++;
//		} else if (yPos > yDestination) {
//			yPos--;
//		}
//
//		if (tablePos != null && xPos == xDestination && yPos == yDestination
//				& (xDestination == tablePos.x + sizeHost)
//				& (yDestination == tablePos.y - sizeHost)) {
//			agent.msgAtTable(table);
//		}
	}

	private BufferedImage image;

	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.drawImage(image, xPos, yPos, null);
		// g.fillRect(xPos, yPos, sizeHost, sizeHost);
	}

	public boolean isPresent() {
		return true;
	}

	public void DoBringToTable(AlfredCustomerRole customer, int tableNumber) {
		Point tablePos = agent.getTablePosition(tableNumber);
		xDestination = tablePos.x + sizeHost;
		yDestination = tablePos.y - sizeHost;
	}

	public void DoLeaveCustomer() {
		xDestination = defaultPosition;
		yDestination = defaultPosition;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}
