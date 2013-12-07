package simcity.bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import simcity.ItemOrder;
import simcity.gui.Gui;
import simcity.bank.BankTellerRole;

public class BankTellerGui implements Gui {
	private BankTellerRole role = null;
	boolean releaseIt = false;
	private int xPos = -20, yPos = -20;
	private int xDestination = -20, yDestination = -20;
	private int xHome = -20, yHome = -20;
	BankGui gui;
	public BankTellerGui(BankTellerRole r){
		this.role = r;
		xPos = -20;
		yPos = -20;
		xDestination = 100;
		yDestination = 200;
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
		if(xPos == xDestination && yPos == yDestination){
			{
				if(releaseIt){
					AtDestination();
				}
			
				}
				
			}
	}
	
	public void draw(Graphics2D g){
		g.setColor(Color.YELLOW);
		g.fillRect(xPos, yPos, 30, 30);
		g.drawString("Teller", xPos, yPos);
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
	
	public void GoToManager(){
		xDestination = 38;
		yDestination = 40;
		releaseIt = true;
	}
	
	public void ExitBank(){
		xDestination = -20;
		yDestination = -20;
		releaseIt = true;
	}
	
	public void GoToCustomer(){
		xDestination = 50;
		yDestination = 50;
		releaseIt = true;
		
	}
	public void GoToDesk(){
		xDestination = 110;
		yDestination = 200;
		releaseIt = true;
		
	}
	public void AtDestination(){
		releaseIt = false;
		role.msgAtDestination();
		
	}
	
	public void setGui(BankGui g){
		this.gui = gui;
	}
}