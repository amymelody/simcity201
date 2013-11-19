package simcity.anjalirestaurant.gui;


import java.awt.Color;
import java.awt.Graphics2D;

import restaurant.CustomerAgent;
import restaurant.WaiterAgent;

public class WaiterGui implements Gui {

    private CherysWaiterRole agent = null;
  private boolean isPresent = false;
  private boolean releaseIt = false;
  private String choice = " ";
  RestaurantGui gui;
    private int xPos = 20, yPos = 20;//default waiter position
    private int xDestination = 20, yDestination = 20;//default start position
    
    private int yHomePosition = 0; 
   
    
    public WaiterGui(CherysWaiterRole w, RestaurantGui gui, int yPosition){ //HostAgent m) {
		this.yHomePosition = yPosition;
    	agent = w;
		xPos = -20;
		yPos = -20;
		xDestination = 10;
		yDestination = yPosition;
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
        if (xPos == xDestination && yPos == yDestination)
		 {
        	if(releaseIt){
        	AtDestination();
        	}
		 }
       
        
        if(xPos == -20 && yPos == -20 && xPos == xDestination && yPos == yDestination){
       //agent.msgBackFromTable();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos,yPos, 30, 30);
        g.drawString(choice, xPos, yPos);
    }

    public boolean isPresent() {
        return true;
    }
/*
    public void DoGoToHost(){
    	xDestination = 
    	yDestination = 10;
    	
    }
    */
    
    /*public void DoGoToWaitingArea(int waitingAreaX, int waitingAreaY){
    	xDestination = waitingAreaX;
    	yDestination = waitingAreaY;
    	releaseIt = true;
    }
    */
    
    public void DoGoToWaitingCustomer(){
    	xDestination = 20;
    	yDestination = 20;
    	releaseIt = true;
    }
    public void DoBringToTable(CherysCustomerRole c, int xLocation, int yLocation) {
        xDestination = xLocation;
        yDestination = yLocation;
        releaseIt = true;
           
        
    }
    
    public void DoGoHomePosition(){
    	xDestination = 10;
    	yDestination = yHomePosition;
    	releaseIt = true;
    }
    public void setPresent(boolean p){
    	isPresent = p;
    }

    public void DoGoToHost() {
        xDestination = 20;
        yDestination = 20;
        releaseIt = true;
    }

    public void DoGoOnBreak(){
    	xDestination = 300;
    	yDestination = 20;
    	releaseIt = true;
    }
    public void DoGoToCustomer(int xLoc, int yLoc)
    {
    	xDestination = xLoc + 20 ;
    	yDestination = yLoc - 20;
    	releaseIt = true; 
    }
    
    public void DoGoToCookArea()
    {
    	xDestination = 10;
    	yDestination = 200;
    	releaseIt = true;
    	
    }
    public void DoGoToPlateArea(){
    	xDestination = 105;
    	yDestination = 200;
    	releaseIt = true;
    }
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void AtDestination(){	 	
    		agent.msgAtTable();	 
    		releaseIt = false;
    }
    
    public void msgDrawFoodChoice(String c){
		choice = c;
	}

}

