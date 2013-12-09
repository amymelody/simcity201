package simcity.anjalirestaurant.gui;


import java.awt.Graphics2D;

import simcity.Anjalirestaurant.AnjaliCustomerRole;
import simcity.Anjalirestaurant.AnjaliHostRole;

public class AnjaliHostGui implements AnjaliGui {

    private AnjaliHostRole agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    
    public static final int xTable = 200;
    public static final int yTable = 250;
    public static final int x2Table = 100;
    public static final int y2Table = 150;
    public AnjaliHostGui(AnjaliHostRole agent) {
        this.agent = agent;
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

        if (xPos == xDestination && yPos == yDestination
        		& (xDestination >= 0) & (yDestination >= 0)) {
          // agent.msgAtTable();
        }
        if(xPos == -20 && yPos == -20 && xPos == xDestination && yPos == yDestination){
        //agent.msgBackFromTable();
        }
    }
/*
    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }
*/
    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(AnjaliCustomerRole customer, int xLocation, int yLocation) {
        xDestination = xLocation + 20;
        yDestination = yLocation - 20;
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
    
}

