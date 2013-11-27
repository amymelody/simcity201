package simcity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import simcity.PersonAgent;

public class PersonGui implements Gui {
	private PersonAgent agent = null;
	private CityGui gui;

	private static final int width = 10;
	private static final int height = 10;
	private int xPos = 0, yPos = 0;//default Person position
	private int xDestination = 0, yDestination = 0;//default Person destination
	private int buildingX, buildingY;
	
	private enum Command {noCommand, GoToDestination};
	private Command command=Command.noCommand;

	public PersonGui(PersonAgent p, CityGui g) {
		agent = p;
		gui = g;
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
        	if (command == Command.GoToDestination) {
        		agent.msgAtDestination();
        	}
        	command = Command.noCommand;
        }
	}

	public void draw(Graphics2D g) {
		if (command != Command.noCommand) { 
			g.setColor(Color.BLUE);
			g.fillRect(xPos, yPos, width, height);
		}
	}
	
	public void DoGoToDestination(Point p) {
		buildingX = p.x;
		buildingY = p.y;
		xDestination = buildingX;
		yDestination = buildingY;
		command = Command.GoToDestination;
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