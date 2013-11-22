package simcity.alfredrestaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import simcity.alfredrestaurant.AlfredCustomerRole;
import simcity.alfredrestaurant.AlfredTable;
import simcity.alfredrestaurant.AlfredWaiterRole;

public class AlfredWaiterGui implements AlfredGui {

	private AlfredWaiterRole agent = null;
	private int xPos = -50, yPos = -50;// default waiter position
	private int xDestination = -50, yDestination = -50;// default start position
			// public static int xTable = 200;
			// public static int yTable = 250;
	public static final int sizeHost = 50;
	public static final int sizeTable = 50;
	private static final int defaultPosition = -50;
	private final String IMAGE_FILE = "C:/Users/AlfredKoshan/Documents/GitHub/restaurant_akoshan/host.jpg";

	public AlfredWaiterGui(AlfredWaiterRole agent) {
		System.out.println("WaiterGui has been created");
		this.agent = agent;
		try {
			image = ImageIO.read(new File(IMAGE_FILE));
		} catch (IOException ex) {
			// handle exception...
		}
	}

	private enum Command {

		noCommand, GoToSeat, ComeToPlatingArea, BringFood, BringNoFood, LeavingDone
	};

	private Command command = Command.noCommand;

	public void updatePosition() {
		AlfredCustomerRole cust = null;
		Point tablePos = null;
		AlfredTable table = null;
		if (agent.getCurrentCustomer() != null) {
			cust = agent.getCurrentCustomer();
			table = cust.getTable();
			if (cust.getTable() != null)
				tablePos = cust.getTable().getPosition();
		} else {
			xDestination = defaultPosition;
			yDestination = defaultPosition;
		}

		// System.out.println(xPos + "," + yPos);
		// System.out.println(xDestination + "," + yDestination);

		if (xPos < xDestination) {
			xPos++;
		} else if (xPos > xDestination) {
			xPos--;
		}

		if (yPos < yDestination) {
			yPos++;
		} else if (yPos > yDestination) {
			yPos--;
		}

		if (tablePos != null && xPos == xDestination && yPos == yDestination
				& (xDestination == tablePos.x + sizeHost)
				& (yDestination == tablePos.y - sizeHost)) {
			if (command == Command.GoToSeat) {
				// agent.msgAtTable();
			} else if (command == Command.BringFood) {
				agent.msgDoneBring();
				command = Command.noCommand;
			} else if (command == Command.BringNoFood) {
				agent.askForNewOrder();
			} else {
				DoLeaveCustomer();
			}
		}
		
		if (xPos == xDestination && yPos == yDestination && command == Command.LeavingDone){
			agent.msgDoneLeaving();
			command = Command.noCommand;
		}
		
		if (xPos == xDestination && yPos == yDestination && command == Command.ComeToPlatingArea){
			agent.msgComeToPlatingArea();
		}
	}

	private BufferedImage image;

	public void draw(Graphics2D g) {
		g.setColor(Color.MAGENTA);
		g.drawImage(image, xPos, yPos, null);
		if (agent.wantingToGoOnBreak){
			g.drawString("T", xPos, yPos);
		}
		g.setColor(Color.BLACK);
		g.drawString("(" + agent.getIndex() + ")", xPos, yPos + 10);
		// g.fillRect(xPos, yPos, sizeHost, sizeHost);
	}

	public boolean isPresent() {
		return true;
	}
	
	public void DoGoToPlatingArea(){
		command = Command.ComeToPlatingArea;
	}

	public void DoBringToTable(AlfredCustomerRole customer, AlfredTable table) {
		Point tablePos = table.getPosition();
		xDestination = tablePos.x + sizeHost;
		yDestination = tablePos.y - sizeHost;
		System.out.println(xDestination + "," + yDestination);
		command = Command.BringFood;
	}
	public void DoLeaving(){
		xDestination = defaultPosition;
		yDestination = defaultPosition;
		command = Command.LeavingDone;
	}
	
	public void DoBringNoFoodToTable(AlfredCustomerRole customer, AlfredTable table) {
		Point tablePos = table.getPosition();
		xDestination = tablePos.x + sizeHost;
		yDestination = tablePos.y - sizeHost;
		System.out.println(xDestination + "," + yDestination);
		command = Command.BringNoFood;
	}	
	

	public void sitCustomerToTable(AlfredCustomerRole customer, AlfredTable table) {
		Point tablePos = table.getPosition();
		xDestination = tablePos.x + sizeHost;
		yDestination = tablePos.y - sizeHost;
		System.out.println(xDestination + "," + yDestination);
		command = Command.GoToSeat;
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

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	

}
