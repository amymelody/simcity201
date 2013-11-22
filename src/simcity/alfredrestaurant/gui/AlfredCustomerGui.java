package simcity.alfredrestaurant.gui;

import simcity.alfredrestaurant.AlfredCustomerRole;
import simcity.alfredrestaurant.AlfredHostRole;
import simcity.alfredrestaurant.AlfredTable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AlfredCustomerGui implements AlfredGui {

	private AlfredCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
	// private HostAgent host;
	AlfredRestaurantGUI gui;
	private int xPos, yPos;
	private int xDestination, yDestination;
	private String food = "";
	private enum Command {

		noCommand, GoToSeat, LeaveRestaurant
	};

	private Command command = Command.noCommand;
	public static final int sizeCustomer = 30;
	private static final int defaultPosition = -40;
	private final String IMAGE_FILE = "C:/Users/AlfredKoshan/Documents/GitHub/restaurant_akoshan/customer.jpg";

	// public static final int xTable = 200;
	// public static final int yTable = 250;
	public AlfredCustomerGui(AlfredCustomerRole c, AlfredRestaurantGUI gui) { // HostAgent m) {
		agent = c;
		xPos = defaultPosition;
		yPos = defaultPosition;
		xDestination = defaultPosition;
		yDestination = defaultPosition;
		// maitreD = m;
		this.gui = gui;

		try {
			image = ImageIO.read(new File(IMAGE_FILE));
		} catch (IOException ex) {
			// handle exception...
		}
	}

	public void updatePosition() {
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
		

		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.GoToSeat) {
				agent.msgAnimationFinishedGoToSeat();
			} else if (command == Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out
						.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command = Command.noCommand;
		}
	}

	private BufferedImage image;

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.drawImage(image, xPos, yPos, null);
		// g.fillRect(xPos, yPos, sizeCustomer, sizeCustomer);
		
		//draw food next to table
		if (!food.equals("")){
			AlfredTable table = agent.getTable();
			g.setColor(Color.BLACK);
			g.drawString(food, (int)table.getPosition().getX() + AlfredHostGui.sizeTable, 
					(int)table.getPosition().getY());
		}
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}

	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat() {// later you will map seatnumber to table
								// coordinates.
		Point tablePos = agent.getTable().getPosition();
		xDestination = tablePos.x;
		yDestination = tablePos.y;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = defaultPosition;
		yDestination = defaultPosition;
		command = Command.LeaveRestaurant;
	}

	public void displayAskingFood(String food) {
		this.food = food + "?";		
	}

	public void displayFood() {
		this.food = food.substring(0, food.length() - 1);		
	}

	public void clearFood() {
		this.food = "";		
	}

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	
}
