package simcity.anjalirestaurant.gui;
//Extra credit for V2.2, cook moves from fridge to cook area to plate area

import java.awt.Color;
import java.awt.Graphics2D;

import simcity.anjalirestaurant.AnjaliCookRole;
import simcity.gui.Gui;

public class AnjaliCookGui implements Gui {

  private boolean isPresent = false;
  private boolean releaseIt = false;
  private String choice = " ";
  AnjaliRestaurantGui gui;
    private int xPos = 20, yPos = 20;//default waiter position
    private int xDestination = 20, yDestination = 20;//default start position
    
    private int yHomePosition = 0; 
   
    
    public AnjaliCookGui(AnjaliCookRole c, AnjaliRestaurantGui gui){ //HostAgent m) {
		xPos = 0;
		yPos = 250;
		xDestination = 5;
		yDestination = 250;
		//maitreD = m;
		this.gui = gui;
	}

    public void updatePosition() {
        
		 }
       
        
       

    public void draw(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.fillRect(xPos,yPos, 30, 30);
        g.drawString(choice, xPos, yPos);
    }

    public boolean isPresent() { 
        return true;
    }

    
    public void DoGoToCookArea(){
    	xDestination = 10;
    	yDestination = 200;    	
    	releaseIt = true;
    }
    public void DoGoToPlateArea(){
        xDestination = 105;
        yDestination = 200;
        releaseIt = true;
           
        
    }
    public void DoGoToFridge(){
    	xDestination = 70;
    	yDestination = 250;
    	releaseIt = true;
    }
    public void DoGoToHome(){
    	xDestination = 5;
    	yDestination = 250;
    	releaseIt = true;
    	
    }
    public void drawFoodChoice(String food){
    	choice = food;
    }
  

}

