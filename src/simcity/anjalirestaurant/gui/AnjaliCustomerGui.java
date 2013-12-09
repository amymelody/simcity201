package simcity.anjalirestaurant.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.CustomerAgent;

public class AnjaliCustomerGui implements Gui{

	private CherysCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, GoToWaitingArea, LeaveRestaurant};
	private Command command=Command.noCommand;
	private String choice = " " ;
	public static final int xTable = 200;
	public static final int yTable = 250;
	private int xHomePos = 0;
	public AnjaliCustomerGui(CherysCustomerRole c, RestaurantGui gui, int xPos){ //HostAgent m) {
		this.xHomePos = xPos;
		agent = c;
		xPos = -20;
		yPos = -20;
		xDestination = xHomePos;
		yDestination = 20;
		//maitreD = m;
		this.gui = gui;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 30, 30);
		g.drawString(choice, xPos, yPos);
	}

	public boolean isPresent() {
		return true;
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
	
	public void DoGoToWaitingArea(){
		xDestination = 20;
		yDestination = 20;
		command = Command.GoToWaitingArea;
	}

	
	public void DoGoToSeat(int tableX, int tableY) {//later you will map seatnumber to table coordinates.
		xDestination = tableX;
		yDestination = tableY;
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	
	public void msgDrawFoodChoice(String c){
		choice = c;
	}
}
