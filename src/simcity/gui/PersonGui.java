package simcity.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import simcity.PersonAgent;

public class PersonGui implements Gui {
	private PersonAgent agent = null;
	private CityGui gui;

	private int xPos = -20, yPos = -20;//default Person position
	private int xDestination = -20, yDestination = -20;//default Person destination
	private int buildingX, buildingY;

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
        	if (xDestination == buildingX && yDestination == buildingY) {
        		agent.msgAtDestination();
        	}
        }
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
	}
	
	public void DoGoToDestination(Point p) {
		buildingX = p.x;
		buildingY = p.y;
		xDestination = buildingX;
		yDestination = buildingY;
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